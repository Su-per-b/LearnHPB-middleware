/*
  Copyright (c) 2007 Harri Kaimio
  
  This file is part of Photovault.

  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
*/

package org.photovault.imginfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.command.CommandException;
import org.photovault.command.DataAccessCommand;
import org.photovault.common.PVDatabase;
import org.photovault.common.PhotovaultException;
import org.photovault.common.PhotovaultSettings;
import org.photovault.folder.PhotoFolder;
import org.photovault.folder.PhotoFolderDAO;

/**
 Command object for creating a new external volume and associating it with 
 certain folder
 <p>
 The command initializes the given folder as a Photovault volume. If the folder
 already contains a Photovault volume, the command checks if that volume is 
 known to current database. If not, it adds the volume to database and returns
 it. However, if the volume is already known it throws a CommandException.
 @author harri
 */
public class CreateExternalVolume extends DataAccessCommand {
    private static Log log = 
            LogFactory.getLog( CreateExternalVolume.class.getName() );
    
    private File basedir;
    private PhotoFolder topFolder;
    private String volumeName;
    private ExternalVolume volume;
    
    /**
     Create a new command object
     @param basedir Directory in which the external volume is mounted
     @param name Name for the external volume
     @param topFolder Folder that is associated with this volume
     */
    public CreateExternalVolume( File basedir, String name, PhotoFolder topFolder ) {
        this.basedir = basedir;
        this.topFolder = topFolder;
        this.volumeName = name;
    }
    
    public ExternalVolume getCreatedVolume() {
       return volume; 
    }
    
    public void execute() throws CommandException {
        VolumeDAO volDAO = daoFactory.getVolumeDAO();
        PhotoFolderDAO folderDAO = daoFactory.getPhotoFolderDAO();
        VolumeManager vm = VolumeManager.instance();
        // Check if basedir is already initialized as volume. Do not persist 
        // returned value yet.
        try {
            VolumeBase tv = vm.getVolumeAt( basedir, null );
            if ( tv != null ) {
                if ( !(tv instanceof ExternalVolume ) ) {
                    throw new CommandException( String.format( 
                            "%s is a Photovault database, cannot convert to external volume!", 
                            basedir.getAbsolutePath()) );
                }
                volume = (ExternalVolume) volDAO.getVolume( tv.getId() );
                if ( volume == null ) {
                    /*
                     Good, this volume was created by some other Photovault 
                    database instance, so we can just persist it here
                     */
                    volume = (ExternalVolume) volDAO.makePersistent( tv );
                } else {
                    // This volume is already indexed in this database
                    throw new CommandException(
                            String.format( "%s is already an external volume!", 
                            basedir.getAbsolutePath() ) );
                }
            } else {
                /*
                This is a new volume, not previously indexed
                 */
                volume = new ExternalVolume();
                volume.setName( volumeName );
                volDAO.makePersistent( volume );
                try {
                    vm.initVolume( volume, basedir );
                } catch ( PhotovaultException e ) {
                    log.error( "Cannot initialize volume", e );
                    throw new CommandException( "Cannot initialize volume" );
                }
            }
            
            if ( topFolder != null ) {
                volume.setFolder( folderDAO.findById( topFolder.getUuid(), false ) );
            }
            
        } catch ( FileNotFoundException e ) {
            log.error( "Cannot open volume identification file", e );
            throw new CommandException( "Cannot open volume identification file" );
        } catch ( IOException e ) {
            log.error( "Cannot read volume identification file", e );
            throw new CommandException( "Cannot read volume identification file" );            
        }
        // Ensure that this directory will be looked for volumes
        PVDatabase db = PhotovaultSettings.getSettings().
                getCurrentDatabase();
        db.addMountPoint( basedir.getAbsolutePath() );
        try {
            PhotovaultSettings.getSettings().saveDbConfig( db );
        } catch ( IOException ex ) {
            log.error( "Error saving database configuration", ex );
            throw new CommandException( "Error saving database configuration" );
        }
    }

}

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.command.CommandException;
import org.photovault.command.DataAccessCommand;
import org.photovault.dcraw.RawImage;
import org.photovault.image.PhotovaultImage;
import org.photovault.replication.VersionedObjectEditor;

/**
 Command for modifying an {@link ImageFile}. The only operations supported are
 adding or removing a location from the file as attributes of the file are 
 immutable.
 <p>
 This command can be used for creating a new ImageFile as well.
 
 */
public class ModifyImageFileCommand extends DataAccessCommand {
    
    static private Log log = LogFactory.getLog( ModifyImageFileCommand.class );
    private PhotovaultImage img;
    /**
     Creates a new instance of ModifyImageFileCommand that creates a new file
     based on attributes of given file
     @param f The file
     */
    public ModifyImageFileCommand( File f ) {
        file = f;
    }
    
    /**
     Creates a new instance of ModifyImageFileCommand that creates a new file
     based on attributes of given file
     @param f The file     
     @param hash Hash of the file. Since calculating hash takes time, this 
     constructor is faster if hash is already known.
     */
    public ModifyImageFileCommand( File f, byte[] hash ) {
        file = f;
        this.hash = hash;
    }

    public ModifyImageFileCommand( PhotovaultImage img, byte[] hash ) {
        file = img.getImageFile();
        this.hash = hash;
        this.img = img;
    }
    
    /**
     Create a new object for modifying existing ImageFile
     @param file The file to modify
     */
    public ModifyImageFileCommand( ImageFile file ) {
        ifUuid = file.getId();
    }

    File file;
    
    long size;
    
    byte[] hash;
    
    VolumeBase volume;
    
    UUID ifUuid;
    
    ImageFile imageFile;
    
    Set<FileLocation> addedLocations = new HashSet<FileLocation>();

    Set<FileLocation> removedLocations = new HashSet<FileLocation>();
    
    /**
     Photos created while executing the command
     */
    List<PhotoInfo> createdPhotos = new ArrayList<PhotoInfo>();

    public ImageFile getImageFile() {
        return imageFile;
    }

    /**
     Get the photos that were created while exectuing the command.
     @return List of the created photos. The returned instances are persisted
     in command handler's persistence context, i.e. most likkely they are 
     detached while the function call returns.
     */
    public List<PhotoInfo> getCreatedPhotos() {
        return createdPhotos;
    }
    
    public void addLocation( FileLocation l ) {        
        removedLocations.remove( l );
        addedLocations.add( l );
    }
    
    public void removeLocation( FileLocation l ) {
        removedLocations.add( l );
        addedLocations.remove( l );        
    }
    
    public void execute() throws CommandException {
        ImageFileDAO ifDAO = daoFactory.getImageFileDAO();
        PhotoInfoDAO photoDAO = daoFactory.getPhotoInfoDAO();
        if ( ifUuid != null ) {
            imageFile = ifDAO.findById( ifUuid, false );
        } else if ( file != null ) {
            try {
                if ( hash != null ) {
                    imageFile = new ImageFile( file, hash );
                } else {
                    imageFile = new ImageFile( file );
                }
                ifDAO.makePersistent( imageFile );
                // This is a new file, create a PhotoInfo object based on the
                // original
                OriginalImageDescriptor imgDesc = 
                        (OriginalImageDescriptor) imageFile.getImage( "image#0" );
                if ( imgDesc == null ) {
                    throw new CommandException( file.getPath() + " is not an image" );
                }
                UUID uuid = UUID.randomUUID();
                VersionedObjectEditor<PhotoInfo> e = 
                        new VersionedObjectEditor<PhotoInfo>( PhotoInfo.class,
                        uuid, daoFactory.getDTOResolverFactory() );
                PhotoInfo photo = e.getTarget();
                PhotoEditor photoEditor = (PhotoEditor) e.getProxy();
                e.setField( "original", imgDesc );
                if ( img != null ) {
                    /**
                     TODO: this should be implemented using 
                     inversion of control pattern, but I don't want to create 
                     dependency from org.photovault.image to imginfo. Try to figure 
                     out something more maintainable.
                     */
                    photoEditor.setFStop( img.getAperture() );
                    photoEditor.setFilmSpeed( img.getFilmSpeed() );
                    photoEditor.setFuzzyShootTime( 
                            new FuzzyDate( img.getTimestamp(), 0 ) );

                    photoEditor.setShutterSpeed( img.getShutterSpeed() );
                    photoEditor.setFocalLength( img.getFocalLength() );
                    String camera = img.getCamera();
                    if ( camera.length() > PhotoInfo.CAMERA_LENGTH ) {
                        camera = camera.substring( 0, PhotoInfo.CAMERA_LENGTH );
                    }
                    photoEditor.setCamera( camera );
                    if ( img instanceof RawImage ) {
                        photoEditor.setRawSettings( ((RawImage)img).getRawSettings() );
                    }
                }
                e.apply();
                // photo.updateFromFileMetadata( file );
                // photoDAO.makePersistent( photo );
                createdPhotos.add( photo );
            } catch ( Exception e ) {
                log.error( "Error in creating image file: " + e.getMessage(), e );
                throw new CommandException( e.getMessage() );
            }
        }
        for ( FileLocation l : addedLocations ) {
            imageFile.addLocation( l );
        }
        for ( FileLocation l : removedLocations ) {
            imageFile.removeLocation( l );
        }
    }
}

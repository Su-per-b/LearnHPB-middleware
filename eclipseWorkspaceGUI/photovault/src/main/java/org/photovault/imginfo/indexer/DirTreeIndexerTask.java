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

package org.photovault.imginfo.indexer;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.command.CommandException;
import org.photovault.folder.ChangePhotoFolderCommand;
import org.photovault.folder.CreatePhotoFolderCommand;
import org.photovault.folder.DeletePhotoFolderCommand;
import org.photovault.folder.PhotoFolder;
import org.photovault.imginfo.ExternalVolume;
import org.photovault.taskscheduler.BackgroundTask;

/**
 This background task replicates a file system directory tree into Photovault 
 {@link PhotoFolder} hierarchy or changes an existing fodler hierarchy to 
 match a directory tree. It is used as the first step of indexing an
 external volume.
 */
public class DirTreeIndexerTask extends BackgroundTask {
    private static Log log = LogFactory.getLog( DirTreeIndexerTask.class.getName() );
    
    private File topDir;
    private PhotoFolder topFolder;
    private ExternalVolume vol;
    private int folderCount = 0;
    private int newFolderCount = 0;
    private int deletedFolderCount = 0;
    private boolean createDirIndexers;
    private LinkedList<DirectoryIndexer> dirIndexers = 
            new LinkedList<DirectoryIndexer>();

    /**
     Constructs a new tree indexer
     @param topDir The top directory of the tree to index
     @param topFolder The folder corresponding to topDir
     */
    public DirTreeIndexerTask( File topDir, PhotoFolder topFolder,
            ExternalVolume vol, boolean createDirIndexers ) {
        this.topDir = topDir;
        this.topFolder = topFolder;
        this.vol = vol;
        this.createDirIndexers = createDirIndexers;
    }
    
    /**
     Constructs a new tree indexer
     @param topDir The top directory of the tree to index
     @param topFolder The folder corresponding to topDir
     */    
    public DirTreeIndexerTask( File topDir, PhotoFolder topFolder,
            ExternalVolume vol ) {
        this( topDir, topFolder, vol, false );
    }
    /**
     Run the task
     */
    @Override
    public void run() {
        if ( topFolder.getExternalDir() == null ) {
            try {
                ChangePhotoFolderCommand cmd = new ChangePhotoFolderCommand( topFolder );
                cmd.setExtDir( vol, "" );
                cmdHandler.executeCommand( cmd );
                topFolder = (PhotoFolder) session.merge( cmd.getChangedFolder() );
            } catch ( CommandException ex ) {
                log.error( "Error associating top folder with directory: ", ex );
            }
        }
        indexDirectory( topDir, topFolder );
        
    }

    /**
     Synchronize a subtree to given folder hierarchy.
     @param dir The top directory
     @param folder the corresponding top folder
     */
    private void indexDirectory( File dir, PhotoFolder folder ) {

        if ( createDirIndexers ) {
            dirIndexers.add( new DirectoryIndexer( dir, folder, vol ) );
        }

        Map<String, PhotoFolder> folders = new HashMap<String, PhotoFolder>();
        for ( PhotoFolder f : folder.getSubfolders() ) {
            folders.put( f.getName(), f );
        }
        File[] dirEntries = dir.listFiles();
        for ( File d : dirEntries ) {
            if ( d.isDirectory() && !d.getName().equals( ".photovault_volume" ) ) {
                folderCount++;
                PhotoFolder f = null;
                // Is there an existing folder for this directory?
                if ( folders.containsKey( d.getName() ) ) {
                    // Yep, this one is OK! Remove for unprocessed folders
                    f = folders.remove( d.getName() );
                } else {
                    // No, this must be created
                    try {
                        CreatePhotoFolderCommand cmd = 
                                new CreatePhotoFolderCommand( folder, d.getName(), "" );
                        StringBuffer pathBuf = 
                                new StringBuffer( folder.getExternalDir().getPath() );
                        if ( pathBuf.length() > 0 ) {
                            pathBuf.append( "/" );
                        }
                        pathBuf.append( d.getName() );
                        cmd.setExtDir( folder.getExternalDir().getVolume(), 
                                pathBuf.toString() );
                        cmdHandler.executeCommand( cmd );
                        f = (PhotoFolder) session.merge( cmd.getCreatedFolder() );
                        newFolderCount++;
                    } catch ( CommandException ex ) {
                        log.error( "Error while creating folder", ex );
                    }
                }
                indexDirectory( d, f );
            }
        }

        // Delete subfolders that were not found
        for ( PhotoFolder f : folders.values() ) {
            try {
                DeletePhotoFolderCommand deleteCmd =
                        new DeletePhotoFolderCommand( f.getUuid() );
                cmdHandler.executeCommand( deleteCmd );
                deletedFolderCount++;
            } catch ( CommandException ex ) {
                log.error( "Error while deleting folder: ", ex );
            }
        }
    }

    /**
     Get the total number of directories checked
     @return Number of checked directories
     */
    public int getFolderCount() {
        return folderCount;
    }

    /**
     Get the number of new folders created
     
     @return Number of new folders created
     */
    public int getNewFolderCount() {
        return newFolderCount;
    }

    /**
     Get the number of folders deletes as he corresponding directory was not 
     found
     @return NUmber of deleted folders
     */
    public int getDeletedFolderCount() {
        return deletedFolderCount;
    }

    /**
     Get the top directory where the indexing starts
     @return Top directory
     */
    public File getTopDir() {
        return topDir;
    }

    /**
     Get the top folder that corresponds to top directory
     @return Top folder
     */
    public PhotoFolder getTopFolder() {
        return topFolder;
    }

    /**
     Get the external volume we are indexing
     @return volume
     */
    public ExternalVolume getVol() {
        return vol;
    }

    /**
     Get the directory indexers for all found directoiries
     @return
     */
    public LinkedList<DirectoryIndexer> getDirIndexers() {
        return dirIndexers;
    }
}

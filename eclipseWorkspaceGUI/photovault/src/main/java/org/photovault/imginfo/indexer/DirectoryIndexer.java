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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.photovault.command.CommandException;
import org.photovault.command.CommandHandler;
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.folder.CreatePhotoFolderCommand;
import org.photovault.folder.DeletePhotoFolderCommand;
import org.photovault.folder.PhotoFolder;
import org.photovault.imginfo.ChangePhotoInfoCommand;
import org.photovault.imginfo.ExternalVolume;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.persistence.HibernateUtil;
import org.photovault.swingui.Photovault;
import org.photovault.taskscheduler.BackgroundTask;

/**
 This class keeps track of state when indexing a directory that belongs to an
 external volume. It compares the directory state to database, creates or deletes 
 changed subfolders and creates {@link IndexFileTask} objects for each file in 
 the directory so that they can be indexed.
 <p>
 This class itself is not a {@link TaskProducer} so it should be instantiated by
 a task producer that coordinates the whole indexing operation.
 @author Harri Kaimio
 @since 0.6.0
 */
public class DirectoryIndexer {
    static Log log = LogFactory.getLog( DirectoryIndexer.class.getName() );
    
    /**
     Count how many instances of a certain photo were found during indexing this
     direwctory. Maps photo ID to number of found instances.
     */
    private HashMap<UUID, Integer> photoInstanceCounts;
    
    /**
     Ids of photos found during indexing that were not previously 
     members of corresponding folder.
     */
    private Set<UUID> newPhotos = new HashSet<UUID>();
    
    /**
     UUIDs of those folders that were not found in the volume (i.e. the corresponding
     directories were deleted or moved.
     */
    private HashSet<UUID> foldersNotFound;
    
    /**
     Files currently in the directory
     */
    List<File> files = new ArrayList<File>(  );
    
    /**
     Iterator to files list, used by getNextFileIndexer()
     */
    Iterator<File> fileIter;
    
    /**
     Indexers created for each subdirectory of this directory.
     */
    List<DirectoryIndexer> subdirIndexers =
            new ArrayList<DirectoryIndexer>(  );
    
    /**
     Has this indexer been initialized
     */
    boolean isInitialized = false;

    /**
     True if the indexing operation has been finalized, false otherwise
     */
    private boolean isFinalized = false;
    
    /**
     PhotoFolder that corresponds to this directory
     */
    private PhotoFolder folder;
    /**
     The directory that is indexed.
     */
    private File dir;
    
    /**
     Volume the directory belongs to.
     */
    private ExternalVolume volume;
    
    /**
     Command handler used to execute commands for changing the folder hierarchy
     @todo This cause currently an illegal dependency to swingui!!!
     */
    PhotovaultCommandHandler commandHandler = null;
    
    /**
     Count of files that have not yet been indexed.
     */
    int filesLeft = -1;
        
    /**
     Create a new directory indexer
     @param dir The directory to index
     @param folder The corresponding folder in Photovault database
     @param vol The volume of dir
     */
    public DirectoryIndexer( File dir, PhotoFolder folder, ExternalVolume vol ) {
        this.dir = dir;
        this.folder = folder;
        this.volume = vol;        
    }

    /**
     Get the directory that is indexed by this indexer.
    @return The directory.
     */
    public File getDirectory() {
        return dir;
    }
    
    /**
     Set the command handler that will be used for directory manipulations
     @param ch the command handler
     @deprecated Some more sophisticated way of passing command handler around 
     is needed.
     */
    public void setCommandHandler( PhotovaultCommandHandler ch ) {
        commandHandler = ch;
    }
        
    /**
     Get indexer for next unindexed file in this directory
     @return Indexer for file or <code>null</code> if indexers have been created
     for all files.
     */
    public BackgroundTask getNextFileIndexer( ) {
        if ( !isInitialized ) {
            return new BackgroundTask() {

                @Override
                public void run() {
                    initialize( cmdHandler );
                }

            };
        }
        while ( fileIter.hasNext(  ) ) {
            File f = fileIter.next(  );
            if ( f.canRead(  ) ) {
                IndexFileTask t =
                        new IndexFileTask( f, folder, volume, this );
                return t;
            }
        }
        
        // No files left, complete indexing if it has not been done
        
        if ( !isFinalized ) {
            isFinalized = true;
            return new BackgroundTask() {

                @Override
                public void run() {
                    allFilesIndexed( cmdHandler );
                }
                
            };
        }
        
        // everything has been done, we can quit.
        return null;
    }
    
    /**
     Get indexers for each subdirectory of this directory
     @return list of indexers for each subdirectory of this one.
     @deprecated This method is deprecated, as DirectoryIndexer should be accessed
     only from background job scheduler.
     */
    public List<DirectoryIndexer> getSubdirIndexers() {
        if ( !isInitialized ) {
            initialize( commandHandler );
        }
        return subdirIndexers;
    }

    /**
     Get information how far we are indexing this directory.
     @return Percentage of files in this directory that have been indexed by
     this indexer. Integer value between 0..100.
     */
    public int getPercentComplete() {
        int fileCount = files.size();
        if ( fileCount == 0 ) {
            return 100;
        }
        return ( fileCount - filesLeft ) * 100 / fileCount; 
    }

    /**
     Initialize data structures: read directory content, compare for folder,
     delete subfodlers that do not have matching directory and add subfolders 
     for new directories.
     */
    private void initialize( CommandHandler cmdHandler ) {
        /**
         Maintain information how many instances for the photos that were previously 
         added to the folder is found
         */
        photoInstanceCounts = new HashMap<UUID,Integer>();
        foldersNotFound = new HashSet<UUID>();
        if ( folder != null ) {
            folder = 
                    (PhotoFolder) HibernateUtil.getSessionFactory().getCurrentSession().
                    get(PhotoFolder.class, folder.getUuid() );
            for ( PhotoInfo photo : folder.getPhotos() ) {
                photoInstanceCounts.put( photo.getUuid(), new Integer( 0 ) );
            }
            for ( PhotoFolder f : folder.getSubfolders() ) {
                foldersNotFound.add(f.getUuid() );
            }
        }
        
        File[] entries = dir.listFiles(  );
        List<File> subdirs = new ArrayList<File>(  );
        // Count the files
        filesLeft = 0;
        int subdirCount = 0;
        for ( File entry : entries ) {
            if ( entry.isDirectory() ) {
                if ( !entry.getName().equals( ".photovault_volume" ) ) {
                    subdirCount++;
                    subdirs.add( entry );
                }
            } else {
                filesLeft++;
                files.add( entry );
            }
        }
        fileIter = files.iterator();
        
        
        // Check the subfolders
        // Index now all subdirectories
        for ( File subdir : subdirs ) {
            // Create the matching folder
            PhotoFolder subfolder = null;
            if ( folder != null ) {
                String folderName = subdir.getName();
                if ( folderName.length() > PhotoFolder.NAME_LENGTH ) {
                    folderName = folderName.substring( 0, PhotoFolder.NAME_LENGTH );
                }
                subfolder = findSubfolderByName( folder, folderName );
                if ( subfolder == null ) {
                    CreatePhotoFolderCommand createFolder =
                            new CreatePhotoFolderCommand( folder, folderName,
                            "imported from " + subdir.getAbsolutePath() );
                    StringBuffer pathBuf =
                            new StringBuffer( folder.getExternalDir().getPath() );
                    if ( pathBuf.length() > 0 ) {
                        pathBuf.append( "/" );
                    }
                    pathBuf.append( subdir.getName() );
                    createFolder.setExtDir( volume, pathBuf.toString() );
                    try {
                        cmdHandler.executeCommand( createFolder );
                    } catch (CommandException ex) {
                        ex.printStackTrace();
                    }
                    subfolder = createFolder.getCreatedFolder();
                    // newFolderCount++;
                } else {
                    foldersNotFound.remove( subfolder.getUuid(  ) );
                }
                subdirIndexers.add( 
                        new DirectoryIndexer( subdir, subfolder, volume ) );
            }
        }
        // If there were subfolders with no matching directory, delete them
        for ( UUID folderId : foldersNotFound ) {
            try {
                DeletePhotoFolderCommand deleteCmd =
                        new DeletePhotoFolderCommand( folderId );
                cmdHandler.executeCommand( deleteCmd );
            } catch ( CommandException ex ) {
                log.error( "Error while deleting folder: " + ex.getMessage() );
            }
            
        }
        isInitialized = true;
    }

    /**
     Called by a file indexer to inform that it has completed its job.
     */
    synchronized void fileIndexerCompleted( IndexFileTask fileIndexer ) {
        filesLeft--;
        // Keep track of the files we have found
        int previousCount = 0;        
        for ( PhotoInfo p : fileIndexer.getPhotosFound() ) {
            if ( photoInstanceCounts.containsKey( p.getUuid() ) ) {
                previousCount = photoInstanceCounts.get( p.getUuid() );
            } else {
                // The photo was not yet in this folder, add it to those that 
                // we must add later.
                newPhotos.add( p.getUuid() );
            }
            photoInstanceCounts.put( p.getUuid(), previousCount+1 );
        }
//        if ( filesLeft == 0 ) {
//            allFilesIndexed();
//        }

    }

    
    /**
     Called after all file indexers have been executed. Remove photos that were 
     not found.
     */
    private void allFilesIndexed( CommandHandler cmdHandler ) {
        /*
         Check if some of the photos that were in folder before were not found in 
         this directory
         */
        
        Set<UUID> photoIdsNotFound = new HashSet<UUID>();
        for ( Map.Entry<UUID, Integer> e : photoInstanceCounts.entrySet() ) {
            if ( e.getValue() == 0 ) {
                photoIdsNotFound.add( e.getKey() );
                log.debug( "photo " + e.getKey() + " not found during indexing" );
            }
        }        
        if ( photoIdsNotFound.size() > 0 ) {
            ChangePhotoInfoCommand removeFromFolderCmd = new ChangePhotoInfoCommand( photoIdsNotFound );
            removeFromFolderCmd.removeFromFolder( folder );
            try {
                log.debug( "Deleting unseen photos" );
                cmdHandler.executeCommand( removeFromFolderCmd );
            } catch (CommandException ex) {
                ex.printStackTrace();
            }
        }
        if ( newPhotos.size() > 0 ) {
            ChangePhotoInfoCommand addToFolderCmd = new ChangePhotoInfoCommand( newPhotos );
            addToFolderCmd.addToFolder( folder );
            try {
                log.debug( "Deleting unseen photos" );
                cmdHandler.executeCommand( addToFolderCmd );
            } catch (CommandException ex) {
                ex.printStackTrace();
            }
            
        }
    }
    

    /**
     Finds a subfolder with given name. This is classified as protected so that also
     test code can use the same method - however, this service should really be offered
     by PhotoFolder API.
     @param folder The fodler to search
     @param name The name to look for
     @return The subfolder with matching name or <code>null</code> if none found.
     If there are multiple subfolders with the matching name an arbitrary one will
     be returned.
     */
    private PhotoFolder findSubfolderByName(PhotoFolder folder, String name ) {
        PhotoFolder subfolder = null;
        for ( int n = 0; n < folder.getSubfolderCount(); n++ ) {
            PhotoFolder candidate = folder.getSubfolder( n );
            if ( name.equals( candidate.getName() ) ) {
                subfolder = candidate;
                break;
            }
        }
        return subfolder;
    }    
}

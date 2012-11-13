/*
 Copyright (c) 2007 Harri Kaimio

 This file is part of Photovault.
 
 Photovault is free software; you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.
 
 Photovault is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even therrore implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with Photovault; if not, write to the Free Software Foundation,
 Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

package org.photovault.swingui.indexer;

import org.photovault.command.CommandHandler;
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.folder.ExternalDir;
import org.photovault.folder.PhotoFolder;
import org.photovault.imginfo.indexer.DirTreeIndexerTask;
import org.photovault.imginfo.indexer.DirectoryIndexer;
import org.photovault.swingui.Photovault;
import org.photovault.swingui.taskscheduler.SwingWorkerTaskScheduler;
import org.photovault.swingui.taskscheduler.TaskPriority;
import org.photovault.taskscheduler.BackgroundTask;
import org.photovault.taskscheduler.TaskProducer;
import org.photovault.taskscheduler.TaskScheduler;

/**
 This indexer is used to synchronize content of current folder with the 
 corresponding directory in external volume. When given a folder, it starts
 to index it in background and continues indexing until the operation completes
 or a new directory is given. If the latter happens first, the ongoing indexing
 operation is cancelled and a new one started.
 
 */
public class CurrentFolderIndexer implements TaskProducer {
    private PhotovaultCommandHandler cmdHandler;

    
    DirectoryIndexer currentIndexer = null;
    
    PhotoFolder currentFolder = null;
    
    /**
     State of the indexing
     */
    public enum IndexingPhase {
        /**
         No folder is being indexed
         */
        INACTIVE,
        /**
         A folder is waiting to be indexed but the operation has not yet been 
         started.
         */
        NOT_STARTED,
        /**
         Synchronizing of directory tree hierarchy is ongoing.
         */
        TREE_INDEX,
        /**
         Indexing the files in the directory is ongoing.
         */
        FILE_INDEX,
        
        /**
         Indexing the directory is complete
         */
        COMPLETE
    };

    IndexingPhase phase = IndexingPhase.INACTIVE;
    
    TaskScheduler sched = null;
    
    /**
     Create a new indexer
     @param sched Scheduler that is used for executing the created tasks
     @param cmdHandler Command handler that this indexer can use
     */
    public CurrentFolderIndexer( TaskScheduler sched, 
            PhotovaultCommandHandler cmdHandler ) {
        this.sched = sched;
        this.cmdHandler = cmdHandler;
    }
    
    public void CurrentFolderIndexer( ) {}
    
    /**
     Start indexing a new folder (and cancel any ongoing indexing operation
     @param folder The folder to index.
     */
    public void updateFolder( PhotoFolder folder ) {
        ExternalDir ed = folder.getExternalDir();
        if ( ed != null ) {
            currentFolder = folder;
            currentIndexer = null;
            phase = IndexingPhase.NOT_STARTED;
            if ( sched != null ) {
                sched.registerTaskProducer( this,
                        TaskPriority.INDEX_CURRENT_DIR.getPriority() );
            }
        }
        
    }
    
    public BackgroundTask requestTask() {
        BackgroundTask ret = null;
        ExternalDir ed = null;
        switch ( phase ) {
        case NOT_STARTED:
            ed = currentFolder.getExternalDir();
            ret = new DirTreeIndexerTask( ed.getDirectory(), currentFolder, ed.getVolume() );
            phase = IndexingPhase.TREE_INDEX;
            break;
        case TREE_INDEX:
            // The directory tree has been updated, index the files in this directory
            ed = currentFolder.getExternalDir();
            currentIndexer = new DirectoryIndexer(ed.getDirectory(), currentFolder, ed.getVolume() );
            currentIndexer.setCommandHandler( cmdHandler );
            phase = IndexingPhase.FILE_INDEX;
            ret = currentIndexer.getNextFileIndexer();
            break;
        case FILE_INDEX:
            ret = currentIndexer.getNextFileIndexer();
            break;
        }
        
        if ( ret == null ) {
            phase = IndexingPhase.COMPLETE;
        }
        
        return ret;
    }


    public IndexingPhase getState() {
        return phase;
    }

    /**
     * Get the folder that is currently being indexed
     * @return The folder that is currently being indexed. If state is
     * {@link IndexingPhase.INACTIVE} or {@link IndexingPhase.COMPLETE} return
     * value is undefined.
     */
    public PhotoFolder getCurrentFolder() {
        return currentFolder;
    }

    /**
     * Get the progress of indexing current directory
     * @return How many percents of the files in the current directory have been
     * indexed (0..100)
     */
    public int getPercentComplete() {
        switch ( phase ) {
            case INACTIVE:
            case NOT_STARTED:
            case TREE_INDEX:
                return 0;
            case FILE_INDEX:
                return currentIndexer.getPercentComplete();
            case COMPLETE:
                return 100;
        }
        return 0;
    }

}

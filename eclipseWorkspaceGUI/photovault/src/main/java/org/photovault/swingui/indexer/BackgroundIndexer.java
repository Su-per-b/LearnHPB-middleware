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

package org.photovault.swingui.indexer;

import java.io.File;
import java.util.LinkedList;
import org.photovault.folder.PhotoFolder;
import org.photovault.imginfo.ExternalVolume;
import org.photovault.imginfo.indexer.DirTreeIndexerTask;
import org.photovault.imginfo.indexer.DirectoryIndexer;
import org.photovault.taskscheduler.BackgroundTask;
import org.photovault.taskscheduler.TaskProducer;

/**
 Indexer that can (re)index an external volume or part of it in background thread.
 The class acts as a task producer and creates {@link IndexFileTask} objects
 for all the files in the directory.
 
 */
public class BackgroundIndexer implements TaskProducer {

    /**
     Top directory for the indexed directory tree
     */
    private File dir;
    /**
     External volume this director belongs to
     */
    private ExternalVolume vol;
    /**
     Folder that matches dir
     */     
    private PhotoFolder topFolder;
    /**
     If <code>true</code> index als subdirectories of dir.
     */
    private boolean indexSubdirs = false;
    
    /**
     Indexers for non-indexed subdirectories
     */
    LinkedList<DirectoryIndexer> indexers = null;
    
    /**
     Currently active directory indexer
     */
    DirectoryIndexer currentIndexer = null;
    
    DirTreeIndexerTask treeIndexer = null;

     /**
     Create a new BackgroundIndexer.
     @param dir directory to index
     @param vol Volume in which the directory is
     @param folder Folder that corresponds to dir
     @param indexSubdirs Should also subdirectories be indexed?
     */
    public BackgroundIndexer( File dir,
            ExternalVolume vol, PhotoFolder folder, boolean indexSubdirs ) {
        this.dir = dir;
        this.vol = vol;
        this.topFolder = folder;
    }
    
    /**
     Give a new task to task scheduler
     @return New IndexFileTask of <code>null</code> if the indexing is complete.
     */
    public BackgroundTask requestTask( ) {
        if ( indexers == null ) {
            // We are not yet initialized
            if ( treeIndexer == null ) {
                /*
                 We have not even started to inizialize, start by analyzing the
                 directory tree structure
                 */
                treeIndexer = new DirTreeIndexerTask( dir, topFolder, vol, true );
                return treeIndexer;
            } else {
                /*
                 Yep, the tree has been analyzed, continue with 
                 indexing the files.
                 */
                indexers = treeIndexer.getDirIndexers();
                treeIndexer = null;
            }
        }
        
        if ( currentIndexer != null ) {
            BackgroundTask task = currentIndexer.getNextFileIndexer();
            if ( task != null ) {
                return task;
            }
        }

        if ( indexers.size() > 0 ) {
            currentIndexer =  indexers.removeFirst(  );
        } else {
            currentIndexer = null;
        }
        return currentIndexer != null ? requestTask(  ) : null;
    }
    
    /**
     Get a status message that describes the current progress of indexing
     
     @return A status message
     */
    public String getStatusMessage() {
        if ( currentIndexer == null ) {
            return "Indexing complete";            
        }        
        return "Indexing " + currentIndexer.getDirectory().getAbsolutePath();
    }
    
    /**
     Get estimated percentage of work done
     @return Number from 0 to 100.
     */
    public int getPercentComplete() {
        return currentIndexer != null ? currentIndexer.getPercentComplete() : null;
    }
}
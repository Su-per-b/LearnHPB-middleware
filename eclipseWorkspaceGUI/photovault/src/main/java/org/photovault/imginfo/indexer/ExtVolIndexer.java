/*
  Copyright (c) 2006-2007 Harri Kaimio
 
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.context.ManagedSessionContext;
import org.photovault.command.CommandException;
import org.photovault.command.CommandHandler;
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.folder.PhotoFolder;
import org.photovault.imginfo.CopyImageDescriptor;
import org.photovault.imginfo.CreateCopyImageCommand;
import org.photovault.imginfo.ExternalVolume;
import org.photovault.imginfo.ImageDescriptorBase;
import org.photovault.imginfo.OriginalImageDescriptor;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.Volume;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.taskscheduler.BackgroundTask;

/**
 ExtVolIndexer implements a background task for indexing all files in an
 external volume. It can be used either to index a new external volume or
 to resync Photovault index with an existing external volume if it is probable
 that the volume content has been modified by user.
 
 */
public class ExtVolIndexer implements Runnable {

    static private Log log = LogFactory.getLog( ExtVolIndexer.class.getName() );
    
    /**
     Creates a new instance of ExtVolIndexer
     @param vol The volume to be indexed
     */
    public ExtVolIndexer( ExternalVolume vol ) {
        volume = vol;
        topFolderId = vol.getFolderId();
    }
    
    /** The volume that is indexed by this instance */
    ExternalVolume volume = null;
    
    CommandHandler commandHandler = null;
    
    public void setCommandHandler( CommandHandler ch ) {
        commandHandler = ch;
    }
    
    /**
     Folder used as top of created folder gierarchy or <code>null</code>
     if no folders should be created
     */
    private UUID topFolderId = null;
    
    private ExtVolIndexerEvent currentEvent = null;
    
    /**
     Etimate of current progress in indexing operation. Values 0..100
     */
    private int percentComplete;
    
    /**
     The indexer can create a folder hierarchy that matches the directory hierarchy
     in external volume if user so wants. If there already is a directory with
     same name under this folder the indexer uses it, otherwise it creates new
     folderst for each directory.
     @param topFolder used as top of the structure or <code>null</code> if no folders
     should be created.
     */
    public void setTopFolder(PhotoFolder topFolder) {
        this.topFolderId = topFolder.getUuid();
        if ( volume != null ) {
            volume.setFolderId( topFolder.getUuid() );
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
    
    /**
     Run the actual indexing operation.
     */
    public void run() {
        Session photoSession = null;
        Session oldSession = null;
        try {
            photoSession = HibernateUtil.getSessionFactory().openSession();
            oldSession = ManagedSessionContext.bind( (org.hibernate.classic.Session) photoSession);
            
            startTime = new Date();
            PhotoFolder topFolder = null;
            if ( topFolderId != null ) {
                DAOFactory daoFactory = DAOFactory.instance( HibernateDAOFactory.class );
                topFolder = daoFactory.getPhotoFolderDAO().findById( topFolderId, false );
            }
            DirectoryIndexer topIndexer = new DirectoryIndexer( volume.getBaseDir(), topFolder, volume );
            indexDirectory( topIndexer, 0, 100 );
            notifyListenersIndexingComplete();
        } catch( Throwable t ) {
            StringWriter strw = new StringWriter();
            strw.write( "Error indexing " + volume.getBaseDir().getAbsolutePath() );
            strw.write( "\n" );
            t.printStackTrace( new PrintWriter( strw ) );
            log.error( strw.toString() );   
            log.error( t );
            notifyListenersIndexingError( t.getMessage() );
        } finally {
            if ( photoSession != null ) {
                photoSession.close();
            }
                ManagedSessionContext.bind( (org.hibernate.classic.Session) oldSession);                
        }
    }
    
    // Listener support
    
    /**
     Set of listreners that are notified about progress in indexing
     */
    private Set<ExtVolIndexerListener> listeners = 
            new HashSet<ExtVolIndexerListener>();
    
    /**
     Add a new listener to the set which will be notified about new indexing
     events.
     @param l The listener object
     */
    public void addIndexerListener( ExtVolIndexerListener l ) {
        listeners.add( l );
    }
    /**
     Remove a new listener from the set which will be notified about new indexing
     events.
     @param l The listener object that will be removed.
     */
    public void removeIndexerListener( ExtVolIndexerListener l ) {
        listeners.remove( l );
    }

    /**
     Index a directory by running all {@IndexFileTask}s produced by a 
     DirectoryIndexer.
     @param indexer The directoryIndexer.
     @param startPercent This is used for determining the completeness of the
     indexing operation. Based on indexing of upper level directories is has been
     estimated that indexing this subhierarchy will advance the indexing operation
     from startPErcent to endPercent. indexDirector will subsequently divide this
     to the operations it performs.
     @param endPercent See above.     
    */
    private void indexDirectory( DirectoryIndexer indexer, int startPercent, int endPercent ) {
        BackgroundTask fileTask = null;
        indexer.setCommandHandler( (PhotovaultCommandHandler) commandHandler );
        int subdirCount = indexer.getSubdirIndexers().size();
        while ( (fileTask = indexer.getNextFileIndexer(  )) != null ) {
            Session photoSession =
                    HibernateUtil.getSessionFactory(  ).openSession(  );
            Session oldSession =
                    ManagedSessionContext.bind( (org.hibernate.classic.Session) photoSession );
            fileTask.setSession( photoSession );
            fileTask.setCommandHandler( commandHandler );
            fileTask.run(  );
            ExtVolIndexerEvent ev =
                    new ExtVolIndexerEvent( this );
            switch ( ((IndexFileTask)fileTask).getResult(  ) ) {
                case NEW_FILE:
                    ev.setResult( ExtVolIndexerEvent.RESULT_NEW_PHOTO );
                    newInstanceCount++;
                    newPhotoCount++;
                    break;
                case NEW_LOCATION:
                    ev.setResult( ExtVolIndexerEvent.RESULT_NEW_INSTANCE );
                    newInstanceCount++;
                    break;
                case NOT_IMAGE:
                    ev.setResult( ExtVolIndexerEvent.RESULT_NOT_IMAGE );
                    break;
                case ERROR:
                    ev.setResult( ExtVolIndexerEvent.RESULT_ERROR );
                    break;
            }
            percentComplete =
                    startPercent +
                    (endPercent - startPercent) * 
                    indexer.getPercentComplete(  ) / (100 * (subdirCount + 1));
            notifyListeners( ev );
            photoSession.close(  );
            ManagedSessionContext.bind( (org.hibernate.classic.Session) oldSession );
        }
        int subdir = 0;
        for ( DirectoryIndexer subdirIndexer : indexer.getSubdirIndexers(  ) ) {
            indexDirectory( subdirIndexer, percentComplete,
                    startPercent +
                    (endPercent - startPercent) * (subdir + 2) /
                    (subdirCount + 1) );
        }
    }

    /**
     Notifies all listeners that a new file has been indexed.
     @param e The indexer event object that describes the event
     */
    private void notifyListeners( ExtVolIndexerEvent e ) {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            ExtVolIndexerListener l = (ExtVolIndexerListener) iter.next();
            l.fileIndexed( e );
        }
    }
    
    /**
     Notify all listeners that indexing operation for the whole volume has
     been completed.
     */
    private void notifyListenersIndexingComplete() {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            ExtVolIndexerListener l = (ExtVolIndexerListener) iter.next();
            l.indexingComplete( this );
        }
    }
    
    /**
     Notify all listeners that an error happened during indexing
     */
    private void notifyListenersIndexingError( String message ) {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            ExtVolIndexerListener l = (ExtVolIndexerListener) iter.next();
            l.indexingError( message );
        }
    }
    
    
    
    // Statistics
    
    /**
     Count of new photos created during indexing
     */
    private int newPhotoCount = 0;
    /**
     Count of new photo instances created during indexing
     */
    private int newInstanceCount = 0;
    
    /**
     Count of new folders created during indexing
     */
    private int newFolderCount = 0;
    
    /**
     Total count of files checked while indexing
     */
    private int indexedFileCount = 0;
    
    /**
     Get the count of new photos created during indexing operation.
     */
    public int getNewPhotoCount() {
        return newPhotoCount;
    }
    
    /**
     Get the count of new photo instances created during indexing operation.
     */
    public int getNewInstanceCount() {
        return newInstanceCount;
    }
    
    /**
     Get the count of new folders created during indexing operation.
     */
    public int getNewFolderCount() {
        return newFolderCount;
    }
    
    /**
     Get the total number of files indexed during the operation.
     */
    public int getIndexedFileCount() {
        return indexedFileCount;
    }
    
    /**
     Returns info on how far the indexing operation has progresses as percentage
     complete
     @return Value in range 0..100;
     */
    public int getPercentComplete() {
        return percentComplete;
    }
    
    Date startTime = null;
    
    /**
     Get the starting time of the last started indexing operation.
     @return Starting time or <code>null</code> if no indexing operation started
     */
    public Date getStartTime() {
        return (startTime != null) ? (Date) startTime.clone() : null;
    }
    /**
     Utility method to find out {@link PhotoInfo}s based on given image
     @param img The image
     @return Set of photos that are based in this image (if it is original) or
     its original.
     */
    private Set<PhotoInfo> getPhotosBasedOnImage(ImageDescriptorBase img) {
        if ( img instanceof OriginalImageDescriptor ) {
            return ((OriginalImageDescriptor)img).getPhotos();
        }
        return ((CopyImageDescriptor)img).getOriginal().getPhotos();
    }
}



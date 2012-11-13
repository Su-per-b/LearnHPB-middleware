/*
  Copyright (c) 2006 Harri Kaimio
  
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

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.hibernate.Session;
import org.photovault.imginfo.ExternalVolume;
import org.photovault.imginfo.VolumeBase;
import org.photovault.imginfo.VolumeDAO;
import org.photovault.imginfo.indexer.IndexFileTask;
import org.photovault.imginfo.indexer.IndexingResult;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.swingui.PhotoViewController;
import org.photovault.swingui.Photovault;
import org.photovault.swingui.StatusChangeEvent;
import org.photovault.swingui.StatusChangeListener;
import org.photovault.swingui.framework.AbstractController;
import org.photovault.swingui.framework.DataAccessAction;
import org.photovault.swingui.framework.DefaultAction;
import org.photovault.swingui.taskscheduler.TaskPriority;
import org.photovault.swingui.taskscheduler.BackgroundTaskListener;
import org.photovault.swingui.taskscheduler.SwingWorkerTaskScheduler;
import org.photovault.taskscheduler.BackgroundTask;
import org.photovault.taskscheduler.TaskProducer;

/**
 * Class control the updating of indexed external volumes. When this action is 
 * initiated it starts to go through all external volumes sequentially and updates
 *their indexes using separate threads.
 * TODO: This class should be refactored more aggressively after migrating to
 * {@link BackgroundTaskScheduler}
 *
 * @author Harri Kaimio
 */
public class UpdateIndexAction extends DefaultAction implements BackgroundTaskListener {
    
    /** Creates a new instance of UpdateIndexAction */
    public UpdateIndexAction( PhotoViewController ctrl, String text, ImageIcon icon, String desc,
            int mnemonic) {
        super( text, icon );
        this.ctrl = ctrl;
	putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, new Integer( mnemonic) );
    }
    
    /**
     Controller owning this action
     */
    PhotoViewController ctrl;
    
    /**
     * List of volumes to index. After indexing of a volume has started it will be 
     * removed from this list
     */
    List<ExternalVolume> volumes = null;
    
    /**
     * Starts the index updating process. After calling this function the action 
     * will stay disabled as long as the opeation is in progress.
     */
    @Override
    public void actionPerformed( ActionEvent actionEvent ) {

        /*
         * Get a list of external volumes
         */
        
        DAOFactory daoFactory = ctrl.getDAOFactory();
        VolumeDAO volDAO = daoFactory.getVolumeDAO();
        List<VolumeBase> allVolumes = volDAO.findAll();
        volumes = new ArrayList<ExternalVolume>();
        for ( VolumeBase vol : allVolumes ) {
            if ( vol instanceof ExternalVolume ) {
                volumes.add( (ExternalVolume)vol );
            }
        }
        
        setEnabled( false );
        indexNextVolume();
    }

    /**
     Initiate indexing of next volume if there are still unindexed volumes.
     The mothod creates a new ExtVolIndexer and runs it in its own thread. 
     Since this method modifies UI status it must be called from AWT event 
     thread.
     */
    private void indexNextVolume() {
        if ( volumes.size() > 0 ) {
            errorFiles.clear();
            vol = (ExternalVolume) volumes.get(0);
            BackgroundIndexer indexer =
                    new BackgroundIndexer( vol.getBaseDir(  ), vol,
                    vol.getFolder(  ), true );
            volumes.remove( vol );
            SwingWorkerTaskScheduler sched = 
                    (SwingWorkerTaskScheduler) Photovault.getInstance().getTaskScheduler();
            sched.addTaskListener(indexer, this );
            sched.registerTaskProducer(indexer, TaskPriority.INDEX_EXTVOL );
            percentIndexed = 0;
            StatusChangeEvent e = new StatusChangeEvent( this, "Indexing " 
                    + vol.getBaseDir() );
            fireStatusChangeEvent( e );
        } else {
            // Nothing more to index
            StatusChangeEvent e = new StatusChangeEvent( this, "" );
            fireStatusChangeEvent( e );
            setEnabled( true );
        }
    }
    
    int percentIndexed = 0;
    /**
     Volume currently under work.
     */
    ExternalVolume vol = null;
    
    /**
     Files that caused error during indexing     
     */
    List errorFiles = new ArrayList();
    
    public void fileIndexed( BackgroundIndexer p, IndexFileTask t ) {
        if ( t.getResult() == IndexingResult.ERROR ) {
            StringBuffer errorBuf = new StringBuffer( "Error indexing file" );
            if ( t.getFile() != null ) {
                errorBuf.append( " " ).append( t.getFile().getAbsolutePath() );
                errorFiles.add( t.getFile() );
            }
            StatusChangeEvent statusEvent = new StatusChangeEvent( this, 
                    errorBuf.toString() );
            fireStatusChangeEvent( statusEvent );
            
        } else {
            StatusChangeEvent statusEvent = 
                    new StatusChangeEvent( this, p.getStatusMessage() + " " + 
                    p.getPercentComplete() + "%" );
            fireStatusChangeEvent( statusEvent );
        }
    }

    public void indexingComplete( BackgroundIndexer indexer ) {
        if ( errorFiles.size() > 0 ) {
            showErrorDialog();
        }
        SwingWorkerTaskScheduler sched =
                (SwingWorkerTaskScheduler) Photovault.getInstance().getTaskScheduler();
        sched.removeTaskListener( indexer, this );
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                indexNextVolume();
            }
        });
    }


    /**
     Shows an error dialog that informas about files that the indexer was not 
     able to index (i.e. files stored in {@link errorFiles}).
     <p>
     The dialog is shown in AWT thread, so this method can be called from any 
     thread.
     **/
    private void showErrorDialog() {
        StringBuffer msgBuf = new StringBuffer( "Could not read the following files:" );
        Iterator iter = errorFiles.iterator();
        while ( iter.hasNext() ) {
            File f = (File) iter.next();
            msgBuf.append( "\n" ).append( f.getAbsolutePath() );
        }
        final String msg = msgBuf.toString();
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog( null, msg, "Indexing error", 
                        JOptionPane.ERROR_MESSAGE );
            }
        });
        
    }
    
    Vector statusChangeListeners = new Vector();
    
    public void addStatusChangeListener( StatusChangeListener l ) {
        statusChangeListeners.add( l );
    }
    
    public void removeStatusChangeListener( StatusChangeListener l ) {
        statusChangeListeners.remove( l );
    }
    
    protected void fireStatusChangeEvent( StatusChangeEvent e ) {
        Iterator iter = statusChangeListeners.iterator();
        while ( iter.hasNext() ) {
            StatusChangeListener l = (StatusChangeListener) iter.next();
            l.statusChanged( e );
        }
    }

    /**
     Callback from task shceduler when file has been indexed for this action
     @param producer The BackgroundIndexer that indexed the file
     @param task The IndexFileTask that was executed
     */
    public void taskExecuted( TaskProducer producer, BackgroundTask task ) {
        if ( task instanceof IndexFileTask ) {
            fileIndexed( (BackgroundIndexer) producer, (IndexFileTask) task );
        }
    }

    /**
     Callback from task scheduler when all files in a volume have been indexed.
     @param producer The BackgroundIndexer responsible for indexing that volume
     */
    public void taskProducerFinished( TaskProducer producer ) {
        indexingComplete( (BackgroundIndexer) producer );
    }

}

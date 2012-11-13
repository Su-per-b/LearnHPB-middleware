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

package org.photovault.swingui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.io.*;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.command.CommandException;
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.imginfo.*;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.ShootingDateComparator;
import org.photovault.imginfo.ShootingPlaceComparator;
import org.photovault.folder.*;
import org.photovault.swingui.framework.AbstractController;
import org.photovault.swingui.framework.DefaultAction;
import org.photovault.swingui.framework.DefaultEvent;
import org.photovault.swingui.framework.DefaultEventListener;
import org.photovault.swingui.indexer.BackgroundIndexer;
import org.photovault.swingui.indexer.CurrentFolderIndexer;
import org.photovault.swingui.indexer.IndexerFileChooser;
import org.photovault.swingui.indexer.UpdateIndexAction;
import org.photovault.swingui.taskscheduler.BackgroundTaskListener;
import org.photovault.swingui.taskscheduler.SwingWorkerTaskScheduler;
import org.photovault.swingui.taskscheduler.TaskPriority;
import org.photovault.taskscheduler.BackgroundTask;
import org.photovault.taskscheduler.TaskProducer;

public class BrowserWindow extends AbstractController {

    static Log log = LogFactory.getLog( BrowserWindow.class.getName() );

    JFrame window;
    
    PhotoViewController viewCtrl = null;
    
    /**
     Indexer job that updates the currently displayed external folder to match
     file system state.
     */
    CurrentFolderIndexer folderIndexer = null;
    
    public BrowserWindow( AbstractController parent ) {
        this( parent, null );
    }
    
    
    /**
       Constructor
    */
    public BrowserWindow( AbstractController parent, final List<PhotoInfo> initialPhotos ) {
        super( parent );
        window = new JFrame( "Photovault Browser");
        SwingWorkerTaskScheduler taskScheduler = 
                (SwingWorkerTaskScheduler) Photovault.getInstance().getTaskScheduler();
        folderIndexer = new CurrentFolderIndexer(
                taskScheduler, (PhotovaultCommandHandler) getCommandHandler() );
        createUI( null );

        taskScheduler.addTaskListener( folderIndexer, new BackgroundTaskListener() {

            public void taskExecuted( TaskProducer producer, BackgroundTask task ) {
                StringBuffer msgBuf = new StringBuffer();
                CurrentFolderIndexer indexer = (CurrentFolderIndexer) producer;
                if ( indexer.getState() == CurrentFolderIndexer.IndexingPhase.FILE_INDEX ) {
                    PhotoFolder f = indexer.getCurrentFolder();
                    msgBuf.append( "Indexing " ).append( f.getName() ).append( "... " );
                    msgBuf.append( indexer.getPercentComplete() ).append( "% complete" );
                    viewCtrl.setIndexingPercentComplete( indexer.getPercentComplete() );
                }
                statusBar.statusChanged(
                        new StatusChangeEvent( indexer, msgBuf.toString() ) );
                
            }

            public void taskProducerFinished( TaskProducer producer ) {
                StringBuffer msgBuf = new StringBuffer();
                CurrentFolderIndexer indexer = (CurrentFolderIndexer) producer;
                if ( indexer.getState() == CurrentFolderIndexer.IndexingPhase.FILE_INDEX ) {
                    PhotoFolder f = indexer.getCurrentFolder();
                    msgBuf.append( "Indexed " ).append( f.getName() );
                }
                statusBar.statusChanged(
                        new StatusChangeEvent( indexer, msgBuf.toString() ) );
                viewCtrl.setIndexingOngoing( false );
            }
        } );

        if ( initialPhotos != null ) {
            viewPane.setPhotos( initialPhotos );
            SwingUtilities.invokeLater(new java.lang.Runnable() {
                public void run() {
                    viewPane.selectFirstPhoto();
                }
            });
        }
        
    }
    
    
    /**
     Show a specific photo collection in this window. Note! This must not be called 
     before UI is created & visible!!!
     @param c The photo collection to show
     */
    public void setPhotoCollection( PhotoCollection c ) {
        viewCtrl.setCollection( c );
    }
    
    protected void createUI( PhotoCollection collection ) {
        window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        tabPane = new JTabbedPane();
        queryPane = new QueryPane();
        PhotoFolderTreeController treeCtrl = new PhotoFolderTreeController( window, this );
        viewCtrl = new PhotoViewController( window, this );
        treePane = treeCtrl.folderTree;
        viewPane = viewCtrl.getThumbPane();
        previewPane = viewCtrl.getPreviewPane();
        tabPane.addTab( "Query", queryPane );
        tabPane.addTab( "Folders", treePane );
        //	viewPane = new TableCollectionView();

        // TODO: get rid of this!!!!
        EditSelectionColorsAction colorAction =
                (EditSelectionColorsAction) viewPane.getEditSelectionColorsAction();
        colorAction.setPreviewCtrl( previewPane );

        // Set listeners to both query and folder tree panes

        /*
        If an actionEvent comes from query pane, swich to query results.
         */
        queryPane.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent e ) {
                viewCtrl.setCollection( queryPane.getResultCollection() );
            }
        } );

        /*
        If the selected folder is changed in treePane, switch to that immediately
         */

        treeCtrl.registerEventListener( PhotoFolderTreeEvent.class,
                new DefaultEventListener<PhotoFolder>() {

                    public void handleEvent( DefaultEvent<PhotoFolder> event ) {
                        PhotoFolder f = event.getPayload();
                        if ( f != null ) {
                            viewCtrl.setCollection( f );
                            if ( f.getExternalDir() != null ) {
                                folderIndexer.updateFolder( f );
                                viewCtrl.setIndexingOngoing( true );
                            }
                        }
                    }
                } );

        collectionPane = viewCtrl.getCollectionPane();

        JSplitPane split = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, tabPane, collectionPane );
        split.putClientProperty( JSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY, new Boolean( true ) );
        Container cp = window.getContentPane();
        cp.setLayout( new BorderLayout() );
        cp.add( split, BorderLayout.CENTER );

        statusBar = new StatusBar();
        cp.add( statusBar, BorderLayout.SOUTH );

        // Create actions for BrowserWindow UI

        ImageIcon indexDirIcon = getIcon( "index_dir.png" );
        DefaultAction indexDirAction = new DefaultAction( "Index directory...", indexDirIcon ) {

            public void actionPerformed( ActionEvent e ) {
                indexDir();
            }
        };
        indexDirAction.putValue( AbstractAction.MNEMONIC_KEY, KeyEvent.VK_D );
        indexDirAction.putValue( AbstractAction.SHORT_DESCRIPTION,
                "Index all images in a directory" );
        this.registerAction( "new_ext_vol", indexDirAction );


        ImageIcon importIcon = getIcon( "import.png" );
        importAction = new AbstractAction( "Import image...", importIcon ) {

            public void actionPerformed( ActionEvent e ) {
                importFile();
            }
        };
        importAction.putValue( AbstractAction.MNEMONIC_KEY, KeyEvent.VK_O );
        importAction.putValue( AbstractAction.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke( KeyEvent.VK_O,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() ) );

        importAction.putValue( AbstractAction.SHORT_DESCRIPTION,
                "Import new image into database" );

        ImageIcon updateIcon = getIcon( "update_indexed_dirs.png" );
        UpdateIndexAction updateIndexAction = new UpdateIndexAction( viewCtrl,
                "Update indexed dirs",
                updateIcon, "Check for changes in previously indexed directories",
                KeyEvent.VK_U );
        this.registerAction( "update_indexed_dirs", updateIndexAction );
        updateIndexAction.addStatusChangeListener( statusBar );

        ImageIcon previewTopIcon = getIcon( "view_preview_top.png" );
        DefaultAction previewTopAction = new DefaultAction( "Preview on top", previewTopIcon ) {

            public void actionPerformed( ActionEvent e ) {
                viewCtrl.setLayout( PhotoViewController.Layout.PREVIEW_HORIZONTAL_THUMBS );
            }
        };
        previewTopAction.putValue( AbstractAction.SHORT_DESCRIPTION,
                "Show preview on top of thumbnails" );
        previewTopAction.putValue( AbstractAction.MNEMONIC_KEY, KeyEvent.VK_T );
        this.registerAction( "view_preview_top", previewTopAction );

        ImageIcon previewRightIcon = getIcon( "view_preview_right.png" );
        DefaultAction previewRightAction = new DefaultAction( "Preview on right", previewRightIcon ) {

            public void actionPerformed( ActionEvent e ) {
                viewCtrl.setLayout( PhotoViewController.Layout.PREVIEW_VERTICAL_THUMBS );
            }
        };
        previewRightAction.putValue( AbstractAction.SHORT_DESCRIPTION,
                "Show preview on right of thumbnails" );
        previewRightAction.putValue( AbstractAction.MNEMONIC_KEY, KeyEvent.VK_R );
        this.registerAction( "view_preview_right", previewRightAction );

        ImageIcon previewNoneIcon = getIcon( "view_no_preview.png" );
        DefaultAction previewNoneAction = new DefaultAction( "No preview", previewNoneIcon ) {

            public void actionPerformed( ActionEvent e ) {
                viewCtrl.setLayout( PhotoViewController.Layout.ONLY_THUMBS );
            }
        };
        previewNoneAction.putValue( AbstractAction.SHORT_DESCRIPTION,
                "Show no preview image" );
        previewNoneAction.putValue( AbstractAction.MNEMONIC_KEY, KeyEvent.VK_O );
        this.registerAction( "view_no_preview", previewNoneAction );

        DefaultAction previewLargeAction = new DefaultAction( "Large", null ) {

            public void actionPerformed( ActionEvent e ) {
                viewCtrl.setPreviewSize( 200 );
            }
        };
        DefaultAction previewSmallAction = new DefaultAction( "Small", null ) {

            public void actionPerformed( ActionEvent e ) {
                viewCtrl.setPreviewSize( 100 );
            }
        };

        JToolBar tb = createToolbar();
        cp.add( tb, BorderLayout.NORTH );

        // Create the menu bar & menus
        JMenuBar menuBar = new JMenuBar();
        window.setJMenuBar( menuBar );
        // File menu
        JMenu fileMenu = new JMenu( "File" );
        fileMenu.setMnemonic( KeyEvent.VK_F );
        menuBar.add( fileMenu );

        JMenuItem newWindowItem = new JMenuItem( "New window", KeyEvent.VK_N );
        newWindowItem.setIcon( getIcon( "window_new.png" ) );
        newWindowItem.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent e ) {
                BrowserWindow br = new BrowserWindow( getParentController(), null );
            //		    br.setVisible( true );
            }
        } );
        fileMenu.add( newWindowItem );

        JMenuItem importItem = new JMenuItem( importAction );
        fileMenu.add( importItem );

        JMenuItem indexDirItem = new JMenuItem( indexDirAction );
        fileMenu.add( indexDirItem );

        fileMenu.add( new JMenuItem( viewCtrl.getActionAdapter( "update_indexed_dirs" ) ) );

        ExportSelectedAction exportAction =
                (ExportSelectedAction) viewPane.getExportSelectedAction();
        JMenuItem exportItem = new JMenuItem( exportAction );
        exportAction.addStatusChangeListener( statusBar );
        fileMenu.add( exportItem );

        JMenu dbMenu = new JMenu( "Database" );
        dbMenu.setMnemonic( KeyEvent.VK_B );
        dbMenu.setIcon( getIcon( "empty_icon.png" ) );

        ExportMetadataAction exportMetadata =
                new ExportMetadataAction( "Export as XML...", null,
                "Export whole database as XML file", KeyEvent.VK_E );
        dbMenu.add( new JMenuItem( exportMetadata ) );

        ImportXMLAction importMetadata =
                new ImportXMLAction( "Import XML data...", null,
                "Import data from other Photovault database as XML", KeyEvent.VK_I );
        importMetadata.addStatusChangeListener( statusBar );
        dbMenu.add( new JMenuItem( importMetadata ) );

        fileMenu.add( dbMenu );

        JMenuItem exitItem = new JMenuItem( "Exit", KeyEvent.VK_X );
        exitItem.setIcon( getIcon( "exit.png" ) );
        exitItem.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent e ) {
                System.exit( 0 );
            }
        } );
        fileMenu.add( exitItem );

        JMenu viewMenu = new JMenu( "View" );
        viewMenu.setMnemonic( KeyEvent.VK_V );
        menuBar.add( viewMenu );

        JMenuItem vertIconsItem = new JMenuItem( previewRightAction );
        viewMenu.add( vertIconsItem );

        JMenuItem horzIconsItem = new JMenuItem( previewTopAction );
        viewMenu.add( horzIconsItem );

        JMenuItem noPreviewItem = new JMenuItem( previewNoneAction );
        viewMenu.add( noPreviewItem );

        JMenuItem nextPhotoItem = new JMenuItem( viewPane.getSelectNextAction() );
        viewMenu.add( nextPhotoItem );

        JMenuItem prevPhotoItem = new JMenuItem( viewPane.getSelectPreviousAction() );
        viewMenu.add( prevPhotoItem );


        viewMenu.add( new JMenuItem( previewSmallAction ) );
        viewMenu.add( new JMenuItem( previewLargeAction ) );

        JMenu sortMenu = new JMenu( "Sort by" );
        sortMenu.setMnemonic( KeyEvent.VK_S );
        sortMenu.setIcon( getIcon( "empty_icon.png" ) );
        JMenuItem byDateItem = new JMenuItem( new SetPhotoOrderAction( viewCtrl,
                new ShootingDateComparator(), "Date", null,
                "Order photos by date", KeyEvent.VK_D ) );
        sortMenu.add( byDateItem );
        JMenuItem byPlaceItem = new JMenuItem( new SetPhotoOrderAction( viewCtrl,
                new ShootingPlaceComparator(), "Place", null,
                "Order photos by shooting place", KeyEvent.VK_P ) );
        sortMenu.add( byPlaceItem );
        JMenuItem byQualityItem = new JMenuItem( new SetPhotoOrderAction( viewCtrl,
                new QualityComparator(), "Quality", null,
                "Order photos by quality", KeyEvent.VK_Q ) );
        sortMenu.add( byQualityItem );
        viewMenu.add( sortMenu );

        // Set default ordering by date
        byDateItem.getAction().actionPerformed( new ActionEvent( this, 0, "Setting default" ) );
        JMenu imageMenu = new JMenu( "Image" );
        imageMenu.setMnemonic( KeyEvent.VK_I );
        menuBar.add( imageMenu );

        imageMenu.add( new JMenuItem( viewPane.getEditSelectionPropsAction() ) );
        imageMenu.add( new JMenuItem( viewPane.getShowSelectedPhotoAction() ) );
        imageMenu.add( new JMenuItem( viewCtrl.getActionAdapter( "rotate_cw" ) ) );
        imageMenu.add( new JMenuItem( viewCtrl.getActionAdapter( "rotate_ccw" ) ) );
        imageMenu.add( new JMenuItem( viewCtrl.getActionAdapter( "rotate_180" ) ) );
        imageMenu.add( new JMenuItem( previewPane.getCropAction() ) );
        imageMenu.add( new JMenuItem( viewPane.getEditSelectionColorsAction() ) );
        imageMenu.add( new JMenuItem( viewPane.getDeleteSelectedAction() ) );

        // Create the Quality submenu
        JMenu qualityMenu = new JMenu( "Quality" );
        qualityMenu.setIcon( getIcon( "empty_icon.png" ) );

        for ( int n = 0; n < 6; n++ ) {
            Action qualityAction = viewCtrl.getActionAdapter( "quality_" + n );
            JMenuItem qualityMenuItem = new JMenuItem( qualityAction );
            qualityMenu.add( qualityMenuItem );
        }
        imageMenu.add( qualityMenu );
        
        JMenu aboutMenu = new JMenu( "About" );
        aboutMenu.setMnemonic( KeyEvent.VK_A );
        aboutMenu.add( new JMenuItem( new ShowAboutDlgAction( "About Photovault...", null, "", null ) ) );

        menuBar.add( Box.createHorizontalGlue() );
        menuBar.add( aboutMenu );
        window.pack();
        window.setVisible( true );
    }
    
    /**
     Create the toolbar for this browser window.
     */
    protected JToolBar createToolbar() {
        JToolBar tb = new JToolBar();
        
        JButton importBtn = new JButton( importAction );
        importBtn.setText( "" );
        JButton indexBtn = new JButton( viewCtrl.getActionAdapter( "new_ext_vol" ) );
        indexBtn.setText( "" );
        JButton updateBtn = new JButton( viewCtrl.getActionAdapter( "update_indexed_dirs" ) );
        updateBtn.setText( "" );
        JButton exportBtn = new JButton( viewPane.getExportSelectedAction() );
        exportBtn.setText( "" );
        JButton deleteBtn = new JButton( viewPane.getDeleteSelectedAction() );
        deleteBtn.setText( "" );
        
        JButton rotCWBtn = new JButton( viewCtrl.getActionAdapter("rotate_cw" ) );
        rotCWBtn.setText( "" );
        JButton rotCCWBtn = new JButton( viewCtrl.getActionAdapter( "rotate_ccw" ) );
        rotCCWBtn.setText( "" );
        JButton rot180Btn = new JButton( viewCtrl.getActionAdapter( "rotate_180" ) );
        rot180Btn.setText( "" );
        JButton cropBtn = new JButton( previewPane.getCropAction() );
        cropBtn.setText( "" );
        JButton colorsBtn = new JButton( viewPane.getEditSelectionColorsAction() );
        colorsBtn.setText( "" );
        
        JButton nextBtn = new JButton( viewPane.getSelectNextAction() );
        nextBtn.setText( "" );
        JButton prevBtn = new JButton( viewPane.getSelectPreviousAction() );
        prevBtn.setText( "" );
        JButton previewRightBtn = new JButton( getActionAdapter( "view_preview_right" ) );
        previewRightBtn.setText( "" );
        JButton previewTopBtn = new JButton( getActionAdapter( "view_preview_top" ) );
        previewTopBtn.setText( "" );
        JButton previewNoneBtn = new JButton( getActionAdapter( "view_no_preview" ) );
        previewNoneBtn.setText( "" );
        
	ZoomComboBox zoomCombo = new ZoomComboBox( previewPane );
        
        
        tb.add( importBtn );
        tb.add( indexBtn );
        tb.add( updateBtn );
        tb.add( exportBtn );
        tb.add( deleteBtn );
        tb.addSeparator();
        tb.add( prevBtn );
        tb.add( nextBtn );
        tb.add( previewRightBtn );
        tb.add( previewTopBtn );
        tb.add( previewNoneBtn );
	tb.add( zoomCombo );
        tb.addSeparator();
        tb.add( rotCWBtn );
        tb.add( rotCCWBtn );
        tb.add( rot180Btn );
        tb.add( cropBtn );
        tb.add( colorsBtn );
        
        return tb;
    }

    /**
     Loads an icon using class loader of this class
     @param resouceName Name of the icon reosurce to load
     @return The icon or <code>null</code> if no image was found using the given
     resource name.
     */
    private ImageIcon getIcon( String resourceName ) {
        ImageIcon icon = null;
        java.net.URL iconURL = JAIPhotoViewer.class.getClassLoader().getResource( resourceName );
        if ( iconURL != null ) {
            icon = new ImageIcon( iconURL );
        }
        return icon;
    }

    /**
     Set whether to show the query/tree view pane
     @param b if true, panel is shown, if false it is not shown.
     */
    public void setShowCollectionPane( boolean b ) {
        tabPane.setVisible( b );
    }
    
    
    /** 
	Shows an file selection dialog that allows user to select a
	file to import. After that shows the PhotoInfo dialog to allow the
	user to edit the eriginal information about the file. 
    */

    protected void importFile() {
	// Show the file chooser dialog
	JFileChooser fc = new JFileChooser();
	fc.addChoosableFileFilter( new ImageFilter() );
	fc.setAccessory( new ImagePreview( fc ) );
	fc.setMultiSelectionEnabled( true );
	
	int retval = fc.showDialog( window, "Import" );
	if ( retval == JFileChooser.APPROVE_OPTION ) {
	    // Add the selected file to the database and allow user to edit its attributes
	    final File[] files = fc.getSelectedFiles();

	    final ProgressDlg pdlg = new ProgressDlg( window, true );
	    
	    // Add all the selected files to DB
	    final PhotoInfo[] photos = new PhotoInfo[files.length];
	    Thread importThread = new Thread() {
                @Override
		    public void run() {

			for ( int n = 0; n < files.length; n++ ) {
			    try {
				photos[n] = PhotoInfo.addToDB( files[n] );
				pdlg.setProgressPercent( (n*100) / files.length );
				pdlg.setStatus( "" + (n+1) + " of " + files.length + " files imported." );
			    } catch ( Exception e ) {
				log.error( "Unexpected exception: " + e.getMessage() );
			    }
			}
			pdlg.completed();
		    }
		};
	    
	    importThread.start();
	    pdlg.setVisible( true );
	    
	    // Show editor dialog for the added photos
	    // PhotoInfoDlg dlg = new PhotoInfoDlg( window, false, photos );
	    PhotoInfoDlg dlg = null;
	    dlg.showDialog();
	}
    }

    /**
     Asks user to select a directory, creates an external volume from it and 
     indexes all images in it. The actual indexing is done asynchronously in a 
     separate thread.
     */ 
    protected void indexDir() {
	IndexerFileChooser fc = new IndexerFileChooser();
        fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        int retval = fc.showDialog( window, "Select directory to index" );
        if ( retval == JFileChooser.APPROVE_OPTION ) {
            File dir = fc.getSelectedFile();
                                    
            // Generate a unique name for the new volume
            String extVolName = "extvol_" + dir.getName();
//            if ( VolumeBase.getVolume( extVolName ) != null ) {
//                int n = 2;
//                while ( VolumeBase.getVolume( extVolName + n ) != null ) {
//                    n++;
//                }
//                extVolName += n;
//            }
            
            // Set up the indexer
            PhotoFolder parentFolder = fc.getExtvolParentFolder();
            PhotoFolderDAO folderDAO = viewCtrl.getDAOFactory().getPhotoFolderDAO();
            if ( parentFolder == null ) {
                parentFolder = folderDAO.findRootFolder();
            }
            String rootFolderName = "extvol_" + dir.getName();
            if ( rootFolderName.length() > PhotoFolder.NAME_LENGTH ) {
                rootFolderName = rootFolderName.substring( 0, PhotoFolder.NAME_LENGTH );
            }
            
            CreatePhotoFolderCommand folderCmd = 
                    new CreatePhotoFolderCommand( parentFolder, 
                    rootFolderName, "" );
            CreateExternalVolume volCmd = null;
            try {
                viewCtrl.getCommandHandler().executeCommand( folderCmd );
                volCmd = new CreateExternalVolume( 
                        dir, rootFolderName,  folderCmd.getCreatedFolder() );
                viewCtrl.getCommandHandler().executeCommand( volCmd );

                // Start indexing
                PhotoFolder topFolder = folderCmd.getCreatedFolder();
                BackgroundIndexer indexer = new BackgroundIndexer( dir, volCmd.getCreatedVolume(),
                        topFolder, true );
                SwingWorkerTaskScheduler sched =
                        (SwingWorkerTaskScheduler) Photovault.getInstance().getTaskScheduler();
                sched.registerTaskProducer( indexer,
                        TaskPriority.INDEX_EXTVOL );

            } catch ( CommandException ex ) {
                JOptionPane.showMessageDialog( window, ex.getMessage(),
                        "Error creating volume", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /**
     Set the title of this window.
     @param title The new title.
     */
    void setTitle(String title) {
        window.setTitle( title );
    }
    
    protected JTabbedPane tabPane = null;
    protected JTabbedPane collectionTabPane = null;
    protected JScrollPane viewScroll = null;
    protected JPanel collectionPane = null;
    protected QueryPane queryPane = null;
    protected PhotoFolderTree treePane = null;
    
    
    
    //    protected TableCollectionView viewPane = null;
    protected PhotoCollectionThumbView viewPane = null;
    protected JAIPhotoViewer previewPane = null;

    /**
     Action that imports a new image to Photovault archive
     */
    AbstractAction importAction;
    
    /**
     *Status bar for this window
     */
    private StatusBar statusBar;
}

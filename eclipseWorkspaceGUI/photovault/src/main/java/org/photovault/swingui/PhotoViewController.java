/*
  Copyright (c) 2007, Harri Kaimio
  
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

import abbot.tester.JSpinnerTester;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import org.jdesktop.jxlayer.JXLayer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.command.CommandExecutedEvent;
import org.photovault.command.DataAccessCommand;
import org.photovault.folder.PhotoFolder;
import org.photovault.folder.PhotoFolderDAO;
import org.photovault.imginfo.ChangePhotoInfoCommand;
import org.photovault.imginfo.CreateCopyImageCommand;
import org.photovault.imginfo.PhotoCollection;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoDAO;
import org.photovault.replication.ChangeDTO;
import org.photovault.swingui.framework.AbstractController;
import org.photovault.swingui.framework.DataAccessAction;
import org.photovault.swingui.framework.DefaultEvent;
import org.photovault.swingui.framework.DefaultEventListener;
import org.photovault.swingui.framework.PersistenceController;
import org.photovault.swingui.taskscheduler.SwingWorkerTaskScheduler;

/**
 Controller for the componenst actually used for viewing or editing photos. This 
 controller handles interaction between {@link PhotoCollectionThumbView} that shows
 all photos in certain collection as thumbnails, {@link JAIPhotoViewer} that 
 displays the selected photo as a preview and related editing dialogs (property, 
 cropping, color editing etc.)
 */

public class PhotoViewController extends PersistenceController {
    
    private static Log log = 
            LogFactory.getLog( PhotoViewController.class.getName() );
    
    PhotoInfoDAO photoDAO = null;
    
    PhotoFolderDAO folderDAO = null;

    private PhotoCollectionThumbView thumbPane;

    private JAIPhotoViewer previewPane;

    private JScrollPane thumbScroll;

    private JPanel collectionPane;

    private JSplitPane splitPane;

    private JLayeredPane layeredPane;

    private JXLayer<JScrollPane> scrollLayer;

    private ProgressIndicatorLayer progressLayer;
    
    /**
     Photos currently in model.
     */
    private List<PhotoInfo> photos = new ArrayList<PhotoInfo>();
    
    /** Creates a new instance of PhotoViewController */
    public PhotoViewController( Container view, AbstractController parentController ) {
        super( view, parentController );
        photoDAO = getDAOFactory().getPhotoInfoDAO();
        folderDAO = getDAOFactory().getPhotoFolderDAO();
        ImageIcon rotateCWIcon = getIcon( "rotate_cw.png" );
        ImageIcon rotateCCWIcon = getIcon( "rotate_ccw.png" );
        ImageIcon rotate180DegIcon = getIcon( "rotate_180.png" );


        registerAction( "rotate_cw", new RotateSelectedPhotoAction( this, 90,
                "Rotate CW", rotateCWIcon,
                "Rotates the selected photo 90 degrees clockwise", KeyEvent.VK_R ) );
        registerAction( "rotate_ccw", new RotateSelectedPhotoAction( this, 270,
                "Rotate CCW", rotateCCWIcon,
                "Rotates the selected photo 90 degrees counterclockwise", KeyEvent.VK_L ) );
        registerAction( "rotate_180", new RotateSelectedPhotoAction( this, 180,
                "Rotate 180 degrees", rotate180DegIcon,
                "Rotates the selected photo 180 degrees counterclockwise", KeyEvent.VK_T ) );
        registerAction( "rotate_180", new RotateSelectedPhotoAction( this, 180,
                "Rotate 180 degrees", rotate180DegIcon,
                "Rotates the selected photo 180 degrees counterclockwise", KeyEvent.VK_T ) );
        String qualityStrings[] = { "Unevaluated", "Top", "Good", "OK", "Poor", "Unusable" };
        String qualityIconnames[] = {
            "quality_unevaluated.png",
            "quality_top.png",
            "quality_good.png",
            "quality_ok.png",
            "quality_poor.png",
            "quality_unusable.png"
        };
        KeyStroke qualityAccelerators[] = {
            null,
            KeyStroke.getKeyStroke( KeyEvent.VK_5,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() ),
            KeyStroke.getKeyStroke( KeyEvent.VK_4,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() ),
            KeyStroke.getKeyStroke( KeyEvent.VK_3,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() ),
            KeyStroke.getKeyStroke( KeyEvent.VK_2,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() ),
            KeyStroke.getKeyStroke( KeyEvent.VK_1,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() ),
        };
        ImageIcon[] qualityIcons = new ImageIcon[qualityStrings.length];
        for ( int n = 0; n < qualityStrings.length; n++ ) {
            qualityIcons[n] = getIcon( qualityIconnames[n] );
            DataAccessAction qualityAction
                    = new SetPhotoQualityAction( this, n, qualityStrings[n],
                    qualityIcons[n],
                    "Set quality of selected phots to \"" + qualityStrings[n] + "\"",
                    null );
            qualityAction.putValue(
                    AbstractAction.ACCELERATOR_KEY, qualityAccelerators[n] );
            registerAction( "quality_" + n, qualityAction );
        }

        // Create the UI controls
        thumbPane = new PhotoCollectionThumbView( this, null );
        thumbPane.addSelectionChangeListener( new SelectionChangeListener() {

            public void selectionChanged( SelectionChangeEvent e ) {
                thumbSelectionChanged( e );
            }
        } );
        previewPane = new JAIPhotoViewer( this );
        previewPane.getActionMap().put( "hide_fullwindow_preview",
                new HidePhotoPreviewAction( this ) );
        previewPane.getActionMap().put(  "move_next", thumbPane.getSelectNextAction() );
        previewPane.getActionMap().put(  "move_prev", thumbPane.getSelectPreviousAction() );

        // Create the split pane to display both of these components

        thumbScroll = new JScrollPane( thumbPane );
        thumbPane.setBackground( Color.WHITE );
        thumbScroll.getViewport().setBackground( Color.WHITE );
        thumbScroll.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized( ComponentEvent e ) {
                handleThumbAreaResize();
            }
        } );

        scrollLayer = new JXLayer<JScrollPane>( thumbScroll );
        progressLayer = new ProgressIndicatorLayer();
        scrollLayer.setUI( progressLayer );

        collectionPane = new JPanel();
        splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(  new StackLayout() );
        collectionPane.add( splitPane );
        collectionPane.add( layeredPane );
        GridBagLayout layout = new GridBagLayout();
        collectionPane.setLayout( layout );

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridy = 0;
        // collectionPane.add( scrollLayer );
        layout.setConstraints( splitPane, c);
        layout.setConstraints( layeredPane, c);
//        collectionPane.add( previewPane );
        thumbPane.setRowHeight( 200 );
        setLayout( Layout.ONLY_THUMBS );

        /*
        Register action so that we are notified of changes to currently
        displayed folder
         */
        registerEventListener( CommandExecutedEvent.class, new DefaultEventListener<DataAccessCommand>() {

            public void handleEvent( DefaultEvent<DataAccessCommand> event ) {
                DataAccessCommand cmd = event.getPayload();
                if ( cmd instanceof ChangePhotoInfoCommand ) {
                    photoChangeCommandExecuted( (ChangePhotoInfoCommand) cmd );
                } else if ( cmd instanceof CreateCopyImageCommand ) {
                    imageCreated( (CreateCopyImageCommand) cmd );
                }
            }
        } );
    }
    /**
     * Comparator used to sort photos visible in thumbnail view
     */
    Comparator photoComparator;

    /**
     * Set the comparator used to sort photos in thumbnail view
     * @param c
     */
    void setPhotoComparator( Comparator c ) {
        photoComparator = c;
        updateThumbView();
    }

    /**
     * Get the comparator used to sort photos in thumbnail view
     * @return
     */
    Comparator getPhotoComparator() {
        return photoComparator;
    }

    /**
     * Update the thumb view to show photos is the {@link #photos} collections,
     * sorted by {@link #photoComparator}
     */
    private void updateThumbView() {
        if ( photoComparator != null && photos != null ) {
            Collections.sort( photos, photoComparator );
        }
        thumbPane.setPhotos( photos );
    }

    /**
     This method is called after a {@link ChangePhotoInfoCommand} has been 
     executed somewhere in the application. It applies the modifications
     to current model.
     @param cmd The executed command
     */
    void photoChangeCommandExecuted( ChangePhotoInfoCommand cmd ) {
        if ( collection instanceof PhotoFolder ) {
            // Does this command impact our current folder?
            switch ( cmd.getFolderState( (PhotoFolder) collection ) ) {
            case ADDED:
                addPhotos( cmd.getChangedPhotos() );
                updateThumbView();
                break;
            case REMOVED:
                removePhotos( cmd.getChangedPhotos() );
                // None of the affected photos can belong to the model anymore
                // so no need for further checks.
                updateThumbView();
                return;
            default:
                // No impact to this folder
                break;
            }
        }

        // Update photos that belong to this collection
        for ( PhotoInfo p: cmd.getChangedPhotos() ) {
            if ( containsPhoto( p ) ) {
                PhotoInfo mergedPhoto = (PhotoInfo) getPersistenceContext().merge( p );
            }
        }
        thumbPane.setPhotos( photos );
    }


    private void imageCreated( CreateCopyImageCommand cmd ) {
        PhotoInfo p = cmd.getPhoto();
        log.debug( "image created for photo " + p.getUuid() );
        if ( containsPhoto( p ) ) {
            PhotoInfo mergedPhoto = (PhotoInfo) getPersistenceContext().merge( p );
            log.debug( "merged chages to photo " + mergedPhoto.getUuid() );
            if ( !mergedPhoto.hasThumbnail() ) {
                log.error( "he photo does not have a thumbnail!!!" );
                getPersistenceContext().update( mergedPhoto );
            }
        }
    }
    
    /**
     Add all photos from a collection to current model if they are not yet
     part of it.
     @param newPhotos Collection of (potentially detached) photos
     */
    private void addPhotos( Collection<PhotoInfo> newPhotos ) {
        for ( PhotoInfo p: newPhotos ) {
            if ( !containsPhoto( p ) ) {
                photos.add( (PhotoInfo) getPersistenceContext().merge( p ) );
            }
        }
    }
    
    /**
     Remove photos from current model if they belong to it
     @param removePhotos Collection of photos that will be removed. Potentially 
     detached instances.
     */
    private void removePhotos( Collection<PhotoInfo> removePhotos ) {
        Set<UUID> removeIds = new TreeSet<UUID>();
        for ( PhotoInfo p : removePhotos ) {
            removeIds.add( p.getUuid() );
        }
        
        ListIterator<PhotoInfo> iter = photos.listIterator();
        while ( iter.hasNext() ) {
            if ( removeIds.contains( iter.next().getUuid() ) ) {
                iter.remove();
            }
        }
    }
    
    /**
     Check whether a given photo belongs currently to the model.
     @param photo Potentially detached photo instance
     @return true if the model contains an instance of the same photo, false
     otherwise.
     */
    private boolean containsPhoto( PhotoInfo photo ) {
        return containsPhoto( photo.getUuid() );
    }

    private boolean containsPhoto( UUID photoUuid ) {
        for ( PhotoInfo p : photos ) {
            if ( p.getUuid().equals( photoUuid ) ) {
                return true;
            }
        }
        return false;

    }

    /**
     This method is called when selection in the thumbnail view changes.
     @param e The selection event
     */
    void thumbSelectionChanged( SelectionChangeEvent e ) {
       Collection selection = thumbPane.getSelection();
       if ( selection.size() == 1 ) {
           Cursor oldCursor = getView().getCursor();
           getView().setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
           PhotoInfo selected = (PhotoInfo) (selection.toArray())[0];
            try {
                previewPane.setPhoto( selected );
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog( getView(), 
                        "Image file for this photo was not found", "File not found",
                        JOptionPane.ERROR_MESSAGE );
            }   
           getView().setCursor( oldCursor );
       } else {
            try {
                previewPane.setPhoto( null );
            } catch (FileNotFoundException ex) {
                // No exception expected when calling with null
            }
       }
       this.fireEvent( e );
    }
    
    void showSelectedPhotoFullWindow() {
        if ( getSelection().size() == 1 ) {
            if ( layout != Layout.ONLY_THUMBS ) {
                setLayout( Layout.ONLY_THUMBS );
            }
        }
        previewPane.setVisible( true );
    }
    
    void hideSelectedPhoto() {
        if ( layout == Layout.ONLY_THUMBS ) {
            previewPane.setVisible( false );
        }
    }

    public Collection getSelection() {
        return thumbPane.getSelection();
    }

    public enum Layout {
        PREVIEW_VERTICAL_THUMBS,
        PREVIEW_HORIZONTAL_THUMBS,
        ONLY_PREVIEW,
        ONLY_THUMBS
    };

    private Layout layout;

    public void setLayout( Layout layout ) {
        this.layout = layout;
        switch( layout ) {
            case ONLY_PREVIEW:
                setupLayoutNoThumbs();
                break;
            case ONLY_THUMBS:
                setupLayoutNoPreview();
                break;
            case PREVIEW_HORIZONTAL_THUMBS:
                setupLayoutPreviewWithHorizontalIcons();
                break;
            case PREVIEW_VERTICAL_THUMBS:
                setupLayoutPreviewWithVerticalIcons();
                break;
        }
    }

    public Layout getLayout() {
        return layout;
    }

    /**
     * Sets up the window layout so that the collection is displayed as one vertical
     * column with preview image on right
     */
    private void setupLayoutPreviewWithVerticalIcons() {
        // Minimum size is the size of one thumbnail
        int thumbColWidth = thumbPane.getColumnWidth();
        int thumbRowHeight = thumbPane.getRowHeight();
        scrollLayer.setMinimumSize( 
                new Dimension( thumbColWidth + 20, thumbRowHeight ));
        scrollLayer.setPreferredSize( 
                new Dimension( thumbColWidth + 20, thumbRowHeight ));
        thumbScroll.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        thumbScroll.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        scrollLayer.setVisible( true );
        thumbPane.setColumnCount( 1 );
        previewPane.setVisible( true );
        layeredPane.removeAll();
        layeredPane.setVisible( false );
        splitPane.setVisible( true );
        splitPane.remove( previewPane );
        splitPane.remove( scrollLayer );
        splitPane.setOrientation( JSplitPane.HORIZONTAL_SPLIT );
        splitPane.setLeftComponent( scrollLayer );
        splitPane.setRightComponent( previewPane );
        // Left component should not be resized 
        splitPane.setResizeWeight( 0.0 );
        Dimension minThumbDim = thumbPane.getMinimumSize();
        scrollLayer.setMinimumSize( new Dimension( (int) minThumbDim.getWidth(), 0 ) );
        previewPane.setMinimumSize( new Dimension(
                splitPane.getWidth() - 250 - splitPane.getInsets().left,
                0 ) );
        splitPane.validate();

        getView().validate();
    }
    
     /**
     * Sets up the window layout so that the collection is displayed as one horizontal
     * row with preview image above it.
     */
    private void setupLayoutPreviewWithHorizontalIcons() {
        // Minimum size is the size of one thumbnail
        int thumbColWidth = thumbPane.getColumnWidth();
        int thumbRowHeight = thumbPane.getRowHeight();
        thumbScroll.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        thumbScroll.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );
        scrollLayer.setMinimumSize( 
                new Dimension( thumbColWidth, thumbRowHeight+30 ));
        scrollLayer.setPreferredSize( 
                new Dimension( thumbColWidth, thumbRowHeight+30 ));
        scrollLayer.setVisible( true );
        thumbPane.setRowCount( 1 );
        previewPane.setVisible( true );
        layeredPane.removeAll();
        layeredPane.setVisible( false );
        splitPane.setVisible( true );
        splitPane.setOrientation( JSplitPane.VERTICAL_SPLIT );
        splitPane.remove( previewPane );
        splitPane.remove( scrollLayer );
        splitPane.setTopComponent( previewPane );
        splitPane.setBottomComponent( scrollLayer );
        // Bottom component should not be resized
        splitPane.setResizeWeight( 1.0 );
        Dimension minThumbDim = thumbPane.getMinimumSize();
        scrollLayer.setMinimumSize( new Dimension( 0, minThumbDim.height ) );
        previewPane.setMinimumSize( new Dimension( 0,
                splitPane.getHeight() - 250 - splitPane.getInsets().top ) );
        splitPane.validate();
        getView().validate();
    }   
    
    /**
     Hide the preview pane
     */
    private void setupLayoutNoPreview() {
        // Minimum size is the size of one thumbnail
        thumbScroll.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        thumbScroll.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        int thumbColWidth = thumbPane.getColumnWidth();
        int thumbRowHeight = thumbPane.getRowHeight();
        splitPane.setVisible( false );
        layeredPane.setVisible( true );
        scrollLayer.setMinimumSize(
                new Dimension( thumbColWidth, thumbRowHeight+50));
        scrollLayer.setVisible( true );
        thumbPane.setRowCount( -1 );
        thumbPane.setColumnCount( -1 );
        splitPane.setOrientation( JSplitPane.VERTICAL_SPLIT );
        splitPane.remove( previewPane );
        splitPane.remove( scrollLayer );
        layeredPane.add( scrollLayer, new Integer( 1 ) );
        layeredPane.add( previewPane, new Integer( 2 ) );
        previewPane.setVisible( false );
        thumbScroll.setVisible( true );
        getView().validate();        
    }

    /**
     Show only preview image, no thumbnail pane.
     */
    private void setupLayoutNoThumbs() {
        previewPane.setVisible( true );
    }
    
    void setPreviewSize( int width ) {
        thumbPane.setThumbWidth( width );
        setLayout( layout );
    }

    void handleThumbAreaResize() {
        if ( layout == Layout.PREVIEW_HORIZONTAL_THUMBS ) {
            int newHeight = thumbScroll.getViewport().getHeight();
            thumbPane.setRowHeight( newHeight-10 );
        } else if ( layout == Layout.PREVIEW_VERTICAL_THUMBS ) {
            int newWidth = thumbScroll.getViewport().getWidth();
            thumbPane.setRowHeight( newWidth );
        }
    }
    
    public SwingWorkerTaskScheduler getBackgroundTaskScheduler() {
        return (SwingWorkerTaskScheduler) Photovault.getInstance().getTaskScheduler();
    }

    void setIndexingOngoing( boolean isOngoing ) {
        progressLayer.setInProgress( isOngoing );
    }

    void setIndexingPercentComplete( int percentComplete ) {
        progressLayer.setPercentComplete( percentComplete );
    }
    /**
     Get the component showing thumbnails
     */
    PhotoCollectionThumbView getThumbPane() {
        return thumbPane;
    }
    
    /**
     Get the preview pane component
     */
    JAIPhotoViewer getPreviewPane() {
        return previewPane;
    }
    
    JPanel getCollectionPane() {
        return collectionPane;
    }

    PhotoCollection collection = null;

    /**
     Set the collection that is contorlled by this controller.
     @param c The collection, this can (and most probably is) an detached 
     instance of folder or an unexecuted query.
     */
    void setCollection(PhotoCollection c) {
        collection = c;
        photos = null;
        /*
         Clear Hibernate cache to avoid memory leaks and race conditions with
         command events from already executed commands.
         */
        getPersistenceContext().clear();
        if ( c != null ) {
            photos = c.queryPhotos(getPersistenceContext());
        }
        updateThumbView();
    }
    
    PhotoCollection getCollection() {
        return collection;
    }    
    
    /**
     Loads an icon using class loader of this class
     @param resouceName Name of the icon reosurce to load
     @return The icon or <code>null</code> if no image was found using the given
     resource name.
     */
    private ImageIcon getIcon( String resourceName ) {
        ImageIcon icon = null;
        java.net.URL iconURL = PhotoViewController.class.getClassLoader().getResource(
                resourceName );
        if ( iconURL != null ) {
            icon = new ImageIcon( iconURL );
        }
        return icon;
    }
    
    

}

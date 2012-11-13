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

package org.photovault.swingui.selection;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.photovault.command.CommandChangeListener;
import org.photovault.command.CommandException;
import org.photovault.command.CommandHandler;
import java.util.*;
import java.io.*;
import org.photovault.imginfo.*;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.swingui.*;
import org.photovault.swingui.folderpane.FolderController;
import org.photovault.swingui.framework.AbstractController;
import org.photovault.swingui.framework.DataAccessAction;
import org.photovault.swingui.framework.PersistenceController;

/**
 * PhotoSelectionController contains the application logic for creating and editing 
 * PhotoInfo records in database, i.e. it implements the controller role in MVC 
 * pattern.
 */

public class PhotoSelectionController extends PersistenceController {
    
    static Log log = LogFactory.getLog( PhotoSelectionController.class.getName() );
    
    /**
     Construct a new PhotoSelectionCOntroller that has its own persistence context       
     @param parent Parent of this controller
     */
    public PhotoSelectionController( AbstractController parent ) {
        this( parent, null );
    }
    
    /**
     * Constructs a new PhotoSelectionController that joins an existing
     * persistence context.
     * @param parent Parent of this controller
     * @param persistenceContext The persistence context to join
     */
    public PhotoSelectionController( AbstractController parent, Session persistenceContext ) {
        super( parent, persistenceContext );
        views = new ArrayList<PhotoSelectionView>();
        cmd = new ChangePhotoInfoCommand();
        folderCtrl = new FolderController( this );

        this.registerAction( "save", new DataAccessAction( "Save" ) {
            public void actionPerformed( ActionEvent ev, org.hibernate.Session session ) {
                save();
            }
        });
        
        this.registerAction( "discard", new DataAccessAction( "Discard" ) {
            public void actionPerformed( ActionEvent ev, org.hibernate.Session session ) {
                discard();
            }            
        });
    }

    FolderController folderCtrl = null;
    
    protected PhotoInfo[] photos = null;
    protected boolean isCreatingNew = true;
    
    protected Collection<PhotoSelectionView> views = null;
    
    /**
     Sets the PhotoInfo record that will be edited
     @param photo The photoInfo object that is to be edited. If null the a new PhotoInfo record will be created
     */
    public void setPhoto( PhotoInfo photo ) {
        if ( photo != null ) {
            isCreatingNew = false;
        } else {
            isCreatingNew = true;
        }
        this.photos = new PhotoInfo[1];
        photos[0] = (PhotoInfo) getPersistenceContext().merge( photo );
        cmd = new ChangePhotoInfoCommand( photo.getUuid() );
        for ( PhotoInfoFields f : EnumSet.allOf( PhotoInfoFields.class ) ) {
            updateViews( null, f );
        }
        
        folderCtrl.setPhotos( this.photos, false );
        photosChanged();
    }
    
    /**
     Sets a group of PhotoInfo records that will be edited. If all of the 
     records will have same value for a certain field the views will display 
     this value. Otherwise, <code>null</code> is displayed and if the value 
     is changed in a view, the new value is updated to all controlled objects.
     */
    public void setPhotos( PhotoInfo[] photos ) {
        // Ensure that the photo instances belong to our persistence context
        if ( photos != null ) {
            this.photos = new PhotoInfo[photos.length];
            for ( int n = 0; n < photos.length; n++ ) {
                this.photos[n] = (PhotoInfo) getPersistenceContext().merge( photos[n] );
            }
        } else {
            this.photos = null;
        }
        // If we are editing several photos simultaneously we certainly are not creating a new photo...
        isCreatingNew = false;
        List<UUID> photoIds = new ArrayList<UUID>();
        if ( photos != null ) {
            for ( PhotoInfo p : photos ) {
                photoIds.add( p.getUuid() );
            }
        }
        this.cmd = new ChangePhotoInfoCommand( photoIds );
        for ( PhotoInfoFields f : EnumSet.allOf( PhotoInfoFields.class ) ) {
            updateViews( null, f );
        }
        folderCtrl.setPhotos( this.photos, false );
        photosChanged();
    }
    
    /**
     Callback that derived classes can override to add their own actions
     that will be executed after photos have been changed.
     */
    protected void photosChanged() {}
    
    /**
     Sets the view that is contorlled by this object
     @param view The controlled view
     */
    public void setView( PhotoSelectionView view ) {
        views.clear();
        addView( view );
        folderCtrl.setViews( views );
    }
    
    /**
     Add a new view to those that are controlled by this object.
     TODO: Only the new view should be set to match the model.
     @param view The view to add.
     */
    public void addView( PhotoSelectionView view ) {
        views.add( view );
        for ( PhotoInfoFields f : EnumSet.allOf( PhotoInfoFields.class ) ) {
            updateViews( null, f );
        }
        folderCtrl.setViews( views );
    }
    
    /**
     Returns the hotoInfo record that is currently edited.
     */
    public PhotoInfo getPhoto() {
        PhotoInfo photo = null;
        if ( photos != null ) {
            photo = photos[0];
        }
        return photo;
    }
    
    /**
     Get the photos in current model.
     */
    public PhotoInfo[] getPhotos() {
        if ( photos != null ) {
            return photos.clone();
        }
        return null;
    }
    
    public FolderController getFolderController() {
        return folderCtrl;
    }
        
    /**
     Get the change command that contains all changes made to selection.
     */
    public ChangePhotoInfoCommand getChangeCommand() {
        return cmd;
    }
    
    /**
     Save the modifications made to the PhotoInfo record
     */
    public void save() {
        /*
         Use change listener to ensure that all changes are merged into current 
         persistence context before returning from this method.
         */
        CommandChangeListener l = new CommandChangeListener() {
            public void entityChanged( Object entity ) {
                if ( entity instanceof PhotoInfo ) {
                    PhotoInfo changedPhoto = (PhotoInfo) entity;
                    getPersistenceContext().merge( changedPhoto );
                }
            }
        };
        CommandHandler cmdHandler = getCommandHandler();
        cmdHandler.addChangeListener( l );
        try {
            cmdHandler.executeCommand( cmd );
        } catch( CommandException e ) {
            log.error ( "Exception while saving: ", e );
            JOptionPane.showMessageDialog( getView(), 
                    "Error while saving changes:\n" + e.getMessage(), 
                    "Save Error", JOptionPane.ERROR_MESSAGE );
        }
        cmdHandler.removeChangeListener( l );
        setPhotos( photos );
    }
    
    /**
     Discards modifications done after last save
     */
    public void discard() {
        setPhotos( photos );
    }
    
    /**
     Adds a new listener that will be notified of events related to this object
     */
    public void addListener( PhotoInfoListener l ) {
    }

    
    // The original file that is to be added to database (if we are creating a new PhotoInfo object)
    // If we are editing an existing PhotoInfo record this is null
    File originalFile = null;
        
    ChangePhotoInfoCommand cmd;
    
    /**
     Get the current field value or values
     */
    @SuppressWarnings( "unchecked" )
    public Set getFieldValues( PhotoInfoFields field ) {
        Set values = new HashSet();
        Object value = cmd.getField( field );
        if ( value != null ) {
            values.add( value );
        } else if ( photos != null) {            
            for ( PhotoInfo p : photos ) {
                try {
                    value = PhotoInfoFields.getFieldValue( p, field );
                } catch (Exception ex) {
                    log.error( ex.getMessage() );
                    ex.printStackTrace();
                }
                if ( value != null ) {
                    values.add( value );
                }
            }
        }
        return values;
    }

    /**
     Get the original field values before potential modifications done in this
     controller.
     @param fiel The field which values to get
     @return Set of all values of field that some photo in the selection have.
     */
    public Set getOriginalFieldValues( PhotoInfoFields field ) {
        Set values = new HashSet();
        if ( photos != null) {
            for ( PhotoInfo p : photos ) {
                Object value = null;
                try {
                    value = PhotoInfoFields.getFieldValue( p, field );
                } catch (Exception ex) {
                    log.error( ex.getMessage() );
                    ex.printStackTrace();
                } 
                values.add( value );
            }
        }
        return values;
    }
    
    /**
     Update all views with the current value of a field.
     @param src The view that has initiated value change and which therefore should
     not be updated. If <code>null</code>, update all views.
     @param field The field that will be updated.
     */
    private void updateViews( PhotoSelectionView src, PhotoInfoFields field ) {
        @SuppressWarnings( "unchecked" )
        List refValues = new ArrayList( getOriginalFieldValues( field ) );
        Object value = cmd.getField( field );
        if ( value == null && refValues.size() == 1 ) {
            value = refValues.get(0);
        }
        for ( PhotoSelectionView view : views ) {
            if ( view != src ) {
                view.setField( field, value, refValues );
            }
            // view.setFieldMultivalued( field, isMultivalued );
        }
    }
    
    /**
     This method is called by views to inform that value of a field has been changed
     @param view The view that called this method
     @param field The changed field
     @param newValue New value for field.
     */
    public void viewChanged( PhotoSelectionView view, PhotoInfoFields field, Object newValue ) {
        Set fieldValues = getFieldValues( field );
        if ( fieldValues.size() != 1 || !fieldValues.contains( newValue ) ) {
            cmd.setField( field, newValue );
        }
        updateViews( view, field );
    }
}

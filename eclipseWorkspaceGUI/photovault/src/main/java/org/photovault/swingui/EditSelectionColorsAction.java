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

package org.photovault.swingui;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.swingui.color.ColorSettingsDlg;
import org.photovault.swingui.color.ColorSettingsDlgController;

/**
 This action displays ColorSettingsDlg for the currently selected photos.
 */
public class EditSelectionColorsAction extends AbstractAction
        implements SelectionChangeListener{
    
    /**
     Thumbnail view whose selection is followed by the dialog controlled
     by this action.
     */
    private PhotoCollectionThumbView selectionView = null;
    
    /**
     Preview control that shows the adjustments made
     */
    private JAIPhotoViewer preview = null;
    
    /**
     The controller for color settings dialog that is displayed when this 
     action is fired
     */
    private ColorSettingsDlgController colorDlgCtrl = null;
    
    /** Creates a new instance of EditSelectionColorsAction
     */
    public EditSelectionColorsAction(PhotoCollectionThumbView selectionView,
            JAIPhotoViewer preview,
            String text, ImageIcon icon, String desc, int mnemonic ) {
        super( text, icon );
        this.selectionView = selectionView;
        this.preview = preview;
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, new Integer( mnemonic) );
        //	putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.CTRL_MASK ) );
        selectionView.addSelectionChangeListener( this );
        setEnabled( selectionView.getSelectedCount() > 0 );
    }
    
    /**
     Set the preview control used by this action.
     @deprecated TODO: this is only needed
     since preview control is not known when this action is created in
     PhotoCollectionThumbView. Get rid of this!!!
     */
    public void setPreviewCtrl( JAIPhotoViewer preview ) {
        this.preview = preview;
        if ( colorDlgCtrl != null ) {
//            colorDlgCtrl.setPreviewControl( preview );    
        }
    }
    
    
    public void actionPerformed(ActionEvent actionEvent) {
        if ( selectionView.getSelectedCount() == 0 ) {
            return;
        }
        Collection selection = selectionView.getSelection();
        Iterator iter = selection.iterator();
        PhotoInfo[] selectedPhotos = new PhotoInfo[selection.size()];
        int i = 0;
        while ( iter.hasNext() && i < selectedPhotos.length ) {
            selectedPhotos[i++] = (PhotoInfo) iter.next();
        }
        
        if ( colorDlgCtrl == null ) {
            // Try to find the frame in which this component is in
            Frame frame = null;
            Container c = selectionView.getTopLevelAncestor();
            if ( c instanceof Frame ) {
                frame = (Frame) c;
            }
            
            colorDlgCtrl = new ColorSettingsDlgController( frame, selectionView.ctrl, selectionView.ctrl.getPersistenceContext() );
            colorDlgCtrl.setPreviewControl( preview );
        } 
        colorDlgCtrl.setPhotos( selectedPhotos );        
        colorDlgCtrl.showDialog();
    }
    
    
    /**
     Called when the selection the view that controls this action is changed
     */
    public void selectionChanged(SelectionChangeEvent e) {
        boolean enabled = false;
        PhotoInfo[] photos = null;
        if ( selectionView.getSelectedCount() > 0 ) {
            enabled = true;
            Collection selection = selectionView.getSelection();
            photos = new PhotoInfo[selection.size()];
            Iterator iter = selection.iterator();
            int n = 0;
            while ( iter.hasNext() && n < photos.length ) {
                photos[n] = (PhotoInfo) iter.next();
                n++;
            }
            
        }
        setEnabled( enabled );
        if ( colorDlgCtrl != null /*&& colorDlg.isVisible() */) {
                colorDlgCtrl.setPhotos( photos );
        }
    }
    
}

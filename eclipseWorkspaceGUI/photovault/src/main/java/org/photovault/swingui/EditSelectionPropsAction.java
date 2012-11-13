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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.Iterator;
import org.hibernate.classic.Session;
import org.photovault.imginfo.*;
import org.photovault.imginfo.PhotoInfo;

class EditSelectionPropsAction extends AbstractAction implements SelectionChangeListener {

    /**
       Constructor.
       @param view The view this action object is associated with. 
    */
    public EditSelectionPropsAction( PhotoCollectionThumbView view, String text, ImageIcon icon,
                      String desc, int mnemonic) {
	super( text, icon );
	this.view = view;
	putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, new Integer( mnemonic) );
	putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, ActionEvent.ALT_MASK ) );
	view.addSelectionChangeListener( this );
	setEnabled( view.getSelectedCount() > 0 );
    }

    public void selectionChanged( SelectionChangeEvent e ) {
        setEnabled( view.getSelectedCount() > 0 );
        if ( propertyDlg != null && propertyDlg.isVisible() ) {
            if ( view.getSelectedCount() > 0 ) {
                Collection selection = view.getSelection();
                PhotoInfo[] photos = new PhotoInfo[selection.size()];
                Iterator iter = selection.iterator();
                int n = 0;
                while ( iter.hasNext() && n < photos.length ) {
                    photos[n++] = (PhotoInfo) iter.next();
                }
                propertyDlg.setPhotos( photos );
            } else {
                // No photos selected
                propertyDlg.setPhotos( null );
            }            
        }
    }
    
    public void actionPerformed( ActionEvent ev ) {
        if ( view.getSelectedCount() == 0 ) {
            return;
        }
	Collection selection = view.getSelection();
        Iterator iter = selection.iterator();
        PhotoInfo[] selectedPhotos = new PhotoInfo[selection.size()];
        int i = 0;
        while ( iter.hasNext() && i < selectedPhotos.length ) {
            selectedPhotos[i++] = (PhotoInfo) iter.next();
        }
	
        // Try to find the frame in which this component is in
        Frame frame = null;
        Container c = view.getTopLevelAncestor();
        if ( c instanceof Frame ) {
            frame = (Frame) c;
        }

        if (propertyDlg == null ) {
            propertyDlg = new PhotoInfoDlg( view.ctrl, (Session) view.ctrl.getPersistenceContext(), false, selectedPhotos );
        } else {
            propertyDlg.setPhotos( selectedPhotos );
        }

            propertyDlg.showDialog();
    }

    PhotoCollectionThumbView view;
    PhotoInfoDlg propertyDlg = null;


}
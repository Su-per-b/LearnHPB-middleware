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

package org.photovault.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import org.photovault.common.PhotovaultException;
import org.photovault.imginfo.PhotoInfo;

/**
  This class provides support for deleting a photo from UI. When the action
 is executed it
 <ul>
 <li>Asks for user confirmation</li>
 <li>Deletes all instances that can be deleted</li>
 <li>If all instances were deleted, deletes the PhotoInfo object itself
 </ul>
 
 */
public class DeletePhotoAction extends AbstractAction implements SelectionChangeListener {

    PhotoCollectionThumbView view;
    
    /** 
     Creates a new instance of DeletePhotoAction
     @param view The thumbnail view from which the selection is determined
     @param text Text that is displayed in menus etc for this action
     @param icon Icon for this action
     @param desc Longer description text for this action
     @param mnemonic Shortcut for this action
     
     */
    public DeletePhotoAction(PhotoCollectionThumbView view, String text, ImageIcon icon,
                      String desc, int mnemonic) {
	super( text, icon );
	this.view = view;
	putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, new Integer( mnemonic) );
	putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ) );
	view.addSelectionChangeListener( this );
	setEnabled( view.getSelectedCount() > 0 );
    }
    
    /**
     Execute the action & delete photos.
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Collection photosToDelete = view.getSelection();
        String warning = "All instances of selected photo" 
                + ((photosToDelete.size() > 1) ? "s" : "")
                + "\nwill be permanently deleted."
                + "\n Do you want to proceed?";
        if ( JOptionPane.showConfirmDialog( null, 
                warning,
                "Deleting " + view.getSelectedCount() + " photos", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE )
                == JOptionPane.YES_OPTION ) {
            Iterator iter = photosToDelete.iterator();
            boolean allDeleted = true;
            while ( iter.hasNext() ) {
                PhotoInfo photo = (PhotoInfo) iter.next();
                try {
                    photo.delete( true );
                    view.removeFromSelection( photo );
                } catch (PhotovaultException ex) {
                    allDeleted = false;
                }
            }
            if ( !allDeleted ) {
                JOptionPane.showMessageDialog( null, 
                        "Photovault was not able to delete all instances of photos",
                        "Some photos not deleted", JOptionPane.INFORMATION_MESSAGE );
            }
            
        }
    }


    /**
     This method is called by associated view when selection is changed. 
     */
    public void selectionChanged(SelectionChangeEvent e) {
        setEnabled( view.getSelectedCount() > 0 );
    }
    
}

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

import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 This action initates crop operation on the currently selected photo.
 */
public class CropPhotoAction extends AbstractAction 
        implements PhotoViewListener {

    JAIPhotoView view = null;

    /** 
     Creates a new instance of CropPhotoAction
     @param view @see JAIPhotoView in hwich the crop operation will be performed
     @param text Text to show for this action in menus etc.
     @param desc Description of this action
     @param mnemonic Mnemonic to use for this action in menus
     @param accelerator Accelerator key stroke for this action
     */
    public CropPhotoAction (
            JAIPhotoView view,
            String text, ImageIcon icon,
            String desc, int mnemonic, KeyStroke accelerator ) {
	super( text, icon );
        this.view = view;
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, new Integer( mnemonic ) );
	putValue( ACCELERATOR_KEY, accelerator );
	view.addPhotoViewListener( this );
        view.addComponentListener( new ComponentListener () {
            public void componentResized(ComponentEvent e) {
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
                checkIfEnabled();
            }

            public void componentHidden(ComponentEvent e) {
                checkIfEnabled();
            }
            
        });
        
        // JaiPhotoView is contained in JaiPhotoViewer, which can
        // be disabled as well. So listen hierarchy change events to catch this
        view.addHierarchyListener( new HierarchyListener() {
            public void hierarchyChanged(HierarchyEvent e) {
                checkIfEnabled();
            }        
        });
        checkIfEnabled();
    }

    /**
     Check whether the action should be enabled or not. It is enabled if 
     <ul>
     <li>View is showing an image</li>
     <li>View itself is visible</li>
     </ul>
     */
    private void checkIfEnabled() {
	setEnabled( (view.getImage() != null) && view.isShowing() );                    
    }
    
    /**
     Called when the action is performed. Moves selection to next or previous photo,
     depending on @see direction.
     @param e The action event
     */
    public void actionPerformed(ActionEvent e) {
        view.setDrawCropped( false );
    }    

    public void imageChanged(PhotoViewEvent e) {
	checkIfEnabled();     
    }
}

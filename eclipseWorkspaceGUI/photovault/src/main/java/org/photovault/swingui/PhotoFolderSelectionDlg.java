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
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.imginfo.*;
import java.io.*;
import org.photovault.folder.*;

/**
   This is a simple dialog for selecting a folder.
*/

public class PhotoFolderSelectionDlg extends JDialog {
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( PhotoFolderSelectionDlg.class.getName() );

    static final String DIALOG_TITLE = "Select folder";
    
    /**
       Constructor. Creates a dialog with the specified window as a parent.
       @param owner Owner of the dialog
       @param modal If true, a modal dialog is created
    */
    public PhotoFolderSelectionDlg( Frame owner, boolean modal ) {
	super( owner, DIALOG_TITLE, modal );
	    
	createUI();
    }

    PhotoFolderTree tree = null;
    PhotoFolderTreeController treeCtrl = null;
    
    PhotoFolder selectedFolder = null;

    /**
       Return the selected folder or null if no folder is selected
    */
    public PhotoFolder getSelectedFolder() {
	return selectedFolder;
    }
	
    
    /**
       Creates the UI components needed for this dialog.
    */
    protected void createUI() {
        // TODO: fix correct controller here!!!
        treeCtrl = new PhotoFolderTreeController( null, null );
        treeCtrl.setCommandHandler( new PhotovaultCommandHandler( null ) );
	tree = treeCtrl.folderTree;
	getContentPane().add( tree, BorderLayout.NORTH );

	// Create a pane for the buttols
	JButton okBtn = new JButton( "OK" );
	okBtn.addActionListener( new ActionListener() {
		public void actionPerformed( ActionEvent e ) {
		    try {
			selectedFolder = treeCtrl.getSelected();
		    } catch ( Exception ex ) {
			log.warn( "problem while saving changes: " + ex.getMessage() );
		    }
		    setVisible( false );
		}
	    } );
		    

	JButton cancelBtn = new JButton( "Cancel" );
	cancelBtn.addActionListener( new ActionListener() {
		public void actionPerformed( ActionEvent e ) {
		    selectedFolder = null;
		    setVisible( false );
		}
	    } );
	    
	JPanel buttonPane = new JPanel();
	buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
	buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	buttonPane.add(Box.createHorizontalGlue());
	buttonPane.add(okBtn);
	buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	buttonPane.add(cancelBtn);
	getContentPane().add( buttonPane, BorderLayout.SOUTH );

	getRootPane().setDefaultButton( okBtn );

	pack();
    }

    /**
       Shows the dialog.
       @return True if user selected folder, false otherwise.
    */
	
    public boolean showDialog() {
	setVisible( true );
	return (selectedFolder != null);
    }

}    

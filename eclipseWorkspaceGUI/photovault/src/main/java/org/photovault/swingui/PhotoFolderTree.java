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

import java.io.IOException;
import java.io.Writer;
import javax.swing.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import javax.swing.tree.*;
import org.photovault.folder.*;
import java.util.*;

/**
   PhotoFolderTree is a control for displaying a tree structure of a PhotoFolder and its subfolders.
*/

public class PhotoFolderTree extends JPanel {
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( PhotoFolderTree.class.getName() );
    
    JTree tree = null;
    JScrollPane scrollPane = null;
    JPopupMenu popup = null;
    PhotoFolderTreeController ctrl = null;
    
    public PhotoFolderTree( PhotoFolderTreeController ctrl ) {
	super();
        this.ctrl = ctrl;
	// model.setRoot( root );
	createUI();
    }

    

    /**
       Adds a listener to listen for selection changes
    */
    public void addPhotoFolderTreeListener( PhotoFolderTreeListener l ) {
	folderTreeListeners.add( l );
    }

    /**
       removes a listener for selection changes
    */
    public void removePhotoFolderTreeListener( PhotoFolderTreeListener l ) {
	folderTreeListeners.remove( l );
    }


    Vector folderTreeListeners = new Vector();
    
    private void createUI() {
	setLayout( new BorderLayout() );
	tree = new JTree( ctrl.model );
	tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
	tree.addTreeSelectionListener( ctrl );
	DropTarget dropTarget = new DropTarget( tree, new PhotoTreeDropTargetListener( tree ) );
	// 	tree.setTransferHandler( new PhotoCollectionTransferHandler(null) );
	scrollPane = new JScrollPane( tree );
	scrollPane.setPreferredSize( new Dimension( 200, 500 ) );
	add( scrollPane, BorderLayout.CENTER );

	// Set up the popup menu
	popup = new JPopupMenu();
        // TODO: implement folder properties
	JMenuItem renameItem = new JMenuItem( "Rename" );
	renameItem.addActionListener( ctrl );
	renameItem.setActionCommand( FOLDER_RENAME_CMD );
	JMenuItem deleteItem = new JMenuItem( "Delete" );
	deleteItem.addActionListener( ctrl );
	deleteItem.setActionCommand( FOLDER_DELETE_CMD );
	JMenuItem newFolderItem = new JMenuItem( "New folder..." );
	newFolderItem.addActionListener( ctrl );
	newFolderItem.setActionCommand( FOLDER_NEW_CMD );
	popup.add( newFolderItem );
	popup.add( renameItem );
	popup.add( deleteItem );
//	popup.add( propsItem );

	MouseListener popupListener = new PopupListener();
	tree.addMouseListener( popupListener );
        

    }

    /**
       This helper class from Java Tutorial handles displaying of popup menu on correct mouse events
    */
    class PopupListener extends MouseAdapter {
	public void mousePressed(MouseEvent e) {
	    maybeShowPopup(e);
	}
	
	public void mouseReleased(MouseEvent e) {
	    maybeShowPopup(e);
	}
	
	private void maybeShowPopup(MouseEvent e) {
	    if (e.isPopupTrigger()) {
		popup.show(e.getComponent(),
			   e.getX(), e.getY());
	    }
	}
    }
    
    static final String FOLDER_PROPS_CMD = "FOLDER_PROPS_CMD";
    static final String FOLDER_RENAME_CMD = "FOLDER_RENAME_CMD";
    static final String FOLDER_DELETE_CMD = "FOLDER_DELETE_CMD";
    static final String FOLDER_NEW_CMD = "FOLDER_NEW_CMD";
    
    /**
     Helper method for writing the folder hierarchy
     @param w Writer into which this hierarchy is written
     @param root root folder of the hierarchy
     @param indent Indentation of the top folder
     */
    
    private void debugPrintFolderTree( Writer w, PhotoFolder root, int indent ) 
            throws IOException {
        String strIndent = "                                 ".substring( 0, indent );
        w.write( strIndent );
        w.write( getName() );
        w.write( "\n" );
        for ( PhotoFolder f : root.getSubfolders() ) {
            debugPrintFolderTree( w, f, indent+2 );
        }        
    }
}


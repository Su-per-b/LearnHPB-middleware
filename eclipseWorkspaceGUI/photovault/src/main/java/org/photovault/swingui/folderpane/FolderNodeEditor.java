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

package org.photovault.swingui.folderpane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.EventObject;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import org.photovault.folder.PhotoFolder;

/**
 This class implements the renderer and editor used to display folders in
 {@link FolderTreePane}. It differs from normal tree cell rendeded so that
 it displays also an checkbox for each folder. Is also provides 3rd state 
 for the check box display to indicate that only part of the photos belong to
 that folder.
 */

class FolderNodeEditor 
        extends JPanel 
        implements TreeCellEditor, TreeCellRenderer, ActionListener {
    
    /**
     FolderNode that is currently edited
     */
    FolderNode node = null;
    /**
     Check box used to render/edit the node
     */
    ThreeStateCheckBox check;
    /**
    Label containing the folder name
    */     
    JLabel name;
    /**
     Folder icon
     */
    JLabel icon;
    /**
     Icon used to represent non-expanded folder
     */
    Icon closedIcon = null;
    /**
     Icon used to represent expanded folder
     */
    Icon expandedIcon = null;
    /**
     The tree that will be notified about edits
     */
    FolderTreePane treePane;

    Color nonSelectedBkg = null;
    
    Color selectedBkg = null;
    
    
    /**
     Constructs a FolderNodeEditor
     @param treePane The tree that will be notified about edits.
     */
    public FolderNodeEditor( FolderTreePane treePane ) {
        super();
        this.treePane = treePane;
        setLayout( new BoxLayout(this, BoxLayout.X_AXIS) );
        check = new ThreeStateCheckBox();
        check.setBackground( Color.WHITE );
        check.addActionListener( this );
        name = new JLabel();
        icon = new JLabel();
        add( icon );
        add( check );
        add( name );
        this.setBackground( Color.WHITE );
        closedIcon = getIcon( "folder_icon.png");
        expandedIcon = getIcon( "folder_expanded_icon.png" );
        DefaultTreeCellRenderer dr = new DefaultTreeCellRenderer();
        name.setFont( dr.getFont() );
        selectedBkg = dr.getBackgroundSelectionColor();
        nonSelectedBkg = dr.getBackgroundNonSelectionColor();
    }

    /**
     Get the component used for edits. Sets up the editor according to parameters 
     and returns <code>this</code>.
     @param jTree      The tree
     @param object     Value of the current node
     @param isSelected Whether the node is selected
     @param isExpanded Whether the node is expanded
     @param isLeaf     Whether the node is leaf
     @param row        Row if the node
     */
    public Component getTreeCellEditorComponent(JTree jTree, Object object, 
            boolean isSelected, boolean isExpanded, boolean isLeaf, int row ) {
        node = (FolderNode) object;
        setupComponent( jTree, node, isSelected, isExpanded, isLeaf, row, true );
        return this;
    }

    /**
     Get the component used for rendering the field. Sets up the editor 
     according to parameters and returns <code>this</code>.
     @param jTree      The tree
     @param object     Value of the current node
     @param isSelected Whether the node is selected
     @param isExpanded Whether the node is expanded
     @param isLeaf     Whether the node is leaf
     @param row        Row if the node
     @param hasFocus   Whether the node has focus
     */
    public Component getTreeCellRendererComponent(JTree jTree, Object object, 
            boolean isSelected, boolean isExpanded, boolean isLeaf, 
            int row, boolean hasFocus) {
        if (object instanceof FolderNode) {
            node = (FolderNode) object;
            setupComponent( jTree, node, isSelected, isExpanded, isLeaf, row, hasFocus );
        }
        return this;
    }        
    
    
    /**
     Set up the component based on information about tree and current folder node
     @param jTree      The tree
     @param node       Value of the current node
     @param isSelected Whether the node is selected
     @param isExpanded Whether the node is expanded
     @param isLeaf     Whether the node is leaf
     @param row        Row if the node
     @param hasFocus   Whether the node has focus     
     */
    private void setupComponent( JTree jTree, FolderNode node,
            boolean isSelected, boolean isExpanded, boolean isLeaf,
            int row, boolean hasFocus ) {
        icon.setIcon( isExpanded ? expandedIcon : closedIcon );
        PhotoFolder f = node.getFolder();
        setupFolder( f );
        name.setBackground( isSelected ? selectedBkg : nonSelectedBkg );
    }

    /**
     Set up the information of current folder (name & whether it has photos)
     @param f  The current folder
     */
    private void setupFolder( PhotoFolder f ) {
        String labelStr = f.getName();
        if ( node.containsPhotos() ) {
            check.setSelected(true);
            if ( node.containsAllPhotos() ) {
                labelStr = "<html><strong>" + labelStr + "</strong>";
                check.setUndef( false );
            } else {
                labelStr = "<html><font color=\"gray\"><strong>" + labelStr + "</strong></font>";
                check.setUndef( true );
            }
        } else {
            check.setSelected( false );
            check.setUndef( false );
        }
        name.setText( labelStr );        
    }
    
    /**
     Get the value of the currently edited node.
     */
    public Object getCellEditorValue() {
        return node;
    }

    /**
     Return whether the cell is editable (true always)
     */
    public boolean isCellEditable(EventObject eventObject) {
        return true;
    }

    public boolean shouldSelectCell(EventObject eventObject) {
        return true;
    }

    public boolean stopCellEditing() {
        return true;
    }

    public void cancelCellEditing() {
    }

    public void addCellEditorListener(CellEditorListener cellEditorListener) {
    }

    public void removeCellEditorListener(CellEditorListener cellEditorListener) {
    }
    
    /**
     Loads an icon using class loader of this class
     @param resouceName Name of the icon reosurce to load
     @return The icon or <code>null</code> if no image was found using the given
     resource name.
     */
    private ImageIcon getIcon(String resourceName) {
        ImageIcon icon = null;
        URL iconURL = FolderNodeEditor.class.getClassLoader().getResource( resourceName );
        if (iconURL != null) {
            icon = new ImageIcon(iconURL);
        }
        return icon;
    }

    /**
     Called when the check box is clicked
     @param actionEvent The action event
     */
    public void actionPerformed(ActionEvent actionEvent) {
        if ( actionEvent.getSource() == check ) {
                check.setUndef( false );
            // Add or remove photos from current node depending on checkbox state
            if ( check.isSelected() ) {
                treePane.addAllToSelectedFolder();
            } else {
                treePane.removeAllFromSelectedFolder();
            }
            setupFolder( treePane.getSelectedFolder() );
        }
    }
}
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

import javax.swing.SwingUtilities;
import org.photovault.folder.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import org.photovault.imginfo.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
   This class implements a TreeModel for PhotoFolders.
*/

public class PhotoFolderTreeModel implements TreeModel, PhotoFolderChangeListener {
    static Log log = LogFactory.getLog( PhotoFolderTreeModel.class.getName() );

    FolderNodeMapper nodeMapper = null;
    
    PhotoFolderTreeController ctrl = null;
    PhotoFolderDAO folderDAO = null;
    
    /**
     Constructs the tee model
     @param mapper The {@link FolderNodeMapper} object used to map folder hierarchy 
                   to objects that represent the nodes in tree.
     
     */
    public PhotoFolderTreeModel( FolderNodeMapper mapper ) {
        super();
        nodeMapper = mapper;
    }
    
    /**
     Construct a fodler tree model using default mapping (i.e. the PhotoFolders
     are used as tree nodes themselves.
     */
    public PhotoFolderTreeModel() {
        super();
        nodeMapper = new DefaultFolderNodeMapper();
    }
    
    public void setController( PhotoFolderTreeController c ) {
        ctrl = c;
    }
    
    /**
     Default mapping from folders to tree nodes that just returns the node itself.
     */
    static class DefaultFolderNodeMapper implements FolderNodeMapper {
        public Object mapFolderToNode(PhotoFolder f) {
            return f;
        }

        public PhotoFolder mapNodeToFolder(Object o) {
            return (PhotoFolder) o;
        }
        
    }
    
    // implementation of javax.swing.tree.TreeModel interface

    /**
     * Returns a child of a given node
     * @param obj <description>
     * @param childNum <description>
     * @return <description>
     */
    public Object getChild(Object obj, int childNum)
    {
	PhotoFolder folder = nodeMapper.mapNodeToFolder( obj );
	PhotoFolder child = folder.getSubfolder( childNum );
        return nodeMapper.mapFolderToNode( child );
    }

    PhotoFolder rootFolder = null;
    
    /**'
       Sets the root folder for the tree
       @param root the new root folder
    */
    public void setRoot( PhotoFolder root ) {
	if ( rootFolder != null ) {
	    rootFolder.removePhotoCollectionChangeListener( this );
	}
	rootFolder = root;
	rootFolder.addPhotoCollectionChangeListener( this );
	// log.warn( "New root " + root.getName() + " - subfolderCount: " + getChildCount( root ) );
    }
    
    /**
    Returns the node representing root folder of the tree.
     */
    public Object getRoot()
    {
        if ( rootFolder == null ) {
            if ( folderDAO == null ) {
                folderDAO = ctrl.getDAOFactory().getPhotoFolderDAO();
            }
            rootFolder = folderDAO.findRootFolder();
        }
	return nodeMapper.mapFolderToNode( rootFolder );
    }

    /**
     * Returns the number of subfolders a given node has
     * @param obj The PhotoFolder we are interested in
     * @return Number of subfolders <code>obj</code> has.
     */
    public int getChildCount(Object obj)
    {
	PhotoFolder folder = nodeMapper.mapNodeToFolder( obj );
	log.debug( "childCount for " + folder.getName() + ": " + folder.getSubfolderCount() );
	return folder.getSubfolderCount();
    }

    /**
     * Returns true if the given subfolder has no subfolders
     * @param obj The node to inspect
     */
    public boolean isLeaf(Object obj)
    {
	PhotoFolder folder = nodeMapper.mapNodeToFolder( obj );
	int subfolderCount = folder.getSubfolderCount();
	log.debug( "subfolderCount for " + folder.getName() + ": " + subfolderCount );
	return (subfolderCount == 0);
    }

    /**
     *
     * @param param1 <description>
     * @param param2 <description>
     */
    public void valueForPathChanged(TreePath param1, Object param2)
    {
	// TODO: implement this javax.swing.tree.TreeModel method
    }

    /**
     * Get the index of a given child
     * @param parent The parent folder
     * @param child the child folder
     * @return index of the child folder inside the parent. If parent or child in <code>null</code> returns -1.
     */
    public int getIndexOfChild(Object parent, Object child)
    {
	PhotoFolder parentFolder = nodeMapper.mapNodeToFolder( parent );
        PhotoFolder childFolder = nodeMapper.mapNodeToFolder( child );
	int childIndex = -1;
	if ( parent != null && child != null ) {
	    int childCount = parentFolder.getSubfolderCount();
	    for ( int n = 0; n < childCount; n++ ) {
		if ( parentFolder.getSubfolder( n ) == childFolder ) {
		    childIndex = n;
		    break;
		}
	    }
	}
	return childIndex;
    }


    private Vector treeModelListeners = new Vector();
    
    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     */
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.addElement(l);
    }
    
    /**
     * Removes a listener previously added with addTreeModelListener().
     */
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.removeElement(l);
    }

    /**
       Sends a strucure changed event to all listeners of this object. Currently only this type of
       event is supported, in future also other types of tree model events should be considered.
    */
      
    protected void fireTreeModelEvent( TreeModelEvent e ) {
	Iterator iter = treeModelListeners.iterator();
        final TreeModelEvent fe = e;
	while ( iter.hasNext() ) {
	    final TreeModelListener l = (TreeModelListener) iter.next();
            SwingUtilities.invokeLater( new Runnable() {
                public void run() {
        	    l.treeStructureChanged( fe );                    
                }
            });
	    log.debug( "Sending treeModelEvent" );
	}
    }
    
    // implementation of imginfo.PhotoCollectionChangeListener interface

    /**
       This method is called when some of the tree nodes has changed. Currently it constructs TreePath to
       the changed object and sends a strucure changed event to listeners of the model.

     */
    public void photoCollectionChanged(PhotoCollectionChangeEvent e)
    {
	PhotoFolder changedFolder = (PhotoFolder)e.getSource();
	if ( e instanceof PhotoFolderEvent  ){
	    changedFolder = ((PhotoFolderEvent)e).getSubfolder();
	}
	Object[] path = findFolderPath( changedFolder );
	
	// Construct the correct event
	TreeModelEvent treeEvent = new TreeModelEvent( changedFolder, path );
	log.debug( "collectionChanged " + path.length );
	fireTreeModelEvent( treeEvent );
    }

    public void subfolderChanged( PhotoFolderEvent e ) {
	PhotoFolder changedFolder = (PhotoFolder)e.getSource();
	if ( e instanceof PhotoFolderEvent ) {
	    changedFolder = ((PhotoFolderEvent)e).getSubfolder();
	}
	Object[] path = findFolderPath( changedFolder );
	
	// Construct the correct event
	TreeModelEvent treeEvent = new TreeModelEvent( changedFolder, path );
	log.debug( "subfolderChanged " + path.length );
	fireTreeModelEvent( treeEvent );
	
    }

    public void structureChanged( PhotoFolderEvent e ) {
	PhotoFolder changedFolder = (PhotoFolder)e.getSource();
	if ( e instanceof PhotoFolderEvent ) {
	    changedFolder = ((PhotoFolderEvent)e).getSubfolder();
	}
	Object[] path = findFolderPath( changedFolder );
	
	// Construct the correct event
	TreeModelEvent treeEvent = new TreeModelEvent( changedFolder, path );
	log.debug( "structureChanged " + path.length );
	fireTreeModelEvent( treeEvent );
    }

    /**
     Construc a tree path from root to given folder
     @param folder The folder at the end of the requested path.
     */
    protected Object[] findFolderPath( PhotoFolder folder ) {
	// Construct a TreePath for this object

	// Number of ancestors between this 
	Vector ancestors = new Vector();
	// Add first the final folder
	ancestors.add( folder );
	log.debug( "starting finding path for " + folder.getName() );
	PhotoFolder ancestor = folder.getParentFolder();
	while ( ancestor != rootFolder && ancestor != null ) {
	    log.debug( "ancestor " + ancestor.getName() );
	    ancestors.add( ancestor );
	    ancestor = ancestor.getParentFolder();
	}
	// Add the root folder to the ancestors
	if ( ancestor == rootFolder ) {
	    ancestors.add( ancestor );
	}

	// Now ancestors has the path but in reversed order.
	Object[] path = new Object[ ancestors.size() ];
	for ( int m = 0; m < ancestors.size(); m++ ) {
	    PhotoFolder f = (PhotoFolder) ancestors.get( ancestors.size()-1-m );
            path[m] = nodeMapper.mapFolderToNode( f );
	}
	return path;
    }	

}
  

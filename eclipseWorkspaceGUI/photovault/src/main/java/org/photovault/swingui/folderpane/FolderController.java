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

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.tree.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.imginfo.ChangePhotoInfoCommand;
import org.photovault.imginfo.PhotoCollectionChangeEvent;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.swingui.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import org.photovault.folder.*;
import org.photovault.swingui.framework.PersistenceController;
import org.photovault.swingui.selection.PhotoSelectionController;
import org.photovault.swingui.selection.PhotoSelectionView;

public class FolderController {

    Log log = LogFactory.getLog( FolderController.class.getName() );

    PhotoFolderTreeModel treeModel;
    FolderToFolderNodeMapper nodeMapper;
    
    PhotoFolderDAO folderDAO = null;
    PhotoSelectionController parentCtrl;
    
    public FolderController( PhotoSelectionController parentCtrl ) {
	this.parentCtrl = parentCtrl;
        addedToFolders = new HashSet();
	removedFromFolders = new HashSet();
        expandedFolders = new HashSet();
        initTree();
    }

    Collection<PhotoSelectionView> views = null;
    
    public void setViews( Collection views ) {
	this.views = views;
	updateAllViews();
    }

    public void updateAllViews() {
        for( PhotoSelectionView view : views ) {
            view.setFolderTreeModel( treeModel );
        }
        expandTreePaths();
    }

    /**
     Expand the paths to those folders that have photos in model
     */
    private void expandTreePaths() {
        for( PhotoFolder folder : expandedFolders ) {
            Vector parents = new Vector();
            // parents.add( nodeMapper.mapFolderToNode( folder ) );
            while ( (folder = folder.getParentFolder() ) != null ) {
                parents.add( 0, nodeMapper.mapFolderToNode( folder ) );
            }
            if ( parents.size() > 0 ) {
                TreePath path = new TreePath( parents.toArray() );
                Iterator viewIter = views.iterator();
                while ( viewIter.hasNext() ) {
                    PhotoSelectionView view = (PhotoSelectionView) viewIter.next();
                    view.expandFolderTreePath( path );
                }
            }
        }
    }
    
    DefaultMutableTreeNode topNode = null;

    public DefaultMutableTreeNode getRootNode() {
	return topNode;
    }

    PhotoInfo[] photos = null;
    
    public void setPhotos( PhotoInfo[] photos, boolean preserveState ) {
	this.photos = photos;
	if ( !preserveState && addedToFolders != null  ) {
	    addedToFolders.clear();
	    removedFromFolders.clear();
            expandedFolders.clear();
	}
	initTree();
    }

    HashSet<PhotoFolder> addedToFolders;
    HashSet<PhotoFolder> removedFromFolders;
    HashSet<PhotoFolder> expandedFolders;
    
    /**
       Mark all photos in model so that they will be added to specified folder
       when committing the changes.
    */
    public void addAllToFolder( PhotoFolder f ) {
	addedToFolders.add( f );
	removedFromFolders.remove( f );
	FolderNode fn = (FolderNode)nodeMapper.mapFolderToNode( f );
	fn.addAllPhotos();
        parentCtrl.getChangeCommand().addToFolder( f );
    }
    
    /**
       Mark all photos in model so that they will be removed from specified folder
       when committing the changes.
    */
    public void removeAllFromFolder( PhotoFolder f ) {
	addedToFolders.remove( f );
	removedFromFolders.add( f );
	
        FolderNode fn = (FolderNode) nodeMapper.mapFolderToNode( f );
        fn.removeAllPhotos();  
        parentCtrl.getChangeCommand().removeFromFolder( f );
    }
    
    /**
       Hash table that maps PhotoFolders into nodes in the tree.
    */
    HashMap folderNodes = new HashMap();

    /** Initializes the JTree object's model based on folders 
	the objects in model belong to
    */
    protected void initTree() {
        /*
         TODO: Currently this is called several times while initializing the dialog.
         Optimize!!!
         */
        log.debug( "initTree()" );
        nodeMapper = new FolderToFolderNodeMapper( photos );
        treeModel = new PhotoFolderTreeModel( nodeMapper );
        if ( folderDAO == null ) {
            folderDAO = parentCtrl.getDAOFactory().getPhotoFolderDAO();
        }
        PhotoFolder root = folderDAO.findRootFolder();
        treeModel.setRoot( root );        
        if ( photos != null ) {
            // Add all photos in the model to folder tree
            for ( PhotoInfo p : photos ) {
                // if the model is empty it can contain a null
                // TODO: this is IMHO a hack - passing null up to this point is certainly
                // not elegant and there might be even more error opportunities
                if ( p!= null ) {
                    addPhotoToTree( p );
                }
            }
        }
        if ( views != null ) {
            updateAllViews();
        }
    }

    void addPhotoToTree( PhotoInfo photo ) {
	Collection folders = photo.getFolders();
	Iterator iter = folders.iterator();
	while ( iter.hasNext() ) {
	    PhotoFolder folder = (PhotoFolder) iter.next();
            expandedFolders.add( folder );
	    FolderNode fn = (FolderNode) nodeMapper.mapFolderToNode( folder );
	    fn.addPhoto( photo );
	}
    }
}
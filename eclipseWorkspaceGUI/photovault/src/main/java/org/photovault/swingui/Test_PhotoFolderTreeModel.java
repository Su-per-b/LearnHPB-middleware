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

import java.util.List;
import org.photovault.folder.*;
import javax.swing.event.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.test.PhotovaultTestCase;


public class Test_PhotoFolderTreeModel extends PhotovaultTestCase {
    static private final Log log = LogFactory.getLog( Test_PhotoFolderTreeModel.class.getName() );

    PhotoFolder rootFolder = null;
    PhotoFolderTreeModel model = null;
    
    /**
     Sets upt the model for the cases so that "subfolderTest" folder is set up 
     as the root folder.
     */
    public void setUp() {
	List folders = null;
//	try {
//	    OQLQuery query = odmg.newOQLQuery();
//	    query.create( "select folders from " + PhotoFolder.class.getName()  + " where name = \"subfolderTest\"" );
//	    folders = (List) query.execute();
//	    tx.commit();
//	} catch ( Exception e ) {
//	    tx.abort();
//	    fail( e.getMessage() );
//	}
	rootFolder = (PhotoFolder) folders.get(0);
	model = new PhotoFolderTreeModel();
	model.setRoot( rootFolder );
    }

    public void tearDown() {
    }

    public void testChildRetrieval() {
	assertEquals( "Number of children don't match", 4, model.getChildCount( rootFolder ) );
	assertEquals( "Subfolder name does not match", "Subfolder2", model.getChild( rootFolder, 1 ).toString() );
    }

    /**
       Test listener for treeModelChanges
    */

    class TestTreeModelListener implements TreeModelListener {
	// implementation of javax.swing.event.TreeModelListener interface

	public boolean nodesChanged = false;
	public boolean nodesInserted = false;
	public boolean nodesRemoved = false;
	public boolean structChanged = false;

	Object[] path = null;;
	PhotoFolder source = null;
	
	/**
	 *
	 * @param param1 <description>
	 */
	public void treeNodesChanged(TreeModelEvent e)
	{
	    nodesChanged = true;
	    source = (PhotoFolder) e.getSource();
	    path = e.getPath();
	}

	/**
	 *
	 * @param param1 <description>
	 */
	public void treeNodesInserted(TreeModelEvent e)
	{
	    nodesInserted = true;
	    source = (PhotoFolder) e.getSource();
	    path = e.getPath();
	}

	/**
	 *
	 * @param param1 <description>
	 */
	public void treeNodesRemoved(TreeModelEvent e)
	{
	    nodesRemoved = true;
	    source = (PhotoFolder)e.getSource();
	    path = e.getPath();
	}

	/**
	 *
	 * @param param1 <description>
	 */
	public void treeStructureChanged(TreeModelEvent e )
	{
	    structChanged = true;
	    source = (PhotoFolder)e.getSource();
	    path = e.getPath();
	}


    }

    /**
       Tests that listeners are notified when a folder is changed
    */
    public void testListener() {
	TestTreeModelListener l = new TestTreeModelListener();
	model.addTreeModelListener( l );

	PhotoFolder f = rootFolder.getSubfolder( 2 );
	f.setDescription( "treeModelListener test string" );
	assertTrue( "Listener was not notified of strucuture change", l.structChanged );
	model.removeTreeModelListener( l );
	l.structChanged = false;
	f.setDescription( "treeModelListener test string2" );
	assertFalse( "Listener was  notified of strucuture change after removal", l.structChanged );
    }

    /**
       Test modifications to the root node. This is a special case since TreePath cannot be empty...
    */
    public void testRootModifications() {
// 	model.setRoot( PhotoFolder.getRoot() );
      
      log.warn( "testRootModifications" );

 	model.setRoot( rootFolder );
	TestTreeModelListener l = new TestTreeModelListener();
	model.addTreeModelListener( l );
	rootFolder.setDescription( "Root description" );
	assertTrue( "Listener was not notified of strucuture change", l.structChanged );
	l.structChanged = false;

	PhotoFolder newFolder = PhotoFolder.create( "Test folder", rootFolder );
	assertTrue( "Listener was not notified of strucuture change", l.structChanged );
	l.structChanged = false;

	newFolder.delete();
	assertTrue( "Listener was not notified of strucuture change", l.structChanged );
	l.structChanged = false;
	
	log.warn( "finished testRootFolderModifications" );
	
    }
}

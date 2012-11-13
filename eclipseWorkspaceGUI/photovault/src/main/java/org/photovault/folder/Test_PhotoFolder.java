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

package org.photovault.folder;

import org.hibernate.Query;
import org.hibernate.Session;
import java.util.*;
import java.io.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.hibernate.context.ManagedSessionContext;
import org.photovault.command.CommandException;
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.imginfo.PhotoCollectionChangeEvent;
import org.photovault.imginfo.PhotoCollectionChangeListener;
import org.photovault.imginfo.PhotoEditor;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoDAO;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.Change;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.FieldConflictBase;
import org.photovault.replication.HibernateDtoResolverFactory;
import org.photovault.replication.SetFieldConflict;
import org.photovault.replication.VersionedObjectEditor;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Test_PhotoFolder extends PhotovaultTestCase {
    static Log log = LogFactory.getLog( Test_PhotoFolder.class.getName() );

    String testImgDir = "testfiles";
    PhotoFolderDAO folderDAO;
    PhotoInfoDAO photoDAO;
    HibernateDAOFactory daoFactory;
    Session session = null;
    Transaction tx = null;
    DTOResolverFactory rf = null;
    
    /**
       Sets up the test environment. retrieves from database the hierarchy with 
       "subfolderTest" as root and creates a TreeModel from it
    */
    @BeforeMethod
    @Override
    public void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        rf = new HibernateDtoResolverFactory( session );

        ManagedSessionContext.bind( (org.hibernate.classic.Session) session);
        daoFactory = (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        daoFactory.setSession( session );
        folderDAO = daoFactory.getPhotoFolderDAO();
        photoDAO = daoFactory.getPhotoInfoDAO();
        tx = session.beginTransaction();
    }

    @AfterMethod
    @Override
    public void tearDown() {
        tx.commit();
        session.close();
    }

    @Test
    public void testCreate() {
	
	PhotoFolder folder = folderDAO.create( "Top", null );
        folderDAO.flush();
        assertMatchesDb( folder, session );
        
        log.debug( "Changing folder name for " + folder.getUuid() );
	folder.setName( "testTop" );
	log.debug( "Folder name changed" );
	
	// Try to find the object from DB
	List folders = null;
	try {
	    folders = session.createQuery("from PhotoFolder where uuid = :id" ).
                    setString( "id", folder.getUuid().toString() ).list();
	} catch ( Exception e ) {
	    fail( e.getMessage() );
	}

        for ( Object o : folders ) {
            PhotoFolder folder2 = (PhotoFolder) o;
	    log.debug( "found top, id = " + folder2.getUuid() );
	    assertEquals( "Folder name does not match", folder2.getName(), "testTop" );
	    log.debug( "Modifying desc" );
	    folder2.setDescription( "Test description" );
	}
    }
    
    // Tests the retrieval of existing folder from database
    @Test
    public void testRetrieve() {
        List folders = null;
        PhotoFolder folder = folderDAO.findById( PhotoFolder.ROOT_UUID, false );
        assertMatchesDb( folder, session );
    }

    
    /** A new photo is created & added to the folder. Verify that it
     * is both persistent & visible in the folder
     */
    @Test
    public void testPhotoAddition() {
	String fname = "test1.jpg";
	File f = new File( testImgDir, fname );
        PhotoInfo photo = PhotoInfo.create();
        photo.getHistory().createChange().freeze();
        photoDAO.makePersistent( photo );
        FolderPhotoAssocDAO assocDAO = daoFactory.getFolderPhotoAssocDAO();
        
	PhotoFolder folder = null;
        // Create a folder for the photo
        PhotoFolder root = folderDAO.findRootFolder();
        folder = folderDAO.create( "PhotoAdditionTest", null );
        folder.reparentFolder( root );
        
        VersionedObjectEditor<PhotoFolder> e =folder.editor( rf );
        VersionedObjectEditor<PhotoInfo> pe = new VersionedObjectEditor<PhotoInfo>( photo, rf );
        FolderEditor fe = (FolderEditor) e.getProxy();        
        PhotoEditor pee = (PhotoEditor) pe.getProxy();
        
        FolderPhotoAssociation a = assocDAO.getAssociation( folder, photo );
        
        pee.addFolderAssociation( a );
        fe.addPhotoAssociation( a );
        e.apply();
        pe.apply();
        folderDAO.flush();
        assertMatchesDb( folder, session );
        
        assertEquals( "Photo not visible in folders' photo count", folder.getPhotoCount(), 1 );
        
        // Clean up the test folder
        PhotoFolder parent = folder.getParentFolder();
        parent.removeSubfolder( folder );
        photo.removeFolderAssociation( a );
        folder.removePhotoAssociation( a );
        assocDAO.makeTransient( a );
        folderDAO.makeTransient( folder );
    }


    public void testPhotoRetrieval() {
	// Find the corrent test case
        List folders = null;
        Query q = session.createQuery( "from PhotoFolder where name = :name" );
        q.setString( "name", "testPhotoRetrieval" );
        folders = q.list();
        PhotoFolder folder = (PhotoFolder) folders.get(0);
        assertMatchesDb( folder, session );
	
	// Check that the folder content is OK
	boolean found = false;
	for (PhotoInfo photo : folder.getPhotos() ) {
	    if ( photo.getDescription().equals( "testPhotoRetrieval1" ) ) {
		found = true;
	    }
	}
	assertTrue( "Photo testRetrieval1 not found", found );

	// TODO: Check that a new photo added to the album is added to DB also

	// TODO: check that removing a photo from the folder removes the link in DB also

    }
    
    /**
       Tests that persistence operations succeed.
    */
    @Test
    public void testPersistence() {
	// Test creation of a new folder
	PhotoFolder f = folderDAO.create( "persistenceTest", null );
        folderDAO.flush();
	assertMatchesDb( f );

	// Test modifications without existing transaction context
	f.setName( "test name 2" );
	f.setDescription( "Description" );
        folderDAO.flush();
	assertMatchesDb( f );

	// Tets modifications in transaction context
	f.setName( "test name 3" );
	f.setDescription( "desc 3" );
        folderDAO.flush();
	assertMatchesDb( f );

    }
    
    /**
     Test the folder creation and change commands.
     */
    @Test
    public void testCreateCommand() {
        PhotovaultCommandHandler cmdHandler = new PhotovaultCommandHandler( null );
        CreatePhotoFolderCommand createCmd = new CreatePhotoFolderCommand( null, "command create", "desc" );
        try {
            cmdHandler.executeCommand( createCmd );
        } catch (CommandException ex) {
            fail( ex.getMessage() );
        }
        PhotoFolder createdFolder = createCmd.getCreatedFolder();
        assertMatchesDb( createdFolder );
        
        ChangePhotoFolderCommand changeCmd = new ChangePhotoFolderCommand( createdFolder );
        changeCmd.setName( "Name 2" );
        changeCmd.setDescription( "Decription 2" );
        changeCmd.setParent( folderDAO.findRootFolder() );        
        try {
            cmdHandler.executeCommand( changeCmd );
        } catch (CommandException ex) {
            fail( ex.getMessage() );
        }
        PhotoFolder changedFolder = changeCmd.getChangedFolder();
        PhotoFolder f = (PhotoFolder) session.merge( changedFolder );
        assertEquals( "Name 2", f.getName() );
	assertMatchesDb( f );
        
        // Check the change history
        Change<PhotoFolder> head = f.getHistory().getVersion();
        assertEquals( 1, f.getHistory().getHeads().size() );
        assertTrue( f.getHistory().getHeads().contains( head ) );
        head.getChangedFields().containsKey( "name" );
        head.getChangedFields().containsKey( "description" );
        
        Change<PhotoFolder> initChange = head.getParentChanges().iterator().next();
        assertEquals( 1, initChange.getParentChanges().size() );
        Change<PhotoFolder> createChange = initChange.getParentChanges().iterator().next();
        assertEquals( 0, createChange.getParentChanges().size() );
        assertEquals( 0, createChange.getChangedFields().size() );
    }
    
    /**
       Utility
    */
    void assertMatchesDb( PhotoFolder folder ) {
        assertMatchesDb( folder, session );
    }
	

	
    
    /**
       Test that subfolders are created correctly
    */
    @Test
    public void testSubfolders() {
        Query q = session.createQuery( "from PhotoFolder where name = :name" );
        q.setString("name", "subfolderTest" );
	PhotoFolder topFolder = (PhotoFolder) q.uniqueResult();
	assertEquals( "Top folder name invalid", "subfolderTest",topFolder.getName() );
	assertEquals( "topFolder should have 4 subfolders", 4, topFolder.getSubfolderCount() );

	String[] subfolderNames = {"Subfolder1", "Subfolder2", "Subfolder3", "Subfolder4"};

	// Check subfolder addition
	PhotoFolder newFolder = folderDAO.create( "Subfolder5", topFolder );
        folderDAO.flush();
	assertEquals( "New subfolder added", 5, topFolder.getSubfolderCount() );
        assertMatchesDb( topFolder, session );
        assertMatchesDb( newFolder, session );

	newFolder.delete();
        folderDAO.makeTransient( newFolder );
        folderDAO.flush();
        assertEquals( "Subfolder deleted", 4, topFolder.getSubfolderCount() );
    }

    class TestListener implements PhotoFolderChangeListener {
	// implementation of imginfo.PhotoCollectionChangeListener interface

	public boolean modified = false;
	public boolean subfolderModified= false;
	public boolean structureModified = false;
	public PhotoFolder changedFolder = null;
	public PhotoFolder structChangeFolder = null;
	/**
	 *
	 * @param param1 <description>
	 */
	public void photoCollectionChanged(PhotoCollectionChangeEvent param1)
	{
	    modified = true;
	}

	public void subfolderChanged( PhotoFolderEvent e ) {
	    subfolderModified = true;
	    changedFolder = e.getSubfolder();
	}

	public void structureChanged( PhotoFolderEvent e ) {
	    structureModified = true;
	    structChangeFolder = e.getSubfolder();
	}
	
    }
    
    class TestCollectionListener implements PhotoCollectionChangeListener {
	// implementation of imginfo.PhotoCollectionChangeListener interface

	public boolean modified = false;
	/**
	 *
	 * @param param1 <description>
	 */
	public void photoCollectionChanged(PhotoCollectionChangeEvent param1)
	{
	    modified = true;
	}

    }

    @Test
    public void testListener() {
	PhotoFolder folder = folderDAO.create( "testListener", null );
	TestListener l1 = new TestListener();
	TestListener l2 = new TestListener();
	TestCollectionListener l3 = new TestCollectionListener();

	folder.addPhotoCollectionChangeListener( l1 );
	folder.addPhotoCollectionChangeListener( l2 );
	folder.addPhotoCollectionChangeListener( l3 );

	folder.setName( "testLiistener" );
	assertTrue( "l1 not called", l1.modified );
	assertTrue( "l2 not called", l2.modified );
	assertTrue( "l3 not called", l3.modified );
	folder.setName( "testListener" );
	l1.modified = false;
	l2.modified = false;
	
	folder.removePhotoCollectionChangeListener( l2 );
	folder.setDescription( "Folder usded to test listener support" );
	assertTrue( "l1 not called", l1.modified );
	assertFalse( "l2 should not have been called", l2.modified );

	// Test creation of a new subfolder
	PhotoFolder subfolder = folderDAO.create( "New subfolder", folder );
	assertTrue( "Not notified of subfolder structure change", l1.structureModified );
	assertEquals( "subfolder info not correct", folder, l1.structChangeFolder );
	l1.structureModified = false;
	l1.changedFolder = null;

	subfolder.setDescription( "Changed subfolder" );
	assertTrue( "l1 not called for subfolder modification", l1.subfolderModified );
	assertEquals( "subfolder info not correct", subfolder, l1.changedFolder );
	l1.subfolderModified = false;
	l1.changedFolder = null;

	// Test that photo addition is notified
	String fname = "test1.jpg";
	File f = new File( testImgDir, fname );
        PhotoInfo photo = PhotoInfo.create();
        photo.getHistory().createChange().freeze();
        photoDAO.makePersistent( photo );

	l1.modified = false;
        FolderPhotoAssociation a  = new FolderPhotoAssociation( folder, photo );
	folder.addPhotoAssociation( a );
	assertTrue( "l1 not called when adding photo", l1.modified );

	l1.modified = false;
	folder.removePhotoAssociation( a );
	assertTrue( "l1 not called when removing photo", l1.modified );
	photo.delete();
	
	subfolder.delete();
	assertTrue( "Not notified of subfolder structure change", l1.structureModified );
	assertEquals( "subfolder info not correct", folder, l1.structChangeFolder );

	// TODO: test other fields
    }

    /**
       test that when a photo is deleted from database the folder is also modified
    */
    
    /*
     This is not relevant with the new schema, as photos cannot really be 
     deleted!!!
     
    @Test
    public void testPhotoDelete() {
	PhotoFolder folder = PhotoFolder.create( "testListener", null );
        folderDAO.makePersistent( folder );
	String fname = "test1.jpg";
	File f = new File( testImgDir, fname );
        PhotoInfo photo = PhotoInfo.create();
        photo.getHistory().createChange().freeze();
        photoDAO.makePersistent( photo );


	folder.addPhoto( photo );
        folderDAO.flush();
	photo.delete();
        folderDAO.flush();
        // TOOD: CHEC DATABASE STATE!!!
	assertEquals( "After deleting the photo there should be no photos in the folder",
		      folder.getPhotoCount(), 0 );
	
    }

     */
    
    /**
       Tests that getRoot() method returns the root folder and that it returns the same
       instance all the time.
    */
    @Test
    public void testGetRoot() {
	PhotoFolder root1 = folderDAO.findRootFolder();
	PhotoFolder root2 = folderDAO.findRootFolder();
	assertTrue( "several instances of root created", root1==root2 );
        
        assertEquals( root1.getName(), "Top" );
        assertNull( root1.getParentFolder() );
    }
    
    /**
     Test that {@link FolderPhotoAssociation} objects are persisted correctly
    */
    @Test
    public void testAssocPersistence() {
        PhotoFolder top = folderDAO.findRootFolder();
        PhotoInfo p1 = PhotoInfo.create();
        PhotoInfo p2 = PhotoInfo.create();
        photoDAO.makePersistent( p1 );
        
        FolderPhotoAssociation a1 = new FolderPhotoAssociation( top, p1 );
        FolderPhotoAssociation a2 = new FolderPhotoAssociation( top, p2 );
        a2.setPhoto( null );
        VersionedObjectEditor<PhotoFolder> fe = top.editor( rf );
        top.addPhotoAssociation( a1 );
        top.addPhotoAssociation( a2 );
        
        FolderPhotoAssocDAO assocDAO = daoFactory.getFolderPhotoAssocDAO();
        assocDAO.makePersistent( a1 );
        assocDAO.makePersistent( a2 );
        
        session.flush();
        tx.commit();
        tx = session.beginTransaction();
        session.clear();
        
        top = folderDAO.findRootFolder();
        Set<FolderPhotoAssociation> assocs = top.getPhotoAssociations();
        assertEquals( 2, assocs.size() );
        boolean foundP1 = false;
        boolean foundP2 = false;
        for ( FolderPhotoAssociation a : assocs ) {
            if ( a.getPhoto() == null ) {
                foundP2 = true;
                // This throws exception if uuid validation fails
                a.setPhoto( p2 );
            } else {
                assertEquals( p1.getUuid(), a.getPhoto().getUuid() );
                foundP1 = true;
            }
        }
        assertTrue( foundP1 );
        assertTrue( foundP2 );
        session.clear();
    }
    
    /**
     Test that {@link FolderPhotoAssociation} checks its state correctly and 
     throws and exception if the folder and photo set do not match with its 
     identity.
     */
    @Test
    public void testFolderAssocValidation() {
        PhotoFolder top = folderDAO.findRootFolder();
        PhotoFolder otherFolder = folderDAO.create( "test", top );
        PhotoInfo p1 = PhotoInfo.create();
        PhotoInfo p2 = PhotoInfo.create();
        FolderPhotoAssociation a1 = new FolderPhotoAssociation( top, p1 );
        a1.setFolder( null );
        a1.setPhoto( null );
        a1.setFolder( otherFolder );
        boolean ok = false;
        try {
            a1.setPhoto( p1 );
        } catch ( IllegalStateException e ) {
            ok = true;
        }
        assertTrue( ok );
        a1.setFolder( top );
        ok = false;
        try {
            a1.setFolder( otherFolder );
        } catch ( IllegalStateException e ) {
            ok = true;
        }
        assertTrue( ok );
    }  
    
    @Test
    public void testFolderHistory() {
        PhotoFolder top = folderDAO.findRootFolder();
        PhotoFolder otherFolder = folderDAO.create( "test", top );
        PhotoFolder thirdFolder = folderDAO.create( "thirdFolder", top );
        VersionedObjectEditor<PhotoFolder> fe = otherFolder.editor( rf );
        FolderEditor fep = (FolderEditor) fe.getProxy();
        
        Change<PhotoFolder> fv1 = otherFolder.getHistory().getVersion();
        
        PhotoInfoDAO photoDao = daoFactory.getPhotoInfoDAO();
        PhotoInfo p1 = PhotoInfo.create();
        PhotoInfo p2 = PhotoInfo.create();
        photoDao.makePersistent( p1 );
        photoDao.makePersistent( p2 );
        VersionedObjectEditor<PhotoInfo> p2e = new VersionedObjectEditor<PhotoInfo>(  p2, rf );
        VersionedObjectEditor<PhotoInfo> p1e = new VersionedObjectEditor<PhotoInfo>(  p1, rf );

        /*
         The version tree for otherFolder
         
         fv1
          | \
         fv2 \          Add p1
          |   \
         fv3  |         parent = thirdFolder
          |   |         
          |  fv4        name = "Moikka", remove p1
          |  /
          fv5           merge
         */
        FolderPhotoAssocDAO assocDao = daoFactory.getFolderPhotoAssocDAO();
        FolderPhotoAssociation a1 = new FolderPhotoAssociation( otherFolder, p1 );
        assocDao.makePersistent( a1 );
        p1e.addToSet( "folderAssociations", a1 );
        fep.addPhotoAssociation( a1 );
        p1e.apply();
        Change<PhotoFolder> fv2 = fe.apply();
        
        // Change parent
        fe = otherFolder.editor( rf );
        fep = (FolderEditor) fe.getProxy();
        fep.reparentFolder( thirdFolder );
        Change<PhotoFolder> fv3 = fe.apply();
        assertEquals( thirdFolder, otherFolder.getParentFolder() );
        assertTrue( thirdFolder.getSubfolders().contains( otherFolder ) );
        assertFalse( top.getSubfolders().contains( otherFolder ) );
        
        session.flush();
        assertTrue( otherFolder.getHistory().getVersion() == fv3 );
        
        assertTrue( p1.getFolderAssociations().contains( a1 ) );
        assertTrue( otherFolder.getPhotoAssociations().contains( a1 ) );

        // Ensure that changes to earlier version removes the association
        fe = otherFolder.editor( rf );
        fe.changeToVersion( fv1 );
        session.flush();
        assertFalse( otherFolder.getPhotoAssociations().contains( a1 ) );
        assertNull( a1.getFolder() );
        assertEquals( top, otherFolder.getParentFolder() );
        
        fep = (FolderEditor) fe.getProxy();
        fep.removePhotoAssociation(  a1 );
        fep.setName( "Moikka" );
        Change<PhotoFolder> fv4 = fe.apply();
        session.flush();
        
        fe = otherFolder.editor( rf );
        Change<PhotoFolder> fv5 = fv3.merge( fv4 );
        Collection<FieldConflictBase> conflicts = fv5.getFieldConficts();
        assertEquals( 1, conflicts.size() );
        for ( FieldConflictBase c : conflicts ) {
            SetFieldConflict sc = (SetFieldConflict) c;
            sc.resolve( 0 );
        }
        fv5.freeze();
        fe.changeToVersion( fv5 );
        session.flush();
        assertTrue( otherFolder.getPhotoAssociations().contains( a1 ) );
        assertTrue( a1.getFolder() == otherFolder );
        assertEquals( "Moikka", otherFolder.getName() );
    }

}


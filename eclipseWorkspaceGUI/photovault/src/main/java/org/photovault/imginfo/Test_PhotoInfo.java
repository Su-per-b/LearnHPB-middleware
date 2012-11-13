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

package org.photovault.imginfo;

import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.awt.image.*;
import javax.imageio.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.photovault.command.CommandException;
import org.photovault.command.CommandHandler;
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.common.PhotovaultException;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.HibernateDtoResolverFactory;
import org.photovault.replication.VersionedObjectEditor;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class Test_PhotoInfo extends PhotovaultTestCase {
    static Log log = LogFactory.getLog( Test_PhotoInfo.class.getName() );

    File testImgDir = new File( System.getProperty( "basedir" ), "testfiles" );
    String nonExistingDir = "/tmp/_dirThatDoNotExist";
    Session session = null;
    Transaction tx = null;
    
    DAOFactory daoFactory;
    PhotoInfoDAO photoDAO;
    
    /**
     * Default constructor to set up OJB environment
     */
    public Test_PhotoInfo() {
        super();
    }

    /**
     Sets ut the test environment
     */
    @BeforeMethod
    public void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        HibernateDAOFactory hdf = (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        hdf.setSession( session );
        daoFactory = hdf;
        photoDAO = daoFactory.getPhotoInfoDAO();
        tx = session.beginTransaction();
    }
    
    /**
     Tears down the testing environment
     */
    @AfterMethod
    public void tearDown() {
        tx.commit();
        session.close();
    }   

    
    //    File testRefImageDir = new File( "c:\\java\\photovault\\tests\\images\\photovault\\imginfo" );
    File testRefImageDir = new File( "tests/images/photovault/imginfo" );
    
    /**
       Test case that verifies that an existing photo infor record 
       can be loaded successfully
    */
    @Test
    public void testRetrievalSuccess() throws InstantiationException, IllegalAccessException {
	UUID photoId = UUID.fromString( "f5d73748-0fb4-40ab-bd05-d3740fb30783");
        HibernateDtoResolverFactory rf = new HibernateDtoResolverFactory( session );
        VersionedObjectEditor<PhotoInfo> pe = new VersionedObjectEditor(  PhotoInfo.class, photoId, rf );        
        photoDAO.makePersistent( pe.getTarget() );
        session.flush();
        session.clear();
        
        PhotoInfo photo = null;
        photo = photoDAO.findByUUID( photoId );
        assertNotNull( photo );
        PhotoInfo photo2 = photoDAO.findByUUID( UUID.randomUUID() );
        assertNull( photo2 );
    }


    /** 
	Test updating object to DB
    */
    @Test
    public void testUpdate() {
	UUID photoId = UUID.fromString( "f5d73748-0fb4-40ab-bd05-d3740fb30783");
        PhotoInfo photo = null;
        photo = photoDAO.findByUUID( photoId );
        assertTrue(photo != null );
        
	// Update the photo
	String shootingPlace = photo.getShootingPlace();
	String newShootingPlace = "Testipaikka";
	photo.setShootingPlace( newShootingPlace );
        photoDAO.flush();
        assertMatchesDb( photo );
    }

    /** 
	Test updating object to DB when shooting date has not been specified
    */
    @Test
    public void testNullShootDateUpdate() {
	UUID photoId = UUID.fromString( "f5d73748-0fb4-40ab-bd05-d3740fb30783");
	PhotoInfo photo = null;	
        photo = photoDAO.findByUUID( photoId );
        assertTrue( photo != null );

	java.util.Date origTime = photo.getShootTime();
	// Update the photo
	photo.setShootTime( null );
        tx.commit();
        session.clear();
        
	// retrieve the updated photo from DB and chech that the
	// modification has been done
        tx = session.beginTransaction();
        photo = photoDAO.findByUUID( photoId );
        assertNull( "Shooting time was supposedly set to null", photo.getShootTime() );

	// restore the shooting place
	photo.setShootTime( origTime );
    }

    /**
       Test normal creation of a persistent PhotoInfo object
    */
    @Test
    public void testPhotoCreation() {
        
	PhotoInfo photo = PhotoInfo.create();
        try {
            session.save( photo );
        } catch ( Throwable t ) {
            fail( t.getMessage() );
        }
	assertNotNull( photo );
	photo.setPhotographer( "TESTIKUVAAJA" );
	photo.setShootingPlace( "TESTPLACE" );
	photo.setShootTime( new java.util.Date() );
	photo.setFStop( 5.6 );
	photo.setShutterSpeed( 0.04 );
	photo.setFocalLength( 50 );
	photo.setCamera( "Canon FTb" );
	photo.setFilm( "Tri-X" );
	photo.setFilmSpeed( 400 );
	photo.setLens( "Canon FD 50mm/F1.4" );
        photo.setCropBounds( new Rectangle2D.Double( 0.1, 0.2, 0.5, 0.7 ) );
	photo.setDescription( "This is a long test description that tries to verify that the description mechanism really works" );
	//	photo.updateDB();
        tx.commit();
        session.clear();
        
        tx = session.beginTransaction();

        PhotoInfo photo2 = null;
        photo2 = photoDAO.findByUUID( photo.getUuid() );

        assertEquals( photo.getPhotographer(), photo2.getPhotographer() );
        assertEquals( photo.getShootingPlace(), photo2.getShootingPlace() );
        // assertEquals( photo.getShootTime(), photo2.getShootTime() );
        assertEquals( photo.getDescription(), photo2.getDescription() );
        assertEquals( photo.getCamera(), photo2.getCamera() );
        assertEquals( photo.getLens(), photo2.getLens() );
        assertEquals( photo.getFilm(), photo2.getFilm() );
        assertTrue( photo.getShutterSpeed() == photo2.getShutterSpeed() );
        assertTrue( photo.getFilmSpeed() == photo2.getFilmSpeed() );
        assertTrue( photo.getFocalLength() == photo2.getFocalLength() );
        assertTrue( photo.getFStop() == photo2.getFStop() );
        assertTrue( photo.getUuid().equals( photo2.getUuid() ) );
        assertTrue( photo.getCropBounds().equals( photo2.getCropBounds() ) );

        //	    assertTrue( photo.equals( photo2 ));

    }

    /**
       Test normal creation of a persistent PhotoInfo object
    */
    @Test
    public void testChangeCommand() {
        PhotovaultCommandHandler cmdHandler = new PhotovaultCommandHandler( null );
        
	ChangePhotoInfoCommand photoCreateCmd = new ChangePhotoInfoCommand( );
        try {
            cmdHandler.executeCommand( photoCreateCmd );
        } catch (CommandException ex) {
            fail( ex.getMessage() );
        }
        Set<PhotoInfo> photos = photoCreateCmd.getChangedPhotos();
        assertEquals( 1, photos.size() );
        PhotoInfo photo = photos.toArray( new PhotoInfo[1] )[0];
        
        photo = (PhotoInfo) session.merge( photo );
        
	assertNotNull( photo );
        
	ChangePhotoInfoCommand photoChangeCmd = new ChangePhotoInfoCommand( photo.getUuid() );
        
        // photoChangeCmd.setUUID( UUID.randomUUID() );
	photoChangeCmd.setPhotographer( "TESTIKUVAAJA" );
	photoChangeCmd.setShootingPlace( "TESTPLACE" );
	// TODO: debug this!!!
        // photoChangeCmd.setShootTime( new java.util.Date() );
	photoChangeCmd.setFStop( 5.6 );
	photoChangeCmd.setShutterSpeed( 0.04 );
	photoChangeCmd.setFocalLength( 50 );
	photoChangeCmd.setCamera( "Canon FTb" );
	photoChangeCmd.setFilm( "Tri-X" );
	photoChangeCmd.setFilmSpeed( 400 );
	photoChangeCmd.setLens( "Canon FD 50mm/F1.4" );
        photoChangeCmd.setCropBounds( new Rectangle2D.Double( 0.1, 0.2, 0.5, 0.7 ) );
	photoChangeCmd.setDescription( "This is a long test description that tries to verify that the description mechanism really works" );

        try {
            cmdHandler.executeCommand( photoChangeCmd );
        } catch (CommandException ex) {
            fail( ex.getMessage() );
        }
        photos = photoChangeCmd.getChangedPhotos();
        assertEquals( 1, photos.size() );
        photo = photos.toArray( new PhotoInfo[1] )[0];
        photo = (PhotoInfo) session.merge( photo );
        
        
        session.clear();
        
        
        PhotoInfo photo2 = null;
        photo2 = photoDAO.findByUUID( photo.getUuid() );

        assertEquals( photo.getPhotographer(), photo2.getPhotographer() );
        assertEquals( photo.getShootingPlace(), photo2.getShootingPlace() );
        // assertEquals( photo.getShootTime(), photo2.getShootTime() );
        assertEquals( photo.getDescription(), photo2.getDescription() );
        assertEquals( photo.getCamera(), photo2.getCamera() );
        assertEquals( photo.getLens(), photo2.getLens() );
        assertEquals( photo.getFilm(), photo2.getFilm() );
        assertTrue( photo.getShutterSpeed() == photo2.getShutterSpeed() );
        assertTrue( photo.getFilmSpeed() == photo2.getFilmSpeed() );
        assertTrue( photo.getFocalLength() == photo2.getFocalLength() );
        assertTrue( photo.getFStop() == photo2.getFStop() );
//        assertTrue( photo.getUid() == photo2.getUid() );
        assertTrue( photo.getUuid().equals( photo2.getUuid() ) );
        assertTrue( photo.getCropBounds().equals( photo2.getCropBounds() ) );

        //	    assertTrue( photo.equals( photo2 ));
    }
    
    @Test
    public void testPhotoDeletion() {
	PhotoInfo photo = new PhotoInfo();
        session.save( photo );
        session.flush();
        
	// Check that the photo can be retrieved from DB

	Connection conn = session.connection();
	String sql = "SELECT * FROM photos WHERE photo_uuid = '" + photo.getUuid() + "'";
	Statement stmt = null;
	ResultSet rs = null;
	try {
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery( sql );
	    if ( !rs.next() ) {
		fail( "Matching DB record not found" );
	    }
	} catch ( SQLException e ) {
	    fail( "DB error:; " + e.getMessage() );
	} finally {
	    if ( rs != null ) {
		try {
		    rs.close();
		} catch ( Exception e ) {}
	    }
	    if ( stmt != null ) {
		try {
		    stmt.close();
		} catch ( Exception e ) {}
	    }
	}

        session.delete( photo );
        session.flush();
        // Check that the photo is deleted from the database
	try {
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery( sql );
	    if ( rs.next() ) {
		fail( "Found matching DB record after delete" );
	    }
	} catch ( SQLException e ) {
	    fail( "DB error:; " + e.getMessage() );
	} finally {
	    if ( rs != null ) {
		try {
		    rs.close();
		} catch ( Exception e ) {}
	    }
	    if ( stmt != null ) {
		try {
		    stmt.close();
		} catch ( Exception e ) {}
	    }
	}
    }
    

	
    @Test
    public void testCreationFromImage() {
	String fname = "test1.jpg";
	File f = new File( testImgDir, fname );
        PhotoInfo photo = createPhoto( f );
	assertNotNull( photo );
        OriginalImageDescriptor orig = photo.getOriginal();
        assertNotNull( orig );
        assertNull( orig.getFile().findAvailableCopy() );
        ImageFile ifile  = orig.getFile();
        orig.photos.remove( photo );
        photo.setOriginal( null );
        session.delete( photo );
    }

    /**
       Test that an exception is generated when trying to add
       nonexisting file to DB
    */
    @Test
    public void testfailedCreation() {
	String fname = "test1.jpg";	
	File f = new File( nonExistingDir, fname );
        try {
            PhotoInfo photo = createPhoto( f );
            // Execution should never proceed this far since addToDB
            // should produce exception
            fail( "Image file should have been nonexistent" );
        } catch ( Throwable e ) {
	    // This is what we except
	}
    }

    /**
       Test that creating a new thumbnail using createThumbnail works
     */
    @Test
    public void testThumbnailCreate() {
	String fname = "test1.jpg";
	File f = new File( testImgDir, fname );
	PhotoInfo photo = createPhoto( f );
	assertNotNull( photo );
	int copyCount = photo.getOriginal().getCopies().size();
	photo.createThumbnail();
	assertEquals( "InstanceNum should be 1 greater after adding thumbnail",
		     copyCount+1, photo.getOriginal().getCopies().size() );
	// Try to find the new thumbnail
	boolean foundThumbnail = false;
	CopyImageDescriptor thumbnail =  (CopyImageDescriptor) photo.getPreferredImage(
                EnumSet.allOf(ImageOperations.class ), 
                EnumSet.allOf(ImageOperations.class ), 
                0, 0, 100, 100 );
	
	assertNotNull( "Could not find the created thumbnail", thumbnail );
	assertEquals( "Thumbnail width should be 100", 100, thumbnail.getWidth() );
	File thumbnailFile = thumbnail.getFile().findAvailableCopy();
	assertTrue( "Image file does not exist", thumbnailFile.exists() );

	// Test the getThumbnail method
	Thumbnail thumb = photo.getThumbnail();
	assertNotNull( thumb );
	assertFalse( "Thumbnail exists, should not return default thumbnail",
		     thumb == Thumbnail.getDefaultThumbnail() );
	
        session.flush();
        assertMatchesDb( photo );
        
	photo.delete();
	assertFalse( "Image file does exist after delete", thumbnailFile.exists() );
    }


    /**
       Tests thumbnail creation when there are no photo instances.
    */
    @Test
    public void testThumbnailCreateNoInstances() throws Exception {
	PhotoInfo photo = PhotoInfo.create();
	try {
	    photo.createThumbnail();
/*      
 TODO: Rewrite so that there is an original without any available locations
	    assertEquals( "Should not create a thumbnail instance when there are no original",
			  0, photo.getNumInstances() );
*/
        } catch (Exception e ) {
	    throw e;
	} finally {
	    photo.delete();
	}
    }

    /**
       Tests thumbnail creation when the database is corrupted & files
       that photo instances refer to do not exist.
    */
    @Test
    public void testThumbnailCreateCorruptInstances() throws Exception {	
	String fname = "test1.jpg";
	File f = new File( testImgDir, fname );
	PhotoInfo photo = createPhoto( f );
        
	// Corrupt the database by deleting the actual image files
	// that instances refer to
	for ( FileLocation ifile : photo.getOriginal().getFile().getLocations() ) {
	    File file = ifile.getFile();
	    file.delete();
	}

	// Create the thumbnail
	photo.createThumbnail();

	try {
	    Thumbnail thumb = photo.getThumbnail();
	    assertNotNull( thumb );
	    assertTrue( "Database is corrupt, should return error thumbnail",
			thumb == Thumbnail.getErrorThumbnail() );
	    assertEquals( "Database is corrupt, getThumbnail should not create a new instance",
			  1, photo.getOriginal().getCopies().size());
	    
	} finally {
	    // Clean up in any case
	    photo.delete();
	}
    }
    
    /**
       Test that creating a new thumbnail using getThumbnail works
     */
    @Test
    public void testGetThumbnail() {
	String fname = "test1.jpg";
	File f = new File( testImgDir, fname );
	PhotoInfo photo = createPhoto( f );

	assertNotNull( photo );
	Thumbnail thumb = photo.getThumbnail();
	assertNotNull( thumb );
	assertFalse( "Thumbnail exists, should not return default thumbnail",
		     thumb == Thumbnail.getDefaultThumbnail() );

        // Try to find the new thumbnail
	boolean foundThumbnail = false;
        OriginalImageDescriptor orig = photo.getOriginal();
        
	CopyImageDescriptor thumbnail = null;
	for ( CopyImageDescriptor copy : orig.getCopies() ) {
            if ( copy.getWidth() <= 100 && copy.getHeight() <= 100 && 
                    copy.getFile().findAvailableCopy() != null ) {
		foundThumbnail = true;
		thumbnail = copy; 
		break;
	    }
	}
	assertTrue( "Could not find the created thumbnail", foundThumbnail );
	assertEquals( "Thumbnail width should be 100", 100, thumbnail.getWidth() );
	File thumbnailFile = thumbnail.getFile().findAvailableCopy();
	assertTrue( "Image file does not exist", thumbnailFile.exists() );
	photo.delete();
	assertFalse( "Image file does exist after delete", thumbnailFile.exists() );
    }

    /**
       Test getThumbnail in situation where there is no image instances for the PhotoInfo
    */
    @Test
    public void testThumbWithNoInstances() {
	PhotoInfo photo = PhotoInfo.create();
	Thumbnail thumb = photo.getThumbnail();
        // TODO: Should getThumbnail really return defaultThumbnail in this situation?
	assertTrue( "getThumbnail should return error thumbnail",
		    thumb == Thumbnail.getErrorThumbnail() ) ;
        
//	assertEquals( "No new instances should have been created", 0, photo.getNumInstances() );

	// Create a new instance and check that a valid thumbnail is returned after this
	File testFile = new File( testImgDir, "test1.jpg" );
        if ( !testFile.exists() ) {
            fail( "could not find test file " + testFile );
        }
	File instanceFile = VolumeBase.getDefaultVolume().getFilingFname( testFile );
	try {
	    FileUtils.copyFile( testFile, instanceFile );
	} catch ( IOException e ) {
	    fail( e.getMessage() );
	}
/*        
	photo.addInstance( VolumeBase.getDefaultVolume(), instanceFile, ImageInstance.INSTANCE_TYPE_ORIGINAL );
*/
        Thumbnail thumb2 = photo.getThumbnail();
   
        assertFalse( "After instance addition, getThumbnail should not return default thumbnail",
			thumb == thumb2 );
	assertEquals( "There should be 1 copy", 1, 
                photo.getOriginal().getCopies().size() );

	photo.delete();
		      
    }

    /**
       Test that thumbnail is rotated if prefRotation is nonzero
    */

    @Test
    public void testThumbnailRotation() {
	String fname = "test1.jpg";
	File f = new File( testImgDir, fname );
	PhotoInfo photo = createPhoto( f );
	photo.setPrefRotation( -45 );

	Thumbnail thumb = photo.getThumbnail();

	// Compare thumbnail to the one saved
	File testFile = new File ( testRefImageDir, "thumbnailRotation1.png" );
	assertTrue( "Thumbnail with 45 deg rotation does not match",
		    org.photovault.test.ImgTestUtils.compareImgToFile( thumb.getImage(), testFile ) );

	photo.setPrefRotation( -90 );
	thumb = photo.getThumbnail();
	testFile = new File ( testRefImageDir, "thumbnailRotation2.png" );
	assertTrue( "Thumbnail with 90 deg rotation does not match",
		    org.photovault.test.ImgTestUtils.compareImgToFile( thumb.getImage(), testFile ) );

	photo.delete();
    }

    /**
       PhotoInfoListener used for test cases
    */
    class TestListener implements PhotoInfoChangeListener {
	public boolean isNotified = false;
	
	public void photoInfoChanged( PhotoInfoChangeEvent e ) {
	    isNotified = true;
	}
    }
    
    /**
       Tests that the listener is working correctly
    */
    @Test
    public void testListener() {

	PhotoInfo photo = PhotoInfo.create();
	TestListener l1 = new TestListener();
	TestListener l2 = new TestListener();
	photo.addChangeListener( l1 );
	photo.addChangeListener( l2 );

	// Test that the listeners are notified
	photo.setPhotographer( "TEST" );
	assertTrue( "l1 was not notified", l1.isNotified );
	assertTrue( "l2 was not notified", l2.isNotified );

	// Test that the listeners are removed correctly
	photo.removeChangeListener( l2 );
	l1.isNotified = false;
	l2.isNotified = false;
	photo.setPhotographer( "TEST2" );
	assertTrue( "l1 was not notified", l1.isNotified );
	assertFalse( "l2 was not supposed to be notified", l2.isNotified );

	// Test all object fields, one by one
	l1.isNotified = false;
	photo.setShootingPlace( "TEST" );
	assertTrue( "no notification when changing shootingPlace", l1.isNotified );
	l1.isNotified = false;
	photo.setFStop( 12 );
	assertTrue( "no notification when changing f-stop", l1.isNotified );
	l1.isNotified = false;
	photo.setFocalLength( 10 );
	assertTrue( "no notification when changing focalLength", l1.isNotified );
	l1.isNotified = false;
	photo.setShootTime( new java.util.Date() );
	assertTrue( "no notification when changing shooting time", l1.isNotified );
	l1.isNotified = false;
	photo.setShutterSpeed( 1.0 );
	assertTrue( "no notification when changing shutter speed", l1.isNotified );
	l1.isNotified = false;
	photo.setCamera( "Leica" );
	assertTrue( "no notification when changing camera", l1.isNotified );
	l1.isNotified = false;
	photo.setLens( "TESTLENS" );
	assertTrue( "no notification when changing lens", l1.isNotified );
	l1.isNotified = false;
	photo.setFilm( "Pan-X"  );
	assertTrue( "no notification when changing film", l1.isNotified );
	l1.isNotified = false;
	photo.setFilmSpeed( 160  );
	assertTrue( "no notification when changing film speed", l1.isNotified );
	l1.isNotified = false;
	photo.setPrefRotation( 107 );
	assertTrue( "no notification when changing preferred rotation", l1.isNotified );
	l1.isNotified = false;
	photo.setDescription( "Test with lots of characters" );
	assertTrue( "no notification when changing description", l1.isNotified );
	photo.delete();
    }

    /**
       Test normal case of exporting image from database
    */
    @Test
    public void testExport() {
	String fname = "test1.jpg";
	File f = new File( testImgDir, fname );
	PhotoInfo photo = createPhoto( f );
	photo.setPrefRotation( -90 );

	File exportFile = new File( "/tmp/exportedImage.png" );
// 	try {
// 	    exportFile = File.createTempFile( "testExport", ".jpg" );
// 	} catch ( IOException e ) {
// 	    fail( "could not create export file: " + e.getMessage() );
// 	}
        try {
            photo.exportPhoto( exportFile, 400, 400 );
        } catch (PhotovaultException e ) {
            fail( e.getMessage() );
        }
	// Read the exported image
	BufferedImage exportedImage = null;
	try {
	    exportedImage = ImageIO.read( exportFile );
	} catch ( IOException e ) {
	    fail( "Could not read the exported image " + exportFile );
	}

	File exportRef = new File( testRefImageDir, "exportedImage.png" );
	    
	// Verify that the exported image matches the reference
	assertTrue( "Exported image " + exportFile + " does not match reference " + exportRef,
		    org.photovault.test.ImgTestUtils.compareImgToFile( exportedImage, exportRef ) );

	photo.delete();
    }

    /**
       Test exporting an image to a file name that cannot be created
    */
/*    public void testExportWriteNotAllowed() {
	fail ("Test case not implemented" );
    }
 */   

    @Test
    public void testRawSettings() {
        PhotoInfo p = PhotoInfo.create();
        p = photoDAO.makePersistent( p );
        double chanMul[] = { 
            1., .7, .5, .7
        };
        
        double daylightMul[] = {
            .3, .5, .7
        };
        RawConversionSettings rs = RawConversionSettings.create( chanMul, daylightMul, 
                16000, 0, -.5, 0., RawConversionSettings.WB_MANUAL, false );
        p.setRawSettings( rs );
        RawConversionSettings rs2 = p.getRawSettings();
        assertTrue( rs.equals( rs2 ) );
        assertEquals( 16000, rs2.getWhite() );
        session.flush();
        assertMatchesDb( p );
//        List l = session.createQuery( "from RawConversionSettings where rawSettingId = :id" ).
//                setInteger( "id", p.getRawSettings().getRawSettingId() ).list();
//        assertEquals( 1, l.size() );
                
    }
    
    @Test
    public void testPreferredImageSelection() throws CommandException {
	File f = new File( testImgDir, "test1.jpg" );
	PhotoInfo photo = createPhoto( f );
        VolumeDAO volDAO = daoFactory.getVolumeDAO();
	File instanceFile = volDAO.getDefaultVolume().getFilingFname( f );
	try {
	    FileUtils.copyFile( f, instanceFile );
	} catch ( IOException e ) {
	    fail( e.getMessage() );
	}        
        ModifyImageFileCommand fileCmd = 
                new ModifyImageFileCommand( photo.getOriginal().getFile() );
        Volume vol = volDAO.getDefaultVolume();
        fileCmd.addLocation( new FileLocation( vol, 
                vol.mapFileToVolumeRelativeName( instanceFile ) ) );
        cmdHandler.executeCommand( fileCmd );
        
        // Create a copy
        CreateCopyImageCommand copyCmd 
                = new CreateCopyImageCommand(photo, vol, 200, 200 );
        CreateCopyImageCommand copy2Cmd 
                = new CreateCopyImageCommand(photo, vol, 100, 100 );
                CreateCopyImageCommand copy3Cmd 
                = new CreateCopyImageCommand(photo, vol, 300, 300 );
        copy3Cmd.setOperationsToApply(EnumSet.of( ImageOperations.COLOR_MAP ) );
                
        cmdHandler.executeCommand( copyCmd );
        cmdHandler.executeCommand( copy2Cmd );
        cmdHandler.executeCommand( copy3Cmd );
        
        ImageDescriptorBase img = photo.getPreferredImage( 
                EnumSet.allOf(ImageOperations.class),
                EnumSet.allOf(ImageOperations.class), 0, 0, 100, 100 );
        assertEquals( 100, img.getWidth() );
        img = photo.getPreferredImage( 
                EnumSet.allOf(ImageOperations.class),
                EnumSet.allOf(ImageOperations.class), 150, 150, 300, 300 );
        assertEquals( 200, img.getWidth() );
        
        photo.setPrefRotation( 90 );
        img = photo.getPreferredImage( 
                EnumSet.allOf(ImageOperations.class),
                EnumSet.allOf(ImageOperations.class), 150, 150, 300, 300 );
        assertNull( img );

        img = photo.getPreferredImage( 
                EnumSet.noneOf(ImageOperations.class),
                EnumSet.allOf(ImageOperations.class), 201, 201, 300, 300 );
        assertEquals( 300, img.getWidth() );
        
    }
    
    CommandHandler cmdHandler = new PhotovaultCommandHandler( null );
    
    PhotoInfo createPhoto( File f ) {
        ModifyImageFileCommand cmd = new ModifyImageFileCommand( f );
        try {
            cmdHandler.executeCommand( cmd );
        } catch ( CommandException ex ) {
            fail( ex.getMessage(  ) );
        }
        return (PhotoInfo) session.merge( cmd.getCreatedPhotos().iterator().next() );
    }

    /**
     Utility to check that the object in memory matches the DB
     */
    void assertMatchesDb( PhotoInfo p ) {
        assertMatchesDb( p, session );
    }
        
}

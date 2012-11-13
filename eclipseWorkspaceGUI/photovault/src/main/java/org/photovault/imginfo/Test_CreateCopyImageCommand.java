/*
  Copyright (c) 2007 Harri Kaimio
 
  This file is part of Photovault.
 
  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.
 
  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even therrore implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.
 
  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

package org.photovault.imginfo;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.classic.Session;
import org.photovault.command.CommandException;
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.common.JUnitHibernateManager;
import org.photovault.common.PhotovaultException;
import org.photovault.folder.FolderPhotoAssociation;
import org.photovault.image.ChannelMapOperationFactory;
import org.photovault.image.ColorCurve;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.HibernateDtoResolverFactory;
import org.photovault.replication.VersionedObjectEditor;
import org.photovault.test.ImgTestUtils;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 *
 * @author harri
 */
public class Test_CreateCopyImageCommand extends PhotovaultTestCase {
    
    /** Creates a new instance of Test_CreateCopyImageCommand */
    public Test_CreateCopyImageCommand() {
        super();
    }
    
    static Volume vol = null;
    
    private static Session session;
    
    static PhotoInfo photo;
    
    File refImageDir;
    
    @BeforeClass
    public void setupEnv() throws IOException, PhotovaultException {
        refImageDir = new File( System.getProperty( "basedir" ), "tests/images/photovault/imginfo/createcopy" );
        
        JUnitHibernateManager.getHibernateManager();
        session = HibernateUtil.getSessionFactory().openSession();
        
        vol = new Volume();
        File tmpdir = File.createTempFile( "pv_createcopy_test", "" );
        tmpdir.delete();
        tmpdir.mkdir();
        vol.setName( "create_copy_image" );
        VolumeManager.instance().initVolume(vol, tmpdir);
        session.save( vol );
        
    }
    
    @AfterClass
    public void cleanup() throws Exception {
        session.delete( vol );
        FileUtils.deleteTree( vol.getBaseDir() );
        session.flush();
        session.close();
    }
    
    @BeforeMethod
    public void setupPhoto() 
            throws IOException, PhotovaultException, 
            InstantiationException, IllegalAccessException {
        // Create the photo
        File testDir = new File( System.getProperty( "basedir" ), "testfiles" );
        File testFile = new File( testDir, "test1.jpg" );
        File dstFile = vol.getFilingFname( testFile );
        FileUtils.copyFile( testFile, dstFile );
        ImageFile imgFile = new ImageFile( dstFile );
        ImageFile oldFile = (ImageFile) session.get( ImageFile.class, imgFile.getId() );
        OriginalImageDescriptor orig = null;
        if ( oldFile != null ) {
            imgFile = oldFile;
            orig = (OriginalImageDescriptor) imgFile.getImage( "image#0" );
        } else {
            session.save( imgFile );
            imgFile.addLocation( new FileLocation( vol, vol.mapFileToVolumeRelativeName( dstFile ) ) );
            orig = new OriginalImageDescriptor( imgFile, "image#0" );
            session.save( orig );
        }
        DTOResolverFactory rf = new HibernateDtoResolverFactory( session );
        VersionedObjectEditor<PhotoInfo> pe = 
                new VersionedObjectEditor<PhotoInfo>( PhotoInfo.class, UUID.randomUUID(), rf );
        photo = pe.getTarget();
        session.save( photo );
        pe.setField( "original", orig );
        PhotoEditor pep = (PhotoEditor) pe.getProxy();
        
        pep.setCropBounds( new Rectangle2D.Double( 0.2, 0.2, 0.8, 0.8 ) );
        pep.setPrefRotation( 20.0 );
        ChannelMapOperationFactory f = new ChannelMapOperationFactory( null );
        ColorCurve c = new ColorCurve();
        c.addPoint( 0.0, 0.1 );
        c.addPoint( 1.0, 0.9 );
        f.setChannelCurve( "value", c );
        pep.setColorChannelMapping( f.create() );
        pe.apply();
        session.flush();
        
    }
    
    /**
     Cleans the tets photo from database before executing next tests
     @throws java.io.FileNotFoundException
     */
    @AfterMethod
    public void cleanPhoto() throws FileNotFoundException {
        photo = (PhotoInfo) session.get( PhotoInfo.class, photo.getUuid() );
        OriginalImageDescriptor orig = photo.getOriginal();
        for ( PhotoInfo p : orig.getPhotos().toArray( new PhotoInfo[0] ) ) {
            for ( FolderPhotoAssociation a : p.getFolderAssociations().toArray( new FolderPhotoAssociation[0] ) ) {
                p.removeFolderAssociation( a );
            }
            session.flush();
            orig.getPhotos().remove( p );
            session.delete( p );
        }
        session.flush();
        for ( ImageDescriptorBase copy : new ArrayList<ImageDescriptorBase>( orig.getCopies() ) ) {
            for ( FileLocation l : copy.getFile().getLocations() ) {
                l.getVolume().mapFileName( l.getFname() ).delete();
            }
            orig.removeCopy( (CopyImageDescriptor) copy );
            ImageFile f = copy.getFile();
            session.delete( f );
            session.flush();
        }
        for ( FileLocation l : orig.getFile().getLocations() ) {
            l.getVolume().mapFileName( l.getFname() ).delete();
        }
        session.delete( orig );
        session.flush();
        session.delete( orig.getFile() );
        session.flush();
        session.clear();
    }
    
    @Test
    public void testCreateCopyFromOriginal() throws CommandException {
        CreateCopyImageCommand cmd = new CreateCopyImageCommand( photo, vol, 200, 200 );
        PhotovaultCommandHandler cmdHandler = new PhotovaultCommandHandler( null );
        cmdHandler.executeCommand( cmd );
        // Test that instance is created correctly
        session.clear();
        HibernateDAOFactory daof = new HibernateDAOFactory();
        daof.setSession( session );
        PhotoInfoDAO photoDAO = daof.getPhotoInfoDAO();
        PhotoInfo p = photoDAO.findByUUID( photo.getUuid() );
        for ( CopyImageDescriptor img : p.getOriginal().getCopies() ) {
            assertEquals( 200, img.getWidth() );
            assertEquals( p.getPrefRotation(), img.getRotation() );
            assertEquals( p.getCropBounds(), img.getCropArea() );
            assertEquals( p.getColorChannelMapping(), img.getColorChannelMapping() );
            ImageFile f = img.getFile();
            Set<FileLocation> locations = f.getLocations();
            File file = f.findAvailableCopy();
            assertTrue( file.exists() );
            return;
        }
        assert false;
    }
    
    /**
     Test that exporting image creates the correct file and adds ImageFile to
     database
     */
    @Test
    public void testExportImage() throws CommandException, IOException {
        File exportFile = File.createTempFile( "pv_createcopy_export", ".jpg" );
        exportFile.delete();
        CreateCopyImageCommand cmd = new CreateCopyImageCommand( photo, exportFile, 200, 200 );
        PhotovaultCommandHandler cmdHandler = new PhotovaultCommandHandler( null );
        cmdHandler.executeCommand( cmd );
        // Test that instance is created correctly
        session.clear();
        assertTrue( exportFile.exists() );
        assertTrue( ImgTestUtils.conpareImageFiles( exportFile, new File( refImageDir, "testExportImage.jpg" ) ) );
        HibernateDAOFactory daof = new HibernateDAOFactory();
        daof.setSession( session );
        PhotoInfoDAO photoDAO = daof.getPhotoInfoDAO();
        PhotoInfo p = photoDAO.findByUUID( photo.getUuid() );
        for ( CopyImageDescriptor img : p.getOriginal().getCopies() ) {
            assertEquals( 200, img.getWidth() );
            assertEquals( p.getPrefRotation(), img.getRotation() );
            assertEquals( p.getCropBounds(), img.getCropArea() );
            assertEquals( p.getColorChannelMapping(), img.getColorChannelMapping() );
            ImageFile f = img.getFile();
            assertEquals( 0, f.getLocations().size() );
            return;
        }
        // If we get here there are no images stored in DB
        assert false;
    }
    
    /**
     Test that creating same image again succeeds
     */
    @Test
    public void testCreateSecondCopy() throws Exception {
        CreateCopyImageCommand cmd = new CreateCopyImageCommand( photo, vol, 200, 200 );
        PhotovaultCommandHandler cmdHandler = new PhotovaultCommandHandler( null );
        cmdHandler.executeCommand( cmd );
        cmd = new CreateCopyImageCommand( photo, vol, 200, 200 );
        cmdHandler.executeCommand( cmd );
        // Test that instance is created correctly
        session.clear();
        HibernateDAOFactory daof = new HibernateDAOFactory();
        daof.setSession( session );
        PhotoInfoDAO photoDAO = daof.getPhotoInfoDAO();
        PhotoInfo p = photoDAO.findByUUID( photo.getUuid() );
        for ( CopyImageDescriptor img : p.getOriginal().getCopies() ) {
            assertEquals( 200, img.getWidth() );
            assertEquals( p.getPrefRotation(), img.getRotation() );
            assertEquals( p.getCropBounds(), img.getCropArea() );
            assertEquals( p.getColorChannelMapping(), img.getColorChannelMapping() );
            ImageFile f = img.getFile();
            assertEquals( 2, f.getLocations().size() );
            for ( FileLocation l : f.getLocations() ) {
                File file = l.getFile();
                assertTrue( file.exists() );
            }
            return;
        }
        assert false;
    }
    
    /**
     Test that creating an uncropped image works
     */
    @Test
    public void testCreateUncropped() throws Exception {
        CreateCopyImageCommand cmd = new CreateCopyImageCommand( photo, vol, 200, 200 );
        cmd.setOperationsToApply( EnumSet.of(
                ImageOperations.COLOR_MAP,
                ImageOperations.RAW_CONVERSION ) );
        PhotovaultCommandHandler cmdHandler = new PhotovaultCommandHandler( null );
        cmdHandler.executeCommand( cmd );
        // Test that instance is created correctly
        session.clear();
        HibernateDAOFactory daof = new HibernateDAOFactory();
        daof.setSession( session );
        PhotoInfoDAO photoDAO = daof.getPhotoInfoDAO();
        PhotoInfo p = photoDAO.findByUUID( photo.getUuid() );
        for ( CopyImageDescriptor img : p.getOriginal().getCopies() ) {
            assertEquals( 200, img.getWidth() );
            assertEquals( 0.0, img.getRotation() );
            assertEquals( new Rectangle2D.Double( 0.0, 0.0, 1.0, 1.0 ), img.getCropArea() );
            assertEquals( p.getColorChannelMapping(), img.getColorChannelMapping() );
            ImageFile f = img.getFile();
            Set<FileLocation> locations = f.getLocations();
            File file = f.findAvailableCopy();
            assertTrue( file.exists() );
            assertTrue( ImgTestUtils.conpareImageFiles( file, new File( refImageDir, "testCreateUncropped.jpg" ) ) );
            return;
        }
        assert false;
    }
    
    /**
     Test that creating an image succeeds if original is not available
     and that error is reported if none of the copies can be used for creating
     the image.
     */
    @Test
    public void testCreateNoOriginal() throws Exception {
        // Create an image that will be used as basis for the rest of images.
        CreateCopyImageCommand cmd = new CreateCopyImageCommand( photo, vol, 400, 400 );
        cmd.setOperationsToApply( EnumSet.of(
                ImageOperations.COLOR_MAP,
                ImageOperations.RAW_CONVERSION ) );
        PhotovaultCommandHandler cmdHandler = new PhotovaultCommandHandler( null );
        cmdHandler.executeCommand( cmd );
        
        // Delete the original image file
        OriginalImageDescriptor orig = photo.getOriginal();
        Set<FileLocation> locations = new HashSet<FileLocation>( orig.getFile().getLocations() );
        for ( FileLocation l : orig.getFile().getLocations() ) {
            l.getVolume().mapFileName( l.getFname() ).delete();
            orig.getFile().getLocations().remove( l );
        }
        
        session.flush();
        session.clear();

        HibernateDAOFactory daof = new HibernateDAOFactory();
        daof.setSession( session );
        PhotoInfoDAO photoDAO = daof.getPhotoInfoDAO();
        photo = photoDAO.findByUUID( photo.getUuid() );
        
        // Try to create an instance from the copy
        cmd = new CreateCopyImageCommand( photo, vol, 200, 200 );
        boolean exceptionThrown = false;
        try {
            cmdHandler.executeCommand( cmd );
        } catch (CommandException ex) {
            exceptionThrown = true;
        }
        assert exceptionThrown;
        
        cmd.setCreateFromOriginal( false );
        cmdHandler.executeCommand( cmd );
        
        session.refresh( photo );
        boolean found = false;
        for ( CopyImageDescriptor img : photo.getOriginal().getCopies() ) {
            if ( img.getWidth() == 200 ) {
                assertEquals( 200, img.getWidth() );
                assertEquals( photo.getPrefRotation(), img.getRotation() );
                assertEquals( photo.getCropBounds(), img.getCropArea() );
                assertEquals( photo.getColorChannelMapping(), img.getColorChannelMapping() );
                ImageFile f = img.getFile();
                locations = f.getLocations();
                File file = f.findAvailableCopy();
                assertTrue( file.exists() );
                assertTrue( ImgTestUtils.conpareImageFiles( file, new File( refImageDir, "testCreateFromCopy1.jpg" ) ) );
            }
        }
    }
}

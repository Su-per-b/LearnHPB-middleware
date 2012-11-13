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

import org.photovault.imginfo.dto.ImageFileDtoResolver;
import org.photovault.imginfo.dto.ImageFileDTO;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.common.JUnitHibernateManager;
import org.photovault.common.PhotovaultException;
import org.photovault.image.ChannelMapOperation;
import org.photovault.image.ChannelMapOperationFactory;
import org.photovault.image.ColorCurve;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.VersionedObjectEditor;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;


/**
 * Unit test cases for{@link ImageFile}
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class Test_ImageFile extends PhotovaultTestCase {
    
    static Log log = LogFactory.getLog( Test_ImageFile.class.getName() );
    /** Creates a new instance of Test_ImageFile */
    public Test_ImageFile() {
    }
    
    HibernateDAOFactory daoFactory;
    ImageFileDAO ifDAO;
    Session session;
    VolumeBase vol1;
    VolumeBase vol2;
    
    @BeforeMethod
    public void setUp() {
        JUnitHibernateManager.getHibernateManager();
        session = HibernateUtil.getSessionFactory().openSession();
        daoFactory = (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );    
        daoFactory.setSession( session );
        ifDAO = daoFactory.getImageFileDAO();
        try {

            vol1 = new Volume();
            File tmpdir = File.createTempFile( "pv_imagefile_test", "" );
            tmpdir.delete();
            tmpdir.mkdir();
            vol1.setName( "vol1" );
            session.save( vol1 );
            VolumeManager.instance().initVolume( vol1, tmpdir );
            vol2 = new ExternalVolume();
            tmpdir = File.createTempFile( "pv_imagefile_test", "" );
            tmpdir.delete();
            tmpdir.mkdir();
            vol2.setName( "vol2" );
            session.save( vol2 );
            VolumeManager.instance().initVolume( vol2, tmpdir );
            session.flush();
        } catch ( Exception ex) {
            fail( "exception while creating volumes" );
        }
    }

    @AfterMethod
    public void tearDown() throws Exception {
        session.close();
        session = HibernateUtil.getSessionFactory().openSession();
        vol1 = (VolumeBase) session.get( VolumeBase.class, vol1.getId() );
        vol2 = (VolumeBase) session.get( VolumeBase.class, vol2.getId() );
        session.delete( vol1 );
        session.delete( vol2 );
        FileUtils.deleteTree( vol1.getBaseDir() );
        FileUtils.deleteTree( vol2.getBaseDir() );
        session.close();
    }
    
   
    @Test
    public void testImageFileCreate() {
        Transaction tx = session.beginTransaction();
        ImageFile i = new ImageFile();
        i.setId( UUID.randomUUID() );
        i.setFileSize( 1000000 );
        Date lastChecked = new Date();
        FileLocation location = new FileLocation( vol1, "testfile1" );
        location.setLastChecked( lastChecked );
        location.setLastModified( lastChecked.getTime() );
        FileLocation location2 = new FileLocation( vol2, "testfile1" );
        location2.setLastChecked( lastChecked );
        location2.setLastModified( lastChecked.getTime() );
        i.addLocation( location );
        i.addLocation( location2 );
        ifDAO.makePersistent( i );
        session.flush();
        tx.commit();
        assertMatchesDb( i, session );
        
        // Try to reload the objects
        session.clear();
        
        ImageFile i2 = ifDAO.findById( i.getId(), false );
        assert i2.getFileSize() == i.getFileSize();
        // assert i2.getHash().equals( i.getHash() );
        Set<FileLocation> locations = i2.getLocations();
        for ( FileLocation l : locations ) {
            log.debug( "Found location " + l.getFname() );
        }
        assertEquals(2, locations.size() );
        boolean vol1Found = false;
        boolean vol2Found = false;
        for ( FileLocation l : locations ) {
            VolumeBase vol = l.getVolume();
            if ( vol.getName().equals( "vol1" ) ) {
                assert vol instanceof Volume;
                assert vol.getBaseDir().equals( vol1.getBaseDir() ); 
                vol1Found = true;
            } else if ( vol.getName().equals( "vol2" ) ) {
                assert vol instanceof ExternalVolume;
                assert vol.getBaseDir().equals( vol2.getBaseDir() ); 
                vol2Found = true;
            } else {
                fail( "Unknown volume" );
            }
        }
        assert vol1Found;
        assert vol2Found;
    }
    
    @Test
    public void testImageDescriptorPersistence() 
            throws InstantiationException, InstantiationException, IllegalAccessException {
        Transaction tx = session.beginTransaction();
        PhotoInfoDAO photoDAO = daoFactory.getPhotoInfoDAO();
        ImageDescriptorDAO idDAO = daoFactory.getImageDescriptorDAO();
        ImageFile f1 = new ImageFile();
        f1.setId( UUID.randomUUID() );
        ImageFile f2 = new ImageFile();
        f2.setId( UUID.randomUUID() );
        ifDAO.makePersistent( f1 );
        ifDAO.makePersistent( f2 );
        
        OriginalImageDescriptor i11 = new OriginalImageDescriptor( f1, "image#0" );
        i11.setWidth( 1200 );
        i11.setHeight( 1400 );
        idDAO.makePersistent( i11 );
        DTOResolverFactory rf = daoFactory.getDTOResolverFactory();
        VersionedObjectEditor<PhotoInfo> pe = 
                new VersionedObjectEditor<PhotoInfo>( PhotoInfo.class, UUID.randomUUID(), rf );
        pe.setField( "original", i11 );
        pe.apply();
        PhotoInfo p1 = pe.getTarget();
        photoDAO.makePersistent( p1 );
        photoDAO.flush();
        CopyImageDescriptor i12 = new CopyImageDescriptor( f1, "image#1", i11 );
        i12.setCropArea( new Rectangle2D.Double( 0.1, 0.2, 0.3, 0.4 ) );
        i12.setWidth( 300 );
        i12.setHeight( 400 );
        idDAO.makePersistent( i12 );
        OriginalImageDescriptor i21 = new OriginalImageDescriptor( f2, "image#0" );
        i21.setWidth( 2000 );
        i21.setHeight( 3000 );
        idDAO.makePersistent( i21 );
        pe = 
                new VersionedObjectEditor<PhotoInfo>( PhotoInfo.class, UUID.randomUUID(), rf );
        pe.setField( "original", i21 );
        pe.apply();
        PhotoInfo p2 = pe.getTarget();
        photoDAO.makePersistent( p2 );
        photoDAO.flush();
        pe = 
                new VersionedObjectEditor<PhotoInfo>( PhotoInfo.class, UUID.randomUUID(), rf );
        pe.setField( "original", i21 );
        pe.apply();
        PhotoInfo p3 = pe.getTarget();
        photoDAO.makePersistent( p3 );
        photoDAO.flush();
        CopyImageDescriptor i22 = new CopyImageDescriptor( f2, "image#1", i11 );
        i22.setWidth( 400 );
        i22.setHeight( 400 );
        i22.setRotation( 90.0 );
        ChannelMapOperationFactory f = new ChannelMapOperationFactory();
        ColorCurve c = new ColorCurve();
        c.addPoint( 0.0, 0.1 );
        c.addPoint( 1.0, 0.9 );
        f.setChannelCurve( "value", c );
        ChannelMapOperation cm = f.create();
        i22.setColorChannelMapping( cm );
        idDAO.makePersistent( i22 );
        
        session.flush();
        tx.commit();
        
        // Verify that changes were persisted correctly
        session.clear();
        ImageFile vf1 = ifDAO.findById( f1.getId(), false );
        int vf1isize = vf1.getImages().size();
        assert vf1.getImages().size() == 2;
        OriginalImageDescriptor vi11 = (OriginalImageDescriptor) vf1.getImages().get( "image#0" );
        CopyImageDescriptor vi12 = (CopyImageDescriptor) vf1.getImages().get( "image#1" );
        assertEquals( 2, vi11.getCopies().size() );
        assertTrue( vi12.getOriginal() == vi11 );
        ImageFile vf2 = ifDAO.findById( f2.getId(), false );
        assertEquals( 2, vf2.getImages().size() );
        assertEquals( 1, vi11.getPhotos().size() );
        
        OriginalImageDescriptor vi21 = (OriginalImageDescriptor) vf2.getImages().get( "image#0" );
        assertEquals( 0, vi21.getCopies().size() );
        CopyImageDescriptor vi22 = (CopyImageDescriptor) vf2.getImages().get( "image#1" );
        assert vi22.getOriginal() == vi11;
        assertEquals( 0.1, vi12.getCropArea().getMinX() );
        assertEquals( cm, vi22.getColorChannelMapping() );
        assertEquals( 2, vi21.getPhotos().size() );
        boolean foundP2 = false;
        boolean foundP3 = false;
        for ( PhotoInfo p : vi21.getPhotos() ) {
            assert p.getOriginal() == vi21;
            if ( p.getUuid().equals( p2.getUuid() ) ) {
                foundP2 = true;
            } else if ( p.getUuid().equals( p3.getUuid() ) ) {
                foundP3 = true;
            } 
        }
        assert foundP2;
        assert foundP3;
    }
    
    @Test
    public void testDto() throws InstantiationException, IllegalAccessException {
        PhotoInfoDAO photoDAO = daoFactory.getPhotoInfoDAO();
        ImageDescriptorDAO idDAO = daoFactory.getImageDescriptorDAO();
        ImageFile f1 = new ImageFile();
        f1.setId( UUID.randomUUID() );
        ifDAO.makePersistent( f1 );
        
        OriginalImageDescriptor i11 = new OriginalImageDescriptor( f1, "image#0" );
        i11.setWidth( 1200 );
        i11.setHeight( 1400 );
        idDAO.makePersistent( i11 );
        DTOResolverFactory rf = daoFactory.getDTOResolverFactory();
        
        VersionedObjectEditor<PhotoInfo> pe = 
                new VersionedObjectEditor<PhotoInfo>( PhotoInfo.class, UUID.randomUUID(), rf );
        pe.setField( "original", i11 );
        pe.apply();
        PhotoInfo p1 = pe.getTarget();
        photoDAO.makePersistent( p1 );
        session.flush();
        
        // Create another ImageFile but don't persist it, just create mathcihng DTO
        ImageFile f2 = new ImageFile();
        f2.setId( UUID.randomUUID() );
        CopyImageDescriptor i21 = new CopyImageDescriptor( f2, "image#0", i11 );
        i21.setWidth( 2000 );
        i21.setHeight( 3000 );

        // ii1 is not corrupted as it has reference to nonpersistent i21
        session.clear();
        f1 = ifDAO.findById( f1.getId(), false );
        i11 = (OriginalImageDescriptor) f1.getImage( "image#0" );
        
        ImageFileDTO fdto1 = new ImageFileDTO( f2 );
        assertEquals(  fdto1.getUuid() ,f2.getId() );
        assertEquals(  fdto1.getHash() ,f2.getHash() );        
        assertEquals(  fdto1.getSize() ,f2.getFileSize() );

                
        ImageFileDtoResolver resolver = new ImageFileDtoResolver();
        resolver.setSession( session );
        ImageFile resolvedf2 = resolver.getObjectFromDto( fdto1 );
        assertEquals(f2.getId(), resolvedf2.getId() );
        assertTrue( ((CopyImageDescriptor)resolvedf2.getImage( "image#0")).getOriginal() == i11 );
    }
    
    @Test
    public void testImageCreationCmd() throws PhotovaultException, IOException {
        File testDir = new File( System.getProperty( "basedir" ), "testfiles" );
        File testFile = new File( testDir, "test1.jpg" );
        ModifyImageFileCommand cmd = 
                new ModifyImageFileCommand( testFile );
        PhotovaultCommandHandler cmdHandler = new PhotovaultCommandHandler( null );
        cmdHandler.executeCommand( cmd );
        
        ImageFile f = cmd.getImageFile();
        UUID fileId = f.getId();
        // UUID instanceId = cmd.getImageInstance().getUuid();
        
        f = ifDAO.findById( fileId, false );
        assert f.getFileSize() == testFile.length();
        assert f.getImages().size() == 1;
        OriginalImageDescriptor img = (OriginalImageDescriptor) f.getImage( "image#0" );
        assertEquals( 1, img.getPhotos().size() );
        assertEquals( 0, f.getLocations().size() ); 
        
        // Now, add the file to default volume
        VolumeDAO volDAO = daoFactory.getVolumeDAO();
        VolumeBase defaultVolume = vol1;
        File fl = defaultVolume.getFilingFname( testFile );
        FileUtils.copyFile( testFile, fl );
        cmd = new ModifyImageFileCommand( f );
        cmd.addLocation( 
                new FileLocation( defaultVolume, 
                defaultVolume.mapFileToVolumeRelativeName(fl) ) );
        cmdHandler.executeCommand( cmd );
        
        session.clear();
        f = cmd.getImageFile();
        fileId = f.getId();        
        f = ifDAO.findById( fileId, false );
        assertEquals( 1, f.getLocations().size() ); 
        FileLocation loc = f.getLocations().iterator().next();
        assertEquals( loc.getVolume().getId(), defaultVolume.getId() );
        assertEquals( loc.getFile().lastModified(), loc.getLastModified() );
    }
    
    @Test
    public void testFindAvailableCopy() throws IOException, PhotovaultException {
        File testDir = new File( System.getProperty( "basedir" ), "testfiles" );
        File testFile = new File( testDir, "test2.jpg" );
        File testFileDst = new File( vol2.getBaseDir(), "test2.jpg" );
        FileUtils.copyFile( testFile, testFileDst );
        ImageFile imgFile = new ImageFile( testFileDst );
        File f = imgFile.findAvailableCopy();
        assertNull( f );
        imgFile.addLocation( new FileLocation( vol2, "test2.jpg") );
        f = imgFile.findAvailableCopy();
        assertEquals( testFileDst, f );
    }

    @Test
    public void testFindByLocation() throws IOException, PhotovaultException {
        File testDir = new File( System.getProperty( "basedir" ), "testfiles" );
        File testFileOrig = new File( testDir, "test2.jpg" );
        File testFile2Orig = new File( testDir, "test3.jpg" );
        File testFile = new File( vol2.getBaseDir(), "test2.jpg" );
        FileUtils.copyFile( testFileOrig, testFile );
        File testFile2 = new File( vol2.getBaseDir(), "test3.jpg" );
        FileUtils.copyFile( testFile2Orig, testFile2 );
        ImageFile imgFile = new ImageFile( testFile );
        imgFile.addLocation( new FileLocation( vol2, "test2.jpg" ) );
        ImageFile imgFile2 = new ImageFile( testFile2 );
        imgFile2.addLocation( new FileLocation( vol2, "test3.jpg" ) );
        ifDAO.makePersistent( imgFile );
        ifDAO.makePersistent( imgFile2 );
        session.flush();
        assertEquals( imgFile, ifDAO.findFileInLocation( (ExternalVolume) vol2, "test2.jpg"  ) );
        assertEquals( imgFile2, ifDAO.findFileInLocation( (ExternalVolume) vol2, "test3.jpg"  ) );
        assertNull( ifDAO.findFileInLocation( (ExternalVolume) vol2, "test4.jpg"  ) );
    }
    
    /**
     Test that PhotoInfo#findPreferredInstance returns correct instance
     */
    public void testFindPreferredInstance() {
        
    }
    
    /**
     Test that deleting a photo deletes also correct files from volumes but leaves
     the ImageFile objects in database.
     */
    @Test( enabled = false )
    public void testPhotoDelete() {
        throw new UnsupportedOperationException( "Photo deletion not yet implemented" );
    }
    
    /**
     Test that deleting photo does not delete files from repository when there
     are several photos based on same original.
     */
    @Test( enabled = false )
    public void testPhotoDeleteMultiplePhotosForOriginal() {
        throw new UnsupportedOperationException( "Photo deletion not yet implemented" );        
    }
    
    private void assertMatchesDb( ImageFile i, Session session ) {
        String query = "select ins.file_size file_size" +
                " from pv_image_files ins where ins.id = :id";
        Object result = 
                session.createSQLQuery( query ).
                addScalar( "file_size", Hibernate.LONG ).
                setString( "id", i.getId().toString()  ).
                uniqueResult();
        assert result.equals( i.getFileSize() );
    }
}

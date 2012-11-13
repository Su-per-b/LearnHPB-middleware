/*
  Copyright (c) 2008 Harri Kaimio
  
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

package org.photovault.replication;

import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.photovault.folder.FolderEditor;
import org.photovault.folder.FolderPhotoAssociation;
import org.photovault.folder.PhotoFolder;
import org.photovault.imginfo.ImageFile;
import org.photovault.imginfo.ImageFileDAO;
import org.photovault.imginfo.OriginalImageDescriptor;
import org.photovault.imginfo.PhotoEditor;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoDAO;
import org.photovault.imginfo.PhotoInfoFields;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 Test cases for change persistence
 */
public class Test_ChangePersistence extends PhotovaultTestCase {
    private Session session;
    
    public Test_ChangePersistence() {
        super();
    }

    /**
     Sets ut the test environment
     */
    @Override
    @BeforeMethod
    public void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
    }


    @AfterMethod
    public void tearDown() {
        session.close();
    }

    /**
     Create a small grap of changes, store it into database, load it in another 
     session and verify.
     */
    @Test
    public void testChangePersistence() {
        DTOResolverFactory rf = new HibernateDtoResolverFactory( session );
        PhotoInfo p = PhotoInfo.create();
        VersionedObjectEditor<PhotoInfo> e1 = new VersionedObjectEditor( p, rf );
        e1.apply();
        Change<PhotoInfo> c1 = e1.change;
        VersionedObjectEditor<PhotoInfo> e2 = new VersionedObjectEditor( p, rf );
        e2.setField( "camera", "Canon 30D" );
        e2.apply();
        Change<PhotoInfo> c2 = e2.change;
        VersionedObjectEditor<PhotoInfo> e3 = new VersionedObjectEditor( p, rf );
        e3.changeToVersion( c1 );
        e3.setField( "photographer", "Harri" );
        e3.apply();
        Change<PhotoInfo> c3 = e3.change;
        Change<PhotoInfo> c4 = c2.merge(  c3 );
        c4.freeze();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate( p );
        tx.commit();
        
        Session sess2 = HibernateUtil.getSessionFactory().openSession();
        ObjectHistory<PhotoInfo> s2cs = 
                (ObjectHistory<PhotoInfo>) sess2.get( 
                ObjectHistory.class, p.getUuid() );
        assertEquals( 1, s2cs.getHeads().size() );
        Change<PhotoInfo> s2c4 = s2cs.getHeads().iterator().next();
        assertEquals( c4.getUuid(), s2c4.getUuid() );
        assertEquals( 2, s2c4.getParentChanges().size() );
        Change<PhotoInfo> s2c2 = null;
        Change<PhotoInfo> s2c3 = null;
        for ( Change<PhotoInfo> c : s2c4.getParentChanges() ) {
            if ( c.getUuid().equals( c2.getUuid() )) {
                s2c2 = c;
            }
            if ( c.getUuid().equals( c3.getUuid() )) {
                s2c3 = c;
            }
        }
        assertNotNull( s2c2 );
        assertNotNull( s2c3 );
        assertTrue( s2c2.getChangedFields().containsKey( "camera" ) );
        assertEquals( "Canon 30D", ((ValueChange)s2c2.getChangedFields().get(  "camera" )).getValue() );
        sess2.close();
    }
    
    
    /**
     Test that changes can be serialized correctly and merging them to a 
     persistence context using {@link ChangeFactory} is successfull
     @throws java.io.IOException
     @throws java.lang.ClassNotFoundException
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        DTOResolverFactory rf = new HibernateDtoResolverFactory( session );
        // Create a new photo with associated history
        PhotoInfo p = PhotoInfo.create();
        VersionedObjectEditor<PhotoInfo> e1 = new VersionedObjectEditor( p, rf );
        e1.apply();               
        Change<PhotoInfo> c1 = e1.change;
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate( p );
        tx.commit();
        
        /**
         Create a few changes that won't be persisted but serialised to 
         a byte array
         */
        tx = session.beginTransaction();
        HibernateDAOFactory daoFactory = new HibernateDAOFactory();
        daoFactory.setSession( session );
        ChangeDAO<PhotoInfo> chDao = daoFactory.getChangeDAO( );
        PhotoInfoDAO photoDAO = daoFactory.getPhotoInfoDAO();
        p = photoDAO.findByUUID( p.getUuid() );
        
        VersionedObjectEditor<PhotoInfo> e2 = new VersionedObjectEditor( p, rf );
        e2.setField( "camera", "Canon 30D" );
        e2.apply();       
        Change<PhotoInfo> c2 = e2.change;

        VersionedObjectEditor<PhotoInfo> e3 = new VersionedObjectEditor( p, rf );
        e3.changeToVersion( c1 );
        e3.setField( "photographer", "Harri" );
        e3.apply();
        Change<PhotoInfo> c3 = e3.change;


        // Create en empty change
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( os );
        ObjectHistoryDTO<PhotoInfo> h = new ObjectHistoryDTO( p.getHistory() );
        oos.writeObject( h );
        byte[] serialized = os.toByteArray();
        tx.rollback();
        
        // Create a new session so that it is not aware of c2 or c3
        session.clear();
        
        daoFactory.setSession( session );
        chDao = daoFactory.getChangeDAO( );
        ChangeFactory<PhotoInfo> cf = new ChangeFactory( chDao );
        Change<PhotoInfo> s2c1 = chDao.findById( c1.getUuid(), false );
        assertEquals( 0, s2c1.getChildChanges().size() );
        assertEquals( c1.getUuid(), s2c1.getUuid() );
        
        ByteArrayInputStream is = new ByteArrayInputStream(  serialized );
        ObjectInputStream ios = new ObjectInputStream( is );
        h = (ObjectHistoryDTO<PhotoInfo>) ios.readObject();
        p = photoDAO.findByUUID( h.getTargetUuid() );
        VersionedObjectEditor<PhotoInfo> ed = new VersionedObjectEditor<PhotoInfo>(  p,  rf );
        ed.addToHistory( h, cf );
        
        Map<UUID, Change<PhotoInfo>> changes = new HashMap<UUID, Change<PhotoInfo>>();
        tx = session.beginTransaction();
        for( Change<PhotoInfo> ch : p.getHistory().getChanges() ) {
            changes.put( ch.getUuid(), ch );
        }
        
        Change<PhotoInfo> serc1 = changes.get( s2c1.getUuid() );
        assertTrue( serc1 == s2c1 );
        Change<PhotoInfo> serc2 = changes.get( c2.getUuid() );
        Change<PhotoInfo> serc3 = changes.get( c3.getUuid() );
        assertEquals( c2.getUuid(), serc2.getUuid() );
        assertEquals( c3.getUuid(), serc3.getUuid() );
        assertEquals( 2, s2c1.getChildChanges().size() );
        assertEquals( 3, s2c1.getTargetHistory().getChanges().size() );
        assertEquals( 2, s2c1.getTargetHistory().getHeads().size() );
        assertEquals( c1.getUuid(), UUID.nameUUIDFromBytes( serc1.getSerializedChange() ) );
        assertEquals( c2.getUuid(), UUID.nameUUIDFromBytes( serc2.getSerializedChange() ) );
        assertEquals( c3.getUuid(), UUID.nameUUIDFromBytes( serc3.getSerializedChange() ) );
        p = photoDAO.findByUUID( s2c1.getTargetHistory().getTargetUuid() );
        
        VersionedObjectEditor<PhotoInfo> pe = p.editor( rf );
        pe.changeToVersion( serc3 );
        assertEquals( "Harri", p.getPhotographer() );
                
        tx.commit();
    }
    
    /**
     Test that references to immutable object are serialized correctly.
     
     The test creates (but does not save) a {@link PhotoInfo} that references
     original image in file stored in database. serializes it and creates 
     database instance by deserializing it.
     */
    @Test
    public void testRefSerialization() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        DTOResolverFactory fieldResolver = new HibernateDtoResolverFactory( session );
        
        // Create image file in the database
        ImageFile ifile = new ImageFile();
        ifile.setFileSize( 100000 );
        ifile.setId( UUID.randomUUID() );
        ifile.setHash( new byte[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
        UUID ifileUuid = ifile.getId();
        OriginalImageDescriptor orig = new OriginalImageDescriptor( ifile, "image#0" );

        Transaction tx = session.beginTransaction();
        session.saveOrUpdate( ifile );
        DTOResolverFactory rf = new HibernateDtoResolverFactory( session );
        VersionedObjectEditor<PhotoFolder> fe = new VersionedObjectEditor<PhotoFolder>( PhotoFolder.class, UUID.randomUUID(), rf );
        FolderEditor fep = (FolderEditor) fe.getProxy();
        fep.setName( "Koe" );
        fe.apply();
        PhotoFolder f = fe.getTarget();
        session.saveOrUpdate( f );
        tx.commit();
                
        
        tx = session.beginTransaction();
        VersionedObjectEditor<PhotoInfo> e1 = new VersionedObjectEditor<PhotoInfo>( PhotoInfo.class, UUID.randomUUID(), fieldResolver );
        e1.setField( "original", orig );
        e1.apply();
        PhotoInfo p = e1.getTarget();
        
        VersionedObjectEditor<PhotoInfo> e2 = new VersionedObjectEditor<PhotoInfo>( p, fieldResolver );
        e2.setField(PhotoInfoFields.PHOTOGRAPHER.getName(), "Harri" );
        e2.setField(PhotoInfoFields.FSTOP.getName(), 5.6 );
        e2.setField( "film", "Tri-X" );
        
        FolderPhotoAssociation a = new FolderPhotoAssociation( f, p );
        e2.addToSet( "folderAssociations", a );
        
        e2.apply();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( os );
        oos.writeObject( new ObjectHistoryDTO( p.getHistory() ) );
        byte[] serialized = os.toByteArray();
        tx.rollback();

        // Start a new session
        session.close();
        session = HibernateUtil.getSessionFactory().openSession();
        HibernateDAOFactory daoFactory = new HibernateDAOFactory();
        daoFactory.setSession( session );
        ifile = daoFactory.getImageFileDAO().findById( ifile.getId(), false ); 
        fieldResolver = new HibernateDtoResolverFactory( session );
        
        // Try to deserialize the changes and verify that the photo references
        // the exising image
        ChangeDAO<PhotoInfo> chDao = daoFactory.getChangeDAO( );
        PhotoInfoDAO photoDao = daoFactory.getPhotoInfoDAO();
        ChangeFactory<PhotoInfo> cf = new ChangeFactory( chDao );
        
        ByteArrayInputStream is = new ByteArrayInputStream(  serialized );
        ObjectInputStream ios = new ObjectInputStream( is );
        tx = session.beginTransaction();
        ObjectHistoryDTO<PhotoInfo> h = (ObjectHistoryDTO<PhotoInfo>) ios.readObject();
        VersionedObjectEditor<PhotoInfo> ed = new VersionedObjectEditor<PhotoInfo>( PhotoInfo.class, p.getUuid(), fieldResolver );
        p = ed.getTarget();
        photoDao.makePersistent( p );
        photoDao.flush();
        ed.addToHistory( h, cf );

        // Ensure that the deserialized changes are not corrupted
        ObjectHistoryDTO dto = new ObjectHistoryDTO( p.getHistory() );

        Map<UUID, Change<PhotoInfo>> changes = new HashMap<UUID, Change<PhotoInfo>>();
        tx = session.beginTransaction();
        for( Change<PhotoInfo> ch : p.getHistory().getChanges() ) {
            changes.put( ch.getUuid(), ch );
        }
        
        ImageFileDAO ifileDao = daoFactory.getImageFileDAO();
        ifile = ifileDao.findById( ifileUuid, false );
        assert( p.getOriginal().getFile() == ifile );
        assertEquals( 1, p.getFolderAssociations().size() );
    }    
  
    /**
     Test starting VersionedObjectEditor without existing target object
     @throws java.lang.InstantiationException
     @throws java.lang.IllegalAccessException
     */
    @Test
    public void testEditorWithNewObject() 
            throws InstantiationException, IllegalAccessException {
        DTOResolverFactory fieldResolver = 
                new HibernateDtoResolverFactory( session );
        UUID uuid = UUID.randomUUID();
        VersionedObjectEditor<PhotoInfo> e = 
                new VersionedObjectEditor( PhotoInfo.class, uuid, fieldResolver );
        PhotoEditor pe = (PhotoEditor) e.getProxy();
        pe.setPhotographer( "photographer" );
        pe.setFStop( 5.6 );
        Change<PhotoInfo> c1 = e.apply();
        Change<PhotoInfo> initial = c1.getParentChanges().iterator().next();
        assertEquals( 0, initial.getParentChanges().size() );
        PhotoInfo p = e.getTarget();
        assertEquals( 5.6, p.getFStop() );
        assertEquals( "photographer", p.getPhotographer() );
        assertEquals( uuid, p.getUuid() );

    }
    
}

/*
  Copyright (c) 2009 Harri Kaimio

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

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.photovault.image.CropOp;
import org.photovault.image.DCRawMapOp;
import org.photovault.image.DCRawOp;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.Change;
import org.photovault.replication.HibernateDtoResolverFactory;
import org.photovault.replication.VersionedObjectEditor;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 * Test cases for {@link ProcGraphEditorHelper}
 * @author Harri Kaimio
 * @since 0.6.0
 */
/**
 *
 * @author harri
 */
public class Test_ProcGraphEditorHelper extends PhotovaultTestCase {
    private Session session;
    private HibernateDAOFactory daoFactory;
    private PhotoInfoDAO photoDAO;
    private Transaction tx;


    /**
     Sets ut the test environment
     */
    @Override
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

    @Test
    public void testGraphNewNodes() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        HibernateDtoResolverFactory rf = new HibernateDtoResolverFactory(
                session );
        VersionedObjectEditor<PhotoInfo> e =
                new VersionedObjectEditor<PhotoInfo>( PhotoInfo.class,
                UUID.randomUUID(), rf );
        ProcGraphEditorHelper pe =
                new ProcGraphEditorHelper( e, "dcraw", DCRawOp.class );
        pe.setProperty( "black", 20 );
        pe.setProperty( "daylightRedGreenRatio", 1.0 );
        pe.setProperty( "daylightBlueGreenRatio", 2.0 );
        pe.setProperty( "colorTemp", 5500.0 );
        pe.setProperty( "greenGain", 1.1 );
        assertFalse( pe.isNodeAlreadyPresent() );
        assertTrue( pe.addNewNode( null, null ) );
        Change<PhotoInfo> ch1 = e.apply();
        assertTrue( ch1.getChangedFields().containsKey( "processing" ) );
        PhotoInfo p = e.getTarget();
        assertEquals( 20, ((DCRawOp)p.getProcessing().getOperation( "dcraw" )).getBlack() );
        assertEquals( "dcraw.out", p.getProcessing().getHead() );

        e = new VersionedObjectEditor<PhotoInfo>( p, rf );
        pe = new  ProcGraphEditorHelper( e, "crop", CropOp.class );
        pe.setProperty( "rot", 5.0 );
        e.setField( "processing.head", "crop.out" );
        assertFalse( pe.isNodeAlreadyPresent() );
        assertTrue( pe.addNewNode( "dcraw.out", null ) );
        Change<PhotoInfo> ch2 = e.apply();
        CropOp crop = (CropOp) p.getProcessing().getOperation( "crop" );
        assertEquals( 5.0, ((CropOp)p.getProcessing().getOperation( "crop" )).getRot() );
        assertEquals( "dcraw.out", crop.getInputPort( "in" ).getSourceName() );
        assertEquals( "crop.out", p.getProcessing().getHead() );

        // Add new node between the existing nodes
        e = new VersionedObjectEditor<PhotoInfo>( p, rf );
        pe = new  ProcGraphEditorHelper( e, "raw-map", DCRawMapOp.class );
        pe.setProperty( "evCorr", -1.0 );
        assertFalse( pe.isNodeAlreadyPresent() );
        assertTrue( pe.addNewNode( "dcraw.out", "crop.in" ) );
        Change<PhotoInfo> ch3 = e.apply();
        DCRawMapOp rawmap = (DCRawMapOp) p.getProcessing().getOperation( "raw-map" );
        assertEquals( -1.0, rawmap.getEvCorr() );
        assertEquals( "dcraw.out", rawmap.getInputPort( "in" ).getSourceName() );

        // Edit existing node
        e = new VersionedObjectEditor<PhotoInfo>( p, rf );
        pe = new  ProcGraphEditorHelper( e, "raw-map", DCRawMapOp.class );
        pe.setProperty( "hlightCompr", 1.0 );
        assertTrue( pe.isNodeAlreadyPresent() );
        assertFalse( pe.addNewNode( "dcraw.out", "crop.in" ) );
        Change<PhotoInfo> ch4 = e.apply();
        assertEquals( 1.0, rawmap.getHlightCompr() );

    }
}

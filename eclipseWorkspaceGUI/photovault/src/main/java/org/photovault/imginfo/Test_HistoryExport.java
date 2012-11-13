/*
  Copyright (c) 2010 Harri Kaimio
  
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;
import org.hibernate.classic.Session;
import org.photovault.image.DCRawMapOp;
import org.photovault.image.ImageOpChain;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.HibernateDtoResolverFactory;
import org.photovault.replication.VersionedObjectEditor;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 *
 * @author harri
 */
public class Test_HistoryExport extends PhotovaultTestCase {
    private Session session;
    private HibernateDAOFactory daoFactory;
    private ImageFileDAO ifDAO;


    @Test
    public void testFileExport() throws InstantiationException,
            IllegalAccessException, IOException {
        session = HibernateUtil.getSessionFactory().openSession();
        daoFactory = (HibernateDAOFactory) DAOFactory.instance(
                HibernateDAOFactory.class );
        daoFactory.setSession( session );
        ifDAO = daoFactory.getImageFileDAO();
        PhotoInfoDAO photoDao = daoFactory.getPhotoInfoDAO();
        ImageFile i = new ImageFile();
        i.setHash( new byte[16] );
        i.setFileSize( 1000000 );
        OriginalImageDescriptor orig =
                new OriginalImageDescriptor( i, "image#0" );
        ifDAO.makePersistent( i );
        HibernateDtoResolverFactory rf = new HibernateDtoResolverFactory(
                session );
        UUID photoId = UUID.randomUUID();
        VersionedObjectEditor<PhotoInfo> pe = new VersionedObjectEditor(
                PhotoInfo.class, photoId, rf );
        pe.setField( "original", orig );

        pe.apply();
        photoDao.makePersistent( pe.getTarget() );

        DataExporter exp = new DataExporter();
        exp.exportFileInfo( i, new File( "/tmp/test.zip" ) );
    }

    @Test
    public void testHistoryImport() throws FileNotFoundException, IOException {
        session = HibernateUtil.getSessionFactory().openSession();
        daoFactory = (HibernateDAOFactory) DAOFactory.instance(
                HibernateDAOFactory.class );
        daoFactory.setSession( session );
        DataExporter exp = new DataExporter();
        File testDir = new File( System.getProperty( "basedir" ), "testfiles" );
        File historyFile = new File( testDir,
                "testHistoryImport/export_testdb_1.pv" );
        exp.importChanges( historyFile, daoFactory );
        PhotoInfoDAO photoDao = daoFactory.getPhotoInfoDAO();
        PhotoInfo p = photoDao.findByUUID( UUID.fromString( 
                "018185f4-c9a7-4d54-a3d5-aa0ceb6641cd" ) );
        assertNotNull( p );
        assertEquals( "Canon EOS 30D", p.getCamera() );
        assertEquals( UUID.fromString( "98ccd908-04e2-351f-8cb8-60d60f820a33" ),
                p.getHistory().getVersion().getUuid() );
        File historyFile2 = new File( testDir,
                "testHistoryImport/export_testdb_2.pv" );
        exp.importChanges( historyFile2, daoFactory );
        p = photoDao.findByUUID( UUID.fromString( 
                "018185f4-c9a7-4d54-a3d5-aa0ceb6641cd" ) );
        assertEquals( UUID.fromString( "f5d157af-7d34-32c8-aac6-7ab0bbfc24d6" ),
                p.getHistory().getVersion().getUuid() );
        ImageOpChain processing = p.getProcessing();
        Rectangle2D crop = processing.getCropping();
        DCRawMapOp rawMapOp = (DCRawMapOp) processing.getOperation( "raw-map" );
        assertEquals( -0.77f, rawMapOp.getEvCorr(), 0.01 );
        assertEquals( -1.0, rawMapOp.getHlightCompr(), 0.01 );
        File historyFile3 = new File( testDir,
                "testHistoryImport/export_testdb_3.pv" );
        exp.importChanges( historyFile3, daoFactory );
        p = photoDao.findByUUID( UUID.fromString(
                "018185f4-c9a7-4d54-a3d5-aa0ceb6641cd" ) );
        assertEquals( UUID.fromString( "37e02b46-bdd2-3d94-b31f-4e77598fa93d" ),
                p.getHistory().getVersion().getUuid() );
        PhotoInfo p2 = photoDao.findByUUID( UUID.fromString(
                "81d9c13c-892b-4810-abeb-ae2958c2cb91" ) );

        assertEquals( 2, p2.getHistory().getHeads().size() );
    }
}

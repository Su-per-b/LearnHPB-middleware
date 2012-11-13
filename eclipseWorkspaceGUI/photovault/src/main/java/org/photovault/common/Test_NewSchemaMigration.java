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

package org.photovault.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DataReader;
import org.apache.ddlutils.io.DataToDatabaseSink;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.folder.PhotoFolder;
import org.photovault.folder.PhotoFolderDAO;
import org.photovault.imginfo.CopyImageDescriptor;
import org.photovault.imginfo.FileLocation;
import org.photovault.imginfo.FileUtils;
import org.photovault.imginfo.FuzzyDate;
import org.photovault.imginfo.ImageOperations;
import org.photovault.imginfo.OriginalImageDescriptor;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoDAO;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.Change;
import org.photovault.replication.ObjectHistory;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

/**
 Test cases for migrating from the old (0.5.0) schema to the new one used by 
 0.6.0.
 */
public class Test_NewSchemaMigration extends PhotovaultTestCase {
    
    static Log log = LogFactory.getLog( Test_NewSchemaMigration.class.getName() );
    
    /** 
     Add the tables from previous schema and populate with data from 
     testfiles/migration_test_data_0.5.0.xml.
     */
    @BeforeClass
    public void setUpTestCase() throws DdlUtilsException, SQLException, IOException  {
        PVDatabase db = PhotovaultSettings.getSettings().getCurrentDatabase();
        int oldVersion = db.getSchemaVersion();
        
        // Find needed information fr DdlUtils
        Session session = HibernateUtil.getSessionFactory().openSession();

        Platform platform = null;
        if ( db.getDbDescriptor() instanceof DerbyDescriptor ) {
            platform = PlatformFactory.createNewPlatformInstance( "derby" );            
        } else if ( db.getDbDescriptor() instanceof MysqlDescriptor ) {
            platform = PlatformFactory.createNewPlatformInstance( "mysql" );
        }
        platform.getPlatformInfo().setDelimiterToken( "" );
        
        // Get the database schema XML file
        InputStream schemaIS = getClass().getClassLoader().getResourceAsStream( "db_schema_0.5.0.xml" );
        DatabaseIO dbio = new DatabaseIO();
        dbio.setValidateXml( false );
        Database dbModel = dbio.read( new InputStreamReader( schemaIS ) );

        // Alter tables to match corrent schema
        Transaction tx = session.beginTransaction();
        final Connection con = session.connection();
        DataSource ds = new DataSource() {

            public Connection getConnection() throws SQLException {
                return con;
            }

            public Connection getConnection( String arg0, String arg1 ) throws SQLException {
                throw new UnsupportedOperationException( "Not supported yet." );
            }

            public PrintWriter getLogWriter() throws SQLException {
                throw new UnsupportedOperationException( "Not supported yet." );
            }

            public void setLogWriter( PrintWriter arg0 ) throws SQLException {
                throw new UnsupportedOperationException( "Not supported yet." );
            }

            public void setLoginTimeout( int arg0 ) throws SQLException {
                throw new UnsupportedOperationException( "Not supported yet." );
            }

            public int getLoginTimeout() throws SQLException {
                throw new UnsupportedOperationException( "Not supported yet." );
            }

            public <T> T unwrap( Class<T> arg0 ) throws SQLException {
                throw new UnsupportedOperationException( "Not supported yet." );
            }

            public boolean isWrapperFor( Class<?> arg0 ) throws SQLException {
                throw new UnsupportedOperationException( "Not supported yet." );
            }
        };
        platform.createTables( con, dbModel, false, true );
        DbInfo dbinfo = DbInfo.getDbInfo();
        dbinfo.setVersion( 11 );
        session.update( dbinfo );
        session.flush();
        tx.commit();

        
        // Load data
        // Insert the seed data to database
        platform = PlatformFactory.createNewPlatformInstance( "derby" );
        platform.getPlatformInfo().setDelimiterToken( "" );
        platform.setDataSource( ds );
        DataToDatabaseSink sink = new DataToDatabaseSink( platform, dbModel );
        DataReader reader = new DataReader();
        reader.setModel( dbModel );
        reader.setSink( sink );
        try {
            sink.start();
            reader.parse( new File( "testfiles", "migration_test_data_0.5.0.xml" ) );
            sink.end();
        } catch (SAXException ex) {
            log.error( "SAXException: " + ex.getMessage(), ex );
        } catch (IOException ex) {
            log.error( "IOException: " + ex.getMessage(), ex );
        }        
        initTestVolume( db );
        initTestExtVolume( db );
        session.close();
    }
    
    /**
     Initialize the defaultVolume of database that is being converted
     @param db
     @throws java.io.IOException
     */
    private void initTestVolume( PVDatabase db ) throws IOException {
        File voldir = File.createTempFile( "pv_conversion_testvol", "" );
        voldir.delete();
        voldir.mkdir();
        File d1 = new File( voldir, "2006" );
        File d2 = new File(  d1, "200605" );
        d2.mkdirs();
        File f1 = new File( "testfiles", "test1.jpg" );
        File df1 = new File( d2, "20060516_00002.jpg");
        FileUtils.copyFile( f1, df1);
        File df2 = new File( d2, "20060516_00003.jpg");
        FileUtils.copyFile( f1,df2 );
        PVDatabase.LegacyVolume lvol = 
                new PVDatabase.LegacyVolume( "defaultVolume", voldir.getAbsolutePath() );
        db.addLegacyVolume( lvol );
    }
    
    
    /**
     Initialize the external volume used by database being converted
     @param db
     @throws java.io.IOException
     */
    private void initTestExtVolume( PVDatabase db ) throws IOException {
        File voldir = File.createTempFile( "pv_conversion_extvol", "" );
        voldir.delete();
        voldir.mkdir();
        File d1 = new File( voldir, "testdir2" );
        d1.mkdirs();    
        File f1 = new File( "testfiles", "test4.jpg" );
        File df1 = new File( voldir, "test4.jpg");
        FileUtils.copyFile( f1, df1);
        File df2 = new File( voldir, "cropped_test4.jpg");
        FileUtils.copyFile( f1,df2 );
        PVDatabase.LegacyVolume lvol = 
                new PVDatabase.LegacyExtVolume( "extvol_pv_convert_test_volume", 
                voldir.getAbsolutePath(), 4 );
        db.addLegacyVolume( lvol );
    }
    
    /**
     Test that migration works OK.
     */
    @Test
    public void testMigrationToVersioned() {
        SchemaUpdateAction sua = new SchemaUpdateAction( PhotovaultSettings.getSettings().getCurrentDatabase() );
        sua.upgradeDatabase();
        
        // Verify that the photos are persisted correctly
        Session s = HibernateUtil.getSessionFactory().openSession();
        HibernateDAOFactory df = 
                (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        df.setSession( s );
        PhotoInfoDAO photoDao = df.getPhotoInfoDAO();
        
        PhotoInfo p1 = photoDao.findByUUID( 
                UUID.fromString( "639f492f-99b2-4d93-b18e-597324edc482") );
        OriginalImageDescriptor o1 = p1.getOriginal();
        assertEquals( 1536, o1.getWidth() );
        assertEquals( 2048, o1.getHeight() );
        assertEquals( "London", p1.getShootingPlace() );
        assertEquals( "Harri", p1.getPhotographer() );
        Set<FileLocation> locations = p1.getOriginal().getFile().getLocations();
        assertEquals( 1, locations.size() );
        FileLocation l = locations.iterator().next();
        assertEquals( "/test4.jpg", l.getFname() );
        
        FuzzyDate fd = p1.getFuzzyShootTime();
        assertEquals( 182.5, fd.getAccuracy(), 0.001 );
        
        ObjectHistory<PhotoInfo> h1 = p1.getHistory();
        Set<Change<PhotoInfo>> ch1 = h1.getChanges();
        assertEquals( 3, ch1.size() );
        assertNull( p1.getRawSettings() );
        
        Set<PhotoFolder> p1folders = p1.getFolders();
        assertEquals( 1, p1folders.size() );
        PhotoFolderDAO folderDao = df.getPhotoFolderDAO();
        PhotoFolder f1 = folderDao.findById( 
                UUID.fromString( "433404fe-ed6b-43a4-872d-286b23a6dfad"), false );
        assertTrue( p1folders.contains( f1 ) );

        /*
         Photo # 23 & #24 were actually the same image but with different original 
         hash due to changed EXIF data. As copies created from #24 are identical 
         to those created from #23, they should be associated with #23.
         */
        
        PhotoInfo p23 = photoDao.findByUUID( 
                UUID.fromString( "7115db43-12e8-43f2-a6ad-d66f8c039636" ) );
        PhotoInfo p24 = photoDao.findByUUID( 
                UUID.fromString( "e1a08867-1b6f-4d53-a22e-2744ab770914" ) );
        
        OriginalImageDescriptor p23orig = p23.getOriginal();
        boolean foundP23Thumb = false;
        for ( CopyImageDescriptor c : p23orig.getCopies() ) {
            if ( c.getFile().getId().equals( 
                    UUID.fromString( "fec3b45f-4acc-4978-9b00-8b2acb5268a1" ) ) ) {
                foundP23Thumb = true;
            }
        }
        assertTrue( foundP23Thumb );
        OriginalImageDescriptor p24orig = p24.getOriginal();
        assertEquals( 0, p24orig.getCopies().size() );
        
        
        // Photo with raw image
        PhotoInfo p2 = photoDao.findByUUID( 
                UUID.fromString( "e3f4b466-d1a3-48c1-ac86-01d9babf373f") );
        RawConversionSettings r2 = p2.getRawSettings();
        assertEquals( 31347, r2.getWhite() );
        assertEquals( 0.5, r2.getHighlightCompression() );
        
        OriginalImageDescriptor o2 = p2.getOriginal();
        CopyImageDescriptor t2 = 
                (CopyImageDescriptor) p2.getPreferredImage( 
                EnumSet.of( ImageOperations.RAW_CONVERSION ), 
                EnumSet.allOf( ImageOperations.class ), 
                66, 66, 200, 200 );
        
        assertEquals( r2, t2.getRawSettings() );
        boolean f = false;
        for ( CopyImageDescriptor c : o2.getCopies() ) {
            if ( c != t2 ) {
                assertEquals( 17847, c.getRawSettings().getWhite() );
                f = true;
            }
        }
        assertTrue( f );
        s.close();
    }


}

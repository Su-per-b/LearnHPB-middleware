/*
  Copyright (c) 2007 Harri Kaimio
  
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

package org.photovault.imginfo.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.photovault.common.PVDatabase;
import org.photovault.common.PhotovaultSettings;
import org.photovault.folder.PhotoFolder;
import org.photovault.image.ChannelMapOperation;
import org.photovault.image.ColorCurve;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoNotFoundException;
import org.photovault.imginfo.Volume;
import org.photovault.imginfo.VolumeManager;
import org.photovault.test.PhotovaultTestCase;

/**
 *
 * @author harri
 */
public class Test_XmlWriter  extends PhotovaultTestCase {

    /**
       Sets ut the test environment
    */
    public void setUp() {
        try {
            volumeRoot = File.createTempFile( "photovaultVolumeTest", "" );
            volumeRoot.delete();
            volumeRoot.mkdir();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            volume = new Volume();
            volume.setName( "testVolume" );
            VolumeManager.instance().initVolume( volume, volumeRoot );
        } catch ( Exception e ) {
            fail( e.getMessage() );
        }

        PhotovaultSettings settings = PhotovaultSettings.getSettings();
        PVDatabase curDb = settings.getCurrentDatabase();
        
//        try {
//            curDb.addVolume( extVol );
//        } catch (PhotovaultException ex) {
//            ex.printStackTrace();
//        }

    }
    private Volume volume;
    private PVDatabase db;

    private File volumeRoot;
    /**
       Tears down the testing environment
    */
    public void tearDown() {
//        db.removeVolume( volume );
//	deleteTree( volume.getBaseDir() );
    }

    static class TestExportListener implements XmlExportListener {
        public int status;
        public boolean error = false;
        public Set objects = new HashSet();
        
        public void xmlExportStatus(XmlExporter exporter, int status) {
            this.status = status;
        }

        public void xmlExportError(XmlExporter exporter, String message) {
            error = true;
        }

        public void xmlExportObjectExported(XmlExporter exporter, Object obj) {
            objects.add( obj );
        }
    }
    
    public void testFolderWriting() throws IOException {
        PhotoFolder root = PhotoFolder.getRoot();
        PhotoFolder subfolder1 = new PhotoFolder();
        subfolder1.setName( "Folder 1" );
        subfolder1.setDescription( "Description 1\nanother line" );
        subfolder1.reparentFolder( root );
        PhotoFolder subfolder2 = new PhotoFolder();
        subfolder2.setName( "Folder 2" );
        subfolder2.setDescription( "Description 2\nanother line" );
        subfolder2.reparentFolder( root );
        PhotoFolder subfolder3 = new PhotoFolder();
        subfolder3.setName( "Folder 3" );
        subfolder3.setDescription( "Description 3\nanother line" );
        subfolder3.reparentFolder( subfolder2 );
        File outfile = File.createTempFile( "pv_export_test", ".xml" );
        FileWriter fw = new FileWriter( outfile );
        BufferedWriter writer = new BufferedWriter( fw );
        XmlExporter exporter = new XmlExporter( writer );
        TestExportListener l = new TestExportListener();
        exporter.addListener( l );
        exporter.write();
        writer.close();
        assertEquals( XmlExporter.EXPORTER_STATE_COMPLETED, l.status );
        assertTrue( l.objects.contains( subfolder3 ) );
        assertFalse( l.error );        
    }
    
    static class TestImportListener implements XmlImportListener {
        public int status;
        public boolean error = false;
        public Set objects = new HashSet();
        
        public void xmlImportStatus(XmlImporter importer, int status) {
            this.status = status;
        }

        public void xmlImportError(XmlImporter importer, String message) {
            error = true;
        }

        public void xmlImportObjectImported(XmlImporter importer, Object obj) {
            objects.add( obj );
        }
    }
    
    
    public void testImport() throws IOException, PhotoNotFoundException {
        File f = new File( "testfiles/test_import.xml" );
        BufferedReader reader = new BufferedReader( new FileReader( f ) );
        XmlImporter importer = new XmlImporter( reader );
        TestImportListener l = new TestImportListener();
        importer.addListener( l );
        importer.importData();
        reader.close();

        assertFalse( l.error );
        assertEquals( XmlImporter.IMPORTING_COMPLETED, l.status );
        // PhotoInfo p = PhotoInfo.retrievePhotoInfo( UUID.fromString( "65bd68f7-79f4-463b-9e37-0a91182e6499") );
        PhotoInfo p = PhotoInfo.create();
        assertEquals( "NIKON D200", p.getCamera() );
        assertEquals( 8.0, p.getFStop() );
        assertEquals( "Digital", p.getFilm() );
        assertEquals( 100, p.getFilmSpeed() );
        assertEquals( 0, p.getQuality() );
        ChannelMapOperation cm = p.getColorChannelMapping();
        ColorCurve c = cm.getChannelCurve( "value" );
        assertEquals( 0.4, c.getY( 1 ) );
        assertEquals( 0.5, c.getX( 1 ) );
        boolean foundOrig = false;
/*        for ( ImageInstance i : p.getInstances() ) {
            if ( i.getInstanceType() == ImageInstance.INSTANCE_TYPE_ORIGINAL ) {
                cm = i.getColorChannelMapping();
                c = cm.getChannelCurve( "value" );
                assertEquals( 0.2, c.getY( 1 ) );
                assertEquals( 0.6, c.getX( 1 ) );                
                foundOrig = true;
            }
        }
        assertTrue( foundOrig );
*/        assertTrue( l.objects.contains( p ) );
        PhotoFolder folder = PhotoFolder.getFolderByUUID( UUID.fromString( "06499cc6-d421-4262-8fa2-30a060982619" ) );
        assertEquals( "test", folder.getName() );
        PhotoFolder parent = folder.getParentFolder();
        assertEquals( "extvol_deletetest", parent.getName() );
        boolean found = false;
        for ( int n = 0; n < folder.getPhotoCount(); n++ ) {
            if ( folder.getPhoto( n ) == p ) {
                found = true;
                break;
            }
        }
        assertTrue( found );
    }
    
    protected boolean deleteTree( File root ) {
	boolean success = true;
	if ( root.isDirectory() ) {
	    File entries[] = root.listFiles();
	    for ( int n = 0; n < entries.length; n++ ) {
		if ( !deleteTree( entries[n] ) ) {
		    success = false;
		}
	    }
	}
	if ( !root.delete() ) {
	    success = false;
	}
	return success;
    }

	
    public static Test suite() {

	return new TestSuite( Test_XmlWriter.class );
    }
    
    
    public static void main( String[] args ) {
	//	org.apache.log4j.BasicConfigurator.configure();
	// log.setLevel( org.apache.log4j.Level.DEBUG );
	org.apache.log4j.Logger instLog = org.apache.log4j.Logger.getLogger( Test_XmlWriter.class.getName() );
	instLog.setLevel( org.apache.log4j.Level.DEBUG );
	junit.textui.TestRunner.run( suite() );
    }
    

}

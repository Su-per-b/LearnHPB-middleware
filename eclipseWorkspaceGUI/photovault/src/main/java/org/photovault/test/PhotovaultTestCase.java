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

package org.photovault.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.photovault.common.JUnitHibernateManager;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.folder.PhotoFolder;
import org.photovault.imginfo.PhotoInfo;

/**
 * This class extends junit TestCase class so that it sets up the OJB environment
 * 
 * @author Harri Kaimio
 */
public class PhotovaultTestCase extends TestCase {
    
    /** Creates a new instance of PhotovaultTestCase */
    public PhotovaultTestCase() {
        JUnitHibernateManager.getHibernateManager();
    }

    /**
     *       Sets ut the test environment
     */
    public void setUp() {
    }

    /**
     Utility to check that the object in memory matches the database. Checks
     that a record with same id exists, that all fields match and that the 
     collections (instances & folders) are saved correctly
     @param p The photo to verify
     @param session Hibernate persistence context used to query the database
     */
    public static void assertMatchesDb( PhotoInfo p, Session session ) {
        String sql = "select * from pv_photos where photo_uuid = '" + p.getUuid() + "'";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = session.connection().createStatement();
            rs = stmt.executeQuery( sql );
            if ( !rs.next() ) {
                fail( "record not found" );
            }
            // TODO: there is no pointer back from instance to photo so this cannot be checked
            //	    assertEquals( "photo doesn't match", i.getPhoto .getUid(), rs.getInt( "photo_id" ) );
            assertEquals( p.getCamera(), rs.getString( "camera" ) );
            assertEquals( p.getDescription(), rs.getString( "description" ) );
            assertEquals( p.getFStop(), rs.getDouble( "f_stop" ) );
            assertEquals( p.getFilm(), rs.getString( "film" ) );
            assertEquals( p.getFilmSpeed(), rs.getInt( "film_speed") );
            assertEquals( p.getFocalLength(), rs.getDouble( "focal_length" ) );
            assertEquals( p.getLens(), rs.getString( "lens" ) );
            assertEquals( p.getOrigFname(), rs.getString( "orig_fname" ) );
            // assertEquals( p.getOrigInstanceHash(), rs.getByte( "hash") );
            assertEquals( p.getPhotographer(), rs.getString( "photographer" ) );
            assertEquals( p.getPrefRotation(), rs.getDouble( "pref_rotation" ) );
            assertEquals( p.getQuality(), rs.getInt( "photo_quality" ) );
            assertEquals( p.getShootTime(), rs.getTimestamp( "shoot_time" ) );
            assertEquals( p.getShootingPlace(), rs.getString( "shooting_place" ) );
            assertEquals( p.getShutterSpeed(), rs.getDouble( "shutter_speed" ) );
            assertEquals( p.getTechNotes(), rs.getString( "tech_notes" ) );
            assertEquals( p.getTimeAccuracy(), rs.getDouble( "time_accuracy" ) );
            RawConversionSettings s = p.getRawSettings();
            if ( s != null ) {
                assertEquals( s.getBlack(), rs.getInt( "raw_blackpoint") );
                assertEquals( s.getWhite(), rs.getInt("raw_whitepoint"));
                assertEquals( s.getEvCorr(), rs.getDouble("raw_ev_corr" ) );
                assertEquals( s.getHighlightCompression(), rs.getDouble("raw_hlight_corr"));
                assertEquals( s.getWhiteBalanceType(), rs.getInt("raw_wb_type" ) );
                assertEquals( s.getRedGreenRatio(), rs.getDouble("raw_r_g_ratio" ) );
                assertEquals( s.getDaylightRedGreenRatio(), rs.getDouble("raw_dl_r_g_ratio" ) );
                assertEquals( s.getBlueGreenRatio(), rs.getDouble("raw_b_g_ratio" ) );
                assertEquals( s.getDaylightBlueGreenRatio(), rs.getDouble("raw_dl_b_g_ratio" ) );
            }
//            assertEquals( p.getOriginal().getId(), rs.getLong( "original_id" ) );
//            assertTrue( "Photo not correct", p.getUid() == rs.getInt( "photo_id" ) );
            
        } catch ( SQLException e ) {
            fail( e.getMessage() );
        } finally {
            if ( rs != null ) {
                try {
                    rs.close();
                } catch ( Exception e ) {
                    fail( e.getMessage() );
                }
            }
            if ( stmt != null ) {
                try {
                    stmt.close();
                } catch ( Exception e ) {
                    fail( e.getMessage() );
                }
            }
        }        
    }
    
    public static void assertMatchesDb( PhotoFolder folder, Session session ) {
	UUID id = folder.getUuid();
	String sql = "select * from pv_folders where folder_uuid = '" + id + "'";
	Statement stmt = null;
	ResultSet rs = null;
	try {
	    stmt = session.connection().createStatement();
	    rs = stmt.executeQuery( sql );
	    if ( !rs.next() ) {
		fail( "record not found" );
	    }
	    assertEquals( "name doesn't match", folder.getName(), rs.getString( "collection_name" ) );
	    assertEquals( "description doesn't match", folder.getDescription(), rs.getString( "collection_desc" ) );
            String parentIdStr =  rs.getString( "parent_uuid" );
            if ( rs.wasNull() ) {
                assertNull( folder.getParentFolder() );
            } else {
                UUID parentId = UUID.fromString( parentIdStr );
                assertEquals( parentId, folder.getParentFolder().getUuid() );
            }
            rs.close();
            
            // Check that subfolders collection matches database
            rs = stmt.executeQuery( "select * from pv_folders where parent_uuid = '" + id + "'" );
            Set<UUID> folderIds = new HashSet<UUID>();
            for ( PhotoFolder f : folder.getSubfolders() ) {
                folderIds.add( f.getUuid() );
            }
            while ( rs.next() ) {
                UUID subId = UUID.fromString( rs.getString( "folder_uuid" ) );
                assertTrue( "folder " + subId + " not in memory copy", 
                        folderIds.remove( subId ) );
                
            }            
            assertTrue( folderIds.size() == 0 );
            rs.close();
            
            // Check that photos collection matches database
            rs = stmt.executeQuery( "select * from pv_folder_photos where folder_uuid = '" + id +"'" );
            Set<UUID> photoIds = new HashSet<UUID>();
            for ( PhotoInfo p : folder.getPhotos() ) {
                photoIds.add( p.getUuid() );
            }
            while ( rs.next() ) {
                UUID photoId = UUID.fromString( rs.getString( "photo_uuid" ) );
                assertTrue( "photo " + photoId + " not in memory copy", 
                        photoIds.remove( photoId ) );                
            }            
            assertTrue( photoIds.size() == 0 );
	} catch ( SQLException e ) {
	    fail( e.getMessage() );
	} finally {
	    if ( rs != null ) {
		try {
		    rs.close();
		} catch ( Exception e ) {
		    fail( e.getMessage() );
		}
	    }
	    if ( stmt != null ) {
		try {
		    stmt.close();
		} catch ( Exception e ) {
		    fail( e.getMessage() );
		}
	    }
	}
    }
	    
}

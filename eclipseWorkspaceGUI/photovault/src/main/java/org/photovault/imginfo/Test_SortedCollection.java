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

import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.hibernate.Session;
import org.photovault.folder.PhotoFolder;
import org.photovault.folder.PhotoFolderDAO;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.test.PhotovaultTestCase;

/**
 * Test cases for testing @see SortedPhotoCollection
 * @author harri
 */
public class Test_SortedCollection extends PhotovaultTestCase {
    
    /** Creates a new instance of Test_SortedCollection */
    public Test_SortedCollection() {
    }

    DAOFactory daoFactory;
    PhotoInfoDAO photoDAO = null;
    PhotoFolderDAO folderDAO = null;
    Session session = null;

    PhotoFolder folder = null;
    PhotoInfo photo1 = null;
    PhotoInfo photo2 = null;
    PhotoInfo photo3 = null;
    
    public void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        HibernateDAOFactory hdf = (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        hdf.setSession( session );
        daoFactory = hdf;
        photoDAO = daoFactory.getPhotoInfoDAO();
        folderDAO = daoFactory.getPhotoFolderDAO();

        folder = folderDAO.create( "SortedCollectionTest", null );
        photo1 = photoDAO.makePersistent( PhotoInfo.create() );
        photo2 = photoDAO.makePersistent( PhotoInfo.create() );
        photo3 = photoDAO.makePersistent( PhotoInfo.create() );
        
        photo1.setShootTime( new Date( 2000, 1, 1 ));
        photo1.setShootingPlace( "TESTPLACE B" );
        photo2.setShootTime( null );
        photo2.setShootingPlace( "TESTPLACE A" );
        photo3.setShootTime( new Date( 2001, 1, 1 ));
        photo3.setShootingPlace( "TESTPLACE B" );
        folder.addPhoto( photo1 );
        folder.addPhoto( photo2 );
        folder.addPhoto( photo3 );
    }
    
    public void tearDown() {
        folder.delete();
        folderDAO.makeTransient( folder );
        photo1.delete();
        photoDAO.makeTransient( photo1 );
        photo2.delete();
        photoDAO.makeTransient( photo2 );
        photo3.delete();
        photoDAO.makeTransient( photo3 );
    }
    
    public void testSorting() {
        SortedPhotoCollection collection = new SortedPhotoCollection( folder );
        TestChangeListener l = new TestChangeListener();
        collection.addPhotoCollectionChangeListener( l );
        collection.setComparator( new ShootingDateComparator() );
        
        // Change listeners must be notified
        assertTrue( "Change listener not called after comparator change", l.isNotified );
        assertTrue( "Photo1 must be 1st in collection", collection.getPhoto( 0 ) == photo1 );
        assertTrue( "Photo2 must be 3nd in collection", 
                (collection.getPhoto( 2 ) == photo2) );
        
        l.isNotified = false;
        collection.setComparator( new ShootingPlaceComparator() );
        // Change listeners must be notified
        assertTrue( "Change listener not called after comparator change", l.isNotified );
        assertTrue( "Photo2 must be 1st in collection", collection.getPhoto( 0 ) == photo2 );
        assertTrue( "Photo1 must be 2nd in collection", 
                (collection.getPhoto( 2 ) == photo1) || (collection.getPhoto( 2 ) == photo3) );
        
    }
    /**
     * Simple test listener
     */
    private class TestChangeListener implements PhotoCollectionChangeListener {
        public boolean isNotified = false;
        public void photoCollectionChanged( PhotoCollectionChangeEvent ev ) {
            isNotified = true;
        }
    }
    
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( Test_SortedCollection.class.getName() );

    public static void main( String[] args ) {
	//	org.apache.log4j.BasicConfigurator.configure();
	log.setLevel( org.apache.log4j.Level.DEBUG );
	org.apache.log4j.Logger photoLog = org.apache.log4j.Logger.getLogger( PhotoInfo.class.getName() );
	photoLog.setLevel( org.apache.log4j.Level.DEBUG );
	junit.textui.TestRunner.run( suite() );
    }
    public static Test suite() {
	return new TestSuite( Test_SortedCollection.class );
    }
}

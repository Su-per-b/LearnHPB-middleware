/*
  Copyright (c) 2006-2007 Harri Kaimio
  
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

import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.photovault.folder.*;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.HibernateDtoResolverFactory;
import org.photovault.replication.VersionedObjectEditor;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Test_PhotoQuery extends PhotovaultTestCase {

    static Log log = LogFactory.getLog( Test_PhotoQuery.class.getName() );

    Vector<PhotoInfo> photos = null;
    Vector uids = null;
    PhotoFolder folder = null;
    PhotoFolder subfolder = null;
    DAOFactory daoFactory;
    PhotoInfoDAO photoDAO = null;
    PhotoFolderDAO folderDAO = null;
    Session session = null;
    Transaction tx = null;
    
    @BeforeClass
    @Override
    public void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        HibernateDAOFactory hdf = (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        hdf.setSession( session );
        daoFactory = hdf;
        photoDAO = daoFactory.getPhotoInfoDAO();
        folderDAO = daoFactory.getPhotoFolderDAO();
        tx = session.beginTransaction();
        
	// Create several photos with different shooting dates
	// Add them to a collection so that they are easy to delete afterwards
	Calendar cal = Calendar.getInstance();
        cal.clear();
	photos = new Vector();
	uids = new Vector();
	cal.set( 2002, 11, 23 );
	makePhoto( cal, 1, "Katsokaa kun Lassi ui" );
	cal.set( 2002, 11, 24 );
	makePhoto( cal, 1, "Lassille kuuluu hyvaa" );
	makePhoto( cal, 2, "" );
	cal.set( 2002, 11, 25 );
	makePhoto( cal, 1, "" );

        PhotoFolderDAO folderDAO = daoFactory.getPhotoFolderDAO();
        folder = folderDAO.create( "QueryTest", null );
        folder.reparentFolder( folderDAO.findRootFolder() );
        subfolder = folderDAO.create( "QueryTest subfolder", folder );
        FolderPhotoAssocDAO assocDao = daoFactory.getFolderPhotoAssocDAO();
        FolderPhotoAssociation assoc1 = assocDao.getAssociation( folder, photos.get(0) );
        folder.addPhotoAssociation( assoc1 );
        photos.get(0).addFolderAssociation( assoc1 );

        FolderPhotoAssociation assoc2 = assocDao.getAssociation( subfolder, photos.get(3) );
        subfolder.addPhotoAssociation( assoc2 );
        photos.get(3).addFolderAssociation( assoc2 );
        
        FolderPhotoAssociation assoc3 = assocDao.getAssociation( folder, photos.get(2) );
        folder.addPhotoAssociation( assoc3 );
        photos.get(2).addFolderAssociation( assoc3 );
        session.flush();
    }

    @AfterClass
    @Override
    public void tearDown() {
	for ( Object o : photos ) {
            PhotoInfo photo = (PhotoInfo) o;
            photo.delete();
            photoDAO.makeTransient( photo );
	}
        folder.delete();
        subfolder.delete();
	folderDAO.makeTransient( folder );	
        folderDAO.makeTransient( subfolder );
        tx.commit();
    }
    
    
    PhotoInfo makePhoto( Calendar cal, double accuracy, String desc ) {
	PhotoInfo photo = photoDAO.create();
        DTOResolverFactory rf = new HibernateDtoResolverFactory( session );
        VersionedObjectEditor<PhotoInfo> pe = 
                new VersionedObjectEditor<PhotoInfo>( photo, rf );
        PhotoEditor pep = (PhotoEditor) pe.getProxy();
        pep.setFuzzyShootTime( new FuzzyDate(  cal.getTime(), accuracy ) );
	pep.setDescription( desc );
        pe.apply();
	photos.add( photo );
	uids.add( photo.getUuid() );
	return photo;
    }
    
    @Test
    public void testUpperUnboundedRange() {
	PhotoQuery q = new PhotoQuery();
	Calendar cal = Calendar.getInstance();
        cal.clear();
	cal.set( 2002, 11, 24 );
	// First the case in which there is only lower bound
	q.setFieldCriteriaRange( PhotoQuery.FIELD_SHOOTING_TIME, cal.getTime(), null );
	
	boolean[] expected1 = { false, true, true, true };
	checkResults( q, expected1 );
    }
    
    @Test
    public void testBoundedRange() {
	PhotoQuery q = new PhotoQuery();
	Calendar cal = Calendar.getInstance();
        cal.clear();
	cal.set( 2002, 11, 24 );
	// First the case in which there is only lower bound
	q.setFieldCriteriaRange( PhotoQuery.FIELD_SHOOTING_TIME, cal.getTime(), cal.getTime() );
	boolean[] expected2 = { false, true, true, false };
	checkResults( q, expected2 );
    }

    @Test
    public void testFuzzyDateProbable() {
	PhotoQuery q = new PhotoQuery();

	// First, check cases where the midpoint belongs to the fuzziness range
 	Calendar cal = Calendar.getInstance();
        cal.clear();
	cal.set( 2002, 11, 24 );
	FuzzyDate fd1 = new FuzzyDate( cal.getTime(), 1.0 );
	// First the case in which there is only lower bound
 	q.setFuzzyDateCriteria( PhotoQuery.FIELD_SHOOTING_TIME,
				PhotoQuery.FIELD_SHOOTING_TIME_ACCURACY,
				fd1, QueryFuzzyTimeCriteria.INCLUDE_PROBABLE );
	// Image 2 should not be included since its fuzzy range is
	// larger that search range
	boolean[] expected1 = { true, true, false, true };
	checkResults( q, expected1 );
    }
    
    @Test
    public void testFuzzyDatePossible() {
	PhotoQuery q = new PhotoQuery();

	// First, check cases where the midpoint belongs to the fuzziness range
 	Calendar cal = Calendar.getInstance();
        cal.clear();
	cal.set( 2002, 11, 24 );
	FuzzyDate fd1 = new FuzzyDate( cal.getTime(), 1.0 );
 	q.setFuzzyDateCriteria( PhotoQuery.FIELD_SHOOTING_TIME,
				PhotoQuery.FIELD_SHOOTING_TIME_ACCURACY,
				fd1, QueryFuzzyTimeCriteria.INCLUDE_POSSIBLE );
	// All expected to be included with INCLUDE_POSSIBLE
	boolean[] expected2 = { true, true, true, true };
	checkResults( q, expected2 );
    }

    @Test
    public void testFuzzyDateCertain() {
	PhotoQuery q = new PhotoQuery();

	// First, check cases where the midpoint belongs to the fuzziness range
 	Calendar cal = Calendar.getInstance();
        cal.clear();
	cal.set( 2002, 11, 24 );
	FuzzyDate fd1 = new FuzzyDate( cal.getTime(), 1.0 );
	q.setFuzzyDateCriteria( PhotoQuery.FIELD_SHOOTING_TIME,
				PhotoQuery.FIELD_SHOOTING_TIME_ACCURACY,
				fd1, QueryFuzzyTimeCriteria.INCLUDE_CERTAIN );
	// All expected to be included with INCLUDE_POSSIBLE
	boolean[] expected3 = { false, true, false, false };
	checkResults( q, expected3 );
	

    }

    @Test
    public void testLowerUnboundedRange() {
	PhotoQuery q = new PhotoQuery();

        Calendar cal = Calendar.getInstance();
        cal.clear();
	cal.set( 2002, 11, 24 );
	// First the case in which there is only lower bound
	q.setFieldCriteriaRange( PhotoQuery.FIELD_SHOOTING_TIME, null, cal.getTime() );
	boolean[] expected3 = { true, true, true, false };
	checkResults( q, expected3 );
    }

    /*
    public void testFulltext() {
	PhotoQuery q = new PhotoQuery();
	q.setFulltextCriteria( "Lassi" );
	boolean[] expected3 = { true, true, false, false };
	checkResults( q, expected3 );
    }
    */
    
    @Test
    public void testLike() {
	PhotoQuery q = new PhotoQuery();

        q.setLikeCriteria( PhotoQuery.FIELD_DESCRIPTION, "%Lassi%" );
	boolean[] expected3 = { true, true, false, false };
	checkResults( q, expected3 );
    }

    @Test
    public void testFolderLimit() {
	PhotoQuery q = new PhotoQuery();

        q.setLikeCriteria( PhotoQuery.FIELD_DESCRIPTION, "%Lassi%" );
	q.limitToFolder( folder );
	boolean[] expected3 = { true, false, false, false };
	checkResults( q, expected3 );
    }
    
    @Test
    public void testFolderLimitSubfolders() {
	PhotoQuery q = new PhotoQuery();

        q.limitToFolder( folder );
	boolean[] expected3 = { true, false, true, true };
	checkResults( q, expected3 );
    }
    
    
    /**
       This query checks that query can be modified and that the results are shown correctly
       Tjis is originally implemented to find demonstrate a defect in which the reuslt set was not cleaned
       before the new query.
    */
    @Test
    public void testQueryModification() {
	PhotoQuery q = new PhotoQuery();

        Calendar cal = Calendar.getInstance();
        cal.clear();
	cal.set( 2002, 11, 24 );
	// First the case in which there is only lower bound
	q.setFieldCriteriaRange( PhotoQuery.FIELD_SHOOTING_TIME, null, cal.getTime() );
	boolean[] expected3 = { true, true, true, false };
	checkResults( q, expected3 );
	// Then add the lower bound, part of the photos should not be in result set this time
	q.setFieldCriteriaRange( PhotoQuery.FIELD_SHOOTING_TIME, cal.getTime(), cal.getTime() );
	boolean[] expected2 = { false, true, true, false };
	checkResults( q, expected2 );
    }

    class TestListener implements PhotoCollectionChangeListener {
	public boolean notified = false;
	public PhotoCollection changedObject = null;
	public void photoCollectionChanged( PhotoCollectionChangeEvent e ) {
	    notified = true;
	    changedObject = (PhotoCollection) e.getSource();
	}
    }
    
    @Test
    public void testNotification() {
	PhotoQuery q = new PhotoQuery();

        TestListener l1 = new TestListener();
	q.addPhotoCollectionChangeListener( l1 );
	Calendar cal = Calendar.getInstance();
	cal.set( 2002, 11, 24 );
	// First the case in which there is only lower bound
	q.setFieldCriteriaRange( PhotoQuery.FIELD_SHOOTING_TIME, null, cal.getTime() );
	assertTrue( "setEndDate should notify listeners", l1.notified );
	assertEquals( "source not correct", q, l1.changedObject );
	l1.notified = false;

	q.setFieldCriteriaRange( PhotoQuery.FIELD_SHOOTING_TIME, cal.getTime(), cal.getTime() );
	assertTrue( "setstartDate should notify listeners", l1.notified );
	l1.notified = false;

	TestListener l2 = new TestListener();
	q.addPhotoCollectionChangeListener( l2 );
	q.removePhotoCollectionChangeListener( l1 );
	q.setFieldCriteriaRange( PhotoQuery.FIELD_SHOOTING_TIME, cal.getTime(), cal.getTime() );
	assertTrue( "setstartDate should notify listeners", l2.notified );
	assertFalse( "Removed listener notified", l1.notified );
    }

    void checkResults( PhotoQuery q, boolean[] expected ) {
	log.debug( "Checking results" );
        List<PhotoInfo> result = q.queryPhotos( session );
	for( PhotoInfo photo : result ) {
	    int m = uids.indexOf( photo.getUuid() );
	    log.debug( "Getting photo " + photo.getUuid() + " " + photo.getShootTime() + " " + m );
	    if ( m >= 0 ) {
		if ( expected[m] ) {
		    expected[m] = false;
		    log.debug( "Photo " + photo.getUuid() + " found" );
		} else {
		    fail( "Photo dated " + photo.getShootTime().toString() + " not expected!!!" );
		}
	    }
	}
	// Check that all photos were found
	log.debug( "Checking that all are found" );
	for ( int n = 0; n < expected.length; n++ ) {
	    if ( expected[n] ) {
                PhotoInfo photo = (PhotoInfo)photos.elementAt( n );
                FuzzyDate d = new FuzzyDate( photo.getShootTime(), photo.getTimeAccuracy() );
		fail( "Photo "+ n + " (id" + photo.getUuid() + ", dated " + d.format() + ") not included in result set" );
	    }
	}
    }
}
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

package org.photovault.swingui;

import java.io.*;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.photovault.imginfo.*;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoNotFoundException;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.Change;
import org.photovault.replication.VersionedClassDesc;
import org.photovault.replication.VersionedObjectEditor;
import org.photovault.swingui.selection.PhotoSelectionController;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Test_PhotoInfoController extends PhotovaultTestCase {
    
    static Log log = LogFactory.getLog( Test_PhotoInfoController.class.getName() );

    PhotoInfo photo = null;
    PhotoSelectionController ctrl = null;
    String testImgDir = "testfiles";
    private Session session;
    private HibernateDAOFactory daoFactory;
    private PhotoInfoDAO photoDAO;
    private Transaction tx;
  
    @BeforeMethod
    @Override
    public void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        HibernateDAOFactory hdf = (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        hdf.setSession( session );
        daoFactory = hdf;
        photoDAO = daoFactory.getPhotoInfoDAO();
        tx = session.beginTransaction();
        
	photo = PhotoInfo.create();
        photoDAO.makePersistent( photo );
        
        VersionedObjectEditor<PhotoInfo> e = 
                new VersionedObjectEditor<PhotoInfo>(  
                photo, daoFactory.getDTOResolverFactory() );
           
        Change<PhotoInfo> ch = e.apply();
        e = new VersionedObjectEditor<PhotoInfo>(
                photo, daoFactory.getDTOResolverFactory() );
        PhotoEditor pe = (PhotoEditor) e.getProxy();
        pe.setPhotographer( "TESTIKUVAAJA" );
        pe.setFStop( 5.6 );
        e.apply();
        
        tx.commit();
        tx = session.beginTransaction();

	ctrl = new PhotoSelectionController( null );
    }

    @AfterMethod
    @Override
    public void tearDown() {
	photo.delete();
        tx.commit();
        session.close();        
    }
    
    
    @Test
    public void testPhotoModification() {
	ctrl.setPhoto( photo );

	String oldValue = photo.getPhotographer();
	String newValue = "Test photographer 2";
        
	ctrl.viewChanged( null, PhotoInfoFields.PHOTOGRAPHER, newValue );
	assertEquals( "PhotoInfo should not be modified at this stage", oldValue, photo.getPhotographer() );
	assertTrue( "Ctrl should reflect the modification", ctrl.getFieldValues( PhotoInfoFields.PHOTOGRAPHER ).contains( newValue ));

	try {
	    ctrl.save();
	} catch ( Exception e ) {
	    fail( "Exception while saving: " + e.getMessage() );
	}
        assertEquals( "After save photo should also reflect the modifications", newValue, photo.getPhotographer() );

        // Check that the value is also stored in DB
        PhotoInfo photo2 = photoDAO.findByUUID( photo.getUuid() );

        assertEquals( photo2.getPhotographer(), photo.getPhotographer() );
        assertTrue( photo2.getFStop() == photo.getFStop() );
    }

    @Test
    public void testChangeDiscarding() {
	ctrl.setPhoto( photo );

	String oldValue = photo.getPhotographer();
	String newValue = "Test photographer 2";
	ctrl.viewChanged( null, PhotoInfoFields.PHOTOGRAPHER, newValue );

	ctrl.discard();
	assertEquals( "PhotoInfo should not be modified", oldValue, photo.getPhotographer() );
        Set fieldValues = ctrl.getFieldValues( PhotoInfoFields.PHOTOGRAPHER );
        assertEquals( 1, fieldValues.size() );
	assertTrue( "Ctrl should have the old value after discard", fieldValues.contains( oldValue ) );
    }


    /**
       Tests creation of PhotoInfo record without giving image file
    */
    @Test
    public void testOnlyRecordCreation() {
	String photographer = "Test photographer";
	ctrl.viewChanged( null, PhotoInfoFields.PHOTOGRAPHER, photographer );
	assertTrue( ctrl.getFieldValues( PhotoInfoFields.PHOTOGRAPHER ).contains(  photographer ) );

	// Saving the ctrl state should create a new photo object
	try {
	    ctrl.save();
	} catch ( Exception e ) {
	    fail( "Exception while saving: " + e.getMessage() );
	}
	PhotoInfo photo = ctrl.getPhoto();
	assertTrue( "getPhoto should return PhotoInfo object after save()", photo != null );
	assertEquals( "PhotoInfo fields should match ctrl",
		      photographer, photo.getPhotographer() );
	

        // Check that the value is also stored in DB
        PhotoInfo photo2 = photoDAO.findByUUID( photo.getUuid() );
        assertNotNull( photo2 );
        photo.delete();
	
    }
	

}

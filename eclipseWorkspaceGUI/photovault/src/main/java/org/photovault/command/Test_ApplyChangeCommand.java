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


package org.photovault.command;

import java.util.UUID;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoDAO;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.Change;
import org.photovault.replication.ChangeDTO;
import org.photovault.replication.VersionedObjectEditor;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 * Thes cases for {@link ApplyChangeCommand}
 * @author Harri Kaimio
 */
public class Test_ApplyChangeCommand extends PhotovaultTestCase {
    private Session session;
    private HibernateDAOFactory daoFactory;
    private PhotoInfoDAO photoDAO;
    private Transaction tx;



    /**
     Sets ut the test environment
     */
    @BeforeMethod
    public void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        HibernateDAOFactory hdf = (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        hdf.setSession( session );
        daoFactory = hdf;
        photoDAO = daoFactory.getPhotoInfoDAO();
    }

    /**
     Tears down the testing environment
     */
    @AfterMethod
    public void tearDown() {
        session.close();
    }

    @Test
    public void testApplyChange() throws InstantiationException, IllegalAccessException, CommandException {
        UUID photoUuid = UUID.randomUUID();
        tx = session.beginTransaction();
        VersionedObjectEditor<PhotoInfo> e =
                new VersionedObjectEditor( PhotoInfo.class, photoUuid,
                daoFactory.getDTOResolverFactory() );
        PhotoInfo p = e.getTarget();
        e.apply();
        photoDAO.makePersistent( p );
        session.flush();
        tx.commit();
        e = new VersionedObjectEditor<PhotoInfo>(  p, daoFactory.getDTOResolverFactory() );
        e.setField( "photographer", "test" );
        Change<PhotoInfo> c = e.getChange();
        ChangeDTO dto = new ChangeDTO( c );
        CommandHandler cmdHandler = new PhotovaultCommandHandler(  session );
        ApplyChangeCommand cmd = new ApplyChangeCommand( dto );
        cmdHandler.executeCommand( cmd );
        assertEquals( "test", p.getPhotographer() );
    }

    @Test
    public void testMergeChanges() throws CommandException, InstantiationException, IllegalAccessException {
        UUID photoUuid = UUID.randomUUID();
        tx = session.beginTransaction();
        VersionedObjectEditor<PhotoInfo> e =
                new VersionedObjectEditor( PhotoInfo.class, photoUuid,
                daoFactory.getDTOResolverFactory() );
        PhotoInfo p = e.getTarget();
        e.apply();
        photoDAO.makePersistent( p );
        session.flush();
        tx.commit();
        e = new VersionedObjectEditor<PhotoInfo>(  p, daoFactory.getDTOResolverFactory() );
        e.setField( "photographer", "test" );
        Change<PhotoInfo> c = e.getChange();
        ChangeDTO dto1 = new ChangeDTO( c );
        e = new VersionedObjectEditor<PhotoInfo>(  p, daoFactory.getDTOResolverFactory() );
        e.setField( "shootingPlace", "Rovaniemi" );
        c = e.getChange();
        ChangeDTO dto2 = new ChangeDTO( c );
        e = new VersionedObjectEditor<PhotoInfo>(  p, daoFactory.getDTOResolverFactory() );
        e.setField( "FStop", 5.6 );
        c = e.getChange();
        ChangeDTO dto3 = new ChangeDTO( c );

        CommandHandler cmdHandler = new PhotovaultCommandHandler(  session );
        ApplyChangeCommand cmd = new ApplyChangeCommand( dto1 );
        cmd.addChange( dto2 );
        cmd.addChange( dto3 );
        cmdHandler.executeCommand( cmd );
        assertEquals( "test", p.getPhotographer() );
        assertEquals( "Rovaniemi", p.getShootingPlace() );
        assertEquals( 5.6, p.getFStop() );
        Change<PhotoInfo> tip = p.getHistory().getVersion();
        assertEquals( 2, tip.getParentChanges().size() );
        assertEquals( 1, p.getHistory().getHeads().size() );
    }

}

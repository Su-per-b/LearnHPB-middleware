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

package org.photovault.swingui;

import abbot.tester.ComponentTester;
import java.awt.FlowLayout;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.photovault.command.CommandException;
import org.photovault.command.CommandExecutedEvent;
import org.photovault.command.CommandListener;
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.folder.CreatePhotoFolderCommand;
import org.photovault.folder.PhotoFolder;
import org.photovault.folder.PhotoFolderDAO;
import org.photovault.imginfo.ChangePhotoInfoCommand;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoDAO;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author harri
 */
public class Test_PhotoViewController extends PhotovaultTestCase {
    private PhotovaultCommandHandler commandHandler;
    private PhotoFolder folder;
    private PhotoInfo photo1;
    private PhotoInfo photo2;
    private Transaction tx;
    private PhotoInfoDAO photoDAO;
    private HibernateDAOFactory daoFactory;
    private Session session;
    private ComponentTester tester;
    private JPanel pane;
    private JFrame frame;

    PhotoViewController ctrl = null;
    
    @BeforeMethod
    @Override
    public void setUp() {        
        frame = new JFrame(getName());
        pane = (JPanel)frame.getContentPane();
        pane.setLayout(new FlowLayout());
        pane.setBorder(new EmptyBorder(50, 50, 50, 50));

        ctrl = new PhotoViewController(pane, null);
        tester = ComponentTester.getTester(PhotoCollectionThumbView.class);
        
        setupSession();
        
    }
    
    public void setupSession() {
        session = HibernateUtil.getSessionFactory().openSession();
        HibernateDAOFactory hdf = (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        hdf.setSession( session );
        daoFactory = hdf;
        photoDAO = daoFactory.getPhotoInfoDAO();
        // tx = session.beginTransaction();
        commandHandler = new PhotovaultCommandHandler( null );
        commandHandler.addCommandListener( new CommandListener() {

            public void commandExecuted( CommandExecutedEvent event ) {
                ctrl.fireEventGlobal( event );
            }
            
        });
    }
    
    @AfterMethod
    @Override
    protected void tearDown() {
        frame.dispose();
    }

    @AfterMethod
    public void tearDownSession() {
        session.close();
    }
    
    @BeforeMethod
    public void createTestFolder() throws CommandException {
        PhotoFolderDAO folderDAO = daoFactory.getPhotoFolderDAO();
        PhotoFolder root = folderDAO.findRootFolder();
        CreatePhotoFolderCommand cmd = 
                new CreatePhotoFolderCommand(root, "Test_PhotoViewController", "" );
        commandHandler.executeCommand( cmd );
        folder = cmd.getCreatedFolder();
        photo1 = photoDAO.create();
        photo2 = photoDAO.create();
        session.flush();
        ChangePhotoInfoCommand pcmd = new ChangePhotoInfoCommand( photo1.getUuid() );
        pcmd.addToFolder( folder );
        pcmd.setShootingPlace( "place1" );
        commandHandler.executeCommand( pcmd );
        pcmd = new ChangePhotoInfoCommand( photo2.getUuid() );
        pcmd.addToFolder( folder );
        pcmd.setShootingPlace( "place2" );
        commandHandler.executeCommand( pcmd );
    }
    
    // handy abbreviation for displaying our test frame
    private void showFrame() {
        // Always do direct component manipulation on the event thread
        tester.invokeAndWait(new Runnable() {
            @SuppressWarnings( "deprecation" )
            public void run() { frame.pack(); frame.show(); }
        });
    }    
    
    @Test
    public void testSetCollection() throws CommandException {
        showFrame();
        PhotoFolderDAO folderDAO = daoFactory.getPhotoFolderDAO();
        PhotoFolder root = folderDAO.findRootFolder();
        ctrl.setCollection( root );
        assertTrue( ctrl.getCollection() == root );
        ctrl.setCollection( folder );
        assertTrue( ctrl.getCollection() == folder );
        ctrl.setPhotoComparator( new Comparator() {

            public int compare( Object o1, Object o2 ) {
                PhotoInfo p1 = (PhotoInfo) o1;
                PhotoInfo p2 = (PhotoInfo) o2;
                return p1.getShootingPlace().compareTo( p2.getShootingPlace() );
            }
        } );
        List<PhotoInfo> photos = ctrl.getThumbPane().getPhotos();
        assertEquals( 2, photos.size() );
        assertEquals( photo1.getUuid(), photos.iterator().next().getUuid() );

        ctrl.setPhotoComparator( new Comparator() {

            public int compare( Object o1, Object o2 ) {
                PhotoInfo p1 = (PhotoInfo) o1;
                PhotoInfo p2 = (PhotoInfo) o2;
                return -p1.getShootingPlace().compareTo( p2.getShootingPlace() );
            }
        } );
        assertEquals( photo2.getUuid(), photos.iterator().next().getUuid() );

        // Test that the controller reacts when someone changes the collection
        ChangePhotoInfoCommand cmd = new ChangePhotoInfoCommand( photo1.getUuid() );
        cmd.removeFromFolder( folder );
        commandHandler.executeCommand(cmd);
        photos = ctrl.getThumbPane().getPhotos();
        assertEquals( 1, photos.size() );

        cmd = new ChangePhotoInfoCommand( photo1.getUuid() );
        cmd.addToFolder( folder );
        commandHandler.executeCommand(cmd);
        photos = ctrl.getThumbPane().getPhotos();
        assertEquals( 2, photos.size() );

        cmd = new ChangePhotoInfoCommand( photo2.getUuid() );
        cmd.setShootingPlace( "testPlace" );
        commandHandler.executeCommand(cmd);
        photos = ctrl.getThumbPane().getPhotos();
        assertEquals( 2, photos.size() );
        assertEquals( "testPlace", photos.get( 0 ).getShootingPlace() );
    }
}

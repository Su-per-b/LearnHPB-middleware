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

package org.photovault.folder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.context.ManagedSessionContext;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.Change;
import org.photovault.replication.ChangeFactory;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.HibernateDtoResolverFactory;
import org.photovault.replication.ObjectHistoryDTO;
import org.photovault.replication.VersionedObjectEditor;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
  Test cases for versioning of a folder tree
 */
public class Test_FolderTreeVersioning extends PhotovaultTestCase {

    List<ObjectHistoryDTO> initialState = null;
    
    List<UUID> folderIdsToDelete = new ArrayList<UUID>();
    UUID rootUuid;
    
    Session session;
    DTOResolverFactory rf;
    private DAOFactory daoFactory;
    
    @BeforeClass
    public void setup() {
        session = HibernateUtil.getSessionFactory().openSession();
        rf = new HibernateDtoResolverFactory( session );

        ManagedSessionContext.bind( (org.hibernate.classic.Session) session);
        daoFactory = (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        ((HibernateDAOFactory) daoFactory).setSession( session );
        
    }
    
    /**
     Create initial history of the version tree. and store it in initialState
     @throws java.io.IOException
     */
    @Test
    public void setupHistory() throws IOException {
        
        PhotoFolderDAO folderDao = daoFactory.getPhotoFolderDAO();
        
        PhotoFolder root = folderDao.create( "Root", null );
        folderIdsToDelete.add( root.getUuid() );
        rootUuid = root.getUuid();
        PhotoFolder f1 = folderDao.create( "f1", root );
        folderIdsToDelete.add( 0, f1.getUuid() );
        PhotoFolder f2 = folderDao.create( "f2", root );
        folderIdsToDelete.add( 0, f2.getUuid() );
        PhotoFolder sf1 = folderDao.create( "sf1", f1 );
        folderIdsToDelete.add( 0, sf1.getUuid() );
        session.flush();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( os );
        initialState = new ArrayList();
        storeFolderHierarchy( root, initialState );
    }
    
    /**
     Verify that the stored history can be used to reconstruct the objects
     @throws java.io.IOException
     @throws java.lang.ClassNotFoundException
     @throws java.lang.InstantiationException
     @throws java.lang.IllegalAccessException
     */
    @Test( dependsOnMethods="setupHistory" )
    public void editFolderHierarchy() 
            throws IOException, ClassNotFoundException, InstantiationException, 
            IllegalAccessException {
        ChangeFactory cf = new ChangeFactory( daoFactory.getChangeDAO() );
        PhotoFolderDAO folderDao = daoFactory.getPhotoFolderDAO();
        for ( ObjectHistoryDTO<PhotoFolder> h : initialState ) {
            PhotoFolder f = folderDao.findByUUID( h.getTargetUuid() );
            VersionedObjectEditor<PhotoFolder> ed = null;
            if ( f != null ) {
                    ed = new VersionedObjectEditor<PhotoFolder>(  f , rf );
            } else {
                ed = new VersionedObjectEditor<PhotoFolder>(  PhotoFolder.class, h.getTargetUuid(), rf );
                folderDao.makePersistent( ed.getTarget() );
                session.flush();
            }
            ed.addToHistory( h, cf );
        }
        
        // Check that the history matches
        PhotoFolder root = folderDao.findByUUID( rootUuid );
        VersionedObjectEditor<PhotoFolder> e = new VersionedObjectEditor<PhotoFolder>(  root, rf );
        e.changeToVersion( root.getHistory().getHeads().iterator().next() );
        assertEquals( "Root", root.getName() );
        assertEquals( 2, root.getSubfolders().size() );
        for ( PhotoFolder f : root.getSubfolders() ) {
            if ( f.getName().equals( "f1" ) ) {
                assertEquals( 1, f.getSubfolders().size() );
            } else if ( f.getName().equals( "f2" ) ) {
                assertEquals( 0, f.getSubfolders().size() );
            } else {
                fail( "Wrong name " + f.getName() );
            }
                    
        }
    }
    
    
    
    private void storeFolderHierarchy( PhotoFolder root, List<ObjectHistoryDTO> historyList ) throws IOException {
        historyList.add( new ObjectHistoryDTO( root.getHistory() ) );
        for ( PhotoFolder f : root.getSubfolders() ) {
            storeFolderHierarchy( f, historyList );
        }
    }
    
    private void readFolderHistory( ObjectInputStream ios ) throws IOException, ClassNotFoundException {
        int changeCount = ios.readInt();
        ChangeFactory cf = new ChangeFactory( daoFactory.getChangeDAO() );
        UUID folderUuid = null;
        for ( int n = 0 ; n < changeCount ; n++ ) {
            Change ch = cf.readChange( ios );            
        }        
    }
    
    @AfterMethod
    public void deleteFolders() {
        PhotoFolderDAO folderDao = daoFactory.getPhotoFolderDAO();
        for ( UUID id : folderIdsToDelete ) {
            PhotoFolder f = folderDao.findById( id, false );
            folderDao.makeTransient( f );
        }
        session.flush();
        session.clear();
    }
    
}

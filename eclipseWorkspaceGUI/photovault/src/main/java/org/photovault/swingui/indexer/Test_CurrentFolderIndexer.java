/*
 Copyright (c) 2007 Harri Kaimio

 This file is part of Photovault.
 
 Photovault is free software; you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.
 
 Photovault is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even therrore implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with Photovault; if not, write to the Free Software Foundation,
 Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

package org.photovault.swingui.indexer;

import java.io.File;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.classic.Session;
import org.hibernate.context.ManagedSessionContext;
import org.photovault.command.CommandException;
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.common.PhotovaultException;
import org.photovault.folder.CreatePhotoFolderCommand;
import org.photovault.folder.PhotoFolder;
import org.photovault.folder.PhotoFolderDAO;
import org.photovault.imginfo.ExternalVolume;
import org.photovault.imginfo.FileUtils;
import org.photovault.imginfo.PhotoInfoDAO;
import org.photovault.imginfo.VolumeManager;
import org.photovault.imginfo.indexer.DirTreeIndexerTask;
import org.photovault.imginfo.indexer.IndexFileTask;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.taskscheduler.BackgroundTask;
import org.photovault.test.PhotovaultTestCase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author harri
 */
public class Test_CurrentFolderIndexer extends PhotovaultTestCase {
    static Log log = LogFactory.getLog( Test_CurrentFolderIndexer.class.getName() );
    private File testfileDir;
    private PhotoFolder topFolder;
    private ExternalVolume vol;
    private PhotovaultCommandHandler cmdHandler;
    private Session session;
    private DAOFactory daoFactory = DAOFactory.instance( HibernateDAOFactory.class );
    private File topDir;

    @BeforeClass
    public void setUpEnv() throws IOException, CommandException, PhotovaultException {
        testfileDir = new File( System.getProperty( "basedir" ), "testfiles" );

        topDir = File.createTempFile( "pv_dir_indexer_test_", "" );
        topDir.delete();
        topDir.mkdir();
        
        session = HibernateUtil.getSessionFactory().openSession();
        ManagedSessionContext.bind( (org.hibernate.classic.Session) session );
        ((HibernateDAOFactory) daoFactory).setSession( session );
        cmdHandler = new PhotovaultCommandHandler( session );
        
        vol = new ExternalVolume();
        vol.setName( topDir.getName() );
        session.save( vol );
        VolumeManager.instance().initVolume(vol, topDir );
        
        CreatePhotoFolderCommand cmd = 
                new CreatePhotoFolderCommand(null, topDir.getName(), "" );
        cmd.setExtDir( vol, "" );
        cmdHandler.executeCommand( cmd );
        topFolder = cmd.getCreatedFolder();
        
    }

    @AfterClass
    @Override
    public void tearDown() {
        log.debug( "entry: tearDown" );
        FileUtils.deleteTree( topDir );
        PhotoFolderDAO folderDAO = daoFactory.getPhotoFolderDAO();        
        PhotoInfoDAO photoDAO = daoFactory.getPhotoInfoDAO();        
        topFolder.delete();
        folderDAO.makeTransient( topFolder );        
        session.close();
    }    
    
    /**
     Test indexing a folder
     @throws java.io.IOException
     */
    @Test
    public void testFolderIndexing() throws IOException {
        // Create a subdirectory
        File subdir = new File( topDir, "subdir" );
        subdir.mkdir();
        File testfile = new File( testfileDir, "test1.jpg" );
        FileUtils.copyFile( testfile, new File( topDir, "test1.jpg" ) );
        
        // Try to index the top dir
        CurrentFolderIndexer indexer = new CurrentFolderIndexer( null, cmdHandler );
        indexer.updateFolder( topFolder );
        BackgroundTask task = indexer.requestTask();
        assertTrue( task instanceof DirTreeIndexerTask );
        
        task.executeTask(session, cmdHandler);
        task = indexer.requestTask();
        task.executeTask( session, cmdHandler );
        task = indexer.requestTask();
        assertTrue( task instanceof IndexFileTask );
        task.executeTask(session, cmdHandler);        
        task = indexer.requestTask();
        task.executeTask(session, cmdHandler);        
        task = indexer.requestTask(); 
        assertNull( task );
    }
    
    /**
     Test changing the folder to another during indexing
     */
    @Test( dependsOnMethods="testFolderIndexing" )
    public void testFolderChange() throws IOException {
        // Start indexing operation
        CurrentFolderIndexer indexer = new CurrentFolderIndexer( null, cmdHandler );
        indexer.updateFolder( topFolder );
        BackgroundTask task = indexer.requestTask();
        assertTrue( task instanceof DirTreeIndexerTask );
        
        PhotoFolder subfolder = topFolder.getSubfolders().iterator().next();
        indexer.updateFolder(subfolder);
        task = indexer.requestTask();
        assertTrue( task instanceof DirTreeIndexerTask );
        File subsubdir = new File( topDir, "subdir/subsubdir" );
        subsubdir.mkdir();
        task.executeTask(session, cmdHandler);
        assertEquals( 1, subfolder.getSubfolders().size() );

        File t2 = new File( testfileDir, "test2.jpg" );
        File tt2 = new File( topDir, "subdir/test2.jpg" );
        FileUtils.copyFile(t2, tt2);
        task = indexer.requestTask();
        while ( !( task instanceof IndexFileTask ) ) {
            task.executeTask(session, cmdHandler);                
            task = indexer.requestTask();            
        }
        assertEquals( tt2, ((IndexFileTask)task).getFile() );
    }
}

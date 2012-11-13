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

import com.sun.media.jai.util.SunTileCache;
import com.sun.media.jai.util.SunTileScheduler;
import java.awt.Dimension;
import java.util.Collection;
import javax.media.jai.JAI;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.context.ManagedSessionContext;
import org.photovault.command.CommandExecutedEvent;
import org.photovault.command.PhotovaultCommandHandler;
import org.photovault.common.PhotovaultException;
import org.photovault.common.SchemaUpdateAction;
import org.photovault.common.SchemaUpdateEvent;
import org.photovault.common.SchemaUpdateListener;
import java.net.URL;
import javax.media.jai.TileScheduler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.common.PVDatabase;
import org.photovault.common.PhotovaultSettings;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Transaction;
import org.photovault.command.CommandListener;
import org.photovault.imginfo.VolumeDAO;
import org.photovault.imginfo.VolumeManager;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.swingui.db.DbSettingsDlg;
import org.photovault.swingui.framework.AbstractController;
import org.photovault.swingui.framework.DefaultEvent;
import org.photovault.swingui.taskscheduler.SwingWorkerTaskScheduler;
import org.photovault.taskscheduler.TaskScheduler;

/**
   Main class for the photovault application
*/
public class Photovault extends AbstractController implements SchemaUpdateListener {
    static Log log = LogFactory.getLog( Photovault.class.getName() );

    PhotovaultSettings settings = null;
    
    static Photovault instance;
    private SwingWorkerTaskScheduler taskScheduler;
    
    
    private Photovault() {
	settings = PhotovaultSettings.getSettings();
        setCommandHandler( new PhotovaultCommandHandler( null ) );
        // Forward command execution events to all subcontrollers
        commandHandler.addCommandListener( new CommandListener(  ) {

            public void commandExecuted( CommandExecutedEvent e ) {
                fireEventGlobal( e );
            }
        } );
        
        taskScheduler = new SwingWorkerTaskScheduler( this );
        instance = this;
    }
    
    /**
     Get the Photovault instance
     @return the Photovault object
     */
    public static Photovault getInstance() {
        return instance;
    }
    
    /**
     Get the task scheduler associated with the application
     @return TaskScheduler
     */
    public TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }
    
    Session currentSession  = null;

    private void login( LoginDlg ld ) throws PhotovaultException {
        boolean success = false;
        String user = ld.getUsername();
        String passwd = ld.getPassword();
        String dbName = ld.getDb();
        log.debug( "Using configuration " + dbName );
        settings.setConfiguration( dbName );
        PVDatabase db = settings.getDatabase( dbName );

        HibernateUtil.init( user, passwd, db );

        // TODO: Hack...
        currentSession = HibernateUtil.getSessionFactory().openSession();
        ManagedSessionContext.bind(
                (org.hibernate.classic.Session) currentSession );
        log.debug( "Connection succesful!!!" );
        // Login is succesfull
        // ld.setVisible( false );
        success = true;

        int schemaVersion = db.getSchemaVersion();
        if ( schemaVersion < PVDatabase.CURRENT_SCHEMA_VERSION ) {
            String options[] = {"Proceed", "Exit Photovault"};
            if ( JOptionPane.YES_OPTION == JOptionPane.showOptionDialog( ld,
                    "The database was created with an older version of Photovault\n" +
                    "Photovault will upgrade the database format before starting.",
                    "Upgrade database",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    null ) ) {
                final SchemaUpdateAction updater = new SchemaUpdateAction( db );
                SchemaUpdateStatusDlg statusDlg = new SchemaUpdateStatusDlg(
                        null, true );
                updater.addSchemaUpdateListener( statusDlg );
                Thread upgradeThread = new Thread() {

                    @Override
                    public void run() {
                        updater.upgradeDatabase();
                    }
                };
                upgradeThread.start();
                statusDlg.setVisible( true );
                success = true;
            } else {
                System.exit( 0 );
            }
        }
    }
    

    /**
     initJai() method calls JAI.setTileCache which causes a static dependency to
     JAI. If the method was part of Photovault class Java VM would fail to load
     the class which is not acceptable since the class must give the user instructions
     how to install JAI if it is not installed. So the method must be isolated to
     its own class.
     */
    static class JaiInitializer {
        /**
         If <code>true</code> open the TileCacheTool window. This can be set by the
         -d command line argument.
         */
        public static boolean debugTileCache = true;
        /**
         Initialize Java Advanced Imaging.
         */
        public static void initJAI() {
            JAI jaiInstance = JAI.getDefaultInstance();
            jaiInstance.setTileCache( new SunTileCache( 100*1024*1024 ) );
            TileScheduler sched = new SunTileScheduler( 4, 0, 4, 0 );
            jaiInstance.setTileScheduler( sched );
            JAI.setDefaultTileSize( new Dimension( 256, 256 ) );
        /*
         Not sure how much this helps in practice - Photovault still seems to use
         all heap available in the long run.
         */
            jaiInstance.setRenderingHint( JAI.KEY_CACHED_TILE_RECYCLING_ENABLED, Boolean.TRUE );
            if ( debugTileCache ) {
                try {
                    Class tcDebugger = Class.forName( "tilecachetool.TCTool" );
                    tcDebugger.newInstance();
                } catch (InstantiationException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    void run() {
        checkSystem();
        // 100 MB tile cache
        JaiInitializer.initJAI();
        PhotovaultSettings settings = PhotovaultSettings.getSettings();
        Collection databases = settings.getDatabases();
        if ( databases.size() == 0 ) {
            DbSettingsDlg dlg = new DbSettingsDlg( null, true );
            if ( dlg.showDialog() != DbSettingsDlg.APPROVE_OPTION ) {
                System.exit( 0 );
            }
        }

        LoginDlg login = new LoginDlg( this );
        boolean loginOK = false;
        while ( !loginOK ) {
            int retval = login.showDialog();
            switch ( retval ) {
                case LoginDlg.RETURN_REASON_CANCEL:
                    System.exit( 0 );
                    break;
                case LoginDlg.RETURN_REASON_NEWDB:
                    DbSettingsDlg dlg = new DbSettingsDlg( null, true );
                    if ( dlg.showDialog() == DbSettingsDlg.APPROVE_OPTION ) {
                        login = new LoginDlg( this );
                    }
                    break;
                case LoginDlg.RETURN_REASON_APPROVE:
                    try {
                        login( login );
                        loginOK = true;
                        BrowserWindow wnd = new BrowserWindow( this, null );
                        wnd.setTitle( "Photovault - " + login.getDb() );
                    } catch ( PhotovaultException e ) {
                        JOptionPane.showMessageDialog( null, e.getMessage(),
                                "Login error", JOptionPane.ERROR_MESSAGE );
                    }
                    SwingUtilities.invokeLater( new Runnable() {

                        public void run() {
                            ManagedSessionContext.bind( (org.hibernate.classic.Session) currentSession );
                            initEnvironmnet();
                        }
                    } );
                    break;
                default:
                    log.error( "Unknown return code form LoginDlg.showDialog(): " + retval );
                    break;
            }
        }
    }


    private void initEnvironmnet() {
        /*
         * Check what volumes we have available
         */
        HibernateDAOFactory df =
                (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        VolumeDAO volDao = df.getVolumeDAO();
        Session s = df.getSession();
        Transaction tx = s.beginTransaction();
        VolumeManager vm = VolumeManager.instance();
        vm.updateVolumeMounts( volDao );
        s.flush();
        tx.commit();
        s.close();
    }

    protected void checkJAI() throws PhotovaultException {
        try {
            String jaiVersion = JAI.getBuildVersion();
        } catch ( Throwable t ) {
            throw new PhotovaultException( 
                      "Java Advanced Imaging not installed\n"
                    + "properly. It is needed by Photovault,\n"
                    + "please download and install it from\n"
                    + "http://java.sun.com/products/java-media/jai/");
        }
    }
    
    /** 
     Checks that the system is in OK state.
     */
    protected void checkSystem() {
        try {
            checkJAI();
        } catch ( PhotovaultException e ) {
            JOptionPane.showMessageDialog( null, e.getMessage(),
                    "Photovault error", JOptionPane.ERROR_MESSAGE );
            System.exit( 1 );
        }
    }
    
    /**
     Use Quaqua look and feel to mimic native MacOS X user experince.
     */
    private static void initQuaqua() {
         // set system properties here that affect Quaqua
         // for example the default layout policy for tabbed
         // panes:
         System.setProperty(
            "Quaqua.tabLayoutPolicy","scroll"

         );

         // set the Quaqua Look and Feel in the UIManager
         try {
              UIManager.setLookAndFeel(
                  "ch.randelshofer.quaqua.QuaquaLookAndFeel"
              );
              log.error( "Quaqua initialized" );
         // set UI manager properties here that affect Quaqua
         } catch (Exception e) {
             // take an appropriate action here
             log.error( "Error initializing Quaqua: " + e.getMessage() );
         }        
    }

    /**
     Check if we are running in Macintosh.
     */
    private static boolean isMac() {
        String os = System.getProperty( "os.name" ).toLowerCase();
        boolean mac = false;
        if ( os.indexOf( "mac") > -1 ) {
            mac = true;
        }
        return mac;
    }
    
    /**
     Parse command line arguments
     @param args the arguments supplied to program
     @return true if parsing was succesful, false otherwise
     */
    private static boolean parseArgs( String[] args ) {
        int n = 0;
        while ( n < args.length ) {
            if ( args[n].equals( "-d" ) || args[n].equals( "--debug" ) ) {
                JaiInitializer.debugTileCache = true;
            } else {
                return false;
            }
            n++;
        }
        return true;
    }
    
    private static void printUsage() {
        System.out.println( "Photovault Image Organizer" );
        System.out.println( "Copyright (c) 2007 Harri Kaimio" );
        System.out.println();
        System.out.println( "Usage:" );
        System.out.println( "photovault [-d]" );
        System.out.println( "-d Show debug information" );
        
        
    }
    
    
    public static void main( String [] args ) {
        URL log4jPropertyURL = Photovault.class.getClassLoader().getResource( "photovault_log4j.properties");
        PropertyConfigurator.configure( log4jPropertyURL );	
        if ( !parseArgs( args ) ) {
            printUsage();
            System.exit( 1 );
        }
        if ( isMac() || System.getProperty("photovault.useQuaqua", "false" ).equals( "true" ) ) {
            initQuaqua();
        }
        Photovault app = new Photovault();
	app.run();
    }

    public void schemaUpdateStatusChanged(SchemaUpdateEvent e) {
        System.out.println( "Phase " + e.getPhase()+ ", " + e.getPercentComplete() + "%" );
    }

    /**
     Called by commandHandler when a command has changed an entity. Fires global 
     event to subcontrollers.
     @param o The changed entity
     */
    public void entityChanged( Object o ) {
        DefaultEvent e = null;
//        if ( o instanceof PhotoInfo ) {
//            e = new PhotoInfoModifiedEvent( this, (PhotoInfo) o );
//        } else if ( o instanceof PhotoFolder ) {
//            e = new PhotoFolderModifiedEvent( this, (PhotoFolder) o );
//        } 
        final AbstractController ttish = this;
        final DefaultEvent evt = e;
        if ( e != null ) {
            SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    ttish.fireEventGlobal( evt );
                }
            });
        }
    }
}



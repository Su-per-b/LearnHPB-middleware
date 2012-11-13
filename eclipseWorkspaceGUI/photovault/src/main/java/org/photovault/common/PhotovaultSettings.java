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

package org.photovault.common;

import com.thoughtworks.xstream.XStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Collection;
import java.io.InputStream;
import java.net.URL;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.imginfo.VolumeManager;
import org.xml.sax.SAXException;

/**
   PhotovaultSettings provides access to the installation specific settings - most 
 * importantly to available photovault databases. <p>
 *
 * This class is singleton, only 1 instance is allowed.
*/


public class PhotovaultSettings {

    static private Log log = LogFactory.getLog( PhotovaultSettings.class.getName() );
    static PhotovaultSettings settings = null;
    
    /**
     * Get the singleton settings object.
     */
    public static PhotovaultSettings getSettings( ) {
        if ( settings == null ) {
            settings = new PhotovaultSettings();
        }
        return settings;
    }
    
    File configFile;
    File configDir;
    PhotovaultDatabases databases;
    
    static final String defaultPropFname = "photovault.properties";


    protected PhotovaultSettings() {

        // Find the location of configuration directory
        String configDirName = System.getProperty( "photovault.configdir" );
        configDir = null;
        if ( configDirName != null ) {
            configDir = new File( configDirName );
        } else {
            File homeDir = new File( System.getProperty( "user.home", "" ) );
            configDir = new File( homeDir, ".photovault" );
        }
        if ( !configDir.exists() ) {
            configDir.mkdir();
        }
        databases = new PhotovaultDatabases();
        readDbConfigs( configDir );
        if ( databases.getDatabases().size() == 0 ) {
            // Check if there is legacy configuration file that can be loaded
            importLegacyConfigFile();
        }
            
    }

    /**
     * Import legacy configuration file (used by versions up to 0.5.0) and
     * store it in the new configuration file format, i.e. separate file for
     * each database.
     */
    void importLegacyConfigFile() {
        String confFileName = System.getProperty( "photovault.configfile" );
        File oldConfigFile = null;
        if ( confFileName != null ) {
            log.debug( "photovault.configfile " + confFileName );
            configFile = new File( confFileName );
            log.debug( configFile );
        } else {
            configFile = new File( configDir, "photovault_config.xml" );
            if ( !configFile.exists() ) {
                // check if there is a config file in the old (pre-0.4.0) format
                // and read it if it exists.
                oldConfigFile = new File( configDir, "photovault.xml" );
            }
        }
        if ( configFile.exists() ) {
            log.debug( "Using config file " + configFile.getAbsolutePath() );
            loadConfig( configFile );
        } else if ( oldConfigFile != null && oldConfigFile.exists() ) {
            loadConfig( oldConfigFile );
        }
        try {
            saveDbConfigs( configDir );
        } catch ( IOException ex ) {
            log.error( ex );
        }
    }

    /**
     * Return all known databases
     *@return Collection of PVDatabase objects
     */
    public Collection getDatabases() {
        return databases.getDatabases();
    }
    
    /**
     * Returns the photovault database descriptor for given database
     * @return the PVDatabase object or null if the named database was not found
     */
    public PVDatabase getDatabase( String dbName ) {
        return databases.getDatabase( dbName );
    }

    /**
     * Reads all database configuration files in given directory and store the
     * {@link PVDatabase} objects in {@link #databases} structure. All files 
     * ending in .pvd are considered potential database configuration files.
     * @param dir The directory that is searched for configuration files.
     */
    void readDbConfigs( File dir ) {
        for ( File f: dir.listFiles() ) {
            if ( f.getName().endsWith( ".pvd" ) ) {
                try {
                    PVDatabase db = readDbConfig( f );
                    if ( db != null ) {
                        databases.addDatabase( db );
                    }
                } catch ( FileNotFoundException ex ) {
                    log.error( ex );
                } catch ( IOException ex ) {
                    log.error( ex );
                } catch( PhotovaultException ex ) {
                    log.error( ex );
                }
            }
        }
    }

    /**
     * Save configuration files for all known databases in given directory
     * @param dir The directory used
     * @throws java.io.IOException If saving of configuration files fails.
     */
    void saveDbConfigs( File dir ) throws IOException {
        for ( PVDatabase db : databases.getDatabases() ) {
            String name = db.getName();
            File f = new File( dir, name+".pvd" );
            if ( !f.exists() ) {
                saveDbConfig( db, f );
            }
        }
    }

    /**
     * Save configuration info for this database in file
     * @param f The file
     * @throws java.io.IOException If an error occurs during writing
     */
    public void saveDbConfig( PVDatabase db, File f ) throws IOException {
        BufferedWriter w = new BufferedWriter( new FileWriter( f ) );
            w.write("<?xml version='1.0' ?>\n");
            w.write( "<!--\n" +
                    "This is configuration file for Photovault database\n" +
                    "See http://www.photovault.org for details\n" +
                    "-->\n");
        XStream xs = new XStream();
        xs.processAnnotations( PVDatabase.class );
        xs.alias( "photovault-database", PVDatabase.class );
        // This is needed to ensure that the db descriptor is really instantianted
        db.getDbDescriptor();
        xs.toXML( db, w);
        w.close();
    }

    /**
     * Save database's configuration file to default configuration directory
     * @param db The database that will be saved.
     * @throws java.io.IOException
     */
    public void saveDbConfig( PVDatabase db ) throws IOException {
        File f = new File( configDir, db.getName() + ".pvd" );
        saveDbConfig( db, f );
    }

    /**
     * Read a database configuration file
     * @param f The file to read
     * @return Database configuration for the database
     * @throws java.io.FileNotFoundException If f does not exist
     * @throws java.io.IOException If f cannot be read
     */
    PVDatabase readDbConfig( File f ) throws FileNotFoundException, IOException {
        XStream xs = new XStream();
        xs.processAnnotations( PVDatabase.class );
        xs.alias( "photovault-database", PVDatabase.class );
        BufferedReader rd = new BufferedReader( new FileReader( f ) );
        PVDatabase db = (PVDatabase) xs.fromXML( rd );
        rd.close();
        return db;
    }

    /**
     * Load legacy (pre-0.6.0) configuration file
     */
    private void loadConfig( File f ) {
        Digester digester = new Digester();
        digester.push(this); // Push controller servlet onto the stack
        digester.setValidating(false);
        
        // Digester rules for parsing the file
        digester.addCallMethod( "photovault-config", "setConfigFileVersion", 1 );
        digester.addCallParam( "photovault-config", 0,"version" );
        digester.addObjectCreate( "databases/databases", PhotovaultDatabases.class );
        digester.addSetNext( "databases/databases", "setDatabases" );
        digester.addObjectCreate( "photovault-config/databases", PhotovaultDatabases.class );
        digester.addSetNext( "photovault-config/databases", "setDatabases" );
        digester.addObjectCreate( "*/databases/database", PVDatabase.class );
        digester.addSetProperties( "*/databases/database" );
        digester.addSetNext( "*/databases/database", "addDatabase" );
        
        // Property setting
        digester.addCallMethod( "*/photovault-config/property", "setProperty", 2 );
        digester.addCallParam( "*/photovault-config/property", 0, "name" );
        digester.addCallParam( "*/photovault-config/property", 1, "value" );
        
        digester.addCallMethod( "*/database/volume-mounts/mountpoint", "addMountPoint", 1 );
        digester.addCallParam( "*/database/volume-mounts/mountpoint", 0, "dir" );
        
        // Volume creation
        digester.addObjectCreate( "*/database/volumes/volume", PVDatabase.LegacyVolume.class );
        String [] volumeAttrNames = {
            "basedir", "name"
        };
        String [] volumePropNames = {
            "baseDir", "name"
        };
        digester.addSetProperties( "*/database/volumes/volume",
                volumeAttrNames, volumePropNames );
        digester.addSetNext( "*/database/volumes/volume", "addLegacyVolume" );
        digester.addObjectCreate( "*/database/volumes/external-volume", PVDatabase.LegacyExtVolume.class );
        String [] extVolAttrNames = {
            "folder", "basedir", "name"
        };
        String [] extVolPropNames = {
            "folderId", "baseDir", "name"
        };
        
        digester.addSetProperties( "*/database/volumes/external-volume", 
                extVolAttrNames, extVolPropNames );
        digester.addSetNext( "*/database/volumes/external-volume", "addLegacyVolume" );
        try {
            
            digester.parse( f );
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setDatabases( PhotovaultDatabases dbs ) {
        this.databases = dbs;
    }

    /**
       Set the configuration used when determining property values
       @param confName Name of the configuration. This must be defined in
       photovault.configNames property - results are undefined otherwise.
    */
    public void setConfiguration( String confName ) {
	this.confName = confName;
        
        VolumeManager vm = VolumeManager.instance();
        for ( File mount : databases.getDatabase( confName ).getMountPoints() ) {
            vm.addMountPoint( mount );
        }
        vm.updateVolumeMounts();
    }


    public PVDatabase getCurrentDatabase() {
        return databases.getDatabase( confName );
    }
    
    HashMap properties = new HashMap();
    
    public void setProperty( String name, String value ) {
        properties.put( name, value );
    }
    
    public String getProperty( String name ) {
        return getProperty( name, null );
    }
    
    public String getProperty( String name, String defaultValue ) {
        String ret = defaultValue;
        if ( properties.containsKey( name ) ) {
            ret = (String) properties.get( name );
        }
        return ret;
    }

    String configFileVersion = "unknown";
    
    public void setConfigFileVersion( String version ) {
        configFileVersion = version;
    }
    /**
     Add a new database to the configuration. Note that the configuration is not 
     saved before calling saveConfiguration().
     @param db The database that is added to the configuration
     @throws @see PhotovaultException if a database with the same name already exists
     */
    public void addDatabase(PVDatabase db) throws PhotovaultException {
        databases.addDatabase( db );
    }
	
    String confName;
    
}
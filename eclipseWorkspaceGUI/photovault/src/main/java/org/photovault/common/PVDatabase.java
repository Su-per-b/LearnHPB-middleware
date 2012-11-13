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
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import  org.photovault.imginfo.Volume;
import java.util.Random;
import org.photovault.imginfo.VolumeBase;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.photovault.folder.PhotoFolder;
import org.photovault.folder.PhotoFolderDAO;
import org.photovault.imginfo.VolumeManager;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;

/**
 * PVDatabase represents metadata about a single Photovault database. A database 
 * consists of an SQL database for storing photo metadata and 1 or more volumes 
 * for storing the actual photos
 * @author Harri Kaimio
 */
@XStreamAlias( "photovault-database" )
public class PVDatabase {
    
    static private Log log = LogFactory.getLog( PVDatabase.class.getName() );
    
    /**
     * Database type for a Photovault instance which uses an embedded Derby database
     */
    public static final String TYPE_EMBEDDED = "TYPE_EMBEDDED";
    
    /**
     * Database type for a Photovault instance which uses database server for 
     * storing meta data
     */
    public static final String TYPE_SERVER = "TYPE_SERVER";
    
    
    /**
     Class for describing the pre-0.6.0 volumes still present in configuration 
     file.
     */
    
    public static class LegacyVolume {
        
        public LegacyVolume() {};
        
        public LegacyVolume( String name, String bd ) {
            this.name = name;
            this.baseDir = bd;

        }
        private String name;
        private String baseDir;

        public String getName() {
            return name;
        }

        public void setName( String name ) {
            this.name = name;
        }

        public String getBaseDir() {
            return baseDir;
        }

        public void setBaseDir( String baseDir ) {
            this.baseDir = baseDir;
        }
        
    }
    
    /**
     Class for describing the pre-0.6.0 external volume still present in 
     configuration file.
     */
    public static class LegacyExtVolume extends LegacyVolume {
        
        public LegacyExtVolume() {
            super();
        }
        
        public LegacyExtVolume( String name, String bd, int folderId ) {
            super( name, bd );
            this.folderId = folderId;
        }
        
        private int folderId;

        public int getFolderId() {
            return folderId;
        }

        public void setFolderId( int folderId ) {
            this.folderId = folderId;
        }
    }
    
    /** Creates a new instance of PVDatabase */
    public PVDatabase() {
        volumes = new ArrayList<VolumeBase>();
    }
    
    @XStreamAsAttribute
    private String name;
    @XStreamOmitField
    private String dbHost = "";
    @XStreamOmitField
    private String dbName = "";
    @XStreamOmitField
    private ArrayList<VolumeBase> volumes;

    /**
     UUID of the default volume.
     */
    private UUID defaultVolumeId;

    public void setName( String name ) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    public void setHost(String dbHost) {
        if ( dbHost != null ) {
            this.dbHost = dbHost;
        } else {
            this.dbHost = "";
        }
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @XStreamAlias( "database" )
    HibernateInitializer dbDescriptor;

    public HibernateInitializer getDbDescriptor() {
        if ( dbDescriptor == null ) {
            if ( instanceType.equals( TYPE_EMBEDDED ) ) {
                DerbyDescriptor dd = new DerbyDescriptor();
                dd.setDirectory( new File( dataDirectory, "derby" ) );
                dbDescriptor = dd;
            } else {
                MysqlDescriptor md = new MysqlDescriptor();
                md.setDbname( dbName );
                md.setHost( dbHost );
                dbDescriptor = md;
            }
        }
        return dbDescriptor;
    }

    public void setDbDescriptor( HibernateInitializer db ) {
        dbDescriptor = db;
    }
    
    /**
     Add a new volume to this database.
     @param volume The new volume
     @throws PhotovaultException if another volume with the same name is already present
     @deprecated For configuration file parsing backward compatibility only.
     */
    public void addVolume( VolumeBase volume ) throws PhotovaultException {
        VolumeBase v = getVolume( volume.getName() );
        if ( v != null ) {
            throw new PhotovaultException( "Volume with name " + volume.getName() +
                    " already exists" );
        }
        volumes.add( volume );
        
        // Ensure that this volume's basedir is in mount point list
        if ( volume.getBaseDir() != null ) {
            mountPoints.add( volume.getBaseDir() );
        }
    }
    
    /**
     Removes a volume from this database object. The data in volume is not deleted,
     it is just removed form the volumes known for this database.
     @param volume The volume that will be removed
     */
    public void removeVolume(Volume volume) {
        volumes.remove( volume );
    }

    @XStreamImplicit( itemFieldName="legacy-volume" )
    List<LegacyVolume> legacyVolumes = new ArrayList<LegacyVolume>();
    
    public void addLegacyVolume( LegacyVolume v ) {
        legacyVolumes.add( v );
    }
    
    List<LegacyVolume> getLegacyVolumes() {
        if ( legacyVolumes == null ) {
            legacyVolumes = new ArrayList<LegacyVolume>();
        }
        return legacyVolumes;
    }
    
    /**
     Mount points in which volumes for this database can be mounted.
     */
    @XStreamImplicit( itemFieldName="mountpoint" )
    Set<File> mountPoints = new HashSet<File>();
    
    /**
     Add a new mount point for volumes
     
     @param path Absolute path to the volume mount point.
     */
    public void addMountPoint( String path ) {
        if ( path != null ) {
            mountPoints.add( new File( path ) );
        }
    }
    
    /**
     Get mount points for volumes of this database
     @return
     */
    public Set<File> getMountPoints() {
        return mountPoints;
    }

    /**
     Get default volume
     @deprecated Use VolumeDAO#getDefaultVolume instead
     */
    public VolumeBase getDefaultVolume() {
        VolumeBase vol = null;
        if ( volumes.size() > 0 ) {
            vol = (VolumeBase) volumes.get(0);
        }
        return vol;
    }
        
    @XStreamOmitField
    private String instanceType = TYPE_SERVER;

    /**
     * Get the instacne type of the database This can be either 
     * TYPE_EMBEDDED or TYPE_SERVER
     * @deprecated Use the type of dbDescriptor instead
     */
    public String getInstanceType() {
        return instanceType;
    }

    
    public void setInstanceType(String instanceType) {
        if ( instanceType.equals( TYPE_EMBEDDED ) ) {
            this.instanceType = TYPE_EMBEDDED;
        } else if ( instanceType.equals( TYPE_SERVER ) ) {
            this.instanceType = TYPE_SERVER;
        } 
    }    

    /**
     Path to the directory where data for this database is stored
     
     */
    @XStreamOmitField
    private File dataDirectory = null;
    
    /**
     Get the data directory of this database. In case of Derby based database,
     both the database and photos reside in this directory. In case of MySQL, 
     only photos are stored here (database resider in server)
     @return Path to data directory.
     */
    public File getDataDirectory() {
        return dataDirectory;
    }

    /**
     Set the data directory for the database.
     @param embeddedDirectory The directory
     */
    public void setDataDirectory( File embeddedDirectory ) {
        this.dataDirectory = embeddedDirectory;
    }
    
    public String getInstanceDir() {
        String res = "";
        if ( dataDirectory != null ) {
            res = dataDirectory.getAbsolutePath();
        }
        return res;
    }

    public void setInstanceDir( String path ) {
        dataDirectory = new File( path );
    }
    
    /**
     * Returns a volume with the given name or <code>null</code> if it does not exist
     *
     */
    public VolumeBase getVolume( String volumeName ) {
        VolumeBase vol = null;
        
        /* TODO: Should we use HashMap for volumes instead of iterating a list?
         * This method is urgenty needed for a WAR for bug 44 so I am doing it 
         * in the least disruptive way - and since the number of volumes per database 
         * is currently low this may be enough.
         */
        Iterator iter = volumes.iterator();
        while ( iter.hasNext() ) {
            VolumeBase candidate = (VolumeBase) iter.next();
            if ( candidate.getName().equals( volumeName ) ) {
                vol = candidate;
            }
        }
        return vol;
    }
    
    /**
     * Creates a new database with the parameters specified in this object.
     * <P>
     * The database schema is stored in DDLUTILS XML format in resource 
     * photovault_schema.xml.
     *
     * @param user Username used when creating the SQL database.
     * @param passwd Password for the user
     */
    public void createDatabase( String user, String passwd ) {
        createDatabase( user, passwd, null );
    }
    
    /**
     * Creates a new database with the parameters specified in this object and
     * populate it with given seed data.
     * <P>
     * The database schema is stored in DDLUTILS XML format in resource 
     * photovault_schema.xml.
     *
     * @param user Username used when creating the SQL database.
     * @param passwd Password for the user
     * @param seedDataResource A resource URI that contains data that should be 
     * loaded to the database. This data must be in Apache DDL format. If no 
     * additional seed data is required this must be <code>null</code>
     */
    public void createDatabase( String user, String passwd, String seedDataResource ) {
        
        // Get the database schema XML file
        
        try {
            HibernateUtil.init( user, passwd, this );
        } catch ( PhotovaultException e ) {
            log.error( e.getMessage(), e );
        }
        SchemaExport schexport = new SchemaExport( HibernateUtil.getConfiguration() );
        schexport.create( true, true );
        
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = s.beginTransaction();
        
        HibernateDAOFactory df = 
                (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        df.setSession( s );
        PhotoFolderDAO folderDao = df.getPhotoFolderDAO();
        PhotoFolder topFolder = folderDao.create( PhotoFolder.ROOT_UUID, null );
        topFolder.setName( "Top" );
        s.save( topFolder );
        
        // TODO: Since the seed has only 48 significant bits this id is not really an 
        // 128-bit random number!!!
        Random rnd = new Random();
        String idStr = "";
        StringBuffer idBuf = new StringBuffer();
        for ( int n=0; n < 4; n++ ) {
            int r = rnd.nextInt();
            idBuf.append( Integer.toHexString( r ) );
        }
        idStr = idBuf.toString();
        
        // Create default volume
        Volume defVol = new Volume();
        defVol.setName( "default_volume" );
        s.save( defVol );
        
        File defVolMount = dataDirectory;
        if ( instanceType != TYPE_SERVER ) {
            defVolMount = new File( dataDirectory, "photos" );
        }
        try {
            VolumeManager.instance().initVolume( defVol, defVolMount );
        } catch ( PhotovaultException ex ) {
            log.error( ex );
        }
        addMountPoint( defVolMount.getAbsolutePath() );
        DbInfo dbInfo = new DbInfo();
        dbInfo.setCreateTime( new Date() );
        dbInfo.setId( idStr );
        dbInfo.setVersion( CURRENT_SCHEMA_VERSION );
        dbInfo.setDefaultVolumeId( defVol.getId() );
        s.save( dbInfo );
        
        s.flush();
        tr.commit();
        s.close();
    }

    /**
     Returns the schema version of the Photovault database
     */ 
    public int getSchemaVersion() {
        DbInfo info = DbInfo.getDbInfo();
        if ( info != null ) {
            return info.getVersion();
        }
        return DbInfo.querySchemaVersion();
    }

    /**
     * Read resolver is needed due to a problem with XStreamer implicit
     * collections. If there are no elements in the collection, XStreamer leaves
     * it as <code>null</code>, not as an empty collection. I am not
     * sure whether this is really a bug, but it definitely is an compatibility
     * problem.
     * @return
     */
    private Object readResolve() {
        if ( mountPoints == null ) {
            mountPoints = new HashSet<File>();
        }
        if ( volumes == null ) {
            volumes = new ArrayList<VolumeBase>();
        }
        if ( legacyVolumes == null ) {
            legacyVolumes = new ArrayList<LegacyVolume>();
        }
        return this;
    }
    /**
     * Save configuration info for this database in file
     * @param f The file
     * @throws java.io.IOException If an error occurs during writing
     */
    public void saveConfig( File f ) throws IOException {
        BufferedWriter w = new BufferedWriter( new FileWriter( f ) );
            w.write("<?xml version='1.0' ?>\n");
            w.write( "<!--\n" +
                    "This is configuration file for Photovault database\n" +
                    "See http://www.photovault.org for details\n" +
                    "-->\n");
        XStream xs = new XStream();
        xs.processAnnotations( PVDatabase.class );
        xs.alias( "photovault-database", PVDatabase.class );
        xs.toXML( this, w);
        w.close();
    }

    /**
     Write this object as an XML element
     @param outputWriter The writer into which the object is written
     @param indent Number of spaces to indent each line
     */
    void writeXml(BufferedWriter outputWriter, int indent ) throws IOException {
        String s = "                                ".substring( 0, indent );
        outputWriter.write( s+ "<database name=\"" + name + 
                "\" instanceType=\"" + getInstanceType() +
                "\" instanceDir=\"" + getInstanceDir() + "\"" );
        if ( instanceType == PVDatabase.TYPE_SERVER ) {
            outputWriter.write( " host=\"" + dbHost + 
                    "\" dbName=\"" + dbName + "\"");
        }        
        outputWriter.write( ">\n" );
        outputWriter.write( String.format( "%s  <volume-mounts>\n", s ) );
        for ( File mount : mountPoints ) {
            outputWriter.write( String.format( 
                    "%s    <mountpoint dir=\"%s\"/>\n", 
                    s, mount.getAbsolutePath() ) );
        }
        outputWriter.write( String.format( "%s  </volume-mounts>\n", s ) );
        
        outputWriter.write( s + "  " + "<volumes>\n" );
        for( LegacyVolume v : legacyVolumes ) {
            if ( v instanceof LegacyExtVolume ) {
                LegacyExtVolume ev = (LegacyExtVolume )  v;
                outputWriter.write( String.format( 
                        "%s    <external-volume name=\"%s\" basedir=\"%s\" folder=\"%d\"/>", 
                        s, ev.getName(), ev.getBaseDir(), ev.getFolderId() ) );
            } else {
                outputWriter.write( String.format( 
                        "%s    <volume name=\"%s\" basedir=\"%s\"/>", 
                        s, v.getName(), v.getBaseDir() ) );
            }
        }
        outputWriter.write( s + "  " + "</volumes>\n" );
        outputWriter.write( s + "</database>\n" );
    }



    /**
     The latest schema version which should be used with this version of 
     Photovault
     */
    static public final int CURRENT_SCHEMA_VERSION = 13;
}

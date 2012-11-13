/*
  Copyright (c) 2006-2009 Harri Kaimio
  
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

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.Vector;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.dcraw.RawSettingsFactory;
import org.photovault.folder.ExternalDir;
import org.photovault.folder.FolderEditor;
import org.photovault.folder.FolderPhotoAssocDAO;
import org.photovault.folder.FolderPhotoAssociation;
import org.photovault.folder.PhotoFolder;
import org.photovault.folder.PhotoFolderDAO;
import org.photovault.image.ChannelMapOperation;
import org.photovault.image.ChannelMapOperationFactory;
import org.photovault.imginfo.CopyImageDescriptor;
import org.photovault.imginfo.ExternalVolume;
import org.photovault.imginfo.FileLocation;
import org.photovault.imginfo.FuzzyDate;
import org.photovault.imginfo.ImageDescriptorBase;
import org.photovault.imginfo.ImageDescriptorDAO;
import org.photovault.imginfo.ImageFile;
import org.photovault.imginfo.ImageFileDAO;
import org.photovault.imginfo.OriginalImageDescriptor;
import org.photovault.imginfo.PhotoEditor;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoDAO;
import org.photovault.imginfo.Volume;
import org.photovault.imginfo.VolumeBase;
import org.photovault.imginfo.VolumeDAO;
import org.photovault.imginfo.VolumeManager;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.VersionedObjectEditor;

/**
 SchemaUpdateAction updates the database schema created by an earlier version of 
 Photovault to match the current databae schema.<P>
 
 The actual schema update is done using DdlUtils but depending on exact situation
 other modificationto datamay be needed in addition just altering DB tables.
 */
public class SchemaUpdateAction {
    static Log log = LogFactory.getLog( SchemaUpdateAction.class.getName() );
    
    PVDatabase db = null;
    
    /** Creates a new instance of SchemaUpdateAction 
     @param db The dabatbase whose schema is going to be updated
     */
    public SchemaUpdateAction( PVDatabase db ) {
        this.db = db;
    }
    
    
    /**
     Upgrades database schema and content to be compatible with the current 
     version of Photovault
     */
    public void upgradeDatabase() {
        fireStatusChangeEvent( new SchemaUpdateEvent( 
                new SchemaUpdateOperation( "Altering daptabase schema" ), 0 ) );
        
        int oldVersion = db.getSchemaVersion();
        

        
        
        SchemaExport schexport = new SchemaExport( HibernateUtil.getConfiguration() );
        schexport.create( true, true );
       
        // Alter tables to match corrent schema
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Connection con = session.connection();
        DbInfo info = DbInfo.getDbInfo( session );
        if ( info == null ) {
            info = new DbInfo();
            info.setCreateTime( new Date() );
            info.setId( UUID.randomUUID().toString() );
            VolumeManager vm = VolumeManager.instance();
        }
        session.saveOrUpdate( info );
        session.flush();
        tx.commit();
        session.clear();
        tx = session.beginTransaction();

        /*
         TODO: Schema changes should be done using Hibernate tools. But how to 
         handle the oledd schemas?
         */
//        try {
//            /*
//            TODO:
//            Derby alter table statements created by DdlUtils have wrong syntax.
//            Luckily we do not need to do such modifications for now. There is
//            error report for DdlUtils (http://issues.apache.org/jira/browse/DDLUTILS-53),
//            after it has been corrected the alterColumns flag should be set to
//            true.
//             */
//            log.info( platform.getAlterTablesSql( con, dbModel ) );
//            platform.alterTables( con, dbModel, false );
//        } catch ( DatabaseOperationException ex ) {
//            log.error( ex.getMessage(), ex );
//        }


        if ( oldVersion < 4 ) {
            // In older version hashcolumn was not included in schema so we must fill it.
            /**
             TODO: Implement this for 0.6.0
             */
            throw new IllegalStateException( "Conversion from pre-0.5.0 databases not yet supported" );
        }
        /*
        if ( oldVersion < 10 ) {
            // Initialize Hibernate sequence generators
            Query q = session.createQuery( "select max( rs.rawSettingId ) from RawConversionSettings rs" );
            int maxRawSettingId = (Integer) q.uniqueResult();
            q = session.createQuery( "select max( photo.id ) from PhotoInfo photo" );
            int maxPhotoId = (Integer) q.uniqueResult();
            q = session.createQuery( "select max( folder.folderId ) from PhotoFolder folder" );
            int maxFolderId = (Integer) q.uniqueResult();
            DynaBean dbInfo = dbModel.createDynaBeanFor( "unique_keys", false );
            dbInfo.set( "id_name", "hibernate_seq" );
            dbInfo.set( "next_val", new Integer( maxPhotoId+1 ) );
            platform.insert( con, dbModel, dbInfo );
            dbInfo.set( "id_name", "rawconv_id_gen" );
            dbInfo.set( "next_val", new Integer( maxRawSettingId+1 ) );
            platform.insert( con, dbModel, dbInfo );
            dbInfo.set( "id_name", "folder_id_gen" );
            dbInfo.set( "next_val", new Integer( maxFolderId+1 ) );
            platform.insert( con, dbModel, dbInfo );
            
            try {
                Statement stmt = con.createStatement();
                stmt.executeUpdate( "insert into unique_keys(hibernate_seq, values ( \"hibernate_seq\", " + (maxPhotoId+1) + " )" );
                stmt.close();
            } catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }
        */
        
        /*
        if ( oldVersion < 11 ) {
            upgrade11( session );
        }
        */
        if ( oldVersion < 12 ) {
            migrateToVersionedSchema();
        }
        
        info = DbInfo.getDbInfo( session );
        info.setVersion( db.CURRENT_SCHEMA_VERSION );
        session.flush();
        tx.commit();
        session.close();
        
        fireStatusChangeEvent( new SchemaUpdateEvent( 
                SchemaUpdateOperation.UPDATE_COMPLETE, 100 ) );
    
    }
    

    /**
     Convert 0.5.0 style database to the new versioned schema used by 0.6.0.
     */
    private void migrateToVersionedSchema() {
        try {
            convertFolders();
            convertVolumes();
            convertPhotos();
            convertFolderAssociations();
            
        } catch ( SQLException e ) {
            log.error( "Error while migrating to new schema: " + e.getMessage(), e );
        }
    }
    
    Map<String, UUID> volIds = new HashMap<String,UUID>();

    Map<Integer, UUID> folderUuids = new HashMap<Integer, UUID>();
    Map<Integer, UUID> photoUuids = new HashMap<Integer, UUID>();
    
     /**
     Convert volumes to new schema
     */
    private void convertVolumes() {
        SchemaUpdateOperation oper = new SchemaUpdateOperation( "Converting volumes" );
        Session s = HibernateUtil.getSessionFactory().openSession();

        HibernateDAOFactory df =
                (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        df.setSession( s );
        VolumeManager vm = VolumeManager.instance();
        VolumeDAO volDao = df.getVolumeDAO();
        PhotoFolderDAO folderDao = df.getPhotoFolderDAO();
        List<PVDatabase.LegacyVolume> vols = db.getLegacyVolumes();
        Volume defVol = null;
        int volCount = vols.size();
        int convertedCount = 0;
        for ( PVDatabase.LegacyVolume lvol : vols ) {
            log.debug(  "Converting" + lvol.getClass().getName() + " " + 
                    lvol.getName() + " at " + lvol.getBaseDir() );
            fireStatusChangeEvent(
                    new SchemaUpdateEvent( oper, convertedCount * 100 / volCount ) );
            File basedir = new File( lvol.getBaseDir() );
            if ( !basedir.exists() ) {
                // The volume was not found
                log.error( "Volume " + lvol.getName() + 
                        " was not found at " + basedir.getAbsolutePath() );
            }
            VolumeBase vol = null;
            
            // Check if the volume is initialized
            try {
                vol = vm.getVolumeAt( basedir, volDao );
            } catch ( FileNotFoundException ex ) {
                log.error( "Could not open volume config file", ex );
            } catch ( IOException ex ) {
                log.error( "Error reading volume config file", ex );
            }

            if ( vol == null ) {
                vol = (lvol instanceof PVDatabase.LegacyExtVolume) ?
                    new ExternalVolume() : new Volume();
                vol.setName( lvol.getName() );
                volDao.makePersistent( vol );
                try {
                    vm.initVolume( vol, basedir );
                } catch ( PhotovaultException ex ) {
                    log.error( "Could not initialize volume:", ex );
                }
            }

            if ( lvol instanceof PVDatabase.LegacyExtVolume ) {
                UUID folderId = folderUuids.get(
                        ((PVDatabase.LegacyExtVolume) lvol).getFolderId() );
                if ( folderId != null ) {
                    PhotoFolder f = folderDao.findByUUID( folderId );
                    ((ExternalVolume) vol).setFolder(
                            folderDao.findByUUID( folderId ) );
                    addExtVolReferences( f,(ExternalVolume) vol, "" );
                }
            } else {
                defVol = (Volume) vol;
            }
            volIds.put( vol.getName(), vol.getId() );
            vm.addMountPoint( basedir );
            db.addMountPoint( basedir.getAbsolutePath() );
            convertedCount++;
        }
        DbInfo info = DbInfo.getDbInfo( s );
        if ( defVol != null ) {
            /**
             * Note that infor is not saved here, as it is global singleton.
             * I know. This is dirty...
             */
            info.setDefaultVolumeId( defVol.getId() );
        }
        s.flush();
        s.close();
        db.getLegacyVolumes().clear();
        vm.updateVolumeMounts();
        try {
            PhotovaultSettings.getSettings().saveDbConfig( db );
        } catch ( IOException ex ) {
            log.error( ex );
        }
        if ( volCount > 0 ) {
            fireStatusChangeEvent(
                    new SchemaUpdateEvent( oper, convertedCount * 100 / volCount ) );
        }
    }

    /**
     * Update the {@link PhotoFolder#extDir} fields for folders that describe
     * the structure of an external volume. The function is called recusrively
     * by {@link #convertVolumes() }.
     * @param f The folder to be updated (all subfolders are updated recursively
     * @param vol Volume f is associated with
     * @param path Path to this folder
     */
    private void addExtVolReferences( PhotoFolder f, ExternalVolume vol, String path ) {
        f.setExternalDir( new ExternalDir( vol, path ) );
        for ( PhotoFolder sub : f.getSubfolders() ) {
            String subFolderPrefix = path + "/" + sub.getName();
            addExtVolReferences( sub, vol, subFolderPrefix );
        }
    }

    /**
     Convert folder hiearchy from old schema to the new one
     */
    private void convertFolders() throws SQLException {
        SchemaUpdateOperation oper = new SchemaUpdateOperation( "COnverting folders" );
        log.debug( "Starting to convert folders to new schema" );
        Session s = HibernateUtil.getSessionFactory().openSession();
        Session sqlSess = HibernateUtil.getSessionFactory().openSession();
        Connection conn = sqlSess.connection();

        Queue<Integer> waiting = new LinkedList<Integer>();
        Map<Integer, PhotoFolder> foldersById = new HashMap<Integer, PhotoFolder>();
        waiting.add(  1 );
        HibernateDAOFactory df = 
                (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        df.setSession( s );
        PhotoFolderDAO folderDao = df.getPhotoFolderDAO();
        DTOResolverFactory rf = df.getDTOResolverFactory();
        PhotoFolder topFolder = folderDao.findByUUID( PhotoFolder.ROOT_UUID );
        if ( topFolder == null ) {
            topFolder = folderDao.create( PhotoFolder.ROOT_UUID, null );
            topFolder.setName( "Top" );
        }
        foldersById.put(  1, topFolder );

        Statement countStmt = conn.createStatement();
        ResultSet countRs = countStmt.executeQuery( "select count(*) from photo_collections" );
        int folderCount = -1;
        if ( countRs.next() ) {
            folderCount = countRs.getInt( 1 );
        }
        countRs.close();
        countStmt.close();

        int convertedCount = 0;
        PreparedStatement stmt = conn.prepareStatement( 
                "select * from photo_collections where parent = ?" );
        while ( !waiting.isEmpty() ) {
            int parentId = waiting.remove();
            PhotoFolder parent = foldersById.get(  parentId );
            log.debug( "Querying for folders with parent " + parentId );
            stmt.setInt( 1, parentId );
            ResultSet rs = stmt.executeQuery();
            while ( rs.next() ) {
                // Create the folder
                
                /*
                 TODO: should the UUID be created algorithmically?
                 Or better, how to ensure that UUIDs for folders that are part
                 of external volume will always be the same?
                 */
                fireStatusChangeEvent(
                        new SchemaUpdateEvent( oper, convertedCount * 100 / folderCount ));
                String uuidStr = rs.getString( "collection_uuid" );
                int id = rs.getInt( "collection_id" );
                log.debug( "Creating folder with old id " + id + ", uuid " + uuidStr );
                UUID uuid = (uuidStr != null) ?
                    UUID.fromString( uuidStr ) :
                    UUID.randomUUID();
                if ( id == 1 ) {
                    uuid = PhotoFolder.ROOT_UUID;
                }
                
                PhotoFolder f = folderDao.create( uuid, parent );
                VersionedObjectEditor<PhotoFolder> e = f.editor( rf );
                FolderEditor fe = (FolderEditor) e.getProxy();
                fe.setName( rs.getString( "collection_name" ) );
                fe.setDescription( rs.getString( "collection_desc" ) );
                e.apply();
                /*
                 TODO: how to set the create time & last modified time without 
                 exposing them to others?
                 */
                log.debug(  "folder saved" );
                foldersById.put( id, f );
                folderUuids.put(  id, uuid );
                waiting.add( id );
                convertedCount++;
            }
            try {
                rs.close();
            } catch ( SQLException e ) {
                log.error( "Error closing result set", e );
            } 
        }
        s.flush();
        sqlSess.close();
        s.close();
        fireStatusChangeEvent(
                new SchemaUpdateEvent( oper, convertedCount * 100 / folderCount ) );
    }
    
    private void convertFolderAssociations() {
        SchemaUpdateOperation oper =
                new SchemaUpdateOperation( "Adding photos to folders" );
        Session s = HibernateUtil.getSessionFactory().openSession();
        HibernateDAOFactory daoFactory = 
                (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        daoFactory.setSession( s );
        PhotoInfoDAO photoDao = daoFactory.getPhotoInfoDAO();
        PhotoFolderDAO folderDAO = daoFactory.getPhotoFolderDAO();
        FolderPhotoAssocDAO assocDAO = daoFactory.getFolderPhotoAssocDAO();
        final String sql =
                "select * from collection_photos";
        String sqlCount = "select count(*) from collection_photos";
        Session sqlSess = HibernateUtil.getSessionFactory().openSession();
        Connection conn = sqlSess.connection();
        Statement stmt = null;
        ResultSet rs = null;
        int assocCount = 0;
        int percentDone = -1;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery( sqlCount );
            int totalAssocCount = -1;
            if ( rs.next() ) {
                totalAssocCount = rs.getInt( 1 );
            }
            rs.close();
            rs = stmt.executeQuery( sql );
            while ( rs.next() ) {
                int newPercentDone = assocCount * 100 / totalAssocCount;
                if ( newPercentDone != percentDone ) {
                    percentDone = newPercentDone;
                    fireStatusChangeEvent(
                            new SchemaUpdateEvent( oper, percentDone ));
                }
                int folderId = rs.getInt( "collection_id" );
                int photoId = rs.getInt( "photo_id" );
                UUID folderUuid = folderUuids.get( folderId );
                UUID photoUuid = photoUuids.get( photoId );
                if ( folderUuid != null && photoUuid != null ) {
                    PhotoFolder f = folderDAO.findById( folderUuid, false );
                    PhotoInfo p = photoDao.findById( photoUuid, false );
                    FolderPhotoAssociation a = new FolderPhotoAssociation( f, p );
                    assocDAO.makePersistent( a );
                    f.addPhotoAssociation( a );
                    p.addFolderAssociation( a );

                    s.flush();
                    assocCount++;
                    if ( assocCount % 50 == 0 ) {
                        s.clear();                        
                    }
                }
            }
        } catch ( SQLException ex ) {
            log.error( ex );
        } finally {
            if ( rs != null ) {
                try {
                    rs.close();
                } catch ( SQLException e ) {
                    log.error( e );
                }  
            }
            if ( stmt != null ) {
                try {
                    stmt.close();
                } catch ( SQLException e ) {
                    log.error( e );
                }  
            }
            s.close();
            sqlSess.close();
        }
    }
    
    /**
     SQL query for fetching information about photos and image instances from 
     old database schema. Alll information is fetched, ordered first by photo ID
     and secondly so that original instance is returned before copies and 
     thumbnails.
     */
    static private String oldPhotoQuerySql = "select " +
            "    p.photo_id as p_photo_id, " +
            "    p.photo_uuid as p_photo_uuid, " +
            "    p.shoot_time as p_shoot_time, " +
            "    p.time_accuracy as p_time_accuracy, " +
            "    p.shooting_place as p_shooting_place, " +
            "    p.photographer as p_photographer, " +
            "    p.f_stop as p_f_stop, " +
            "    p.focal_length as p_focal_length, " +
            "    p.shutter_speed as p_shutter_speed, " +
            "    p.camera as p_camera, " +
            "    p.lens as p_lens, " +
            "    p.film as p_film, " +
            "    p.film_speed as p_film_speed, " +
            "    p.pref_rotation as p_pref_rotation, " +
            "    p.clip_xmin as p_clip_xmin, " +
            "    p.clip_xmax as p_clip_xmax, " +
            "    p.clip_ymin as p_clip_ymin, " +
            "    p.clip_ymax as p_clip_ymax, " +
            "    p.orig_fname as p_orig_fname, " +
            "    p.description as p_description, " +
            "    p.photo_quality as p_photo_quality, " +
            "    p.last_modified as p_last_modified, " +
            "    p.channel_map as p_channel_map, " +
            "    p.hash as p_hash, " +
            "    dr.whitepoint as p_whitepoint, " +
            "    dr.blackpoint as p_blackpoint, " +
            "    dr.ev_corr as p_ev_corr, " +
            "    dr.hlight_corr as p_hlight_corr, " +
            "    dr.embedded_profile as p_embedded_profile , " +
            "    dr.wb_type as p_wb_type, " +
            "    dr.r_g_ratio as p_r_g_ratio, " +
            "    dr.b_g_ratio as p_b_g_ratio, " +
            "    dr.dl_r_g_ratio as p_dl_r_g_ratio, " +
            "    dr.dl_b_g_ratio as p_dl_b_g_ratio, " +
            "    i.volume_id as i_volume_id, " +
            "    i.fname as i_fname, " +
            "    i.instance_uuid as i_instance_uuid, " +
            "    i.width as i_width, " +
            "    i.height as i_height, " +
            "    i.rotated as i_rotated, " +
            "    i.instance_type as i_instance_type, " +
            "    i.hash as i_hash, " +
            "    i.channel_map as i_channel_map, " +
            "    i.file_size as i_file_size, " +
            "    i.mtime as i_mtime, " +
            "    i.check_time as i_check_time, " +
            "    i.crop_xmin as i_crop_xmin, " +
            "    i.crop_xmax as i_crop_xmax, " +
            "    i.crop_ymin as i_crop_ymin, " +
            "    i.crop_ymax as i_crop_ymax, " +
            "    ir.whitepoint as i_whitepoint, " +
            "    ir.blackpoint as i_blackpoint, " +
            "    ir.ev_corr as i_ev_corr, " +
            "    ir.hlight_corr as i_hlight_corr, " +
            "    ir.embedded_profile as i_embedded_profile , " +
            "    ir.wb_type as i_wb_type, " +
            "    ir.r_g_ratio as i_r_g_ratio, " +
            "    ir.b_g_ratio as i_b_g_ratio, " +
            "    ir.dl_r_g_ratio as i_dl_r_g_ratio, " +
            "    ir.dl_b_g_ratio as i_dl_b_g_ratio, " +
            "    case " +
            "        when i.instance_type = 'original' then 1 " +
            "        else 0 " +
            "    end i_is_original " +
            "from " +
            "    photos p left outer join " +
            "    dcraw_settings dr  on p.rawconv_id = dr.rawconv_id, " +
            "    image_instances i left outer join " +
            "    dcraw_settings ir  on i.rawconv_id = ir.rawconv_id " +
            "where " +
            "    i.photo_id = p.photo_id " +
            "order by p.photo_id, i_is_original desc ";

    private void convertPhotos() throws SQLException {
        SchemaUpdateOperation oper =
                new SchemaUpdateOperation( "Converting photos" );
        Session s = HibernateUtil.getSessionFactory().openSession();
        HibernateDAOFactory daoFactory =
                (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
        daoFactory.setSession( s );
        DTOResolverFactory rf = daoFactory.getDTOResolverFactory();
        PhotoInfoDAO photoDao = daoFactory.getPhotoInfoDAO();
        ImageDescriptorDAO imgDao = daoFactory.getImageDescriptorDAO();
        ImageFileDAO ifDao = daoFactory.getImageFileDAO();
        VolumeDAO volDao = daoFactory.getVolumeDAO();

        /*
        We need a second session for reading old data so that Hibernate can 
        commit its changes
         */
        Session sqlSess = HibernateUtil.getSessionFactory().openSession();
        Connection conn = sqlSess.connection();
        Statement stmt = conn.createStatement();
        ResultSet countRs = stmt.executeQuery( "select count(*) from photos" );
        int photoCount = -1;
        if ( countRs.next() ) {
            photoCount = countRs.getInt( 1 );
        }
        countRs.close();
        ResultSet rs = stmt.executeQuery( oldPhotoQuerySql );
        int currentPhotoId = -1;
        OriginalImageDescriptor currentOriginal = null;
        long photoStartTime = -1;
        int convertedCount = -1;

        while ( rs.next() ) {
            int photoId = rs.getInt( "p_photo_id" );
            boolean isNewPhoto = false;
            if ( photoId != currentPhotoId ) {
                isNewPhoto = true;
                currentPhotoId = photoId;
                convertedCount++;
                fireStatusChangeEvent(
                    new SchemaUpdateEvent( oper, convertedCount * 100 / photoCount ));
                /*
                Photos are not dependent from each other, so clear the session 
                cache to improve performance and memory usage.
                 */
                s.clear();
                log.debug( "finished photo " + currentPhotoId + " in " +
                        (System.currentTimeMillis() - photoStartTime) + " ms" );
                log.debug( "Starting photo " + photoId );
                photoStartTime = System.currentTimeMillis();
            }
            // Create the image file corresponding to this row

            ImageFile imgf = ifDao.findImageFileWithHash( rs.getBytes( "i_hash" ) );
            if ( imgf == null ) {
                // The file was not previously known, create
                imgf = new ImageFile();

                imgf.setFileSize( rs.getInt( "i_file_size" ) );
                imgf.setHash( rs.getBytes( "i_hash" ) );
                String instUuid = rs.getString( "i_instance_uuid" );
                if ( instUuid != null ) {
                    imgf.setId( UUID.fromString( instUuid ) );
                } else {
                    imgf.setId( UUID.randomUUID() );
                }
                if ( rs.getInt( "i_is_original" ) == 1 ) {
                    currentOriginal = new OriginalImageDescriptor( imgf, "image#0" );
                    currentOriginal.setHeight( rs.getInt( "i_height" ) );
                    currentOriginal.setWidth( rs.getInt( "i_width" ) );
                } else {
                    CopyImageDescriptor cimg =
                            new CopyImageDescriptor( imgf, "image#0", currentOriginal );
                    ChannelMapOperation cm =
                            ChannelMapOperationFactory.createFromXmlData(
                            rs.getBytes( "i_channel_map" ) );
                    cimg.setColorChannelMapping( cm );
                    Rectangle2D crop = readCropArea( rs, "i_crop_" );
                    cimg.setCropArea( crop );
                    cimg.setHeight( rs.getInt( "i_height" ) );
                    cimg.setWidth( rs.getInt( "i_width" ) );
                    cimg.setRotation( rs.getDouble( "i_rotated" ) );
                    // Does this instance have raw conversion applied?
                    rs.getDouble( "i_whitepoint" );
                    if ( !rs.wasNull() ) {
                        RawConversionSettings r = readRawSettings( rs, "i_" );
                        cimg.setRawSettings( r );
                    }
                // imgDao.makePersistent( cimg );
                }


                ifDao.makePersistent( imgf );
            } else {
                log.debug( "Found existing file with id " + imgf.getId() );
                // TODO: verify that the file matches
                ImageDescriptorBase img = imgf.getImage( "image#0" );
                if ( img instanceof OriginalImageDescriptor ) {
                    /*
                     The existing image is original. Use it for this photo as well
                     */
                    if ( isNewPhoto ) {
                        currentOriginal = (OriginalImageDescriptor) img;
                    } 
                    /*
                     TODO: If we have already different original for this photo,
                     then the previous judgment that this file is an original
                     is wrong. Delete it and modify connected photos to use original
                     of this photo as theri original!!!
                     */
                } else {
                    /*
                     The existing image is a copy.
                     */
                    if ( rs.getInt( "i_is_original" ) == 1 ) {
                        /*
                         The old database image instance is marked as original 
                         but we already know that this is a copy of another image.
                         
                         TODO: calculate all parameters by combining the 
                         transformations made for these two!!!
                         */
                        log.warn( "Resetting original to another image" );
                    } else if ( ((CopyImageDescriptor) img).getOriginal() != currentOriginal ) {
                        /*
                         Not much we can do. This is a known copy of some other image,
                         so for now we just add information of the location to 
                         database. We cannot associate the copy with two originals.                         
                         */
                        log.warn( "Existing copy has different original!!!" );
                    }
                }
            }

            String volName = rs.getString( "i_volume_id" );
            UUID volUuid = volIds.get( volName );
            if ( volUuid != null ) {
                FileLocation fl =
                        new FileLocation( volDao.findById( volUuid, false ),
                        rs.getString( "i_fname" ) );
                imgf.addLocation( fl );
            }


            if ( isNewPhoto ) {
                convertPhotoInfo( rs, rf, currentOriginal, photoDao );
            }
            s.flush();
        }
        s.close();
        try {
            rs.close();
            stmt.close();
            sqlSess.close();
        } catch ( SQLException e ) {
            log.error( e );
            throw e;
        }

    }

   /**
     Creates a PhotoInfo object using the new (0.6.0) data model from old database 
     schema. Field values are read from result set.
     <p>
     The conversion maintains old uuid, and creates 2 change records 
     - one that sets just original and UUID and another that sets the other fields
     to values they have in the old database.
    <p>
    Mapping from old integer photo_id to UUID of the new photo is stored 
    in {@link photoUuids}
     
     @param rs ResultSet from which the field values are read.
     @param rf Resolver factory used when constructing change objects
     @param original Origianl image of the photo
     @param photoDao DAO used to perist photo
     @throws java.oldPhotoQuerySql.SQLException If an error occurs when reading old data.
     */
    private void convertPhotoInfo( ResultSet rs, 
            DTOResolverFactory rf, 
            OriginalImageDescriptor original,
            PhotoInfoDAO photoDao ) throws SQLException {

        // Create photo to match this image
        PhotoInfo photo = null;
        String photoUuidStr = rs.getString( "p_photo_uuid" );
        UUID photoUuid;
        if ( photoUuidStr != null ) {
            log.debug( "Creating photo with uuid " + photoUuidStr );
            photoUuid = UUID.fromString( photoUuidStr );
        } else {
            photoUuid = UUID.randomUUID();
        }        
        VersionedObjectEditor<PhotoInfo> pe;
        try {
            pe = new VersionedObjectEditor<PhotoInfo>( PhotoInfo.class, photoUuid, rf );
        } catch ( InstantiationException ex ) {
            throw new Error( "Cannot instantiate PhotoInfo!!!", ex );
        } catch ( IllegalAccessException ex ) {
            throw new Error( "Cannot access PhotoInfo!!!", ex );
        }
        photo = pe.getTarget();        
        
        photoDao.makePersistent( photo );
        int photoId = rs.getInt( "p_photo_id");
        photoUuids.put(  photoId, photo.getUuid() );

        // First change should contain just information about the original
        pe = new VersionedObjectEditor<PhotoInfo>( photo, rf );
        pe.setField( "original", original );
        pe.apply();

        // Second change sets all other fields
        pe = new VersionedObjectEditor<PhotoInfo>( photo, rf );
        PhotoEditor e = (PhotoEditor) pe.getProxy();
        e.setCamera( rs.getString( "p_camera" ) );
        ChannelMapOperation cm = 
                ChannelMapOperationFactory.createFromXmlData( 
                rs.getBytes( "p_channel_map" ) );
        e.setColorChannelMapping( cm );
        e.setCropBounds( readCropArea( rs, "p_clip_" ) );
        e.setDescription( rs.getString( "p_description" ) );
        e.setFStop( rs.getDouble( "p_f_stop" ) );
        e.setFilm( rs.getString( "p_film" ) );
        e.setFilmSpeed( rs.getInt( "p_film_speed" ) );
        e.setFocalLength( rs.getDouble( "p_focal_length" ) );
        FuzzyDate ft = new FuzzyDate( rs.getTimestamp( "p_shoot_time" ), 
                rs.getDouble( "p_time_accuracy" ) );
        e.setFuzzyShootTime( ft );
        e.setLens( rs.getString( "p_lens" ) );
        e.setOrigFname( rs.getString( "p_orig_fname" ) );
        e.setPhotographer( rs.getString( "p_photographer" ) );
        e.setPrefRotation( rs.getDouble( "p_pref_rotation" ) );
        e.setQuality( rs.getInt( "p_photo_quality" ) );
        e.setShootingPlace( rs.getString( "p_shooting_place" ) );
        rs.getDouble( "p_whitepoint" );
        if ( !rs.wasNull() ) {
            RawConversionSettings r = readRawSettings( rs, "p_" );
            e.setRawSettings( r );
        }
        pe.apply();
    }    
 
    /**
     Creates a rectangle based on crop coordinates from result set
     @param rs The result set from which to read the coordinates
     @param prefix Prefix to determine field names. The field names are of form 
     prefix(x|y)(min|max)
     @return Rectangle based on the coordinates read
     @throws java.oldPhotoQuerySql.SQLException if an error occurs during reading.
     */
    private Rectangle2D readCropArea( ResultSet rs, String prefix ) 
            throws SQLException {
        log.debug(  "entry: readCropArea " + prefix );
        double xmin = rs.getDouble( prefix + "xmin" );
        double xmax = rs.getDouble( prefix + "xmax" );
        double ymin = rs.getDouble( prefix + "ymin" );
        double ymax = rs.getDouble( prefix + "ymax" );
        Rectangle2D crop = 
                new Rectangle2D.Double( xmin, ymin, xmax - xmin, ymax - ymin );
        log.debug(  "crop area " + crop );
        return crop;
    }   
    
    /**
     Reads raw conversions settings from result set queried by 
     {@link oldPhotoQuerySql}
     @param rs Result set containing the settings
     @param prefix Prefix that will be added in front of the column names
     @return Raw conversion settings or <code>null</code> if the settings cannot
     be read (e.g. if some required field is null or some other error occurs)
     */
    private RawConversionSettings readRawSettings( ResultSet rs, String prefix ) {
        
        RawConversionSettings r = null;
        try {
            RawSettingsFactory rsf = new RawSettingsFactory();
            rsf.setBlack( rs.getInt( prefix + "blackpoint" ) );
            rsf.setWhite( rs.getInt( prefix + "whitepoint" ) );
            rsf.setBlueGreenRatio( rs.getDouble( prefix + "b_g_ratio" ) );
            rsf.setRedGreenRation( rs.getDouble( prefix + "r_g_ratio" ) );
            double[] dlm = {rs.getDouble( prefix + "dl_r_g_ratio" ),
                1.0,
                rs.getDouble( prefix + "dl_b_g_ratio" )
            };
            rsf.setDaylightMultipliers( dlm );
            rsf.setHlightComp( rs.getDouble( prefix + "hlight_corr" ) );
            rsf.setEvCorr( rs.getDouble( prefix + "ev_corr" ) );
            r = rsf.create();
        } catch ( PhotovaultException ex ) {
            log.warn( ex );
        } catch ( SQLException ex ) {
            log.warn( ex );
        }
        return r;
    }
    /**
     List of object implementing SchemaUpdateListener that need to be notified 
     about status changes.
     */
    Vector listeners = new Vector();
    
    /**
     Adds a new listener for this update action.
     @param l The listener that will be added.
     */
    public void addSchemaUpdateListener( SchemaUpdateListener l ) {
        listeners.add( l );
    }
    
    /**
     Removes a listener from this action.
     
     @param l The listener that will be removed.
     */
    public void removeSchemaUpdateListener( SchemaUpdateListener l ) {
        listeners.remove( l );
    }

    /**
     Notifies all listeners about a status change.
     
     @param e The event that will be sent to all listeners.
     */
    protected void fireStatusChangeEvent( SchemaUpdateEvent e ) {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            SchemaUpdateListener l = (SchemaUpdateListener) iter.next();
            l.schemaUpdateStatusChanged( e );
        }
    }

    
    /**
     Upgrade to schema version 11 (move raw settings to photos & image_instances
     tables.
     */
    private void upgrade11(Session session) {
        Connection con = session.connection();
        try {
            Statement stmt = con.createStatement();
            String sql = "update photos p, ";
            stmt.executeUpdate( "update photos p, ");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     Add {@link ExternalDir} to folders that correspond to an external volume 
     directory
     @param session
     */
    private void upgrade12( Session session ) {
        throw new UnsupportedOperationException( "Not yet implemented" );
    }
}
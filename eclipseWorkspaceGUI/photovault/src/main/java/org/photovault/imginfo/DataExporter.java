/*
  Copyright (c) 2009 Harri Kaimio

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

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.photovault.folder.PhotoFolder;
import org.photovault.folder.PhotoFolderDAO;
import org.photovault.image.ImageOpChain;
import org.photovault.imginfo.dto.CopyImageDescriptorDTO;
import org.photovault.imginfo.dto.FileLocationDTO;
import org.photovault.imginfo.dto.ImageDescriptorDTO;
import org.photovault.imginfo.dto.ImageFileDTO;
import org.photovault.imginfo.dto.ImageFileDtoResolver;
import org.photovault.imginfo.dto.ImageFileXmlConverter;
import org.photovault.imginfo.dto.OrigImageDescriptorDTO;
import org.photovault.imginfo.dto.OrigImageRefResolver;
import org.photovault.imginfo.dto.PhotoChangeSerializer;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.GenericDAO;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.Change;
import org.photovault.replication.ChangeDTO;
import org.photovault.replication.ChangeFactory;
import org.photovault.replication.DTOResolver;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.FieldConflictBase;
import org.photovault.replication.ObjectHistory;
import org.photovault.replication.ObjectHistoryDTO;
import org.photovault.replication.VersionedObjectEditor;
import org.photovault.replication.XStreamChangeSerializer;
import org.photovault.swingui.PhotoInfoEditor;

/**
 * Export or import the contents of a Photovault database history.
 *
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class DataExporter {

    Log log = LogFactory.getLog( DataExporter.class );

    public void DataExporter() {
    }

    public void exportPhotos( File zipFile, DAOFactory df ) throws FileNotFoundException, IOException {
        PhotoInfoDAO photoDAO = df.getPhotoInfoDAO();
        FileOutputStream os = new FileOutputStream( zipFile );
        ZipOutputStream zipo = new ZipOutputStream( os );
        int photoCount = 0;
        ImageFileDAO ifDao = df.getImageFileDAO();
        List<ImageFile> files = ifDao.findAll();
        ZipEntry filedir = new ZipEntry( "files/" );
        zipo.putNextEntry( filedir );
        for ( ImageFile f : files ) {
            addFileInfo( f, zipo );
        }

        PhotoFolderDAO folderDAO = df.getPhotoFolderDAO();
        exportFolderHierarchy( folderDAO.findRootFolder(), zipo );
        List<PhotoInfo> allPhotos = photoDAO.findAll();
        for ( PhotoInfo p : allPhotos ) {
            String dirName = "photo_" +  p.getUuid() + "/";
            exportHistory( p.getHistory(), dirName, zipo );
            photoCount++;
            log.debug( "" + photoCount + " photos exported" );
        }
        zipo.close();
    }

    /**
     * Export the folder hierarchy below a given folder to externam file
     * @param f The top folder of the hierarchy
     * @param zipo Stream in which the exported data will be written.
     * @throws IOException
     */
    private void exportFolderHierarchy( PhotoFolder f, ZipOutputStream zipo )
            throws IOException {
        ObjectHistory<PhotoFolder> h = f.getHistory();
        String dirName = "folder_" + f.getUuid() + "/";
        exportHistory( h, dirName, zipo );
        for ( PhotoFolder child : f.getSubfolders() ) {
            exportFolderHierarchy( child, zipo);
        }
    }


    /**
     * Import changes from a export file
     * @param is stream used to read the file
     * @param df factory for accessing current database and persistiong new
     * objects
     * @throws IOException
     */
    public void importChanges( ObjectInputStream is, DAOFactory df )
            throws IOException {
        ObjectHistoryDTO dto = null;
        int folderCount = 0;
        int photoCount = 0;
        int totalCount = 0;
        try {
            dto = (ObjectHistoryDTO) is.readObject();
        } catch ( ClassNotFoundException ex ) {
            log.error( ex );
        }
        HibernateDAOFactory hdf = (HibernateDAOFactory) df;
        Session session = hdf.getSession();
        PhotoFolderDAO folderDao = df.getPhotoFolderDAO();
        PhotoInfoDAO photoDao = df.getPhotoInfoDAO();
        DTOResolverFactory rf = df.getDTOResolverFactory();
        ChangeFactory cf = new ChangeFactory( df.getChangeDAO() );
        while ( dto != null ) {
            long startTime = System.currentTimeMillis();
            Transaction tx = session.beginTransaction();
            UUID uuid = dto.getTargetUuid();
            String className = dto.getTargetClassName();

            VersionedObjectEditor e = null;
            if ( className.equals( PhotoFolder.class.getName() ) ) {
                e = getFolderEditor( uuid, folderDao, rf );
                folderCount++;
            } else {
                e = getPhotoEditor( uuid, photoDao, rf );
                photoCount++;
            }
            totalCount++;
            try {
                e.addToHistory( dto, cf );
            } catch ( ClassNotFoundException ex ) {
                log.error( ex );
            }
            session.flush();
            tx.commit();
            session.clear();
            log.debug(  "Imported " + className + " in " + (System.currentTimeMillis()-startTime) + " ms. " +
                    photoCount + " photos, " + folderCount + " folders." );
            // e.apply();
            try {
                dto = (ObjectHistoryDTO) is.readObject();
            } catch ( ClassNotFoundException ex ) {
                log.error( ex );
                return;
            }
        }
    }


    /**
     * Get editor for a folder with given UUID. if the folder is already knwon
     * in current database return editor for it. If it is unknown, create a
     * local instance and return editor for it.
     * @param uuid UUID of the folder
     * @param folderDao DAO for accessing local instances of folders.
     * @param rf Resolver factory for the folders.
     * @return
     */
    private VersionedObjectEditor<PhotoFolder> getFolderEditor(
            UUID uuid, PhotoFolderDAO folderDao, DTOResolverFactory rf ) {
        PhotoFolder target = null;
        log.debug( "getFolderEditor(), uuid " + uuid );
        target = folderDao.findByUUID( uuid );
        VersionedObjectEditor<PhotoFolder> e = null;
        if ( target != null ) {
            log.debug( "getFodlerEditor: folder " + uuid + " found" );
            e = new VersionedObjectEditor( target, rf );
        } else {
            try {
                log.debug( "getFodlerEditor: Creating new folder " + uuid );
                e = new VersionedObjectEditor( PhotoFolder.class, uuid, rf );
                target = e.getTarget();
                folderDao.makePersistent( target );
                folderDao.flush();
            } catch ( InstantiationException ex ) {
                log.error( ex );
            } catch ( IllegalAccessException ex ) {
                log.error( ex );
            }
        }
        return e;
    }

    /**
     * Get editor for a photo with given UUID. If the photo is already knwon
     * in current database return editor for it. If it is unknown, create a
     * local instance and return editor for it.
     * @param uuid UUID of the photo
     * @param photoDao DAO for accessing local instances of photos.
     * @param rf Resolver factory for the photos.
     * @return
     */
    private VersionedObjectEditor<PhotoInfo> getPhotoEditor(
            UUID uuid, PhotoInfoDAO photoDao, DTOResolverFactory rf ) {
        PhotoInfo target = null;
        target = photoDao.findByUUID( uuid );
        VersionedObjectEditor<PhotoInfo> e = null;
        if ( target != null ) {
            e = new VersionedObjectEditor( target, rf );
        } else {
            try {
                e = new VersionedObjectEditor( PhotoInfo.class, uuid, rf );
                target = e.getTarget();
                photoDao.makePersistent( target );
                photoDao.flush();
            } catch ( InstantiationException ex ) {
                log.error( ex );
            } catch ( IllegalAccessException ex ) {
                log.error( ex );
            }
        }
        return e;
    }

    XStreamChangeSerializer ser = new PhotoChangeSerializer();

    public void exportFileInfo( ImageFile f, File sidecar ) throws IOException {
        OutputStream os = new FileOutputStream( sidecar );
        ZipOutputStream zipo = new ZipOutputStream( os );
        ZipEntry filedir = new ZipEntry( "files/" );
        zipo.putNextEntry( filedir );
        addFileInfo( f, zipo );


        for ( Map.Entry<String, ImageDescriptorBase> e : f.getImages().entrySet() ) {
            ImageDescriptorBase img = e.getValue();
            if ( img instanceof OriginalImageDescriptor ) {
                OriginalImageDescriptor orig = (OriginalImageDescriptor) img;
                for ( PhotoInfo p : orig.getPhotos() ) {
                    String dirName = "photo_" + p.getUuid() + "/";
                    exportHistory( p.getHistory(), dirName, zipo );
                }
            }
        }
        zipo.close();
    }

    public void importChanges( File zipFile, DAOFactory df )
            throws FileNotFoundException, IOException {
        FileInputStream is = new FileInputStream( zipFile );
        ZipInputStream zipis = new ZipInputStream( is );

        PhotoInfo p;
        UUID targetId = null;
        VersionedObjectEditor<PhotoInfo> pe = null;
        ObjectHistoryDTO<PhotoInfo> h = null;
        PhotoInfoDAO photoDao = df.getPhotoInfoDAO();
        DTOResolverFactory drf = df.getDTOResolverFactory();
        ChangeFactory<PhotoInfo> cf = new ChangeFactory<PhotoInfo>( df.getChangeDAO() );
        for ( ZipEntry e = zipis.getNextEntry(); e != null ; e= zipis.getNextEntry() ) {
            if ( e.isDirectory() ) {
                continue;
            }
            String ename = e.getName();
            log.debug( "start processing entry " + ename );
            String[] path = ename.split( "/" );
            if ( path.length != 2 ) {
                log.warn( "zip directory hierarchy should have 2 levels: " + ename );
            }
            String fname = path[path.length-1];
            if ( !fname.endsWith( ".xml" ) ) {
                log.warn( "Unexpected suffix: " + ename );
            }
            byte[] data = readZipEntry( e, zipis );

            if ( path[0].equals( "files" ) ) {
                String xml = new String( data, "utf-8" );
                this.parseFileInfo( xml, df );
            } else {
                Class targetObjectClass = null;
                if ( path[0].startsWith( "photo_" ) ) {
                    targetObjectClass = PhotoInfo.class;
                } else if ( path[0].startsWith( "folder_" ) ) {
                    targetObjectClass = PhotoFolder.class;
                } else {
                    log.error( "Illegal folder name found: " + path[0] );
                    continue;
                }

                ChangeDTO dto = ChangeDTO.createChange( 
                        data, targetObjectClass );
                if ( dto == null ) {
                    log.warn( "Failed to read change " + ename );
                    continue;
                }
                UUID changeId = dto.getChangeUuid();
                if ( !fname.startsWith( changeId.toString() ) ) {
                    log.warn( "Unexpected changeId " + changeId + " in file "
                            + ename );
                }

                UUID newTargetId = dto.getTargetUuid();
                if ( !newTargetId.equals( targetId ) ) {
                    if ( h != null ) {
                        addHistory( h, df );
                    }
                    targetId = newTargetId;
                    h = new ObjectHistoryDTO( targetObjectClass, targetId );
                }
                h.addChange( dto );
                targetId = newTargetId;
            }
        }
    }

    /**
     * Reads a zip file entry into byte array
     * @param e The entry to read
     * @param zipis ZipInputStream that is read
     * @return The data in entry
     * @throws IOException
     */

    private byte[] readZipEntry( ZipEntry e, ZipInputStream zipis ) throws IOException {
        long esize = e.getSize();
        byte[] data = null;
        if ( esize > 0 ) {
            data = new byte[(int) esize];
            zipis.read( data );
        } else {
            byte[] tmp = new byte[65536];
            int offset = 0;
            int bytesRead = 0;
            while ( (bytesRead = zipis.read( tmp, offset, tmp.length - offset )) >
                    0 ) {
                offset += bytesRead;
                if ( offset >= tmp.length ) {
                    tmp = Arrays.copyOf( tmp, tmp.length * 2 );
                }
            }
            data = Arrays.copyOf( tmp, offset );
        }
        return data;
    }

    private GenericDAO getDaoForClass( Class clazz, DAOFactory df ) {
        if ( PhotoInfo.class.equals( clazz ) ) {
            return df.getPhotoInfoDAO();
        } else if ( PhotoFolder.class.equals( clazz ) ) {
            return df.getPhotoFolderDAO();
        }
        return null;
    }
    
    private Object findExistingInstance( Class clazz, UUID id, DAOFactory df ) {
        if ( PhotoInfo.class.equals( clazz ) ) {
            PhotoInfoDAO dao = df.getPhotoInfoDAO();
            return dao.findByUUID( id );
        } else if ( PhotoFolder.class.equals( clazz ) ) {
            PhotoFolderDAO dao = df.getPhotoFolderDAO();
            return dao.findByUUID( id );
        }
        throw new IllegalArgumentException(
                "Only PhotoInfo and PhotoFolder are supported, not " +
                clazz.getName() );
    }

    private void addHistory( ObjectHistoryDTO h, DAOFactory df ) {
        log.debug( "entry: addHistory, " + h.getTargetClassName() + " " + h.getTargetUuid() );
        Class targetClass;
        try {
            targetClass = Class.forName( h.getTargetClassName() );
        } catch ( ClassNotFoundException ex ) {
            log.error( ex );
            throw new IllegalStateException(
                    "Cannot find class of change's target", ex );
        }
        GenericDAO dao = getDaoForClass( targetClass, df );
        DTOResolverFactory drf = df.getDTOResolverFactory();
        Transaction tx = null;
        Session s = null;
        if ( df instanceof HibernateDAOFactory ) {
            s = ((HibernateDAOFactory)df).getSession();
            tx = s.beginTransaction();
        }

        Object targetObj = findExistingInstance( targetClass, h.getTargetUuid(), df );
        VersionedObjectEditor pe = null;
        if ( targetObj == null ) {
            log.debug(  "  Target object not found." );
            try {
                pe = new VersionedObjectEditor( h, drf );
                pe.apply();
            } catch ( InstantiationException ex ) {
                log.error( ex );
                if ( tx != null ) tx.rollback();
                return;
            } catch ( IllegalAccessException ex ) {
                log.error( ex );
                if ( tx != null ) tx.rollback();
                return;
            } catch ( ClassNotFoundException ex ) {
                log.error( ex );
                if ( tx != null ) tx.rollback();
                return;
            } catch ( IllegalStateException ex ) {
                log.error( ex );
                if ( tx != null ) tx.rollback();
                return;
            }
            targetObj = pe.getTarget();
            dao.makePersistent( targetObj );
        } else {
            log.debug(  "  Target object found" );
            pe = new VersionedObjectEditor( targetObj, df.getDTOResolverFactory() );
            ObjectHistory hist = pe.getHistory();
            try {
                ChangeFactory cf =
                        new ChangeFactory( df.getChangeDAO() );
                Change oldVersion = hist.getVersion();
                boolean wasAtHead = hist.getHeads().contains( oldVersion );
                pe.addToHistory( h, cf );
                Change newVersion = hist.getVersion();
                Set<Change> newHeads = new HashSet( hist.getHeads() );
                if ( newHeads.size() > 1 ) {
                    log.debug( "  merging heads" );
                    boolean conflictsLeft = false;
                    Change currentTip = null;
                    for ( Change head : newHeads ) {
                        if ( currentTip != null && currentTip != head ) {
                            log.debug( "merging " + currentTip.getUuid() +
                                    " with " + head.getUuid() );
                            Change merged = currentTip.merge( head );
                            if ( !merged.hasConflicts() ) {
                                merged.freeze();
                                log.debug( "merge succesfull, " + merged.getUuid() );
                                currentTip = merged;
                            } else {
                                conflictsLeft = true;
                                if ( log.isDebugEnabled() ) {
                                    StringBuffer conflicts = new StringBuffer( " Conflicts unresolved: \n" );
                                    for ( Object o: merged.getFieldConficts() ) {
                                        // Why does this not work without cast from Object???
                                        FieldConflictBase conflict = (FieldConflictBase) o;
                                        String fieldName = conflict.getFieldName();
                                        conflicts.append( fieldName );
                                        conflicts.append( ": " );
                                        conflicts.append(
                                                currentTip.getField( fieldName) );
                                        conflicts.append( " <-> " );
                                        conflicts.append(
                                                head.getField( fieldName) );
                                    }
                                    log.debug(  conflicts );
                                }
                            }
                        } else {
                            currentTip = head;
                        }
                    }
                    if ( wasAtHead && !conflictsLeft ) {
                        pe.changeToVersion( currentTip );
                    }
                }
                dao.flush();
            } catch ( ClassNotFoundException ex ) {
                log.error( ex );
                if ( tx != null ) tx.rollback();
                return;
            } catch ( IOException ex ) {
                log.error( ex );
                if ( tx != null ) tx.rollback();
                return;
            }
        }
        if ( tx != null ) {
            tx.commit();
            s.clear();
        }
    }

    private void exportHistory( ObjectHistory h, String dirName, ZipOutputStream os ) throws IOException {
        ZipEntry photoDir = new ZipEntry( dirName );
        os.putNextEntry( photoDir );
        ObjectHistoryDTO<PhotoInfo> hdto = new ObjectHistoryDTO<PhotoInfo>( h );
        for ( ChangeDTO ch : hdto.getChanges() ) {
            UUID chId = ch.getChangeUuid();
            String fname = dirName + chId + ".xml";
            ZipEntry chEntry = new ZipEntry( fname );
            os.putNextEntry( chEntry );
            byte[] xml = ch.getXmlData();
            os.write( xml );
        }
    }

    private XStream fileXstream;

    private void addFileInfo( ImageFile f, ZipOutputStream zipo ) throws IOException {
        String fileName = "files/file_" + f.getId().toString()+ ".xml";
        ZipEntry fileEntry = new ZipEntry( fileName );
        zipo.putNextEntry( fileEntry );
        ImageFileDTO dto = new ImageFileDTO( f );
        String xml = getFileXstream().toXML( dto );
        zipo.write( xml.getBytes( "utf-8" ) );
    }

    private void parseFileInfo( String xml, DAOFactory df ) {
        ImageFileDTO dto = (ImageFileDTO) getFileXstream().fromXML( xml );
        if ( dto.getHash() == null && dto.getLocations().isEmpty() ) {
            /*
             * No way to identify the file if we encounter it, so importing
             * it would be meaningless.
             */
            return;
        }
        ImageFileDtoResolver resolver = new ImageFileDtoResolver();
        DTOResolverFactory drf = df.getDTOResolverFactory();
        ImageFileDtoResolver fdr = (ImageFileDtoResolver) drf.getResolver( ImageFileDtoResolver.class );
        Transaction tx = null;
        Session s = null;
        if ( df instanceof HibernateDAOFactory ) {
            s = ((HibernateDAOFactory)df).getSession();
            tx = s.beginTransaction();
        }
        fdr.getObjectFromDto( dto );
        if ( tx != null ) {
            s.flush();
            tx.commit();
            s.clear();
        }
    }

    private synchronized XStream getFileXstream() {
        if ( fileXstream == null ) {
            XStream xstream = new XStream();
            ImageFileXmlConverter c = new ImageFileXmlConverter( xstream.
                    getMapper(), true );
            xstream.registerConverter( c, XStream.PRIORITY_VERY_HIGH );
            xstream.processAnnotations( ImageFileDTO.class );
            xstream.processAnnotations( FileLocationDTO.class );
            xstream.processAnnotations( ImageDescriptorDTO.class );
            xstream.processAnnotations( OrigImageDescriptorDTO.class );
            xstream.processAnnotations( CopyImageDescriptorDTO.class );
            xstream.processAnnotations( OrigImageRefResolver.class );
            ImageOpChain.initXStream( xstream );
            fileXstream = xstream;
        }
        return fileXstream;
    }


}

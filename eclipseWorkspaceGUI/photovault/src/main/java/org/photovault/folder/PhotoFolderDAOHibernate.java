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
package org.photovault.folder;

import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.photovault.persistence.GenericHibernateDAO;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.HibernateDtoResolverFactory;
import org.photovault.replication.VersionedObjectEditor;

/**
 *
 * @author harri
 */
public class PhotoFolderDAOHibernate 
        extends GenericHibernateDAO<PhotoFolder, UUID>
        implements PhotoFolderDAO {
    
    Log log = LogFactory.getLog(  PhotoFolderDAOHibernate.class.getName() );
    
    /** Creates a new instance of PhotoFolderDAOHibernate */
    public PhotoFolderDAOHibernate() {
        super();
    }

    /**
     Find the root folder of folder tree
     @return Root folder in associated persistence context.
     */
    public PhotoFolder findRootFolder() {
        return findById( PhotoFolder.ROOT_UUID, false );
    }

    public PhotoFolder findByUUID(UUID uuid) {
        Query q = getSession().createQuery( "from PhotoFolder where uuid = :uuid" );
        q.setParameter("uuid", uuid );
        return (PhotoFolder) q.uniqueResult();
    }

    public PhotoFolder create( UUID uuid, PhotoFolder parent ) {
        DTOResolverFactory rf = new HibernateDtoResolverFactory( getSession() );
        PhotoFolder folder = new PhotoFolder();
        try {
            VersionedObjectEditor<PhotoFolder> ed = 
                    new VersionedObjectEditor<PhotoFolder>( PhotoFolder.class, uuid, rf );
            FolderEditor fe = (FolderEditor) ed.getProxy();
            fe.reparentFolder( parent );
            ed.apply();
            folder = ed.getTarget();
        } catch ( Exception ex ) {
           log.error( "Cannot create folder", ex );
        }
        makePersistent( folder );
        return folder;
    }

    public PhotoFolder create( String name, PhotoFolder parent ) {
        DTOResolverFactory rf = new HibernateDtoResolverFactory( getSession() );
        PhotoFolder folder = new PhotoFolder();
        try {
            VersionedObjectEditor<PhotoFolder> ed =
                    new VersionedObjectEditor<PhotoFolder>( PhotoFolder.class, UUID.randomUUID(), rf );
            folder = ed.getTarget();
            FolderEditor fe = (FolderEditor) ed.getProxy();
            fe.setName( name );
            fe.reparentFolder( parent );
            ed.apply();
        } catch ( Exception ex ) {
           log.error( "Cannot create folder", ex );
        }
        makePersistent( folder );
        return folder;
    }


}

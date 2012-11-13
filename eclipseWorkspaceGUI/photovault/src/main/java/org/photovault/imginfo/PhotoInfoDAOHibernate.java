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

package org.photovault.imginfo;

import java.util.List;
import java.util.UUID;
import org.hibernate.Query;
import org.photovault.persistence.GenericHibernateDAO;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.HibernateDtoResolverFactory;
import org.photovault.replication.VersionedObjectEditor;

/**
 *
 * @author harri
 */
public class PhotoInfoDAOHibernate 
        extends GenericHibernateDAO<PhotoInfo, UUID> 
        implements PhotoInfoDAO {
    
    /** Creates a new instance of PhotoInfoDAOHibernate */
    public PhotoInfoDAOHibernate() {
        super();
    }

    public List findPhotosWithOriginalHash(byte[] hash) {
        Query q = getSession().createQuery( "from PhotoInfo where hash = :hash" );
        q.setBinary( "hash", hash );
        return q.list();
    }

    public List<PhotoInfo> findPhotosWithHash(byte[] hash) {
        throw new UnsupportedOperationException( "findPhotosWithHash not yet implemented");
    }

    public PhotoInfo findByUUID(UUID uuid) {
        Query q = getSession().createQuery( "from PhotoInfo where uuid = :uuid" );
        q.setParameter("uuid", uuid );
        return (PhotoInfo) q.uniqueResult();        
    }

    public PhotoInfo create() {
        DTOResolverFactory rf = new HibernateDtoResolverFactory( getSession() );
        VersionedObjectEditor<PhotoInfo>pe;
        try {
            pe = new VersionedObjectEditor<PhotoInfo>( PhotoInfo.class, UUID.randomUUID(), rf );
        } catch ( Exception ex ) {
            throw new Error( "Could not create PhotoInfo object", ex );
        }
        PhotoInfo photo = pe.getTarget();
        makePersistent( photo );
        return photo;
    }
    
}

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

package org.photovault.imginfo;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.hibernate.Query;
import org.photovault.common.PhotovaultException;
import org.photovault.persistence.GenericHibernateDAO;

/**
 *
 * @author harri
 */
public class VolumeDAOHibernate 
        extends GenericHibernateDAO<VolumeBase, UUID >
        implements VolumeDAO {
    
    /** Creates a new instance of VolumeDAOHibernate */
    public VolumeDAOHibernate() {
    }
    
    public Volume getDefaultVolume( ) {
        String sql = "select {v.*} from pv_volumes v "+
                "join database_info d on v.volume_id = d.default_volume_id";
        Query q = getSession().createSQLQuery( sql ).addEntity("v", VolumeBase.class );
        return (Volume) q.uniqueResult();
    }

    public VolumeBase getVolumeOfFile(File f) throws PhotovaultException, IOException {
        return VolumeManager.instance().getVolumeOfFile( f, this );
    }

    public VolumeBase getVolume( UUID id ) {
        Query q = getSession().createQuery( "from VolumeBase where id = :id" );
        q.setParameter( "id", id );
        return (VolumeBase) q.uniqueResult();
    }
    
}

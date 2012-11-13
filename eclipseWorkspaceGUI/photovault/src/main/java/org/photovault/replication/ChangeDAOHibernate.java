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

package org.photovault.replication;

import java.util.UUID;
import org.hibernate.Query;
import org.photovault.persistence.GenericHibernateDAO;

/**
 Implementation of {@link ChangeDAO} for Hibernate persistence layer.
 
 @since 0.6
 @author Harri Kaimio
 */
public class ChangeDAOHibernate<T> 
        extends GenericHibernateDAO<Change<T>,UUID>
        implements ChangeDAO<T>
{

    public ChangeDAOHibernate() {
        super();
    }
    
    public ObjectHistory<T> findObjectHistory( UUID id ) {
        Query q = getSession().createQuery( 
                "from ObjectHistory where targetUuid = :uuid" );
        q.setParameter( "uuid", id );
        return (ObjectHistory<T>) q.uniqueResult();
    }

    public Change<T> findChange( UUID id ) {
        Query q = getSession().createQuery( "from Change where uuid = :uuid" );
        q.setParameter( "uuid", id );
        return (Change<T>) q.uniqueResult();
    }

    public void makePersistent( T targetObject ) {
        getSession().saveOrUpdate( targetObject );
    }

}

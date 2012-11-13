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

import org.hibernate.Session;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;

/**
 Base class for DTO resolvers that use Hibernate persistence to look up for
 existing objects. The only addition to {@link DTOResolver} is the addition
 of session field to access Hibernate session.
 
 @since 0.6.0
 @author Harri Kaimio
 @see DTOResolver
 */
public abstract class HibernateDTOResolver<T,D> implements DTOResolver<T,D> {

    /**
     Session used to look up instances
     */
    private Session session;
    
    /**
     Set the session used by this object
     @param s The Hibernate session that will be used
     */
    final public void setSession( Session s ) {
        session = s;
    }
    
    /**
     Get the session that is used for looking up objetcs
     @return The session set by setSession. If this is <code>null</code>, returns
     the current sesson bount to this thread.
     */
    final protected Session getSession() {
        if (session == null)
            session = HibernateUtil.getSessionFactory()
            .getCurrentSession();
        return session;
    }

    private HibernateDAOFactory daoFactory = null;

    final protected DAOFactory getDAOFactory() {
        if ( daoFactory == null ) {
            daoFactory = new HibernateDAOFactory();
            daoFactory.setSession( getSession() );
        }
        return daoFactory;
    }
}

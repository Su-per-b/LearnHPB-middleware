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


package org.photovault.dbhelper;

import java.lang.RuntimeException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.photovault.persistence.HibernateUtil;


/**
   ODMGXAWrapper was a simple wrapper for ODMG transactions. Currently it just 
 checks whether we are in transaction context.
*/

public class ODMGXAWrapper {
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( ODMGXAWrapper.class.getName() );

    public ODMGXAWrapper() {
//        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//        Session session = sessionFactory.getCurrentSession();
//        if ( session == null ) {
//            throw new RuntimeException( "Not in transaction context!!!" );
//        }
    }

    public void commit() {
        // Empty
    }

    public void abort() {
        // Empty
    }

    public void lock( Object obj, int type ) {
    }

    public void flush() {
    }
}

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

package org.photovault.common;

import org.hibernate.cfg.Configuration;

/**
 * Simple interface for objects that can initialize Hibernate configuration
 * for Photovault database.
 * @author Harri Kaimio
 * @since 0.6.0
 */
public interface HibernateInitializer {
    /**
     * Initialize Hibernate configuration object for accessing the database
     * @param username Username used for accessing the database
     * @param passwd Password for the user
     * @return Configuration object that is configured for database access
     * @throws org.photovault.common.PhotovaultException If the configuration
     * object cannot be initialized.
     */    
    public Configuration initHibernate( String username, String passwd )
            throws PhotovaultException;
}

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

import java.io.File;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

/**
 * Database descriptor for Photovault database that uses Derby database engine
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class DerbyDescriptor implements HibernateInitializer {

    /**
     * Directory of the Derby database
     */
    private File dbDir;

    /**
     * Constructor
     */
    public DerbyDescriptor() {

    }

    /**
     * Set the directory where the Derby database is.
     * @param dir The directory
     */
    public void setDirectory( File dir ) {
        dbDir = dir;
    }

    /**
     * Get the Derby database directory
     * @return Directory where the Derby database is stored
     */
    public File getDirectory() {
        return dbDir;
    }

    /**
     * Creates Hibernate configuration. Since Derby does not specify API for
     * setting non-standard database directory, this method also sets the system
     * property "derby.system.home" to point to {@link #dbDir}
     * @param user Must be null
     * @param passwd Must be null
     * @return The Hibernate configuration
     * @throws org.photovault.common.PhotovaultException If user or password is set
     */
    public Configuration initHibernate( String user, String passwd )
            throws PhotovaultException {
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        cfg.configure();
        cfg.setProperty( "hibernate.connection.driver_class",
                "org.apache.derby.jdbc.EmbeddedDriver" );
        cfg.setProperty( "hibernate.dialect",
                "org.hibernate.dialect.DerbyDialect" );
        cfg.setProperty( "hibernate.connection.url",
                "jdbc:derby:photovault;create=true" );
        System.setProperty( "derby.system.home", dbDir.getAbsolutePath() );
        if ( (user != null && user.length() != 0) ||
                (passwd != null && passwd.length() != 0) ) {
            throw new PhotovaultException( "No username or password allowed for Derby database" );
        }
        return cfg;
    }

    @Override
    public boolean equals( Object o ) {
        if ( o == null || ! (o instanceof DerbyDescriptor) ) {
            return false;
        }
        DerbyDescriptor db = (DerbyDescriptor) o;
        return ( dbDir == db.dbDir ) ||
                ( dbDir != null && dbDir.equals( db.dbDir ) );
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.dbDir != null ? this.dbDir.hashCode() : 0);
        return hash;
    }

}

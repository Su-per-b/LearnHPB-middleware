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

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

/**
 * Database descriptor for Photovault database that uses Mysql database engine
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class MysqlDescriptor implements HibernateInitializer {

    /**
     * Default constructor
     */
    public MysqlDescriptor() {}

    /**
     * Mysql database host
     */
    private String host;

    /**
     * Name of the MySql database
     */
    private String dbname;

    /**
     * @return the database host
     */
    public String getHost() {
        return host;
    }

    /**
     * Set name of the database host
     * @param host Name of the database host
     */
    public void setHost( String host ) {
        this.host = host;
    }

    /**
     * @return the Name of the MySql database
     */
    public String getDbname() {
        return dbname;
    }

    /**
     * Set the name of MySql database
     * @param dbname the dbname to set
     */
    public void setDbname( String dbname ) {
        this.dbname = dbname;
    }

    /**
     * Cretes Hibernate configuration for accessing the database.
     * @param username MySql user name
     * @param passwd password for user
     * @return The Hibernate configuration
     * @throws org.photovault.common.PhotovaultException Not thrown by this
     * class.
     */
    public Configuration initHibernate( String username, String passwd )
            throws PhotovaultException {
        Configuration cfg = new AnnotationConfiguration();
        cfg.configure();
        cfg.setProperty( "hibernate.connection.driver_class",
                "com.mysql.jdbc.Driver" );
        cfg.setProperty( "hibernate.dialect",
                "org.hibernate.dialect.MySQLDialect" );
        cfg.setProperty( "hibernate.connection.url",
                "jdbc:mysql://" + host + "/" + dbname );
        cfg.setProperty( "hibernate.connection.username", username );
        cfg.setProperty( "hibernate.connection.password", passwd );
        return cfg;
    }

}

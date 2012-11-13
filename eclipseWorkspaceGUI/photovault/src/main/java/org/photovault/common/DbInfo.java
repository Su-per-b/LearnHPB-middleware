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

package org.photovault.common;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;
import javax.persistence.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.photovault.persistence.HibernateUtil;

/**
 This class represents the database_info structure for the current database
 
 */
@Entity
@Table( name = "database_info" )
public class DbInfo {
    
    static private final Log log = LogFactory.getLog( DbInfo.class.getName() );
    
    /**
     Creates a new instance of DbInfo. THis should <b>not</b> be used by
     any aplication code, it is public only because OJB requires it. Instead,
     use @see getDbInfo method to get the database infor structure of the
     currently open database.
     */
    public DbInfo() {
    }
    
    static DbInfo info = null;
    
    /**
     Returns the current database infor structure of the currently open
     database.
     @deprecated Use {@link #getDbInfo(org.hibernate.Session) } instead.
     */
    static public DbInfo getDbInfo() {
        if ( info == null ) {
            String query = "select info from " + DbInfo.class.getName();

            Session session =
                    HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            info = getDbInfo( session );
            tx.commit();
            session.close();
        }
        return info;
    }

    static public DbInfo getDbInfo( Session s ) {
        DbInfo i = null;
        try {
            List infos = null;
            infos = s.createQuery( "from DbInfo i" ).list();

            if ( infos.size() > 0 ) {
                i = (DbInfo) infos.get( 0 );
            }
        } catch ( Exception e ) {
            /*
            Could not get the database info, most likely because the
            schema is of too old version
             */
            log.warn( e );
        }
        return i;
    }

    /**
     * Get the schema version of current database using SQL query. This is
     * useful in cases in which schema has not been upgraded to latest version,
     * and {@link #getDbInfo() } method cannot construct the DbInfo object.
     * @return Current schema version.
     */
    static int querySchemaVersion() {
            Session session =
                    HibernateUtil.getSessionFactory().openSession();
            SQLQuery q = session.createSQLQuery(
                    "select schema_version from database_info" );
            int v = (Integer) q.uniqueResult();
            session.close();
            return v;
    }

    /**
     Set the revision of database schema.
     @param version the version number.
     */
    public void setVersion( int version ) {
        this.version = version;
    }
    
    /**
     Get schema version this database is based on.
     */
    @Column( name = "schema_version" )
    public int getVersion() {
        return version;
    }
    
    /**
     Get the time when this database was created.
     */
    @Column( name = "create_time" )
    @Temporal(value = TemporalType.TIMESTAMP )
    public Date getCreateTime() {
        return createTime != null ? (Date) createTime.clone() : null;
    }
    
    protected void setCreateTime( Date t ) {
        createTime = t;
    }
    
    /**
     Get the unique ID of thsi database
     */
    @Id
    @Column( name = "database_id")
    public String getId() {
        return id;
    }
    
    protected void setId( String id ) {
        this.id = id;
    }
    
    UUID defVolId;
            
    @Column( name="default_volume_id" )
    @org.hibernate.annotations.Type(type = "org.photovault.persistence.UUIDUserType")
    public UUID getDefaultVolumeId() {
        return defVolId;
    }
    
    public void setDefaultVolumeId( UUID id ) {
        defVolId = id;
    }
    
    private String id;
    private int version = -1;
    private Date createTime = null;
}

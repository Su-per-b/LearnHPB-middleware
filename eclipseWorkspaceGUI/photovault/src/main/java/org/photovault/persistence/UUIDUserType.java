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
package org.photovault.persistence;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
  Hibernate type mapping for UUID. 
 */
public class UUIDUserType implements UserType {
    
    /** Creates a new instance of UUIDUserType */
    public UUIDUserType() {
    }
    
    public int[] sqlTypes() {
        return new int[] { Hibernate.STRING.sqlType() };
    }
    
    public Class returnedClass() {
        return UUID.class;
    }
    
    public boolean equals( Object x, Object y ) throws HibernateException {
        if (x == y) return true;
        if (x == null || y == null) return false;
        return x.equals(y);
    }
    
    public int hashCode(Object object) throws HibernateException {
        return object.hashCode();
    }
    
    public Object nullSafeGet( ResultSet rs, String[] names, Object owner ) 
    throws HibernateException, SQLException {
        String strUUID = rs.getString( names[0] );
        if ( rs.wasNull() ) {
            return null;
        }
        UUID uuid = UUID.fromString( strUUID );
        return uuid;
    }
    
    public void nullSafeSet(PreparedStatement statement, Object value, int index ) 
    throws HibernateException, SQLException {
        if ( value == null ) {
            statement.setNull( index, Hibernate.STRING.sqlType() );
        } else {
            String strUuid = value.toString();
            statement.setString( index, strUuid );
        }
    }
    
    public Object deepCopy(Object object) throws HibernateException {
        return object;
    }
    
    public boolean isMutable() {
        return false;
    }
    
    public Serializable disassemble(Object object) throws HibernateException {
        return (Serializable) object;
    }
    
    public Object assemble(Serializable cached, Object owner) 
    throws HibernateException {
        return cached;
    }
    
    public Object replace( Object original, Object target, Object owner ) 
    throws HibernateException {
        return original;
    }
    
}

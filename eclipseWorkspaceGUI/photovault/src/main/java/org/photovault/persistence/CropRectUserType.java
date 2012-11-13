/*
 * CropRectUserType.java
 *
 * Created on June 27, 2007, 4:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.photovault.persistence;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
/**
 *
 * @author harri
 */
public class CropRectUserType implements CompositeUserType{
    
    /** Creates a new instance of CropRectUserType */
    public CropRectUserType() {
    }

    public String[] getPropertyNames() {
        return new String[] {
            "minX", "maxX", "minY", "maxY"
        };
    }

    public Type[] getPropertyTypes() {
        return new Type[] {
            Hibernate.DOUBLE,
            Hibernate.DOUBLE,
            Hibernate.DOUBLE,
            Hibernate.DOUBLE
        };
    }

    public Object getPropertyValue(Object object, int i) throws HibernateException {
        Rectangle2D r = (Rectangle2D) object;
        switch ( i ) {
            case 0:
                return r.getMinX();
            case 1:
                return r.getMaxX();
            case 2:
                return r.getMinY();
            case 3: 
                return r.getMaxY();
        }
        return null;
    }

    public void setPropertyValue(Object object, int i, Object value) 
    throws HibernateException {
        double v = ((Double)value).doubleValue();
        Rectangle2D r = (Rectangle2D) object;
        double p[] = {r.getMinX(), r.getMaxX(), r.getMinY(), r.getMaxY()};
        p[i] = v;
        r.setRect( p[0], p[1], p[2]-p[0], p[3]-p[1] );
    }

    public Class returnedClass() {
        return Rectangle2D.class;
    }

    public boolean equals(Object x, Object y) 
    throws HibernateException {
        if (x == y) return true;
        if (x == null || y == null) return false;
        return x.equals(y);        
    }

    public int hashCode(Object object) throws HibernateException {
        return object.hashCode();
    }

    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) 
    throws HibernateException, SQLException {
        double x1 = rs.getDouble( names[0]);
        if ( rs.wasNull() ) {
            return null;
        }
        double x2 = rs.getDouble( names[1] );
        double y1 = rs.getDouble( names[2] );
        double y2 = rs.getDouble( names[3] );
        return new Rectangle2D.Double( x1, y1, x2-x1, y2-y1 );
    }

    public void nullSafeSet(PreparedStatement stmt, Object value, int i, SessionImplementor session) 
    throws HibernateException, SQLException {
        if ( value == null ) {
            stmt.setNull( i, Hibernate.DOUBLE.sqlType() );
            stmt.setNull( i+1, Hibernate.DOUBLE.sqlType() );
            stmt.setNull( i+2, Hibernate.DOUBLE.sqlType() );
            stmt.setNull( i+3, Hibernate.DOUBLE.sqlType() );
        } else {
            Rectangle2D r = (Rectangle2D)value;
            stmt.setDouble( i, r.getMinX() );
            stmt.setDouble( i+1, r.getMaxX() );
            stmt.setDouble( i+2, r.getMinY() );
            stmt.setDouble( i+3, r.getMaxY() );
        }
    }

    public Object deepCopy(Object object) throws HibernateException {
        return object != null ? ((Rectangle2D)object).clone() : null;
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object object, SessionImplementor sessionImplementor) throws HibernateException {
        return (Serializable)object;
    }

    public Object assemble(Serializable cached, SessionImplementor sessionImplementor, Object object) throws HibernateException {
        return cached;
    }

    public Object replace(Object orig, Object target, SessionImplementor session, Object owner) throws HibernateException {
        return ((Rectangle2D)orig).clone();
    }
    
}

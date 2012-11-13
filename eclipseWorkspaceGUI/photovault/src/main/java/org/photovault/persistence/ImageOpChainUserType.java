/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.photovault.persistence;

import java.io.Serializable;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.tool.ant.HibernateToolTask;
import org.hibernate.usertype.UserType;
import org.photovault.image.ImageOpChain;

/**
 *
 * @author harri
 */
public class ImageOpChainUserType implements UserType {

    static private int[] types = { Types.CLOB };

    public int[] sqlTypes() {
        return types;
    }

    public Class returnedClass() {
        return ImageOpChain.class;
    }

    public boolean equals( Object x, Object y ) throws HibernateException {
        if ( x == y ) {
            return true;
        }
        if ( x == null || y == null ) {
            return false;
        }
        return x.equals( y );
    }

    public int hashCode( Object obj ) throws HibernateException {
        return obj.hashCode();
    }

    public Object nullSafeGet( ResultSet rs, String[] names, Object owner )
            throws HibernateException, SQLException {
        Clob clob = rs.getClob( names[0] );
        String xml = clob.getSubString( 1,(int) clob.length());
        return ImageOpChain.fromXml( xml );

        
    }

    public void nullSafeSet( PreparedStatement stmt, Object value, int index )
            throws HibernateException, SQLException {
        String xml = "";
        if ( value != null ) {
            xml = ((ImageOpChain)value).getAsXml();
        }
        StringReader r = new StringReader( xml );
        stmt.setCharacterStream( index, r );
    }

    public Object deepCopy( Object obj ) throws HibernateException {
        ImageOpChain chain = (ImageOpChain) obj;
        return new ImageOpChain( chain );
    }

    public boolean isMutable() {
        return true;
    }

    public Serializable disassemble( Object arg0 ) throws HibernateException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public Object assemble( Serializable arg0, Object arg1 ) throws HibernateException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public Object replace( Object orig, Object target, Object owner ) throws HibernateException {
        return new ImageOpChain( (ImageOpChain) orig);
    }

}

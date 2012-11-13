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

package org.photovault.change;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 This is a simple "union" of field values to help persistence layer to store them
 properly.
 */
@Embeddable
public class FieldChangeDesc {
    
    private Object value;

    /**
     Contructor for persistence layer
     */
    protected FieldChangeDesc() {}
    
    /**
     Constructor
     @param value Value of the field
     */
    public FieldChangeDesc( Object value ) {
        this.value = value;
    }
    
    
    /**
     Set the value from persistence layer
     @param v The value fo field
     */
    protected void setStringValue( String v ) {
        if ( v != null ) {
            value = v;
        }
    }
    
    @Column( name = "string_value" )
    public String getStringValue() {
        return ( value instanceof String ) ? (String) value : null;
    }
    
    /**
     Set the value from persistence layer
     @param v The value fo field
     */
    protected void setIntValue( Integer v ) {
        if ( v != null ) {
            value = v;
        }
    }
    
    @Column( name = "int_value" )
    public Integer getIntValue() {
        return ( value instanceof Integer ) ? (Integer) value : null;
    }
    
    /**
     Set the value from persistence layer
     @param v The value fo field
     */
    protected void setDoubleValue( Double v ) {
        if ( v != null ) {
            value = v;
        }
    }
    
    @Column( name = "double_value" )
    public Double getDoubleValue() {
        return ( value instanceof Double ) ? (Double) value : null;
    }
    
    /**
     Set the value from persistence layer
     @param v The value fo field
     */
    protected void setDateValue( Date v ) {
        if ( v != null ) {
            value = v;
        }
    }
    
    @Column( name = "date_value" )
    public Date getDateValue() {
        return ( value instanceof Date ) ? (Date) value : null;
    }
    
    /**
     Set the value of the field
     @param v New value
     */
    public void setValue( Object v ) {
        value = v;
    }
    
    /**
     Get the value of the field
     @return Current value
     */
    @Transient
    public Object getValue() {
        return value;
    }
}

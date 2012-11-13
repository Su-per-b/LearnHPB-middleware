/*
  Copyright (c) 2008-2009 Harri Kaimio
  
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

import java.util.Collections;
import java.util.List;

/**
 Description of a merge conflict that occurs due to conflicting values in a 
 field
 */
public final class ValueFieldConflict extends FieldConflictBase {

    String property;
    List values;
        
    /**
     Constructor.
     @param fc Field change that contains this conflicts
     @param values List of all conflicting values
     */
    ValueFieldConflict( FieldChange fc, List values ) {
        super( fc );
        this.values = values;
    }

    /**
     * Constructor.
     * @param fc Name of the field
     * @param property Name of the conflicting property
     * @param values List of values for the property
     */
    ValueFieldConflict( FieldChange fc, String property, List values ) {
        super( fc );
        this.property = property;
        this.values = values;
    }

    /**
     * Get the conflicting property.
     * @return
     */
    public String getProperty() {
        return property;
    }
    
    /**
     Returns list of the conflicting values. 
     */
    public List getConflictingValues() {
        return Collections.unmodifiableList( values );
    }
    

    /**
     Resolve the conflict by setting field value to the same as it is in one
     of the conflicting changes
     @param winningValue order number of the winning change in the list returned 
     by getConflictingValues
     */
    public void resolve( int winningValue ) {
        Object value = values.get( winningValue );
        if ( property == null ) {
            ((ValueChange) change).setValue( value );
        } else {
            ((ValueChange) change).addPropChange( property, value );
        }
        change.conflictResolved( this );
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append( getFieldName() );
        if ( property != null ) {
            b.append( "." );
            b.append( property );
        }
        b.append( ": [" );
        boolean isFirst = true;
        for ( Object v : values ) {
            if ( !isFirst ) {
                b.append( ", " );                
            } else {
                isFirst = false;
            }
            b.append( v );
        }
        b.append( "]" );
        return b.toString();
    }

}

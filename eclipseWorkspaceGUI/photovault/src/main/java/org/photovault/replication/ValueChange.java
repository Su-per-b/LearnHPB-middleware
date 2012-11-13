/*
  Copyright (c) 2008-2009 Harri Kaimio
 
  This file is part of Photovault.
 
  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.
 
  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even therrore implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.
 
  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

package org.photovault.replication;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 Change to a value field, i.e. field whose value does not contain any substate.
 
 @author Harri Kaimio
 @since 0.6.0
 */
final class ValueChange extends FieldChange implements Externalizable {
    
    static Log log = LogFactory.getLog( ValueChange.class );

    /**
     * Changed properties. The properties are stored with a key that includes
     * also the name of this field, i.e. if the name of the field in ops, then
     * property ops[dcraw].white = 3 is set using call
     * <code>
     * this.getOps( "dcraw" ).setWhite( 3 );
     * </code>
     */
    private SortedMap<String,Object> propChanges = new TreeMap<String,Object>();

    /**
     Default constructor should be used only by serialization.
     */
    public ValueChange() {
        super();
    }
    
    /**
     Constructor
     @param name Name of the field
     @param newValue New value for the field
     */
    public ValueChange( String name, Object newValue ) {
        super( name );
        propChanges.put(  name, newValue );
    }

    /**
     * Constuctor
     * @param name Name of the field
     * @param property Name of the property to change
     * @param newValue New value for property
     */
    public ValueChange( String name, String property, Object newValue ) {
        super( name );
        propChanges.put( name + "." + property, newValue );
    }

    /**
     Returns the new value for changed field. If only some properties of this
     * field has been changed returns <code>null</code>.
     */
    public Object getValue() {
        return propChanges.get(  getName() );
    }

    /**
     * Set the value of this field.
     * @param value New value
     */
    void setValue( Object value ) {
        propChanges.clear();
        propChanges.put( getName(), value );
    }

    /**
     * Get a read-only version of all property changes done to this object.
     * @return
     */
    public SortedMap<String,Object> getPropChanges() {
        return Collections.unmodifiableSortedMap( propChanges );
    }

    /**
     * Add a new property change to the field.
     * @param propName Name of the property
     * @param newValue New value for the property
     */
    void addPropChange( String propName, Object newValue ) {
        // Find all previous attemps to set a part of this property
        String subPartStart = getKeyForProp( propName );
        SortedMap<String,Object> propParts = propChanges.tailMap( subPartStart );


        Set<String> subValueChanges = new HashSet<String>();
        for ( String k : propParts.keySet() ) {
            if ( k.startsWith( subPartStart ) ) {
                subValueChanges.add( k );
            } else {
                break;
            }
        }
        for ( String k : subValueChanges ) {
            propChanges.remove( k );
        }
        propChanges.put( getKeyForProp( propName ), newValue );
    }

    @Override
    public boolean conflictsWith( FieldChange ch ) {
        if ( !(ch instanceof ValueChange) ) {
            return false;
        }
        ValueChange vc = (ValueChange) ch;
        
        Iterator<Map.Entry<String,Object>> thisIter = propChanges.entrySet().iterator();
        Iterator<Map.Entry<String,Object>> thatIter = vc.propChanges.entrySet().iterator();
        /*
         * If either of the property maps contains either 
         * - property with different value or
         * - subproperty of a property that exists in the other map
         * return false
         */
        Map.Entry<String, Object> thisEntry = thisIter.next();
        Map.Entry<String, Object> thatEntry = thatIter.next();
        boolean thisReady = false;
        boolean thatReady = false;
        while ( !thisReady || !thatReady ) {
            String thisKey = thisEntry.getKey();
            String thatKey = thatEntry.getKey();
            boolean moveThis = false;
            boolean moveThat = false;
            try {
            if ( thisKey.equals( thatKey ) ) {
                /*
                 * Same property set in both changes. If the values differ, 
                 * there is a conflict, otherwise move to next element.
                 */
                if ( !thisEntry.getValue().equals( thatEntry.getValue() ) ) {
                    // Same property with different value
                    return true;
                }
                moveThis = moveThat = true;
            } else if ( thatKey.startsWith( thisKey ) ) {
                String subPropName = thatKey.substring( thisKey.length() );
                Object subPropValue = PropertyUtils.getNestedProperty(
                        thisEntry.getValue(), subPropName );
                if ( !thatEntry.getValue().equals( subPropValue ) ) {
                    return true;
                }
                moveThat = true;
            } else if ( thisKey.startsWith( thatKey ) ) {
                String subPropName = thisKey.substring( thatKey.length() );
                Object subPropValue = PropertyUtils.getNestedProperty(
                        thatEntry.getValue(), subPropName );
                if ( !thisEntry.getValue().equals( subPropValue ) ) {
                    return true;
                }
                moveThis = true;
            }
            } catch ( IllegalAccessException ex ) {
                log.error( ex );
                return true;
            } catch ( InvocationTargetException ex ) {
                log.error( ex );
                return true;
            } catch ( NoSuchMethodException ex ) {
                log.error( ex );
                return true;
            }
            if ( !moveThis && !moveThat ) {
                if ( thisKey.compareTo( thatKey ) < 0 ) {
                    moveThis = true;
                } else {
                    moveThat = true;
                }
            }
            if ( moveThis || thatReady ) {
                if ( thisIter.hasNext() ) {
                    thisEntry = thisIter.next();
                } else {
                    thisReady = true;
                }
            }
            if ( moveThat || thisReady ) {
                if ( thatIter.hasNext() ) {
                    thatEntry = thatIter.next();
                } else {
                    thatReady = true;
                }
            }
        }
        return false;
    }

    private boolean isSubProperty( String propName, String subPropName ) {
        if ( subPropName.startsWith( propName ) ) {
            String subPart = subPropName.substring( propName.length() );
            if ( subPart.startsWith( "." ) || subPart.startsWith( "[" ) ||
                    subPart.startsWith( "(" ) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addChange( FieldChange ch ) {
        if ( ch instanceof ValueChange ) {
            ValueChange vc = (ValueChange) ch;
            for ( Map.Entry<String,Object> e : vc.propChanges.entrySet() ) {
                /*
                 * Remove all changes to a subproperty changed by vc, as it
                 * will be overwritten by vc
                 */
                SortedMap<String,Object> subprops =
                        propChanges.subMap( e.getKey(), e.getKey()+"a" );
                Iterator<Map.Entry<String,Object>> iter = subprops.entrySet().iterator();
                while ( iter.hasNext() ) {
                    Map.Entry<String,Object> sub = iter.next();
                    if ( isSubProperty( e.getKey(), sub.getKey() ) ) {
                        iter.remove();
                    }
                }
            }
            propChanges.putAll( vc.propChanges );
        }
    }


    @Override
    public void addEarlier( FieldChange ch ) {
        if ( ! (ch instanceof ValueChange ) ) {
            throw new IllegalArgumentException(
                    "Cannot add " + ch.getClass().getName() + " to ValueChange" );

        }
        ValueChange vc = (ValueChange) ch;
        // Add all properties from ch whose parent has not been set by this change
        Set<String> propsToAdd = new HashSet<String>();
        Iterator<String> thisIter = propChanges.keySet().iterator();
        Iterator<String> thatIter = vc.propChanges.keySet().iterator();
        String parentCandidate = thisIter.next();
        while ( thatIter.hasNext() ) {
            String propCandidate = thatIter.next();
            boolean isSubProp = isSubProperty( parentCandidate, propCandidate );
            boolean isPastParents = parentCandidate.compareTo( propCandidate ) > 0;
            while ( thisIter.hasNext() && !isSubProp && !isPastParents ) {
                parentCandidate = thisIter.next();
                isSubProp = isSubProperty( parentCandidate, propCandidate );
                isPastParents = parentCandidate.compareTo( propCandidate ) > 0;
            }
            if ( !isSubProp ) {
                propsToAdd.add( propCandidate );
            }
        }
        for ( Map.Entry<String, Object> e : vc.propChanges.entrySet() ) {
            if ( propsToAdd.contains( e.getKey() ) ) {
                propChanges.put( e.getKey(), e.getValue() );
            }
        }
    }

    Object getSubProperty( String propName ) 
            throws IllegalAccessException, InvocationTargetException, 
            NoSuchMethodException {
        String key = (propName != null && propName.length() > 0 ) ? 
            name + "." + propName : name;
        if ( propChanges.containsKey( key ) ) {
            return propChanges.get( key );
        }
        int lastPartStart = propName.lastIndexOf( "." );
        Object p = null;
        if ( lastPartStart >= 0 ) {
            p = getSubProperty( propName.substring( 0, lastPartStart ) );
        } else {
            p = propChanges.get( name );
        }
        if ( p != null ) {
            return PropertyUtils.getNestedProperty(
                    p, propName.substring( lastPartStart + 1 ) );
        }
        return null;
    }

    @Override
    public FieldChange merge( FieldChange ch ) {
        if ( ! ( ch instanceof ValueChange ) ) {
            throw new IllegalArgumentException( 
                    "Cannot merge " + ch.getClass().getName() + " to ValueChange" );
        }
        ValueChange vc = (ValueChange) ch;
        ValueChange ret = new ValueChange();
        ret.name = this.getName();
        Set<String> keys = new HashSet<String>(propChanges.keySet() );
        keys.addAll( vc.propChanges.keySet() );
        for ( String key : keys ) {
            String prop = getPropName( key );
            Object thisVal = null;
            Object thatVal = null;
            try {
                thisVal = getSubProperty( prop );
                thatVal = vc.getSubProperty( prop );
            } catch ( IllegalAccessException ex ) {
                log.error( ex );
                return null;
            } catch ( InvocationTargetException ex ) {
                log.error( ex );
                return null;
            } catch ( NoSuchMethodException ex ) {
                log.error( ex );
                return null;
            }
            if ( thisVal == null ) {
                ret.propChanges.put( key, thatVal );
            } else if ( thatVal == null ) {
                ret.propChanges.put( key, thisVal );
            } else if ( thisVal.equals( thatVal ) ) {
                ret.propChanges.put( key, thisVal );
            } else {
                List conflicts = new ArrayList( 2 );
                conflicts.add( thisVal );
                conflicts.add( thatVal );
                ret.addConflict( new ValueFieldConflict( ret, prop, conflicts ) );
            }
        }
        return ret;
    }

    @Override
    public FieldChange getReverse( Change baseline ) {
        ValueChange ret = new ValueChange();
        ret.name = name;
        for ( String key: propChanges.keySet() ) {
            String propName = getPropName( key );
            for ( Change c = baseline ; c != null ; c = c.getPrevChange() ) {
                ValueChange fc = (ValueChange) c.getFieldChange( name );
                if ( fc != null ) {
                    try {
                    Object prevValue = fc.getSubProperty( propName );
                    if ( prevValue != null ) {
                        ret.addPropChange( propName, prevValue );
                        break;
                    }
                    } catch ( IllegalAccessException ex ) {
                        log.error( ex );
                        throw new IllegalStateException(
                                "Cannot query property " + propName +
                                " in ValueChange.getReverse()", ex);
                    } catch ( InvocationTargetException ex ) {
                        log.error( ex );
                        throw new IllegalStateException(
                                "Cannot query property " + propName +
                                " in ValueChange.getReverse()", ex);
                    } catch ( NoSuchMethodException ex ) {
                        log.error( ex );
                        throw new IllegalStateException(
                                "Cannot query property " + propName +
                                " in ValueChange.getReverse()", ex);
                    }
                }
            }
        }
        return ret;
    }    

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append( name ).append(  "\n" );
        for ( Map.Entry<String,Object> e : propChanges.entrySet() ) {
            buf.append( getPropName( e.getKey() ) ).append( ":" ).append( e.getValue() );
        }
        return buf.toString();
    }

    public void writeExternal( ObjectOutput out ) throws IOException {
        out.writeObject( name );
        out.writeObject( getValue() );
    }

    public void readExternal( ObjectInput in ) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        setValue( in.readObject() );
    }

    String getPropName( String key ) {
        if ( !key.startsWith( name ) ) {
            return null;
        }
        if ( key.length() > name.length() ) {
            return key.substring( name.length() + 1 );
        }
        return "";
    }

    String getKeyForProp( String propName ) {
        if ( propName == null || propName.length() == 0 ) {
            return getName();
        }
        return getName() + "." + propName;
    }
}

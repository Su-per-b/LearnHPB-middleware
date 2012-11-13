/*
  Copyright (c) 2008 Harri Kaimio
 
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;

/**
 Description of a versioned field with single value (i.e. no sub-state)

 @author Harri Kaimio
 @sice 0.6.0
 */
class ValueFieldDesc extends FieldDesc {

    /**
     Method used to set the value of this field
     */
    public Method setter;

    /**
     Default constructor
     */
    ValueFieldDesc() {
        super();
    }

    /**
     Constructor
     @param clDesc Descriptor for the class that contains this field
     @param getMethod Method used to get value of the field in described class
     @param editorIntf Editor proxy interface for the class
     @throws java.lang.NoSuchMethodException If no suitable method for setting
     field value is found.
     */
    ValueFieldDesc( VersionedClassDesc clDesc, 
            Method getMethod, Class editorIntf ) throws NoSuchMethodException {
        this.clDesc = clDesc;
        ValueField ann = getMethod.getAnnotation( ValueField.class );
        name = ann.field();
        String methodNameBase = null;
        if ( name.equals( "" ) ) {
            String getMethodName = getMethod.getName();
            int nameStart = 0;
            if ( getMethodName.startsWith( "get" ) ) {
                nameStart = 3;
            }
            name = getMethodName.substring( nameStart, nameStart+1 ).toLowerCase() + 
                    getMethodName.substring( nameStart+1 );
            methodNameBase = getMethodName.substring( nameStart );
        } else {
            methodNameBase = name.substring( 0,1 ).toUpperCase() + name.substring( 1 );
        }
        
        clazz = getMethod.getReturnType();
        
        dtoResolverClass = ann.dtoResolver();
        
        getter = getMethod;
        
        String setMethodName = ann.setMethod();
        if ( setMethodName.equals( "" ) ) {
            setMethodName = "set" + methodNameBase;
        }
        
        setter = clDesc.getDescribedClass().getMethod( setMethodName, clazz );
        
        // Add handlers for proxy methods
        if ( editorIntf != null ) {
            try {
                Method editorSetter = editorIntf.getMethod( setMethodName, clazz );
                clDesc.setEditorMethodHandler( 
                        editorSetter, new ProxyMethodHandler( this ) {

                    @Override
                    Object methodInvoked(  VersionedObjectEditor e,
                            Object[] args ) {
                        e.setField( fd.name, args[0] );
                        return null;
                    }
                } );
            } catch ( NoSuchMethodException ex ) {
            }

        }
        
    }
    
    /**
     Applies a change of this field change to an object
     @param target The object that is modified
     @param ch The change wih new value for the field
     @param resolverFactory Resolver factory to be used
     */
    @Override
    void applyChange( Object target, FieldChange ch,
            DTOResolverFactory resolverFactory ) {
        for ( Map.Entry<String, Object> e :
            ((ValueChange) ch).getPropChanges().entrySet() ) {
                String propName = e.getKey();
            try {
                if ( propName.equals(  ch.getName() ) ) {
                    DTOResolver resolver = resolverFactory.getResolver(
                            dtoResolverClass );
                    Object fieldVal =
                            resolver.getObjectFromDto( e.getValue() );
                    setter.invoke( target, fieldVal );
                } else {
                    applyPropertyChange( target, propName, e.getValue() );
                }
            } catch ( IllegalAccessException ex ) {
                throw new IllegalStateException( "Cannot access setter", ex );
            } catch ( InvocationTargetException ex ) {
                throw new IllegalStateException(
                        "InvocationTargetException while setting field", ex );
            } catch ( NoSuchMethodException ex ) {
                throw new IllegalStateException(
                        "No suitable method for setting " + propName, ex );
            } catch ( Exception ex ) {
                throw new IllegalStateException(
                        "Unexpected problem setting " + propName, ex );
            }
        }

    }

    /**
     * Change a certain property of the target obejct
     * @param target Object that will be changed
     * @param propName Property to change
     * @param newValue New value for the property
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    void applyPropertyChange( Object target, String propName, Object newValue )
            throws IllegalAccessException,
            InvocationTargetException,
            NoSuchMethodException {
        PropertyUtils.setProperty( target, propName, newValue);
    }

}

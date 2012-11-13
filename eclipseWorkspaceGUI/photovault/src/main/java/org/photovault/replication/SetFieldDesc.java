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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 Descriptor of version controlled field with set semantics
 
 @author Harri Kaimio
 @since 0.6.0
 */
class SetFieldDesc extends FieldDesc {

    static private Log log = LogFactory.getLog( SetFieldDesc.class.getName() );
    
    /**
     Method in target class that is used to add new element to the set
     */
    private Method addMethod;
    
    /**
     Method in target class that is used to remove an element to the set
     */
    private Method removeMethod;
        
    /**     
     @param clDesc Class descriptor for the class that owns this field
     @param getMethod Method used to get value of the field (the whole set) in 
     owner class
     @param editorIntf Proxy editor interface for owner class
     @throws java.lang.NoSuchMethodException If no suitable method for adding or
     deleting set items is found.
     */
    SetFieldDesc( VersionedClassDesc clDesc, Method getMethod, Class editorIntf ) 
            throws NoSuchMethodException {
        SetField ann = getMethod.getAnnotation( SetField.class );
        
        name = ann.field();
        String methodNameBase = null;
        if ( name.equals( "" ) ) {
            String getMethodName = getMethod.getName();
            int nameStart = 0;
            int nameEnd = getMethodName.length();
            if ( getMethodName.startsWith( "get" ) ) {
                nameStart = 3;
            }
            if ( getMethodName.endsWith( "s" ) ) {
                nameEnd--;
            }
            name = getMethodName.substring( nameStart, nameStart+1 ).toLowerCase() + 
                    getMethodName.substring( nameStart+1 );
            methodNameBase = getMethodName.substring( nameStart, nameEnd );
        }
        
        Class elementType = ann.elemClass();
        clazz = elementType;
        
        dtoResolverClass = ann.dtoResolver();
        
        // Adder method
        String addMethodName = ann.addMethod();
        if ( addMethodName.equals( "" ) ) {
            addMethodName = "add"+ methodNameBase;            
        }
        addMethod = clDesc.getDescribedClass().
                getMethod( addMethodName, elementType );
        
        // Remover method
        String removeMethodName = ann.removeMethod();
        if ( removeMethodName.equals( "" ) ) {
            removeMethodName = "remove"+ methodNameBase;           
        }
        removeMethod = clDesc.getDescribedClass().
                getMethod( removeMethodName, elementType );
        
        // Add handlers for proxy methods
               // Add handlers for proxy methods
        if ( editorIntf != null ) {
            try {
                Method editorAddMethod = 
                        editorIntf.getMethod( addMethodName, clazz );
                clDesc.setEditorMethodHandler( 
                        editorAddMethod, new ProxyMethodHandler( this ) {

                    @Override
                    Object methodInvoked(  VersionedObjectEditor e,
                            Object[] args ) {
                        e.addToSet( fd.name, args[0] );
                        return null;
                    }
                } );
            } catch ( NoSuchMethodException ex ) {
                log.warn( "No method " + addMethodName + 
                        " found in editory interface" );
            }
            try {
                Method editorRemoveMethod = 
                        editorIntf.getMethod( removeMethodName, clazz );
                clDesc.setEditorMethodHandler(
                        editorRemoveMethod, new ProxyMethodHandler( this ) {

                    @Override
                    Object methodInvoked(  VersionedObjectEditor e,
                            Object[] args ) {
                        e.removeFromSet( fd.name, args[0] );
                        return null;
                    }
                } );
            } catch ( NoSuchMethodException ex ) {
                log.warn( "No method " + removeMethodName + 
                        " found in editory interface" );
            }
        }

        
    }

    /**
     Apply change to this field in given object
     @param target The object that will be changed
     @param ch Change to this field
     @param resolverFactory DTO resolver factory used
     */
    @Override
    void applyChange(  Object target, FieldChange ch,
            DTOResolverFactory resolverFactory ) {
        SetChange sc = (SetChange) ch;
        DTOResolver resolver = resolverFactory.getResolver( dtoResolverClass );
        try {
            for ( Object itemDto : sc.getAddedItems() ) {
                Object item = resolver.getObjectFromDto( itemDto );
                addMethod.invoke( target, item );
            }
            for ( Object itemDto : sc.getRemovedItems() ) {
                Object item = resolver.getObjectFromDto( itemDto );
                removeMethod.invoke( target, item );
            }
        } catch ( IllegalAccessException ex ) {
            log.error( ex );
            throw new IllegalStateException( "Invalid set handling method", ex );
        } catch ( IllegalArgumentException ex ) {
            log.error( ex );
            throw new IllegalStateException( "Invalid set handling method", ex );
        } catch ( InvocationTargetException ex ) {
            log.error( ex );
            throw new IllegalStateException( "Invalid set handling method", ex );
        }


    }

}

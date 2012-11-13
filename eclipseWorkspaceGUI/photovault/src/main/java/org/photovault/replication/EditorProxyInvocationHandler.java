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

package org.photovault.replication;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 Handler for method invocations in editor of versioned objects.
 
 @since 0.6.0
 @author Harri Kaimio
 */
public class EditorProxyInvocationHandler implements InvocationHandler {

    /**
     Editor of the object
     */
    VersionedObjectEditor editor;
    /**
     Class descriptor for the object being edited.
     */
    VersionedClassDesc classDesc;
    
    /**
     Constructor
     @param e
     @param classDesc
     */
    public EditorProxyInvocationHandler( 
            VersionedObjectEditor e, VersionedClassDesc classDesc ) {
        editor = e;
        this.classDesc = classDesc;
    }

    
    /**
     Handles method invocation of the proxy object.
     @param proxy
     @param method
     @param args
     @return
     @throws java.lang.Throwable
     */
    public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
        ProxyMethodHandler h = classDesc.getEditorMethodHandler( method );
        if ( h != null ) {
            return h.methodInvoked( editor, args);
        }
        return null;
    }
}

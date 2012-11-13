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

/**
 Base class for objetcs used to handle calls to editor proxies' methods
 
 @author Harri Kaimio
 @since 0.6.0
 
 */
abstract class ProxyMethodHandler {
    
    /**
     Descriptor for the field that is affected by this method
     */
    protected FieldDesc fd;
    
    /**
     Constructor
     @param fd Descriptor of the field that is affected by the method handled 
     by this object
     */
    ProxyMethodHandler( FieldDesc fd ) {
        this.fd = fd;
    }

    /**
     This method is called y {@link EditorProxyInvocationHandler} when the 
     associated method of editor proxy interface is called
     @param editor {@link VersionedObjectEditor} resoponsible for the edited 
     obejct
     @param args Arguments of the editor proxy method call
     @return The return value for editor proxy call
     */
    abstract Object methodInvoked( VersionedObjectEditor editor, Object[] args );
}

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

import java.lang.reflect.Method;

/**
 Description of a single field of versioned class.
 
 @author Harri Kaimio
 @since 0.6.0
 */
abstract class FieldDesc {

    /**
     Name of the field
     */
    public String name;
    
    /**
     Method used to get current value of the field
     */
    public Method getter;
    
    /**
     Type of this field
     */
    public Class clazz;
    
    /**
     Class used to convert between the field value and its DTO representation
     */
    public Class dtoResolverClass;    
    
    /**
     Class descriptor that owns this field
     */
    public VersionedClassDesc clDesc;

    /**
     Default constructor
     */
    FieldDesc() {
        super();
    }

    /**
     Constructor
     @param clDesc Class descriptor
     @param name Name of the field
     @param fieldClass Type of the field
     @param dtoResolverClass DTO resover for the field
     */
    FieldDesc( VersionedClassDesc clDesc, String name, Class fieldClass, Class dtoResolverClass  ) {
        this.name = name;
        this.clDesc = clDesc;
        clazz = fieldClass;
        this.dtoResolverClass = dtoResolverClass;
    }
    
    /**
     Apply a change on this field to given object
     @param target The object that is modified
     @param ch Field change to be applied
     @param resolverFactory FTO resolver factory used to get DTO resolver if 
     needed
     */
    abstract void applyChange( Object target, FieldChange ch,
            DTOResolverFactory resolverFactory );
}

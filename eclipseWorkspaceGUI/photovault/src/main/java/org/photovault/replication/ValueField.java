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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 Annotation used to indicate a field with single value
 @author Harri Kaimio
 @since 0.6.0
 */
@Retention( RetentionPolicy.RUNTIME )
@Target(ElementType.METHOD)
public @interface ValueField {
    
    /**
     Name of the field. If not defined, deduce field name from getter method
     */
    String field() default "";
    
    /**
     Name of method used to set value of field. If undefined, deduce method name 
     from field name using Java Beans semantics.
     */
    String setMethod() default "";
    
    /**
     {@link DTOResolver} used with this field
     */
    Class dtoResolver() default DefaultDtoResolver.class;

}

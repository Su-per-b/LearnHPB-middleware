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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 Annotation to indicate that a class is intended to be versioned using the
 replication logic.
 @since 0.6.0
 @author Harri Kaimio
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface Versioned {
    /**
     Interface used to edit the versioned object. THis interface is used to 
     construct the proxy returned by VersionedObjectEditor#getProxy()
     @return
     */
    Class editor();

    /**
     * {@link ChangeSerializer} that is used for serializing the chages related
     * to this class. The default serializer uses XStream to generate XML with
     * XStream's default conversion rules for everything else except the replication
     * related classes themselves, which have custom converters.
     * <p>
     * This parameter can be used to select another method for serializing changes.
     * e.g. default Java serialization method.
     * @return
     */
    Class changeSerializer() default XStreamChangeSerializer.class;
}

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
 * Interface that defines the methods needed for serializing and deserializing
 * changes of certain class of objects. As the changes are represented by their
 * serialized form (<strong>not</strong> the Java object!) different applications
 * may have different expecations for how this serialization is done in practice.
 * <p>
 * User serializer is defined with the {@link Versioned} annotation's {@link
 * Versioned#changeSerializer() } field. and is specific to certain class of
 * objects. If the serializer is not defined, {@link XStreamChangeSerializer} is
 * used as default.
 * @author Harri Kaimio
 * @since 0.6.0
 */
public interface ChangeSerializer {

    /**
     * Serialize a DTO to byte stream
     * @param dto The DTO to be serialized
     * @return dto's serialized form
     */
    byte[] serializeChange( ChangeDTO dto );


    /**
     * Construct DTO from its serialized form
     * @param serialized The serialized form
     * @return DTO constructed from serialized form.
     */
    ChangeDTO deserializeChange( byte[] serialized );

}

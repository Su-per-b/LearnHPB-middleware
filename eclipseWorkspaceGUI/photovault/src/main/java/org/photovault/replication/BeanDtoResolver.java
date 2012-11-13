/*
  Copyright (c) 2009 Harri Kaimio

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

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Resolver used for creating changes for fields that are handled as Java beans.
 * The difference to {@link DefaultDtoResolver} is that the bean is deep copied
 * to the DTO, as the bean in target object is not immutable.
 *
 * @since 0.6.0
 * @author Harri Kaimio
 */
public class BeanDtoResolver<T> implements DTOResolver<T,T> {

    public T getObjectFromDto( T dto ) {
        try {
            return (T) BeanUtils.cloneBean( dto );
        } catch ( IllegalAccessException ex ) {
            throw new IllegalStateException( ex );
        } catch ( InstantiationException ex ) {
            throw new IllegalStateException( ex );
        } catch ( InvocationTargetException ex ) {
            throw new IllegalStateException( ex );
        } catch ( NoSuchMethodException ex ) {
            throw new IllegalStateException( ex );
        }
    }

    public T getDtoFromObject( T object ) {
        try {
            return (T) BeanUtils.cloneBean( object );
        } catch ( IllegalAccessException ex ) {
            throw new IllegalStateException( ex );
        } catch ( InstantiationException ex ) {
            throw new IllegalStateException( ex );
        } catch ( InvocationTargetException ex ) {
            throw new IllegalStateException( ex );
        } catch ( NoSuchMethodException ex ) {
            throw new IllegalStateException( ex );
        }
    }

}

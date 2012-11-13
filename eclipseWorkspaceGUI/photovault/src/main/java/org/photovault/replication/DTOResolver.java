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

/**
 Interface that must be implemented by objects that are used to convert data 
 transfer objects used in change descriptors to persistent objects and vice 
 versa.
 
 @param T Type of the persistent object
 @param D Type of the DTO
 
 @author Harri Kaimio
 @since 0.6.0
 */
public interface DTOResolver<T, D> {
    T getObjectFromDto( D dto );
    
    D getDtoFromObject( T object );

}

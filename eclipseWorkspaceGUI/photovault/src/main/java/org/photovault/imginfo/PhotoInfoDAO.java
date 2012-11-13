/*
  Copyright (c) 2007 Harri Kaimio
  
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

package org.photovault.imginfo;

import java.util.List;
import java.util.UUID;
import org.photovault.persistence.GenericDAO;

/**
 *
 * @author harri
 */
public interface PhotoInfoDAO extends GenericDAO<PhotoInfo, UUID> {
    
    /**
     Find photo with given UUID
     @param uuid UUID to look for
     @return The photo with given UUID or <code>null</code> if not found
     */
    PhotoInfo findByUUID( UUID uuid );
    
    /**
     Find photos whose original instance file has a given MD5 hash
     @deprecated Use {@link ImageFileDAO} instead
     @param hash The hash code to search for
     @param List of matching photos
     */
    List findPhotosWithOriginalHash( byte[] hash );

    /**
     Find photos that have any instance matching a given hash code
     @param hash The hash code to search for
     @param List of matching photos     
     */
    List findPhotosWithHash( byte[] hash );
    
    /**
     Creates a new persistent photo with random UUID
     @return
     */
    PhotoInfo create();
    
}

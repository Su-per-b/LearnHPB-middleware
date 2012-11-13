/*
  Copyright (c) 2007 Harri Kaimio
 
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

package org.photovault.imginfo;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.photovault.common.PhotovaultException;
import org.photovault.persistence.GenericDAO;

/**
 *
 * @author harri
 */
public interface VolumeDAO extends GenericDAO<VolumeBase, UUID> {

    /**
     * Get a volume with given ID
     * @param id UUID of the volume
     * @return The volume with given ID of <code>null</code> if it is not known
     */
    VolumeBase getVolume( UUID id );

    /**
     Get the default volume for current database
     */
    Volume getDefaultVolume();
    
    /**
     Get the volume in which a given file belongs
     @param f
     @return Volume in which f belongs or <code>null</code> if it does not belong
     to any volume.     
     */
    VolumeBase getVolumeOfFile( File f ) throws PhotovaultException, IOException;
    
}

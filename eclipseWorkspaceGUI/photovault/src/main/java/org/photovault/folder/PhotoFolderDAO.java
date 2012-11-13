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

package org.photovault.folder;

import java.util.UUID;
import org.photovault.persistence.GenericDAO;

/**
 Data access object for doing persistence operations for {@link PhotoFolder}.
 */
public interface PhotoFolderDAO extends GenericDAO<PhotoFolder, UUID> {
    
    /**
     Find the root folder
     @return Root folder of local folder hierarchy.
     */
    public PhotoFolder findRootFolder();
    
    /**
     Find a folder with a specific uuid
     @param uuid UUID to look for
     @return The folder with given UUID or <code>null</code> if none is found.
     */
    public PhotoFolder findByUUID( UUID uuid );
    
    /**
     Creates a new folder that belongs to the current data access context
     @param uuid UUID of the created object
     @param parent Parent folder
     @return Folder with given uuid and parent that is associated with the 
     context of this DAO.
     */
    public PhotoFolder create( UUID uuid, PhotoFolder parent );
    
    /**
     Creates a new folder that belongs to the current data access context
     
     @param name Name of the folder
     @param parent Parent folder
     @return
     */
    public PhotoFolder create( String name, PhotoFolder parent );
    
    
    
}

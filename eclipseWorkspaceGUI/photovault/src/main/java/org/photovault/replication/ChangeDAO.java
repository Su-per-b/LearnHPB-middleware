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

import java.util.UUID;
import org.photovault.persistence.GenericDAO;

/**
 Data access object interface for managing persistenc {@link Change} instances
 */
public interface ChangeDAO<T> 
        extends GenericDAO<Change<T>,UUID> {
    
    /**
     Find the change history of the given object
     */
    ObjectHistory<T> findObjectHistory( UUID id );
    
    /**
     Find change with given ID. Unlike the standard findById method this method 
     is guaranteed to check whether object with given ID exists in database and
     not to return an proxy for nonexistent object.
     @param id UUID of the change
     @return Change if it is found from local context, <code>null</code> 
     otherwise.
     */
    Change<T> findChange( UUID id );
        
    /**
     Make the target object og a change persistent
     @param targetObject
     */
    void makePersistent( T targetObject );
}

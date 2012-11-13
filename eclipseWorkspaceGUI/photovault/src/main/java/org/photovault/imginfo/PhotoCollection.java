/*
  Copyright (c) 2006 Harri Kaimio
  
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
import org.hibernate.Session;

/**
   Interface to access a collection of photos, like folder or query result set
*/
public interface PhotoCollection {
    /**
       returns the number of photos in this collection
    */
    public int getPhotoCount();
    /**
       Get a single hpto from the collection
       @param numPhoto Number of the photo to retrieve. This must be >= 0 and < than
       the number of photos in collection.
    */
    public PhotoInfo getPhoto( int numPhoto );

    /**
       Adds a new listener that will be notified of changes to the collection
    */
    public void addPhotoCollectionChangeListener( PhotoCollectionChangeListener l );


    /** Remove a listener
     */
    public void removePhotoCollectionChangeListener( PhotoCollectionChangeListener l );
    
    /**
     Get instalces for photos in this collection associated to given persistence
     context. Derived classes should run a query in the session scope and return
     the query results.
     @param session The session
     @return List of PhotoInfo objects returend from the query.
     */
    public List<PhotoInfo> queryPhotos( Session session );
}

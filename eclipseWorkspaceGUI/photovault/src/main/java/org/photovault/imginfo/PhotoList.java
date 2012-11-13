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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hibernate.Session;

/**
  Simple list of photos that implements the PhotoCollection interface.
 */
public class PhotoList implements PhotoCollection {
    
    /**
     Photos in this collection
     */
    private ArrayList<PhotoInfo> photos;
    
    /**
     Default constructor
     */
    public PhotoList() {
        photos = new ArrayList<PhotoInfo>();
    }
    
    /** 
     Creates a new instance of PhotoList 
     @param photos The photos initially stored in this object
     */
    public PhotoList( PhotoInfo[] photos )  {
        this.photos = new ArrayList<PhotoInfo>( photos.length );
        for ( PhotoInfo p : photos ) {
            this.photos.add( p );
        }        
    }

    /** 
     Creates a new instance of PhotoList 
     @param photos The photos initially stored in this object
     */
    public PhotoList( Collection photos )  {
        this.photos = new ArrayList<PhotoInfo>( photos );
    }

    
    /**
     Count the photos in this collection
     @return Number of photos.
     */
    public int getPhotoCount() {
        return photos.size();
    }

    /**
     Get nth photo from the collection
     @param numPhoto order umber of the photo
     @return Photo
     @throws IndexOutOfBoundsException if n < 0 or n >= getPhotoCount()
     */
    public PhotoInfo getPhoto(int numPhoto) {
        return photos.get( numPhoto );
    }

    /**
     Add a new photo as the last element of the collection
     @param p Photo to add
     */
    public void addPhoto( PhotoInfo p ) {
        photos.add( p );
        fireChangeEvent();
    }
    
    /**
     Listeners that are notified about changes to the collection
     */
    private ArrayList<PhotoCollectionChangeListener> listeners = 
            new ArrayList<PhotoCollectionChangeListener>();
    
    /**
     Add a new listener
     @param l to add
     */
    public void addPhotoCollectionChangeListener(PhotoCollectionChangeListener l) {
        listeners.add( l );
    }


    /**
     Remove an existing listener
     @param l to remove
     */
    public void removePhotoCollectionChangeListener(PhotoCollectionChangeListener l) {
        listeners.add( l );
    }
    
    /**
     Notify all registered listeners about a change to this collection
     */
    private void fireChangeEvent( ) {
        for ( PhotoCollectionChangeListener l : listeners ) {
            l.photoCollectionChanged( new PhotoCollectionChangeEvent( this ) );
        }
    }

    public List<PhotoInfo> queryPhotos( Session session ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}

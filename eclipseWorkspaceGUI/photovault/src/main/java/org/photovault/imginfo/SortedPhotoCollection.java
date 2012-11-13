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
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Iterator;
import java.util.UUID;
import org.hibernate.Session;

/**
 * <p>This class implements a filter for sorting a photo collection by specific criteria. 
 * In practice, it creates a copy of the collection, sorts it and listens to change events
 * from the original colelction. If it is changed, the sorted copy is also recreated. </p>
 * <p> The sorting order can be defined using a Comparator object that is capable of 
 * Comparing PhotoInfo objects. The default is to order photos according to their ID.</p>
 * @author Harri Kaimio
 */
public class SortedPhotoCollection implements PhotoCollection, PhotoCollectionChangeListener {
    
     static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( SortedPhotoCollection.class.getName() );
     
    /**
     * Creates a new SortedPhotoCollection that is based on c.
     * @param c The PhotoCOllection that this object will sort.
     */
    public SortedPhotoCollection( PhotoCollection c ) {
        origCollection = c;
        comparator = new PhotoIdComparator();
        sortCollection();
        c.addPhotoCollectionChangeListener( this );
    }
    
    /**
     * Original collection that is sorted
     */
    protected PhotoCollection origCollection;
    
    /**
     * This Vector contains the photos in sorted order.
     */
    protected ArrayList sortedPhotos = null;
    /**
     * Comparator used in sorting the photosd.
     */
    private Comparator comparator = null;
    /**
     * List of PhotoCollectionChangeLIsteners interested in changes to this collection
     */
    protected Vector changeListeners = new Vector();
    
    /**
     * Comparator that orders the photos based on their uuid.
     */
    static class PhotoIdComparator implements Comparator {
        public int compare( Object o1, Object o2 ) {
            PhotoInfo p1 = (PhotoInfo) o1;
            PhotoInfo p2 = (PhotoInfo) o2;
            UUID id1 = p1.getUuid();
            UUID id2 = p2.getUuid();
            return id1.compareTo( id2 );
        }
    }
        
   
    /** Creates a new instance of SortedPhotoCollection */
    public SortedPhotoCollection() {
    }

    /**
     * Remove a listener
     * @param l Listener that is sto be removed
     */
    public void removePhotoCollectionChangeListener(PhotoCollectionChangeListener l) {
        changeListeners.remove( l );
    }

    /**
     *       Adds a new listener that will be notified of changes to the collection
     * @param l The new listener
     */
    public void addPhotoCollectionChangeListener(PhotoCollectionChangeListener l) {
        changeListeners.add( l );
    }

    /**
     * Get a single photo from the collection
     * @param numPhoto Number of the photo to retrieve. This must be >= 0 and < than
     * the number of photos in collection.
     * @return The photo with the given index.
     * @throws ArrayIndexOutOfBoundsException if numPhoto is out of bounds.
     */
    public PhotoInfo getPhoto(int numPhoto) {
        if ( sortedPhotos == null || numPhoto >= sortedPhotos.size() ) {
	    throw new ArrayIndexOutOfBoundsException();
	}
    	return (PhotoInfo) sortedPhotos.get( numPhoto );
    }

    /**
     *       returns the number of photos in this collection
     * @return Number of photos.
     */
    public int getPhotoCount() {
        if ( sortedPhotos == null ) {
	    return 0;
	}
	return sortedPhotos.size();
    }

    /**
     *       This method will be changed when the photo collection has changed for some reason
     * @param ev Event describing change to the collection
     */
    public void photoCollectionChanged(PhotoCollectionChangeEvent ev) {
        sortCollection();
    }
    
    
    /**
     * Actual sorting of the collection is done by this method. 
     */
    protected void sortCollection() {
 
        sortedPhotos = new ArrayList( origCollection.getPhotoCount() );
        for ( int n = 0; n < origCollection.getPhotoCount(); n++ ) {
            PhotoInfo p = origCollection.getPhoto( n );
            sortedPhotos.add( p );
        }
        Collections.sort( sortedPhotos, comparator );
        notifyListeners();
    }
    
    /**
     * Sends a change event to all listeners of this collection.
     */
     protected void notifyListeners() {
	PhotoCollectionChangeEvent ev = new PhotoCollectionChangeEvent( this );
	Iterator iter = changeListeners.iterator();
	while ( iter.hasNext() ) {
	    PhotoCollectionChangeListener l = (PhotoCollectionChangeListener) iter.next();
	    l.photoCollectionChanged( ev );
	}
    }

    /** 
     Returns the original collection object unsorted 
     */
    public PhotoCollection getOrigCollection() {
        return origCollection;
    }
    /**
     * Return the comparator used when sorting photos.
     */
    public Comparator getComparator() {
        return comparator;
    }

    /**
     * Set the comparator used when sorting photos
     * @param comparator Comparator object that will be used
     */
    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
        sortCollection();
        notifyListeners();
    }

    public List<PhotoInfo> queryPhotos( Session arg0 ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}

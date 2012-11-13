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

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author harri
 */
@Entity
@DiscriminatorValue( "original" )
public class OriginalImageDescriptor extends ImageDescriptorBase {
    
    /** Creates a new instance of OriginalImageDescriptor */
    public OriginalImageDescriptor() {
        super();
    }
    
    public OriginalImageDescriptor( ImageFile f, String locator ) {
        super( f, locator );
    }
    
    Set<CopyImageDescriptor> copies = new HashSet<CopyImageDescriptor>();

    @OneToMany( mappedBy="original", cascade  = { CascadeType.PERSIST, CascadeType.MERGE } )
    @org.hibernate.annotations.Cascade({
               org.hibernate.annotations.CascadeType.SAVE_UPDATE })    
    public Set<CopyImageDescriptor> getCopies() {
        return copies;
    }

    public void setCopies(Set<CopyImageDescriptor> copies) {
        this.copies = copies;
    }
    
    /**
     Remove copy from the list of know copies. Called when deleting copy image 
     descriptor
     @param copy
     */
    void removeCopy( CopyImageDescriptor copy ) {
        copies.remove( copy );
    }
    
    Set<PhotoInfo> photos = new HashSet<PhotoInfo>();
    
    @OneToMany( mappedBy="original", cascade  = { CascadeType.PERSIST, CascadeType.MERGE } )
    @org.hibernate.annotations.Cascade({
               org.hibernate.annotations.CascadeType.SAVE_UPDATE })    
    public Set<PhotoInfo> getPhotos() {
        return photos;
    }

    protected void setPhotos( Set<PhotoInfo> photos ) {
        this.photos = photos;
    }
}

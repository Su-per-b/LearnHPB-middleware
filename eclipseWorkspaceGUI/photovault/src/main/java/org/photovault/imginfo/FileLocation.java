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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 This class describes a location where an {@link ImageFile} is stored.
 */
@Embeddable
public class FileLocation {

    static Log log = LogFactory.getLog( FileLocation.class.getName() );
    ImageFile file;

    private VolumeBase volume;

    private String fname;
    
    /**
     Default constructor for persistence layer.
     */
    protected FileLocation() {
    }

    /** 
     Creates a new instance of FileLocation. The consteuctor also populates 
     fields of this objetc from the file if it is possible.
     @param volume Volume in which the file is
     @param fname volume internal name for the file.
     */
    public FileLocation( VolumeBase volume, String fname ) {
        this.setVolume( volume );
        this.setFname( fname );

        // Set the lastModified field if possible.
        try {
            File f = volume.mapFileName( fname );
            lastModified = f.lastModified();
        } catch ( FileNotFoundException ex ) {
            log.warn( "file " + fname + " in volume " + volume.getId() + "does not exist" );
        }
    }

    @org.hibernate.annotations.Parent
    public ImageFile getImageFile() {
        return file;
    }

    public void setImageFile(ImageFile file) {
        this.file = file;
    }

    @ManyToOne
    @JoinColumn( name = "volume_id", nullable = false, updatable = false )
    public VolumeBase getVolume() {
        return volume;
    }

    public void setVolume(VolumeBase volume) {
        this.volume = volume;
    }

    @Column( name = "fname", nullable = false, updatable = false )
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    
    private long lastModified;

    @Column( name = "last_modified" )
    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
    
    public void setLastModifiled( Date lastModified ) {
        this.lastModified = lastModified.getTime();
    }
    
    private Date lastChecked = new Date();
    
    @Column( name = "last_checked" )
    @Temporal(value = TemporalType.TIMESTAMP )
    public Date getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(Date lastChecked) {
        this.lastChecked = lastChecked;
    }
    
    @Override
    public boolean equals( Object o ) {
        if ( o instanceof FileLocation ) {
            FileLocation that = (FileLocation) o;
            return ( fname.equals( that.fname ) && 
                    (lastModified == that.lastModified) && 
                    lastChecked.equals( that.lastChecked ) &&
                    (volume == that.volume) && 
                    (file == that.file) );
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return volume.hashCode() + fname.hashCode();
    }

    /**
     Get the handle to the actual file described by this location object
     @return The file described by this location if it is available, 
     <code>null</code> otherwise.
     */
    @Transient
    public File getFile() {
        try {
            return volume.mapFileName( fname );
        } catch ( FileNotFoundException e ) {
            return null;
        }
    }
}

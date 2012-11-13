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

package org.photovault.folder;

import java.io.File;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.photovault.imginfo.ExternalVolume;

/**
 Description of an directory in external volume
 */
@Embeddable
public class ExternalDir {
    ExternalVolume volume;
    String path;

    /**
     Default constructor for persistence layer.
     */
    protected ExternalDir() {}
    
    /**
     Create a new instance
     @param vol ExternalVolume in which this directory is
     @param path relative path from volume root to the directory
     */
    public ExternalDir( ExternalVolume vol, String path ) {
        volume = vol;
        this.path = path;
    }
    
    /**
     Get the external volume of this directory
     @return the volume
     */
    @ManyToOne
    @JoinColumn( name = "extvol_uuid" )    
    public ExternalVolume getVolume() {
        return volume;
    }
    
    /**
     Set the volume (for persisence layer usage, use constructor to set up 
     otherwise.
     @param vol The volume
     */
    protected void setVolume( ExternalVolume vol ) {
        this.volume = vol;
    }
    
    /**
     Get the path to the directory
     @return relative path string from volume root to directory
     */
    @Column( name = "extvol_path" )
    public String getPath() {
        return path;
    }
    
    /**
     Set the path (for persistence layer internal usage, user constructor for
     setup otherwise.
     @param path
     */
    public void setPath( String path ) {
        this.path = path;
    }
    
    /**
     Get the absolute path name to directory in file system
     @return File object with absolute path to the directory if the external 
     volume is available. <code>null</code> otherwise. Note that even if this
     method returns non-null it does not mean that the directory exists, only
     that the external volume is available. The directory may be removed by 
     user.
     */
    @Transient
    public File getDirectory() {
        File ret = null;
        if ( volume.isAvailable() ) {
            ret = new File( volume.getBaseDir(), path );
        }
        return ret;
    }
}

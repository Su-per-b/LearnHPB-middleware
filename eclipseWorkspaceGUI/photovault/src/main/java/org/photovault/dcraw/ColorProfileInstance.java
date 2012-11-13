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

package org.photovault.dcraw;

import java.io.File;
import java.io.FileNotFoundException;
import org.photovault.imginfo.Volume;
import org.photovault.imginfo.VolumeBase;

/**
 * Represents an instance of a ICC color profile file. The actual profile is 
 * described by {@link ColorProfileDesc}, but the profile can be stored in several
 * volumes.
 * @author Harri Kaimio
 * @since 0.4
 */
public class ColorProfileInstance {
    
    /** Creates a new instance of ColorProfileInstance */
    public ColorProfileInstance() {
    }
    
    /**
     * OJB id of the profile represented by this instance
     */
    int profileId;
    
    /**
     * Reference to the profile description
     */
    ColorProfileDesc profile;
    
    /**
     * OJB id of the volume in which this instance resides
     */
    String volumeId;
    
    VolumeBase volume = null;
    
    /**
     * File name of this profile instance relative to Volume root
     */
    String fname;
    
    /**
     * Get the volume in which this instance is stored
     * @return The Volume of this instance
     */
    public VolumeBase getVolume() {
        if ( volume == null ) {
            volume = VolumeBase.getVolume( volumeId );
        }
        return volume;
    }
    
    /**
     * Get the file this instance represents
     * @return File in which the ICC profile is stored.
     */
    public File getProfileFile() {
        File ret = null;
        try {
            ret = getVolume().mapFileName( fname );
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
    
    /**
     Get the profile description for this profile.
     */
    public ColorProfileDesc getProfile() {
        return profile;
    }
}

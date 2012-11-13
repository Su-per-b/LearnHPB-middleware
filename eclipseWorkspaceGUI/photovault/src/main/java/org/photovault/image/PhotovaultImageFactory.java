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

package org.photovault.image;

import java.io.File;
import org.photovault.common.PhotovaultException;
import org.photovault.dcraw.RawImage;

/**
 Factory class for instantiating the correct PhotovaultImage subclass for reading 
 a certain image file.
 @author Harri Kaimio
 @since 0.5.0
 */
public class PhotovaultImageFactory {
    
    /** Creates a new instance of PhotovaultImageFactory */
    public PhotovaultImageFactory() {
    }

    /**
     Instantiates a proper PhotovaultImage subclass to read given image file.
     Currently the loginc for choosing the file is to first check if Java ImageIO
     recognizes the suffix of the image file name. If htat is not the case, RawImage
     is tried.
     @param f The file that will be loaded
     @param loadImage If <code>true</code> the image will be read to memory 
     immediately. If <code>false</code> it will be read upon demand.
     @param loadMetadata If <code>true</code> the image metadata will be read to 
     memory immediately. If <code>false</code> it will be read upon demand.
     @return Object of a subclass of PhotovaultImage if the file can be read. 
     <code>null</code> if it not of an recognized image file format.
     @throws PhotovaultException if dcraw has not been initialized properly ({@link 
     org.photovault.dcraw.DCRawProcessWrapper})
     */
    public PhotovaultImage create( File f, boolean loadImage, boolean loadMetadata )
            throws PhotovaultException {
        PhotovaultImage ret = ImageIOImage.getImage( f, loadImage, loadMetadata );
        if ( ret == null ) {
            RawImage raw = new RawImage( f );
            if ( raw.isValidRawFile() ) {
                ret = raw;
            }
        }
        return ret;        
    }
}

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


package org.photovault.swingui;

import java.util.EventObject;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.imginfo.PhotoInfo;

/**
 Event that is sent to a preview window when it should display preview of new 
 raw settings.
 
 */
public class PhotoViewChangeEvent extends EventObject {

    /**
     The new photo in the view.
     */
    private PhotoInfo newPhoto;

    
    /** Creates a new instance of PhotoViewChangeEvent 
     @param src Object that initiated this event
     @param newPhoto the photo That is now displayed
     */
    public PhotoViewChangeEvent( JAIPhotoViewer src, PhotoInfo newPhoto ) {
        super( src );
        this.newPhoto = newPhoto;
    }
    
    /**
     Get the photo that is now displayed
     */
    public PhotoInfo getNewPhoto() {
        return newPhoto;
    }    
}

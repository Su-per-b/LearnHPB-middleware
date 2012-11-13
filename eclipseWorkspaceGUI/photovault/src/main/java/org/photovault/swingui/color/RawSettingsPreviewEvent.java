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

package org.photovault.swingui.color;

import java.util.EventObject;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.imginfo.PhotoInfo;

/**
 Event that is sent to a preview window when it should display preview of new 
 raw settings.
 
 */
public class RawSettingsPreviewEvent extends EventObject {

    /**
     The photos whose settings were changed
     */
    private PhotoInfo[] model;

    /**
     New conversions ettings for the changed photos
     */
    private RawConversionSettings newSettings;
    
    /** Creates a new instance of RawSettingsPreviewEvent 
     @param src Object that initiated this event
     @param model the photos whose settings were changed
     @param newSettings New settings for the changed photos
     */
    public RawSettingsPreviewEvent( Object src, PhotoInfo[] model,
            RawConversionSettings newSettings ) {
        super( src );
        this.model = model;
        this.newSettings = newSettings;
    }
    
    /**
     Get the new settings for the changed photos
     */
    public RawConversionSettings getNewSettings() {
        return newSettings;
    }
    
    /**
     Get the photo that were changed
     */
    public PhotoInfo[] getModel() {
        return model;
    }
    
}

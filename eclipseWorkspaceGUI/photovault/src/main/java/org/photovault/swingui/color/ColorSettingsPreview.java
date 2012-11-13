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

package org.photovault.swingui.color;

import java.util.List;
import org.photovault.image.PhotovaultImage;
import org.photovault.imginfo.ChangePhotoInfoCommand;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoFields;

/**
 Interface that {@link ColorSettingsDialogController} uses to manage color 
 settings in image viewing component.
 
 */
public interface ColorSettingsPreview {
    /**
     Set the field value in preview
     @param field The field to set
     @param value New value for the field
     @param refValues Reference values to show if any
     */
    public void setField(PhotoInfoFields field, Object value, List refValues);
    
    /**
     Get the photo currently shown in image viewer
     */
    public PhotoInfo getPhoto();
    
    /**
     Get the currently displayed image
     
     @return Image currently displayed in the preview control or <code>null</code>
     if none.
     */
    public PhotovaultImage getImage();
}

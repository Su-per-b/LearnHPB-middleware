/*
  Copyright (c) 2008 Harri Kaimio
  
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

package org.photovault.imginfo.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.photovault.imginfo.*;

/**
 Abstract base class for data transfer objects used to move or copy image 
 descriptors between data bases. Subclasses are used and constructed as a part 
 of {@link ImageFileDTO}
 
 @since 0.6.0
 @author Harri Kaimio
 @see ImageFileDTO
 @see ImageDescriptorBase
 @see ImageFileDtoResolver
 */
@XStreamAlias( "image" )
public abstract class ImageDescriptorDTO {

    /**
     Constructor
     @param img The image used to construct this DTO
     */
    ImageDescriptorDTO( ImageDescriptorBase img ) {
        width = img.getWidth();
        height = img.getHeight();
        locator = img.getLocator();
    }

    ImageDescriptorDTO() {
    }
    
    /**
     Width of the image (in pixels)
     */
    @XStreamAsAttribute
    private int width;
    
    /**
     Height of the image (in pixels)
     */
    @XStreamAsAttribute
    private int height;
    
    /**
     Location of the image in containing file
     */
    @XStreamAsAttribute
    private String locator;

    /**
     Create a new image descriptor based on this DTO. Used internally by 
     {@link ImageFileDtoResolver}. The method first calls createImageDescriptor()
     to instantiate correct subclass of ImageDescriptorBAse. After that it calls
     updateDescriptor() to set the fields to correct values.
     @param resolver The resolver used to find or create referenced ImageFile 
     instances
     @return A new image descriptor that corresponds to this DTO. The returned 
     instance is not persistent yet.
     */
    ImageDescriptorBase getImageDescriptor( ImageFileDtoResolver resolver ) {
        ImageDescriptorBase img = createImageDescriptor();
        updateDescriptor( img, resolver );
        return img;
    }
    
    /**
     Creates a correct subclass of {@link ImageDescriptorBase} to be used with 
     this DTO class. Derived classes must override this to return new object of 
     the right class.     
     @return Newly created instance of corresponding class.
     */
    protected abstract ImageDescriptorBase createImageDescriptor();
    
    /**
     Set the fields of given image descriptor to match this DTO. Derived classes
     must override this method if they define new fields.
     @param img The iamge descriptor being modified.
     @param fileResolver Resolver used to find possible {@link ImageFile} 
     references.
     */
    protected void updateDescriptor( ImageDescriptorBase img, 
            ImageFileDtoResolver fileResolver ) {
        img.setHeight( getHeight() );
        img.setWidth( getWidth() );
        img.setLocator( getLocator() );        
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getLocator() {
        return locator;
    }
}
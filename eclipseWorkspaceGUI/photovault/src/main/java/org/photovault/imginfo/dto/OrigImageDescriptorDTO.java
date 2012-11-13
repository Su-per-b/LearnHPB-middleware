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
import java.io.Serializable;
import org.photovault.imginfo.*;
import java.util.Map;
import java.util.UUID;

/**
 Data transfer object of {@link OriginalImageDescriptor} objects.
 
 @serial TODO write decent serialized form documentation 
 
 @since 0.6.0
 @author Harri Kaimio
 @see OriginalImageDescriptor
 @see ImageDescriptorDTO
 */
@XStreamAlias( "original" )
public class OrigImageDescriptorDTO 
        extends ImageDescriptorDTO implements Serializable {

    /**
     Constructor used internally
     @param img Image used to construct this object
     @param createdFiles Files already created in this graph
     */
    OrigImageDescriptorDTO( ImageDescriptorBase img, Map<UUID, ImageFileDTO> createdFiles ) {
        super( img );
    }

    /**
     Default constructor, for serialization
     */
    OrigImageDescriptorDTO() {
        super();
    }

    
    
    @Override
    protected ImageDescriptorBase createImageDescriptor() {
        return new OriginalImageDescriptor();
    }
}

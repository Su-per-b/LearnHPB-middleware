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

import org.photovault.imginfo.ImageFile;
import org.photovault.imginfo.OriginalImageDescriptor;
import org.photovault.replication.HibernateDTOResolver;

/**
 Class for converting between reference to original image and corresponding 
 {@link OrigImageRefDTO}
 
 @since 0.6.0
 @author Harri Kaimio
  
 */
public class OrigImageRefResolver 
        extends HibernateDTOResolver<OriginalImageDescriptor, OrigImageRefDTO> {
   
    ImageFileDtoResolver ifileResolver;
    
    private ImageFileDtoResolver getIfileResovler() {
        if ( ifileResolver == null ) {
            ifileResolver = new ImageFileDtoResolver( getSession() );
        }
        return ifileResolver;
    }
    
    
    
    public OriginalImageDescriptor getObjectFromDto( OrigImageRefDTO dto ) {
        if ( dto == null ) {
            return null;
        }
        ImageFileDTO ifileDto = dto.getFileDto();
        ImageFile ifile = getIfileResovler().getObjectFromDto( ifileDto );
        return (OriginalImageDescriptor) ifile.getImage( dto.getLocator() );
    }

    public OrigImageRefDTO getDtoFromObject( OriginalImageDescriptor object ) {
        if ( object == null ) {
            return null;
        }
        return new OrigImageRefDTO( object );
    }

}

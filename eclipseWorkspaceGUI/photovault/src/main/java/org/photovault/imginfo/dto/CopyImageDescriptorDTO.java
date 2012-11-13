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
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.image.ChannelMapOperation;
import org.photovault.image.ImageOpChain;
import org.photovault.imginfo.CopyImageDescriptor;
import org.photovault.imginfo.ImageDescriptorBase;
import org.photovault.imginfo.ImageFile;
import org.photovault.imginfo.OriginalImageDescriptor;

/**
 Data transfer object of {@link CopyImageDescriptor} objects.
 
 @serial TODO write decent serialization doc
 
 @since 0.6.0
 @author Harri Kaimio
 @see CopyImageDescriptor
 @see ImageDescriptorDTO
 */
@XStreamAlias( "copy" )
public class CopyImageDescriptorDTO 
        extends ImageDescriptorDTO implements Serializable {
    
    /**
     Creates a new instance
     @param img Image whose DTO is created
     @param createdFiles Map from image file uuids whose DTO has already been created
     as part of this transaction to the actual DTOs. This is used to decide
     whether to create a DTO for the file or to use the existing one. The map is 
     updated with all DTOs that are created by the constructor.
     */
    CopyImageDescriptorDTO( CopyImageDescriptor img, Map<UUID, ImageFileDTO> createdFiles ) {
        super( img );
        processing = img.getProcessing();
        
        // Make sure that also original instance is stored in this graph
        ImageFile origFile = img.getOriginal().getFile();
        UUID origFileId = origFile.getId();
        if ( createdFiles.containsKey( origFileId ) ) {
            origImageFile = createdFiles.get( origFileId );
        } else {
            origImageFile = new ImageFileDTO( origFile, createdFiles );
        }
        origLocator = img.getOriginal().getLocator();
    }

    /**
     Default constructor
     */
    CopyImageDescriptorDTO() {
        super();
    }
    
    private ImageOpChain processing;

    /**
     DTO of the file that contains this iamge
     @serialField 
     */
    private ImageFileDTO origImageFile;
    /**
     Location of the image inside the file
     @serialField 
     */
    private String origLocator;

    /**
     Create a new {@link CopyImageDescriptor}. Used by getImageDescriptor()
     to instantiate the correct class.
     */
    @Override
    protected ImageDescriptorBase createImageDescriptor() {
        return new CopyImageDescriptor();
    }
    
    /**
     Updates a {@link CopyImageDescriptor} to match the state of this DTO
     @param img The CopyImageDesciptor that will be updated
     @param fileResolver resolver used to resolve any image file references.
     */
    @Override
    protected void updateDescriptor( ImageDescriptorBase img,
            ImageFileDtoResolver fileResolver ) {
        super.updateDescriptor( img, fileResolver );
        CopyImageDescriptor cimg = (CopyImageDescriptor) img;
        cimg.setProcessing( processing );
        ImageFile origFile = fileResolver.getObjectFromDto( getOrigImageFile() );
        OriginalImageDescriptor original = 
                (OriginalImageDescriptor) origFile.getImage( getLocator() );
        cimg.setOriginal( original );
    }

    public ImageOpChain getProcessing() {
        return processing;
    }

    public ImageFileDTO getOrigImageFile() {
        return origImageFile;
    }

    public String getOrigLocator() {
        return origLocator;
    }
}

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

package org.photovault.imginfo;

import java.awt.geom.Rectangle2D;
import java.util.EnumSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.image.ChannelMapOperation;
import org.photovault.image.ImageOpChain;

/**
 * CopyImageDescriptor describes the properties a a single image that is stored in an
 * image file.
 */

@Entity
@DiscriminatorValue( "modified" )
public class CopyImageDescriptor extends ImageDescriptorBase {
    
    /**
     * Creates a new instance of CopyImageDescriptor
     */
    public CopyImageDescriptor() {
        super();
    }
    
    public CopyImageDescriptor( ImageFile f, String locator, OriginalImageDescriptor orig ) {
        super( f, locator );
        this.original = orig;
        orig.copies.add( this );
    }

    /**
     * Image processing chain used to obtain this image from original
     */
    private ImageOpChain processing = new ImageOpChain();

    private OriginalImageDescriptor original;

    @Column( name="processing", length=1000000 )
    @Type(type="org.photovault.persistence.ImageOpChainUserType")
    public ImageOpChain getProcessing() {
        return processing;
    }

    public void setProcessing( ImageOpChain chain ) {
        processing = chain;
    }



    @Transient
    public Rectangle2D getCropArea() {
        return processing.getCropping();
    }

    public void setCropArea(Rectangle2D cropArea) {
        processing.applyCropping( cropArea );
    }

    @Transient
    public double getRotation() {
        return processing.getRotation();
    }

    public void setRotation(double rotation) {
        processing.applyRotation( rotation );
    }

    @Transient
    public RawConversionSettings getRawSettings() {
        return processing.getRawConvSettings();
    }

    public void setRawSettings(RawConversionSettings rawSettings) {
        processing.applyRawConvSetting( rawSettings );
    }

    @Transient
    public ChannelMapOperation getColorChannelMapping() {
        return processing.getChanMap();
    }

    public void setColorChannelMapping(ChannelMapOperation colorChannelMapping) {
        processing.applyChanMap( colorChannelMapping );
    }
    


    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @org.hibernate.annotations.Cascade( {org.hibernate.annotations.CascadeType.SAVE_UPDATE } )
    @JoinColumn( name = "original_id", nullable = true )
    public OriginalImageDescriptor getOriginal() {
        return original;
    }

    public void setOriginal(OriginalImageDescriptor original) {
        this.original = original;
    }

    /**
     Get operations that have been applied to this image when creating it form 
     original
     @return set of {@link ImageOperations} values for those operations that have 
     been applied to this image.
     */
    @Transient
    public EnumSet<ImageOperations> getAppliedOperations() {
        EnumSet<ImageOperations> applied = EnumSet.noneOf( ImageOperations.class );
        
        if ( processing.getOperation( "crop" ) != null ) {
            applied.add( ImageOperations.CROP );
        }
        // Check for raw conversion
        if ( processing.getOperation( "dcraw" ) != null ) {
            applied.add( ImageOperations.RAW_CONVERSION );
        }
        // Check for color mapping
        ChannelMapOperation colorMap = getColorChannelMapping();
        if ( processing.getOperation( "chan-map" ) != null ) {
            applied.add( ImageOperations.COLOR_MAP );
        }
        return applied;
    }
}

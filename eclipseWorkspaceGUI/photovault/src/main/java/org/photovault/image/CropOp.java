/*
  Copyright (c) 2009 Harri Kaimio

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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * CropOp describes the cropping and rotation done for an image as part of its
 * processing chain.
 * @author Harri Kaimio
 * @since 0.6.0
 */
@XStreamAlias( "crop" )
public class CropOp extends ImageOp {

    /**
     * Constructor
     */
    public CropOp() {
        super();
        addInputPort( "in" );
        addOutputPort( "out" );
    }

    public CropOp( CropOp op ) {
        super( op );
        initPorts();
        minx = op.minx;
        miny = op.miny;
        maxx = op.maxx;
        maxy = op.maxy;
        rot = op.rot;
    }
    /**
     * Constructor
     * @param chain Chain in which the operation belongs
     * @param string Name of the operation
     */
    public CropOp( ImageOpChain chain, String string ) {
        super();
        initPorts();
        setName( string );
        chain.addOperation( this );
    }

    /**
     * Rotation that will be applied to the photo (in degrees, clockwise)
     */
    @XStreamAsAttribute
    private double rot = 0.0;
    /**
     * Minimum X coordinate (after rotation, normalized to 0..1)
     */
    @XStreamAsAttribute
    private double minx = 0.0;
    /**
     * Maximum X coordinate
     */
    @XStreamAsAttribute
    private double maxx = 1.0;
    /**
     * Minimum Y coordinate
     */
    @XStreamAsAttribute
    private double miny = 0.0;
    /**
     * Maximum Y coordinate
     */
    @XStreamAsAttribute
    private double maxy = 1.0;



    @Override
    protected void initPorts() {
        addInputPort( "in" );
        addOutputPort( "out" );
    }

    /**
     * @return the rot
     */
    public double getRot() {
        return rot;
    }

    /**
     * @param rot the rot to set
     */
    public void setRot( double rot ) {
        this.rot = rot;
    }

    /**
     * @return the xmin
     */
    public double getMinX() {
        return minx;
    }

    /**
     * @param xmin the xmin to set
     */
    public void setMinX( double xmin ) {
        this.minx = xmin;
    }

    /**
     * @return the xmax
     */
    public double getMaxX() {
        return maxx;
    }

    /**
     * @param xmax the xmax to set
     */
    public void setMaxX( double xmax ) {
        this.maxx = xmax;
    }

    /**
     * @return the ymin
     */
    public double getMinY() {
        return miny;
    }

    /**
     * @param ymin the ymin to set
     */
    public void setMinY( double ymin ) {
        this.miny = ymin;
    }

    /**
     * @return the ymax
     */
    public double getMaxY() {
        return maxy;
    }

    /**
     * @param ymax the ymax to set
     */
    public void setMaxY( double ymax ) {
        this.maxy = ymax;
    }

    @Override
    public ImageOp createCopy() {
        return new CropOp( this );
    }

}

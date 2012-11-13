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
 * DCRawMapOp describes the tone mapping from raw image that is converted to
 * linear sRGB color space to the final image. THis is a global tone mapping
 * operation that just controls for varying contrast and white and black levels
 *
 * @author Harri Kaimio
 * @since 0.6.0
 */
@XStreamAlias( "raw-map" )
public class DCRawMapOp extends ImageOp {

    public DCRawMapOp() {
        super();
        addInputPort( "in" );
        addOutputPort( "out" );
    }


    protected DCRawMapOp( DCRawMapOp op ) {
        super( op );
        initPorts();
        black = op.black;
        white = op.white;
        evCorr = op.evCorr;
        hlightCompr = op.hlightCompr;
    }

    public DCRawMapOp( ImageOpChain chain, String name ) {
        super();
        initPorts();
        setName( name );
        chain.addOperation( this );
    }



    @XStreamAsAttribute
    private int white;
    @XStreamAsAttribute
    private int black;
    @XStreamAsAttribute
    private double evCorr;
    @XStreamAsAttribute
    private double hlightCompr;


    @Override
    protected void initPorts() {
        addInputPort( "in" );
        addOutputPort( "out" );
        
    }

    /**
     * Get the reference white level (see {@link #getEvCorr() } and {@link
     * #getHlightCompr()} for more explanation
     * @return the white
     */
    public int getWhite() {
        return (white > 65535) ? 65535 : white;
    }

    /**
     * @param white the white to set
     */
    public void setWhite( int white ) {
        this.white = white;
    }

    /**
     * Get black level. Every pixel darker than this will be set to 0.
     * @return the black
     */
    public int getBlack() {
        return black;
    }

    /**
     * @param black the black to set
     */
    public void setBlack( int black ) {
        this.black = black;
    }

    /**
     * Get the exposure correction in EV units. If 0, all channels with value of
     * >= {@link #white} will be set to white. If evCorr is non-zero, white level
     * will be multiplied by 2^evCorr.
     * @return the evCorr
     */
    public double getEvCorr() {
        return evCorr;
    }

    /**
     * @param evCorr the evCorr to set
     */
    public void setEvCorr( double evCorr ) {
        this.evCorr = evCorr;
    }

    /**
     * Get the anount of highlight compression. Positive values will decrease
     * contrast, negative will increase it especially in high key areas so that
     * 1 unit will affect highlights roughly like 1 EV unit change in exposure.
     * @return the hlightCompr
     */
    public double getHlightCompr() {
        return hlightCompr;
    }

    /**
     * @param hlightCompr the hlightCompr to set
     */
    public void setHlightCompr( double hlightCompr ) {
        this.hlightCompr = hlightCompr;
    }

    @Override
    public ImageOp createCopy() {
        return new DCRawMapOp( this );
    }

}

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
import com.thoughtworks.xstream.annotations.XStreamConverter;
import java.util.HashMap;
import java.util.Map;

/**
 * Channel mixing and mapping for gamma corrected sRGB images.
 * @author Harri Kaimio
 * @since 0.6.0
 */
@XStreamAlias( "chan-map" )
@XStreamConverter( ChanMapOpXmlConverter.class )
public class ChanMapOp extends ImageOp {


    public ChanMapOp() {
        super();
        initPorts();
    }

    protected ChanMapOp( ChanMapOp op ) {
        super( op );
        initPorts();
        for ( Map.Entry<String, ColorCurve> e : op.channels.entrySet() ) {
            ColorCurve opc = e.getValue();
            ColorCurve c = new ColorCurve();
            for ( int n = 0 ; n < opc.getPointCount() ; n++ ) {
                c.addPoint( opc.getX( n), opc.getY( n ) );
            }
            channels.put( e.getKey(), c );
        }
    }

    public ChanMapOp( ImageOpChain chain, String name ) {
        super();
        initPorts();
        setName( name );
        chain.addOperation( this );
    }

    public ImageOp createCopy() {
        return new ChanMapOp( this );
    }

    @Override
    protected void initPorts() {
        addInputPort( "in" );
        addOutputPort( "out" );
    }

    Map<String, ColorCurve> channels = new HashMap<String, ColorCurve>();

    /**
     * Set mapping for given channel
     * @param name Name of the channel. Normal values are "red", "green", "blue",
     * "value" and "satuaration"
     * @param chan Mapping curve for given channel.
     */
    public void setChannel( String name, ColorCurve chan ) {
        channels.put( name, chan );
    }

    public ColorCurve getChannel( String name ) {
        return channels.get( name );
    }
}

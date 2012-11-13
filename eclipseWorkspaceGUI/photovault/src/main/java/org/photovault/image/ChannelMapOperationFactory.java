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

package org.photovault.image;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 Factory class used to crete ChannelMapOperation objects (which are immutable)
 */
public class ChannelMapOperationFactory {
    
    /** 
     Creates a new instance of ChannelMapOperationFactory with no defined 
     channel curves
     */
    public ChannelMapOperationFactory() {
    }
    
    /**
     Creates a new ChannelMapOperationFactory that initially creates objetcs that 
     are equal to an existing ChannelMapOperation.
     @param o The operation used as template. If <code>null</code> the result is 
     similar as if constructed with the no-argument constructor.
     */
    public ChannelMapOperationFactory( ChannelMapOperation o ) {
        if (o == null ) {
            return;
        }
        Iterator iter = o.channelPoints.entrySet().iterator();
        while ( iter.hasNext() ) {
            Map.Entry e = (Map.Entry) iter.next();
            String name = (String) e.getKey();
            Point2D[] points = (Point2D[]) e.getValue();
            ColorCurve c = new ColorCurve();
            for ( int n = 0 ; n < points.length ; n++ ) {
                Point2D p = points[n];
                c.addPoint( p.getX(), p.getY() );
            }
            channelCurves.put( name, c );
        }
    } 
    
    /**
     Create a ChannelMapOperation object from XML data in database. This is 
     intended mostly for Hibernate use.
     @param data The raw data from database field used for cunstruction the 
     channel mapping.
     */
    public static ChannelMapOperation createFromXmlData( byte[] data ) {
        if ( data == null ) {
            return null;
        }
        String xmlStr = new String( data );
        Digester d = new Digester();
        d.addRuleSet( new ChannelMapRuleSet() );
        ChannelMapOperationFactory f;
        try {
            f = (ChannelMapOperationFactory) d.parse(new StringReader(xmlStr));
        } catch (IOException ex) {
            return null;
        } catch (SAXException ex) {
            return null;
        }
        return f.create();
    }

    /**
     Map from channel name to the respective curve.
     */
    Map channelCurves = new HashMap();
    
    /**
     Set a new curve for a given channel
     @param channel channel name
     @param curve New curve for the channel
     */
    public void setChannelCurve( String channel, ColorCurve curve ) {
        if ( curve != null ) {
            channelCurves.put( channel, curve );
        } else {
            channelCurves.remove( channel );
        }
    }
    
    /**
     Get the mapping curve for a color channel
     @param channel Color channel name
     @return Respective mapping curve or <code>null</code> if it has not been 
     specified.
     */
    public ColorCurve getChannelCurve( String channel ) {
        return (ColorCurve) channelCurves.get( channel );
    }
    
    /**
     Remove a channel from mappings.
     @param channel Name of the channel to remove.
     */
    public void removeChannel( String channel ) {
        channelCurves.remove( channel );
    }
  
    /**
     Create a new ChannelMappingOperation with the channel settings in this factory
     @return A new object.
     */
    public ChannelMapOperation create() {
        ChannelMapOperation ret = new ChannelMapOperation();
        Iterator iter = channelCurves.entrySet().iterator();
        while ( iter.hasNext() ) {
            Map.Entry e = (Map.Entry) iter.next();
            String name = (String) e.getKey();
            ColorCurve c = (ColorCurve) e.getValue();
            Point2D[] points = new Point2D.Double[c.getPointCount()];
            for ( int n = 0 ; n < points.length ; n++ ) {
                points[n] = new Point2D.Double( c.getX( n ), c.getY( n ) );
            }
            ret.channelPoints.put( name, points );
        }
        return ret;
    }
}

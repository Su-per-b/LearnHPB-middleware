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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 This class is the database representation of mapping from input color channels 
 to output cahnnels. It does not contain logic for the actual mapping; this is done
 in {@link ColorCurve}
 <p>
 This class is immutable, new objects should be created using {@link 
 ChannelMapOperationFactory}.
 */
public class ChannelMapOperation implements Serializable {

    static final long serialVersionUID = 5997544060700510698L;
    
    /**
     * Creates a new instance of ChannelMapOperation. Should not be used by 
     application code, this is purely for OJB.
     */
    public ChannelMapOperation() {
    }
    
    /**
     Map from channel name to an array of control points (Point2D objects)
      */
    transient Map<String, Point2D[]> channelPoints = new HashMap();
    
    /**
     Get names of defined channels
     @return Array of all channel names
     */
    public String[] getChannelNames() {
        return (String[]) channelPoints.keySet().toArray( new String[0] );
    }
    
    /**
     Get the mapping curve for given channel. Note that the curve is a copy, 
     changes to it are not applied to this object.
     @param channel Name of the channel
     @return Mapping curve for the channel.
     */
    public ColorCurve getChannelCurve( String channel ) {
        ColorCurve c = null;
        if ( channelPoints.containsKey( channel ) ) {
            c = new ColorCurve();
            Point2D[] points = (Point2D[]) channelPoints.get( channel );
            for ( int n = 0; n < points.length ; n++ ) {
                Point2D p = points[n];
                c.addPoint( p.getX(), p.getY() );
            }
        }
        return c;
    }
    
    /**
     Helper function to get a string to use as indentation
     */
    static private String getIndent( int i ) {
        return "                                                  ".substring( 0, i );
    }
    
    /**
     Get XML representation of the object
     @param i Number of spaces to add as indentation for the top level element
     @return XML representation of the object
     */
    public String getAsXml( int i ) {
        StringBuffer buf = new StringBuffer();
        buf.append( getIndent( i ) ).append( "<color-mapping>" ).append( "\n" );
        i += 2;
        Iterator iter = channelPoints.entrySet().iterator();
        while ( iter.hasNext() ) {
            Map.Entry e = (Map.Entry) iter.next();
            String name = (String) e.getKey();
            buf.append( getIndent( i ) ).append( "<channel name=\"" + name + "\">" ).append( "\n" );
            i+=2;
            Point2D[] points = (Point2D[]) e.getValue();
            for ( int n = 0 ; n < points.length ; n++ ) {
                buf.append( getIndent( i ) ).
                        append( "<point x=\"" ).append( points[n].getX() ).
                        append( "\" y=\"" ).append( points[n].getY() ).append("\"/>\n" );
            }
            i-=2;
            buf.append( getIndent( i ) ).append( "</channel>" ).append( "\n" );
        }
        i-= 2;
        buf.append( getIndent( i ) ).append( "</color-mapping>" ).append( "\n" );
        return buf.toString();
    }

    /**
     Get XML representation of the object
     @return XML representation of the object
     */
    public String getAsXml() {
        return getAsXml( 0 );
    }
    

    /**
     Test for equality
     @param o The object to compare this object with
     @return true if o and this object are equal, false otherwise
     */
    @Override
    public boolean equals( Object o ) {
        if ( !(o instanceof ChannelMapOperation ) ) {
            return false;
        }
        ChannelMapOperation c = (ChannelMapOperation) o;
        if ( channelPoints.size() != c.channelPoints.size() ) {
            return false;
        }
        String[] channelNames = getChannelNames();
        for ( int n = 0 ; n < channelNames.length ; n++ ) {
            Point2D[] p1 = (Point2D[]) channelPoints.get( channelNames[n] );
            Point2D[] p2 = (Point2D[]) c.channelPoints.get( channelNames[n] );
            if ( p2 == null || p2.length != p1.length ) {
                return false;
            }
            for ( int i = 0 ; i < p1.length ; i++ ) {
                if ( !p1[i].equals( p2[i] ) ) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     Test whether two channel mappings produce equal results up to given precision
     
     @param c the other channel mapping
     @param precision Maximum error allowed
     @return <code>true</code> if the mappings are equal, <code>false</code> 
     otherwise
     */
    public boolean isAlmostEqual( ChannelMapOperation c, double precision ) {
        if ( c == null ) {
            return isAlmostIdentity( precision );
        }
        Set<String> chanNames = new HashSet<String>();
        chanNames.addAll( (Set<String>) channelPoints.keySet() );
        chanNames.addAll( (Set<String>) c.channelPoints.keySet() );
        for ( String chanName : chanNames ) {
            ColorCurve c1 = this.getChannelCurve( chanName );
            ColorCurve c2 = c.getChannelCurve( chanName );
            if ( c1 == null && c2 != null && !c2.isAlmostIdentity( precision ) ) {
                return false;
            } else if ( c1 != null && c2 == null && !c1.isAlmostIdentity( precision ) ) {
                return false;
            } else if ( c1 != null && c2 != null && !c1.isAlmostEqual( c2, precision ) ) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     Check whether this is an Identity mapping up to given precision
     
     @param precision The maximum deviation fron identity allowed
     @return
     */
    private boolean isAlmostIdentity( double precision ) {
        for ( String chanName : (Set<String>) channelPoints.keySet() ) {
            if ( !getChannelCurve( chanName ).isAlmostIdentity( precision ) ) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        String[] channelNames = getChannelNames();
        for ( int n = 0 ; n < channelNames.length ; n++ ) {
            hash = hash * 31 + channelNames[n].hashCode();
            Point2D[] p = (Point2D[]) channelPoints.get( channelNames[n] );
            hash = hash * 31 + p.hashCode();
        }
        return hash;
    }

    /**
     Serialize the object
     @param os The object stream
     @throws java.io.IOException
     @serialData Number of channels, followed by for each channel: name of the 
     channel, number of points, for each point x and y coordinates
     */
    private void writeObject( ObjectOutputStream os ) throws IOException {
        os.defaultWriteObject();
        os.writeInt( channelPoints.size() );
        for ( Map.Entry<String, Point2D[]> e : channelPoints.entrySet() ) {
            os.writeObject( e.getKey() );
            Point2D[] points = e.getValue();
            os.writeInt( points.length );
            for ( Point2D p : points ) {
               os.writeDouble( p.getX() ); 
               os.writeDouble( p.getY() ); 
            }
        }
    }
    
    private void readObject( ObjectInputStream is ) 
            throws IOException, ClassNotFoundException {
        is.defaultReadObject();
        channelPoints = new HashMap<String, Point2D[]>();
        int chanCount = is.readInt();
        for ( int n = 0 ; n < chanCount ; n++ ) {
            String chanName = (String) is.readObject();
            int pointCount = is.readInt();
            Point2D[] points = new Point2D[pointCount];
            for ( int m = 0 ; m < pointCount ; m++ ) {
                double x = is.readDouble();
                double y = is.readDouble();
                points[m] = new Point2D.Double( x, y );
            }
            channelPoints.put(  chanName, points );
        }
    }
}

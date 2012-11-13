/*
  Copyright (c) 2007-2008 Harri Kaimio
  
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
import java.awt.geom.CubicCurve2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
  Mapping from original color channel to to desired output channel values. In 
 practice, this class implements a 1-dimensional function defined by a cubic 
 (Bezier) spline that goes through user-defined control points. Assumption in 
 Photovault is that color channle values are mapped to range (0.0->1.0) for both 
 input and output but the class itself does not pose such restriction.
 <p>
 More specifially, the mapping is defined as follows
 <ul>
 <li>x values smaller than smallest control point X value are mapped to y value 
 of the CP with smallest X</li>
 <li>Similarly X values that are larger than any CP are mapped to Y value of CP 
 with largest X</li>
 <li>In conrol points the tangent of f(x) is parallel to the line between previous 
 and next control points, with the exception that in first and last CP the derivative 
 is noncontiguous.
 */
@XStreamAlias( "curve" )
@XStreamConverter( ColorCurveXmlConverter.class )
public class ColorCurve implements Serializable {
    
    static final long serialVersionUID = -4691331290177499814L;
    
    /** Creates a new instance of ColorCurve */
    public ColorCurve() {
    }
    
    /**
     X coordinates for control points, in increasing order
     */
    transient double[] pointX = {};
    
    /**
     Y coordinates for control points.
     */
    transient double[] pointY = {};
    
    /**
     Bezier coefficiens for each segment. This array contains the Y coordinates, 
     X coordinates x1 & x2 are defined so that x1 = 2/3 * x0 + 1/3 * x3 and
     x2 = 1/3 * x0 + 2/3 * x3. 
     */
    transient double b[][] = null;
    
    /**
     Add a new control point to the function
     @param x X coordinate
     @param y Y coordinate     
     */
    public void addPoint( double x, double y ) {
        double[] newX = new double[pointX.length+1];
        double[] newY = new double[pointY.length+1];
        int n = 0;
        while ( n < pointX.length && pointX[n] < x ) {
            newX[n] = pointX[n];
            newY[n] = pointY[n];
            n++;
        }
        newX[n] = x;
        newY[n] = y;
        while ( n < pointX.length ) {
            newX[n+1] = pointX[n];
            newY[n+1] = pointY[n];
            n++;
        }
        pointX = newX;
        pointY = newY;
        calcCoeffs();
    }
    
    /**
     Set the nth control point to a new value
     @param n Number of the control point to set
     @param x New X coordinate
     @param y New Y coordinate
     @throws IllegalArgumentException If X is not between control points n-1 and n+1
     @throws IndexOutOfBoundsException If n has invalid value
     */
    public void setPoint( int n, double x, double y ) {
        if ( n < 0 || n >= pointX.length ) {
            throw new IndexOutOfBoundsException( "" + n + 
                    " not in [0:" + (pointX.length-1) + "]");
        }
        if ( n > 0 && x <= pointX[n-1] ) {
            throw new IllegalArgumentException( "New X for contorl point " + n + 
                    " (" + x + ") smaller than " + pointX[n-1] );
        }
        if ( n < pointX.length -1  && x >= pointX[n+1] ) {
            throw new IllegalArgumentException( "New X for contorl point " + n + 
                    " (" + x + ") larger than " + pointX[n+1] );
        }
        pointX[n] = x;
        pointY[n] = y;
        calcCoeffs();
    }
    
    /**
     Get X coordinate of nth control point
     @param n Number of control point
     @return X coordinate
     */
    public double getX( int n ) {
        return pointX[n];
    }
    
    /**
     Get Y coordinate of nth control point
     @param n Number of control point
     @return Y coordinate
     */
    public double getY( int n ) {
        return pointY[n];
    }
    
    /**
     Get number of control points
     @return Number of control points.
     */
    public int getPointCount() {
        return pointX.length;
    }
    
    /**
     Get value of the function in given point
     @param x X coordinate of the point
     @return Value of fynction in given point
     */
    public double getValue( double x )  {
        // Special case: 0 points -> identity transform
        if ( pointX.length == 0 ) {
            return x;
        }
        
        // Special case: 1 control poit -> constant
        if ( pointX.length == 1 ) {
            return pointY[0];
        }
        
        if ( b == null ) {
            // The object was just de-serialized
            calcCoeffs();
        }
                
        // Find the correct segment
        int n = 0;
        while ( n < pointX.length && x >= pointX[n] ) {
            n++;
        }
        
        double dx = 1.0;
        double t = 0.0;
        if ( n > 0 && n < pointX.length ) {
            dx = pointX[n]-pointX[n-1];
            t = (x-pointX[n-1])/dx;
        }
        double t1 = 1.0 - t;
        
        double y = b[n][0] * t1*t1*t1 +
                3 * b[n][1] * t*t1*t1 +
                3 * b[n][2] * t*t*t1 +
                b[n][3] * t*t*t;
        return y;
    }
    
    /**
     Get the Bezier curve that defines a segment 
     of this curve
     @param n The order number of the segment, so that segment n is between 
     points n and n+1.
     @return Cubic curve for the segment in question.
     */
    public CubicCurve2D getSegment( int n ) {
        CubicCurve2D c = new CubicCurve2D.Double();
        double x = pointX[n];
        double dx = (pointX[n+1]-pointX[n])/3.0;
        c.setCurve( x, b[n+1][0], x+dx, b[n+1][1], x+2.0*dx, b[n+1][2], pointX[n+1], b[n+1][3] );
        return c;
    }
    
    /**
     Test for equality
     @param o Object to compare this curve with
     @return true if o is equal to this object, false otherwise
     */
    @Override
    public boolean equals( Object o ) {
        if ( !(o instanceof ColorCurve) ) {
            return false;
        }
        ColorCurve c = (ColorCurve) o;
        if ( c.pointX.length != pointX.length ) {
            return false;
        }
        
        final double delta = 0.00000001;
        for ( int n = 0 ; n < pointX.length ; n++ ) {
            if ( Math.abs( pointX[n]- c.pointX[n] ) > delta ||
                 Math.abs( pointY[n]- c.pointY[n] ) > delta ) {
                return false;
            }
        }
        return true;
    }
    
    /**
     Check whether two curves are equal up to given precision. The method tests 
     values of both curves in each control point defined to either of them.
     
     @param c The other ColorCurve
     @param precision Maximum error allowed
     @return <code>true</code> if the curves are equal, <code>false</code> 
     otherwise. If c is <code>null</code> the method calls isAlmostIdentity() and
     returns result of that, as null curve is assumed to act as identity curve in 
     Photovault.
     */
    public boolean isAlmostEqual( ColorCurve c, double precision ) {
        if ( c == null ) {
            return isAlmostIdentity( precision );
        }
        
        for ( int n = 0 ; n < pointX.length ; n++ ) {
            if ( Math.abs( c.getValue( pointX[n] ) - pointY[n] ) > precision ) {
                return false;
            }
        }
        for ( int n = 0 ; n < c.pointX.length ; n++ ) {
            if ( Math.abs( getValue( c.pointX[n] ) - c.pointY[n] ) > precision ) {
                return false;
            }
        }
        return true;
    }
    
    /**
     Check whether this curve defines an identity mapping up to a given precision
     
     @param precision Maximum deviation from identity mapping allowed
     @return
     */
    public boolean isAlmostIdentity( double precision ) {
        for ( int n = 0 ; n < pointX.length ; n++ ) {
            if ( Math.abs( pointX[n] - pointY[n] ) > precision ) {
                return false;
            }
        }
        return true;
    }
    
    /**
     Calculate hash code for this curve.
     */
    @Override
    public int hashCode() {
        long hash = 0;
        for ( int n = 0 ; n < pointX.length ; n++ ) {
            // We cannot use the hash codes for doubles directly since we allow error
            // margin in equality.
            hash = hash * 31 + Math.round( pointX[n] * 1000 ) + Math.round( pointY[n] * 1000 );
        }
        return (int)( hash % Integer.MAX_VALUE );
    }    
    
    /**
     Recalculate Bezier coefficients based on current control points.
     */
    private void calcCoeffs() {
        b = new double[pointX.length+1][4];
        
        // First segment is straight line
        b[0][0] = pointY[0];
        b[0][1] = pointY[0];
        b[0][2] = pointY[0];
        b[0][3] = pointY[0];
        
        for ( int n = 1; n < pointX.length ; n++ ) {
            b[n][0] = pointY[n-1];
            b[n][3] = pointY[n];
            if ( n > 1 ) {
                /*
                 If possible, set the starting slope of the line to be the same
                 as slope between previous point & segment end point
                 */
                double dy = pointY[n] - pointY[n-2];
                double dx = pointX[n] - pointX[n-2];
                b[n][1] = pointY[n-1] + (dy/dx) * (pointX[n]-pointX[n-1]) / 3.0;
            } else {
                // Slope of the first segment is towards the other end point
                b[n][1] = ( 2.0 * pointY[n-1] + pointY[n] ) / 3.0;
            } 
            
            if ( n < pointX.length -1 ) {
                double dy = pointY[n+1] - pointY[n-1];
                double dx = pointX[n+1] - pointX[n-1];
                b[n][2] = pointY[n] - (dy/dx) * (pointX[n]-pointX[n-1]) / 3.0;                
            } else {
                b[n][2] = ( pointY[n-1] + 2.0 * pointY[n] ) / 3.0;
            } 
        }
        
        b[pointX.length][0] = pointY[pointY.length-1];
        b[pointX.length][1] = pointY[pointY.length-1];
        b[pointX.length][2] = pointY[pointY.length-1];
        b[pointX.length][3] = pointY[pointY.length-1];
    }
    
    /**
     Serialization code
     @serialData First then number of points is written as integer. After that 
     follows the coordinates for each point - first X coordinate and after that 
     Y coordinate as doubles.
     @param os
     @throws java.io.IOException
     */
    private void writeObject( ObjectOutputStream os ) throws IOException {
        os.defaultWriteObject();
        os.writeInt( pointX.length );
        for ( int n = 0 ; n < pointX.length ; n++ ) {
            os.writeDouble( pointX[n] );
            os.writeDouble( pointY[n] );
        }
    }
    
    private void readObject( ObjectInputStream is ) 
            throws IOException, ClassNotFoundException {
        is.defaultReadObject();
        int pointCount = is.readInt();
        pointX = new double[pointCount];
        pointY = new double[pointCount];
        for  ( int n = 0  ; n < pointCount ; n++ ) {
            pointX[n] = is.readDouble();
            pointY[n] = is.readDouble();            
        }
        calcCoeffs();
    }
}

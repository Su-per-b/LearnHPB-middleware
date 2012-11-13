/*
  Copyright (c) 2006 Harri Kaimio
  
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

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;


/**
   This class provides a set of static methods for creating image transforms
*/

public class ImageXform {

    /**
       Returns a transform that scales & rotates a image with given resolution
       @param scale The scale for the transformation
       @param rot Rotation for the transformation (in degrees) - positive means clockwise
       @param width Width of the image in pixels - used to calculate the needed translation after the roation
       @param height Heighjt of the image
    */
    public static AffineTransform getScaleXform( double scale, double rot, double width, double height ) {
	AffineTransform at = new AffineTransform();
 	at.rotate( rot*Math.PI/180.0 );
	
	Rectangle2D bounds = getBounds( at, width, height );

	at.preConcatenate( at.getTranslateInstance( -scale*bounds.getMinX(), -scale*bounds.getMinY() ) );
	at.scale( scale, scale );
	return at;
    }

    public static AffineTransform getFittingXform( double newWidth, double newHeight, double rot, double curWidth, double curHeight ) {
	AffineTransform at = new AffineTransform();
 	at.rotate( rot*Math.PI/180.0 );
	
	Rectangle2D bounds = getBounds( at, curWidth, curHeight );

	double widthScale = ((double)newWidth)/bounds.getWidth();
	double heightScale = ((double)newHeight)/bounds.getHeight();

	double scale = widthScale;
	if ( heightScale < scale ) {
	    scale = heightScale;
	}
	at.preConcatenate( at.getTranslateInstance( -scale*bounds.getMinX(), -scale*bounds.getMinY() ) );
	at.scale( scale, scale );
	return at;
    }

    /**
     Returns a transfor which rotates an image & translates it so that it is 
     fully in the positive quadrant after the rotation
     @param rot The rotation to be applied (in degrees, clockwise)
     @param curWidth current width of rotated image im pixels (to translate in correctly)
     @param curHeight The current height of the image
     */
    
    public static AffineTransform getRotateXform( double rot, double curWidth, double curHeight ) {
	AffineTransform at = new AffineTransform();
 	at.rotate( rot*Math.PI/180.0 );
	
	Rectangle2D bounds = getBounds( at, curWidth, curHeight );

	at.preConcatenate( at.getTranslateInstance( -bounds.getMinX(), -bounds.getMinY() ) );
	return at;        
    }

    
    private static Rectangle2D getBounds( AffineTransform xform, double w, double h ) {
	double[] corners = {0.0f,              0.0f,
			   0.0f,              (double) h,
			   (double) w, (double) h,
			   (double) w, 0.0f };
	xform.transform( corners, 0, corners, 0, 4 );
	double minX = corners[0];
	double maxX = corners[0];
	double minY = corners[1];
	double maxY = corners[1];
	for ( int n = 2; n < corners.length; n += 2 ) {
	    if ( corners[n+1] < minY ) {
		minY = corners[n+1];
	    }
	    if ( corners[n+1] > maxY ) {
		maxY = corners[n+1];
	    }
	    if ( corners[n] < minX ) {
		minX = corners[n];
	    }
	    if ( corners[n] > maxX ) {
		maxX = corners[n];
	    }
	}

	return new Rectangle2D.Double( minX, minY, maxX-minX, maxY-minY );
    }
}

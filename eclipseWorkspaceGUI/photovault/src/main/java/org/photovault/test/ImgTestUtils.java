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

package org.photovault.test;

import javax.imageio.*;
import javax.imageio.stream.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

/**
   This class contains helper functions that can be used in test cases that need to analyze images.
*/
public class ImgTestUtils {
  static String tempDir = "/tmp";
    public static boolean compareImgToFile( BufferedImage img, File file ) {
	if ( file.exists() ) {
	    System.err.println( "File " + file.getName() + " exists" );
	    BufferedImage fImg = null;
	    try {
		fImg = ImageIO.read( file );
	    System.err.println( "Read image" );
	    } catch ( IOException e ) {
		System.err.println( "Error reading image: " + e.getMessage() );
		return false;
	    }
	    boolean eq = equals( img, fImg );
	    if ( !eq ) {
		File f = new File( tempDir, "error_" + file.getName() );
		Iterator writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = (ImageWriter)writers.next();
		ImageOutputStream ios = null;
		try {
		    ios = ImageIO.createImageOutputStream(f);
		    writer.setOutput(ios);
		    writer.write( img );
		} catch( IOException e ) {
		    System.err.println( "Cannot write to " + f.getName());
		    return false;
		} finally {
	    if ( ios != null ) {
		try {
		    ios.close();
		} catch (IOException e ) {}
	    }
	}
	    }
		
	    return eq;
	}        
        
	// The image file does not yet exist, so save it
	// First, make sure that the directory is created

	System.err.println( "Image " + file.getName() + " does not exist, creating a new." );
	Iterator writers = ImageIO.getImageWritersByFormatName("png");
	ImageWriter writer = (ImageWriter)writers.next();
	file.getParentFile().mkdirs();
	// Create the image file with a name like candidate_name.png
	file = new File( file.getParentFile(), "candidate_" + file.getName() );
	ImageOutputStream ios = null;
	try {
	    ios = ImageIO.createImageOutputStream(file);
	    writer.setOutput(ios);
	    writer.write(img);
	} catch( IOException e ) {
	    System.err.println( "Could not write file " + file.getName());
	    return false;
	} finally {
	    if ( ios != null ) {
		try {
		    ios.close();
		} catch (IOException e ) {}
	    }
	}
	return false;
    }	
	 
    /**
     Compare whether images stored in 2 files are the same.
     @param f1 First file to compare
     @param f2 Second file to compare
     @return true if the images are same, false otherwise.
     */
    public static boolean conpareImageFiles( File f1, File f2 ) {
        if ( f1.exists() && f2.exists() ) {
            BufferedImage f1Img = null;
            BufferedImage f2Img = null;
            try {
                f1Img = ImageIO.read( f1 );
                f2Img = ImageIO.read( f2 );
                System.err.println( "Read image" );
            } catch ( IOException e ) {
                System.err.println( "Error reading image: " + e.getMessage() );
                return false;
            }
            boolean eq = equals( f1Img, f2Img );
            return eq;
        }
        return false;
    }
    
    
    /**
       Returns true if the 2 images are equal, false otherwise
    */
    public static boolean equals( BufferedImage img1, BufferedImage img2 ) {
	if ( img1.getWidth() != img2.getWidth() ) {
	    return false;
	}
	if ( img1.getHeight() != img2.getHeight() ) {
	    return false;
	}
	System.err.println( "Equal size" );
	
	int[] data1 = img1.getRGB( 0, 0, img1.getWidth(), img1.getHeight(), null, 0, img2.getWidth() );
	int[] data2 = img2.getRGB( 0, 0, img2.getWidth(), img2.getHeight(), null, 0, img2.getWidth() );
	
	// Compare the images pixel by pixel
	int diffCount = 0;
	for ( int n = 0; n < data1.length; n++ ) {
	    if ( data1[n] != data2[n] ) {
		diffCount++;
	    }
	}
	return (diffCount < 10) ? true : false;
    }

}

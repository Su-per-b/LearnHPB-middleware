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

import junit.framework.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.geom.*;

public class Test_ImageXform extends TestCase {
    
    BufferedImage source = null;
    File testDir = null;
  String testImgDir = "testfiles";
  String refImgDir = "tests/images";
  
    /**
       Sets ut the test environment
    */
    public void setUp() {
      File f = new File( testImgDir, "test1.jpg" );
	try {
	    source = ImageIO.read( f );
	} catch ( IOException e ) {
	    System.err.println( "Error reading image: " + e.getMessage() );
	    return;
	}
	testDir = new File( refImgDir, "photovault/image/ImageXform" );
    }

    public void testScaling() {
	AffineTransform at = ImageXform.getScaleXform( 0.5, 0, source.getWidth(), source.getHeight() );
	AffineTransformOp atop = new AffineTransformOp( at, AffineTransformOp.TYPE_BILINEAR );
	BufferedImage dst = atop.filter( source, null );
	File testFile = new File( testDir, "scaling1.png" );
	assertTrue( "50% scaling not correct",
		    org.photovault.test.ImgTestUtils.compareImgToFile( dst, testFile ) );
	
	at = ImageXform.getScaleXform( 0.5, 45, source.getWidth(), source.getHeight() );
	atop = new AffineTransformOp( at, AffineTransformOp.TYPE_BILINEAR );
	dst = atop.filter( source, null );
	testFile = new File( testDir, "scaling2.png" );
	assertTrue( "50% scaling & rotation not correct",
		    org.photovault.test.ImgTestUtils.compareImgToFile( dst, testFile ) );
    }
	
    public void testFitting() {
	AffineTransform at = ImageXform.getFittingXform( 100, 100, 0, source.getWidth(), source.getHeight() );
	AffineTransformOp atop = new AffineTransformOp( at, AffineTransformOp.TYPE_BILINEAR );
	BufferedImage dst = atop.filter( source, null );
	File testFile = new File( testDir, "fitting1.png" );
	assertTrue( "fitting not correct",
		    org.photovault.test.ImgTestUtils.compareImgToFile( dst, testFile ) );
	
	at = ImageXform.getFittingXform( 100, 100, 45, source.getWidth(), source.getHeight() );
	atop = new AffineTransformOp( at, AffineTransformOp.TYPE_BILINEAR );
	dst = atop.filter( source, null );
	testFile = new File( testDir, "fitting2.png" );
	assertTrue( "fitting & rotation not correct",
		    org.photovault.test.ImgTestUtils.compareImgToFile( dst, testFile ) );
    }


    public static Test suite() {
	return new TestSuite( Test_ImageXform.class );
    }
    
    public static void main( String[] args ) {
	junit.textui.TestRunner.run( suite() );
    }        
}

    
    
	

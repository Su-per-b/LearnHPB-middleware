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


package org.photovault.dcraw;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.JPEGEncodeParam;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedImageAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.photovault.common.PhotovaultException;

/**
  JUnit test cases for dcraw wrapper code.
 
 */
public class Test_DCRawProcessWrapper extends TestCase {
    
    /** Creates a new instance of Test_DCRawProcessWrapper */
    public Test_DCRawProcessWrapper() {
    }
    
    /**
     This is not actually a test case but demonstator driver for raw conversion
     */
    public void testReadAsTiff() {
        File f = new File( "/tmp/img_2434.cr2" );
        try {
            File outf = new File( "/tmp/test.jpg" );
            OutputStream os = new FileOutputStream( outf );
            RawImage ri = null;
            try {
                ri = new RawImage(f);
            } catch (PhotovaultException ex) {
                fail( ex.getMessage() );
            }

            PlanarImage thumbImage = new RenderedImageAdapter( ri.getRenderedImage( 200, 200, true ) );

            JPEGEncodeParam encodeParam = new JPEGEncodeParam();
            ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", os,
                    encodeParam);
            try {
                encoder.encode( thumbImage );
                os.close();
                // origImage.dispose();
                thumbImage.dispose();
            } catch (Exception e) {
                fail( "Error writing thumbnail: " + e.getMessage() );
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            fail();
        } catch (IOException ex) {
            ex.printStackTrace();
            fail();
        } 
    }
         
    private byte [] getGammaLut( double dw ) {
        int white = (int) dw + 1;
        byte lut[] = new byte[0x10000];
        for ( int n = 0; n < lut.length; n++ ) {
            double r = ((double)n)/dw;
            double val = (r <= 0.018) ? r*4.5 : Math.pow(r,0.45)*1.099-0.099;
            if ( val > 1. ) {
                val = 1.;
            }
            lut[n] = (byte)(val*256.);
        }
        return lut;        
    }
    
    public static void main( String[] args ) {
	junit.textui.TestRunner.run( suite() );
    }
    
    public static Test suite() {
	return new TestSuite( Test_DCRawProcessWrapper.class );
    }
        
}

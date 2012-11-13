/*
 * Test_ImageIOImage.java
 *
 * Created on March 2, 2007, 9:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.photovault.image;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.common.PhotovaultException;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 * Test cases for ImageIOImage
 * @author harri
 */
public class Test_ImageIOImage {
    static private final Log log = LogFactory.getLog( Test_ImageIOImage.class.getName() );    
    /** Creates a new instance of Test_ImageIOImage */
    public Test_ImageIOImage() {
    }
    
    /**
     * Verify that a JPEG file and its metadata can be read correctly
     */
    @Test
    public void testJPEGImageRead() {
        File f = new File ( "testfiles", "test1.jpg" );
        PhotovaultImageFactory fact = new PhotovaultImageFactory();
        ImageIOImage img = null;
        try {
            img = (ImageIOImage) fact.create(f, false, false);
        } catch (PhotovaultException ex) {
            fail( "Could not load image: " + ex.getMessage() );
        }
        assertNotNull( img );
        assertEquals( img.getCamera(), "Minolta Co., Ltd. DiMAGE 7i" );
        assertEquals( img.getAperture(), 3.5 );
        assertEquals( img.getShutterSpeed(), 0.1 );
        RenderedImage ri = img.getRenderedImage( 1.0, false );
        assertEquals( 2560, ri.getWidth() );
        assertEquals( 1920, ri.getHeight() );
    }
    
    /**
     * Test that an attempt to read nonexistent file works correctly.
     */
    @Test
    public void testReadNonExistent() {
        File f = null;
        try {
            f = File.createTempFile("pv_testfile", "jpg");
        } catch (IOException ex) {
            fail( "Could not create temp file: " + ex.getMessage() );
        }
        f.delete();
        ImageIOImage img = null;
        PhotovaultImageFactory fact = new PhotovaultImageFactory();
        try {
            img = (ImageIOImage) fact.create(f, false, false);
        } catch (PhotovaultException ex) {
            fail( "Could not load image: " + ex.getMessage() );
        }
        assertNull( img );        
    }
    
    /**
     * Test that reading a file that is not a recognized iamge works correctly
     */
    @Test
    public void testReadNonImage() {
        File f = new File ( "testfiles", "testconfig.xml" );
        ImageIOImage img = null;
        PhotovaultImageFactory fact = new PhotovaultImageFactory();
        try {
            img = (ImageIOImage) fact.create(f, false, false);
        } catch (PhotovaultException ex) {
            fail( "Could not load image: " + ex.getMessage() );
        }
        assertNull( img );        
    }    
    
    @Test
    public void testXmpMetadata() {
        File f = new File ( "testfiles", "xmptest.jpg" );
        PhotovaultImageFactory fact = new PhotovaultImageFactory();
        ImageIOImage img = null;
        try {
            img = (ImageIOImage) fact.create(f, false, true);
        } catch (PhotovaultException ex) {
            fail( "Could not load image: " + ex.getMessage() );
        }
        assertNotNull( img );
        
    }
}

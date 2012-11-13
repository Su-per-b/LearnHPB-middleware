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

import java.io.File;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.photovault.common.PhotovaultException;

/**
 *
 * @author harri
 */
public class Test_RawImage extends TestCase {
    
    /** Creates a new instance of Test_RawImage */
    public Test_RawImage() {
    }
    
    File f = new File( "/tmp/img_2434.cr2" );
    
    public void testColorTempConversion() {
        RawImage img;
        try {
            img = new RawImage(f);
            img.setColorTemp( 5500 );
            img.setColorTemp( 2000 );
            img.setColorTemp( 8000 );
            img.setColorTemp( 10000 );
        } catch (PhotovaultException ex) {
            fail( ex.getMessage() );
        }
    }

    
    public static void main( String[] args ) {
	junit.textui.TestRunner.run( suite() );
    }
    
    public static Test suite() {
	return new TestSuite( Test_RawImage.class );
    }
            
}

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

package org.photovault.dcraw;

import org.testng.annotations.Test;

/**
 *
 * @author harri
 */
public class Test_LibRaw {

    LibRaw lr = LibRaw.INSTANCE;

    @Test
    public void testRawOpen() {
        String version = lr.libraw_version();
        int vn = lr.libraw_versionNumber();
        LibRawData lrd = lr.libraw_init( 0 );
        lr.libraw_open_file( lrd, "/home/harri/tmp/IMG_4767.CR2" );
        System.out.println( "Camera: " + lrd.idata.getMake() + " " + lrd.idata.getModel() );
        System.out.println( "Shot at " + lrd.other.getTimestamp() );
        System.out.println( "Focal length: " + lrd.other.getFocalLen() );
        System.out.println( "ISO: " + lrd.other.getIsoSpeed() );
        System.out.println( "Shutter speed: " + lrd.other.getShutterSpeed() );
        System.out.println( "Aperture: F" + lrd.other.getAperture() );
        
        lr.libraw_recycle( lrd );
    }

}

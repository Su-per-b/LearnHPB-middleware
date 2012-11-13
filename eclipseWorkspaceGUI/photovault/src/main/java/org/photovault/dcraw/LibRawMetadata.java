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

import com.sun.jna.Structure;
import java.util.Date;

/**
 * Java wrapper for libraw structure libraw_imgother_t
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class LibRawMetadata extends Structure {
    public float iso_speed;
    public float shutter_speed;
    public float aperture;
    public float focal_len;
    public long timestamp;
    public int shot_order;
    public int[] gpsdata = new int[32];
    public byte[] desc = new byte[512];
    public byte[] artist = new byte[64];

    /**
     * @return the iso_speed
     */
    public float getIsoSpeed() {
        return iso_speed;
    }

    /**
     * @return the shutter_speed
     */
    public float getShutterSpeed() {
        return shutter_speed;
    }

    /**
     * @return the aperture
     */
    public float getAperture() {
        return aperture;
    }

    /**
     * @return the focal_len
     */
    public float getFocalLen() {
        return focal_len;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return new Date( 1000l * timestamp );
    }

    /**
     * @return the shot_order
     */
    public int getShotOrder() {
        return shot_order;
    }

    /**
     * @return the gpsdata
     */
    public int[] getGpsData() {
        return gpsdata;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return new String( desc );
    }

    /**
     * @return the artist
     */
    public String getArtist() {
        return new String( artist );
    }
}

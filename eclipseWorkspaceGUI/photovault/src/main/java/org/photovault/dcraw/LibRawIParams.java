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

/**
 * Java wrapper for libraw structure libraw_iparams_t
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class LibRawIParams extends Structure {
    public byte[] make = new byte[64];
    public byte[] model = new byte[64];
    public int raw_count;
    public int dng_version;
    public int is_foveon;
    public int colors;
    public int filters;
    public byte[] cdesc = new byte[5];

    /**
     * @return the make
     */
    @SuppressWarnings("empty-statement")
    public String getMake() {
        int len = 0;
        for ( len = 0; len < make.length && make[len] != 0; len++ );
        return new String( make, 0, len );
    }

    /**
     * @return the model
     */
    @SuppressWarnings("empty-statement")
    public String getModel() {
        int len = 0;
        for ( len = 0; len < model.length && model[len] != 0; len++ );
        return new String( model, 0, len );
    }

    /**
     * @return the raw_count
     */
    public int getRawCount() {
        return raw_count;
    }

    /**
     * @return the dng_version
     */
    public int getDngVersion() {
        return dng_version;
    }

    /**
     * @return the is_foveon
     */
    public boolean getIsFoveon() {
        return is_foveon != 0;
    }

    /**
     * @return the colors
     */
    public int getColors() {
        return colors;
    }

    /**
     * @return the filters
     */
    public int getFilters() {
        return filters;
    }

    /**
     * @return the cdesc
     */
    public byte[] getCdesc() {
        return cdesc;
    }
}

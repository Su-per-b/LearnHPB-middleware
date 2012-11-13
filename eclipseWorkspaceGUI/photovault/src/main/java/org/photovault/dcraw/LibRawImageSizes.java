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
 * Java wrapper for libraw structure libraw_image_sizes_t
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class LibRawImageSizes extends Structure {
    public short raw_height;
    public short raw_width;
    public short height;
    public short width;
    public short top_margin;
    public short left_margin;
    public short iheight;
    public short iwidth;
    public double pixel_aspect;
    public int flip;
    public short right_margin;
    public short bottom_margin;
}

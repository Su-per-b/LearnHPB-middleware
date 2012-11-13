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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * Structure libraw_masked_t designed for storing pixel data for dark frame
 * (black or masked pixels, not included in active image sensor area). These
 * pixel values can be used for black level subtraction, noise and banding
 * removal and so on.
 * <p>
 * Unlike imgdata.image bitmap which has 4-component pixels, masked pixel data
 * stored in 1-component 16-bit values. Different parts of border are stored in
 * different buffers within libraw_masked_t structure.
 * <p>
 * Some cameras does not provide dark frame data. In this case buffer for frame
 * data is not allocated and all pointers are initialized to zero. Also,
 * structure data is not allocated if image is extracted into half-sized bitmap
 * (i.e. if half_size, wavelet threshold or aber[] fields is set in processing
 * options).
 * <p>
 * Some cameras provides not full masked frame, but only several sides of it
 * (only left and top for Canons, only left and right for some Nikons and so
 * on). In this case all structure fields are initialized, but allocated size
 * for this part of frame is equal to zero and corresponding size parameter
 * (top_/left_/bottom_/right_margin) is set too zero too.
 *
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class LibRawMaskedArea extends Structure {
    /**
     * Whole allocated buffer. Buffer size is (raw_width*raw_height - width*height).
     */
    public Pointer buffer;
    /**
     * Pointer to part of buffer designated for storing top-left corner of black
     * frame. Data size equal to (top_margin*left_margin).
     */
    public Pointer tl;
    /**
     * Pointer to part of buffer for storing top part of black frame. Size is
     * (top_margin*width).
     */
    public Pointer top;
    /**
     * Pointer to right top corned data. Size is (top_margin*right_margin).
     */
    public Pointer tr;
    /**
     * Pointer to pixel data of left frame side. (left_margin*height) pixels.
     */
    public Pointer left;
    /**
     * Right side of frame. (right_margin*height) pixels.
     */
    public Pointer right;
    /**
     * Bottom left corner of frame. (bottom_margin*left_margin) pixels.
     */
    public Pointer bl;
    /**
     * Bottom side of frame. Pixel count is (bottom_margin*width).
     */
    public Pointer bottom;
    /**
     * Bottom right corner with (bottom_margin*right_margin) pixels.
     */
    public Pointer br;
    /**
     * Buffers containing black level data, read from RAW file. Each item
     * contains two elements, one for left half-row, one for right half row.
     * Number of items is equal to row count.
     */
    public Pointer ph1_black;
    // public Pointer ph1_black_right;


}

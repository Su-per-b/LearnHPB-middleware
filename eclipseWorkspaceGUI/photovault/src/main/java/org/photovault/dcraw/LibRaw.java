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

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Java wrapper for LibRaw C library.
 * @author Harri Kaimio
 * @since 0.6.0
 */
public interface LibRaw extends Library {

    public LibRaw INSTANCE = (LibRaw) Native.loadLibrary( "pvraw", LibRaw.class );

    /**
     * Initializes a new LibRaw processor
     * @param flags
     * @return Data structure that describes LibRaw state
     */
    LibRawData libraw_init( int flags );

    /**
     * Opens a raw image file and reads EXIF metadata from it.
     * @param lr The libraw instance
     * @param fname File name
     * @return
     */
    int libraw_open_file( LibRawData lr, String fname );

    /**
     * Unpacks the RAW files of the image, calculates the black level (not for
     * all formats), subtracts black (not for all formats). The results are
     * placed in imgdata.image.
     * @param lr handle to the libraw structure
     * @return
     */
    int libraw_unpack( LibRawData lr );

    /**
     * Reads (or unpacks) the image preview (thumbnail), placing the result into
     * the imgdata.thumbnail.thumb buffer.
     *
     * JPEG previews are placed into this buffer without any changes (with the
     * header etc.). Other preview formats are placed into the buffer in the
     * form of the unpacked bitmap image (three components, 8 bits per
     * component).
     * The thumbnail format is written to the imgdata.thumbnail.tformat field;
     * for the possible values, see description of constants and data structures.
     * @param lr
     * @return
     */
    int libraw_unpack_thumb( LibRawData lr );

    /**
     * Frees the allocated data of LibRaw instance, enabling one to process the
     * next file using the same processor. Repeated calls of recycle() are quite
     * possible and do not conflict with anything.
     * @param lr
     */
    void libraw_recycle( LibRawData lr );

    /**
     * Closes the libraw instance
     * @param lr
     */
    void libraw_close( LibRawData lr );

    /**
     * Returns the version of libraw
     */
    String libraw_version();

    int libraw_versionNumber();
    // Camera list
    String[] libraw_cameraList();

    int libraw_cameraCount();

    int libraw_adjust_sizes_info_only( LibRawData lr );

    int libraw_dcraw_document_mode_processing( LibRawData lr );

    /**
     *  The function emulates the postprocessing capabilities available in dcraw.
     * Called after calling LibRaw::unpack();
     *
     * The entire functionality of dcraw (set via the field values in
     * imgdata.params) is supported, except for dark frame subtraction and
     * work with bad pixels.
     * The function is intended solely for demonstration and testing purposes;
     * it is assumed that its source code will be used in most real applications
     * as the reference material concerning the order of RAW data processing.
     * @param lr
     * @return
     */
    int libraw_dcraw_process( LibRawData lr);

    
}

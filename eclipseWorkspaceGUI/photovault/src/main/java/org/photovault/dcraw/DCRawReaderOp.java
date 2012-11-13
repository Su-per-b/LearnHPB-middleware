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

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import javax.media.jai.ImageLayout;
import javax.media.jai.PlanarImage;
import javax.media.jai.SourcelessOpImage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JAI operator that reads raw Bayer data from raw image using libraw. The
 * produced image is a single channel, 16 bit image containing the raw data.
 * Bayer filter pattern is delivered in property "bayerfilter".
 * @author Harri Kaimio
 * @since 0.6.0
 */
class DCRawReaderOp extends SourcelessOpImage {

    static private final Log log = LogFactory.getLog( DCRawReaderOp.class );

    private LibRawData lrd;
    private LibRaw lr = LibRaw.INSTANCE;
    private File file;
    private boolean unpacked = false;
    private int bayerfilter;
    private int black;
    private float[] cam_mul;

    public DCRawReaderOp( File f, RenderingHints hints ) {
        super( null, hints, null, 1, 1, 1, 1 );
        file = f;
        lrd = lr.libraw_init( 0 );
        lrd.output_params.half_size = 1;
        lr.libraw_open_file( lrd, f.getAbsolutePath() );
        sampleModel = new PixelInterleavedSampleModel( DataBuffer.TYPE_USHORT,
                lrd.sizes.width, lrd.sizes.height,
                1, lrd.sizes.width, new int[] {0} );
        ColorSpace cs = ColorSpace.getInstance( ColorSpace.CS_GRAY );
        ColorModel c = new ComponentColorModel( cs, false,
                false, ColorModel.OPAQUE, DataBuffer.TYPE_USHORT );
        ImageLayout il = new ImageLayout( 0, 0, 256, 256, sampleModel, c );
        setImageLayout( il );
        minX = 0;
        minY = 0;
        width = lrd.sizes.width;
        height = lrd.sizes.height;
        bayerfilter = lrd.idata.filters;
        cam_mul = lrd.color.cam_mul;
        setProperty( "bayerfilter", lrd.idata.filters );
        setProperty( "dcraw_cam_mul", cam_mul );
        setProperty( "dcraw_cam_xyz", lrd.color.cam_xyz );
        setProperty( "dcraw_rgb_cam", lrd.color.rgb_cam );
        setProperty( "dcraw_black", lrd.color.black );
        setProperty( "dcraw_max", lrd.color.maximum );
        setProperty( "dcraw_black", lrd.color.black );
        setProperty( "dcraw_margin_top", lrd.sizes.top_margin );
        setProperty( "dcraw_margin_left", lrd.sizes.left_margin );
    }

    @Override
    protected void computeRect( PlanarImage[] srcs, WritableRaster dst, Rectangle area ) {
        log.debug( "entry: computeRect " + area );
        checkDataUnpacked();
        int[] intVals = new int[1];
        for ( int row = (int) area.getMinY() ; row < area.getMaxY() ; row++ ) {
            int py = row / 2;
            for ( int col = (int) area.getMinX() ; col < area.getMaxX() ; col++ ) {
                int px = col / 2;
                int color = fc( row, col );
                intVals[0] = lrd.image.getChar( 8*(py * lrd.sizes.iwidth + px) + 2 * color ) - black;
                if ( intVals[0] < 0 ) intVals[0] = 0;
                dst.setPixel( col, row, intVals );
            }
        }
        log.debug( "exit: computeRect" + area );
    }

    private synchronized void checkDataUnpacked() {
        if ( !unpacked ) {
            log.debug( "unpacking" );
            lrd.output_params.half_size = 1;
            lrd.output_params.four_color_rgb = 1;
            lr.libraw_unpack( lrd );
            black = lrd.color.black;
            unpacked = true;
        }
    }

    private int fc( int row, int col ) {
        row += lrd.sizes.top_margin;
        col += lrd.sizes.left_margin;
        return (bayerfilter >> ((((row) << 1 & 14) + ((col) & 1)) << 1) & 3);
//        int pos = 2 * ( row % 8 ) + ( col % 2 );
//        return (bayerfilter >> ( 2 * pos )) & 0x3;
    }

}

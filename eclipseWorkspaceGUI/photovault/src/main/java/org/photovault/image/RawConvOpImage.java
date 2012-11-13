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

package org.photovault.image;

import java.awt.RenderingHints;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import javax.media.jai.ImageLayout;
import javax.media.jai.PointOpImage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JAI operator for doing exposure & contrast processing for raw image (or any
 * linear RGB, 16 bit image for that matter)
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class RawConvOpImage extends PointOpImage {

    private static final Log log = LogFactory.getLog( RawConvOpImage.class );
    private RenderedImage source;

    /**
     * White point of the image
     */
    private int white;

    /**
     * Black point of the image
     */
    private int black;

    /**
     * Highlight compression.
     * <ul>
     * <li>1.0 - No highlight compression </li>
     * <li>below 1.0 - highlights are exaggerated so that {@link white}/hlightComp
     * is seen as white</li>
     * <li> above 1.0 - the highlights are compressed
     *
     */
    private double hlightComp;

    /**
     * Default contructor
     * @param src SOurce image
     * @param white white point
     * @param black black point
     * @param hlightComp
     * @param layout
     * @param hints
     * @param b
     */
    public RawConvOpImage( RenderedImage src, int white, int black, double hlightComp,
            ImageLayout layout, RenderingHints hints, boolean b ) {
        super(src, layout, hints, b );
        this.source = src;
        this.white = white;
        this.black = black;
        this.hlightComp = hlightComp;
    }

    /**
     * Compute single tile of the image
     * @param x
     * @param y
     * @return
     */
    @Override
    public Raster computeTile( int x, int y) {
        if ( contrastLut == null ) {
            createLumLut();
        }
        Raster r = source.getTile( x, y );
        WritableRaster w = r.createCompatibleWritableRaster(
                r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight() );
        int startX = r.getMinX();
        int startY = r.getMinY();
        for ( int l = startY ; l < startY + r.getHeight() ; l++ ) {
            for ( int c = startX; c < startX + r.getWidth(); c++ ) {
                long sr = r.getSample( c, l, 0 );
                long sg = r.getSample( c, l, 1 );
                long sb = r.getSample( c, l, 2 );
                long avg = (sr + sg + sb) / 3;
                long m = contrastLut[(int) avg];
                long[] pixel = new long[3];
                pixel[0] = (sr*m) >> 16;
                pixel[1] = (sg*m) >> 16;
                pixel[2] = (sb*m) >> 16;
                long clippedSum = 0;
                long totalHeadroom = 0;
                boolean clipped[] = new boolean[3];
                int channelsClipped = 0;
                int channelsUnclipped = 0;
                for ( int n = 0; n < 3; n++ ) {
                    if ( pixel[n] > 65535 ) {
                        channelsClipped++;
                        clipped[n] = true;
                        clippedSum += pixel[n] - 65535;
                    } else {
                        clipped[n] = false;
                        totalHeadroom += 65536-pixel[n];
                        channelsUnclipped++;
                    }
                }
                if ( channelsClipped > 0 ) {
                    for ( int n = 0; n < 3 ; n++ ) {
                        if ( !clipped[n] ) {
                            // Spread the clipped energy to other channels so that
                            // they reach saturation at the same time
                            long headroom = 65536 - pixel[n];
                            pixel[n] += clippedSum * headroom / totalHeadroom;
                        }
                    }
                }
//                while ( channelsClipped > 0 && clippedSum > 0 &&
//                        channelsUnclipped > 0 ) {
//                    long spreaded = 0;
//                    long spreadPerChan = clippedSum / channelsUnclipped +1;
//                    for ( int n = 0; n < 3; n++ ) {
//                        if ( !clipped[n] ) {
//                            long add = Math.min( spreadPerChan, 65536 - pixel[n] );
//                            pixel[n] += add;
//                            spreaded += add;
//                            if ( pixel[n] > 65535 ) {
//                                channelsUnclipped--;
//                            }
//                        }
//                    }
//                    clippedSum -= spreaded;
//                }
                try {
                    w.setSample( c, l, 0, Math.min( 65535, pixel[0] ) );
                    w.setSample( c, l, 1, Math.min( 65535, pixel[1] ) );
                    w.setSample( c, l, 2, Math.min( 65535, pixel[2] ) );
                } catch ( ArrayIndexOutOfBoundsException e ) {
                    log.error( e );
                }
            }
        }
        return w;
    }

    int contrastLut[] = null;

    private void createLumLut() {
        contrastLut = new int[65536];
        double  dw = white;
        int black = 0;
        double whiteLum = Math.pow( 2, hlightComp );
        for ( int n = 0; n < contrastLut.length; n++ ) {
            double r = ((double)(n-black))/dw;
            if ( r <= 0.00001 ) {
                contrastLut[n] = 0;
            } else {
                int v =  (int) ( 65536.0 * 65536.0 / dw * r*(1+(r/(whiteLum*whiteLum)))/(r*(1+r)) );
                contrastLut[n] = v;
            }
        }
    }

}

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
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Vector;
import javax.media.jai.ImageLayout;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JAI operator for interpolating 3 channel RGB image from raw Bayer data produced
 * by {@link DCRawReaderOp}. The resulting image is either interpolated using
 * the AHD algorithm or subsampled using box filter if the downsample parameter
 * is set to larger than 1.
 * <p>
 * The resulting image is in camera's native linear RGB color space, so it must
 * be later converted to e.g. sRGB. However, color balance is adjusted as
 * specified by the color multipliers given as paramteres.
 * @author Harri Kaimio
 * @since 0.6.0
 */
class AHDInterpolateOp extends OpImage {

    private static final Log log = LogFactory.getLog( AHDInterpolateOp.class );

    /**
     * Color channel multipliers used
     */
    private double mult[] = new double[4];
    /**
     * Conversion matrix from camera's color space to linear sRGB
     */
    private double[][] camToRGB;
    /**
     * Bayer filter pattern of source
     */
    private int bayerfilter;
    /**
     * Black level in the raw source image.
     */
    private int rawBlackLevel;
    /**
     * Downsampling factor. If 1, AHD interpolation is performed. If >1 the image
     * is simply downsampled using a box filter.
     */

    private int topMargin;

    private int leftMargin;
    
    private int downSample;

    static private ImageLayout layoutHelper( RenderedImage src, int downSample ) {
        int width = src.getWidth() / downSample;
        int height = src.getHeight() / downSample;
        PixelInterleavedSampleModel sampleModel = new PixelInterleavedSampleModel( DataBuffer.TYPE_USHORT,
                width, height,
                3, width * 3, new int[]{0, 1, 2} );
        ColorSpace cs = ColorSpace.getInstance( ColorSpace.CS_LINEAR_RGB );
        ColorModel c = new ComponentColorModel( cs, false,
                false, ColorModel.OPAQUE, DataBuffer.TYPE_USHORT );
        ImageLayout il = new ImageLayout( 0, 0, 256, 256, sampleModel, c );
        il.setWidth( width );
        il.setHeight( height );
        return il;
    }

    static private Vector sourceHelper( RenderedImage src ) {
        Vector v = new Vector();
        v.add( src );
        return v;
    }

    /**
     * Constructor
     * @param src Source image
     * @param rmult Red channel multiplier
     * @param gmult Green channel multiplier
     * @param bmult Blue channel multiplier
     * @param downsample Downsampling factor
     * @param hints Rendering hints
     */
    public AHDInterpolateOp( RenderedImage src, double rmult, double gmult,
            double bmult, int downsample, RenderingHints hints ) {
        super( sourceHelper( src ), layoutHelper( src, downsample ), hints, false );
        mult[0] = rmult;
        mult[1] = gmult;
        mult[2] = bmult;
        mult[3] = gmult;
        this.downSample = downsample;
        double maxMult = Math.max(  gmult, Math.max( rmult, bmult) );

        int rawMax = (Integer) src.getProperty( "dcraw_max" );
        rawBlackLevel = (Integer) src.getProperty( "dcraw_black" );
        double m = 0x10000 / (maxMult* (double) rawMax);
        for ( int n = 0 ; n < mult.length ; n++ ) {
            mult[n] *= m;
        }

        Object obj = src.getProperty( "bayerfilter" );
        bayerfilter = (Integer) obj;
        obj = src.getProperty( "dcraw_margin_top" );
        topMargin = (Short) obj;
        obj = src.getProperty( "dcraw_margin_left" );
        leftMargin = (Short) obj;


        camToRGB = new double[3][4];
        float[] rgb_cam = (float[]) src.getProperty( "dcraw_rgb_cam" );
        for ( int r = 0 ; r < 3; r++ ) {
            for ( int c = 0 ; c< 3 ; c++ ) {
                camToRGB[r][c] = rgb_cam[r*4+c];
            }
        }
        initCielabConv();
    }

    @Override
    public Rectangle mapDestRect( Rectangle src, int srcIndex ) {
        Rectangle ret = null;
        if ( downSample == 1 ) {
        ret = new Rectangle( (int) src.getMinX() - 3,
                (int) src.getMinY() - 3, (int) src.getWidth() + 6,
                (int) src.getHeight() + 6 );
        } else {
            ret = new Rectangle(
                    (int) src.getMinX() * downSample,
                    (int) src.getMinY() * downSample,
                    (int) src.getWidth() * downSample,
                    (int) src.getHeight() * downSample );
        }
        return ret;
    }

    @Override
    public Rectangle mapSourceRect( Rectangle source, int srcIndex ) {
        Rectangle ret = new Rectangle(
                (int) source.getMinX() + 3,  (int) source.getMinY() + 3,
                (int) source.getWidth() - 6, (int) source.getHeight() - 6 );
        return ret;
    }

    @Override
    protected void computeRect( PlanarImage[] img, WritableRaster dst, Rectangle area ) {
        log.debug( "computeRect " + area.toString() );
        if ( downSample == 1 ) {
            computeRectAHD( img, dst, area );
        } else {
            computeDownsample( img, dst, area );
        }

    }

    /**
     * Conpute a rectangle of destination image using AHD interpolation
     * @param img Array of soruce images (containing just one image in this case
     * @param dst Raster for the results
     * @param area Area of dst that needs to be computed
     */
    private void computeRectAHD( PlanarImage[] img, WritableRaster dst, Rectangle area ) {
        log.debug( "entry: computeAHD " + area );
        long entryTime = System.currentTimeMillis();

        // RandomIterFactory.create( img[0], area);
        int minx = Math.max(  (int)area.getMinX()-3, 0 );
        int miny = Math.max(  (int)area.getMinY()-3, 0 );
        int maxx = Math.min(  (int)area.getMaxX()+3, getWidth()-1 );
        int maxy = Math.min(  (int)area.getMaxY()+3, getHeight()-1 );
        Rectangle enlargedArea = new Rectangle( minx, miny,
                maxx-minx+1, maxy-miny+1 );
        int[][][][] rgb = new int[2][(int)enlargedArea.getWidth()][(int)enlargedArea.getHeight()][3];
        int[][][][] lab = new int[2][(int)enlargedArea.getWidth()][(int)enlargedArea.getHeight()][3];
        byte[][][] homo = new byte[2][(int)enlargedArea.getWidth()][(int)enlargedArea.getHeight()];

        RandomIter riter = RandomIterFactory.create( img[0], enlargedArea );

        double xyz[] = new double[3];
        // Copy the data to temp array
        for ( int y = 0 ; y < maxy-miny ; y++ ) {
            for ( int x = 0 ; x < maxx - minx ; x++ ) {
                int color = fc( y+miny, x+minx );
                if ( color == 3 ) color = 1;
                rgb[0][x][y][color] = rgb[1][x][y][color] =
                        (int) (mult[color] * riter.getSample( x+minx, y+miny, 0 ));

            }
        }

        // Interpolate green
        for ( int y = 2 ; y < maxy-miny-2; y++ ) {
            int firstColor = fc( y+miny, minx+3 );
            int startCol = minx+3-(firstColor%2);
            for ( int x = startCol-minx ; x < maxx-minx - 2 ; x+=2 ) {
                int c = fc( y+miny, x+minx );
                if ( c == 3 ) {
                    c = 1;
                }
                int tc = rgb[0][x][y][c];
                int eg = rgb[0][x-1][y][1];
                int wg = rgb[0][x+1][y][1];
                int sg = rgb[0][x][y+1][1];
                int ng = rgb[0][x][y-1][1];
                int ec = rgb[0][x-2][y][c];
                int wc = rgb[0][x+2][y][c];
                int sc = rgb[0][x][y+2][c];
                int nc = rgb[0][x][y-2][c];

                // Horizonally
                int green = 2 * ( wg + tc + eg ) - ( wc + ec );
                green >>= 2;
                if ( green < 0 ) green = 0;
                rgb[0][x][y][1] = green;
                // Vertically
                green = 2 * ( ng + tc + sg ) - ( nc + sc );
                green >>= 2;
                if ( green < 0 ) green = 0;
                rgb[1][x][y][1] = green;
            }
        }
        // Interpolate R & B
        for ( int dir = 0; dir < 2; dir++ ) {
            for ( int y = 3; y < maxy - miny - 3; y++ ) {
                for ( int x = 3; x < maxx - minx - 3; x++ ) {
                    int color = fc( y + miny, x + minx );
                    if ( color == 1 || color == 3 ) {
                        // We need to interpolate both red and blue
                        int c = fc( y + 1 + miny, x + minx );
                        int tg = rgb[dir][x][y][1];
                        int ng = rgb[dir][x][y - 1][1];
                        int sg = rgb[dir][x][y + 1][1];
                        int eg = rgb[dir][x + 1][y][1];
                        int wg = rgb[dir][x - 1][y][1];
                        int nc = rgb[dir][x][y - 1][c];
                        int sc = rgb[dir][x][y + 1][c];
                        int wo = rgb[dir][x - 1][y][2 - c];
                        int eo = rgb[dir][x + 1][y][2 - c];
                        int val = tg +
                                ((wo + eo - ng - sg) >> 1);
                        if ( val < 0 ) val = 0;
                        rgb[dir][x][y][2 - c] = val;
                        val = tg +
                                ((nc + sc - ng - sg) >> 1);
                        if ( val < 0 ) val = 0;
                        rgb[dir][x][y][c] = val;
                    } else {
                        /*
                        This pixel is either red or blue so only one of those needs
                        to be interpolated
                         */
                        int c = 2 - color;
                        int tg = rgb[dir][x][y][1];
                        int nwg = rgb[dir][x - 1][y - 1][1];
                        int seg = rgb[dir][x + 1][y + 1][1];
                        int swg = rgb[dir][x - 1][y + 1][1];
                        int neg = rgb[dir][x + 1][y - 1][1];
                        int nwc = rgb[dir][x - 1][y - 1][c];
                        int nec = rgb[dir][x + 1][y - 1][c];
                        int swc = rgb[dir][x - 1][y + 1][c];
                        int sec = rgb[dir][x + 1][y + 1][c];
                        int val =
                                tg + ((nwc + nec + sec + swc - nwg - neg - swg -
                                seg) >> 2);
                        if ( val < 0 ) val = 0;
                        rgb[dir][x][y][c] = val;
                    }
                    xyz[0] = xyz[1] = xyz[2] = 0.5;
                    // Convert to cielab
                    for ( int i = 0 ; i < 3; i++ ) {
                        xyz[0] += xyz_cam[0][i] * rgb[dir][x][y][i];
                        xyz[1] += xyz_cam[1][i] * rgb[dir][x][y][i];
                        xyz[2] += xyz_cam[2][i] * rgb[dir][x][y][i];
                    }
                    xyz[0] = cbrt[Math.max( 0, (int) Math.min( xyz[0], 65535.0 ) ) ];
                    xyz[1] = cbrt[Math.max( 0, (int) Math.min( xyz[1], 65535.0 ) ) ];
                    xyz[2] = cbrt[Math.max( 0, (int) Math.min( xyz[2], 65535.0 ) ) ];
                    lab[dir][x][y][0] = Math.max(0, (int) (64.0 * (116.0 * xyz[1] - 16.0)));
                    lab[dir][x][y][1] = 0x8000 + 10 * (int) (64.0 * 500.0 * (xyz[0] - xyz[1]));
                    lab[dir][x][y][2] = 0x8000 + 10 * (int) (64.0 * 200.0 * (xyz[1] - xyz[2]));
                }
            }
        }

        // Calculate the homogeneity maps
        int ldiff[][] = new int[2][4];
        int abdiff[][] = new int[2][4];
        int dx[] = { -1, 1, 0, 0};
        int dy[] = { 0, 0, -1, 1};
        for ( int y = 2; y < maxy - miny - 2; y++ ) {
            for ( int x = 2; x < maxx - minx - 2; x++ ) {
                for ( int d = 0; d < 2; d++ ) {
                    for ( int i = 0; i < 4; i++ ) {
                        ldiff[d][i] = Math.abs(
                                lab[d][x][y][0] -
                                lab[d][x + dx[i]][y + dy[i]][0] );
                        int da = lab[d][x][y][1] -
                                lab[d][x + dx[i]][y + dy[i]][1];
                        int db = lab[d][x][y][1] -
                                lab[d][x + dx[i]][y + dy[i]][1];
                        abdiff[d][i] = da * da + db * db;
                    }
                }
                int leps = Math.min( Math.max( ldiff[0][0], ldiff[0][1] ),
                        Math.max( ldiff[1][2], ldiff[1][3] ) );
                int abeps = Math.min( Math.max( abdiff[0][0], abdiff[0][1] ),
                        Math.max( abdiff[1][2], abdiff[1][3] ) );
                for ( int d = 0; d < 2; d++ ) {
                    for ( int i = 0; i < 4; i++ ) {
                        if ( ldiff[d][i] <= leps && abdiff[d][i] <= abeps ) {
                            homo[d][x][y]++;
                        }
                    }
                }
            }
        }


        int dstMinx = Math.max( (int)area.getMinX(), 5 );
        int dstMiny = Math.max( (int)area.getMinY(), 5 );
        int dstMaxy = Math.min( (int)area.getMaxY(), getHeight()-5 );
        int dstMaxx = Math.min( (int)area.getMaxX(), getWidth()-5 );
        for ( int row = dstMiny ; row < dstMaxy ; row++ ) {
            for ( int col = dstMinx ; col < dstMaxx ; col++ ) {
                int hm0 = 0, hm1 = 0;
                for ( int i = row-miny-1 ; i <= row-miny+1 ; i++ ) {
                    for ( int j = col-minx-1 ; j <= col-minx+1 ;j++ ) {
                        hm0 += homo[0][j][i];
                        hm1 += homo[1][j][i];
                    }
                }
                if ( hm0 < hm1 ) {
                    dst.setPixel( col, row, rgb[1][col-minx][row-miny] );
                } else if ( hm0 > hm1 ) {
                    dst.setPixel( col, row, rgb[0][col-minx][row-miny] );
                } else {
                    for ( int i = 0 ; i < 3; i++ ) {
                      rgb[0][col-minx][row-miny][i] += rgb[1][col-minx][row-miny][i];
                      rgb[0][col-minx][row-miny][i] /= 2;
                    }
                    dst.setPixel( col, row, rgb[0][col-minx][row-miny] );
                }
            }
        }
        long dur = System.currentTimeMillis()-entryTime;
        log.debug( "exit: computeRectAHD in " + dur + "ms" );
    }

    /**
     * Compute a rectangle of destination image by downsampling original
     * @param img Array of soruce images (containing just one image in this case
     * @param dst Raster for the results
     * @param area Area of dst that needs to be computed
     */
    private void computeDownsample( PlanarImage[] img, WritableRaster dst, Rectangle area ) {
        log.debug( "entry: computeDownsample " + area );
        long entryTime = System.currentTimeMillis();

        // Current line of image
        int rgb[][] = new int[(int)area.getWidth()][3];
        int sampleCount[][] = new int[(int)area.getWidth()][3];

        Rectangle srcArea = mapDestRect( area, 0 );
        int srcMinx = (int) srcArea.getMinX();
        int srcMaxx = (int) srcArea.getMaxX();
        int srcMiny = (int) srcArea.getMinY();
        int srcMaxy = (int) srcArea.getMaxY();
        int dstY = (int) area.getMinY();
        int dstMinx = (int) area.getMinX();
        int sampleY = 0;
        RandomIter riter = RandomIterFactory.create( img[0], srcArea );

        for ( int y = srcMiny ; y < srcMaxy ; y++ ) {
            int sampleX = 0;
            int dstX = 0;
            for ( int x = srcMinx ; x < srcMaxx ; x++ ) {
                int val = riter.getSample( x, y, 0 );
                int color = fc( y, x );
                color = ( color == 3 ) ? 1 : color;
                rgb[dstX][color] += val;
                sampleCount[dstX][color]++;
                sampleX++;
                if ( sampleX >= downSample ) {
                    dstX++;
                    sampleX = 0;
                }
            }
            sampleY++;
            if ( sampleY >= downSample ) {
                for ( int x = 0; x < rgb.length ; x++ ) {
                    for ( int c = 0 ; c < 3 ; c++ ) {
                        int count = sampleCount[x][c];
                        if ( count == 0 ) {
                            throw new IllegalStateException(
                                    "zero samples for color component" );
                        }
                        rgb[x][c] /= count;
                        rgb[x][c] = (int) (rgb[x][c] * mult[c]);
                    }
                    dst.setPixel( dstMinx+x, dstY, rgb[x] );
                }
                for ( int x = 0 ; x < rgb.length ; x++ ) {
                    for ( int c = 0 ; c < 3 ; c++ ) {
                        rgb[x][c] = 0;
                        sampleCount[x][c] = 0;
                    }
                }
                sampleY = 0;
                dstY++;
            }
        }
        long dur = System.currentTimeMillis()-entryTime;
        log.debug( "exit: computeDownsample in " + dur + "ms" );
    }

    /**
     * Helper function to find out the color of given pixel in original Bayer
     * data
     * @param row row of the pixel
     * @param col Column of the pixel
     * @return Color of the pixel (0 - red, 1 or 3 - green, 2 - blue)
     */
    private int fc( int row, int col ) {
        row+=topMargin;
        col+=leftMargin;
        return (bayerfilter >> ((((row) << 1 & 14) + ((col) & 1)) << 1) & 3);
//        int pos = 2 * ( row % 8 ) + ( col % 2 );
//        return (bayerfilter >> ( 2 * pos )) & 0x3;
    }

    /**
     * Initialize the lookup tables needed for converting calera data to CIElab
     */
    private void initCielabConv() {
        for ( int i = 0; i < 0x10000; i++ ) {
            double r = i / 65535.0;
            cbrt[i] = r > 0.008856 ? Math.pow( r, 1.0 / 3.0 )
                    : 7.787 * r + 16 / 116.0;
        }
        double[][] xyz_rgb = {
            {0.412453, 0.357580, 0.180423},
            {0.212671, 0.715160, 0.072169},
            {0.019334, 0.119193, 0.950227}
        };
        double[] d65_white = { 0.950456, 1, 1.088754 };
        xyz_cam = new double[3][3];

        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                xyz_cam[i][j] = 0.0;


                for ( int k = 0; k < 3; k++ ) {
                    xyz_cam[i][j] += xyz_rgb[i][k] * camToRGB[k][j] /
                            d65_white[i];
                }
            }
        }
    }

    private double cbrt[] = new double[0x10000];
    private double[][]xyz_cam = new double[3][3];
}

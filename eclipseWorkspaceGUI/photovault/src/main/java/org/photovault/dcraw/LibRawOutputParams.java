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
 * Used for management of dcraw-compatible calls dcraw_process(),
 * dcraw_ppm_tiff_writer(), dcraw_thumb_writer(), and
 * dcraw_document_mode_processing(). Fields of this structure correspond to
 * command line keys of dcraw.
 *
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class LibRawOutputParams extends Structure {

    /**
     * 4 numbers corresponding to the coordinates (in pixels) of the rectangle
     * that is used to calculate the white balance.
     */
    public int greybox[] = new int[4];
    /**
     * dcraw keys: -C
     * Correction of chromatic aberrations; the only specified values are
     * <ul>
     * <li>aber[0], the red multiplier</li>
     * <li>aber[2], the green multiplier. For some formats, it affects RAW data
     * reading, since correction of aberrations changes the output size. </li>
     * </ul>
     */
    public double aber[] = new double[4];
    /**
     * dcraw keys: -g power toe_slope
     * Sets user gamma-curve. Library user should set first two fields of gamm
     * array:
     * <ul>
     * <li>gamm[0] - inverted gamma value)</li>
     * <li>gamm[1] - slope for linear part (so called toe slope). Set to zero
     * for simple power curve.</li>
     * </ul>
     * Remaining 4 values are filled automatically.
     * <p>
     * By default settings for rec. BT.709 are used: power 2.222 (i.e.
     * gamm[0]=1/2.222) and slope 4.5. For sRGB curve use gamm[0]=1/2.4 and
     * gamm[1]=12.92, for linear curve set gamm[0]/gamm[1] to 1.0.
     */
    public double gamma[] = new double[6];
    /**
     * dcraw keys: -r mul0 mul1 mul2 mul3
     * <p>
     * 4 multipliers (r,g,b,g) of the user's white balance.
     */
    public float user_mul[] = new float[4];
    /**
     * dcraw keys: -s
     * <p>
     * Selection of image number for processing (for formats that contain 
     * several RAW images in one file). The multi_out ( -s all) mode should be 
     * programmed by the user, since dcraw_process() does not support it. 
     */
    public int shot_select;
    /**
     * dcraw keys: -b
     * <p>
     * Brightness (default 1.0). 
     */
    public float bright;
    /**
     * dcraw keys: -n
     * <p>
     * Parameter for noise reduction through wavelet denoising. 
     */
    public float thereshold;
    /**
     * dcraw keys: -h
     * <p>
     * Outputs the image in 50% size. For some formats, it affects RAW data 
     * reading. 
     */
    public int half_size;
    /**
     * dcraw keys: -f
     * <p>
     * Switches on separate interpolations for two green components.
     */
    public int four_color_rgb;
    /**
     * dcraw keys: -d/-D
     * <p>
     * <ul>
     * <li>0: standard processing (with white balance)</li>
     * <li>1: corresponds to -d (without color processing or debayer)</li>
     * <li>2: corresponds to -D (-d without white balance). </li>
     * </ul>
     */
    public int document_mode;
    /**
     * dcraw keys: -H
     * <p>
     * 0-9: Highlight mode (0=clip, 1=unclip, 2=blend, 3+=rebuild). 
     */
    public int highlight;
    /**
     * dcraw keys: -a
     * <p>
     * Use automatic white balance obtained after averaging over the entire image.
     */
    public int use_auto_wb;
    /**
     * dcraw keys: -w
     * <p>
     * If possible, use the white balance from the camera.
     */
    public int use_camera_wb;
    /**
     * dcraw keys: +M/-M
     * <p>
     * Use (1)/don't use camera color matrix. 
     */
    public int use_camera_matrix;
    /**
     * dcraw keys: -o
     * <p>
     * [0-5] Output colorspace (raw, sRGB, Adobe, Wide, ProPhoto, XYZ). 
     */
    public int output_color;
    /**
     * dcraw keys: -o filename
     * <p>
     * Path to output profile ICC file (used only if LibRaw compiled with LCMS 
     * support) 
     */
    public Pointer output_profile;
    /**
     * dcraw keys: -o file
     * <p>
     * Path to input (camera) profile ICC file (or 'embed' for embedded 
     * profile). Used only if LCMS support compiled in. 
     */
    public Pointer camera_profile;
    /**
     * dcraw keys: -P file
     * <p>
     * to file with bad pixels map (in dcraw format: "column row 
     * date-of-pixel-death-in-UNIX-format", one pixel per row). 
     */
    public Pointer bad_pixels;
    /**
     * dcraw keys: -K file
     * <p>
     * Path to dark frame file (in 16-bit PGM format) 
     */
    public Pointer dark_frame;
    /**
     * dcraw keys: -4
     * <p>
     * 8 bit (default)/16 bit (key -4). 
     */
    public int output_bps;
    /**
     * dcraw keys: -T
     * <p>
     * 0/1: output PPM/TIFF. 
     */
    public int output_tiff;
    /**
     * dcraw keys: -t
     * <p>
     * [0-7] Flip image (0=none, 3=180, 5=90CCW, 6=90CW). Default -1, which 
     * means taking the corresponding value from RAW.
     * <p>
     * For some formats, affects RAW data reading, e.g., unpacking of thumbnails 
     * from Kodak cameras.
     */
    public int user_flip;
    /**
     * dcraw keys: -q
     * <p>
     * 0-3: interpolation quality (0 - linear, 1- VNG, 2 - PPG, 3 - AHD).
     */
    public int user_qual;
    /**
     * dcraw keys: -k
     * <p>
     * User black level. 
     */
    public int user_black;
    /**
     * dcraw keys: -S
     * <p>
     * Saturation adjustment. 
     */
    public int user_sat;
    /**
     * dcraw keys: -m
     * <p>
     * Number of median filter passes.
     */
    public int med_passes;
    /**
     * dcraw keys:none
     * <p>
     * Portion of clipped pixels when auto brighness increase is used. Default 
     * value is 0.01 (1%) for dcraw compatibility. Recommended value for modern 
     * low-noise multimegapixel cameras depends on shooting style. Values in 
     * 0.001-0.00003 range looks reasonable.
     */
    public float auto_br_thr;
    public int use_fuji_rotate;
    public int filtering_mode;
}

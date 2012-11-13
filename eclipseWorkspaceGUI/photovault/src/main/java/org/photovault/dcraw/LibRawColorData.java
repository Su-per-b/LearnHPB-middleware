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
 * Java wrapper for libraw ColorData structure
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class LibRawColorData extends Structure {

    public int color_data_state;

    public short[] white_box = new short[64];
    public float[] cam_mul = new float[4];
    public float[] pre_mul = new float[4];
    public float[] cmatrix = new float[12];
    public float[] rgb_cam = new float[12];
    public float[] cam_xyz = new float[12];
    public short[] curve = new short[0x10000];
    public int black;
    public int maximum;

    public static class PhaseOneData extends Structure {
        public int format, key_off, t_black, black_off, split_col, tag_21a;
        public float tag_210;
    }

    public PhaseOneData ph1_data;
    public float flash_used;
    public float canon_ev;
    public byte[] model = new byte[64];
    public Pointer profile;
    public int profile_length;
}

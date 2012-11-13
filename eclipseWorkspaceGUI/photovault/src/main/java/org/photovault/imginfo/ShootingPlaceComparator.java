/*
  Copyright (c) 2006 Harri Kaimio
  
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


package org.photovault.imginfo;

import java.util.Comparator;
import java.util.Date;
    
/**
 * Comparator that orders the photos based on their Shooting place.
 */
public class ShootingPlaceComparator implements Comparator {
    public int compare( Object o1, Object o2 ) {
        PhotoInfo p1 = (PhotoInfo) o1;
        PhotoInfo p2 = (PhotoInfo) o2;
        String place1 = p1.getShootingPlace();
        String place2 = p2.getShootingPlace();
        if ( place1 == null ) {
            place1 = "";
        }
        if ( place2 == null ) {
            place2 = "";
        }
        int res = 0;
        res = place1.compareTo( place2 );
        return res;
    }
}
        
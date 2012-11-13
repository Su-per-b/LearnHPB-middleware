/*
  Copyright (c) 2006-2007 Harri Kaimio
  
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
 * Comparator that orders the photos based on their shooting date.
 */
public class ShootingDateComparator implements Comparator {
    public int compare( Object o1, Object o2 ) {
        PhotoInfo p1 = (PhotoInfo) o1;
        PhotoInfo p2 = (PhotoInfo) o2;
        Date d1 = p1.getShootTime();
        Date d2 = p2.getShootTime();
        int res = 0;
        if ( d1 == null ) {
            d1 = new Date( Long.MAX_VALUE );
        }
        if ( d2 == null ) {
            d2 = new Date( Long.MAX_VALUE );
        }
        res = d1.compareTo( d2 );
        
        return res;
    }
}
        
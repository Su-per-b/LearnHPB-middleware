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

package org.photovault.swingui;

import java.awt.event.ActionEvent;
import java.util.Comparator;
import java.util.UUID;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.photovault.imginfo.PhotoInfo;

/**
 * Sets the order of photos in thumbnail view. The order is determined by
 * comparator that is given as a parameter to constructor of this object. The
 * order is always guaranteed to be fully determined even if the comparator
 * regards two objects as the same - in this case the photos are ordered first
 * by their original file name and if these are equal as well by their UUID.
 * @author Harri Kaimio
 */
public class SetPhotoOrderAction extends AbstractAction  {

    /**
       Constructor.
       @param ctrl The controller for the views this action is associated with.
       The action gets the selection to export from this view.
       @param c Comparator object that is used to sort the photos
       @param text Test to display in menus
       @param icon Icon that is displayed in menus etc.
       @param desc Description of the action
       @param mnemonic mnemonic used for executing this action
    */
    public SetPhotoOrderAction( PhotoViewController ctrl, Comparator c,
            String text, ImageIcon icon, String desc, Integer mnemonic ) {
        super( text, icon );
        this.ctrl = ctrl;
        this.c = new FullOrderPhotoComparator( c );
        putValue( SHORT_DESCRIPTION, desc );
        putValue( MNEMONIC_KEY, mnemonic );
    }

    Comparator c;
    
    /**
     Helper class that forces an absolute order on the photos, i.e. it gurantees
     that even photos that would be compared as equal will always be sorted to
     same order.
     */
    static private class FullOrderPhotoComparator implements Comparator {
        public FullOrderPhotoComparator( Comparator comp ) {
            c = comp;
        }

        Comparator c;
        
        public int compare(Object o1, Object o2 ) {
            PhotoInfo p1 = (PhotoInfo) o1;
            PhotoInfo p2 = (PhotoInfo) o2;
            int res = c.compare( o1, o2 );
            if ( res == 0 ) {
                String name1 = p1.getOrigFname();
                String name2 = p2.getOrigFname();
                if ( name1 != null && name2 != null ) {
                    res = name1.compareTo( name2 );
                } else if ( name1 != null ) {
                    res = -1;
                } else if ( name2 != null ) {
                    res = 1;
                }
            }
            if ( res == 0 ) {
                UUID id1 = p1.getUuid();
                UUID id2 = p2.getUuid();
                res = id1.compareTo( id2 );
            }
            return res;
        }
    };
    
    public void actionPerformed( ActionEvent ev ) {
        ctrl.setPhotoComparator( c );
    }

    PhotoViewController ctrl;
}
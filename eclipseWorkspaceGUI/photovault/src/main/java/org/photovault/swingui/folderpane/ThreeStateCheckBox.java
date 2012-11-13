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

package org.photovault.swingui.folderpane;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JCheckBox;

/**
  This is an <strong>extremely</strong> simple implementation of a checkbox
 that can have a special "undefined" state in addition to the normal selected 
 and non-selected states. Although the logic is very simple compared to the 
 more complete implementations available, it fits perfectly into the puropose of
 folder trees. In most of those, the component traverses the states always in same 
 order. However, in photovault we don't want to go back to the undefined state 
 after we have left it.
 <p>
 Currently setting and unsetting the undefined state must be done by component user,
 it is not changed by the component itself. In future, this should be improved. Also
 the component should be tested with other LAFs than Metal.
 */
public class ThreeStateCheckBox extends JCheckBox {
    
    /**
     Size of the box to indicate undefined stated
     */
    final static int undefBoxSize = 9;
    
    /** Creates a new instance of ThreeStateCheckBox */
    public ThreeStateCheckBox() {
        super();
        
    }
    
    boolean undef = false;
    
    /**
     Sets whether this check box is displayer as undefined
     */
    public void setUndef( boolean isUndefined ) {
        undef = isUndefined;
    }
    
    public boolean getUndef() {
        return undef;
    }
            
    public void paint( Graphics g ) {
        super.paint( g );
        if ( undef ) {
            int w = getWidth();
            int h = getHeight();
            Color c = Color.GRAY;
            Color oldColor = g.getColor();
            g.setColor( c );
            g.fillRect( (w-undefBoxSize)/2, (h-undefBoxSize)/2,
                    undefBoxSize, undefBoxSize );
        }
    }
}

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

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.geom.AffineTransform;
import org.photovault.imginfo.*;
import org.photovault.imginfo.PhotoInfo;


/**
   This class implements a table renderer for displaying PhotoInfo objects as tumbnails
*/
class ThumbnailTableRenderer extends ThumbnailView implements TableCellRenderer {

    /**
       Constructor
    */
    public ThumbnailTableRenderer() {
	super();
	setOpaque( true );
    }

    /**
       Implementation of TableCellRenderer interface
    */
    public Component getTableCellRendererComponent( JTable table, Object obj, 
						    boolean isSelected, boolean hasFocus,
						    int row, int column) {
	PhotoInfo p = (PhotoInfo) obj;
	setPhoto(  p );
	if (isSelected) {
	    if (selectedBorder == null) {
		setBackground( table.getSelectionBackground() );
	    }
	    setBorder(selectedBorder);
	} else {
	    if (unselectedBorder == null) {
		setBackground( table.getBackground() );
	    }
	    setBorder(unselectedBorder);
	}
	return this;
    }

    Border selectedBorder = null;
    Border unselectedBorder = null;
}

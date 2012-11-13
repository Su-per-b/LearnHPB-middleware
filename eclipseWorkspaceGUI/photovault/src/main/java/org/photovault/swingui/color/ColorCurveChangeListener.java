/*
  Copyright (c) 2007 Harri Kaimio
  
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

package org.photovault.swingui.color;

import org.photovault.image.ColorCurve;

/**
 Listener interface for receiving events from {@link ColorCurvePanel}
 */
public interface ColorCurveChangeListener {
    
    /**
     This method is called every time use moves any of the control points
     @param p The {@link ColorCurvePanel} that initiated the event
     @param c Curve after applying the change
     */
    public void colorCurveChanging( ColorCurvePanel p, ColorCurve c );

    /**
     This method is called after user has completed changing a control point,
     i.e. releases mouse on it.
     @param p The {@link ColorCurvePanel} that initiated the event
     @param c Curve after applying the change
     */
    public void colorCurveChangeCompleted( ColorCurvePanel p, ColorCurve c );
    
}

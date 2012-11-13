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

import java.awt.geom.Rectangle2D;
import java.util.EventObject;

/**
 CropAreaChangeEvent describes a change to the @see JaiImageView croping area.
 */
public class CropAreaChangeEvent extends EventObject {
    
    private Rectangle2D cropArea;
    private double rotDegrees;
    
    /** Creates a new instance of CropAreaChangeEvent */
    public CropAreaChangeEvent( 
            Object source, 
            Rectangle2D cropArea, 
            double rotDegrees ) {
        super( source );
        this.cropArea = cropArea;
        this.rotDegrees = rotDegrees;
    }
    
    public Rectangle2D getCropArea() {
        return (Rectangle2D)cropArea.clone();
    }
    
    public double getRotation() {
        return rotDegrees;
    }
    
}

/*
  Copyright (c) 2008 Harri Kaimio
  
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

package org.photovault.imginfo.xml;

import java.awt.geom.Rectangle2D;
import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.xml.sax.Attributes;

/**
 Factory for generation Rectangle2D object from its XML representation
 */
public class RectangleFactory extends AbstractObjectCreationFactory {

    public RectangleFactory() {
        super();
    }

    public Object createObject( Attributes attrs ) {
        String xminStr = attrs.getValue( "xmin" );
        double xmin = Double.parseDouble( xminStr );
        String xmaxStr = attrs.getValue( "xmax" );
        double xmax = Double.parseDouble( xmaxStr );
        String yminStr = attrs.getValue( "ymin" );
        double ymin = Double.parseDouble( yminStr );
        String ymaxStr = attrs.getValue( "ymax" );
        double ymax = Double.parseDouble( ymaxStr );
        Rectangle2D r =
                new Rectangle2D.Double( xmin, ymin, xmax - xmin, ymax - ymin );
        return r;
    }
}

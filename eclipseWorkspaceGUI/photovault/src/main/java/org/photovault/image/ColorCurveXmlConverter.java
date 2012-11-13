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

package org.photovault.image;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * XStream converter for {@link ColorCurve} objects to XML and back
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class ColorCurveXmlConverter extends AbstractCollectionConverter {

    public ColorCurveXmlConverter( Mapper mapper ) {
        super( mapper );
    }

    @Override
    public boolean canConvert( Class clazz ) {
        return ColorCurve.class.equals( clazz );
    }

    @Override
    public void marshal( Object obj, HierarchicalStreamWriter writer,
            MarshallingContext ctx ) {
        ColorCurve curve = (ColorCurve) obj;
        for ( int n = 0; n < curve.getPointCount(); n++ ) {
            writer.startNode( "point" );
            writer.addAttribute( "x", Double.toString( curve.getX( n ) ) );
            writer.addAttribute( "y", Double.toString( curve.getY( n ) ) );
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal( HierarchicalStreamReader reader,
            UnmarshallingContext ctx ) {
         ColorCurve curve = new ColorCurve();
         while ( reader.hasMoreChildren() ) {
             reader.moveDown();
             if ( "point".equals( reader.getNodeName() ) ) {
                 double x = Double.parseDouble( reader.getAttribute( "x" ) );
                 double y = Double.parseDouble( reader.getAttribute( "y" ) );
                 curve.addPoint( x, y );
             }
             reader.moveUp();
         }
         return curve;
    }

}

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
import java.util.Map;

/**
 * XStream converter for {@link ChanMapOp}\ objects to XML and back
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class ChanMapOpXmlConverter extends AbstractCollectionConverter {

    public ChanMapOpXmlConverter( Mapper mapper ) {
        super( mapper );
    }

    @Override
    public boolean canConvert( Class clazz ) {
        return ChanMapOp.class.equals( clazz );
    }

    @Override
    public void marshal( Object obj, HierarchicalStreamWriter writer,
            MarshallingContext ctx ) {
        ChanMapOp map = (ChanMapOp) obj;
        for( Map.Entry<String, ColorCurve> e : map.channels.entrySet() ) {
            writer.startNode( "channel" );
            writer.addAttribute( "name", e.getKey() );
            ctx.convertAnother( e.getValue() );
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal( HierarchicalStreamReader reader,
            UnmarshallingContext ctx ) {
        ChanMapOp map = new ChanMapOp();
        while ( reader.hasMoreChildren() ) {
            reader.moveDown();
            if ( "channel".equals( reader.getNodeName() ) ) {
                String chanName = reader.getAttribute( "name" );
                ColorCurve curve =
                        (ColorCurve) ctx.convertAnother( ctx, ColorCurve.class );
                map.setChannel( chanName, curve );
            }
            reader.moveUp();
        }
        return map;
    }

}

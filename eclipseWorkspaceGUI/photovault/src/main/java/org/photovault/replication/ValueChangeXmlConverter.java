/*
  Copyright (c) 2008-2009 Harri Kaimio

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

package org.photovault.replication;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.util.Map;

/**
 XStream converter for (un)marshalling {@link ValueChange} instances
 @author Harri Kaimio
 @since 0.6.0
 */
public class ValueChangeXmlConverter implements Converter {

    private Mapper mapper;

    public ValueChangeXmlConverter( Mapper mapper ) {
        this.mapper = mapper;
    }
    
    public boolean canConvert( Class clazz ) {
        return clazz.equals( ValueChange.class );
    }

    public void marshal( Object obj, HierarchicalStreamWriter writer, MarshallingContext ctx ) {
        ValueChange ch = (ValueChange) obj;
        writer.addAttribute( "field", ch.getName() );
        for ( Map.Entry<String, Object> pc : ch.getPropChanges().entrySet() ) {
            String propName = ch.getPropName( pc.getKey() );
            writer.startNode( "prop" );
            writer.addAttribute( "name", propName );
            Object val = pc.getValue();
            if ( val != null ) {
                writer.startNode( mapper.serializedClass( val.getClass() ) );
                ctx.convertAnother( val );
                writer.endNode();
            } else {
                writer.startNode( "null" );
                writer.endNode();
            }
            writer.endNode();
        }
    }


    public Object unmarshal( HierarchicalStreamReader reader,
            UnmarshallingContext ctx ) {
        String field = reader.getAttribute( "field" );
        ValueChange ret = new ValueChange();
        ret.name = field;
        while ( reader.hasMoreChildren() ) {
            reader.moveDown();
            String nodeName = reader.getNodeName();
            if ( "prop".equals( nodeName ) ) {
                String propName = reader.getAttribute( "name" );
                reader.moveDown();
                String className = reader.getNodeName();
                Class clazz = mapper.realClass( className );
                Object val = ctx.convertAnother( ctx, clazz );
                ret.addPropChange( propName, val );
                reader.moveUp();
            } else {
                /*
                 * If the change contains nodes not named "prop" use them as value
                 * of the change for backward compatibility
                 */
                String className = reader.getNodeName();
                Class clazz = mapper.realClass( className );
                Object val = ctx.convertAnother( ctx, clazz );
                ret.setValue( val );
            }
            reader.moveUp();
        }
        return ret;
    }


}

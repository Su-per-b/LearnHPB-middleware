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

package org.photovault.replication;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.util.HashSet;
import java.util.Set;


/**
 XStream converter for (un)marshalling {@link SetChange} instances
 @author Harri Kaimio
 @since 0.6.0
 */
public class SetChangeXmlConverter implements Converter {
    private Mapper mapper;

    public SetChangeXmlConverter( Mapper mapper ) {
        this.mapper = mapper;
    }
    
    public boolean canConvert( Class clazz ) {
        return SetChange.class.equals( clazz );
    }

    public void marshal( Object obj, HierarchicalStreamWriter writer, MarshallingContext ctx ) {
        SetChange ch = (SetChange) obj;
        writer.addAttribute( "field", ch.getName() );
        writer.startNode( "add" );
        for ( Object val : ch.getAddedItems() ) {
            if ( val != null ) {
                writer.startNode( mapper.serializedClass( val.getClass() ) );
                ctx.convertAnother( val );
                writer.endNode();
            } else {
                writer.startNode( "null" );
                writer.endNode();
            }
        }
        writer.endNode();
        writer.startNode( "remove" );
        for ( Object val : ch.getRemovedItems() ) {
            if ( val != null ) {
                writer.startNode( mapper.serializedClass( val.getClass() ) );
                ctx.convertAnother( val );
                writer.endNode();
            } else {
                writer.startNode( "null" );
                writer.endNode();
            }
        }
        writer.endNode();
    }
    
    public Object unmarshal( HierarchicalStreamReader reader,
            UnmarshallingContext ctx ) {
        String f = reader.getAttribute( "field" );
        SetChange ch = new SetChange( f );
        while ( reader.hasMoreChildren() ) {
            reader.moveDown();
            if ( "add".equals( reader.getNodeName() ) ) {
                Set added = (Set) ctx.convertAnother( ch, HashSet.class );
                for ( Object o : added ) {
                    ch.addItem( o );
                }
            } else if ( "remove".equals( reader.getNodeName() ) ) {
                Set removed = (Set) ctx.convertAnother( ch, HashSet.class );
                for ( Object o : removed ) {
                    ch.removeItem( o );
                }
            }
            reader.moveUp();
        }
        return ch;
    }
    
}

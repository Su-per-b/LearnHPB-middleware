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

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 XStream converter for (un)marshalling {@link ChangeDTO} objects.
 <p>

 Note that output of this converter is used to calcolate identity of the change,
 so the ID cannot be included in it. Therefore the unmarshalled instance cannot
 be used as such either, and therefore this is not general purpose XML serializer.
 When unmarshalling changes from XML, the {@link ChangeDTO#createChange(byte[]) }
 method should be used instead.
 <h2>Format of generated XML</h2>
 Here is an example of the genrerated XML:
 <pre>
 <change targetId="ae8c0e8e-aa03-4c31-94fe-a76bda920690"
         targetClass="org.photovault.folder.PhotoFolder">
  <parent idref="6c1347cd-46b3-3e1d-bbcc-4beabd11c04b"/>
  <value-change field="name">
    <string>Koe</string>
  </value-change>
</change>
 </pre>

 TODO: actual schema definition is needed.
 @author Harri Kaimio
 @since 0.6.0
 */
class ChangeDtoXmlConverter<T> extends AbstractCollectionConverter {

    /**
     Constructor
     @param mapper Mapper used to map element names to Java classes
     */
    public ChangeDtoXmlConverter( Mapper mapper ) {
        super( mapper ); 
    }
    
    public boolean canConvert( Class clazz ) {
        return clazz.equals( ChangeDTO.class );
    }

    public void marshal( Object obj, HierarchicalStreamWriter writer, MarshallingContext ctx ) {
        ChangeDTO<T> ch = (ChangeDTO) obj;
        writer.addAttribute( "targetId", ch.targetUuid.toString() );
        writer.addAttribute( "targetClass", ch.targetClassName );
        for ( UUID pid : ch.parentIds ) {
            writer.startNode( "parent" );
            writer.addAttribute( "idref", pid.toString() );
            writer.endNode();
        }
        for ( Map.Entry<String, FieldChange> e : ch.changedFields.entrySet() ) {
            FieldChange c = e.getValue();
            writer.startNode( mapper().serializedClass( c.getClass() ) );
            ctx.convertAnother( c );
            writer.endNode();
        }
    }

    public Object unmarshal( HierarchicalStreamReader reader, UnmarshallingContext ctx ) {
        String idStr = reader.getAttribute( "id" );
        String targetId = reader.getAttribute( "targetId" );
        String targetClass = reader.getAttribute( "targetClass" );
        List<UUID> parentIds = new ArrayList<UUID>();
        SortedMap<String, FieldChange> fields = new TreeMap<String, FieldChange>();
        while ( reader.hasMoreChildren() ) {
            reader.moveDown();
            if ( "parent".equals( reader.getNodeName() ) ) {
                UUID pid = UUID.fromString( reader.getAttribute( "idref" ) );
                parentIds.add( pid );
            } else {
                String className = reader.getNodeName();
                Class clazz = mapper().realClass( className );
                FieldChange fc = (FieldChange) ctx.convertAnother( ctx, clazz );
                fields.put( fc.getName(), fc );
            }
            reader.moveUp();    
        }
        ChangeDTO dto = new ChangeDTO( );
        dto.targetClassName = targetClass;
        dto.targetUuid = UUID.fromString( targetId );
        dto.parentIds = parentIds;
        dto.changedFields = fields;
        return dto;
    }
        

}

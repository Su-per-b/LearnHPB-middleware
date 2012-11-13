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

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import org.photovault.imginfo.PhotoInfo;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 Test cases for change XML representation marshalling and unmarshalling
 @since 0.6.0
 @authod Harri Kaimio
 */
public class Test_ChangeXmlConversion {
    
    @Test
    public void testEmptyUnmarshal() throws UnsupportedEncodingException {
        String xml = "<change targetId=\"cd52f082-63d9-4822-9a71-3358ccd148a8\"  targetClass=\"org.photovault.inginfo.PhotoInfo\"    />";
        ChangeDTO dto = ChangeDTO.createChange( xml.getBytes( "utf-8" ), null );
        assertEquals( UUID.fromString( "cd52f082-63d9-4822-9a71-3358ccd148a8"), dto.targetUuid );
        assertEquals( "org.photovault.inginfo.PhotoInfo", dto.targetClassName );
        assertEquals( xml.getBytes( "utf-8" ), dto.getXmlData() );
    }
    
    @Test
    public void testValueFieldUnmarshal() throws UnsupportedEncodingException {
        String xml = "<change targetId=\"cd52f082-63d9-4822-9a71-3358ccd148a8\"  targetClass=\"org.photovault.inginfo.PhotoInfo\">\n"
                + "<parent idref=\"cae62f7e-58a4-4ca2-bd6b-1e1fdaa72944\"/>\n"
                + "<parent idref=\"cae62f7e-58a4-4ca2-bd6b-1e1fdaa72944\"/> \n"
                + "<value-change field=\"photographer\">\n"
                +   "<string>Harri Kaimio</string>\n" 
                + "</value-change>\n"
                + "<value-change field=\"nullField\">\n"
                +   "<null/>\n" 
                + "</value-change>\n"
                + "</change>";
        ChangeDTO dto = ChangeDTO.createChange( xml.getBytes( "utf-8" ), PhotoInfo.class );
        assertEquals( UUID.fromString( "cd52f082-63d9-4822-9a71-3358ccd148a8"), dto.targetUuid );
        assertEquals( "org.photovault.inginfo.PhotoInfo", dto.targetClassName );
        assertEquals( 2, dto.parentIds.size() );
        assertEquals( 2, dto.changedFields.size() );
        ValueChange fc = (ValueChange) dto.changedFields.get( "photographer" );
        ValueChange nullFc = (ValueChange) dto.changedFields.get( "nullField" );
        assertNull( nullFc.getValue() );
        assertEquals( xml.getBytes( "utf-8" ), dto.getXmlData() );
    }
    
    @Test
    public void testSetFieldMarshal() {
        ChangeDTO dto = new ChangeDTO();
        dto.targetClassName = "org.photovault.inginfo.PhotoInfo";
        SetChange ch = new SetChange( "folders" );
        ch.addItem( UUID.randomUUID() );
        ch.addItem( 4 );
        ch.addItem( "teststring" );
        dto.changedFields.put( "folders", ch );
        byte[] ser = dto.getXmlData();
    }

}

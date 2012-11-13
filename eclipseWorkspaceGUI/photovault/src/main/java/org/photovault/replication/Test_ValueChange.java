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

package org.photovault.replication;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import org.photovault.imginfo.PhotoInfo;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 * Thes cases for {@link ValueChange}.
 *
 * @since 0.6.0
 * @author Harri Kaimio
 */
public class Test_ValueChange {
    @Test
    public void testValueChangePropSet() {
        ValueChange c = new ValueChange( "test", 1 );
        assertEquals( 1, c.getValue() );
        c.addPropChange( "t1", 2 );
        assertEquals( 2, c.getPropChanges().get(  "test.t1" ) );
        c.addPropChange( "t1.sub2", 3 );
        assertEquals( 2, c.getPropChanges().get(  "test.t1" ) );
        assertEquals( 3, c.getPropChanges().get(  "test.t1.sub2" ) );
        c.addPropChange( "t1", 3 );
        assertEquals( 3, c.getPropChanges().get(  "test.t1" ) );
        assertFalse( c.getPropChanges().containsKey( "test.t1.sub2" ) );
    }

    @Test
    public void testConflicts() {
        ValueChange c1 = new ValueChange( "test", 1 );
        ValueChange c2 = new ValueChange( "test", 2 );
        ValueChange c3 = new ValueChange( "test", "t1", 1 );
        ValueChange c4 = new ValueChange( "test", "t2", 1 );
        assertTrue( c1.conflictsWith( c2 ) );
        assertTrue( c2.conflictsWith( c3 ) );
        assertTrue( c1.conflictsWith( c3 ) );
        assertTrue( c3.conflictsWith( c1 ) );
        assertFalse( c3.conflictsWith( c4 ) );
        assertFalse( c4.conflictsWith( c3 ) );
        assertFalse( c3.conflictsWith( c3 ) );

        c3.addPropChange( "t3", 3 );
        c4.addPropChange( "t3", 4 );
        assertTrue( c3.conflictsWith( c4 ) );
        c4.addPropChange( "t3", 3 );
        assertFalse( c3.conflictsWith( c4 ) );
        c4.addPropChange( "t3.sub1", 3 );
        assertTrue( c3.conflictsWith( c4 ) );
        c3.addPropChange( "t3.sub1", 3 );
        assertFalse( c3.conflictsWith( c4 ) );
        c4.addPropChange( "b2", 5 );
        assertFalse( c3.conflictsWith( c4 ) );
        c3.addPropChange( "c5", 2 );
        assertFalse( c3.conflictsWith( c4 ) );
        c3.addPropChange( "b2", 5 );
        assertFalse( c3.conflictsWith( c4 ) );
        c3.addPropChange( "b2", 4 );
        assertTrue( c3.conflictsWith( c4 ) );
    }

    @Test
    public void testAddChange() {
        ValueChange c1 = new ValueChange( "test", 1 );
        ValueChange c2 = new ValueChange( "test", 2 );
        c1.addChange( c2 );
        assertEquals( 2, c1.getValue() );
        ValueChange c3 = new ValueChange( "test", "t1", 4 );
        c1.addPropChange( "t1.sub1", 5 );
        c1.addChange( c3 );
        assertFalse( c1.getPropChanges().containsKey( "test.t1.sub") );
        assertEquals( 4, c1.getPropChanges().get( "test.t1" ) );

        ValueChange c4 = new ValueChange( "test", "t1.sub1", 6 );
        c1.addPropChange( "t1.sub", 5 );
        c3.addEarlier( c4 );
        assertEquals( 4, c1.getPropChanges().get( "test.t1" ) );
        assertFalse( c3.getPropChanges().containsKey( "test.t1.sub1" ) );
        c4.setValue( 7 );
        c3.addEarlier( c4 );
        assertEquals( 7, c3.getPropChanges().get( "test" ) );
        assertEquals( 4, c3.getPropChanges().get( "test.t1" ) );

    }

    @Test
    public void testMerge() {
        ValueChange ch1 = new ValueChange( "test", "t1", 1 );
        ValueChange ch2 = new ValueChange( "test", "t2", 2 );
        ValueChange ret1 = (ValueChange) ch1.merge( ch2 );
        assertEquals( ch1.getName(), ret1.getName() );
        assertEquals( 1, ret1.getPropChanges().get( "test.t1" ) );
        assertEquals( 2, ret1.getPropChanges().get( "test.t2" ) );
        ch2.addPropChange( "t1", 3 );
        ValueChange ret2 = (ValueChange) ch1.merge( ch2 );
        assertEquals( 2, ret1.getPropChanges().get( "test.t2" ) );
        Collection<FieldConflictBase> conflicts = ret2.getConflicts();
        assertEquals( 1, conflicts.size() );
    }

    @Test
    public void testXmlUnmarshall() throws UnsupportedEncodingException {
        String xml = "<change targetId=\"1ada2ec9-5c72-4f8d-a686-da9d9816429a\" targetClass=\"org.photovault.imginfo.PhotoInfo\">" +
                "<parent idref=\"8e25f7b3-f025-3b18-8b67-ebbbc1a46e18\"/>" +
                "<value-change field=\"processing\">" +
                "    <prop name=\"connection(crop.in)\">" +
                "      <string>raw-map.out</string>" +
                "    </prop>" +
                "    <prop name=\"head\">" +
                "      <string>crop.out</string>" +
                "    </prop>" +
                "    <prop name=\"operation(crop)\">" +
                "      <org.photovault.image.CropOp>" +
                "        <inputPorts>" +
                "          <entry>" +
                "            <string>in</string>" +
                "            <org.photovault.image.ImageOp_-Sink>" +
                "              <op class=\"org.photovault.image.CropOp\" reference=\"../../../..\"/>" +
                "              <name>in</name>" +
                "            </org.photovault.image.ImageOp_-Sink>" +
                "          </entry>" +
                "        </inputPorts>" +
                "        <outputPorts>" +
                "          <entry>" +
                "            <string>out</string>" +
                "            <org.photovault.image.ImageOp_-Source>" +
                "              <op class=\"org.photovault.image.CropOp\" reference=\"../../../..\"/>" +
                "              <name>out</name>" +
                "            </org.photovault.image.ImageOp_-Source>" +
                "          </entry>" +
                "        </outputPorts>" +
                "        <rot>-0.6847775070091451</rot>" +
                "        <minx>0.0878772801792451</minx>" +
                "        <maxx>0.9042857436256849</maxx>" +
                "        <miny>0.025577266850996164</miny>" +
                "        <maxy>0.7756266332931306</maxy>" +
                "      </org.photovault.image.CropOp>" +
                "    </prop>" +
                "  </value-change>" +
                "</change>";
        ChangeDTO dto = ChangeDTO.createChange( xml.getBytes( "utf-8"), PhotoInfo.class );

    }
}

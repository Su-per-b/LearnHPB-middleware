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

package org.photovault.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import org.apache.commons.digester.Digester;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import org.xml.sax.SAXException;


/**
 Unit tests for {@link ChannelMapOperation} and associated classes
 */
public class Test_ChannelMapOperation {
    
    
    /** Creates a new instance of Test_ChannelMapOperation */
    public Test_ChannelMapOperation() {
    }

    /**
     Test creation of ChannelMapOperations from scratch & from existing operations
     */
    @Test
    public void testMapCreation() throws IOException, ClassNotFoundException {
        ChannelMapOperationFactory f = new ChannelMapOperationFactory();
        ColorCurve r = new ColorCurve();
        r.addPoint( 0.0, 0.1 );
        r.addPoint( 0.2, 0.4 );
        r.addPoint( 1.0, 1.0 );
        f.setChannelCurve( "red", r );
        ColorCurve b = new ColorCurve();
        b.addPoint( 0.0, 0.2 );
        b.addPoint( 0.4, 0.4 );
        b.addPoint( 1.0, 0.9 );
        f.setChannelCurve( "blue", b );
        ChannelMapOperation o = f.create();
        ColorCurve r1 = o.getChannelCurve( "red" );
        assertEquals( r, r1 );
        ColorCurve b1 = o.getChannelCurve( "blue" );
        assertEquals( b, b1 );
        ColorCurve e1 = o.getChannelCurve( "nonexisting" );
        assertNull( e1 );        
        
        // Create a second factory using this object as a template
        ChannelMapOperationFactory f2 = new ChannelMapOperationFactory( o );
        ChannelMapOperation o2 = f2.create();
        assertEquals( o, o2 );
        
        
        ColorCurve r2 = new ColorCurve();
        f2.setChannelCurve( "red", r2 );
        ChannelMapOperation o3 = f2.create();
        assertFalse( o2.equals( o3 ) );

        // Check that serializing this class works
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( os );
        oos.writeObject( o2 );
        byte[] data = os.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream( data );
        ObjectInputStream ois = new ObjectInputStream( is );
        ChannelMapOperation o4 = (ChannelMapOperation) ois.readObject();
        assertEquals( o2, o4 );        
    }
    
    /**
     Test that converting ChannelMapOperation to its XML representation and back to 
     java object creates an identical object.
     */
    @Test
    public void testXmlConvert() {
        ChannelMapOperationFactory f = new ChannelMapOperationFactory();
        ColorCurve r = new ColorCurve();
        r.addPoint( 0.0, 0.1 );
        r.addPoint( 0.2, 0.4 );
        r.addPoint( 1.0, 1.0 );
        f.setChannelCurve( "red", r );
        ColorCurve b = new ColorCurve();
        b.addPoint( 0.0, 0.2 );
        b.addPoint( 0.4, 0.4 );
        b.addPoint( 1.0, 0.9 );
        f.setChannelCurve( "blue", b );
        ChannelMapOperation o = f.create();

        String xml = o.getAsXml();
        ChannelMapOperation o2 = null;
        Digester d = new Digester();
        d.addRuleSet( new ChannelMapRuleSet() );
        try {
            ChannelMapOperationFactory f2 =
                    (ChannelMapOperationFactory) d.parse( new StringReader(xml) );
            o2 = f2.create();
        } catch (SAXException ex) {
            fail( ex.getMessage() );
        } catch (IOException ex) {
            fail( ex.getMessage() );
        }
        assertEquals( o, o2 );
    }    
}

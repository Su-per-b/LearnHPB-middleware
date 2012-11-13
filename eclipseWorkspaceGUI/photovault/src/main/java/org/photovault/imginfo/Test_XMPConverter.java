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

package org.photovault.imginfo;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 Test cases for {@link XMPConverter}
 */
public class Test_XMPConverter {
    
    
    /**
     Invocation handler used in the test cases to verify that 
     {@link XMPConverter#updatePhoto(com.adobe.xmp.XMPMeta, org.photovault.imginfo.PhotoEditor) }
     calls the eidtor correctly.
     */
    static private class TestEditorInvocationHandler implements InvocationHandler {

        Map<String, Object> fields = new HashMap<String,Object>();

        public Object invoke( Object obj, Method method, Object[] args ) throws Throwable {
            if ( method.getName().startsWith( "set" ) ) {
                fields.put( method.getName(), args[0] );
            }
            return null;
        }
        
    }
    
    
    /**
     Tets that photo is updated correctly from XMP metadata.
     @throws java.io.FileNotFoundException
     @throws com.adobe.xmp.XMPException
     */
    @Test
    public void testPhotoUpdate() throws FileNotFoundException, XMPException {
        File f = new File( "testfiles", "xmpTest.xml" );
        InputStream is = new FileInputStream(  f );
        XMPMeta xmp = XMPMetaFactory.parse( is );
        XMPConverter c = new XMPConverter( null );
        TestEditorInvocationHandler ih = new TestEditorInvocationHandler();
        PhotoEditor e = (PhotoEditor) Proxy.newProxyInstance( 
                this.getClass().getClassLoader(), new Class[]{PhotoEditor.class}, ih );
        c.updatePhoto( xmp, e );
        assertEquals( "Harri Kaimio", ih.fields.get( "setPhotographer" ) );
        assertEquals( "Kartano jossa juhlitaan.", ih.fields.get( "setDescription" ) );                
    }

}

/*
  Copyright (c) 2008 Harri Kaimio
 
  This file is part of Photovault.
 
  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.
 
  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even therrore implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.
 
  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

package org.photovault.replication;

import java.lang.reflect.InvocationTargetException;
import org.photovault.imginfo.PhotoInfo;
import org.testng.annotations.Test;

/**
 Test cases for class descriptor
 */
public class Test_ClassDescriptor {

    @Test
    public void testClassAnalysis() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        VersionedClassDesc photoClassDesc = new VersionedClassDesc( PhotoInfo.class );
        PhotoInfo p = PhotoInfo.create();
        
//        assertEquals( "Harri", p.getPhotographer() );
//        assertEquals( "Harri", photoClassDesc.getFieldValue( p, "photographer" ) );
        
        ObjectHistory<PhotoInfo> h = p.getHistory();
        ObjectHistory h2 = photoClassDesc.getObjectHistory( p );
        assert( h == h2 );
        System.out.println( photoClassDesc );
    }
}

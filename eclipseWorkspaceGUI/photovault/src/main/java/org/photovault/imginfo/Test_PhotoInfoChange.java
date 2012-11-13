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

import java.util.UUID;
import org.photovault.replication.Change;
import org.photovault.replication.ObjectHistory;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.HibernateDtoResolverFactory;
import org.photovault.replication.VersionedObjectEditor;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 *
 * @author harri
 */
public class Test_PhotoInfoChange {
    
    @Test
    public void testPhotoChangeRecord() {
        PhotoInfo photo = PhotoInfo.create();
        ObjectHistory<PhotoInfo> history = photo.getHistory();
        Change<PhotoInfo> change = history.createChange();
        change.setField(PhotoInfoFields.PHOTOGRAPHER.getName(), "Harri" );
        change.setField(PhotoInfoFields.FSTOP.getName(), 5.6 );
        change.setField( "film", "Tri-X" );
        change.freeze();
        assertEquals( "Harri", photo.getPhotographer() );
        assertEquals( 5.6, photo.getFStop() );
        assertEquals( "Tri-X", photo.getFilm() );
    }


}

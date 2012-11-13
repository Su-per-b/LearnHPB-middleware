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

package org.photovault.folder;

import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.replication.ChangeDTO;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.HibernateDTOResolver;
import org.photovault.replication.HibernateDtoResolverFactory;
import org.photovault.replication.VersionedObjectEditor;

/**
 Resolve the uuid of parent folder in folder related {@link ChangeDTO} into the 
 actual folder. If no local copy of the folder exists, create one.
 
 @author Harri Kaimio
 @since 0.6.0
 */
public class ParentRefResolver extends HibernateDTOResolver<PhotoFolder, UUID> {

    Log log = LogFactory.getLog( ParentRefResolver.class.getName() );
    
    public PhotoFolder getObjectFromDto( UUID dto ) {
        if ( dto == null ) {
            return null;
        }
        PhotoFolder f = 
                (PhotoFolder) getSession().get(  PhotoFolder.class, dto );
        if ( f == null ) {
            DTOResolverFactory rf = new HibernateDtoResolverFactory( getSession() );
            VersionedObjectEditor<PhotoFolder> fe;
            try {
                fe = new VersionedObjectEditor<PhotoFolder>( PhotoFolder.class, dto, rf );
                fe.apply();
                f = fe.getTarget();
            } catch ( Exception ex ) {
                log.error( "Cannot isntantiate local copy of folder " + dto );
            }
        }
        return f;
    }

    public UUID getDtoFromObject( PhotoFolder object ) {
        return (object != null ) ? object.getUuid() : null;
    }

}

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

package org.photovault.imginfo.dto;

import java.util.UUID;
import org.photovault.folder.FolderPhotoAssociation;
import org.photovault.folder.PhotoFolder;
import org.photovault.folder.PhotoFolderDAO;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoDAO;
import org.photovault.persistence.DAOFactory;
import org.photovault.replication.HibernateDTOResolver;

/**
 DTO resolver for converting {@link FolderRefDTO} to {@link FolderPhotoAssociation}
 and vice versa.
 
 @author Harri Kaimio
 @since 0.6.0
 */
public class FolderRefResolver extends 
        HibernateDTOResolver<FolderPhotoAssociation, FolderRefDTO> {

    public FolderPhotoAssociation getObjectFromDto( FolderRefDTO dto ) {
        DAOFactory df = getDAOFactory();
        PhotoFolderDAO folderDao = df.getPhotoFolderDAO();
         PhotoInfoDAO photoDao = df.getPhotoInfoDAO();
        FolderPhotoAssociation a = 
                (FolderPhotoAssociation) getSession().get(  
                FolderPhotoAssociation.class,dto.getAssocId() );
        if ( a == null ) {
            a = new FolderPhotoAssociation();
            a.setUuid( dto.getAssocId() );
            getSession().saveOrUpdate( a );
        }
        UUID photoUuid = dto.getPhotoId();
        if ( photoUuid != null ) {
            PhotoInfo p = photoDao.findByUUID( photoUuid );
            if ( p != null ) {
                a.setPhoto( p );
            }
        }
        UUID folderUuid = dto.getFolderId();
        if ( folderUuid != null ) {
            PhotoFolder f = folderDao.findByUUID( folderUuid );
            if ( f != null ) {
                a.setFolder( f );
            }
        }
        return a;
    }

    public FolderRefDTO getDtoFromObject( FolderPhotoAssociation a ) {
        return new FolderRefDTO( a );
    }

}

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

import java.io.Serializable;
import java.util.UUID;
import org.photovault.folder.FolderPhotoAssociation;
import org.photovault.folder.PhotoFolder;
import org.photovault.imginfo.PhotoInfo;

/**
 Data transfer object for storing {@link FolderPhotoAssociation} in changes. It 
 stores only UUIDs of the folder and photo, so that the reference can later be
 contructed by {@link FolderRefResolver}.
 @author Harri Kaimio
 @since 0.6.0
 */
public class FolderRefDTO implements Serializable {
    
    /**
     UUID of the folder
     */
    private UUID assocId;

    /**
    UUID of the photo
     */
    private UUID photoId;

    /**
    UUID of the folder
     */
    private UUID folderId;
    
    /**
     Cosntruct new DTO
     @param a The folder-photo association used
     */
    public FolderRefDTO( FolderPhotoAssociation a ) {
        assocId = a.getUuid();
        PhotoFolder f = a.getFolder();
        if ( f != null ) {
            folderId = f.getUuid();
        }
        PhotoInfo p = a.getPhoto();
        if ( p != null ) {
            photoId = p.getUuid();
        }
    }

    public UUID getAssocId() {
        return assocId;
    }

    public UUID getPhotoId() {
        return photoId;
    }

    public UUID getFolderId() {
        return folderId;
    }
    
    @Override
    public boolean equals( Object o ) {
        if ( ! (o instanceof FolderRefDTO ) ) {
            return false;
        }
        FolderRefDTO that = (FolderRefDTO) o;
        return that.assocId.equals( assocId );
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.assocId != null ? this.assocId.hashCode() : 0);
        return hash;
    }
    
}

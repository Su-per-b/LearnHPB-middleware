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

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.imginfo.PhotoInfo;

/** 
 This class describes the association of a photo to certain folder. Due to 
 the fact that Photovault 0.6.0 database can be distributed it is normal that
 the database does not contain infomration about both of them. Therefore the
 association has been modeled as an entity object with its own ID which is 
 derived from the uuids of both the photo  and folder.

 @author Harri Kaimio
 @since 0.6.0
 @see PhotoFolder
 @see PhotoInfo
 */
@Entity(name = "pv_folder_photos")
public class FolderPhotoAssociation {

    static private Log log = LogFactory.getLog( FolderPhotoAssociation.class.getName() );
    /**
     UUID of this association
     */
    private UUID uuid;
    /**
     The photo
     */
    private PhotoInfo photo;
    /**
     The folder assocaited with the photo
     */
    private PhotoFolder folder;

    /**
     Default constructor
     */
    public FolderPhotoAssociation() {
    }

    /**
     Construct a new association between f & p
     @param f The folder
     @param p The photo
     */
    public FolderPhotoAssociation( PhotoFolder f, PhotoInfo p ) {
        photo = p;
        folder = f;
        uuid = calcUuid();
    }

    /**
     Calculate the uuid based on the photo and folder identities
     @return The calculated uuid
     */
    private UUID calcUuid() {
        if ( photo == null || folder == null ) {
            return null;
        }

        String uuidStr = folder.getUuid().toString() + photo.getUuid().toString();
        byte[] b;
        try {
            b = uuidStr.getBytes( "utf-8" );
            return UUID.nameUUIDFromBytes( b );
        } catch ( UnsupportedEncodingException ex ) {
            log.error( "UTF-8 not supported!!!", ex );
        }
        return null;
    }

    /**
     Check that the state of the object is not inconsistent with UUID. This can 
     be done only if both photo and folder are known
     @throws IllegalStateException if uuid is not consistent with photo and folder
     */
    private void checkState() {
        if ( photo != null && folder != null && uuid != null ) {
            UUID currentUuid = calcUuid();
            if ( !uuid.equals( currentUuid ) ) {
                throw new IllegalStateException( "Association state does not match uuid" );
            }
        }
    }

    /**
     Get the UUID of this association
     @return
     */
    @Id
    @Column(name = "assoc_uuid")
    @org.hibernate.annotations.Type(type = "org.photovault.persistence.UUIDUserType")
    public UUID getUuid() {
        return uuid;
    }

    /**
     Set the UUID for this association. Mostly for Hibernate use
     @param uuid
     */
    public void setUuid( UUID uuid ) {
        this.uuid = uuid;
        checkState();
    }

    /**
     Get the photo associated with folder by this object (if known)
     @return
     */
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "photo_uuid", nullable = true)
    public PhotoInfo getPhoto() {
        return photo;
    }

    /**
     Associate a photo with the folder
     @param photo
     @throws IllegalStateException If the uuid has already been defined and the 
     photo identity is inconsistent with it.
     */
    public void setPhoto( PhotoInfo photo ) {
        this.photo = photo;
        checkState();
    }

    /**
     Get the folder associated with photo by this object (if known)
     @return
     */
    @ManyToOne( fetch=FetchType.LAZY )
    @JoinColumn(name = "folder_uuid", nullable = true)
    public PhotoFolder getFolder() {
        return folder;
    }

    /**
     Associate a folder with the photo
     @param photo
     @throws IllegalStateException If the uuid has already been defined and the 
     folder identity is inconsistent with it.
     */
    public void setFolder( PhotoFolder folder ) {
        this.folder = folder;
        checkState();
    }

    @Override
    public boolean equals( Object o ) {
        if ( o == null || !(o instanceof FolderPhotoAssociation) ) {
            return false;
        }
        FolderPhotoAssociation other = (FolderPhotoAssociation) o;
        return other.uuid.equals( uuid );
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}

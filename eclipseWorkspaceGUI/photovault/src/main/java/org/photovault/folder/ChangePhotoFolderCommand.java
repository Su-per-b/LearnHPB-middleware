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

package org.photovault.folder;

import java.util.HashMap;
import java.util.Map;
import org.photovault.command.CommandException;
import org.photovault.command.DataAccessCommand;
import org.photovault.imginfo.ExternalVolume;
import org.photovault.persistence.HibernateUtil;
import org.photovault.replication.DTOResolverFactory;
import org.photovault.replication.VersionedObjectEditor;

/**
  Command object for changing the fields of {@link PhotoFolder}.
 */
public class ChangePhotoFolderCommand extends DataAccessCommand {
    
    /**
     Folder that will be changed. This instance can be either detached of associated
     with any persistence context.
     */
    private PhotoFolder folder = null;    
    
    /**
     Folder after change. This instance is in this command's persistence context 
     or detached if the context has been closed.
     */
    private PhotoFolder changedFolder = null;
    
    /**
     Fields that will be changed.
     */
    private Map<PhotoFolderFields,Object> changedFields = 
            new HashMap<PhotoFolderFields,Object>();
    
    
    enum PhotoFolderFields {
        NAME,
        DESCRIPTION,
        PARENT,
        EXTDIR
    }
    
    /** 
     Creates a new instance of ChangePhotoFolderCommand 
     @param f The folder that will be cneged. This instance can be detached
     or associated with any persistence context.
     */
    public ChangePhotoFolderCommand( PhotoFolder f ) {
        folder = f;
    }

    /**
     Get the folder that willb e changed.
     */
    public PhotoFolder getFolder() {
        return folder;
    }
    
    /**
     Get the changed folder.
     @param Detached instance of the changed folder (with changes applied) or 
     <code>null</code> if the change was not made.
     */
    public PhotoFolder getChangedFolder() {
        return changedFolder;
    }
    
    /**
     Set new name fo rthe folder
     @param newName
     */
    public void setName( String newName ) {
        changedFields.put( PhotoFolderFields.NAME, newName );
    }
    
    /**
     Set new description for the folder.
     @param newDesc
     */
    public void setDescription( String newDesc ) {
        changedFields.put( PhotoFolderFields.DESCRIPTION, newDesc );
    }
    
    /**
     Set new parent for the folder
     @param New parent. This can be eithe rdetached instance or associated with 
     any persistence context.
     */
    public void setParent( PhotoFolder parent ) {
        changedFields.put( PhotoFolderFields.PARENT, parent );
    }
    
    /**
     Set the external directory associated with this folder
     @param vol Extenral volume
     @param path relative path from volume root to the directory
     */
    public void setExtDir( ExternalVolume vol, String path ) {
        changedFields.put( PhotoFolderFields.EXTDIR, new ExternalDir( vol, path ) );
    }
    
    public void execute() throws CommandException {
        PhotoFolderDAO folderDAO = daoFactory.getPhotoFolderDAO();
        PhotoFolder f = folderDAO.findById( folder.getUuid(), false );
        DTOResolverFactory rf = daoFactory.getDTOResolverFactory();
        VersionedObjectEditor<PhotoFolder> ed = 
                new VersionedObjectEditor<PhotoFolder>( f, rf );
        FolderEditor fe = (FolderEditor) ed.getProxy();
        for ( Map.Entry<PhotoFolderFields,Object> change: changedFields.entrySet() ) {
            PhotoFolderFields field = change.getKey();
            switch ( field ) {
                case NAME:
                    fe.setName( (String) change.getValue() );
                    break;
                case DESCRIPTION:
                    fe.setDescription( (String) change.getValue() );
                    break;
                case PARENT:
                    PhotoFolder parent = (PhotoFolder) HibernateUtil.getSessionFactory().
                            getCurrentSession().merge( (PhotoFolder) change.getValue() );
                    fe.reparentFolder( parent );
                    break;
                case EXTDIR:
                    f.setExternalDir( (ExternalDir)change.getValue() );
            }
        }
        ed.apply();
        changedFolder = f;
    }   
}

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

package org.photovault.persistence;

import org.photovault.folder.FolderPhotoAssocDAO;
import org.photovault.folder.PhotoFolderDAO;
import org.photovault.imginfo.ImageDescriptorDAO;
import org.photovault.imginfo.ImageFileDAO;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoDAO;
import org.photovault.imginfo.VolumeDAO;
import org.photovault.replication.ChangeDAO;
import org.photovault.replication.DTOResolverFactory;

/**
  Abstract factory for creating a DAO.
 */
public abstract class DAOFactory {
    
    /**
     Create an instance of a DAO factory.
     @param factory Class of the created factory, e.g. {@link HIbernateDAOFactory}.
     */
    public static DAOFactory instance( Class factory ) {
        try {
            return (DAOFactory)factory.newInstance();
        } catch (Exception e ) {
            throw new RuntimeException( "Could not create new DAO factory " + factory, e );
        }
    }
    
    /**
     Create a new {@link PhotoInfoDAO}
     */
    public abstract PhotoInfoDAO getPhotoInfoDAO();
    /**
     Create a new {@link PhotoFolderDAO}
     */
    public abstract PhotoFolderDAO getPhotoFolderDAO();

    /**
     Create a new {@link ImageFileDAO}
     */
    public abstract ImageFileDAO getImageFileDAO();
    /**
     Create a new {@link ImageDescriptorDAO}
     */
    public abstract ImageDescriptorDAO getImageDescriptorDAO();
    
    public abstract VolumeDAO getVolumeDAO();
    
    public abstract ChangeDAO getChangeDAO( );
    
    public abstract DTOResolverFactory getDTOResolverFactory();
    
    public abstract FolderPhotoAssocDAO getFolderPhotoAssocDAO();

}
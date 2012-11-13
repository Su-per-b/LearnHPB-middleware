/*
  Copyright (c) 2007 Harri Kaimio
 
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

package org.photovault.imginfo;

import java.util.List;
import java.util.UUID;
import org.hibernate.Query;
import org.photovault.persistence.GenericHibernateDAO;

/**
 * Hibernate implementation of {@link ImageFileDAO}.
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class ImageFileDAOHibernate 
        extends GenericHibernateDAO<ImageFile, UUID>
        implements ImageFileDAO {

    /** Creates a new instance of ImageFileDAOHibernate */
    public ImageFileDAOHibernate() {
        super();
    }

    /**
     Find image files that match a given hash code
     @param hash The hash code to search for
     @return ImageFile with matching hash or <code>null</code> if no such file 
     found.
     */
    public ImageFile findImageFileWithHash( byte[] hash ) {
        Query q = getSession().createQuery( "from ImageFile where hash = :hash" );
        q.setBinary( "hash", hash );
        List<ImageFile> images = q.list();
        return images.isEmpty() ? null : images.get( 0 );
    }

    public ImageFile findFileInLocation(ExternalVolume volume, String string) {
        return (ImageFile) getSession().getNamedQuery( "findImageFileByLocation" ).
                setEntity( "volume", volume).setString( "fname", string ).
                uniqueResult();
    }
    
}   

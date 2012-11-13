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

/**
 Interface used to edit versioned {@link PhotoFolder} objects.
 
 @author Harri Kaimio
 @since 0.6.0
 */
public interface FolderEditor {
    /**
     Set name of the folder
     @param name
     */
    public void setName( String name );
    
    /**
     Set the description text for the folder
     @param desc
     */
    public void setDescription( String desc );
    
    /**
     Associate the folder with a new photo
     @param a
     */
    public void addPhotoAssociation( FolderPhotoAssociation a ); 
    
    /**
     Remove existing association from the folder
     @param a
     */    
    public void removePhotoAssociation( FolderPhotoAssociation a );

    /**
     Set new parent for the edited folder
     @param thirdFolder
     */
    public void reparentFolder( PhotoFolder thirdFolder );

}

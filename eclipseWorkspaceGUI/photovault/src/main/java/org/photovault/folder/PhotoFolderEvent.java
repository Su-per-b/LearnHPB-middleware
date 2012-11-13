/*
  Copyright (c) 2006 Harri Kaimio
  
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

import org.photovault.imginfo.*;
import org.photovault.imginfo.PhotoCollectionChangeEvent;

/**
 PhotoFolderEvent describes an event that has changed a PhotoFolder. It 
 extends @see PhotoCollectionChangeEvent by providing information about the 
 subfolder that has changed it thas has been the case.
*/

public class PhotoFolderEvent extends PhotoCollectionChangeEvent {

    PhotoFolder subfolder = null;
    PhotoFolder[] path = null;

    /**
     Constructor
     @param source The PhotoFolder object that has initiateed the event
     @param subfolder If the evenbt has been created by a change to a subfolder, 
     reference to it. Otherwise <code>null</code>.
     @param path Array of PhotoFolders that describes the hierarchy from source 
     to subfolder.
     */
    public PhotoFolderEvent( PhotoFolder source, PhotoFolder subfolder, PhotoFolder[] path ) {
	super( source );
	this.subfolder = subfolder;
	this.path = (path != null ) ? path.clone() : null;
    }

    public PhotoFolder getSubfolder() {
	return subfolder;
    }

}

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
import org.photovault.imginfo.PhotoCollectionChangeListener;


/**
   PhotoFolderChangeListener is an extension of PhotoCollectionChangeListener that
   contains methods to inform also about changes to subfolders or subfolder hierarchy.
   <p>
   Note that these methods are called in the thread that initiated the change 
   operation. Specifially, the call is <strong>not</strong> guaranteed to happen
   in AWT event thread.
*/

public interface PhotoFolderChangeListener extends PhotoCollectionChangeListener {

    /**
       This method is called when attributes of a subfolder are changed
    */
    public void subfolderChanged( PhotoFolderEvent e );

    /**
       This method is called when the sutructure under the folder is radically changed
    */
    public void structureChanged( PhotoFolderEvent e );
}

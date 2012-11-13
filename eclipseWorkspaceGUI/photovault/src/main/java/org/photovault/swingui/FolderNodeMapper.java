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

package org.photovault.swingui;

import org.photovault.folder.PhotoFolder;

/**
 Mapping from photo folders to the objects that {@link PhotoFolderTreeModel}
 shows to Swing tree component.
 <p>
 Since in different situations the representation of the nodes in folder tree may
 vary, an object that implements this interface can map the actual folders
 and represent them in the tree model.
 */
public interface FolderNodeMapper {
    /**
     Get the tree node that represents the given folder
     @param f The folder to map
     @return An object that represents the folder in the tree.
     */
    Object mapFolderToNode( PhotoFolder f );
    
    /**
     Get the folder that corresponds to a given tree node
     */
    PhotoFolder mapNodeToFolder( Object o );
}

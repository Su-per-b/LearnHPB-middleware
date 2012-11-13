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

package org.photovault.swingui.folderpane;

import java.util.HashMap;
import org.photovault.folder.PhotoFolder;
import org.photovault.swingui.FolderNodeMapper;

/**
 Maps PhotoFolder and respective FolderNode to each other
 */
class FolderToFolderNodeMapper implements FolderNodeMapper {
    
    /** Creates a new instance of FolderToFolderNodeMapper */
    public FolderToFolderNodeMapper( Object [] model ) {
        this.model = model;
        folderToNode = new HashMap();
        nodeToFolder = new HashMap();
    }
    Object[] model;
    HashMap folderToNode;
    HashMap nodeToFolder;
    
    public Object mapFolderToNode(PhotoFolder f) {
        if ( f != null && !folderToNode.containsKey( f ) ) {
            FolderNode node = new FolderNode( model, f );
            folderToNode.put( f, node );
            nodeToFolder.put( node, f );
        }
        return folderToNode.get( f );
    }

    public PhotoFolder mapNodeToFolder(Object o) {
        return (PhotoFolder) nodeToFolder.get( o ); 
    }
    
}

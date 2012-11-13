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

import java.util.UUID;
import org.photovault.command.CommandException;
import org.photovault.command.DataAccessCommand;

/**
  Command object for deleting a {@link PhotoFolder}. After the folder is 
 deleted, its previous parent is stored into parentFolder.
 
 */
public class DeletePhotoFolderCommand extends DataAccessCommand {

    /**
     UUID of the folder to delete
     */    
    UUID folderId = null;
    /**
     Parent folder of the deleted folder.
     */
    PhotoFolder parentFolder = null;
    
    /** 
     Creates a new instance of DeletePhotoFolderCommand 
     @param f The folder that will be deleted by this command. Can be detached or
     associated with any persistence context.
     */
    public DeletePhotoFolderCommand( PhotoFolder f ) {
        super();
        folderId = f.getUuid();        
    }

    /**
     Creates a new instance of DeletePhotoFolderCommand
     @param uuid UUID of the folder that will be deleted.
     */
    public DeletePhotoFolderCommand( UUID uuid ) {
        super();
        folderId = uuid;
    }
    
    /**
     Get the folder that was parent of the deleted folder.
     @return Parent of deleted folder or <code>null</code> if the command has
     not been executed. Note that the returned object is in detached state if 
     the command has been executed in its own persistence context.
     */
    public PhotoFolder getParentFolder() {
        return parentFolder;
    }
    
    public void execute() throws CommandException {
        PhotoFolderDAO folderDAO = daoFactory.getPhotoFolderDAO();
        PhotoFolder f = folderDAO.findByUUID( folderId );
        PhotoFolder parent = f.getParentFolder();
        f.delete();
        folderDAO.makeTransient( f );
        parentFolder = parent;
    }
}

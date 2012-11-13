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

package org.photovault.imginfo.indexer;

/**
 Objects that implement this interface can register to be notified of events
 in indexing a directory hierarchy
 */
public interface ExtVolIndexerListener {
    /**
     This method is called when a new file has been indexed, whether it is found 
     to be a valid photo intanc or not.
     @param e ExtVolIndexrEvent describing the indexed file
     */
    void fileIndexed( ExtVolIndexerEvent e );
    
    /**
     This method is called after the indexing operation has been completed
     @param indexer The indexer that completed its operation
     */
    void indexingComplete( ExtVolIndexer indexer );
    
    /**
     This method is called if an unrecovable error happens while indexing 
     the volume.
     @param message Message that describes the error
     */
    void indexingError( String message );
}

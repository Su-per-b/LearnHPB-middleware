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

import java.io.File;
import java.util.EventObject;
import org.photovault.imginfo.PhotoInfo;

/**
 ExtVolIndexerEvent describes the result of indexing a file.
 */
public class ExtVolIndexerEvent extends EventObject {
    
    /** Creates a new instance of ExtVolIndexerEvent */
    public ExtVolIndexerEvent( Object source ) {
        super( source );
    }
    
    // Result codes
    
    /**
     Something is seriously wrong, result code was not set.
     */
    final public static int RESULT_INVALID = 0;
    
    /**
     The result of indexing operation was that a new PhotoInfo was created
    */
    final public static int RESULT_NEW_PHOTO = 1;

    /**
     The result of indexing operation was that a new instance was added to an 
     existing PhotoInfo
    */
    final public static int RESULT_NEW_INSTANCE = 2;

    /**
     The indexed file was not an image
    */
    final public static int RESULT_NOT_IMAGE = 3;

    /**
     Error was encountered while indexing the file
    */
    final public static int RESULT_ERROR = 4;
    
    /**
     Result code of the indexing operation
     */
    int result;
    
    /**
     Get the result code
     */
    public int getResult() {
        return result;
    }
            
    /**
     Set the result code
     @param res Result code
     */
    public void setResult( int res ) {
        result = res;
    }
    
    /**
     File that was iedxed in this operation
     */
    private File indexedFile;

    /**
     Get the file that was indexed in this operation
     @return The indexed file
     */
    public File getIndexedFile() {
        return indexedFile;
    }

    /**
     Sets the indexed file
     @param indexedFile File indexed
     */
    public void setIndexedFile(File indexedFile) {
        this.indexedFile = indexedFile;
    }

    /**
     The photo into which the indexed file was added as an instance or <code>null
     </code> if no photo was changed.
     */
    private PhotoInfo photo = null;

    /**
     Get the photo into which the indexed file was added as an instance
     @return The photo or <code>null</code> if it was not an image or error occurred.
     */
    public PhotoInfo getPhoto() {
        return photo;
    }

    /**
     Sets the photo
     @param photo The photo
     */     
    public void setPhoto(PhotoInfo photo) {
        this.photo = photo;
    }
}

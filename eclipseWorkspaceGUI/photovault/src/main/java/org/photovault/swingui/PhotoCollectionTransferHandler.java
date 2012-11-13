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

import java.io.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import javax.swing.*;
import org.photovault.imginfo.PhotoCollection;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.folder.PhotoFolder;
import org.photovault.imginfo.*;
import java.util.Collection;
import java.util.Iterator;


/**
   PhotoCollectionTransferHandler implements drag-n-drop and clipboard support 
   for @see PhotoCollectionThumbView. Currently data is only transferred inside
   the same application. Future improvement plans include
   <ul>
   <li> Transfer of persistent PhotoInfo objects between several applications based
   on photovault engine </li>
   <li> Transfer of images as files from Photovault database to other applications </li>
   <li> Importing files to Photovault database</li>
   <li> Transfer of image data to other applications </li>
   </ul>
*/

public class PhotoCollectionTransferHandler extends TransferHandler {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( PhotoCollectionTransferHandler.class.getName() );

    /**
       Data flavor for an array of PhotoInfo objects. This is used when transferring
       photos inside the same virtual machine
    */
    DataFlavor photoInfoFlavor = null;
    
    /**
       Array of photos that were transferred from the source
    */
    PhotoInfo[] sourcePhotos = null;

    PhotoCollectionThumbView view = null;
    
    /**
       Constructor
       @param c Component whose transfers are handled
    */
    public PhotoCollectionTransferHandler( PhotoCollectionThumbView c ) {
	super();
	view = c;
	try {
	    photoInfoFlavor = new DataFlavor( DataFlavor.javaJVMLocalObjectMimeType
					      + ";class=\"" + PhotoInfo[].class.getName()
					      + "\"" );
	} catch ( Exception e ) {
	}

    }

    /**
       Indicates whether a component would accept an import of the given set of
       data flavors prior to actually attempting to import it.

       @param c  the component to receive the transfer; this argument is provided
       to enable sharing of TransferHandlers by multiple components but is not used
       by this implementation
       @param flavors the data formats available
       @return true if the data can be inserted into the component, false otherwise
    */
    
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
	for (int i = 0; i < flavors.length; i++) {
	    if (photoInfoFlavor.equals(flavors[i])) {
		return true;
	    }
	}
	return false;
    }
    
    /**
     The collection into which the last import was done. This is used as a workaround
     to SWING design bug - we must know the import target in exportDone in order to 
     decide whether we need to remove the photos from folder or not
     */
    static private PhotoCollection lastImportTarget = null;
  
    /**
     Set the lastImportTarget global variable
     */
    static protected void setLastImportTarget( PhotoCollection c ) {
        lastImportTarget = c;
    }
    
    /**
       Causes a transfer to a component from a clipboard or a DND drop operation.
       The Transferable represents the data to be imported into the component.
    */
    
    public boolean importData(JComponent c, Transferable t) {
	log.warn( "importData" );
        if (canImport(c, t.getTransferDataFlavors())) {
            // TODO: change to use collection from controller
	    PhotoCollection collection = view.ctrl.getCollection();
	    if ( collection instanceof PhotoFolder ) {
		log.warn( "importing" );
		// Photos were dropped to a folder so we can insert them
		PhotoFolder folder = (PhotoFolder) collection;
                lastImportTarget = folder;
		try {
		    PhotoInfo[] photos = (PhotoInfo[])t.getTransferData(photoInfoFlavor);
		    for ( int n = 0; n < photos.length; n++ ) {
			folder.addPhoto( photos[n] );
		    }
		    return true;
		} catch (UnsupportedFlavorException ufe) {
		    log.warn("importData: unsupported data flavor");
		} catch (IOException ioe) {
		    log.warn("importData: I/O exception");
		}
	    }
	}
        return false;
    }

    /**
       Creates a Transferable to use as the source for a data transfer.
       Returns the representation of the data to be transferred, or null
       if the component's property is null
       @return A @see PhotoCollectionTransferable object that represents current
       selection of the component
    */
    protected Transferable createTransferable(JComponent c) {
	log.warn( "createTransferable" );
	Collection selection = view.getSelection();
	sourcePhotos = new PhotoInfo[selection.size()];
	Iterator iter = selection.iterator();
	int i = 0;
	while ( iter.hasNext() ) {
	    sourcePhotos[i] = (PhotoInfo) iter.next();
	    i++;
	}
	log.warn( "" + i + " photos selected" );
        PhotoCollection sourceCollection = view.ctrl.getCollection();
        return new PhotoCollectionTransferable( sourcePhotos );
    }
    
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    /**
       This method is called after the data has been exported. If the action was MOVE
       it removes all transferred photos from the folder unless the destination was the same
     @param c The source component of the tranfer
     @param data The data that was transferred of null if transfer action was NONE
     @param action The actual action that was performed
    */
    protected void exportDone(JComponent c, Transferable data, int action) {

        PhotoCollection collection = view.ctrl.getCollection();
        
        // Find out into which collection this transfer was done
        
        if ( (collection != lastImportTarget) && (action == MOVE) && collection instanceof PhotoFolder ) {
	    PhotoFolder folder = (PhotoFolder) collection;
	    for ( int i = 0; i < sourcePhotos.length; i++ ) {
		folder.removePhoto( sourcePhotos[i] );
	    }
        }
        lastImportTarget = null;
    }

    /**
     Representation of transfed data for PhotoInfo objects.
     
     This class includes a workaround to a design bug in SWING TransferHandler: we must
     know the destination collection in order to do DnD MOVE operation. DnD framework
     calls first importData() of the target location (which adds the photos to
     a collection) and then calls exportDone() of source TransferHandler <b>
     which deletes the photos just added if the source & destination components show
     the same component</b>. This bug seems to be fixed in Mustang but breaks API 
     compatibility.
     */
    class PhotoCollectionTransferable implements Transferable {
        private PhotoInfo[] photos = null;

        /**
         Constructor
         @param sourcePhotos Array og PhotoInfo objects that are transferred
         */
        PhotoCollectionTransferable( PhotoInfo[] sourcePhotos ) {
            photos = sourcePhotos;
        }

        /**
         @return The PhotoInfo array that was transferred
         */
        public Object getTransferData(DataFlavor flavor)
	    throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return photos;
        }

        /**
         Get the data flavours this transfer supports. Currently only PhotoInfoFlavour 
         is supported.
         */
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { photoInfoFlavor };
        }

        /**
         @return True if a specific data flavour is supproted, false otherwise
         */
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return photoInfoFlavor.equals(flavor);
        }
    }
}

   
   

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


import org.photovault.imginfo.PhotoInfo;
import org.photovault.folder.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * FolderNode keeps track of photos currently in the model that belong to a certain folder.
 * It determises the presentation of the folder in folder tree.
 * <P>
 * FolderNode is used as an user object of DefaultMutableTreeNode. 
 * <p>
 * TODO: Note that currently this object is used only as a "dummy" data structure 
 * to get the node representation for DefaultMutableTree, since the representation 
 * needs information from both field model and PhotoFolder. However almost all 
 * processing is done in @see FolderController - it might be more logical to 
 * encapsulate also the relevant logic here.
 */

class FolderNode {

    static Log log = LogFactory.getLog( FolderNode.class );
    Object[] model;
    /**
     * Constructor
     * @param model The model (@see FieldController) that is represented in the folder tree
     * @param folder The PhotoFolder that this FoldeNode represents.
     */
    public FolderNode( Object[] model, PhotoFolder folder ) {
	this.folder = folder;
	this.model = model;
    }

    PhotoFolder folder;

    public PhotoFolder getFolder() {
	return folder;
    }

    boolean allAdded = false;
    /** Adds all photos in the model to this folder
     */
    public void addAllPhotos() {
	allAdded = true;
	allRemoved = false;
    }

    boolean allRemoved = false;
    
    /**
     * Removes all photos that belong to model from this folder
     */
    public void removeAllPhotos() {
	allRemoved = true;
	allAdded = false;
    }

    HashSet photos = new HashSet();
    
    /**
     * Adds a photo to this folder.
     */
    public void addPhoto( PhotoInfo photo ) {
        log.debug( "addPhoto node " + this + " photo " + photo.getUuid() );
	photos.add( photo );
    }

    /**
     * Returns <code>true</code> if the FolderNode contains photos, 
     * <code>false</code> otherwise.
     */
    
    public boolean containsPhotos() {
        log.debug( "containsPhotos node " + this + " allAdded " + allAdded + 
                ", removed: " + allRemoved + ", photos: " + photos.size() );
        return allAdded || ((photos.size() > 0) && !allRemoved);
    }

    /**
     * Returns <code>true</code> if the FolderNode contains all photos in model, 
     * <code>false</code> otherwise.
     */
    boolean containsAllPhotos() {
        return (model!= null && (photos.size() == model.length || allAdded)
				&& !allRemoved );
    }
	    
    /**
     * Returns the string that represents this folder in folder tree. <p>
     *
     * The representation is determined like this:
     * <ul>
     * <li> If the folder contains no photos the folder name is displayed as normal text
     * <li> If the folder contains all photos in the model the folder name is 
     * displayed as bolded black text.
     * <li> If the folder contains some of the photos in model the fodler name is 
     * displayed in gray bolded text.
     * <ul>
     */
    public String toString() {
	StringBuffer strbuf = new StringBuffer();
	strbuf.append( "<html>" );
	boolean hasPhotos = ((photos.size() > 0 || allAdded) && !allRemoved);
	boolean hasAllPhotos = (model!= null && (photos.size() == model.length || allAdded)
				&& !allRemoved );
	if ( hasPhotos ) {
	    strbuf.append( "<b>" );
	    if ( !hasAllPhotos ) {
		strbuf.append( "<font color=gray>" );
	    }
	}

	strbuf.append( folder.getName() );
	
	if ( hasPhotos ) {
	    if ( !hasAllPhotos ) {
		strbuf.append( "</font>" );
	    }
	    strbuf.append( "</b>" );
	}
	return strbuf.toString();
    }
}
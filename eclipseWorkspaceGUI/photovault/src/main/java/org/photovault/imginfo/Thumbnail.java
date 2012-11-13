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

package org.photovault.imginfo;


import java.net.URL;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;


/**
   Thumbnail class represents an image thumbnail. It encapsulates the image data as well as info
   about the PhotoInfo object it represents
*/

public class Thumbnail {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( Thumbnail.class.getName() );


    /**
       Constructor. It is not expected that Thumbnails are created independently but
       only via PhotoInfo.getThumbnail()
    */

    private Thumbnail() {
    }

    /**
       Returns a BufferedImage that contains the thumbnail image data
    */
    public BufferedImage getImage() {
	return image;
    }

    /**
       Returns the photo that the thumbnail presents
    */

    public PhotoInfo getPhotoInfo() {
	return photo;
    }

    BufferedImage image = null;
    PhotoInfo photo = null;
    
    /**
       Creates a thumbnail from a given image instance
       @param photo The photoInfo which the thumbnail presents
       @param thumbnailFile File to be used as a thumbnail
       @return A new Thumbnail object if the image instance represents a thumbnail. Otherwise returns the
       defauld thumbnail.
    */

    protected static Thumbnail createThumbnail( PhotoInfo photo, File thumbnailFile ) {
	if ( thumbnailFile == null ) {
	    return getDefaultThumbnail();
	}
	Thumbnail thumb = new Thumbnail();
	thumb.photo = photo;
	log.debug( "Creating thumbnail for " + photo.getUuid() );
	log.debug( " - " + thumbnailFile.getPath() );
	try {
	    thumb.image = ImageIO.read( thumbnailFile );
	} catch ( IOException e ) {
	    log.warn( "Error reading thumbnail image: " + e.getMessage() );
	    // Apparently database is corrupt, use the default Thumbnail
	    return getDefaultThumbnail();
	}
	return thumb;
    }

    /**
       Returns the thumbnail image that is used if thumbnail for a specific photo cannot
       be loaded for any reason.
    */
    public static Thumbnail getDefaultThumbnail() {
	if ( defaultThumbnail == null ) {
	    defaultThumbnail = new Thumbnail();
            try {
                URL defThumbURL = Thumbnail.class.getClassLoader().getResource( "defthumb.png" );
                defaultThumbnail.image = ImageIO.read( defThumbURL );
            } catch (IOException ex) {
                log.error( "Error loading default thumbnail: " + ex.getMessage() );
                defaultThumbnail.image = new BufferedImage( 100, 75, BufferedImage.TYPE_INT_RGB );
            }
	}
	return defaultThumbnail;
    }

    /**
     Returns the thumbnail image that is used if thumbnail for a specific 
     is broken
    */
    public static Thumbnail getErrorThumbnail() {
	if ( errorThumbnail == null ) {
	    errorThumbnail = new Thumbnail();
            try {
                URL errThumbURL = Thumbnail.class.getClassLoader().getResource( "defthumb.png" );
                errorThumbnail.image = ImageIO.read( errThumbURL );
            } catch (IOException ex) {
                log.error( "Error loading error thumbnail: " + ex.getMessage() );
                errorThumbnail.image = new BufferedImage( 100, 75, BufferedImage.TYPE_INT_RGB );
            }
	}
	return errorThumbnail;
    }

    /**
     Default thumbnail.
     */
    static Thumbnail defaultThumbnail = null;
    
    /**
     Error thumbnail
     */
    static Thumbnail errorThumbnail = null;
}

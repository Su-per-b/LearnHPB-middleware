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


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.font.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.geom.*;
import org.photovault.imginfo.*;
import java.text.*;
import java.util.Date;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.PhotoInfoChangeEvent;
import org.photovault.imginfo.PhotoInfoChangeListener;
import org.photovault.imginfo.Thumbnail;

/**
   ThumbnailView is a very simple component for displaying Thumbnails.
*/

public class ThumbnailView extends JPanel implements PhotoInfoChangeListener {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( ThumbnailView.class.getName() );
    
    public ThumbnailView() {
	super();
    }

    public void paint( Graphics g ) {
	super.paint( g );
        log.debug( "start paintinh thumbnail" );
        long startTime = System.currentTimeMillis();
        long startDrawingTime = 0;
        long attrDrawingStartTime = 0;
	Graphics2D g2 = (Graphics2D) g;
	// Current position in which attributes can be drawn
	Dimension compSize = getSize();
	int ypos = ((int)compSize.getHeight())/2;

	if ( thumbnail != null ) {
	    // Find the position for the thumbnail
	    BufferedImage img = thumbnail.getImage();
	    int x = ((int)(compSize.getWidth() - img.getWidth()))/(int)2;
	    int y = ((int)(compSize.getHeight() - img.getHeight()))/(int)2;
            
            startDrawingTime = System.currentTimeMillis();
	    g2.drawImage( img, new AffineTransform( 1f, 0f, 0f, 1f, x, y ), null );
	    // Increase ypos so that attributes are drawn under the image
	    ypos += ((int)img.getHeight())/2 + 4;
	}

	// Draw the attributes
        attrDrawingStartTime = System.currentTimeMillis();
	if ( photo != null ) {
	    Font attrFont = new Font( "Arial", Font.PLAIN, 10 );
	    FontRenderContext frc = g2.getFontRenderContext();
	    if ( showDate && photo.getShootTime() != null ) {
		long accuracy = ((long) photo.getTimeAccuracy() ) * 24 * 3600 * 1000;
		log.warn( "Accuracy = " + accuracy );
		String dateStr = "";
		if ( accuracy > 0 ) {
		    // Show the limits of the accuracy range
		    DateFormat df = new SimpleDateFormat( "dd.MM.yyyy" );
		    Date lower = new Date( photo.getShootTime().getTime() - accuracy );
		    Date upper = new Date( photo.getShootTime().getTime() + accuracy );
		    String lowerStr = df.format( lower );
		    String upperStr = df.format( upper );
		    dateStr = lower + " - " + upper;
		} else {
		    DateFormat df = new SimpleDateFormat( "dd.MM.yyyy k:mm" );
		    dateStr = df.format( photo.getShootTime() );
		}

		TextLayout txt = new TextLayout( dateStr, attrFont, frc );
		// Calculate the position for the text
		Rectangle2D bounds = txt.getBounds();
		int xpos = ((int)(compSize.getWidth()-bounds.getWidth()))/2 - (int)bounds.getMinX();
		txt.draw( g2, xpos, (int)(ypos + bounds.getHeight()) );
		ypos += bounds.getHeight() + 4;
	    }
	    String shootPlace = photo.getShootingPlace();
	    if ( showPlace && shootPlace != null && shootPlace.length() > 0  ) {
		TextLayout txt = new TextLayout( photo.getShootingPlace(), attrFont, frc );
		// Calculate the position for the text
		Rectangle2D bounds = txt.getBounds();
		int xpos = ((int)(compSize.getWidth()-bounds.getWidth()))/2 - (int)bounds.getMinX();
		txt.draw( g2, xpos, (int)(ypos + bounds.getHeight()) );
		ypos += bounds.getHeight() + 4;
	    }
	}
        long endTime = System.currentTimeMillis();
        log.debug( "Drawn thumbnail, thumb fetch " + (startDrawingTime - startTime) + 
                ", thumb draw " + (attrDrawingStartTime - startDrawingTime) 
                + ", attribute draw " + (endTime - attrDrawingStartTime) 
                + ", total " + (endTime - startTime) );
    }


    boolean showDate = true;
    boolean showPlace = true;
	
    public void setShowShootingTime( boolean b ) {
	showDate = b;
	repaint( 0, 0, 0, getWidth(), getHeight() );
    }

    public boolean getShowShootingTime() {
	return showDate;
    }

    public void setShowShootingPlace( boolean b ) {
	showPlace = b;
	repaint( 0, 0, 0, getWidth(), getHeight() );
    }

    public boolean getShowShootingPlace() {
	return showPlace;
    }
    
    public Dimension getPreferredSize() {
	return new Dimension( 150, 150 );
    }

    /**
       Implementation of @see PhotoInfoChangeListener. Checks if the Thumbnail has changed (e.g. the preferred
       rotation has changed) and updates thumbnail if appropriate
    */
    public void photoInfoChanged( PhotoInfoChangeEvent e ) {
	Thumbnail newThumb = photo.getThumbnail();
	if ( newThumb != thumbnail ) {
	    thumbnail = newThumb;
	    repaint();
	}
    }
    
    /**
       Set the photo that is displayed as a thumbnail
    */
    public void setPhoto( PhotoInfo photo ) {
	if ( this.photo != null ) {
	    this.photo.removeChangeListener( this );
	}
	this.photo = photo;
	if ( photo != null ) {
	    photo.addChangeListener( this );
	    thumbnail = photo.getThumbnail();
	} else {
	    thumbnail = null;
	}
	repaint();
    }

    /**
       Returns the currently displayed photo.
    */
    public PhotoInfo getPhoto() {
	return photo;
    }

    PhotoInfo photo = null;
    Thumbnail thumbnail = null;

}
		     
	    

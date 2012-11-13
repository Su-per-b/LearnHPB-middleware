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

import java.lang.*;
import javax.swing.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.photovault.imginfo.*;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.imginfo.Thumbnail;
import org.photovault.persistence.DAOFactory;
import org.photovault.persistence.HibernateDAOFactory;
import org.photovault.persistence.HibernateUtil;

/**
 ThumbCreatorThread is used for deferred creation of thumbnails by PhotoCollectionThumbView.
 It starts a new thread that waits for thumbnail creation requests and notifies 
 the PhotoCollectionThumbVew object when the requested thumbnail is ready.
 <p>
 This class implements a simple producer-consumer model for passing photos between the 2 
 threads.
 
 */

class ThumbCreatorThread extends Thread {
    static org.apache.log4j.Logger log 
	= org.apache.log4j.Logger.getLogger( ThumbCreatorThread.class.getName() );

    /**
     Constructor
     @param view The view that will be notified after the thumbnail has been drawn
     */
    public ThumbCreatorThread( PhotoCollectionThumbView view ) {
        super( "ThumbCreator" );
	this.view = view;
    }

    PhotoCollectionThumbView view;
    PhotoInfo photo;
    
    /**
     Submits a request for creation of a new thumbnail. This method returns immediately
     after notifying the thumbnail creation thread. However, it blocks on a monitor
     until the thread is free to take another job. So in performance critical situations
     it is safest to call isBusy() before claling this.
     @param photo the photo for which the thumbnail will be created.
     */
    synchronized public void createThumbnail( PhotoInfo photo ) {
	log.debug( "createThumbnail for " + photo.getUuid() );
	this.photo = photo;
	notify();
    }

    /**
     @return true if the thread is currently working on a new thumbnail, false otherwise.
     */
    public boolean isBusy() {
	return ( photo != null );
    }

    /**
     Actual code for the thread.
     */
    public void run() {
	synchronized ( this ) {
	    while ( true ) {
		try {
		    log.debug( "Waiting..." );
		    wait();
		    log.debug( "Waited..." );
		    if ( photo != null ) {
			log.debug( "Creating thumbnail for " + photo.getUuid() );
                        Session session = HibernateUtil.getSessionFactory().openSession();
                        Transaction tx = session.beginTransaction();
                        HibernateDAOFactory daoFactory = (HibernateDAOFactory) DAOFactory.instance( HibernateDAOFactory.class );
                        daoFactory.setSession( session );
                        PhotoInfoDAO photoDAO = daoFactory.getPhotoInfoDAO();
                        photo = photoDAO.findByUUID( photo.getUuid() );
			Thumbnail thumb = null;
                        
			while ( thumb == null ) {
			    try {
				thumb = photo.getThumbnail();
			    } catch ( Throwable e ) {
				// Most likely out of memory. Sleep for a while (to allow for other
				// tasks to release memory) and try again
				log.warn( "Error while creating thumbnail: " + e.getMessage() );
				try {
				    sleep( 5 * 1000 );
				} catch ( InterruptedException e1 ) {
				    // Interrupted while sleeping, no action required
				}
			    }
			}
			log.debug( "Done!" );
                        session.flush();
                        tx.commit();
                        session.close();

			// Inform the view that the thumbnail is now created
			final PhotoInfo lastPhoto = photo;
			photo = null;
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
				    log.debug( "drawing new thumbnail for " + lastPhoto.getUuid() );
				    view.thumbnailCreated( lastPhoto );
                                }
                        });
		    }
		} catch ( InterruptedException e ) {
		    // Interrupt while waiting for mutex, just continute...
		} 
	    }
	}
    }
}
					    
					    
	
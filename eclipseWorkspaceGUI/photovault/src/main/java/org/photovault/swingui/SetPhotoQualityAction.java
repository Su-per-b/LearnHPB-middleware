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


import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.ImageIcon;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.command.CommandException;
import org.photovault.command.CommandHandler;
import org.photovault.imginfo.ChangePhotoInfoCommand;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.swingui.framework.DataAccessAction;
import org.photovault.swingui.framework.DefaultEvent;
import org.photovault.swingui.framework.DefaultEventListener;
import org.hibernate.Session;
/**
   This action sets the "quality" attribut of all selected photos to a specific value
*/
class SetPhotoQualityAction extends DataAccessAction {

    static private Log log = LogFactory.getLog( SetPhotoQualityAction.class );

    /**
       Constructor.
       @param view The view this action object is associated with.
       @param quality The quality that is set to selected photos.
       @see PhotoInfo for semantics of different quality levels
       @param text Text displayed in menus etc. with this action
       @param icon Icon displayed in menus etc.
       @param desc
       @param mnemonic Keyboard shortcut for this action
    */
    public SetPhotoQualityAction( PhotoViewController ctrl,
				  int quality,
				  String text, ImageIcon icon,
				  String desc, Integer mnemonic) {
	super( text, icon );
	this.ctrl = ctrl;
	putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        ctrl.registerEventListener( SelectionChangeEvent.class,
                new DefaultEventListener<SelectionChangeEvent>() {

                    public void handleEvent(
                            DefaultEvent<SelectionChangeEvent> event ) {
                        selectionChanged();
                    }
                } );
	this.quality = quality;
    }

    /**
       Listener for changes in selection. If no photos are selected disable the action.
    */
    public void selectionChanged() {
	setEnabled( !ctrl.getSelection().isEmpty() );
    }

    /**
       This is called when the action must be executed.
    */
    public void actionPerformed( ActionEvent ev, Session session ) {
        Collection selectedPhotos = ctrl.getSelection();
        CommandHandler cmdHandler = ctrl.getCommandHandler();
        Iterator iter = selectedPhotos.iterator();
        while ( iter.hasNext() ) {
            PhotoInfo photo = (PhotoInfo) iter.next();

            if ( photo != null ) {
                ChangePhotoInfoCommand cmd = new ChangePhotoInfoCommand( photo.
                    getUuid() );
                cmd.setQuality( quality );
                try {
                    cmdHandler.executeCommand( cmd );
//                    PhotoInfo[] changedPhotos = cmd.getChangedPhotos().toArray( new PhotoInfo[1] );
//                    photo = ctrl.getDAOFactory().getPhotoInfoDAO().makePersistent( changedPhotos[0] );
                } catch (CommandException ex) {
                    log.error( ex );
                }

            }
        }
    }

    PhotoCollectionThumbView view;
    PhotoViewController ctrl;
    int quality;
}
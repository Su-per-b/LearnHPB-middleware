/*
  Copyright (c) 2008 Harri Kaimio
  
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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.replication.Change;
import org.photovault.replication.ObjectHistory;

/**
 Show the history a selected photo(s). Currently this action just prints
 changes applied to this photo recursively to logger.
 
 @author Harri Kaimio
 @since 0.6.0
 */
public class ShowPhotoHistoryAction extends AbstractAction
        implements SelectionChangeListener {
    
    static Log log = LogFactory.getLog( ShowPhotoHistoryAction.class.getName() );
    private PhotoCollectionThumbView view;

    /**
     Constructor.
     
     @param view
     @param text
     @param icon
     @param desc
     @param mnemonic
     @param accelerator
     */
    public ShowPhotoHistoryAction( 
            PhotoCollectionThumbView view,
            String text, ImageIcon icon,
            String desc, int mnemonic, KeyStroke accelerator ) {
	super( text, icon );
        this.view = view;
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, new Integer( mnemonic ) );
	putValue( ACCELERATOR_KEY, accelerator );
        view.addSelectionChangeListener( this );
        setEnabled( view.getSelectedCount() > 0 );
    }
    
    /**
     Executes the action and prints history to log.
     @param ev the event that triggered this action
     */
    public void actionPerformed( ActionEvent ev ) {
        Collection<PhotoInfo> selected = view.getSelection();
        for ( PhotoInfo p : selected ) {
            ObjectHistory<PhotoInfo> history = p.getHistory();
            Set<UUID> processedChanges = new HashSet<UUID>();
            StringBuffer buf = new StringBuffer();
            buf.append(  "History of photo " );
            buf.append( p.getUuid() );
            buf.append( "\n" );
            for ( Change<PhotoInfo> head : history.getHeads() ) {
                printChange( buf, head, processedChanges );
            }
            log.debug( buf.toString() );
        }
    }

    /**
     Print a change and its predecessord if they are not yet printed
     @param buf Buffer where the changes are printed
     @param ch The change
     @param processed Set of UUIDs of changes that are already printed. The 
     changes printed by this method are added to this set.
     */
    private void printChange( StringBuffer buf, Change<PhotoInfo> ch, 
            Set<UUID> processed ) {
        if ( processed.contains( ch.getUuid() ) ) {
            return;
        }
        buf.append(  ch );
        processed.add(  ch.getUuid() );
        for ( Change<PhotoInfo> prev : ch.getParentChanges() ) {
            printChange( buf, prev, processed );
        }
    } 
    
    public void selectionChanged( SelectionChangeEvent e ) {
        setEnabled( view.getSelectedCount() > 0 );
    }


}

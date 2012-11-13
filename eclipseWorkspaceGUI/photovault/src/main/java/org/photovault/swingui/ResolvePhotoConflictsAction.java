/*
  Copyright (c) 2010 Harri Kaimio

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
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.replication.Change;
import org.photovault.swingui.conflict.ResolveConflictDlg;

/**
 * UI actionf or displaying {@link ResolveConflictDlg} for currently selected
 * photo.
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class ResolvePhotoConflictsAction extends AbstractAction 
        implements SelectionChangeListener {

    public ResolvePhotoConflictsAction(
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

    private PhotoCollectionThumbView view;
    private PhotoInfo selectedPhoto = null;

    public void actionPerformed( ActionEvent e ) {
        if ( selectedPhoto == null ) {
            return;
        }
        Set<Change<PhotoInfo>> heads = selectedPhoto.getHistory().getHeads();
        Iterator<Change<PhotoInfo>> iter = heads.iterator();
        Change ch1 = iter.next();
        Change ch2 = iter.next();

        ResolveConflictDlg dlg = 
                new ResolveConflictDlg( null, view.ctrl.getCommandHandler(), ch1, ch2);
        dlg.setVisible( true );
    }



    public void selectionChanged( SelectionChangeEvent e ) {
        Collection selected = view.getSelection();
        if ( selected.size() == 1 ) {
            PhotoInfo p = (PhotoInfo) selected.iterator().next();
            if ( p.getHistory().getHeads().size() > 1 ) {
                selectedPhoto = p;
                setEnabled( true );
                return;
            }
        }
        selectedPhoto = null;
        setEnabled( false );
    }

}

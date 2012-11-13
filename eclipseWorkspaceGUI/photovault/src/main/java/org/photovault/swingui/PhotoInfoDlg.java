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
import org.hibernate.classic.Session;
import org.photovault.imginfo.*;
import java.io.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.swingui.framework.AbstractController;
import org.photovault.swingui.framework.DataAccessAction;
import org.photovault.swingui.selection.PhotoSelectionController;


/**
   PhotoInfoDlg is a simple wrapper that wraps a PhotoInfoeditor to a diaplog frame.
*/

public class PhotoInfoDlg extends PhotoSelectionController {
    static Log log = LogFactory.getLog( PhotoInfoDlg.class.getName() );

    static final String DIALOG_TITLE = "Edit photo info";
    /**
       Constructor. Creates a PhotoInfoDlg with the specified window as a parent.
       @param owner Owner of the dialog
       @param modal If true, a modal dialog is created
    */
    public PhotoInfoDlg( AbstractController parent, Session session, boolean modal, PhotoInfo photo ) {
        super( parent, session );
        createUI();
	setPhoto( photo );
    }

    public PhotoInfoDlg( PhotoViewController parent, Session session, boolean modal, PhotoInfo[] photos ) {
	super( parent, session );
	createUI();
        setPhotos( photos );
    }

    PhotoViewController masterCtrl;
    JDialog dialogWindow = null;
    
    /**
       Creates the UI components needed for this dialog.
    */
    protected void createUI() {
        Container parentView = getParentController().getView();
        Window wnd = SwingUtilities.getWindowAncestor( parentView );
        dialogWindow = new JDialog( (Frame) wnd );
        editor = new PhotoInfoEditor( this );
        addView( editor );
        dialogWindow.getContentPane().add( editor, BorderLayout.NORTH );
	final PhotoInfoDlg staticThis = this;
        
        registerAction( "save_close", new DataAccessAction( "OK" ) {
            public void actionPerformed(ActionEvent actionEvent, 
                    org.hibernate.Session currentSession) {
                save();
                dialogWindow.setVisible( false );
            }
            
        });
        registerAction( "discard_close", new DataAccessAction( "Cancel" ) {
            public void actionPerformed(ActionEvent actionEvent, 
                    org.hibernate.Session currentSession) {
                discard();
                dialogWindow.setVisible( false );
            }
            
        });
        
	JButton okBtn = new JButton( this.getActionAdapter( "save_close" ) );
//	okBtn.addActionListener( new ActionListener() {
//            public void actionPerformed( ActionEvent e ) {
//                try {
//                    ChangePhotoInfoCommand cmd = ctrl.getChangeCommand();
//                    masterCtrl.getCommandHandler().executeCommand( cmd );
//                    photoChanged = true;
//                    setVisible( false );
//                } catch ( Exception ex ) {
//                    JOptionPane.showMessageDialog(
//                            staticThis,
//                            "Error while saving changes: \n" + ex.getMessage(),
//                            "Error saving changes",
//                            JOptionPane.ERROR_MESSAGE,
//                            null );
//                    log.warn( "problem while saving changes: " + ex.getMessage() );
//                }
//            }
//        } );

        JButton applyBtn = new JButton( getActionAdapter( "save" ) );
//        applyBtn.addActionListener( new ActionListener() {
//            public void actionPerformed( ActionEvent e ) {
//                try {
//                    ChangePhotoInfoCommand cmd = ctrl.getChangeCommand();
//                    masterCtrl.getCommandHandler().executeCommand( cmd );
//                    photoChanged = true;
//                } catch ( Exception ex ) {
//                    JOptionPane.showMessageDialog(
//                            staticThis,
//                            "Error while saving changes: \n" + ex.getMessage(),
//                            "Error saving changes",
//                            JOptionPane.ERROR_MESSAGE,
//                            null );
//                    log.warn( "problem while saving changes: " + ex.getMessage() );
//                }
//            }
//        } );
        
        JButton discardBtn = new JButton( getActionAdapter( "discard" ) );
//        discardBtn.addActionListener( new ActionListener() {
//            public void actionPerformed( ActionEvent e ) {
//                ctrl.discard();
//            }
//        } );
        
        JButton closeBtn = new JButton( getActionAdapter( "discard_close" ) );
//        closeBtn.addActionListener( new ActionListener() {
//            public void actionPerformed( ActionEvent e ) {
//                ctrl.discard();
//                setVisible( false );
//            }
//        } );
	    
	JPanel buttonPane = new JPanel();
	buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
	buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	buttonPane.add(Box.createHorizontalGlue());
	buttonPane.add(okBtn);
	buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	buttonPane.add(applyBtn);
	buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	buttonPane.add(discardBtn);
	buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
	buttonPane.add(closeBtn);
	dialogWindow.getContentPane().add( buttonPane, BorderLayout.SOUTH );

	dialogWindow.getRootPane().setDefaultButton( applyBtn );

	dialogWindow.pack();
    }

    /**
       Shows the dialog.
       @return Ture if the dialog modified the photo data, false otherwise.
    */
	
    public boolean showDialog() {
	photoChanged = false;
	dialogWindow.setVisible( true );
	return photoChanged;
    }

    PhotoInfoEditor editor = null;
    PhotoSelectionController ctrl = null;
    /**
       Indicates whether the photo inforamtion was changed (by pressing OK or Apply)
    */
    boolean photoChanged = false;
    public boolean isPhotoChanged() {
	return photoChanged;
    }

    boolean isVisible() {
        return dialogWindow.isVisible();
    }

}


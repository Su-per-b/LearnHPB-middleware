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

/**
   ProgressDlg is a simple dialog that displays a progress bar and information message
*/

public class ProgressDlg extends JDialog {
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( PhotoInfoDlg.class.getName() );

    static final String DIALOG_TITLE = "Progress";

    /**
       Constructor
    */
    ProgressDlg( Frame owner, boolean modal ) {
	super( owner, DIALOG_TITLE, modal );
	createUI();

    }

    protected void createUI() {
	JPanel pane = new JPanel();
	getContentPane().add( pane,  BorderLayout.NORTH );
	pane.setLayout(new BoxLayout( pane, BoxLayout.Y_AXIS ));
	pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	statusLabel = new JLabel();
	// Set text to non-null value so that the component is laid out correctly
	statusLabel.setText( " " );
	progressBar = new JProgressBar();
	progressBar.setMaximum( 100 );
	progressBar.setValue( 50 ); // for debugging purposes
	pane.add( statusLabel );
	pane.add(Box.createRigidArea(new Dimension(300, 10)));
	pane.add( progressBar );

	pack();
    }

    /**
       Sets the progress bar to specified percentage
       @param percent percentage of completeness (0-100)
    */
    
    void setProgressPercent( int percent ) {
	final int percent2 = percent;
	SwingUtilities.invokeLater( 
				   new Runnable() {
				       public void run() {
					   progressBar.setValue( percent2 );
				       }
				   }
				   );

    }

    /**
       Sets the status string displayed
       @param status The status string
    */
    void setStatus( final String status ) {
	SwingUtilities.invokeLater( 
				   new Runnable() {
				       public void run() {
					   statusLabel.setText( status );
				       }
				   }
				   );
    }

    /**
       Informs the dialog that the task has been completed. Dialog is dismissed
    */
    void completed() {
	SwingUtilities.invokeLater( 
				   new Runnable() {
				       public void run() {
					   setVisible( false );
				       }
				   }
				   );
    }
	
       
    JProgressBar progressBar;
    JLabel statusLabel;
}
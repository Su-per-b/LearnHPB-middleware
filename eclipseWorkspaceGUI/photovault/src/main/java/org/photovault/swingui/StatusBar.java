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

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


/**
 Simple status bar for Photovault windows
 */
public class StatusBar extends JPanel implements StatusChangeListener {
    
    /** Creates a new instance of StatusBar */
    public StatusBar() {
        createUI();
    }
    
    /**
     * Shows a message in status bar
     *@param message the message to show
     */
    public void setMessage( String message ) {
        statusMessageLabel.setText( message + " " );
    }

    JLabel statusMessageLabel = null;
    
    private void createUI() {
        this.setBorder( BorderFactory.createLoweredBevelBorder() );
        statusMessageLabel = new JLabel();
        statusMessageLabel.setText( " " );
        statusMessageLabel.setHorizontalAlignment( SwingConstants.LEFT );
        
        BorderLayout layout = new BorderLayout();
        setLayout( layout );
        add( statusMessageLabel, BorderLayout.SOUTH );
    }

    /**
     Called when status of a monitored object changes
     */
    public void statusChanged(StatusChangeEvent event) {
        final String message = event.getMessage();
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                setMessage( message );        
            }
        });
    }
    
}

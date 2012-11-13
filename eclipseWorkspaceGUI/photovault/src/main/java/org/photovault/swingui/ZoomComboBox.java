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
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.swing.JComboBox;

/**
 Combo box for selecting zoom levels in toolbar. This class populates the combo box 
 qith preset zoom levels, handles parsing of user entered values and disables or
 enables the control based on viewer visibility & content.
 */
public class ZoomComboBox extends JComboBox {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( ZoomComboBox.class.getName() );

    /**
     Preset zoom levels
     */
    static String[] defaultZooms = {
        "12%",
        "25%",
        "50%",
        "100%",
        "Fit"
    };

    
    /**
     Creates a new instance of ZoomComboBox
     @param view The viewer that is connected with this control.
     */
    public ZoomComboBox( JAIPhotoViewer view ) {
        super( defaultZooms );
        viewer = view;
        
        setEditable( true );
        setSelectedItem( "Fit" );
        setMaximumSize( getPreferredSize() );
        
        addActionListener(  new ActionListener() {
            public void actionPerformed( ActionEvent ev ) {
                selectionChanged();
            }
        });
        
        viewer.addComponentListener( new ComponentListener() {
            public void componentResized(ComponentEvent e) {
            }
            
            public void componentMoved(ComponentEvent e) {
            }
            
            public void componentShown(ComponentEvent e) {
                checkIfEnabled();
            }
            
            public void componentHidden(ComponentEvent e) {
                checkIfEnabled();
            }
            
        });
        
        viewer.addViewChangeListener( new PhotoViewChangeListener() {
            public void photoViewChanged(PhotoViewChangeEvent e) {
                checkIfEnabled();
            }            
        });
        checkIfEnabled();
    }
    
    /**
     JAIPhotoViewer conected with this control
     */
    JAIPhotoViewer viewer;
    
    /**
     Called when the selected item in this combo box has been changed. Parses the
     selection and sets zoom level in connected view if needed.
     */
    private void selectionChanged() {
        String selected = (String)getSelectedItem();
        log.debug( "Selected: " + selected );
        boolean isFit = false;
        
        // Parse the pattern
        DecimalFormat percentFormat = new DecimalFormat( "#####.#%" );
        if ( selected == "Fit" ) {
            isFit = true;
            log.debug( "Fitting to window" );
            viewer.fit();
            String strNewScale = "Fit";
            setSelectedItem( strNewScale );
        } else {
            // Parse the number. First remove all white space to ease the parsing
            StringBuffer b = new StringBuffer( selected );
            int spaceIndex =  b.indexOf( " " );
            while ( spaceIndex >= 0  ) {
                b.deleteCharAt( spaceIndex );
                spaceIndex =  b.indexOf( " " );
            }
            selected = b.toString();
            boolean success = false;
            float newScale = 0;
            try {
                newScale = Float.parseFloat( selected );
                newScale /= 100.0;
                success = true;
            } catch (NumberFormatException e ) {
                // Not a float
            }
            if ( !success ) {
                try {
                    newScale = percentFormat.parse( selected ).floatValue();
                    success = true;
                } catch ( ParseException e ) {
                }
            }
            if ( success ) {
                log.debug( "New scale: " + newScale );
                viewer.setScale( newScale );
                String strNewScale = percentFormat.format( newScale );
                setSelectedItem( strNewScale );
            }
        }
        
    }
    
    /**
      Enables or disables this control based on whether connected view is visible 
     and is showing a photo.
     */
    private void checkIfEnabled() {
        setEnabled( viewer.getPhoto() != null && viewer.isShowing() );
    }
}

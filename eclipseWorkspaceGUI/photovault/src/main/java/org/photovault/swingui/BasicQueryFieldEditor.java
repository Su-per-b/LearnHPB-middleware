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

import org.photovault.imginfo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

/**
   UI for setting up the basic query criterias for shooting place, description and photographer
*/

public class BasicQueryFieldEditor extends JPanel {

    /**
       Constructor
    */
    public BasicQueryFieldEditor() {
	createUI();
    }

    protected void createUI() {
	JLabel descLabel = new JLabel( "Search for photos whose description contains" );
	JLabel shootingPlaceLabel = new JLabel( "photographed in" );
	JLabel photographerLabel = new JLabel( "photographed by" );

	descText = new JTextField();
	//	descText.setColumns( 25 );
	shootingPlaceText = new JTextField();
	//	shootingPlaceText.setColumns( 15 );
	photographerText = new JTextField();
	//	photographerText.setColumns( 15 );

	GridBagLayout layout = new GridBagLayout();
	GridBagConstraints labelConstraints = new GridBagConstraints();
	labelConstraints.anchor = GridBagConstraints.WEST;
	labelConstraints.gridwidth = GridBagConstraints.REMAINDER;     //end row
	labelConstraints.insets = new Insets( 8, 2, 2, 2 );
	labelConstraints.weightx = 1.0;
	
	GridBagConstraints fieldConstraints = new GridBagConstraints();
	fieldConstraints.anchor = GridBagConstraints.WEST;
	fieldConstraints.gridwidth = GridBagConstraints.REMAINDER;     //end row
	fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
	fieldConstraints.insets = new Insets( 2, 2, 2, 2 );
	fieldConstraints.weightx = 1.0;
	
	setLayout( layout );
	layout.setConstraints( descLabel, labelConstraints );
	add( descLabel );
	layout.setConstraints( descText, fieldConstraints );
	add( descText );
// 	add( Box.createRigidArea(new Dimension(0, 10)), BorderLayout.PAGE_END );
	layout.setConstraints( shootingPlaceLabel, labelConstraints );
	add( shootingPlaceLabel );
	layout.setConstraints( shootingPlaceText, fieldConstraints );
	add( shootingPlaceText );
// 	add( Box.createRigidArea(new Dimension(0, 10)), BorderLayout.PAGE_END );
	layout.setConstraints( photographerLabel, labelConstraints );
	add( photographerLabel );
	layout.setConstraints( photographerText, fieldConstraints );
	add( photographerText );
	//	add( Box.createVerticalGlue(), BorderLayout.PAGE_END );
    }


    public String getDescription() {
	return descText.getText();
    }
    
    public String getShootingPlace() {
	return shootingPlaceText.getText();
    }
    
    public String getPhotographer() {
	return photographerText.getText();
    }
    
    JTextField descText = null;
    JTextField shootingPlaceText = null;
    JTextField photographerText = null;
}

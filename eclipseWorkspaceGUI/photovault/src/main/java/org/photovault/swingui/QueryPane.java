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
import org.photovault.imginfo.*;
import java.util.*;
import org.photovault.imginfo.PhotoCollection;
import org.photovault.imginfo.PhotoQuery;
import org.photovault.persistence.HibernateUtil;

/**
   QueryPane implements the container inlcudes the UI componets used to edit the queiry parameters
*/

public class QueryPane extends JPanel implements ActionListener {
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( QueryPane.class.getName() );

    public QueryPane() {
	super();
	createUI();
	query = new PhotoQuery();
    }

    private void createUI() {
// 	setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
	GridBagLayout layout = new GridBagLayout();
	setLayout( layout );
	GridBagConstraints panelConstraints = new GridBagConstraints();
 	panelConstraints.anchor = GridBagConstraints.WEST;
 	panelConstraints.gridwidth = GridBagConstraints.REMAINDER;     //end row
  	panelConstraints.fill = GridBagConstraints.HORIZONTAL;
	panelConstraints.weightx = 1;
	
	basicFields = new BasicQueryFieldEditor();
	layout.setConstraints( basicFields, panelConstraints );
	add( basicFields );
	// Add search button to the bottom
	JButton searchButton = new JButton( "Search" );
	searchButton.setActionCommand( SEARCH_CMD );
	searchButton.addActionListener( this );
	GridBagConstraints searchBtnConstraints = new GridBagConstraints();
 	searchBtnConstraints.anchor = GridBagConstraints.EAST;
 	searchBtnConstraints.gridwidth = GridBagConstraints.REMAINDER;     //end row
	layout.setConstraints( searchButton, searchBtnConstraints );
	add( searchButton );
	dateCB = new JCheckBox( "Shooting date", false );
	dateCB.setActionCommand( DATE_CB_CHANGED );
	dateCB.addActionListener( this );
	layout.setConstraints( dateCB, panelConstraints );
	add( dateCB );
	shootingDateRange = new DateRangeQueryEditor();
 	shootingDateRange.setVisible( false );
	layout.setConstraints( shootingDateRange, panelConstraints );
	add( shootingDateRange );

	JPanel filler = new JPanel();
	GridBagConstraints fillerConstraints = new GridBagConstraints();
 	fillerConstraints.weighty = 1;
	layout.setConstraints( filler, fillerConstraints );
	add( filler );
	
// 	JPanel buttonPane = new JPanel();
// 	buttonPane.setLayout( new BoxLayout( buttonPane, BoxLayout.X_AXIS ) );
// 	buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
// 	buttonPane.add(Box.createHorizontalGlue());
// 	buttonPane.add( searchButton );
// 	layout.setConstraints( buttonPane, panelConstraints );
// 	add( buttonPane );

// 	// Add glue so that the empty space will go to the bottom
// 	add( Box.createVerticalGlue() );
    }

    public void actionPerformed( ActionEvent e ) {
	if ( e.getActionCommand() == SEARCH_CMD ) {
	    log.debug( "Action performed" );
	    updateQuery();
	    fireActionEvent( SEARCH_CMD );
	} else if ( e.getActionCommand() == DATE_CB_CHANGED ) {
	    shootingDateRange.setVisible( dateCB.isSelected() );
	}
    }

    public void addActionListener( ActionListener l ) {
	actionListeners.add( l );
    }

    public void remoceActionListener( ActionListener l ) {
	actionListeners.remove( l );
    }

    Vector actionListeners = new Vector();

    protected void fireActionEvent( String action ) {
	Iterator iter = actionListeners.iterator();
	while ( iter.hasNext() ) {
	    ActionListener l = (ActionListener) iter.next();
	    l.actionPerformed( new ActionEvent( this, ActionEvent.ACTION_PERFORMED, action ) );
	}
    }
	
    

    static final String SEARCH_CMD = "search";
    static final String DATE_CB_CHANGED = "date visibility changed";

    JCheckBox dateCB = null;
    
    public PhotoCollection getResultCollection() {
	return query;
    }

    
    protected void updateQuery() {
	query = new PhotoQuery();
	String photographer = basicFields.getPhotographer();
	if( photographer.length() > 0 ) {
	    query.setLikeCriteria( PhotoQuery.FIELD_PHOTOGRAPHER,
				   photographer );
	}
	String desc = basicFields.getDescription();
	if( desc.length() > 0 ) {
	    query.setLikeCriteria( PhotoQuery.FIELD_DESCRIPTION,
				   "%" + desc + "%" );
	}
	String shootingPlace = basicFields.getShootingPlace();
	if( shootingPlace.length() > 0 ) {
	    query.setLikeCriteria( PhotoQuery.FIELD_SHOOTING_PLACE,
				   shootingPlace );
	}
	if ( dateCB.isSelected() ) {
// 	    query.setFieldCriteriaRange( PhotoQuery.FIELD_SHOOTING_TIME,
// 					 shootingDateRange.getStartDate(), shootingDateRange.getEndDate() );
	    query.setFuzzyDateCriteria( PhotoQuery.FIELD_SHOOTING_TIME, PhotoQuery.FIELD_SHOOTING_TIME_ACCURACY,
					shootingDateRange.getDateRange(),
					shootingDateRange.getStrictness() );

	}
    }

    DateRangeQueryEditor shootingDateRange = null;
    BasicQueryFieldEditor basicFields = null;
    PhotoQuery query = null;
    
    public static void main( String [] args ) {
	JFrame frame = new JFrame( "PhotoInfoEditorTest" );
	QueryPane qp = new QueryPane();
	frame.getContentPane().add( qp, BorderLayout.CENTER );
	frame.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    System.exit(0);
		}
	    } );
	frame.pack();
	frame.setVisible( true );
    }

}

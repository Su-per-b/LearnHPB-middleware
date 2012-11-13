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
import org.photovault.imginfo.FuzzyDate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import org.photovault.imginfo.QueryFuzzyTimeCriteria;

public class DateRangeQueryEditor extends JPanel implements ActionListener {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( DateRangeQueryEditor.class.getName() );
    
    /**
       Constructor
    */
    public DateRangeQueryEditor() {
	createUI();
    }

    boolean useFuzzyDateField = true;
    int strictness = QueryFuzzyTimeCriteria.INCLUDE_PROBABLE;
    
    protected void createUI() {
	DateFormat df = DateFormat.getDateInstance();
	startDate = new JFormattedTextField( df );
	startDate.setColumns( 10 );
	startDate.setValue( new Date( ));
	endDate = new JFormattedTextField( df );
	endDate.setColumns( 10 );
	endDate.setValue( new Date( ));
	dateRange = new JTextField( 25 );
	

	
	JLabel startLabel = new JLabel( "Start date" );
	JLabel endLabel = new JLabel( "End date" );
        
        DateFormat df1 = new SimpleDateFormat( "MMMM yyyy" );
        Calendar cal = Calendar.getInstance();
        cal.set( 2006, 0, 15 );
        Date d = cal.getTime();
        String date2Str = df1.format( d );
	JLabel dateRangeLabel = new JLabel( "Enter date range (e.g. \"2005 - " 
                + date2Str +"\")");
	JLabel guide1Label = new JLabel( "or enter start & end dates" );
// 	JLabel shootingPlaceLabel = new JLabel( "Shooting place" );
// 	JLabel descLabel = new JLabel( "Description" );

	JLabel guide2Label = new JLabel( "If  date is uncertain include if it is" );
	JLabel guide3Label = new JLabel( "in the range" );
	JRadioButton certainInRangeBtn = new JRadioButton( CERTAINLY_IN_RANGE );
	certainInRangeBtn.addActionListener( this );
	certainInRangeBtn.setEnabled( false );
	JRadioButton probablyInRangeBtn = new JRadioButton( PROBABLY_IN_RANGE );
	probablyInRangeBtn.addActionListener( this );
	probablyInRangeBtn.setSelected( true );
	JRadioButton maybeInRangeBtn = new JRadioButton( MAYBE_IN_RANGE );
	maybeInRangeBtn.addActionListener( this );
	maybeInRangeBtn.setEnabled( false );
	ButtonGroup bg = new ButtonGroup();
	bg.add( certainInRangeBtn );
	bg.add( probablyInRangeBtn );
	bg.add( maybeInRangeBtn );
	
	
	JLabel[] labels = {startLabel, endLabel };
	JTextField[] fields = {startDate, endDate };
	GridBagLayout layout = new GridBagLayout();
	setLayout( layout );

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
	layout.setConstraints( dateRangeLabel, labelConstraints );
	add( dateRangeLabel );
        layout.setConstraints( dateRange, fieldConstraints );
	add( dateRange );
/*
 TODO: date range probabilities are currently not functional so this is not enabled

	layout.setConstraints( guide1Label, labelConstraints );
	add( guide1Label );
	
	UIUtils.addLabelTextRows( labels, fields, layout, this );

	layout.setConstraints( guide2Label, labelConstraints );
	add( guide2Label );
	layout.setConstraints( certainInRangeBtn, fieldConstraints );
	add( certainInRangeBtn );
	layout.setConstraints( probablyInRangeBtn, fieldConstraints );
	add( probablyInRangeBtn );
	layout.setConstraints( maybeInRangeBtn, fieldConstraints );
	add( maybeInRangeBtn );
	layout.setConstraints( guide3Label, labelConstraints );
	add( guide3Label );
*/	
    }

    public void actionPerformed( ActionEvent e ) {
	log.debug( "Entry: DateRangeQueryEditor.actionPerformed" );
	log.debug( "command: " + e.getActionCommand() );
	if ( e.getActionCommand() == CERTAINLY_IN_RANGE ) {
	    strictness = QueryFuzzyTimeCriteria.INCLUDE_CERTAIN;
	} else if ( e.getActionCommand() == PROBABLY_IN_RANGE ) {
	    strictness = QueryFuzzyTimeCriteria.INCLUDE_PROBABLE;
	} else if ( e.getActionCommand() == MAYBE_IN_RANGE ) {
	    strictness = QueryFuzzyTimeCriteria.INCLUDE_POSSIBLE;
	} 
    }
    
    /**
       Returns the start date of the range of null if the range has unbounded start
    */
    public Date getStartDate() {
	Date start = null;
	if ( useFuzzyDateField ) {
	    log.debug( "Parsing date range" );
	    FuzzyDate fd = FuzzyDate.parse( dateRange.getText() );
	    if ( fd != null ) {
		log.debug( "Parsed date range: " + fd.format() );
		start = fd.getMinDate();
	    }
	} else {
	    start = (Date) startDate.getValue();
	}
	log.debug( "Start date " + start );
	return start;
    }

    /**
       Returns the end date of the range of null if the range has unbounded end
    */
    public Date getEndDate() {
	Date end = null;
	if ( useFuzzyDateField ) {
	    log.debug( "Parsing date range" );
 	    FuzzyDate fd = FuzzyDate.parse( dateRange.getText() );
	    if ( fd != null ) {
		log.debug( "Parsed date range: " + fd.format() );
		end = fd.getMaxDate();
	    }
	} else {
	    end = (Date) endDate.getValue();
	}
	log.debug( "End date " + end );
	return end;
    }

    public FuzzyDate getDateRange() {
	return FuzzyDate.parse( dateRange.getText() );
    }

    public int getStrictness() {
	return strictness;
    }

    
    JFormattedTextField startDate = null;
    JFormattedTextField endDate = null;
    JTextField dateRange = null;

    final static String CERTAINLY_IN_RANGE = "Certainly";
    final static String PROBABLY_IN_RANGE = "Probably";
    final static String MAYBE_IN_RANGE = "Possibly";

}
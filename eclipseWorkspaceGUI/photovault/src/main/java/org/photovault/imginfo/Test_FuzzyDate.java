/*
  Copyright (c) 2006-2008 Harri Kaimio
  
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

package org.photovault.imginfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class Test_FuzzyDate {
    
    String[] months = null;
    
    @BeforeMethod
    public void setUp() {
        // Get the month names from current locale
        DateFormat df = new SimpleDateFormat( "MMMM" );
        months = new String[12];
        for ( int n = 0; n < 12; n++ ) {
            months[n] = df.format( createDate( 2005, n, 2, 0, 0, 0 ) );
        }
    }

    @AfterMethod
    public void tearDown() {

    }


    /**
       Verify that FuzzyDate behaves well if date is null
    */
    @Test
    public void testNullDate() {
	FuzzyDate fd = new FuzzyDate( null, 0 );
	Date maxDate = fd.getMaxDate();
	assertNull( "maxDate", maxDate );
	Date minDate = fd.getMinDate();
	assertNull( "minDate", minDate );
	String asStr = fd.format();
	assertEquals( asStr, "" );
    }
    
    /**
     * Check that fuzzy date created from fuzzyDateStr spans range from min to max
     */
    
    private void checkParsingBoundaries( String fuzzyDateStr, Date min, Date max ) {
        FuzzyDate fd = FuzzyDate.parse( fuzzyDateStr );
        long min1 = min.getTime();
        long min2 = fd.getMinDate().getTime();
        if ( Math.abs( min1-min2 ) > 10000 ) {
            fail( "Min date " + fd.getMinDate() + ", expected " + min + ", difference " + (min1-min2) );
        }
        long max1 = max.getTime();
        long max2 = fd.getMaxDate().getTime();
        if ( Math.abs( max1-max2 ) > 10000 ) {
            fail( "Max date " + fd.getMaxDate() + ", expected " + max + ", difference " + (max1-max2) );
        }
    }
    
    static Calendar cal = Calendar.getInstance();
    
    private Date createDate( int year, int month, int day, int hour, int minute, int second ) {
        cal.set( year, month, day, hour, minute, second );
        return cal.getTime();
    }
    
    /**
     Test tar date ranges are parsed correctly
     */
    @Test
    public void testParseDateRange() {
	Calendar cal = Calendar.getInstance();        
        checkParsingBoundaries( "2.3.2005 - 13.4.2005", createDate( 2005, 2, 2, 0, 0, 0), createDate( 2005, 3, 13, 23, 59, 59 ) );
    }

    @Test
    public void testParseWeek() {
	Calendar cal = Calendar.getInstance();     
        // TODO: This check is made according to European week number standard - this 
        // test case fails with US locale!!!
        checkParsingBoundaries( "wk 44 2005", createDate( 2005, 9, 31, 0, 0, 0), createDate( 2005, 10, 6, 23, 59, 59 ) );
    }

    @Test
    public void testParseMonth() {
	Calendar cal = Calendar.getInstance();        
        checkParsingBoundaries( months[0] + " 2005", createDate( 2005, 0, 1, 0, 0, 0), createDate( 2005, 1, 1, 0, 0, 0 ) );
        checkParsingBoundaries( months[1] + " 2005", createDate( 2005, 1, 1, 0, 0, 0), createDate( 2005, 2, 1, 0, 0, 0 ) );
        checkParsingBoundaries( months[2] + " 2005", createDate( 2005, 2, 1, 0, 0, 0), createDate( 2005, 3, 1, 0, 0, 0 ) );
        checkParsingBoundaries( months[3] + " 2005", createDate( 2005, 3, 1, 0, 0, 0), createDate( 2005, 4, 1, 0, 0, 0 ) );
        checkParsingBoundaries( months[4] + " 2005", createDate( 2005, 4, 1, 0, 0, 0), createDate( 2005, 5, 1, 0, 0, 0 ) );
        checkParsingBoundaries( months[5] + " 2005", createDate( 2005, 5, 1, 0, 0, 0), createDate( 2005, 6, 1, 0, 0, 0 ) );
        checkParsingBoundaries( months[6] + " 2005", createDate( 2005, 6, 1, 0, 0, 0), createDate( 2005, 7, 1, 0, 0, 0 ) );
        checkParsingBoundaries( months[7] + " 2005", createDate( 2005, 7, 1, 0, 0, 0), createDate( 2005, 8, 1, 0, 0, 0 ) );
        checkParsingBoundaries( months[8] + " 2005", createDate( 2005, 8, 1, 0, 0, 0), createDate( 2005, 9, 1, 0, 0, 0 ) );
        checkParsingBoundaries( months[9] + " 2005", createDate( 2005, 9, 1, 0, 0, 0), createDate( 2005, 10, 1, 0, 0, 0 ) );
        checkParsingBoundaries( months[10] + " 2005", createDate( 2005, 10, 1, 0, 0, 0), createDate( 2005, 11, 1, 0, 0, 0 ) );
        checkParsingBoundaries( months[11] + " 2005", createDate( 2005, 11, 1, 0, 0, 0), createDate( 2006, 0, 1, 0, 0, 0 ) );
    }

    @Test
    public void testParseYear() {
	Calendar cal = Calendar.getInstance();        
        checkParsingBoundaries( "2005", createDate( 2005, 0, 1, 0, 0, 0), createDate( 2005, 11, 31, 23, 59, 59 ) );
        /* Test leap year */
        checkParsingBoundaries( "2004", createDate( 2004, 0, 1, 0, 0, 0), createDate( 2004, 11, 31, 23, 59, 59 ) );
        /* 2000 was not a leap year */
        checkParsingBoundaries( "2000", createDate( 2000, 0, 1, 0, 0, 0), createDate( 2000, 11, 31, 23, 59, 59 ) );
    }

    private void testConversionInvariance( String strFuzzyDate ) {
        FuzzyDate fd = FuzzyDate.parse( strFuzzyDate );
        String converted = fd.format();
        assertEquals( "formatted FuzzyDate does mot match original", 
                strFuzzyDate, converted );
    }
    
    /**
     * Test that fuzzy dates created from strings return the same string 
     */ 
    @Test
    public void testConversionInvariances() {
        testConversionInvariance( "01.01.2005 12:34" );
        testConversionInvariance( "01.01.2005" );
        testConversionInvariance( "30.06.2005" );
        testConversionInvariance( "wk 8 2000" );
        testConversionInvariance( "wk 52 2000" );
        testConversionInvariance( months[0] + " 2005" );
        testConversionInvariance( months[1] + " 2000" );
        testConversionInvariance( "2000" );
        testConversionInvariance( "2005" );
    }
    
    @Test
    public void testEquals() {
	Calendar cal = Calendar.getInstance();
	cal.set( 2002, 11, 23 );
	
	FuzzyDate fd1 = new FuzzyDate( cal.getTime(), 0 );
	FuzzyDate fd2 = new FuzzyDate( cal.getTime(), 0 );
	FuzzyDate fd3 = new FuzzyDate( cal.getTime(), 100 );
	cal.set( 2002, 11, 24 );
	FuzzyDate fd4 = new FuzzyDate( cal.getTime(), 0 );
	FuzzyDate fd5 = new FuzzyDate( null, 0 );
	FuzzyDate fd6 = new FuzzyDate( null, 0 );
	FuzzyDate fd7 = new FuzzyDate( null, 100 );

	assertEquals( fd1, fd2 );
	assertFalse( fd1.equals( fd3 ) );
	assertFalse( fd1.equals( fd4 ) );
	assertFalse( fd1.equals( null ) );
	assertEquals( fd5, fd6 );
	assertFalse( fd5.equals( fd7 ) );
	assertFalse( fd4.equals( fd5 ) );
	assertFalse( fd5.equals( fd4 ) );
    }
}
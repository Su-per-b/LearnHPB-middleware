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

package org.photovault.imginfo;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FuzzyDate implements Serializable {

    static final transient Log log = LogFactory.getLog( FuzzyDate.class.getName() );

    static final long serialVersionUID = -5128190100805002040L;
    
    public FuzzyDate( Date date, double accuracy ) {
	this.midpoint = (date != null) ? date.getTime() : Long.MIN_VALUE;
	this.variation = (long) (accuracy * MILLIS_IN_DAY);
    }
    
    /**
     Midpoint of the date range, as milliseconds from 00:00 Jan 1 1970 GMT
     @serial
     */
    long midpoint;
        
    /**
     Allowed deviation from midpoint in milliseconds
     @serial
     */
    long variation;
    
    static final transient double MILLIS_IN_MINUTE = 60000;
    static final transient double MILLIS_IN_HOUR = 3600 * 1000;
    static final transient double MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR;

    static class FuzzyDateParser {
        
        public FuzzyDateParser( String formatStr, long fuzzyPeriodLength ) {
            dateFormatStr = formatStr;
            this.fuzzyPeriodLength = fuzzyPeriodLength;
        }
        
        String dateFormatStr;
        long fuzzyPeriodLength;
        DateFormat df = null;
        
        protected long getFuzzyPeriodLength( Date startDate ) {
            return fuzzyPeriodLength;
        }

        /**
         * Returns the number of days in the fuzziness period.
         */ 
        protected double getFloatFuzzyPeriodLength( Date startDate ) {
            return ((double)(getFuzzyPeriodLength( startDate ) ) ) / FuzzyDate.MILLIS_IN_DAY; 
        }
        
        protected DateFormat getDateFormat() {
            if ( df == null ) {
                df = new SimpleDateFormat( dateFormatStr );
            }
            return df;
        }
        
        public FuzzyDate parse(String strDate) {
            DateFormat df = getDateFormat();
            FuzzyDate fd = null;
            try {
                Date d = df.parse( strDate );
                if ( d != null ) {
                    Date midpoint = new Date( d.getTime() + getFuzzyPeriodLength(d) / 2 );
                    fd = new FuzzyDate( midpoint, 0.5 * getFloatFuzzyPeriodLength( d ) );
                }
            } catch ( ParseException e ) {
                log.warn( "ParseException: " + e.getMessage() );

            }
            return fd;
        }
        
        /**
         * Formats a date using this formatter
         */
        public String format( Date date ) {
            DateFormat df = getDateFormat();
            return df.format( date );
        }
    }
    
    static class FuzzyDateMonthParser extends FuzzyDateParser {
        public FuzzyDateMonthParser( String formatStr ) {
            super( formatStr, 0 );
        }
        
        protected long getFuzzyPeriodLength( Date startDate ) {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime( startDate );
            c.add( GregorianCalendar.MONTH, 1 );
            return c.getTimeInMillis() - startDate.getTime();
        }        
    }

    static class FuzzyDateYearParser extends FuzzyDateParser {
        public FuzzyDateYearParser( String formatStr ) {
            super( formatStr, 0 );
        }
        
        protected long getFuzzyPeriodLength( Date startDate ) {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime( startDate );
            c.add( GregorianCalendar.YEAR, 1 );
            return c.getTimeInMillis() - startDate.getTime();
        }        
    }
    

    static transient FuzzyDateParser fdParsers[] = null;
    
    public Date getDate() {
        if ( midpoint == Long.MIN_VALUE ) {
            return null;
        }
	return new Date( midpoint );
    }

    public double getAccuracy() {
        return ((double) variation) / MILLIS_IN_DAY;
    }


    /**
       Returns the earliest time that fits into the accuracy interval
    */
    public Date getMinDate() {
        if ( midpoint == Long.MIN_VALUE ) {
            return null;
        }
	return new Date( midpoint - variation );
    }
    
    /**
       Returns the latest time that fits into the accuracy interval
    */
    public Date getMaxDate() {
        if ( midpoint == Long.MIN_VALUE ) {
            return null;
        }
	return new Date( midpoint + variation );
    }
    
    static private void createParsers() {
        fdParsers = new FuzzyDateParser[5];
        fdParsers[0] = new FuzzyDateParser( "dd.MM.yyyy k:mm", (long) 60000 );
        fdParsers[1] = new FuzzyDateParser( "dd.MM.yyyy", (long)24 * 3600 * 1000 );
        fdParsers[2] = new FuzzyDateParser( "'wk' w yyyy", (long)7 * 24 * 3600 * 1000 );
        fdParsers[3] = new FuzzyDateMonthParser( "MMMM yyyy" );
        fdParsers[4] = new FuzzyDateYearParser( "yyyy" );
    }
    
    static public FuzzyDate parse( String str ) {
        if ( fdParsers == null ) {
            createParsers();
        }
	log.warn( "Parsing " + str );
	FuzzyDate fd = null;

	// First check whether the string contains a range with start and end dates
	String rangeSeparator = " - ";
	int separatorIdx = str.indexOf( rangeSeparator );
	if ( separatorIdx > 0 ) {
	    // Yes, it is. Parse both sides separately and set the fuzzy date to cover
	    // the whole range

	    if ( str.lastIndexOf( rangeSeparator ) != separatorIdx ||
		 separatorIdx > str.length() - rangeSeparator.length() ) {
		// Only 1 range separator is allowed, return null
		return null;
	    }

	    FuzzyDate date1 = FuzzyDate.parse( str.substring( 0, separatorIdx ) );
	    FuzzyDate date2 = FuzzyDate.parse( str.substring( separatorIdx + rangeSeparator.length() ) );
	    if ( date1 == null || date2 == null ) {
		return null;
	    }

	    // Set the date as average of begin and end
	    long t1 = date1.getMinDate().getTime();
	    long t2 = date2.getMaxDate().getTime();
	    if ( t1 > t2 ) {
		// The range is not valid since end time is smaller than start time!!!
		return null;
	    }
	    Date avgDate = new Date( (t1+t2)/2 );
	    long accuracy = (t2-t1)/2;
	    
	    fd = new FuzzyDate( avgDate, ((double)accuracy) / MILLIS_IN_DAY );
	} else {
	    // No, just one date given
	
	    // attempt to parse the date using all format strings

	    for ( int i = 0; i < fdParsers.length; i++ ) {
                fd = fdParsers[i].parse( str );
		if ( fd != null ) {
                    break;
                }
	    }
	}
	return fd;
    }

    public String format() {

        String dateStr = "";
	if ( midpoint == Long.MIN_VALUE ) {
	    return "";
	}
        
        Date lower = new Date( midpoint - variation );
        Date upper = new Date( midpoint + variation - 1 );

	if ( fdParsers == null ) {
            createParsers();
        }
	if ( variation > 0 ) {
	    // Find the correct format to use
            FuzzyDateParser parser = fdParsers[0];
            for ( int i = 0; i < fdParsers.length; i++ ) {
		if ( (2 * variation) < fdParsers[i].getFuzzyPeriodLength( lower ) ) {
		    break;
		}
		parser = fdParsers[i];
	    }
	    
	    // Show the limits of the accuracy range
	    String lowerStr = parser.format( lower );
	    String upperStr = parser.format( upper );
	    dateStr = lowerStr;
	    if ( !lowerStr.equals( upperStr ) ) {
		dateStr += " - " + upperStr;
	    }
	} else {
	    DateFormat df = new SimpleDateFormat( "dd.MM.yyyy k:mm" );
	    dateStr = df.format( new Date( midpoint ) );
	}
	return dateStr;
    }

    @Override
    public boolean equals( Object obj ) {
	if ( obj instanceof FuzzyDate ) {
	    FuzzyDate fd = (FuzzyDate) obj;
            return fd.midpoint == midpoint && fd.variation == variation;
	}
	return false;
    }
    
    @Override
    public int hashCode() {
        int hash = (int) (31 * (midpoint + 27 * variation));
        return hash;
    }
}

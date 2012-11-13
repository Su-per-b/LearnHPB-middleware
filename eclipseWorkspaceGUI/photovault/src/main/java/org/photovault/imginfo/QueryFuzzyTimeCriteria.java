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

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.photovault.imginfo.FuzzyDate;

public class QueryFuzzyTimeCriteria implements QueryFieldCriteria {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( QueryFuzzyTimeCriteria.class.getName() );

    QueryField dateField;
    QueryField accuracyField;
    FuzzyDate date;

    public static final int INCLUDE_CERTAIN = 1;
    public static final int INCLUDE_PROBABLE = 2;
    public static final int INCLUDE_POSSIBLE = 3;
    int strictness;
    
    QueryFuzzyTimeCriteria( QueryField dateField, QueryField accuracyField ) {
	this.dateField = dateField;
	this.accuracyField = accuracyField;
	strictness = INCLUDE_PROBABLE;
    }

    public void setStrictness( int value ) {
	strictness = value;
    }

    public int getStrictness() {
	return strictness;
    }

    public void setDate( FuzzyDate date ) {
	this.date = date;
    }

    public FuzzyDate getDate() {
	return date;
    }

    
    
    // Implementation of imginfo.QueryFieldCriteria

    /**
     * Describe <code>setupQuery</code> method here.
     *
     * @param crit a <code>Criteria</code> value
     */
    public final void setupQuery(final Criteria crit) {
	log.debug( "Entry: SetupQuery" );

        if ( date == null ) {
            log.warn( "null query date" );
            return;
        }
        
	switch ( strictness ) {
	case INCLUDE_CERTAIN:
	    log.debug( "INCLUDE_CERTAIN" );
	    /* Only certain results must be included, so the whole
	    possible time period of object must be inside the accuracy
	    limits.  */
            /*
	    crit.addGreaterOrEqualThan( "subdate("+dateField.getName()
					+ ", " + accuracyField.getName() + ")",
					date.getMinDate() );
	    crit.addLessOrEqualThan( "adddate("+dateField.getName()
				     +", " + accuracyField.getName() + ")",
				     date.getMaxDate() );
	    */
             String gtclauseCertain = "{fn TIMESTAMPADD(SQL_TSI_SECOND, CAST( -3600*24*{alias}." + accuracyField.getColumnName() + " AS INT)"
		+", {alias}." + dateField.getColumnName() + ")} >= ?";
            crit.add( Restrictions.sqlRestriction( gtclauseCertain, date.getMinDate(), Hibernate.TIMESTAMP ) );
	    String ltclauseCertain = "{fn TIMESTAMPADD(SQL_TSI_SECOND, CAST( 3600*24*{alias}." + accuracyField.getColumnName() + " AS INT)"
		+", {alias}." + dateField.getColumnName() + ")} <= ?";
            crit.add( Restrictions.sqlRestriction( ltclauseCertain, date.getMaxDate(), Hibernate.TIMESTAMP ) );
            break;
	case INCLUDE_PROBABLE:
	    log.debug( "INCLUDE_PROBABLE" );
	    crit.add( Restrictions.between( dateField.getName(),
			     date.getMinDate(),
			     date.getMaxDate() ) );
            crit.add( Restrictions.le( accuracyField.getName(),
				     new Double( date.getAccuracy() ) ) );
	    break;

	case INCLUDE_POSSIBLE:
            /*
             * Include photos whose time range intersects with the query range
             */
	    log.debug( "INCLUDE_POSSIBLE" );
	    String gtclause = "{fn TIMESTAMPADD(SQL_TSI_SECOND, CAST( 3600*24*{alias}." + accuracyField.getColumnName() + " AS INT)"
		+", {alias}." + dateField.getColumnName() + ")} >= ?";
	    crit.add( Restrictions.sqlRestriction( gtclause,
					date.getMinDate(), Hibernate.TIMESTAMP ) );
	    log.debug( gtclause + " >= " + date.getMinDate() );
	    String ltclause = "{fn TIMESTAMPADD(SQL_TSI_SECOND, CAST( -3600*24*{alias}." + accuracyField.getColumnName() + " AS INT)"
		+", {alias}." + dateField.getColumnName() + ")} <= ?";
	    crit.add( Restrictions.sqlRestriction( ltclause,
				     date.getMaxDate(), Hibernate.TIMESTAMP ) );
	    log.debug( ltclause + " <= " + date.getMaxDate() );
	    break;
	default:
	    log.error( "Illegal value for strictness: " + strictness );
	    

	}

	log.debug( "Exit: SetupQuery" );
    }



}
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
import org.hibernate.criterion.Restrictions;

public class QueryRangeCriteria implements QueryFieldCriteria {

    public QueryRangeCriteria( QueryField field ) {
	this.field = field;
    }

    public QueryRangeCriteria( QueryField field, Object lower, Object upper ) {
	this.field = field;
	setRange( lower, upper );
    }

    public void setRange( Object lower, Object upper ) {
	this.lower = lower;
	this.upper = upper;
    }

    public void setupQuery( Criteria crit ) {
	if ( lower != null && upper != null ) {
	    crit.add( Restrictions.between( field.getName(), lower, upper ) );
	} else if ( lower != null ) {
	    crit.add( Restrictions.ge( field.getName(), lower ) );
	} else if ( upper != null ) {
	    crit.add( Restrictions.le( field.getName(), upper ) );
	}
    }

    Object lower = null;
    Object upper = null;
    QueryField field;
}
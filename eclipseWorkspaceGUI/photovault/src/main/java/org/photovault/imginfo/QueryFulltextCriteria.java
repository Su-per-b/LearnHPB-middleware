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

public class QueryFulltextCriteria implements QueryFieldCriteria {
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( QueryFulltextCriteria.class.getName() );

    public QueryFulltextCriteria( QueryField field ) {
	this.field = field;
    }

    public QueryFulltextCriteria( QueryField field, String text ) {
	this.field = field;
	this.text = text;
    }

    public void setText( String text ) {
	this.text = text;
    }

    public void setupQuery( Criteria crit ) {
	String sql = "MATCH(" + field.getName() + ") AGAINST('" + text + "')";
	log.debug( sql );
        crit.add( Restrictions.sqlRestriction( "MATCH(" + field.getName() + ") AGAINST( ? )", text, Hibernate.STRING ) );
	log.debug( "Added" );
    }

    String text = null;
    QueryField field;
}
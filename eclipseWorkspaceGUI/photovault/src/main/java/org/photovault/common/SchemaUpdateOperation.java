/*
  Copyright (c) 2009 Harri Kaimio

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

package org.photovault.common;

/**
 * This class describes a certain operation in converting the database schema to
 * a newer version.
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class SchemaUpdateOperation {
    /**
     * Description of this operation
     */
    private String desc;

    /**
     * Operation that indicates that the conversion is completed and no further
     * actions are needed
     */
    public final static SchemaUpdateOperation UPDATE_COMPLETE =
            new SchemaUpdateOperation( "Complete" );

    /**
     * Constructor
     * @param desc Description of this operation
     */
    public SchemaUpdateOperation( String desc ) {
        this.desc = desc;
    }

    /**
     * Get the description of this operations
     * @return
     */
    public String getDescription() {
        return desc;
    }


}

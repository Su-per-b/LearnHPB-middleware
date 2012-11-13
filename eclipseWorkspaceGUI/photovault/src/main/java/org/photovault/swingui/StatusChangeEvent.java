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

import java.util.EventObject;

/**
 Event that indicates that the status of some process htat is of interest for user
 has changed.
 <p>
 This is used to inform @see StatusChangeListener objects about the change
 */
public class StatusChangeEvent extends EventObject {

    private String message;
    
    /** 
     Creates a new instance of StatusChangeEvent
     @param src Source of this status change
     @param message Message that is to be displayed to user
     */
    public StatusChangeEvent( Object src, String message ) {
        super( src );
        this.message = message;
    }
    
    /**
     Get the message describing this status change
     */
    public String getMessage() {
        return message;
    }
}

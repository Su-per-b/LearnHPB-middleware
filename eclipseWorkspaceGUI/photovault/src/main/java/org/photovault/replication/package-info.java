/*
  Copyright (c) 2008 Harri Kaimio
 
  This file is part of Photovault.
 
  Photovault is free software; you can redistribute it and/or modify it
  under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.
 
  Photovault is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even therrore implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.
 
  You should have received a copy of the GNU General Public License
  along with Photovault; if not, write to the Free Software Foundation,
  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

/**
 Usage of replication services
 
 - Create an enum for listing the fields in the replicable object
 
 - Subclass ChangeSupport, implement setField and getField methods for adjusting 
 fields

 Editing the object
 
 - Create a new change:
 
 Change c = o.createChange();
 
 - Make needed modifications in the change
 
 - Freeze the change:
 
 c.freeze();
 
 This will also apply the change to o.
 
 Merging conflicts
 
 Calling freeze() will throw an exception if there is more than one head. In this 
 case, you need to merge the additional heads to this change:
 
 o.getHeads();
 
 c.merge( head );
 
 for ( Conflict co : c.getConflicts() ) {
     c.resolve();
 }
 */
package org.photovault.replication;


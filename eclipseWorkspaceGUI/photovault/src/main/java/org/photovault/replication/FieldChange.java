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

package org.photovault.replication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 FieldChange is the base class for all classes that describe a change made to a 
 field of an object. A field is defined as an orthogonal subpart of the object, 
 i.e. change to a field may not change value of any other field of any object.
 <p>
 FieldChange is cloneable as copies of the objetc may be needed in processing 
 changes. FieldChange itself implementa clone() by just calling Object.clone(); 
 derived concrete classes must override this if the state change description is 
 not immutable.
 
 @author Harri Kaimio
 @since 0.6.0

 */
abstract class FieldChange implements Cloneable {
    
    /**
     Name of the field
     */
    protected String name;
    
    List<FieldConflictBase> conflicts;
    
    /**
     Default constructor. Package protected as it should be used only by 
     serialization mechanism
     */
    FieldChange() {}
    
    /**
     Constructor
     @param name Name of the changed field
     */
    public FieldChange( String name ) {
        this.name = name;
    }
    
    /**
     Returns name of the changed field
     */
    public final String getName() {
        return name;
    }
    
    /**
     Get all conflicts in this field change if it is part of a merge change. 
     If there are no conflicts, returns an empty collection.
     @return
     */
    public final Collection<FieldConflictBase> getConflicts() {
        if ( conflicts == null ) {
            conflicts = new ArrayList<FieldConflictBase>();
        }
        return Collections.unmodifiableCollection( conflicts );
    }
    
    /**
     Add conflict to this change. Called by change during meerge operation
     @param conflict
     */
    protected final void addConflict( FieldConflictBase conflict ) {
        if ( conflicts == null ) {
            conflicts = new ArrayList<FieldConflictBase>();
        }
        conflicts.add( conflict );
    }
    
    /**
     Called by field conflict to inform that the conflict in question has been 
     resolved.
     @param conflict The resolved conflict.
     */
    void conflictResolved( FieldConflictBase conflict ) {
        conflicts.remove( conflict );
    }
            
    
    /**
     Returns <code>true</code> if the change conflicts with another change. 
     Conflict means that the state of the changed field will be different 
     depending on the order in which these changes are applied.
     @param ch The other change
     @return Whether the changes conflict
     */
    public abstract boolean conflictsWith( FieldChange ch );
    
    /**
     Add the state of another change to this object, as the other change would
     have been applied to the field after this change.
     @param ch
     */
    public abstract void addChange( FieldChange ch );
    
    /**
     Add the state of another change to this object so as the other change would
     have been applied before this one.
     @param ch
     */
    
    public abstract void addEarlier( FieldChange ch );
    
    /**
     Merge this change with another change
     @param ch The change to merge
     @return The merged change
     */
    public abstract FieldChange merge( FieldChange ch );
    
    /**
     Creates a FieldChange object that reverses the impact of this change when 
     both field changes are applied to baseline.
     @param baseline BAseline change against which the the operation is 
     constructed (as not all changes contain enough state to be reversible, 
     constructing the reverse operation may require looking back in history.
     @return
     */
    public abstract FieldChange getReverse( Change baseline );
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}

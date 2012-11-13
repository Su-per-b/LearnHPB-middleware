/*
  Copyright (c) 2008 Harri Kaimio
  
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

package org.photovault.replication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 Description of a conflict that arises from merging several changes to a set. 
 Unlike single valued field, there can be several items in the set that cause a 
 conflict; this object describes the operations affcetion a single item that 
 lead to a conflict.
 
 @author Harri Kaimio
 @since 0.6.0
 */
public class SetFieldConflict extends FieldConflictBase {

    /**
     Item that has been changed in several branches in conflicting ways
     */
    Object item;

    /**
     Operations done to the item in different branches
     */
    List<SetOperation> operations;
    
    /**
     COnstructor
     @param ch The merged change
     @param item Item that causes the conflict
     @param ops Operations done to the item in different branches
     */
    SetFieldConflict( SetChange ch, Object item, List<SetOperation> ops ) {
        super( ch );
        this.item = item;
        this.operations = ops;
    }
    
    /**
     Constructor for creating descriptor of conflict between two branches
     @param ch The merged change
     @param item Item that causes the conflict
     @param op1 Operation done in first branch 
     @param op2 Operation done in second branch 
     */
    SetFieldConflict( SetChange ch, Object item, SetOperation op1, SetOperation op2 ) {
        super( ch );
        this.item = item;
        List<SetOperation> ops = new ArrayList<SetOperation>( 2 );
        ops.add( op1 );
        ops.add( op2 );
        this.operations = ops;
    }
    
    /**
     Returns the conflicting item
     */
    public Object getItem() {
        return item;
    }

    /**
     Returns list of operations done for the item in all branches
     */
    public List<SetOperation> getOperations() {
        return Collections.unmodifiableList( operations );
    }
    
    @Override
    public void resolve( int winningBranch ) {
        SetOperation op = operations.get(  winningBranch );
        if ( op == SetOperation.ADD ) {
            ((SetChange)change).addItem( item );
        } else {
            ((SetChange)change).removeItem( item );            
        }
        change.conflictResolved( this );
    }

}

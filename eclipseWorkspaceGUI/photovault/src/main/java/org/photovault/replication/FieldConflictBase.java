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

/**
  Base class for classes describing field conflicts that occur during merging
 changes from different branches
 
 @author Harri Kaimio
 @since 0.6.0
 
 */
public abstract class FieldConflictBase {
    
    /**
     The merged change that contains this conflict
     */
    protected FieldChange change;
    
    /**
     Constructor
     @param ch The merged field change that contains this conflict
     */
    protected FieldConflictBase( FieldChange ch ) {
        change = ch;
    }
    
    /**
     Returns the name of the conflicting field
     */
    public String getFieldName() {
        return change.name;
    }
    
    /**
     Resolve the conflict by setting state of the merged change to match that 
     from given branch. 
     @param winningBranch Number of the branch. Derived chasses should follow 
     convention that they priovide a method for retrieving a list of state change
     descriptions in all branches, adn this parameter should then be the order 
     number of the winning branch.
     */
    abstract public void resolve( int winningBranch );
}

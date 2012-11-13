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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 Base class for describing conflicts that have occurred during merge
 */
public abstract class ConflictBase {

    /**
     Changes that conflict
     */
    protected Set<Change> changes;
    
    /**
     The change that should merge these cahnges together
     */
    protected Change mergeChange;
    
    /**
     Constructor
     @param mergeChange The merge change
     @param changes conflicting changes
     */
    public ConflictBase( Change mergeChange, Change[] changes ) {
        this.mergeChange = mergeChange;
        this.changes = new HashSet<Change>( Arrays.asList( changes ) );
    }
    
    /**
     Get the changes
     @return
     */
    public  Set<Change> getChanges() {
        return Collections.unmodifiableSet( changes );
    }
    
    /**
     Resolve the conflict by selecting which value will be used. Derived classes
     must override this method to do needded resolving
     @param winningChange The change that will be used to resolve the conflict
     */
    public abstract void resolve( Change winningChange );
    

}

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
import java.util.List;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 Test cases for {@link SetChange}
 
 @author Harri Kaimio
 @since 0.6.0 
 */
public class Test_SetChange {
    
    /**
     Test that basic operations (adding or removing item) work
     */
    @Test
    public void testChangeElem() {
        SetChange s = new SetChange( "t" );
        s.addItem( 1 );
        assertTrue( s.getAddedItems().contains( 1 ) );
        s.removeItem( 1 );
        assertTrue( s.getRemovedItems().contains( 1 ) );
        assertFalse( s.getAddedItems().contains( 1 ) );
    }
    
    /**
     Test that adding two changes work
     */
    @Test
    public void testAddChanges() {
        SetChange s1 = new SetChange( "t" );
        SetChange s2 = new SetChange( "t" );
        s1.addItem( 1 );
        s1.addItem( 2 );
        s2.removeItem( 1 );
        s2.addItem(  3 );
        s1.addChange( s2 );
        assertTrue( s1.getAddedItems().contains( 2 ) );
        assertTrue( s1.getAddedItems().contains( 3 ) );
        assertFalse( s1.getAddedItems().contains( 1 ) );

        SetChange s0 = new SetChange( "t" );
        s0.removeItem(  3 );
        s0.removeItem( 4 );
        s0.addItem( 5 );
        
        s1.addEarlier( s0 );
        assertTrue( s1.getAddedItems().contains( 3 ) );
        assertTrue( s1.getAddedItems().contains( 5 ) );
        assertTrue( s1.getRemovedItems().contains( 4 ) );
        
    }

    /**
     Test that merging changes, including conflict detection and resolving work
     */
    @Test
    public void testMerge() {
        SetChange s1 = new SetChange( "t" );
        SetChange s2 = new SetChange( "t" );
        
        s1.addItem( 1 );
        s1.removeItem(  2 );
        s1.addItem( 3 );
        s1.removeItem( 4 );
        
        s2.addItem( 5 );
        s2.addItem( 2 );
        s2.removeItem( 3 );
        s2.removeItem(  6 );
        
        SetChange merged = (SetChange) s1.merge( s2 );
        assertTrue( merged.getAddedItems().contains( 1 ) );
        assertTrue( merged.getAddedItems().contains( 5 ) );
        assertTrue( merged.getRemovedItems().contains( 4 ) );
        assertTrue( merged.getRemovedItems().contains( 6 ) );
        List<FieldConflictBase> conflicts = 
                new ArrayList<FieldConflictBase>( merged.getConflicts() );
        assertEquals( 2, conflicts.size() );
        int found = 0;
        for ( FieldConflictBase c : conflicts ) {
            SetFieldConflict sc = (SetFieldConflict) c;
            List<SetOperation> ops = sc.getOperations();
            if ( sc.getItem().equals( 2 ) ) {
                assertEquals( SetOperation.REMOVE, ops.get(  0 ) );
                assertEquals( SetOperation.ADD, ops.get( 1 ) );
                c.resolve( 0 ); // Remove wins
                found++;
            } else if ( sc.getItem().equals( 3 ) ) {
                assertEquals( SetOperation.ADD, ops.get(  0 ) );
                assertEquals( SetOperation.REMOVE, ops.get( 1 ) );                
                c.resolve( 1 ); // Remove wins
                found++;
            }
        }
        assertEquals( 2, found );
        assertTrue( merged.getRemovedItems().contains( 2 ) );
        assertTrue( merged.getRemovedItems().contains( 3 ) );
    }
}

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
/**
 Test cases for {@link Change} class
 */
public class Test_Change {
    
    static private class TestDtoResolvFactory implements DTOResolverFactory {

        static DTOResolver resolver = new DefaultDtoResolver();
        public DTOResolver getResolver( Class<? extends DTOResolver> clazz ) {
            if ( DefaultDtoResolver.class.equals( clazz) ) {
            return resolver;
            }
            try {
                return clazz.newInstance();
            } catch ( InstantiationException ex ) {
            } catch ( IllegalAccessException ex ) {
            }
            return resolver;
        }
        
    }
    
    DTOResolverFactory resolvFactory = new TestDtoResolvFactory();
    
    static interface TestObjectEditor {
        public void setF1( int i );
        public void setF2( int i );
        public void setSub( TestStruct sub );
    }

    static public class TestStruct {
        private int i1;
        private int i2;
        private Map<String, TestStruct> map = new HashMap<String, TestStruct>();

        public  TestStruct( int i1, int i2 ) {
            this.i1 = i1;
            this.i2 = i2;
        }

        public TestStruct() {}

        /**
         * @return the i1
         */
        public int getI1() {
            return i1;
        }

        /**
         * @param i1 the i1 to set
         */
        public void setI1( int i1 ) {
            this.i1 = i1;
        }

        /**
         * @return the i2
         */
        public int getI2() {
            return i2;
        }

        /**
         * @param i2 the i2 to set
         */
        public void setI2( int i2 ) {
            this.i2 = i2;
        }

        public void setMapItem( String item, TestStruct value ) {
            map.put( item, value );
        }

        public TestStruct getMapItem( String item ) {
            return map.get( item );
        }

        public String toString() {
            return "(" + i1 + ", " + i2 + ")";
        }
    }
    
    @Versioned(editor=TestObjectEditor.class )
    static public class TestObject {

        Change version;
        
        ObjectHistory<TestObject> cs = new ObjectHistory<TestObject>( this );
        
        @History
        public ObjectHistory<TestObject> getHistory() { return cs; };
        
        UUID uuid = UUID.randomUUID();

        TestObject() {
            cs.setTargetUuid( uuid );
        }

        public Change getVersion() {
            return cs.getVersion();
        }
        
        int f1;
        int f2;
        private TestStruct sub;

        public void setF2( int i ) {
            f2 = i;
        }

        @ValueField
        public int getF2() {
            return f2;
        }
        
        public void setF1( int i ) {
            f1 = i;
        }
        
        @ValueField
        public int getF1() {
            return f1;
        }
        
        Set<Integer> numbers = new HashSet<Integer>();
        
        @SetField( elemClass=int.class )
        public Set<Integer> getNumbers() { return numbers; }
        
        public void addNumber( int n ) { numbers.add( n ); }

        public void removeNumber( int n ) { numbers.remove( n ); }

        
        public UUID getGlobalId() {
            return uuid;
        }

        public Change createChange() {
            return cs.createChange();
        }

        @ValueField( dtoResolver=BeanDtoResolver.class )
        public TestStruct getSub() {
            return sub;
        }

        public void setSub( TestStruct sub ) {
            this.sub = sub;
        }


        
    }
    @Test
    public void testApplyChange() {
        TestObject t = new TestObject();
        t.f1 = 1;
        t.f2 = 2;
        
        
        VersionedObjectEditor<TestObject> e1 = new VersionedObjectEditor<TestObject>(  t, resolvFactory );
           
        e1.setField( "f2", 3 );
        e1.addToSet(  "numbers", 2 );
        e1.addToSet(  "numbers", 3 );
        e1.setField( "sub", new TestStruct( 1,2 ) );
        e1.setField( "sub.mapItem(test)", new TestStruct( 2,3 ) );
        e1.setField( "sub.mapItem(test2)", new TestStruct( 5,6 ) );
        e1.apply();
        Change<TestObject> c = e1.getChange();
        assertEquals( 3, t.f2 );
        assertEquals( 1, t.f1 );
        assertEquals( 1, t.getSub().getI1() );
        assertEquals( 2, t.getSub().getI2() );
        assertEquals( c, t.getVersion() );
        assertEquals( 3, t.getSub().getMapItem( "test" ).getI2() );
        
        VersionedObjectEditor<TestObject> e2 = new VersionedObjectEditor<TestObject>(  t, resolvFactory );
        e2.setField("f2", 5 );
        e2.setField( "sub.i1", 3 );
        e2.setField( "sub.mapItem(test).i2", 7 );
        e2.removeFromSet( "numbers", 2 );
        e2.addToSet( "numbers", 4 );
        e2.apply();
        assertEquals( 5, t.f2 );
        assertEquals( 3, t.getSub().getI1() );
        assertFalse( t.getNumbers().contains( 2 ) );
        assertTrue( t.getNumbers().contains( 3 ) );
        assertTrue( t.getNumbers().contains( 4 ) );
        Change<TestObject> c2 = e2.getChange();
        assertEquals( c2, t.getVersion() );
        assertEquals( c, c2.getPrevChange() );
        assertTrue( c.getChildChanges().contains( c2 ) );
    }
    
    @Test( expectedExceptions={IllegalStateException.class} )
    public void testApplyWrongChange() {
        TestObject t = new TestObject();
        VersionedObjectEditor<TestObject> e1 = new VersionedObjectEditor<TestObject>(  t, resolvFactory );
        TestObjectEditor te = (TestObjectEditor) e1.getProxy();
                
        te.setF1( 1 );
        te.setF2( 2 );
        e1.apply();
        
        Change<TestObject> initialState = e1.getChange();
        
        Change<TestObject> c = new Change<TestObject>( t.cs );
        c.freeze();
        Change<TestObject> c2 = new Change<TestObject>( t.cs );        
        c2.setPrevChange( c );
        c.setField("f2", 3 );
        c.freeze();
    }
    
    @Test
    public void testMerge() {
        TestObject t = new TestObject();
        VersionedObjectEditor<TestObject> e = new VersionedObjectEditor<TestObject>( t, resolvFactory );
        TestObjectEditor te = (TestObjectEditor) e.getProxy();
                
        te.setF1( 1 );
        te.setF2( 2 );
        te.setSub( new TestStruct() );
        e.apply();
        assertEquals( 1, t.f1 );
        assertEquals( 2, t.f2 );
        
        Change<TestObject> initialState = e.getChange();
        
        e = new VersionedObjectEditor<TestObject>( t, resolvFactory );
        e.setField( "f1", 2 );
        e.setField( "f2", 3 );
        e.addToSet( "numbers", 1 );
        e.addToSet( "numbers", 2 );
        e.setField( "sub.mapItem(test)", new TestStruct( 10, 2 ) );
        e.apply();
        Change<TestObject> c = e.getChange();
        
        e = new VersionedObjectEditor<TestObject>( t, resolvFactory );
        e.setField("f1", 3);
        e.setField("f2", 5);
        e.setField( "sub.i1", 3 );
        e.setField( "sub.mapItem(test)", new TestStruct( 11, 3 ) );
        e.addToSet( "numbers", 3 );
        e.removeFromSet( "numbers", 1 );
        e.apply();
        Change<TestObject> c2 = e.getChange();

        e = new VersionedObjectEditor<TestObject>( t, resolvFactory );
        e.changeToVersion( c );
        assertEquals( c, t.getVersion() );
        assertEquals( 2, t.f1 );
        assertEquals( 3, t.f2 );
        assertEquals( 0, t.getSub().getI1() );
        assertEquals( 10, t.getSub().getMapItem( "test" ).getI1() );
        assertTrue( t.numbers.contains( 1 ) );
        assertFalse( t.numbers.contains( 3 ) );
        
        e.setField("f1", 4);
        e.setField("f2", 5);
        e.setField( "sub.i1", 4 );
        e.setField( "sub.mapItem(test).i1", 14 );
        e.addToSet( "numbers", 1 );
        e.addToSet( "numbers", 4 );
        e.removeFromSet(  "numbers", 2 );        
        e.apply();
        Change<TestObject> c3 = e.getChange();
        
        
        Change merged = c2.merge( c3 );
        
        assertTrue( merged.hasConflicts() );
        
        Collection<FieldConflictBase> conflicts = merged.getFieldConficts();
        assertEquals( 4, merged.getFieldConficts().size() );
        ValueFieldConflict f1Conflict = null;
        ValueFieldConflict subI1Conflict = null;
        ValueFieldConflict subMapConflict = null;
        SetFieldConflict numbersConflict = null;
        // Check the conflicts
        boolean f1ConflictExists = false;
        boolean f2COnflictExists = false;
        boolean subI1ConflictExists = false;
        boolean subMapConflictExists = false;
        boolean numbersConflictExists = false;
        for ( FieldConflictBase cf : conflicts ) {
            if ( cf.getFieldName().equals( "f1" ) ) {
                f1ConflictExists = true;
                f1Conflict = (ValueFieldConflict) cf;
                assertTrue( f1Conflict.getConflictingValues().contains( 4 ) );
                assertTrue( f1Conflict.getConflictingValues().contains( 3 ) );
            } else if ( cf.getFieldName().equals( "f2" ) ) {
                f2COnflictExists = true;
            } else if ( cf.getFieldName().equals( "numbers" ) ) {
                numbersConflict = (SetFieldConflict) cf;
                assertEquals( SetOperation.REMOVE, numbersConflict.getOperations().
                        get( 0 ) );
                assertEquals( SetOperation.ADD, numbersConflict.getOperations().
                        get( 1 ) );
                numbersConflictExists = true;
            } else if ( cf.getFieldName().equals( "sub" ) ) {
                ValueFieldConflict vc = (ValueFieldConflict) cf;
                if ( "i1".equals( vc.getProperty() ) ) {
                    subI1ConflictExists = true;
                    subI1Conflict = vc;
                } else if ( "mapItem(test).i1".equals( vc.getProperty() ) ) {
                    subMapConflictExists = true;
                    subMapConflict = vc;
                } else {
                    fail( "conflict for " + vc.getProperty() );
                }
            }
        }
        assertTrue( f1ConflictExists );
        assertFalse( f2COnflictExists );
        assertTrue( numbersConflictExists );
        assertTrue( subI1ConflictExists );
        assertTrue( subMapConflictExists );
        
        f1Conflict.resolve( 0 );
        numbersConflict.resolve( 1 );
        subI1Conflict.resolve( 1 );
        subMapConflict.resolve( 0 );
        assertFalse( merged.hasConflicts() );
        /*
         TODO: current API is broken as we must manually freeze the change after 
         resolving conflicts
         */
        merged.freeze();
        assertEquals( 3, merged.getField("f1") );
        assertEquals( 2, t.getSub().getMapItem( "test").getI2() );
        assertEquals( 14, t.getSub().getMapItem( "test").getI1() );
        e = new VersionedObjectEditor<TestObject>( t, resolvFactory );
        e.changeToVersion( merged );
        assertTrue( t.numbers.contains( 1 ) );
        assertTrue( t.numbers.contains( 4 ) );
        assertFalse( t.numbers.contains( 2 ) );
        assertEquals( 11, t.getSub().getMapItem( "test").getI1() );
    }
    
    @Test
    public void testSerialize() throws IOException, ClassNotFoundException {
        TestObject t = new TestObject();
        t.f1 = 1;
        t.f2 = 2;   
        
        Change c = t.createChange();
        c.setField( "f1", 2 );
        c.setField( "f2", 3 );
        c.freeze();
        
        Change c2 = t.createChange();
        c2.setField("f1", 3);
        c2.setField("f2", 5);
        c2.setFieldProperty( "sub", "i1", 6 );
        c2.freeze();
        
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream( s );
        ChangeDTO dto1 = new ChangeDTO( c );
        ChangeDTO dto2 = new ChangeDTO( c2 );
        os.writeObject( dto1 );
        os.writeObject( dto2 );
        
        byte[] serialized = s.toByteArray();
        String asString = new String(serialized, "utf-8" );

        ByteArrayInputStream is = new ByteArrayInputStream( serialized );
        ObjectInputStream ios = new ObjectInputStream( is );
        ChangeDTO readDto1 = (ChangeDTO) ios.readObject();
        ChangeDTO readDto2 = (ChangeDTO) ios.readObject();
        
        readDto1.verify();
        readDto2.verify();
    }

}

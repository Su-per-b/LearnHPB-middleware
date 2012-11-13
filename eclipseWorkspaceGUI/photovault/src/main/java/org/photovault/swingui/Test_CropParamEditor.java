/*
  Copyright (c) 2006-2007 Harri Kaimio
  
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

import abbot.finder.BasicFinder;
import abbot.finder.ComponentNotFoundException;
import abbot.finder.Matcher;
import abbot.finder.MultipleComponentsFoundException;
import abbot.tester.ComponentTester;
import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author harri
 */
public class Test_CropParamEditor extends TestCase {
    
    private ComponentTester tester;
    CropParamEditor editor;
    
    /** Creates a new instance of Test_CropParamEditor */
    public Test_CropParamEditor() {
    }
    
    public void setUp() {
        editor = new CropParamEditor( null, false );
        tester = ComponentTester.getTester( editor );
    }

    
    public void tearDown() {
        try {
            SwingUtilities.invokeAndWait( new Runnable() {
                public void run() { 
                    editor.setVisible( false );     
                    editor.dispose();
                }
            });
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
        
    // handy abbreviation for displaying our test frame
    private void showFrame() {
        // Always do direct component manipulation on the event thread
        tester.invokeAndWait(new Runnable() {
            public void run() { editor.setVisible( true );  }
        });
    }
    
    
    private JTextField findField( String name ) {
        final String staticName = name;
        class FieldMatcher implements Matcher {
            public boolean matches( Component c ) {
                return c instanceof JTextField && staticName.equals( c.getName() );
            }
        }
        BasicFinder finder = new BasicFinder();
        
        Matcher m = new FieldMatcher();
        JTextField f = null;
        try {
            f = (JTextField) finder.find( editor, m );
        } catch (ComponentNotFoundException ex) {
            ex.printStackTrace();
        } catch (MultipleComponentsFoundException ex) {
            ex.printStackTrace();
        }
        return f;
    }
    
    class TestListener implements CropParamEditorListener {
        public boolean cropApplied = false;
        public boolean cropCanceled = false;
        public boolean paramsChanged = false;
        
        public void applyCrop() {
            cropApplied = true;
        }

        public void cancelCrop() {
            cropCanceled = true;
        }

        public void cropParamsChanged() {
            paramsChanged = true;
        }
        
    }
    
    /**
     Test that changing rotation works.
     
     */
    public void testRotationChange() {
        showFrame();
        editor.setRot( 76.8 );
        JTextField rotField = findField( "rotField" );
        JTextField xminField = findField( "xminField" );
        assertEquals( "76.8", rotField.getText() );
        
        // Change the string
        TestListener l = new TestListener();
        editor.addListener( l );
        tester.actionClick( rotField, 1, 1, InputEvent.BUTTON1_MASK, 2 );
        tester.actionKeyString( rotField, "80" );
        // tester.action( xminField );
        
        assertTrue( l.paramsChanged );
        assertEquals( 80.0, editor.getRot() );
        
        // Check that non-number string does not chane the value
        l.paramsChanged = false;
        tester.actionClick( rotField, 1, 1, InputEvent.BUTTON1_MASK, 2 );
        tester.actionKeyString( rotField, "No number" );
        assertFalse( l.paramsChanged );
        assertEquals( 80.0, editor.getRot() );        
    }

    public void testApplyOnEnter() {
        showFrame();
        editor.setRot( 76.8 );
        JTextField rotField = findField( "rotField" );
        
        // Change the string
        TestListener l = new TestListener();
        editor.addListener( l );
        tester.actionClick( rotField, 1, 1, InputEvent.BUTTON1_MASK, 2 );
        tester.actionKeyString( rotField, "80" );
        tester.actionKeyStroke( KeyEvent.VK_ENTER );
        
        assertTrue( l.cropApplied );
        assertEquals( 80.0, editor.getRot() );
    }

    public void testCancelOnEsc() {
        showFrame();
        editor.setRot( 76.8 );
        JTextField rotField = findField( "rotField" );
        
        // Change the string
        TestListener l = new TestListener();
        editor.addListener( l );
        tester.actionKeyStroke( KeyEvent.VK_ESCAPE );
        // tester.action( xminField );
        
        assertTrue( l.cropCanceled );
    }

    
    public void testOKBtn() {
        showFrame();
        TestListener l = new TestListener();
        editor.addListener( l );

        // Find the OK button
        final String staticName = "okBtn";
        class FieldMatcher implements Matcher {
            public boolean matches( Component c ) {
                return c instanceof JButton && staticName.equals( c.getName() );
            }
        }
        BasicFinder finder = new BasicFinder();
        Matcher m = new FieldMatcher();
        JButton btn = null;
        try {
            btn = (JButton) finder.find( editor, m );
        } catch (ComponentNotFoundException ex) {
            ex.printStackTrace();
        } catch (MultipleComponentsFoundException ex) {
            ex.printStackTrace();
        }
        
        tester.actionClick( btn );
        assertTrue( l.cropApplied );
    }
    
    public void testCancelBtn() {
        showFrame();
        TestListener l = new TestListener();
        editor.addListener( l );

        // Find the OK button
        final String staticName = "cancelBtn";
        class FieldMatcher implements Matcher {
            public boolean matches( Component c ) {
                return c instanceof JButton && staticName.equals( c.getName() );
            }
        }
        BasicFinder finder = new BasicFinder();
        Matcher m = new FieldMatcher();
        JButton btn = null;
        try {
            btn = (JButton) finder.find( editor, m );
        } catch (ComponentNotFoundException ex) {
            ex.printStackTrace();
        } catch (MultipleComponentsFoundException ex) {
            ex.printStackTrace();
        }
        
        tester.actionClick( btn );
        assertTrue( l.cropCanceled );
    }

    
    public static void main( String[] args ) {
	junit.textui.TestRunner.run( suite() );
    }
    
    public static Test suite() {
	return new TestSuite( Test_CropParamEditor.class );
    }    
}

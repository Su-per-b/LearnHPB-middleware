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

package org.photovault.swingui.color;

import abbot.finder.BasicFinder;
import abbot.finder.ComponentNotFoundException;
import abbot.finder.Matcher;
import abbot.finder.MultipleComponentsFoundException;
import abbot.tester.ComponentTester;
import abbot.tester.JSliderTester;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Hashtable;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Module test cases for {@link FieldSliderCombo}
 * @author Harri Kaimio
 * @since 0.4.0
 */
public class Test_FieldSliderCombo extends TestCase {
    
    /** Creates a new instance of Test_FieldSliderCombo */
    public Test_FieldSliderCombo() {
    }

    private ComponentTester tester;    
    FieldSliderCombo c = null;
    JFrame frame = null;
    JPanel pane = null;
    JFormattedTextField valueField = null;
    JSlider valueSlider = null;
    
    public void setUp() {
        c = new FieldSliderCombo();
        frame = new JFrame(getName());        
        pane = (JPanel)frame.getContentPane();
        pane.setLayout(new FlowLayout());
        pane.setBorder(new EmptyBorder(50, 50, 50, 50));
        tester = ComponentTester.getTester( c );
        pane.add( c );     
        valueField = findValueField();
        valueSlider = findValueSlider();
    }    

    // handy abbreviation for displaying our test frame
    private void showFrame() {
        // Always do direct component manipulation on the event thread
        tester.invokeAndWait(new Runnable() {
            public void run() { frame.pack(); frame.show(); }
        });
    }
    
    private JFormattedTextField findValueField() {
        class FieldMatcher implements Matcher {
            public boolean matches( Component c ) {
                return c instanceof JFormattedTextField;
            }
        }
        BasicFinder finder = new BasicFinder();
        
        Matcher m = new FieldMatcher();
        JFormattedTextField f = null;
        try {
            f = (JFormattedTextField) finder.find( c, m );
        } catch (ComponentNotFoundException ex) {
            ex.printStackTrace();
        } catch (MultipleComponentsFoundException ex) {
            ex.printStackTrace();
        }
        return f;
    }    
    
    private JSlider findValueSlider() {
        class FieldMatcher implements Matcher {
            public boolean matches( Component c ) {
                return c instanceof JSlider;
            }
        }
        BasicFinder finder = new BasicFinder();
        
        Matcher m = new FieldMatcher();
        JSlider f = null;
        try {
            f = (JSlider) finder.find( c, m );
        } catch (ComponentNotFoundException ex) {
            ex.printStackTrace();
        } catch (MultipleComponentsFoundException ex) {
            ex.printStackTrace();
        }
        return f;
    }    
    
    /**
     * test basic functionality of the component.
     */
    public void testShow() {
        showFrame();
        c.setMajorTickSpacing( 0.5 );
        c.setMinorTickSpacing( 0.1 );
        assertEquals( 1, valueSlider.getMinorTickSpacing() );
        c.setPaintTicks( true );
        assertEquals( true, valueSlider.getPaintTicks() );
        c.setValue( 0.3 );
        assertEquals( 3, valueSlider.getValue() ); 
        assertEquals( "0.3", valueField.getText() );
        JSliderTester sliderTester = new JSliderTester();
        sliderTester.actionSlide( valueSlider, 7 );
        assertEquals( "0.7", valueField.getText() );
        sliderTester.actionSlideMaximum( valueSlider );
        assertEquals( "1.0", valueField.getText() );
        sliderTester.actionSlideMinimum( valueSlider );
        assertEquals( "0.0", valueField.getText() );
        sliderTester.actionSlide( valueSlider, 7 );
        c.setAnnotations( new double[] {0.0, 0.2, 0.45} );
        Hashtable labels = new Hashtable();
        labels.put( new Double( 0.0 ), new JLabel( "0" ) );
        labels.put( new Double( 0.5 ), new JLabel( "0.5" ) );
        labels.put( new Double( 1.0 ), new JLabel( "1" ) );
        c.setPaintLabels( true );
        c.setFractionDigits( 2 );
        assertEquals( 10, valueSlider.getMinorTickSpacing() );
        assertEquals( "0.70", valueField.getText() );
        assertEquals( 70, valueSlider.getValue() );         
        c.setMaximum( 10.0 );
        assertEquals( 1000, valueSlider.getMaximum() );                 
    }
    
    public void tearDown() {
        frame.dispose();
    }    
    
        
    public static void main( String[] args ) {
	junit.textui.TestRunner.run( suite() );
    }
    
    public static Test suite() {
	return new TestSuite( Test_FieldSliderCombo.class );
    }    
}


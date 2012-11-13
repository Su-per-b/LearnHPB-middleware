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

import abbot.tester.ComponentTester;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.photovault.image.ColorCurve;

/**
 Module test cases for ColorCurvePanel
 */
public class Test_ColorCurvePanel extends TestCase {
    
    /** Creates a new instance of Test_ColorCurvePanel */
    public Test_ColorCurvePanel() {
    }

    ColorCurvePanel cp;
    private ComponentTester tester;    
    JFrame frame = null;
    JPanel pane = null;
    
    public void setUp() {
        cp = new ColorCurvePanel();
        frame = new JFrame(getName());        
        pane = (JPanel)frame.getContentPane();
        pane.setLayout(new FlowLayout());
        pane.setBorder(new EmptyBorder(50, 50, 50, 50));
        tester = ComponentTester.getTester( cp );
        pane.add( cp );     
    }    

    public void tearDown() {
        frame.dispose();
    }    
    
    /**
     Simple listener for checking ColorCurvePanel events
     */
    static class CurvePanelListener implements ColorCurveChangeListener {

        public void colorCurveChanging(ColorCurvePanel p, ColorCurve c) {
            dragStarted = true;
        }

        public void colorCurveChangeCompleted(ColorCurvePanel p, ColorCurve c) {
            dragCompleted = true;
        }
        public boolean dragStarted = false;
        public boolean dragCompleted = false;

    }
    
    // handy abbreviation for displaying our test frame
    private void showFrame() {
        // Always do direct component manipulation on the event thread
        tester.invokeAndWait(new Runnable() {
            public void run() { frame.pack(); frame.show(); }
        });
    }

    /**
     Test that dragging a control point works.
     */
    public void testSetCurve() {
        showFrame();
        tester.actionWaitForIdle();
        ColorCurve c = new ColorCurve();
        c.addPoint( 0.0, 0.0 );
        c.addPoint( 1.0, 1.0 );
        cp.setCurve( c );
        
        // Drag the black point
        int startX = 0;
        int startY = cp.getHeight()-1;
        int endX = cp.getWidth() / 10;
        int endY = cp.getHeight() * 8 / 10;
        tester.mousePress( cp, startX, startY );
        tester.mouseMove( cp, endX, endY );
        tester.mouseRelease();
        c = cp.getCurve();
        assertEquals( 0.1, c.getX( 0 ), 0.02 );
        assertEquals( 0.2, c.getY( 0 ), 0.02 );
        
    }


    /**
     Test dragging control point outside window.
     */
    public void testDragOutside() {
        showFrame();
        tester.actionWaitForIdle();
        ColorCurve c = new ColorCurve();
        c.addPoint( 0.0, 0.0 );
        c.addPoint( 1.0, 1.0 );
        cp.setCurve( c );
        
        // Drag the black point
        int startX = 0;
        int startY = cp.getHeight()-1;
        tester.mousePress( cp, startX, startY );
        Point endPoint = getPoint( 0.0, 0.1 );
        tester.mouseMove( pane, 0, (int)(endPoint.getY()) + 50 );
        tester.mouseRelease();
        c = cp.getCurve();
        assertEquals( 0.0, c.getX( 0 ) );
        assertEquals( 0.1, c.getY( 0 ), 0.02 );
    }
    
    /**
     Test creating control point by dragging a point in curve in middle of
     existing control points.
     */
    public void testCreateCP() {
        showFrame();
        tester.actionWaitForIdle();
        ColorCurve c = new ColorCurve();
        c.addPoint( 0.0, 0.0 );
        c.addPoint( 1.0, 1.0 );
        cp.setCurve( c );
        
        CurvePanelListener l = new CurvePanelListener();
        cp.addListener( l );
        Point start = getCurvePoint( c, 0.3 );
        tester.mousePress( cp, (int)start.getX(), (int)start.getY() );
        Point end = getPoint( 0.6, 0.4 );
        tester.mouseMove( cp, (int)end.getX(), (int)end.getY() );
        assertTrue( l.dragStarted );
        tester.mouseRelease();
        tester.actionWaitForIdle();
        assertTrue( l.dragCompleted );
        c = cp.getCurve();
        assertEquals( 3, c.getPointCount() );
        assertEquals( 0.6, c.getX( 1 ), 0.01 );
        assertEquals( 0.4, c.getY( 1 ), 0.01 );
        
    }
    
    /**
     Test dragging a control point across another point.
     */
    public void testDragCross() {
        showFrame();
        tester.actionWaitForIdle();
        ColorCurve c = new ColorCurve();
        c.addPoint( 0.0, 0.0 );
        c.addPoint( 1.0, 1.0 );
        c.addPoint( 0.5, 0.5 );
        c.addPoint( 0.6, 0.4 );
        cp.setCurve( c );
        Point start = getCurvePoint( c, 0.5 );
        tester.mousePress( cp, (int)start.getX(), (int)start.getY() );
        // Drag CP 1 so that it goes to the other side of CP 2
        Point end = getPoint( 0.65, 0.4 );
        tester.mouseMove( cp, (int)end.getX(), (int)end.getY() );
        tester.mouseRelease();
        c = cp.getCurve();
        assertEquals( 4, c.getPointCount() );
        assertEquals( 0.5, c.getX( 1 ), 0.01 );
        assertEquals( 0.5, c.getY( 1 ), 0.01 );
        
    }
    
    public void testHistogram() {
        int[] hist1 = {1,2};
        int[] hist2 = new int[1000];
        for ( int n = 0 ; n < 1000 ; n++ ) {
            hist2[n] = n;
        }
        int[] hist3 = new int[256];
        for ( int n = 0 ; n < 256 ; n++ ) {
            hist3[n] = 100;
        }
        showFrame();
        cp.setHistogram( hist1, Color.BLACK );
	tester.waitForIdle();
	BufferedImage bi = tester.capture( cp );
	// File f = new File( testRefImageDir, "thumbnailShow1.png" );
	//assertTrue( org.photovault.test.ImgTestUtils.compareImgToFile( bi, f ) );
        
        cp.setHistogram( hist2, Color.BLACK );
	tester.waitForIdle();
        bi = tester.capture( cp );
	// File f = new File( testRefImageDir, "thumbnailShow1.png" );
	// assertTrue( org.photovault.test.ImgTestUtils.compareImgToFile( bi, f ) );

        cp.setHistogram( hist3, Color.BLACK );
	tester.waitForIdle();
        bi = tester.capture( cp );
    }

    /**
     Get coordinates (in this panel's coordinate system) that matches a point in curve
     @param c The curve
     @param val value of X coordinate in curve
     @return position in which the curve point with x=val is.
     */
    public Point getCurvePoint( ColorCurve c, double val ) {
        double y = c.getValue( val );
        Point p = new Point( (int)(val * cp.getWidth()), (int)((1.0-y) * cp.getHeight()) );
        return p;
    }
    
    /**
     Map from curve coordinates to panel coordinates.
     */
    public Point getPoint( double x, double y ) {
        Point p = new Point( (int)(x * cp.getWidth()), (int)((1.0-y) * cp.getHeight()) );
        return p;
    }
    
    public static Test suite() {
	return new TestSuite( Test_ColorCurvePanel.class );
    }    
    public static void main( String[] args ) {
	junit.textui.TestRunner.run( suite() );
    }      
}

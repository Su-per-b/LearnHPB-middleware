/*
 * ColorCurvePanel.java
 *
 * Created on April 27, 2007, 6:29 PM
 */

package org.photovault.swingui.color;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JFrame;
import org.photovault.image.ColorCurve;

/**
 A simple UI control for editing {@link Color Curve}s.
 */
public class ColorCurvePanel extends javax.swing.JPanel {
    
    /** Creates new form ColorCurvePanel */
    public ColorCurvePanel() {
        initComponents();
        curve.addPoint( 0.0, 0.0 );
        curve.addPoint( 0.5, 0.5 );
        curve.addPoint( 1.0, 1.0 );
        initPoints();
    }
    
    /**
     Active ColorCurvePanelListeners
     */
    Set listeners = new HashSet();
    
    /**
     Add a new listener that will be notified about changes
     @param l The listener to add
     */
    public void addListener( ColorCurveChangeListener l ) {
        listeners.add( l );
    }


    /**
     Remove a new listener 
     @param l The listener to remove
     */
    public void removeListener( ColorCurveChangeListener l ) {
        listeners.remove( l );
    }
    
    /**
     Inform all listeners that the color curve is changing
     */
    protected void fireCurveChangingEvent() {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            ColorCurveChangeListener l = (ColorCurveChangeListener) iter.next();
            l.colorCurveChanging( this, curve );
        }
    }
    
    /**
     Inform all listeners that the color curve change is complete
     */
    protected void fireCurveChangeCompleteEvent() {
        Iterator iter = listeners.iterator();
        while ( iter.hasNext() ) {
            ColorCurveChangeListener l = (ColorCurveChangeListener) iter.next();
            l.colorCurveChangeCompleted( this, curve );
        }
    }
    
    /**
     Get the curve that is currently being edited
     @return The color curve
     */
    public ColorCurve getCurve() {
        return curve;
    }

    /**
     Set the curve
     @param curve The curve that will be displayed and edited, bu the component.
     @param color Color of the curve
     */
    public void setCurve( ColorCurve curve, Color color ) {
        this.curve = curve;
        this.curveColor = color;
        initPoints();
        repaint();
    }

    
    /**
     Set the curve
     @param c The curve that will be displayed and edited, bu the component.
     */
    public void setCurve( ColorCurve c ) {
        setCurve( c, Color.BLACK );
    }
    
    /**
     Add a new reference curve that will be drawn on background
     @param curve The curve to draw
     @param color Color of the curve
     */
    public void addReferenceCurve( ColorCurve curve, Color color ) {
        refCurves.add( new CurveDescriptor( curve, color ) );
        repaint();
    }
    
    /**
     Clear all currently displayed reference curves
     */
    public void clearReferenceCurves() {
        refCurves.clear();
        repaint();
    }
    
    int [] histogram;
    
    public void setHistogram( int[] histData, Color color ) {
        histogram = histData;
        createHistogramPolygon();
        repaint();
    }
    
    /*
     Create a polygon that describes the current histogram data.
     */
    private void createHistogramPolygon() {
        if ( histogram != null ) {
            int width = getWidth();
            double dx = (double)width / (double)histogram.length;
            // x positions of left edges of histogram bars
            int[] xpos = new int[width];
            double[] ypos = new double[width];
            
            int bin = 0;
            int x = 0;
            double ymax = 0;
            int sample = 0;
            while ( sample < histogram.length ) {
                // Check if next histogram bin is so close that is goes to the 
                // same pixel column
                if ( ((sample+1)*dx) > (double)(x+1) ) {
                    // This sample crosses the boundary between current & next bin. 
                    // Split it between these 2
                    x = (int) ((sample+1)*dx);
                    double overflowRatio = ((sample+1)*dx - (double)x)/dx;
                    ypos[bin] += histogram[sample] * ( 1.0 - overflowRatio );
                    ypos [bin+1] = histogram[sample] * overflowRatio;
                    xpos[bin+1] = x;
                    ypos[bin] /= (double)(xpos[bin+1]-xpos[bin]);
                    ymax = Math.max( ymax, ypos[bin] );
                    bin++;
                } else {
                    // This sample fits into current bin
                    ypos[bin] += histogram[sample];
                }
                sample++;
            }
            
            // Now create the histogram polygon based on this data
            int histogramHeight = getHeight() / 2;
            int panelHeight = getHeight();
            Polygon p = new Polygon();
            int py = panelHeight;
            // p.addPoint( 0, h );
            for ( int n = 0 ; n <= bin ; n++ ) {
                p.addPoint( xpos[n], py );
                py = panelHeight - (int)(ypos[n]*histogramHeight/ymax);
                p.addPoint( xpos[n], py );
            }
            p.addPoint( width-1, py );
            p.addPoint( width-1, panelHeight );
            histogramShape = p;
        } else {
            histogramShape = null;
        }
    }
    
    Shape histogramShape = null;
    Color histogramColor = new Color( 0.0f, 0.0f, 0.0f, 0.5f );
        
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        initPoints();
    }//GEN-LAST:event_formComponentResized

    /**
     Move selected control point according to current mouse movement & repaint 
     curve.
     */
    private void movePointWithMouse( int x, int y ) {
            x = Math.max( 0, Math.min( x, getWidth()-1 ) );
            y = Math.max( 0, Math.min( y, getHeight()-1 ) );
            int dx = x - mouseStartX;
            int dy = y - mouseStartY;
            if ( (movingPoint == pointX.length-1 || pointStartX + dx < pointX[movingPoint+1] )
            && ( movingPoint == 0 || pointStartX + dx > pointX[movingPoint-1] ) ) {
                // The point is between its siblings, set new coordinates
                pointX[movingPoint] = pointStartX + dx;
                pointY[movingPoint] = pointStartY + dy;
                int w = getWidth();
                int h = getHeight();
                double xx = ((double) pointX[movingPoint]) / (double)w;
                double yy = ((double) (h-pointY[movingPoint])) / (double)h;
                curve.setPoint( movingPoint, xx, yy );
                repaint();
            }
        
    }
    
    /**
     Mouse is dragged on top of this control, move a control point of one is selected.
     */
    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if ( movingPoint >= 0 ) {
            movePointWithMouse( evt.getX(), evt.getY() );
            fireCurveChangingEvent();
        }        
    }//GEN-LAST:event_formMouseDragged

    /**
     Mouse is released, move control point to its final location and notify 
     listeners.
     */
    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        if ( movingPoint >= 0 ) {
            movePointWithMouse( evt.getX(), evt.getY() );
            fireCurveChangeCompleteEvent();
        }
        movingPoint = -1;
    }//GEN-LAST:event_formMouseReleased

    /**
     Curve that is currently edited.
     */
    ColorCurve curve = new ColorCurve();
    
    /**
     Color of the curve
     */
    Color curveColor = Color.BLACK;
    
    /**
     Strucutre that describes a reference curve and its color
     */
    static class CurveDescriptor {
        public CurveDescriptor( ColorCurve curve, Color color ) {
            this.curve = curve;
            this.color = color;
        }
        
        ColorCurve curve;
        Color color;
    }
    
    /**
     Curves drawn for reference
     */
    ArrayList<CurveDescriptor> refCurves = new ArrayList<CurveDescriptor>();

    /**
     Paint grid for the control
     @param g java Graphics object to use.
     */
    private void paintGrid( Graphics2D g ) {
        // Paint grid
        Graphics2D g2 = (Graphics2D) g.create();
        Stroke s = new BasicStroke( 1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] {10.0f, 5.0f}, 0.0f );
        g2.setStroke( s );
        g2.setColor( Color.LIGHT_GRAY );
        
        int dx = getWidth() / 4;
        for ( int x = dx; x < getWidth()-4; x+= dx ) {
            g2.drawLine( x, 0, x, getHeight() );
        }

        int dy = getHeight() / 4;
        for ( int y = dy; y < getHeight()-4; y+= dy ) {
            g2.drawLine( 0, y, getWidth(), y );
        }
        
    }
    
    /**
     Paints a given curve
     @param c The curve to paint
     @param g Graphics2D object used to paint the curve
     */
    private void paintCurve( ColorCurve c, Graphics2D g ) {
        // Paint curve segments
        
        for ( int n = 0 ; n < c.getPointCount()-1 ; n++ ) {
            CubicCurve2D segment = c.getSegment( n );
            double dw = getWidth();
            double dh = getHeight();
            CubicCurve2D xlated = new CubicCurve2D.Double(
                    segment.getX1()*dw, (1.0-segment.getY1())*dh,
                    segment.getCtrlX1() * dw, (1.0-segment.getCtrlY1())*dh,
                    segment.getCtrlX2()*dw, (1.0-segment.getCtrlY2())*dh,
                    segment.getX2()*dw, (1.0-segment.getY2())*dh );
            g.draw( xlated );
        }
    }
    
    /**
     Paint the control
     @param g Java Graphics object
     */
    public void paint( Graphics g ) {
        super.paint( g );
        Graphics2D g2 = (Graphics2D) g.create();
        
        paintGrid( g2 );

        if ( histogramShape != null ) {
            Graphics2D histGraphics = (Graphics2D) g2.create();
            histGraphics.setColor( histogramColor );
            histGraphics.setPaint( histogramColor );
            histGraphics.fill( histogramShape );
        }
        
        if ( refCurves.size() > 0 ) {
            Graphics2D refCurveGraphics = (Graphics2D) g2.create();
            refCurveGraphics.setColor( Color.GRAY );
            refCurveGraphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON );
            for ( CurveDescriptor c : refCurves ) {
                if ( c.curve != null ) {
                    Color oldColor = refCurveGraphics.getColor();
                    refCurveGraphics.setColor( c.color );
                    paintCurve( c.curve, refCurveGraphics );
                    refCurveGraphics.setColor( oldColor );
                }
            }
        }
        
        Stroke curveStroke = new BasicStroke( 2.0f );
        g2.setStroke( curveStroke );
        Color oldColor = g2.getColor();
        g2.setColor( curveColor );
        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );
        // Paint the curve
        paintCurve( curve, g2 );
        g2.setColor( oldColor );
        
        // Draw points
        Stroke handleStroke = new BasicStroke( 1.0f );
        g2.setStroke( handleStroke );
        g2.setColor( Color.BLACK );
        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF );
        for ( int i = 0; i < pointX.length ; i++ ) {
            g2.drawRect( pointX[i]-3, pointY[i]-3, 6, 6 );
        }
        
    }
    
    /**
     X coordinate for mouse when drag started
     */
    int mouseStartX;
    /**
     Y coordinate for mouse when drag started
     */
    int mouseStartY;
    /**
     X coordinate for the currently moving point before move started
     */
    int pointStartX;
    /**
     Y coordinate for the currently moving point before move started
     */
    int pointStartY;
    /**
     Number of the control point that is muved currently of -1 if no move ongoing.
     */
    int movingPoint = -1;
    
    /**
     X coordinates (in control's coordinate system of the control points.
     */
    int[] pointX = {};
    /**
     Y coordinates (in control's coordinate system of the control points.
     */
    int[] pointY = {};

    /**
     Number of pixels that mouse press position can differ from control point 
     (hamming distance) so that it is still considered a hit.
     */
    final static int MOUSE_THRESHOLD = 5;
    
    /**
     Get control point in given location
     @param x X coordinate of the location
     @param y Y coordinate of the location
     @return Number of the control point given location or negative is no hit.
     */
    private int getPoint( int x, int y ) {
        int point = -1;
        for ( int n = 0; n < pointX.length ; n++ ) {
            if ( Math.abs( x-pointX[n] ) < MOUSE_THRESHOLD 
                    && Math.abs( y-pointY[n] ) < MOUSE_THRESHOLD ) {
                point = n;
            }
        }
        return point;
    }
    
    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        int point = getPoint( evt.getX(), evt.getY() );
        if ( point >= 0 ) {
            movingPoint = point;
            mouseStartX = evt.getX();
            mouseStartY = evt.getY();            
            pointStartX = pointX[point];
            pointStartY = pointY[point];
        }  else {
            // No control point in this location. Check if mouse is on top
            // of curve & create new control point if it is.
            double x = ((double) evt.getX())/getWidth();
            double y = curve.getValue( x );
            if ( Math.abs( (1.0-y) * getHeight() - evt.getY() ) < 3 ) {
                curve.addPoint( x, y );
                initPoints();
                // Rehandle the event after curve has been updated
                formMousePressed( evt );
            }
        }
    }//GEN-LAST:event_formMousePressed
    
    /**
     (Re)initialize control points based on curve.
     */
    private void initPoints() {
        int c = curve.getPointCount();
        pointX = new int[c];
        pointY = new int[c];
        int w = getWidth();
        int h = getHeight();
        for ( int n = 0; n < c ; n++ ) {
            pointX[n] = (int)(w * curve.getX( n ));
            pointY[n] = h - (int)(h * curve.getY( n ));
        }
    }    
    
    /**
     Test method for control.
     */
    public static void main( String args[] ) {
        ColorCurvePanel p = new ColorCurvePanel();
        
        p.addListener( new ColorCurveChangeListener() {
            public void colorCurveChanging(ColorCurvePanel p, ColorCurve c) {
                System.out.println( "curve changing" ); 
            }

            public void colorCurveChangeCompleted(ColorCurvePanel p, ColorCurve c) {
                System.out.println( "Change complete" );
            }
            
        });
        
        JFrame f = new JFrame();
        f.add( p );
        f.addWindowListener( new WindowListener() {
            public void windowOpened(WindowEvent windowEvent) {
            }

            public void windowClosing(WindowEvent windowEvent) {
                System.exit( 0 );
            }

            public void windowClosed(WindowEvent windowEvent) {
            }

            public void windowIconified(WindowEvent windowEvent) {
            }

            public void windowDeiconified(WindowEvent windowEvent) {
            }

            public void windowActivated(WindowEvent windowEvent) {
            }

            public void windowDeactivated(WindowEvent windowEvent) {
            }
            
        });
        f.setVisible( true );
        
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}

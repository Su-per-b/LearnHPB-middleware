/*
Copyright (c) 2009 Harri Kaimio

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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Timer;
import org.jdesktop.jxlayer.plaf.AbstractLayerUI;
import org.jdesktop.jxlayer.JXLayer;

/**
 * Decorator to {@link PhotoCollectionThumbView} that shows the progress of
 * indexing the currently shown external directory.
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class ProgressIndicatorLayer extends AbstractLayerUI<JScrollPane>
        implements ActionListener {

    /**
     * Is the indicator showing that activity is in progress
     */
    private boolean inProgress = false;


    /**
     * States for the indicator visibility
     */
    enum IndicatorState {

        /**
         * The indicator is not visible
         */
        INVISIBLE,

        /**
         * Fading in
         */
        FADE_IN,

        /**
         * Visible and fully faded in
         */
        VISIBLE,

        /**
         * Fading out
         */
        FADE_OUT
    }

    /**
     * How many percent of the task is done (0-100)
     */
    int progressPercent = 0;

    /**
     * Current visibility state
     */
    IndicatorState state = IndicatorState.INVISIBLE;

    /**
     * Area where the progreaa bar is drawn
     */
    Rectangle progressBarArea = new Rectangle( 40, 15, 100, 10 );


    /**
     * Default constructor
     */
    public ProgressIndicatorLayer() {
        initGraphics();
        timer = new Timer( 100, this );
        timer.setRepeats( true );
        timer.start();
    }

    /**
     * Positions of the balls in the progress indicator
     */
    private Point[] ballPos;

    /**
     * At which position the first ball is in
     */
    int firstBall = 0;

    /**
     * Time when the currently ongoing fade was started (as reported by
     * {@link System#currentTimeMillis() }) if the {@link #state} is either
     * {@link IndicatorState#FADE_IN} or {@link IndicatorState#FADE_IN}.
     */
    long fadeStarted = 0;

    /**
     * Timer used to create events for this animation
     */
    private Timer timer;

    /**
     * Initialize the geometry of graphics
     */
    private void initGraphics() {
        ballPos = new Point[12];

        for ( int n = 0; n < ballPos.length; n++ ) {
            double a = ((double) n) * Math.PI / 6.0;
            double x = Math.sin( -a ) * 14;
            double y = Math.cos( a ) * 14;
            ballPos[n] = new Point( (int) x + 15, (int) y + 15 );
        }

    }

    /**
     * Paint the progress indicator
     * @param g2
     * @param l
     */
    /*
     * TODO: For some season I cannot paramerize l to JXLayer<JScrollPane>
     */
    @Override
    protected void paintLayer( Graphics2D g2, JXLayer l ) {
        super.paintLayer( g2, l );

        long currentTime = System.currentTimeMillis();
        int gradientY = 0;
        int ringAlpha = 255;
        if ( state == IndicatorState.FADE_IN ) {
            long fadeTime = currentTime - fadeStarted;
            if ( fadeTime > 1000 ) {
                state = IndicatorState.VISIBLE;
            } else {
                gradientY = (int) ((fadeTime / 25) - 40);
                ringAlpha = (int) (fadeTime / 4);
            }
        } else if ( state == IndicatorState.FADE_OUT ) {
            long fadeTime = currentTime - fadeStarted;
            if ( fadeTime > 1000 ) {
                state = IndicatorState.INVISIBLE;
            } else {
                gradientY = (int) (- fadeTime / 25);
                ringAlpha = (int) (255-fadeTime / 4);
            }

        }

        if ( state == IndicatorState.INVISIBLE ) {
            return;
        }
        JViewport vp = ((JScrollPane)l.getView()).getViewport();
        int w = l.getWidth();
        GradientPaint backgroundPaint = new GradientPaint(
                new Point( 0, gradientY + vp.getY() ), new Color( 201, 221, 242, 128 ),
                new Point( 0, gradientY + vp.getY()+40 ), new Color( 201, 221, 242, 0 ) );
        Graphics2D backgroundGraphics = (Graphics2D) g2.create();
        backgroundGraphics.setPaint( backgroundPaint );
        backgroundGraphics.fillRect( vp.getX(), gradientY + vp.getY(), vp.getWidth(), 40 );


        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );
        for ( int n = 0; n < ballPos.length; n++ ) {
            int ballNum = firstBall - n;
            if ( ballNum < 0 ) {
                ballNum += ballPos.length;
            }
            int brightness = 150 + n * 10;
            if ( brightness > 220 ) {
                brightness = 220;
            }
            g2.setColor( new Color( brightness, brightness, brightness, ringAlpha ) );
            Point p = ballPos[ballNum];
            g2.fillOval( p.x, p.y, 6, 6 );
        }
        
        if ( inProgress ) {
            firstBall++;
            if ( firstBall >= ballPos.length ) {
                firstBall = 0;
            }
        }
        // Draw the progress bar
        Color progressBarColor = new Color( 201, 221, 242, ringAlpha );
        Color progressBarBorderColor = new Color( 255, 255, 255, ringAlpha );
        g2.setColor( progressBarColor );
        g2.fillRect( progressBarArea.x, progressBarArea.y,
                progressBarArea.width, progressBarArea.height );
        g2.setColor( progressBarBorderColor );
        g2.fillRect( progressBarArea.x + 1, progressBarArea.y + 1,
                progressBarArea.width - 2, progressBarArea.height - 2 );
        g2.setColor( progressBarColor );
        g2.fillRect( progressBarArea.x + 2, progressBarArea.y + 2,
                (progressBarArea.width - 4) * progressPercent / 100,
                progressBarArea.height - 4 );


    }

    /**
     * Set whether the indicator show progress (i.e. is animated & visible)
     * @param inProgress
     */
    public void setInProgress( boolean inProgress ) {
        if ( inProgress && !this.inProgress ) {
            state = IndicatorState.FADE_IN;
            fadeStarted = System.currentTimeMillis();
            timer.start();
        } else if ( !inProgress && this.inProgress ) {
            state = IndicatorState.FADE_OUT;
            fadeStarted = System.currentTimeMillis();
        }
        this.inProgress = inProgress;
    }

    /**
     * Set how many percents of the work is done
     * @param percentComplete
     */
    void setPercentComplete( int percentComplete ) {
        progressPercent = percentComplete;
    }

    
    public void actionPerformed( ActionEvent e ) {
        setDirty( true );
    }
}

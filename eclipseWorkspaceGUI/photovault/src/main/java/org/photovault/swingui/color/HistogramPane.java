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

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
   Component for showing a simple histogram + superimposed graph.
 
 */
public class HistogramPane extends JPanel {
    
    /** Creates a new instance of HistogramPane */
    public HistogramPane() {
        super();
        // setBackground( Color.WHITE );
    }
    
    /**
     The raw distogram data
     */
    int [][] histBins = null;
    /**
     Displayable histogram calculated from histBins by scaling the width
     of the histogram to match component width.
     */
    int histogram[] = null;
    
    /**
     Superimposed graph, scaled to match component width
     */
    int graph[] = null;
    
    /**
     Raw data for the graph
     */
    byte graphData[] = null;
    
    /**
     Size of the largest bin (used to calculate histogram scaling in vertical 
     dimension
     */
    int maxBinSize = 0;
    
    /**
     Set the histogram data
     @param histogramBins size of the bins. Outer index determines the band,
     inner index the order number of the bin.
     */
    public void setHistogram( int[][] histogramBins ) {
        histBins = histogramBins;
        histogram = null;
        repaint();
    }
    
    /**
     Set the raw data for graph
     @param lut The data
     */
    public void setTransferGraph( byte[] lut ) {
        graph = null;
        graphData = lut;
        repaint();
    }
    
    /**
     Create a histogram that fits to the window
     */
    void createHistogram() {
        int histWidth = getWidth() - 20;
        histogram = new int[histWidth];
        maxBinSize = 0;
        int bin = 0;
        for ( int n = 0; n < histWidth; n++ ) {
            int e = histBins[0].length * n / histWidth;
            for ( ; bin < e; bin++ ) {
                for ( int band = 0; band < histBins.length ; band++ ) {
                    histogram[n] += histBins[band][bin];
                }
                
            }
            if ( histogram[n] > maxBinSize ) {
                maxBinSize = histogram[n];
            }        
        }
    }
    
    /**
     Create the graph based on raw data
     */
    void createGraph() {
        int histWidth = getWidth() - 20;
        graph = new int[histWidth];
        for ( int n = 0; n < histWidth; n++ ) {
            int e = graphData.length * n / histWidth;
            graph[n] = graphData[e];
            if ( graph[n] < 0 ) graph[n] += 0x100;
        }        
    }
    
    /**
     Paint the histogram & graph
     */
    public void paintComponent( Graphics g ) {
        super.paintComponent( g );
        int w = getWidth();
        int h = getHeight();
        Graphics g2 = g.create();
        g2.setColor( Color.BLACK );
        // X axis
        g2.drawLine( 9, h-10, w-10, h-10 );
        // Y axis
        g2.drawLine( 9, 10, 9, h-10 );
        if (  histBins != null ) {
            if ( histogram == null || histogram.length != w-20 ) {
                createHistogram();
            }
            int scale = maxBinSize / (h-21);
            g2.setColor( Color.GRAY );
            for ( int n = 0; n < histogram.length; n++ ) {
                int y = histogram[n]/scale;
                g2.drawLine( 10+n, h-11-y, 10+n, h-11 );
            }
            
        }
        if ( graphData != null ) {
            if ( graph == null || graph.length != w-20 ) {
                this.createGraph();
            }
            int lastY = graph[0] * (h-20) / 256;
            g2.setColor( Color.BLACK );
            for ( int n = 1; n < graph.length; n++ ) {
                int y = graph[n]* (h-20) / 256;
                g2.drawLine( 9+n, h-10-lastY, 10+n, h-10-y );
                lastY = y;
            }
        }
    }
}

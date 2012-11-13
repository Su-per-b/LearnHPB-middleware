/*
  Copyright (c) 2010 Harri Kaimio

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

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import javax.media.jai.PlanarImage;
import javax.media.jai.TileCache;
import javax.media.jai.TileComputationListener;
import javax.media.jai.TileRequest;
import javax.swing.JComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class represents a single tile fo the currently displayed image
 * @author Harri Kaimio
 * @since 0.6.0
 */
public class ImageTile implements TileComputationListener {
    static private Log log = LogFactory.getLog( ImageTile.class );

    PlanarImage img;
    boolean isQueued;
    int tileX;
    int tileY;
    JComponent comp;
    TileRequest tileReq;
    int tileWidth;
    int tileHeight;
    Rectangle bounds;

    public ImageTile( JComponent comp, PlanarImage img, int tileX, int tileY ) {
        this.img = img;
        this.comp = comp;
        this.tileX = tileX;
        this.tileY = tileY;
        tileWidth = img.getTileWidth();
        tileHeight = img.getTileHeight();
        bounds = new Rectangle( tileX*tileWidth, tileY*tileHeight, tileWidth, tileHeight );
    }


    /**
     * Draws the tile if it is found in cache. Otherwise, schedule a request
     * for computing it.
     * @param cache
     * @param g
     */
    public void drawTile( TileCache cache, Graphics2D g ) {
        Raster r = cache.getTile( img, tileX, tileY );
        if ( r == null ) {
            if ( tileReq == null ) {
                Point[] tileCoord = new Point[1];
                tileCoord[0] = new Point( tileX, tileY );
                tileReq = img.queueTiles( tileCoord );
            }
        } else {
            DataBuffer buf = r.getDataBuffer();
            SampleModel sm = r.getSampleModel();
            WritableRaster wr = Raster.createWritableRaster( sm, buf, new Point(
                    0, 0 ) );
            ColorModel cm = img.getColorModel();
            BufferedImage bufImg = new BufferedImage( cm, wr, false, null );
            g.drawImage( bufImg, null, tileX*tileWidth, tileY*tileHeight );
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void tileComputed( Object o, TileRequest[] trs, PlanarImage pi, int i,
            int i1, Raster raster ) {
        tileReq = null;
        log.debug( "tileComputed " + tileX + ", " + tileY );
    }

    public void tileCancelled( Object o, TileRequest[] trs, PlanarImage pi,
            int i, int i1 ) {
        tileReq = null;
        log.debug( "tileCancelled " + tileX + ", " + tileY );
    }

    public void tileComputationFailure( Object o, TileRequest[] trs,
            PlanarImage pi, int i, int i1, Throwable thrwbl ) {
        tileReq = null;
        log.debug( "tileComputationFailure " + tileX + ", " + tileY + ": " + thrwbl );
    }
}

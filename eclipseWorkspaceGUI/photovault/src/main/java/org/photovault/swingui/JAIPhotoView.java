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

package org.photovault.swingui;

import com.sun.media.jai.util.SunTileCache;
import com.sun.media.jai.util.SunTileScheduler;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileRequest;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.*;
import java.awt.font.LineMetrics;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.TileCache;
import javax.media.jai.TileComputationListener;
import javax.media.jai.widget.ScrollingImagePanel;
import org.photovault.dcraw.RawConversionSettings;
import org.photovault.dcraw.RawImage;
import org.photovault.image.ChannelMapOperation;
import org.photovault.image.ChannelMapOperationFactory;
import org.photovault.image.ColorCurve;
import org.photovault.image.PhotovaultImage;


/**
   PhotoView the actual viewing component for images stored in the database.
*/
public class JAIPhotoView extends JPanel 
        implements MouseListener, 
        MouseMotionListener, 
        CropParamEditorListener {
    
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( JAIPhotoView.class.getName() );

    ScrollingImagePanel canvas;
    CropParamEditor paramEditor = null;
    
    public JAIPhotoView() {
	super();
	imgRot = 0;
        newRotDegrees = 0;
        newCrop = crop = new Rectangle2D.Float( 0.0F, 0.0F, 1.0F, 1.0F );
        addMouseListener( this );
        addMouseMotionListener( this );
        // Capture keyboard shortcuts
        final JAIPhotoView staticThis = this;
        Action applyCropAction = new AbstractAction() {
            public void actionPerformed( ActionEvent evt ) {
                if ( !staticThis.getDrawCropped() ) {
                    staticThis.applyCrop();
                }
            }
        };
        getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( 
                KeyStroke.getKeyStroke( "ENTER"), 
                "applyCrop" );
        getActionMap().put( "applyCrop", applyCropAction );
        
        Action cancelCropAction = new AbstractAction() {
            public void actionPerformed( ActionEvent evt ) {
                if ( !staticThis.getDrawCropped() ) {
                    staticThis.cancelCrop();
                }
            }
        };
        getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( 
                KeyStroke.getKeyStroke( "ESCAPE"), 
                "cancelCrop" );
        getActionMap().put( "cancelCrop", cancelCropAction );

        
        paramEditor = new CropParamEditor( null, false );
        updateCropParamEditor();
        paramEditor.addListener( this );

        setBackground( new Color( 0.2f, 0.2f, 0.2f ) );
    }

    static class ImageTileComputationListener implements TileComputationListener {

        private final JAIPhotoView view;

        public ImageTileComputationListener( JAIPhotoView view ) {
            this.view = view;
        }



        public void tileComputed( Object o, TileRequest[] trs, PlanarImage pi,
                int tilex, int tiley, Raster raster ) {
            int x = view.imgX + pi.getTileWidth() * tilex;
            int y = view.imgY + pi.getTileWidth() * tiley;
            view.repaint( x, y, pi.getTileWidth(), pi.getTileHeight() );
        }

        public void tileCancelled( Object o, TileRequest[] trs, PlanarImage pi,
                int x, int y ) {
            log.debug( "tileCancelled("+ x + ", " + y + ")" );
        }

        public void tileComputationFailure( Object o, TileRequest[] trs,
                PlanarImage pi, int x, int y, Throwable thrwbl ) {
            log.debug( "tileComputationFailure ("+ x + ", " + y + "): " + thrwbl );
        }

    }
    
    public void paint( Graphics g ) {
        super.paint( g );
	if ( xformImage == null && origImage != null ) {
            buildXformImage();
        }
	
 	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 	int compWidth = getWidth();
 	int compHeight = getHeight();
 	if ( xformImage != null ) {
 	    // Determine the place for the image. If the image is smaller that window, center it
	    imgWidth = xformImage.getWidth();
	    imgHeight = xformImage.getHeight();
	    imgX = (compWidth-imgWidth)/2;
	    imgY = (compHeight-imgHeight)/2;
            g2.translate( imgX, imgY );
            Shape oldClip = g2.getClip();
            g2.setClip( 0, 0, xformImage.getWidth(), xformImage.getHeight() );
            int xTileCount = xformImage.getNumXTiles();
            int yTileCount = xformImage.getNumYTiles();
            TileCache cache = JAI.getDefaultInstance().getTileCache();
            for ( int ytile = 0 ; ytile < yTileCount ; ytile++ ) {
                for ( int xtile = 0; xtile < xTileCount ; xtile++ ) {
                    ImageTile tile = xformImageTiles[xtile][ytile];
                    Rectangle tileBounds = tile.getBounds();
                    if ( g2.hitClip( (int) tileBounds.getMinX(),
                            (int) tileBounds.getMinY(),
                            (int) tileBounds.getWidth(),
                            (int) tileBounds.getHeight() ) ) {
                        xformImageTiles[xtile][ytile].drawTile( cache, g2 );
                    }
                }
            }
            g2.setClip( oldClip );
//            g2.drawRenderedImage( xformImage, new AffineTransform(1f,0f,0f,1f, imgX, imgY) );
            g2.translate( -imgX, -imgY );
            if ( !drawCropped ) {
                paintCropBorder( g2, imgX, imgY, imgWidth, imgHeight );
            } else if ( fitSize ) {
                paintImageBorder( g2, imgX, imgY, imgWidth, imgHeight );
            }
            log.debug( "JAI cache info: " + getJAICacheDebugInfo() );
            if ( showInfo ) {
                paintInfo( g2 );
            }
	}
    }

    /**
     * Duration how long the color info box is shown, in milliseconds
     */
    private int colorInfoDurationMsec = 4000;
    /**
     * Duration of fade out of color info box, in milliseconds
     */
    private int colorInfoFadeMsec = 500;

    /**
     * Draw a info box about the color of a pixel
     * @param g Graphics context
     */
    private void paintInfo( Graphics2D g ) {
        long timeElapsed = System.currentTimeMillis() -infoStartTime;
        if ( timeElapsed > colorInfoDurationMsec ) {
            showInfo = false;
        } else {
            float alpha = 
                    (timeElapsed < colorInfoDurationMsec-colorInfoFadeMsec ) ?
                        1.0f :
                        (colorInfoDurationMsec-timeElapsed)/(float)(colorInfoFadeMsec);
            Rectangle2D rgbLineRect = g.getFont().getStringBounds( infoTextRGB,
                    g.getFontRenderContext() );
            Rectangle2D labLineRect = g.getFont().getStringBounds( infoTextLab,
                    g.getFontRenderContext() );
            Color background = new Color( 0.3f, 0.3f, 0.3f, 0.5f*alpha );
            g.setColor( background );

            float rectWidth = ((int) Math.max( rgbLineRect.getWidth(), labLineRect.getWidth() ) ) + 20;
            g.fillRoundRect( infoX+imgX+2, infoY+imgY+2, (int) rectWidth, 35, 8, 8 );
            g.setColor( new Color( 1.0f, 1.0f, 1.0f, alpha ) );
            Stroke s = new BasicStroke( 2 );
            g.setStroke( s );
            g.drawRoundRect( infoX+imgX, infoY+imgY, (int) rectWidth, 35, 8, 8 );

            g.drawString( infoTextRGB, infoX+imgX+10, infoY+imgY+14 );
            g.drawString( infoTextLab, infoX+imgX+10, infoY+imgY+29 );
            repaint( infoX + imgX, infoY + imgY,(int) (rectWidth + 10), 45 );
        }

    }

    public String getJAICacheDebugInfo() {
        StringBuffer buf = new StringBuffer();
        SunTileCache tileCache = (SunTileCache) JAI.getDefaultInstance().getTileCache();
        buf.append( "cmiss=" + tileCache.getCacheMissCount() + ", chits=" + tileCache.getCacheHitCount() + 
                ", ctiles=" + tileCache.getCacheTileCount() + "cache used=" + tileCache.getCacheMemoryUsed() );
        SunTileScheduler sched;
        return buf.toString();
    }
        
    /** Image top left X coordinate in the component */
    int imgX;
    /** Image top left Y coordinate in the component */
    int imgY;
    /** Image width in the component */
    int imgWidth;
    /** Image height in the component */
    int imgHeight;

    /**
     * Margin to be left above fitted image
     */
    int fitMarginTop = 20;
    int fitMarginBottom = 20;
    int fitMarginLeft = 20;
    int fitMarginRight = 20;

    int cropBorderXpoints[] = null;
    int cropBorderYpoints[] = null;
    int cropHandlesX[] = new int[9];
    int cropHandlesY[] = new int[9];
    /**
     Cursors used when mouse is hovering on top of the crop handles
     */
    int cropHandleCursors[] = {
        Cursor.NW_RESIZE_CURSOR,
        Cursor.NE_RESIZE_CURSOR,
        Cursor.SE_RESIZE_CURSOR,
        Cursor.SW_RESIZE_CURSOR,
        Cursor.N_RESIZE_CURSOR,
        Cursor.E_RESIZE_CURSOR,
        Cursor.S_RESIZE_CURSOR,
        Cursor.W_RESIZE_CURSOR,
        Cursor.MOVE_CURSOR
    };
    
    final static int HANDLE_TOP_LEFT = 0;
    final static int HANDLE_TOP_RIGHT = 1;
    final static int HANDLE_BOT_RIGHT = 2;
    final static int HANDLE_BOT_LEFT = 3;
    final static int HANDLE_TOP = 4;
    final static int HANDLE_RIGHT = 5;
    final static int HANDLE_BOT = 6;
    final static int HANDLE_LEFT = 7;
    final static int HANDLE_CENTER = 8;
    
    /** X coordinate of the crop border coordinate system origo in image coordinates*/
    double cropBorderX = 0;
    /** Y coordinate of the crop border coordinate system origo in image coordinates */
    double cropBorderY = 0;
    /** Local Y coordinate of the top line in crop border coordinates */
    double cropBorderTop = 0;
    /** Local Y coordinate of the top line in crop border coordinates */
    double cropBorderBottom = 0;
    /** Local X coordinate of the left border */
    double cropBorderLeft = 0;
    /** Local X coordinate of the right border */
    double cropBorderRight = 0;
    /** Rotation of the crop border (compared to ImageView trasfcormation)
     in radians, clockwise */
    double cropBorderRot = 0;
    /** X coordinates of the rotation handle, first the stating point of line, 
     then the actual handle */
    int rotHandleX[] = new int[2];
    /** Y coordinates of the rotation handle */
    int rotHandleY[] = new int[2];
    
    private final static int ROT_HANDLE_LENGTH = 20;
  
    private HashSet cropAreaListeners = new HashSet();
    
    public void addCropAreaChangeListener( CropAreaChangeListener l ) {
        cropAreaListeners.add( l );
    }

    
    public void removeCropAreaChangeListener( CropAreaChangeListener l ) {
        cropAreaListeners.remove( l );
    }
    
    protected void fireCropAreaChangeEvent( CropAreaChangeEvent e ) {
        Iterator i = cropAreaListeners.iterator();
        while ( i.hasNext() ) {
            CropAreaChangeListener l = (CropAreaChangeListener) i.next();
            l.cropAreaChanged( e );
        }
    }
    
    private HashSet photoViewListeners = new HashSet();
    
    public void addPhotoViewListener( PhotoViewListener l ) {
        photoViewListeners.add( l );
    }

    
    public void removePhotoViewListener( PhotoViewListener l ) {
        photoViewListeners.remove( l );
    }
    
    protected void fireImageChangedEvent( PhotoViewEvent e ) {
        Iterator i = photoViewListeners.iterator();
        while ( i.hasNext() ) {
            PhotoViewListener l = (PhotoViewListener) i.next();
            l.imageChanged( e );
        }
    }
    
    
    
    private void calcCropBorderCoords() {
        double sinRot = Math.sin( cropBorderRot );
        double cosRot = Math.cos( cropBorderRot );
        cropBorderXpoints[0] = imgX + (int)(cropBorderX - cropBorderLeft  * cosRot + cropBorderTop    * sinRot);
        cropBorderYpoints[0] = imgY + (int)(cropBorderY - cropBorderLeft  * sinRot - cropBorderTop    * cosRot);
        cropBorderXpoints[1] = imgX + (int)(cropBorderX + cropBorderRight * cosRot + cropBorderTop    * sinRot );
        cropBorderYpoints[1] = imgY + (int)(cropBorderY + cropBorderRight * sinRot - cropBorderTop    * cosRot);
        cropBorderXpoints[2] = imgX + (int)(cropBorderX + cropBorderRight * cosRot - cropBorderBottom * sinRot );
        cropBorderYpoints[2] = imgY + (int)(cropBorderY + cropBorderRight * sinRot + cropBorderBottom * cosRot);
        cropBorderXpoints[3] = imgX + (int)(cropBorderX - cropBorderLeft  * cosRot - cropBorderBottom * sinRot );
        cropBorderYpoints[3] = imgY + (int)(cropBorderY - cropBorderLeft  * sinRot + cropBorderBottom * cosRot);
        
        // Calculate the handle coordinates
        // The first 4 handles are the corners so just copy the previously calculated
        // coordinates
        int  centerX = 0;
        int centerY = 0;
        for ( int n = 0; n < 4; n++ ) {
            cropHandlesX[n] = cropBorderXpoints[n];
            cropHandlesY[n] = cropBorderYpoints[n];
            centerX += cropBorderXpoints[n];
            centerY += cropBorderYpoints[n];            
        }
        centerX /= 4;
        centerY /= 4;
        
        // The handles in the middle of a side
        for ( int n = 4; n < 8; n++ ) {
            cropHandlesX[n] = (cropBorderXpoints[n%4]+cropBorderXpoints[(n+1)%4]) / 2;
            cropHandlesY[n] = (cropBorderYpoints[n%4]+cropBorderYpoints[(n+1)%4]) / 2;
        }
        
        cropHandlesX[8] = centerX;
        cropHandlesY[8] = centerY;
        
        rotHandleX[0] = (cropBorderXpoints[1]+cropBorderXpoints[0])/2;
        rotHandleY[0] = (cropBorderYpoints[1]+cropBorderYpoints[0])/2;
        rotHandleX[1] = rotHandleX[0] + (int)((double)(ROT_HANDLE_LENGTH) * sinRot );
        rotHandleY[1] = rotHandleY[0] - (int)((double)(ROT_HANDLE_LENGTH) * cosRot );
    }
    
    private void calcCropBorder( int x, int y, int imgWidth, int imgHeight ) {
        cropBorderX = imgWidth*(newCrop.getMinX() + 0.5*newCrop.getWidth() ) ;
        cropBorderY = imgHeight*(newCrop.getMinY() + 0.5*newCrop.getHeight() ) ;
        cropBorderLeft = 0.5*imgWidth*newCrop.getWidth();
        cropBorderRight = cropBorderLeft;
        cropBorderTop = 0.5*imgHeight*newCrop.getHeight();
        cropBorderBottom = cropBorderTop;
        cropBorderRot = 0;
        if ( cropBorderXpoints == null ) {
            cropBorderXpoints = new int[4];
            cropBorderYpoints = new int[4];
        }
        calcCropBorderCoords();
    }

    private void moveCropHandle( int handle, int newx, int newy ) {
        // Calculate the new position in local coordinates
        double sinRot = Math.sin( cropBorderRot );
        double cosRot = Math.cos( cropBorderRot );
        double localX = (newx-imgX-cropBorderX)*cosRot + (newy-imgY-cropBorderY)*sinRot;
        double localY = (newy-imgY-cropBorderY)*cosRot - (newx-imgX-cropBorderX)*sinRot; 
        final double minCropSize = 5.0;
        // Determine top & botton
        switch( handle ) {
            case HANDLE_TOP_LEFT:
            case HANDLE_TOP_RIGHT:
            case HANDLE_TOP:
                cropBorderTop = ( cropBorderBottom-localY > minCropSize ) 
                                ? -localY : -cropBorderBottom +minCropSize;       
                break;
                
            case HANDLE_BOT_LEFT:
            case HANDLE_BOT_RIGHT:
            case HANDLE_BOT:
                cropBorderBottom = (cropBorderTop+localY > minCropSize)
                ? localY : -cropBorderTop + minCropSize;
                break;
                
            case HANDLE_CENTER:
                cropBorderY = newy-imgY;
                break;
                
            default:
                break;

        }
        
        switch ( handle ) {
            case HANDLE_TOP_LEFT:
            case HANDLE_BOT_LEFT:
            case HANDLE_LEFT:
                cropBorderLeft = (cropBorderRight-localX > minCropSize )
                ? -localX : -cropBorderRight + minCropSize;
                break;
            case HANDLE_TOP_RIGHT:
            case HANDLE_BOT_RIGHT:
            case HANDLE_RIGHT:
                cropBorderRight = (cropBorderLeft+localX > minCropSize )
                ? localX : - cropBorderLeft+minCropSize;
                break;
                
            case HANDLE_CENTER:
                cropBorderX = newx-imgX;
                break;
            
            default:
                break;
                
        }
        calcCropBorderCoords();
    }
    
    /**
     Recalculate the local crop border coordinate system so that origin (and 
     center of rotation is at the center of the crop area.
     */
    private void alignCropBorderLocalCoordinates() {
        double dx = cropBorderRight - cropBorderLeft;
        cropBorderLeft += 0.5 * dx; cropBorderRight = cropBorderLeft;
        double dy = cropBorderBottom - cropBorderTop;
        cropBorderTop += 0.5 * dy; cropBorderBottom = cropBorderTop;
        
        // Change the origin in image coordinates
        double sinRot = Math.sin( cropBorderRot );
        double cosRot = Math.cos( cropBorderRot );
        cropBorderX += 0.5*(dx*cosRot - dy*sinRot);
        cropBorderY += 0.5*(dy*cosRot + dx*sinRot);
    }
    
    /**
     The new rotation that should be applied when next time transforming the
     image
     */
     
    double newRotDegrees;
    
    /**
     Calculates the new crop for image based on crop rectanlge
     */
    private void calcNewCrop() {
        double imgRotRadians = Math.toRadians( imgRot );
        double newRot = imgRotRadians - cropBorderRot;
        double sinNewRot = Math.sin( newRot );
        double cosNewRot = Math.cos( newRot );
        
        double sinOldRot = Math.sin( imgRotRadians );
        double cosOldRot = Math.cos( imgRotRadians );
        
        double sinDeltaRot = Math.sin( -cropBorderRot );
        double cosDeltaRot = Math.cos( -cropBorderRot );
        
        /* 
         Calculate the coordinate of crop border origin after applying
         the rotation in screen coordinates via the center of the image. Note 
         that this code expects that the image from which the crop is done is 
         itself uncropped.
         */
        double imgCenterX = 0.5*imgWidth;
        double imgCenterY = 0.5*imgHeight;
        double imgCoordX =  imgCenterX + cosDeltaRot*(cropBorderX-imgCenterX) 
                            - sinDeltaRot*(cropBorderY-imgCenterY);
        double imgCoordY =  imgCenterY + sinDeltaRot*(cropBorderX-imgCenterX) 
                            + cosDeltaRot*(cropBorderY-imgCenterY);
        
        /*
         Crop is presented in normalized coordinates so that the full image 
         fits inside (0,0)->(1,1) rectangle. Calcalate the scaling needed when 
         converting from image coordinates to normalized coordinates
         */
        double origWidth = (double) origImage.getWidth();
        double origHeight = (double) origImage.getHeight();
        double oldWidth = Math.abs(cosOldRot)*origWidth + Math.abs(sinOldRot)*origHeight; 
        double oldHeight = Math.abs(cosOldRot)*origHeight + Math.abs(sinOldRot)*origWidth; 
        double newWidth = Math.abs(cosNewRot)*origWidth + Math.abs(sinNewRot)*origHeight; 
        double newHeight = Math.abs(cosNewRot)*origHeight + Math.abs(sinNewRot)*origWidth; 
        // These are in image coordinates which may have been scaled to fdit the window
        double screenScaling = imgWidth / oldWidth;
        
        // We rotated around image center, transform origin into top left corner
        imgCoordX += ( newWidth-oldWidth ) * screenScaling * 0.5;
        imgCoordY += ( newHeight-oldHeight ) * screenScaling * 0.5;
        
        // And finally, normalize everything
        double newCropX = (imgCoordX-cropBorderLeft) / (newWidth*screenScaling);
        double newCropY = (imgCoordY-cropBorderTop) / (newHeight*screenScaling);
        double newCropWidth = (cropBorderRight + cropBorderLeft) / (newWidth*screenScaling);
        double newCropHeight = (cropBorderBottom + cropBorderTop) / (newHeight*screenScaling);
        
        newCrop = new Rectangle2D.Double( newCropX, newCropY, 
                newCropWidth, newCropHeight);
        newRotDegrees = Math.toDegrees( newRot );
    }
    
    /**
     Paints the crop border on top of an uncropped image
     */
    private void paintCropBorder( Graphics2D g2, int x, int y, int imgWidth, int imgHeight ) {
        if ( cropBorderXpoints == null ) {
            calcCropBorder( x, y, imgWidth, imgHeight );
        }
        
        Graphics2D borderG2 = (Graphics2D) g2.create();
        borderG2.setColor( Color.BLACK );
        GeneralPath border = new GeneralPath( GeneralPath.WIND_EVEN_ODD, 
                cropBorderXpoints.length );
        border.moveTo( cropBorderXpoints[0], cropBorderYpoints[0] );
        for ( int n = 1; n < cropBorderXpoints.length; n++ ) {
            border.lineTo( cropBorderXpoints[n], cropBorderYpoints[n] );
        }
        border.closePath();
        borderG2.draw( border );
        // Draw the handles
        int handleRadius = 4;
        for ( int n = 0; n < cropHandlesX.length; n++ ) {
            borderG2.setColor( Color.WHITE );
            borderG2.fillOval(
                    cropHandlesX[n]-handleRadius,
                    cropHandlesY[n]-handleRadius,
                    handleRadius*2, handleRadius*2 );
            borderG2.setColor( Color.BLACK );
            borderG2.drawOval(
                    cropHandlesX[n]-handleRadius,
                    cropHandlesY[n]-handleRadius,
                    handleRadius*2, handleRadius*2 );
        }
        // Draw the rotation handle
        borderG2.setColor( Color.BLACK );
        borderG2.drawLine( rotHandleX[0], rotHandleY[0], 
                rotHandleX[1], rotHandleY[1] );
        borderG2.setColor( Color.GREEN );
        borderG2.fillOval( 
                rotHandleX[1]-handleRadius,
                rotHandleY[1]-handleRadius,
                handleRadius*2, handleRadius*2 );
        borderG2.setColor( Color.BLACK );
        borderG2.drawOval( 
                rotHandleX[1]-handleRadius,
                rotHandleY[1]-handleRadius,
                handleRadius*2, handleRadius*2 );
    }

    private void paintImageBorder( Graphics2D g2, int x, int y, int imgWidth, int imgHeight ) {
        Graphics2D borderG2 = (Graphics2D) g2.create();
        Stroke stroke = new BasicStroke( 2.0f );
        borderG2.setStroke( stroke );
        // Paint the inner border
        GradientPaint p = new GradientPaint( new Point( x, y ), Color.black,
                new Point( x+imgWidth, y ), Color.white );
        borderG2.setPaint( p );
        borderG2.drawLine( x, y, x+imgWidth, y );
        p = new GradientPaint( new Point( x+imgWidth, y ), Color.white,
                new Point( x+imgWidth, y+imgHeight ), Color.black );
        borderG2.setPaint( p );
        borderG2.drawLine( x+imgWidth, y, x+imgWidth, y+imgHeight );
        p = new GradientPaint( new Point( x, y+imgHeight ), Color.white,
                new Point( x+imgWidth, y+imgHeight ), Color.black );
        borderG2.setPaint( p );
        borderG2.drawLine( x, y+imgHeight, x+imgWidth, y+imgHeight );
        p = new GradientPaint( new Point( x, y ), Color.black,
                new Point( x, y+imgHeight ), Color.white );
        borderG2.setPaint( p );
        borderG2.drawLine( x, y, x, y+imgHeight );

        // Paint the outer border
        x -= 2;
        y -= 2;
        imgWidth += 4;
        imgHeight += 4;
        p = new GradientPaint( new Point( x, y ), Color.white,
                new Point( x+imgWidth, y ), Color.black );
        borderG2.setPaint( p );
        borderG2.drawLine( x, y, x+imgWidth, y );
        p = new GradientPaint( new Point( x+imgWidth, y ), Color.black,
                new Point( x+imgWidth, y+imgHeight ), Color.white );
        borderG2.setPaint( p );
        borderG2.drawLine( x+imgWidth, y, x+imgWidth, y+imgHeight );
        p = new GradientPaint( new Point( x, y+imgHeight ), Color.black,
                new Point( x+imgWidth, y+imgHeight ), Color.white );
        borderG2.setPaint( p );
        borderG2.drawLine( x, y+imgHeight, x+imgWidth, y+imgHeight );
        p = new GradientPaint( new Point( x, y ), Color.white,
                new Point( x, y+imgHeight ), Color.black );
        borderG2.setPaint( p );
        borderG2.drawLine( x, y, x, y+imgHeight );
    }
    
    /**
     Get the crop rectangle handle that is at a specified position
     @param x x coordinate of the position
     @param y y coordinate of the position
     @return Number of the handle at the given position or -1 if none
     */ 
    private int getHandleAt( int x, int y ) {
        int accuracy = 10;
        int handle = -1;
        for ( int n = 0; n < cropHandlesX.length; n++ ) {
            int dx = x - cropHandlesX[n];
            int dy = y - cropHandlesY[n];
            if ( dx*dx + dy*dy < accuracy * accuracy ) {
                handle = n;
                break;
            }
        }
        return handle;
    }
    
    /**
     Checks if the crop area rotation handle is at the given coordinates
     @param x x coordinate of the position
     @param y y coordinate of the position
     @return true if the rotetion handle is at the position, false otherwise
     */
    private boolean isRotHandleAt( int x, int y ) {
        int accuracy = 10;
        int dx = x - rotHandleX[1];
        int dy = y - rotHandleY[1];
        boolean isHandle = ( dx*dx + dy*dy < accuracy * accuracy );
        log.debug( "Is rot handle? " + isHandle );
        return isHandle;
    }
    
    /**
     Rotates the crop area so that rotation handle points to a given (mouse) 
     position.
     @param x x coordinate of the position
     @param y y coordinate of the position
     */
    private void rotateCrop( int newx, int newy ) {
        // Calculate new rotation
        double dx = newx-imgX-cropBorderX;
        double dy = newy-imgY-cropBorderY;
        if ( Math.abs( dy ) > 0.0001 ) {
            cropBorderRot = -Math.atan( dx/dy );
        } else {
            cropBorderRot =  0;
        }
        if ( dy > 0 ) {
            cropBorderRot += Math.PI;
        }
        log.debug( "New rotation " + cropBorderRot );
        calcCropBorderCoords();

    }

    /**
     Sets the image displayed in the component
     @param img The image
     */
    public void setImage( PhotovaultImage img ) {
        if ( origImage != null ) {
            origImage.dispose();
        }
	origImage = img;
	xformImage = null;
        fireImageChangedEvent( new PhotoViewEvent( this ) );
 	repaint();
    }

    public PhotovaultImage getImage() {
        return origImage;
    }
    
    public Dimension getPreferredSize() {
	
	int w = 0;
	int h = 0;
	if ( origImage != null ) {
	    if ( xformImage == null ) {
		buildXformImage();
	    }
            if ( xformImage != null ) {
                w = xformImage.getWidth();
                h = xformImage.getHeight();
                if ( drawCropped && fitSize ) {
                    w += fitMarginLeft + fitMarginRight;
                    h += fitMarginTop + fitMarginLeft;
                }
            }
	}
	return new Dimension( w, h );
    }

    /**
     Set the zoom scaling factor used for drawing the image
     @param newValue (1.0 = actual scale, no zooming)
     */
    public void setScale( float newValue ) {
	imgScale = newValue;
	xformImage = null;
	fitSize = false;
	revalidate();
        // Revalidate issues a paint request if the component's size changes. 
        // Hovewer if this does not happen we need to explicitly repaint thi component
        repaint();
    }
    
    public float getScale() {
	return imgScale;
    }

    /**
     Set the rotation to apply for the image
     @param newRot in degrees, clockwise
     */
    public void setRotation( double newRot ) {
	newRotDegrees = newRot;
	xformImage = null;
	revalidate();
        repaint();
    }

    
    public double getRotation() {
	return imgRot;
    }

    /**
     Set the crop rectangle to use for the image
     */
    public void setCrop( Rectangle2D crop ) {
        newCrop = (Rectangle2D) crop.clone();
        xformImage = null;
        updateCropParamEditor();
        revalidate();
        repaint();
    }

    public Rectangle2D getCrop( ) {
        return (Rectangle2D) crop.clone();
    }
    
    
    void setSaturation(double newSat) {
        if ( origImage != null ) {
            origImage.setSaturation( newSat );
            xformImage = null;
            repaint();
        }
    }
    
    /**
     Set color mapping curve for a color channel
     @param name Name of the color channel
     @param c The new mapping curve
     */
    void setColorCurve(String name, ColorCurve c) {
        if ( origImage != null ) {
            if ( c == null ) {
                // Null curve, use identity mapping
                c = new ColorCurve();
            }
            ChannelMapOperation cm = origImage.getColorAdjustment();
            ChannelMapOperationFactory f = new ChannelMapOperationFactory( cm );
            f.setChannelCurve( name, c );
            origImage.setColorAdjustment( f.create() );
            xformImage = null;
            repaint();
        }
    }

    /**
     Set the raw settings that are used to display the image (assuming it is a 
     raw image)
     @param s The new raw settings.
     */
    void setRawSettings( RawConversionSettings s ) {
        if ( origImage != null && origImage instanceof RawImage ) {
            ((RawImage) origImage).setRawSettings( s );
        }
    }
    
    /**
     Get the raw conversion settings that are currently applied to the image
     @return The current raw conversion parameters or <code>null</code> if the 
     image is not a raw image.
     */
    RawConversionSettings getRawSettings() {
        RawConversionSettings ret = null;
        if ( origImage != null && origImage instanceof RawImage ) {
            ret = ((RawImage) origImage).getRawSettings();
        }
        return ret;
    }
    
    private double imgRot;
    private Rectangle2D crop;
    
    /**
     The new crop as a result of the crop operation
     */
    private Rectangle2D newCrop;

    /**
     Flag that tells whether to draw the cropped or original image
     <ul>
     <li>if <code>true</code> draws only the cropped portion of the image</li>
     <li>if <code>false</code> draws the entire image without cropping</li>
     */
    private boolean drawCropped = true;
    
    /**
     Sets the @see drawCropped property
     */
    public void setDrawCropped( boolean cropped ) {
        drawCropped = cropped;
        paramEditor.setVisible( !drawCropped );
        xformImage = null;
        revalidate();
        repaint();
    }
    
    /**
     Get the current value of @see drawCropped property
     */
    
    public boolean getDrawCropped() {
        return drawCropped;
    }
    
    /**
       Returns the width of the currently displayed image. Note that the original image may be rotated for
       displaying it correctly, so this cannot be used to calculate e.g. scaling needed to fit the image
       to certain dimensions.
    */
    public int getOrigWidth() {
	if ( origImage == null ) {
	    return 0;
	}
	return origImage.getWidth();
    }
    
    /**
       Returns the height of the currenty displayed image
    */
    public int getOrigHeight() {
	if ( origImage == null ) {
	    return 0;
	}
	return origImage.getHeight();
    }

    /**
       Set the scaling so that the image fits inside given dimensions
       @param width The maximum width of the scaled image
       @param height The maximum height of the scaped image
    */
    public void fitToRect( double width, double height ) {
	//	System.err.println( "Fitting to " + width + ", " + height );
	fitSize = true;
	maxWidth = width;
	maxHeight = height;
	// Revalidate the geometry & image size
	xformImage = null;
        revalidate();
	repaint();
    }
    
    /**
     Rebuilds the zoomed, cropped & rotated image to show 
     */
    private void buildXformImage() {
        Cursor oldCursor = getCursor();
        try {
            
            imgRot = newRotDegrees;
            
            // Set the hourglass cursor
            setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
            
            Rectangle2D cropUsed = newCrop;
            if ( !drawCropped ) {
                cropUsed = new Rectangle2D.Double( 0.0, 0.0, 1.0, 1.0 );
            } else {
                crop = newCrop;
            }
            origImage.setCropBounds( cropUsed );
            origImage.setRotation( imgRot );
            
            // Create the zoom xform
            if ( fitSize ) {
                double usedMaxWidth = maxWidth;
                double usedMaxHeight = maxHeight;
                if ( drawCropped ) {
                    usedMaxWidth -= fitMarginLeft + fitMarginRight;
                    usedMaxHeight -= fitMarginBottom + fitMarginTop;
                }
                xformImage = origImage.getRenderedImage( (int)usedMaxWidth, (int)usedMaxHeight, false );
            } else {
                xformImage = origImage.getRenderedImage( imgScale, false );
                
            }
            // Create tile painters for the image
            int xTileCount = xformImage.getNumXTiles();
            int yTileCount = xformImage.getNumYTiles();
            xformImageTiles = new ImageTile[xTileCount][yTileCount];
            for ( int ytile = 0 ; ytile < yTileCount ; ytile++ ) {
                for ( int xtile = 0; xtile < xTileCount ; xtile++ ) {
                    xformImageTiles[xtile][ytile] =
                            new ImageTile( this, (PlanarImage) xformImage,
                            xtile, ytile);
                }
            }
            ((PlanarImage)xformImage).addTileComputationListener(
                    new ImageTileComputationListener( this ) );

            cropBorderXpoints = null;
            cropBorderYpoints = null;
        } catch ( Exception ex ) {
            /*
             There was some kind of error when constructing the image to show.
             Most likely the image file was corrupted.
             */
            final String exMsg = ex.getMessage();
            final JAIPhotoView staticThis = this;
            staticThis.origImage = null;
            SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog( staticThis,
                            "Error while showing an image\n" +
                            exMsg +
                            "\nMost likely the image file in Photovault database is corrupted.",
                            "Error displaying image",
                            JOptionPane.ERROR_MESSAGE );
                }
            });
            
        }
        setCursor( oldCursor );
    }

    int handleMoving = -1;
    boolean isRotating = false;

    /**
     * Time when color info box was made visible
     */
    long infoStartTime = 0;
    /**
     * Should color info box be drawn
     */
    boolean showInfo = false;
    /**
     * The RGB color info text
     */
    String infoTextRGB = "";
    /**
     * The Lab color info text
     */
    String infoTextLab = "";
    /**
     * X coordinate of the pixel whose color is dsplayed in color info box
     */
    int infoX;
    /**
     * Y coordinate of the pixel whose color is dsplayed in color info box
     */
    int infoY;
    
    public void mouseClicked(MouseEvent mouseEvent) {
        if ( drawCropped && xformImage != null ) {
            int x = mouseEvent.getX() - imgX;
            int y = mouseEvent.getY() - imgY;
            showColorInfoBox( x, y);
        }
    }

    private static void xyzToLab( float[] xyz, float[] lab ) {
        float eps = 0.008856f;
        float k = 903.3f;
        float fx = xyz[0] > eps ? (float) Math.pow( xyz[0], 1.0f/3.0f ) : (k*xyz[0]+16.0f)/116.0f;
        float fy = xyz[1] > eps ? (float) Math.pow( xyz[1], 1.0f/3.0f ) : (k*xyz[1]+16.0f)/116.0f;
        float fz = xyz[2] > eps ? (float) Math.pow( xyz[2], 1.0f/3.0f ) : (k*xyz[2]+16.0f)/116.0f;
        lab[0] = 116.0f*fy-16.0f;
        lab[1]= 500.0f * ( fx - fy );
        lab[2]= 200.0f * ( fy - fz );
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if  ( !drawCropped ) {
            // Check if we clicked on a handle
            handleMoving = getHandleAt( mouseEvent.getX(), mouseEvent.getY() );
            log.debug( "Moving handle " + handleMoving );
            if ( handleMoving < 0 ) {
                isRotating = isRotHandleAt( mouseEvent.getX(), mouseEvent.getY() );
            }
        }
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        if ( handleMoving >= 0 || isRotating ) {
            calcNewCrop();
            updateCropParamEditor();
            alignCropBorderLocalCoordinates();
            handleMoving = -1;
            isRotating = false;
        } 
    }

    public void mouseEntered(MouseEvent mouseEvent) {
    }

    public void mouseExited(MouseEvent mouseEvent) {
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if ( !drawCropped ) {
            if ( handleMoving >= 0 ) {
                moveCropHandle( handleMoving, mouseEvent.getX(), mouseEvent.getY() );
                repaint();
            } else if ( isRotating ) {
               rotateCrop( mouseEvent.getX(), mouseEvent.getY() );
               repaint();
            }
        }
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        // Change the mouse cursor if on top of crop handle
        if ( !drawCropped && handleMoving == -1 && !isRotating ) {
            int handle = getHandleAt( mouseEvent.getX(), mouseEvent.getY() );
            if ( handle >= 0 ) {
               setCursor( Cursor.getPredefinedCursor( cropHandleCursors[handle] ) );
            } else if ( isRotHandleAt( mouseEvent.getX(), mouseEvent.getY() ) ) {
                setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );               
            } else {
                setCursor( Cursor.getDefaultCursor() );
            }
        }
    }

    /**
     Called by @see CropParamEditor when the user want's to apply the crop to 
     image. Sends crop area change event to listeners.
     */
    public void applyCrop() {
        CropAreaChangeEvent e = new CropAreaChangeEvent( this, newCrop, newRotDegrees );
        fireCropAreaChangeEvent( e );
        
    }

    /**
     Called by @see CropParamEditor when the user want's to cancel the crop operation.     
     */
    public void cancelCrop() {
        /* 
         To cancel the crop, create a crop area change event with the old 
         crop parameters
         */
        newCrop = crop;
        updateCropParamEditor();
        newRotDegrees = imgRot;
        CropAreaChangeEvent e = new CropAreaChangeEvent( this, crop, imgRot );
        fireCropAreaChangeEvent( e );
        
    }

    public void cropParamsChanged() {
        cropBorderRot = Math.toRadians( imgRot - paramEditor.getRot() );
        if ( cropBorderXpoints != null ) {
            calcCropBorderCoords();
            calcNewCrop();
            repaint();
        }
    }

    /**
     * Initialize the color info box that shows color of a pixel
     * @param x X coordinate of the pixel (from xformImage)
     * @param y Y coordinate of the pixel (from xformImage)
     */
    private void showColorInfoBox( int x, int y ) {
        if ( x < xformImage.getMinX() ||
                y < xformImage.getMinY() ||
                x >= xformImage.getMinX()+xformImage.getWidth() ||
                y >= xformImage.getMinY()+xformImage.getHeight() ) {
            return;
        }
        Raster imgRaster = xformImage.getData();
        float[] sample = new float[3];
        for ( int n = 0; n < 3;
                n++ ) {
            sample[n] = imgRaster.getSampleFloat( x, y, n ) / 255.0f;
        }
        ColorSpace cs = xformImage.getColorModel().getColorSpace();
        float[] ciexyz = cs.toCIEXYZ( sample );
        float[] labValue = new float[3];
        xyzToLab( ciexyz, labValue );

        // Create the info dialog
        infoX = x;
        infoY = y;
        infoTextRGB =
                String.format( "RGB: %.2f, %.2f, %.2f", sample[0], sample[1],
                sample[2] );
        infoTextLab =
                String.format( "Lab: %2.1f, %2.1f, %2.1f", labValue[0],
                labValue[1], labValue[2] );
        infoStartTime = System.currentTimeMillis();
        showInfo = true;
        repaint( x + imgX, y + imgY, 200, 200 );
    }
	
    private void updateCropParamEditor() {
        paramEditor.setXmin( newCrop.getX() );
        paramEditor.setYmin( newCrop.getY() );
        paramEditor.setXmax( newCrop.getX() + newCrop.getWidth() );
        paramEditor.setYmax( newCrop.getY() + newCrop.getHeight() );
        paramEditor.setRot( this.newRotDegrees );
    }
    
    // The image that is viewed
    PhotovaultImage origImage = null;
    RenderedImage xformImage = null;
    ImageTile[][] xformImageTiles = null;

    // scale of the image
    float imgScale = 1.0f;

    /**
       If true, fits the image size to a maximum.
    */
    boolean fitSize = false;

    /**
       Maximum width of the image if fitSize is true. Otherwise ignored
    */
    double maxWidth;
    /**
       Maximum height of the image if fitSize is true. Otherwise ignored
    */
    double maxHeight;
    
}
	

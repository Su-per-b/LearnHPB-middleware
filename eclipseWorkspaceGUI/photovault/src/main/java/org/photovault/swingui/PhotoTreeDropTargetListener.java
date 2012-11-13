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

import java.awt.dnd.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;

import org.photovault.imginfo.*;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.folder.*;    

/**
   Implements the dropping logic for PhotoInfoTree. Modified from JavaWorld tip,
   @see http://www.javaworld.com/javaworld/javatips/jw-javatip114.html
*/
class PhotoTreeDropTargetListener implements DropTargetListener
{
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( PhotoTreeDropTargetListener.class.getName() );
    private TreePath lastPath = null;
    private Rectangle2D cueRect = new Rectangle2D.Float();
    private Rectangle2D ghostImgRect = new Rectangle2D.Float();
    private Color cueRectColor;
    private Point _ptLast = new Point();
    private Timer hoverTimer;
    private int	_nLeftRight = 0;	// Cumulative left/right mouse movement
    private int	_nShift	= 0;

    private final JTree tree;

    /**
       Data flavor for an array of PhotoInfo objects. This is used when transferring
       photos inside the same virtual machine
    */
    DataFlavor photoInfoFlavor = null;
    
    
    // Constructor...
    public PhotoTreeDropTargetListener( JTree t )
    {
	this.tree = t;
	cueRectColor = new Color(
				  SystemColor.controlShadow.getRed(),
				  SystemColor.controlShadow.getGreen(),
				  SystemColor.controlShadow.getBlue(),
				  64
				  );

	try {
	    photoInfoFlavor = new DataFlavor( DataFlavor.javaJVMLocalObjectMimeType
					      + ";class=\"" + PhotoInfo[].class.getName()
					      + "\"" );
	} catch ( Exception e ) {
	}

	// Set up a hover timer, so that a node will be automatically expanded or collapsed
	// if the user lingers on it for more than a short time
	hoverTimer = new Timer(1000, new ActionListener()
	    {
		public void actionPerformed(ActionEvent e)
		{
		    if (isRootPath(lastPath))
			return;	// Do nothing if we are hovering over the root node
		    if (tree.isExpanded(lastPath))
			tree.collapsePath(lastPath);
		    else
			tree.expandPath(lastPath);
		}
	    });
	hoverTimer.setRepeats(false);	// Set timer to one-shot mode
    }
		
    // DropTargetListener interface
    public void dragEnter(DropTargetDragEvent e)
    {
	if (!isDragAcceptable(e))
	    e.rejectDrag();
	else
	    e.acceptDrag(e.getDropAction());	
    }
		
    public void dragExit(DropTargetEvent e)
    {
	if (!DragSource.isDragImageSupported())
	    {
		tree.repaint(ghostImgRect.getBounds());				
	    }
    }

    /**
     * This is where the ghost image is drawn
     */		
    public void dragOver(DropTargetDragEvent e)
    {
	// Even if the mouse is not moving, this method is still invoked 10 times per second
	Point pt = e.getLocation();
	if (pt.equals(_ptLast))
	    return;
			
	// Try to determine whether the user is flicking the cursor right or left
	int nDeltaLeftRight = pt.x - _ptLast.x;
	if ( (_nLeftRight > 0 && nDeltaLeftRight < 0) || (_nLeftRight < 0 && nDeltaLeftRight > 0) )
	    _nLeftRight = 0;
	_nLeftRight += nDeltaLeftRight;	


	_ptLast = pt;	
			

	Graphics2D g2 = (Graphics2D) tree.getGraphics();

	// If a drag image is not supported by the platform, then draw my own drag image
	if (!DragSource.isDragImageSupported())
	    {
		tree.paintImmediately(ghostImgRect.getBounds());	// Rub out the last ghost image and cue line
		// And remember where we are about to draw the new ghost image
// 		ghostImgRect.setRect(pt.x - _ptOffset.x, pt.y - _ptOffset.y, _imgGhost.getWidth(), _imgGhost.getHeight());
// 		g2.drawImage(_imgGhost, AffineTransform.getTranslateInstance(ghostImgRect.getX(), ghostImgRect.getY()), null);				
	    }
	else	// Just rub out the last cue line
	    tree.paintImmediately(cueRect.getBounds());				
			
			
			
	TreePath path = tree.getClosestPathForLocation(pt.x, pt.y);
	if (!(path == lastPath))			
	    {
		_nLeftRight = 0; 	// We've moved up or down, so reset left/right movement trend
		lastPath = path;
		hoverTimer.restart();
	    }

	// In any case draw (over the ghost image if necessary) a cue line indicating where a drop will occur
	Rectangle raPath = tree.getPathBounds(path);
	cueRect.setRect( raPath );

	g2.setColor(cueRectColor);
	g2.fill(cueRect);
			
	// Now superimpose the left/right movement indicator if necessary
	if (_nLeftRight > 20)
	    {
		_nShift = +1;
	    }
	else if (_nLeftRight < -20)
	    {
		_nShift = -1;
	    }
	else
	    _nShift = 0;
			

	// And include the cue line in the area to be rubbed out next time
	ghostImgRect = ghostImgRect.createUnion(cueRect);	

	/*				
	// Do this if you want to prohibit dropping onto the drag source
	if (path.equals(_pathSource))			
	e.rejectDrag();
	else
	e.acceptDrag(e.getDropAction());	
	*/				
    }
		
    public void dropActionChanged(DropTargetDragEvent e)
    {
	if (!isDragAcceptable(e))
	    e.rejectDrag();
	else
	    e.acceptDrag(e.getDropAction());	
    }
		
    public void drop(DropTargetDropEvent e) {
	hoverTimer.stop();	// Prevent hover timer from doing an unwanted expandPath or collapsePath
			
	if (!isDropAcceptable(e)) {
	    e.rejectDrop();
	    return;
	}
			
	e.acceptDrop(e.getDropAction());
			
	Transferable transferable = e.getTransferable();
			
	DataFlavor[] flavors = transferable.getTransferDataFlavors();
	for (int i = 0; i < flavors.length; i++ ) {
	    DataFlavor flavor = flavors[i];
	    if (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType)) {
		try {
		    Point pt = e.getLocation();
		    TreePath pathTarget = tree.getClosestPathForLocation(pt.x, pt.y);
		    PhotoFolder folder = (PhotoFolder) pathTarget.getLastPathComponent();
                    PhotoCollectionTransferHandler.setLastImportTarget( folder );
		    PhotoInfo[] photos = (PhotoInfo[])transferable.getTransferData(photoInfoFlavor);
		    for ( int n = 0; n < photos.length; n++ ) {
			folder.addPhoto( photos[n] );
		    }
		    break; // No need to check remaining flavors
		} catch (UnsupportedFlavorException ufe) {
		    log.warn(ufe);
		    e.dropComplete(false);
		    return;
		} catch (IOException ioe) {
		    log.warn(ioe);
		    e.dropComplete(false);
		    return;
		}
	    }
	}
			
	e.dropComplete(true);
    }
		
		
		
    // Helpers...
    public boolean isDragAcceptable(DropTargetDragEvent e) {
	return true;
	// 	// Only accept COPY or MOVE gestures (ie LINK is not supported)
// 	if ((e.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0)
// 	    return false;

// 	// Only accept this particular flavor	
// 	if (!e.isDataFlavorSupported(CTransferableTreePath.TREEPATH_FLAVOR))
// 	    return false;
				
// 	/*				
// 	// Do this if you want to prohibit dropping onto the drag source...
// 	Point pt = e.getLocation();
// 	TreePath path = getClosestPathForLocation(pt.x, pt.y);
// 	if (path.equals(_pathSource))			
// 	return false;

// 	*/
				
// 	/*				
// 	// Do this if you want to select the best flavor on offer...
// 	DataFlavor[] flavors = e.getCurrentDataFlavors();
// 	for (int i = 0; i < flavors.length; i++ )
// 	{
// 	DataFlavor flavor = flavors[i];
// 	if (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType))
// 	return true;
// 	}
// 	*/
// 	return true;
    }

    public boolean isDropAcceptable(DropTargetDropEvent e)
    {
// 	// Only accept COPY or MOVE gestures (ie LINK is not supported)
// 	if ((e.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0)
// 	    return false;

// 	// Only accept this particular flavor	
// 	if (!e.isDataFlavorSupported(CTransferableTreePath.TREEPATH_FLAVOR))
// 	    return false;

// 	/*				
// 	// Do this if you want to prohibit dropping onto the drag source...
// 	Point pt = e.getLocation();
// 	TreePath path = getClosestPathForLocation(pt.x, pt.y);
// 	if (path.equals(_pathSource))			
// 	return false;
// 	*/				
				
// 	/*				
// 	// Do this if you want to select the best flavor on offer...
// 	DataFlavor[] flavors = e.getCurrentDataFlavors();
// 	for (int i = 0; i < flavors.length; i++ )
// 	{
// 	DataFlavor flavor = flavors[i];
// 	if (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType))
// 	return true;
// 	}
// 	*/
	return true;
    }

  private boolean isRootPath(TreePath path)
    {
    return tree.isRootVisible() && tree.getRowForPath(path) == 0;
  }
    

}
	

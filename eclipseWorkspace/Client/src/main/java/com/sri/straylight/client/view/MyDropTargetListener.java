package com.sri.straylight.client.view;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

class MyDropTargetListener extends DropTargetAdapter {
    private DropTarget dropTarget;
    private JPanel panel;
    private SimulationEngineDialog simulationEngineDialog_;

    public MyDropTargetListener(JPanel panel, SimulationEngineDialog simulationEngineDialog) {
      this.panel = panel;
      dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
      this.simulationEngineDialog_ = simulationEngineDialog;
    }
    

    public void drop(DropTargetDropEvent event) {
    	
      try {
        Transferable tr = event.getTransferable();
        
         //DataFlavor[] dataFlavorAry =  tr.getTransferDataFlavors();
        
         
        if (event.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
          event.acceptDrop(DnDConstants.ACTION_COPY);
          	
          Object javaFileListObj = tr.getTransferData(DataFlavor.javaFileListFlavor);
          
          java.util.List<File> javaFileList = (java.util.List<File>) javaFileListObj;
        		  
          if (javaFileList.size() == 1) {
        	  
        	  File theFile = javaFileList.get(0);
        	  //String path = theFile.getPath();
        	  
        	  this.simulationEngineDialog_.selectFile(theFile);
        	  
          }
          
          event.dropComplete(true);
          return;
        }
         
        event.rejectDrop();
        
      } catch (Exception e) {
        e.printStackTrace();
        event.rejectDrop();
      }
    }
  }

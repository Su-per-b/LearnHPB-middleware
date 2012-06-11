package com.sri.straylight.client.controller;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.InitializedInfo;

public class InternalTableController  extends AbstractController {
	
	
    private  JTable table_;
    private DefaultTableModel dataModel_;
    
    
	public InternalTableController(AbstractController parentController) {
		super(new JPanel(), parentController);	
	}
	
	
	public void init(InitializedInfo initializedStruct) {  
		

	    JPanel panelTable = (JPanel) getView();

	    panelTable.setPreferredSize(new Dimension(704, 500));
	    panelTable.setLayout(new GridLayout(1, 1, 0, 0));
	    
		dataModel_ = new DefaultTableModel (
			initializedStruct.getInternalData(),
			initializedStruct.getColumnNames()
		);
		
		table_ = new JTable(dataModel_);
		table_.setPreferredScrollableViewportSize(new Dimension(700, 600));
		table_.setFillsViewportHeight(true);
		
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(table_);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    panelTable.add(scrollPaneTable);

	    
    }
    
}

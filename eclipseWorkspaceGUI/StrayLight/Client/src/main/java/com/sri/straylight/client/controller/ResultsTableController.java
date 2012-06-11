package com.sri.straylight.client.controller;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.InitializedInfo;


public class ResultsTableController extends AbstractController {
	
    private  JTable table_;
    private DefaultTableModel dataModel_;
    
    
	public ResultsTableController(AbstractController parentController) {
		super(parentController);
	}
	
	public void init(InitializedInfo initializedStruct) {  
		

	    JPanel panel = new JPanel();
	    
	    panel.setPreferredSize(new Dimension(704, 500));
	    panel.setLayout(new GridLayout(1, 1, 0, 0));
	    
		Object[][] data = {{}};
		
		dataModel_ = new DefaultTableModel(data,initializedStruct.columnNames);
		
		table_ = new JTable(dataModel_);
		table_.setPreferredScrollableViewportSize(new Dimension(700, 600));
		table_.setFillsViewportHeight(true);
		
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(table_);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    panel.add(scrollPaneTable);
	    
	    setView_(panel);
	    
    }
    
	
	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {

		Object[] newRow = event.resultItem.getStrings();

		dataModel_.insertRow(1,newRow);
	}
	
	
}
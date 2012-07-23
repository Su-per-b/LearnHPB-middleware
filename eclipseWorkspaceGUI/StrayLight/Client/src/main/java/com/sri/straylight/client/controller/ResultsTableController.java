package com.sri.straylight.client.controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;


public class ResultsTableController extends AbstractController {
	
    private  JTable table_;
    private DefaultTableModel dataModel_;
    
    
	public ResultsTableController(AbstractController parentController) {
		super(parentController);
	}
	
	public void init(XMLparsed xmlParsed) {  
		

	    JPanel panel = new JPanel();
	    
	    panel.setPreferredSize(new Dimension(704, 500));
	    panel.setLayout(new GridLayout(1, 1, 0, 0));
	    
		Object[][] data = {{}};
		
		dataModel_ = new DefaultTableModel(data,xmlParsed.getOutputColumnNames());
		
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
	
	
	public void reset() {
		
		int count = dataModel_.getRowCount();
		for (int i = 0; i < count; i++) {
			dataModel_.removeRow(0);
		}
		
	}
	
	
	
	
	
	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {

		Vector<String> resultOuput = event.resultOfStep.getOutputList();
		
		double time = event.resultOfStep.getTime();
		
		
		resultOuput.insertElementAt(Double.toString(time), 0);
		
		
		dataModel_.insertRow(0,resultOuput);
	}
	
	
}
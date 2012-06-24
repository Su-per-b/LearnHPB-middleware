package com.sri.straylight.client.controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValue;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;


public class ResultsFormController extends AbstractController {
	
    private  JTable table_;
    private DefaultTableModel tableModel_;
	
    
	public ResultsFormController(AbstractController parentController) {
		super(parentController);
	}
	
	public void init(XMLparsed xmlParsed) {  
		
	    JPanel panel = new JPanel();
	    
	    panel.setPreferredSize(new Dimension(704, 500));
	    panel.setLayout(new GridLayout(1, 1, 0, 0));
	    
		tableModel_ = new DefaultTableModel (
				
				xmlParsed.getOutputFormData(),
				xmlParsed.getOutputFormColumnNames()
		);
		

		table_ = new JTable(tableModel_);
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

		Vector<String> resultOutput = event.resultOfStep.getOutputList();
		
		int len = resultOutput.size();
		

		//populate the 'value' column
		for (int i = 0; i < len; i++) {
			//double sv =  ary[i];
			String str = resultOutput.get(i);
			
			tableModel_.setValueAt(str, i, 1);
		}
		
	}
	
	
	
}
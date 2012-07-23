package com.sri.straylight.client.controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.InputChangeRequest;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.client.model.SimStateClient;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;

public class InputFormController extends AbstractController {

    private  JTable table_;
    private DefaultTableModel tableModel_;
    
    private XMLparsed xmlParsed_;
    
	public InputFormController(AbstractController parentController) {
		super(parentController);
	}
	
	private final JButton btnSubmit_ = new JButton("submit");
	
	private double[] resultInputAry_;
	
	public void init(XMLparsed xmlParsed) {  
		
		xmlParsed_ = xmlParsed;
		
		JPanel panelButton = new JPanel();
	    JPanel panel = new JPanel();
	    
	    panel.setPreferredSize(new Dimension(704, 300));
	    panel.setLayout(new GridLayout(2, 1, 0, 0));
	    
		tableModel_ = new DefaultTableModel (
				
				xmlParsed_.getInputData(),
				xmlParsed_.getInputFormColumnNames()
		);
		

		table_ = new JTable(tableModel_);
		table_.setPreferredScrollableViewportSize(new Dimension(700, 600));
		table_.setFillsViewportHeight(true);
		
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPaneTable = new JScrollPane(table_);
	    scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scrollPaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    
	    
	    panelButton.add(btnSubmit_);
	    
	    panel.add(scrollPaneTable);
	    panel.add(panelButton);
	    
	    setView_(panel);
	    bindActions_();
	    
    }
	
	
	public void reset(XMLparsed xmlParsed) {  
		xmlParsed_ = xmlParsed;
		
		tableModel_ = new DefaultTableModel (
				
				xmlParsed_.getInputData(),
				xmlParsed_.getInputFormColumnNames()
		);
		
		table_.setModel(tableModel_);
		
	}
	
	
	private void bindActions_() {

		btnSubmit_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				submitChanges();
				

			}
		}
				);

	}
	
	
	private void submitChanges() {
		
		 Object[][] inData = xmlParsed_.getInputData();
		 
		 
		
		int len = xmlParsed_.inputVars.length;
		
		for (int i = 0; i < len; i++) {
			int idx = xmlParsed_.inputVars[i].idx;
			
			
			String theCell = (String) inData[i][1];
			String newValueStr = (String) tableModel_.getValueAt(i, 1);
			double newValueDouble = Double.valueOf(newValueStr);
			
			double currentValueDouble = resultInputAry_[i];
			
			if (newValueDouble != currentValueDouble) {
				
				resultInputAry_[i] = newValueDouble;
				
				InputChangeRequest event = new InputChangeRequest(idx,newValueDouble);
				EventBus.publish(event);

			}
			
			

			
			
		}
		
		

		
		
	}
	
	
	
	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {

		Vector<String> resultInput = event.resultOfStep.getInputList();
		int len = resultInput.size();
		
		resultInputAry_ = event.resultOfStep.getInput();
				
		//populate the 'value' column
		for (int i = 0; i < len; i++) {
			String str = resultInput.get(i);
			
			tableModel_.setValueAt(str, i, 1);
		}
		
	}
    
}

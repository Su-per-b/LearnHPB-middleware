package com.sri.straylight.client.model;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;

public class ResultsLogModel {
	
	private XMLparsed xmlParsed_;
	
	private DefaultTableModel tableModel_;
	
	public ResultsLogModel(XMLparsed xmlParsed) {
		xmlParsed_ = xmlParsed;
		
		init();
		
	}

	
	public void init() {
		
		Object[][] data = {{}};
		String[] columnNames = xmlParsed_.getOutputColumnNames();
		
		tableModel_ = new DefaultTableModel(data, columnNames);
		tableModel_.removeRow(0);
		
	}
	
	
	public DefaultTableModel getTableModel() {
		return tableModel_;
	}
	
	

	public void addNewResult(ScalarValueResults scalarValueResults) {

		Vector<String> resultOuput = scalarValueResults.output.getStringList();
		double time = scalarValueResults.getTime();

		resultOuput.insertElementAt(Double.toString(time), 0);
		tableModel_.insertRow(0,resultOuput);
		
	}

}

package com.sri.straylight.client.model;

import java.util.Vector;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;

public class ResultsLogModel  extends VariableDataModel {
	

	
	public ResultsLogModel(Vector<ScalarVariableReal> variables) {
		
		super(variables);

	}

/*	
	public DefaultTableModel makeTableModel() {
		
		Object[][] data = {{}};
		
		String[] columns = getColumns();
		
		tableModel_ = new DefaultTableModel(data, columns);
		tableModel_.removeRow(0);
		
		return tableModel_;
	}*/
	
//	public DefaultTableModel getTableModel() {
//
//		return tableModel_;
//	}
	
	public void clear() {
		tableModel_.setRowCount(0);
	}
	

	public void addNewResult(ScalarValueResults scalarValueResults) {

		Vector<String> resultOuput = scalarValueResults.getOutput().getStringList();
		double time = scalarValueResults.getTime();

		resultOuput.insertElementAt(Double.toString(time), 0);
		tableModel_.insertRow(0,resultOuput);
		
	}


	protected String[][] getRows() {
		

//		String[][] rows = new String[][];
//
//		
//		for (int i = 0; i < rowCount; i++) {
//			ScalarVariableReal sv = variables_.get(i);
//			rows[i] = sv.toStringArray();
//		}

		return null;
	
	}
	
	public  String[] getColumns() {

		int len = variables_.size();
		String[] columns = new String[len+1];
		
		columns[0] = "time";
		
		
		for (int i = 0; i < len; i++) {
			ScalarVariableReal sv = variables_.get(i);
			columns[i+1] = sv.getName();
		
		}

		return columns;

	}

}

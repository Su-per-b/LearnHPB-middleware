package com.sri.straylight.client.model;

import java.util.Vector;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;

public class ResultsLogModel extends VariableDataModel {
	

	
	public ResultsLogModel(String title, Vector<ScalarVariableReal> variables) {
		super(title,variables);
	}


	
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

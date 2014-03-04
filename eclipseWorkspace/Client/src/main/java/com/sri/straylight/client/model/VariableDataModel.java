package com.sri.straylight.client.model;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voNative.Enu;

public class VariableDataModel {
	
    
    protected Vector<ScalarVariableReal> variables_;
    protected ScalarValueCollection latestValues_;
    protected DefaultTableModel tableModel_;
	protected Enu causality_;
    
	
	public VariableDataModel(Vector<ScalarVariableReal> variables) {
		
		variables_ = variables;
		makeTableModel();
		
	}
	
	protected void makeTableModel() {
		
		Object[][] rows = getRows();
		String[] columns = getColumns();
		
		tableModel_ = new DefaultTableModel(rows, columns);
		
	}
	
	
	public DefaultTableModel getTableModel() {

		return tableModel_;
	}
	
	
	protected String[][] getRows() {
		
		int rowCount = variables_.size();
		String[][] rows = new String[rowCount][];

		
		for (int i = 0; i < rowCount; i++) {
			ScalarVariableReal sv = variables_.get(i);
			rows[i] = sv.toStringArray();
		}

		return rows;
	
	}


	protected String[] getColumns() {
		return ScalarVariableReal.getColumnNamesArray();
	}


	public Vector<ScalarVariableReal> getVariables() {
		return variables_;
	}
	


	public void addResult(ScalarValueCollection scalarValueCollection) {
		
		latestValues_ = scalarValueCollection;
		
		Vector<String> list = latestValues_.getStringList();
		int len = list.size();

		for (int i = 0; i < len; i++) {
			String str = list.get(i);
			tableModel_.setValueAt(str, i, 1);
		}

	}



	public ScalarValueReal getValueAt(int idx) {
		
		if (latestValues_ != null) {
			ScalarValueReal val = latestValues_.getRealList().get(idx);
			return val;
		} else {
			return null;
		}
		
		
	}
	
}

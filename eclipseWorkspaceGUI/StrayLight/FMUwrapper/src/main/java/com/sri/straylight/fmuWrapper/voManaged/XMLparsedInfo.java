package com.sri.straylight.fmuWrapper.voManaged;




import java.util.Vector;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLparsed.
 */
public class XMLparsedInfo implements JsonSerializable  {

	/** The scalar variables all_. */
	private ScalarVariablesAll scalarVariablesAll_;
	
	
	/**
	 * Instantiates a new xM lparsed.
	 */
	public XMLparsedInfo() {

	}
	
	/**
	 * Instantiates a new xmlparsed.
	 *
	 * @param scalarVariablesAll the scalar variables all
	 */
	public XMLparsedInfo(ScalarVariablesAll scalarVariablesAll) {
		scalarVariablesAll_ = scalarVariablesAll;	
	}
	
	
	/**
	 * Gets the scalar variables all.
	 *
	 * @return the scalar variables all
	 */
	public ScalarVariablesAll getScalarVariablesAll() {
		
		return scalarVariablesAll_;
	}
	


	
	/**
	 * Gets the input vars.
	 *
	 * @return the input vars
	 */
	public Vector<ScalarVariableReal> getInputVars() {
		
		Vector<ScalarVariableReal> realVarList = scalarVariablesAll_.getInput().getRealVarList();
		
		return realVarList;
	}
	
	
	/**
	 * Gets the output column names.
	 *
	 * @return the output column names
	 */
	public String[] getOutputColumnNames() {
		
		Vector<ScalarVariableReal> realVarList = scalarVariablesAll_.getOutput().getRealVarList();
		

		int len = realVarList.size() + 1;
		
		String[] columnNames = new String[len];
		columnNames[0] = "time";
		for (int i =1; i < len; i++) {
			columnNames[i] = realVarList.get(i-1).getName();
		}
		
		return columnNames;
	}
	
	/**
	 * Gets the output form column names.
	 *
	 * @return the output form column names
	 */
	public String[] getOutputFormColumnNames() {

		String[] columnNames = ScalarVariableReal.getColumnNamesArray();
		return columnNames;
	}
	
	
	/**
	 * Gets the internal column names.
	 *
	 * @return the internal column names
	 */
	public String[] getInternalColumnNames() {

		String[] columnNames  = {
				"name",
				"value",
				"type",
				"causality",
				"variability",
				"description"
		};
		
		return columnNames;
	}
	
	/**
	 * Gets the input form column names.
	 *
	 * @return the input form column names
	 */
	public String[] getInputFormColumnNames() {

		String[] columnNames = ScalarVariableReal.getColumnNamesArray();
		return columnNames;
	}
	
	/**
	 * Gets the output data.
	 *
	 * @return the output data
	 */
	public Object[][] getOutputData () {
		
		Vector<ScalarVariableReal> realVarList = scalarVariablesAll_.getOutput().getRealVarList();
		int len = realVarList.size();
		
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableReal sv = realVarList.get(i);
			String[] row  = {
				sv.getName(),
				"Real",
				sv.getCausalityEnum().toString(),
				sv.getVariabilityEnum().toString(),
				sv.getDescription()
			};
			
			data[i] = row;
		}

		return data;
	}
	
	/**
	 * Gets the output form data.
	 *
	 * @return the output form data
	 */
	public Object[][] getOutputFormData () {
		
		Vector<ScalarVariableReal> realVarList = scalarVariablesAll_.getOutput().getRealVarList();
		
		int len = realVarList.size();
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableReal sv = realVarList.get(i);
			data[i] = sv.toStringArray();
		}

		return data;
	}
	
	
	/**
	 * Gets the internal data.
	 *
	 * @return the internal data
	 */
	public Object[][] getInternalData () {
		
		Vector<ScalarVariableReal> realVarList = scalarVariablesAll_.getInternal().getRealVarList();
		
		int len = realVarList.size();
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableReal sv = realVarList.get(i);
			
			String[] row  = {
				sv.getName(),
				"unknown",
				"Real",
				sv.getCausalityEnum().toString(),
				sv.getVariabilityEnum().toString(),
				sv.getDescription()
			};
			
			data[i] = row;
		}

		return data;
	}
	

	
	/**
	 * Gets the input data.
	 *
	 * @return the input data
	 */
	public Object[][] getInputData() {
		
		Vector<ScalarVariableReal> realVarList = scalarVariablesAll_.getOutput().getRealVarList();
		
		int len = realVarList.size();
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableReal sv = realVarList.get(i);
			data[i] = sv.toStringArray();
		}

		return data;
	}


	public String toJson() {
		return JsonController.getInstance().toJson(this);
	}
	

}

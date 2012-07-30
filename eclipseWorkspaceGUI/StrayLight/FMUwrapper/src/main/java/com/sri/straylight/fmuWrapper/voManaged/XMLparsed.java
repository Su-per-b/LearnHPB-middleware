package com.sri.straylight.fmuWrapper.voManaged;




import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;
import com.sun.jna.Structure;

public class XMLparsed extends Structure {

	private String[] outputVarNames;

	public ScalarVariableRealStruct[] inputVars;
	public ScalarVariableRealStruct[] outputVars;
	public ScalarVariableRealStruct[] internalVars;

	
	public String[] getOutputColumnNames() {

		int len = outputVars.length + 1;
		
		String[] columnNames = new String[len];
		columnNames[0] = "time";
		for (int i =1; i < len; i++) {
			columnNames[i] = outputVars[i-1].name;
		}
		
		return columnNames;
	}
	
	public String[] getOutputFormColumnNames() {

		String[] columnNames  = {
				"name",
				"value",
				"type",
				"start",
				"nominal",
				"min",
				"max",
				"description"
		};
		
		return columnNames;
	}
	
	
	public String[] getInternalColumnNames() {

		String[] columnNames  = {
				"name",
				"value",
				"type",
				"causality",
				"description"
		};
		
		return columnNames;
	}
	
	public String[] getInputFormColumnNames() {

		String[] columnNames  = {
				"name",
				"value",
				"start",
				"nominal",
				"min",
				"max",
				"causality",
				"description"
		};
		
		return columnNames;
	}
	
	public Object[][] getOutputData () {
		
		int len = outputVars.length;
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableRealStruct sv = outputVars[i];
			String[] row  = {
				sv.name,
				"Real",
				sv.description
			};
			
			data[i] = row;
		}

		return data;
	}
	
	public Object[][] getOutputFormData () {
		
		int len = outputVars.length;
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableRealStruct sv = outputVars[i];
			String[] row  = {
					sv.name,
					"{not set}",
					"Real",
					Double.toString(sv.typeSpecReal.start),
					Double.toString(sv.typeSpecReal.nominal),
					Double.toString(sv.typeSpecReal.min),
					Double.toString(sv.typeSpecReal.max),
					sv.description
			};
			
			data[i] = row;
		}

		return data;
	}
	
	
	public Object[][] getInternalData () {
		
		int len = internalVars.length;
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableRealStruct sv = internalVars[i];
			String[] row  = {
				sv.name,
				"unknown",
				"Real",
				sv.getCausalityEnum().toString(),
				sv.description
			};
			
			data[i] = row;
		}

		return data;
	}
	

	
	public Object[][] getInputData() {
		
		int len = inputVars.length;
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableRealStruct sv = inputVars[i];
			String[] row  = {
				sv.name,
				"{unknown}",
				Double.toString(sv.typeSpecReal.start),
				Double.toString(sv.typeSpecReal.nominal),
				Double.toString(sv.typeSpecReal.min),
				Double.toString(sv.typeSpecReal.max),
				sv.getCausalityEnum().toString(),
				sv.description
			};
			
			data[i] = row;
		}

		return data;
	}



}

package com.sri.straylight.fmuWrapper.voManaged;




import com.sri.straylight.fmuWrapper.voNative.MetaDataStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;
import com.sun.jna.Structure;

public class Initialized extends Structure {

	public String[] outputVarNames;

	public ScalarVariableStruct[] inputVars;
	public ScalarVariableStruct[] outputVars;
	public ScalarVariableStruct[] internalVars;

	

	public String[] getColumnNames() {

		String[] columnNames  = {
				"name",
				"value",
				"type",
				"causality",
				"description"
		};
		
		return columnNames;
	}
	
	public String[] getInputColumnNames() {

		String[] columnNames  = {
				"name",
				"type",
				"start",
				"causality",
				"description"
		};
		
		return columnNames;
	}
	
	private Object[][] getData (ScalarVariableStruct[] ary ) {
		
		int len = ary.length;
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableStruct sv = ary[i];
			String[] row  = {
				sv.name,
				"unknown",
				sv.getTypeEnum().toString(),
				sv.getCausalityEnum().toString(),
				sv.description
			};
			
			data[i] = row;
		}

		return data;
	}
	

	public Object[][] getInternalData() {
		Object[][] data = getData(internalVars);
		return data;
	}
	
	
	
	public Object[][] getInputData() {
		
		int len = inputVars.length;
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableStruct sv = inputVars[i];
			String[] row  = {
				sv.name,
				sv.getTypeEnum().toString(),
				"start",
				sv.getCausalityEnum().toString(),
				sv.description
			};
			
			data[i] = row;
		}

		return data;
	}



}

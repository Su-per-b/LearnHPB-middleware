package com.sri.straylight.fmuWrapper.voManaged;




import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;
import com.sun.jna.Structure;

public class InitializedInfo extends Structure {

	public String[] columnNames;

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
		Object[][] data = getData(inputVars);
		return data;
	}



}

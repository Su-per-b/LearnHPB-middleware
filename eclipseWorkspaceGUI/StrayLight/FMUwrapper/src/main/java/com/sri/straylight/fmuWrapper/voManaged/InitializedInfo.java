package com.sri.straylight.fmuWrapper.voManaged;




import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStruct;
import com.sun.jna.Structure;

public class InitializedInfo extends Structure {

	public String[] columnNames;

	public ScalarVariableStruct[] inputVars;
	public ScalarVariableStruct[] outputVars;


	public String[] getInputColumnNames() {

		String[] columnNames2  = {
				"name",
				"value",
				"type",
				"cuasality",
				"description"
		};
		
		return columnNames2;
	}

	public Object[][] getInputData() {

		
		int len = inputVars.length;
		
		Object[][] data = new Object[len][];

			
		for (int i = 0; i < len; i++) {
			String[] row  = {
					inputVars[i].name,
					"unknown",
					"unknown",
					inputVars[i].getCausalityEnum().toString(),
					"unknown"
			};
			
			data[i] = row;
		}

		

		return data;
	}



}

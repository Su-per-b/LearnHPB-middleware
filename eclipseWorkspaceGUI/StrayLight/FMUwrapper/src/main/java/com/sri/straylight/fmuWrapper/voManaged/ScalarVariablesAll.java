package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.ScalarVariableCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;

public class ScalarVariablesAll {

	public ScalarVariableCollection input;
	public ScalarVariableCollection output;
	public ScalarVariableCollection internal;
	
	
	
	public ScalarVariablesAll(ScalarVariablesAllStruct struct) {
		

		input = new ScalarVariableCollection(struct.input);
		output = new ScalarVariableCollection(struct.output);
		internal = new ScalarVariableCollection(struct.internal);
		 
	}
	
	
}

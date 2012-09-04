package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariablesAll.
 */
public class ScalarVariablesAll {

	/** The input. */
	public ScalarVariableCollection input;
	
	/** The output. */
	public ScalarVariableCollection output;
	
	/** The internal. */
	public ScalarVariableCollection internal;
	
	
	
	/**
	 * Instantiates a new scalar variables all.
	 *
	 * @param struct the struct
	 */
	public ScalarVariablesAll(ScalarVariablesAllStruct struct) {
		

		input = new ScalarVariableCollection(struct.input);
		output = new ScalarVariableCollection(struct.output);
		internal = new ScalarVariableCollection(struct.internal);
		 
	}
	
	
}

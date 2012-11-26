package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariablesAll.
 */
public class ScalarVariablesAll {

	/** The input. */
	private ScalarVariableCollection input_;
	
	/** The output. */
	private ScalarVariableCollection output_;
	
	/** The internal. */
	private ScalarVariableCollection internal_;
	
	
	
	/**
	 * Instantiates a new scalar variables all.
	 *
	 * @param struct the struct
	 */
	public ScalarVariablesAll(ScalarVariablesAllStruct struct) {
		input_ = new ScalarVariableCollection(struct.input);
		output_ = new ScalarVariableCollection(struct.output);
		internal_ = new ScalarVariableCollection(struct.internal, 100);
	}


	public ScalarVariablesAll() {
		
	}


	/**
	 * @return the input_
	 */
	public ScalarVariableCollection getInput() {
		return input_;
	}


	/**
	 * @param input_ the input_ to set
	 */
	public void setInput(ScalarVariableCollection input_) {
		this.input_ = input_;
	}
	
	
	/**
	 * @return the output_
	 */
	public ScalarVariableCollection getOutput() {
		return output_;
	}
	
	/**
	 * @return the internal_
	 */
	public ScalarVariableCollection getInternal() {
		return internal_;
	}
	
}

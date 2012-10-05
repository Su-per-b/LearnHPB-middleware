package com.sri.straylight.fmuWrapper.voManaged;




import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLparsed.
 */
public class XMLparsed  {

	/** The output var names. */
	private String[] outputVarNames;

	/** The input vars. */
	private ScalarVariableRealStruct[] inputVars;
	
	/** The output vars. */
	private ScalarVariableRealStruct[] outputVars;
	
	/** The internal vars. */
	private ScalarVariableRealStruct[] internalVars;

	/** The scalar variables all_. */
	private ScalarVariablesAll scalarVariablesAll_;
	
	
	/**
	 * Instantiates a new xM lparsed.
	 */
	public XMLparsed() {

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
	 * Instantiates a new xM lparsed.
	 *
	 * @param scalarVariablesAll the scalar variables all
	 */
	public XMLparsed(ScalarVariablesAll scalarVariablesAll) {
		scalarVariablesAll_ = scalarVariablesAll;
		
		inputVars = scalarVariablesAll.input.realValue;
		outputVars = scalarVariablesAll.output.realValue;
		internalVars = scalarVariablesAll.internal.realValue;
			
	}

	
	/**
	 * Gets the input vars.
	 *
	 * @return the input vars
	 */
	public ScalarVariableRealStruct[] getInputVars() {
		
		return inputVars;
	}
	
	
	/**
	 * Gets the output column names.
	 *
	 * @return the output column names
	 */
	public String[] getOutputColumnNames() {

		int len = outputVars.length + 1;
		
		String[] columnNames = new String[len];
		columnNames[0] = "time";
		for (int i =1; i < len; i++) {
			columnNames[i] = outputVars[i-1].name;
		}
		
		return columnNames;
	}
	
	/**
	 * Gets the output form column names.
	 *
	 * @return the output form column names
	 */
	public String[] getOutputFormColumnNames() {
		
		String[] columnNames = ScalarVariableRealStruct.getColumnNamesArray();
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

		String[] columnNames = ScalarVariableRealStruct.getColumnNamesArray();
		return columnNames;
	}
	
	/**
	 * Gets the output data.
	 *
	 * @return the output data
	 */
	public Object[][] getOutputData () {
		
		int len = outputVars.length;
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableRealStruct sv = outputVars[i];
			String[] row  = {
				sv.name,
				"Real",
				sv.getCausalityEnum().toString(),
				sv.getVariabilityEnum().toString(),
				sv.description
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
		
		int len = outputVars.length;
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableRealStruct sv = outputVars[i];
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
		
		int len = internalVars.length;
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableRealStruct sv = internalVars[i];
			String[] row  = {
				sv.name,
				"unknown",
				"Real",
				sv.getCausalityEnum().toString(),
				sv.getVariabilityEnum().toString(),
				sv.description
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
		
		int len = inputVars.length;
		Object[][] data = new Object[len][];

		for (int i = 0; i < len; i++) {
			ScalarVariableRealStruct sv = inputVars[i];
			data[i] = sv.toStringArray();
		}

		return data;
	}



}

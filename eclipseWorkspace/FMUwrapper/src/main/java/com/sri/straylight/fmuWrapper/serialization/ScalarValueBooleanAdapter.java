
package com.sri.straylight.fmuWrapper.serialization;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueBoolean;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarValueBooleanAdapter 
	extends AdapterBase<ScalarValueBoolean> {

	
	final protected String[][] fieldNamesEx_ = { 
			{ "idx_", "i" },
			{ "value_", "v" }
		};

	
	public ScalarValueBooleanAdapter() {
		super(ScalarValueBoolean.class);
		super.init(fieldNamesEx_);
		
	}
	

}

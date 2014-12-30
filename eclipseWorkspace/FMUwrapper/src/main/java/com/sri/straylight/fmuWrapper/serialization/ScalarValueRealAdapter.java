package com.sri.straylight.fmuWrapper.serialization;

import com.sri.straylight.fmuWrapper.voManaged.ScalarValueReal;


/**
 * The Class ResultEventAdapter.
 */
public class ScalarValueRealAdapter extends AdapterBase<ScalarValueReal> {


	final protected String[][] fieldNamesEx_ = { 
			{ "idx_", "i" },
			{ "value_", "v" }
		};
	
	public ScalarValueRealAdapter() {
		

		super(ScalarValueReal.class);
		super.init(fieldNamesEx_);

	}


	
}

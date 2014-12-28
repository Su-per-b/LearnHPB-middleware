package com.sri.straylight.fmuWrapper.serialization;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;

public class ScalarValueRealStructAdapter extends
	AdapterBase<ScalarValueRealStruct> {


	final protected String[][] fieldNamesEx_ = { 
			{ "idx", "i" },
			{ "value", "v" }
	};
	
	public ScalarValueRealStructAdapter() {
		
		super(ScalarValueRealStruct.class);
		super.init(fieldNamesEx_);

	}


}

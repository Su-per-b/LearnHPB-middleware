package com.sri.straylight.fmuWrapper.serialization;

import com.sri.straylight.fmuWrapper.voManaged.StringPrimitive;


public class StringPrimitiveAdapter extends AdapterBase<StringPrimitive> {
	
	final protected String[][] fieldNamesEx_ = { 
			{ "value_", "v" }
	};

	
	public StringPrimitiveAdapter() {
		
		super(StringPrimitive.class);
		super.init(fieldNamesEx_);
		
	}

	
}

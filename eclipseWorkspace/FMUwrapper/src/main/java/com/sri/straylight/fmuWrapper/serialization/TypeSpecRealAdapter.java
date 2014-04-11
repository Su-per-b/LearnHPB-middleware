package com.sri.straylight.fmuWrapper.serialization;

import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;

public class TypeSpecRealAdapter extends AdapterBase<TypeSpecReal> {
	
	final protected String[] fieldNames_ = { 
			"start",  "nominal", "min", "max", "unit"
	};

	
	public TypeSpecRealAdapter() {
		
		super(TypeSpecReal.class);
		super.init(fieldNames_);
		
	}



	
	
}

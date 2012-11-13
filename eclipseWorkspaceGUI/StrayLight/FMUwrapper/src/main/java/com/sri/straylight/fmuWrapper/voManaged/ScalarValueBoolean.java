package com.sri.straylight.fmuWrapper.voManaged;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueBooleanStruct;

public class ScalarValueBoolean extends BaseScalarValue {
	
	
	/** The string. */
	private Boolean value_;
	
	
	/**
	 * Instantiates a new scalar value.
	 */
	public ScalarValueBoolean(int i, Boolean v) {
		super(i);
		value_ = v;
	}
	
	
	public ScalarValueBoolean(ScalarValueBooleanStruct boolStruct) {
		super(boolStruct.idx);
		value_ = boolStruct.value;
	}


	public String toString() {
		
		return Boolean.toString(value_);
		
	}
	
	

}

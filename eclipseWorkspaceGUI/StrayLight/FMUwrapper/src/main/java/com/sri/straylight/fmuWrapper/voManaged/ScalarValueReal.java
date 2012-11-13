package com.sri.straylight.fmuWrapper.voManaged;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;

public class ScalarValueReal extends BaseScalarValue {
	
	public static final int PRECISION = 3;
	
	/** The string. */
	private Double value_;
	
	
	
	/**
	 * Instantiates a new scalar value.
	 */
	public ScalarValueReal(int i, Double v) {
		super(i);
		value_ = v;
	}
	
	
	public ScalarValueReal(ScalarValueRealStruct realStruct) {
		super(realStruct.idx);
		value_ = realStruct.value;
	}


	
	public Double getValue() {
		return value_;
	}
	
	
	public String toString() {
		
		BigDecimal bd = new BigDecimal(value_).setScale(
				ScalarValueReal.PRECISION, RoundingMode.HALF_UP);
		
		return bd.toString();
	}
	
	

}

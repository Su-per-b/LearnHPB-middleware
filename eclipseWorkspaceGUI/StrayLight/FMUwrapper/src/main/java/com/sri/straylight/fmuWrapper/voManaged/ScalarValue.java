package com.sri.straylight.fmuWrapper.voManaged;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;


// TODO: Auto-generated Javadoc
/**
 * The Class ScalarValue.
 */
public class ScalarValue<T> extends BaseScalarValue {
		
	
	/** The string. */
	private T value_;
	
	
	/**
	 * Instantiates a new scalar value.
	 */
	public ScalarValue(int i, T v) {
		super(i);
		value_ = v;
	}
	
	

	



	
}

package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarValue.
 */
public class ScalarValue {
	
	/** The idx. */
	public int idx;
	
	/** The string. */
	public String string;
	
	
	/**
	 * Instantiates a new scalar value.
	 */
	public ScalarValue() {
		
	}
	
	
	/**
	 * Instantiates a new scalar value.
	 *
	 * @param struct the struct
	 */
	public ScalarValue(ScalarValueStruct struct) {
		this.idx = struct.idx;
		this.string = struct.string;
	}
	
	
}

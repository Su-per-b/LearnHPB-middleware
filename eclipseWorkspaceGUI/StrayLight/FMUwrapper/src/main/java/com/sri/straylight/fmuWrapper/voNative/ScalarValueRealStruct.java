package com.sri.straylight.fmuWrapper.voNative;



import java.math.BigDecimal;
import java.math.RoundingMode;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarValueRealStruct.
 */
public class ScalarValueRealStruct extends Structure {
	
	public static class ByReference extends ScalarValueRealStruct implements Structure.ByReference { }
	
	/** The idx. */
	public int idx;
	
	/** The value. */
	public double value;
	  
	
	/**
	 * Gets the string.
	 *
	 * @return the string
	 */
	public String toString() {
		
		BigDecimal bd = new BigDecimal(value).setScale(3, RoundingMode.HALF_UP);
		
		return bd.toString();
		
	}
	
	
	
	
}

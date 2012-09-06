package com.sri.straylight.fmuWrapper.voNative;



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

		return Double.toString(value);
		
	}
	
	
}

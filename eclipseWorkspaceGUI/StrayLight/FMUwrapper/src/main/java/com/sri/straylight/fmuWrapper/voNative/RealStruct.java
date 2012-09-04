package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class RealStruct.
 */
public class RealStruct extends Structure {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends RealStruct implements Structure.ByReference { }
	
	/** The min. */
	public double min;
	
	/** The max. */
	public double max;
	
	/** The nominal. */
	public double nominal;
	
	/** The start. */
	public double start;
	
}

	

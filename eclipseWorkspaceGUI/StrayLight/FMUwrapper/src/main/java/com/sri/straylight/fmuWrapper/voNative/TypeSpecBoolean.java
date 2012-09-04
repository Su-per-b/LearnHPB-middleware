package com.sri.straylight.fmuWrapper.voNative;


import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeSpecBoolean.
 */
public class TypeSpecBoolean extends Structure {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends TypeSpecBoolean implements Structure.ByReference { }
	
	/** The start. */
	public byte start;
	
	/** The fixed. */
	public byte fixed;

	/** The start value status. */
	public int startValueStatus;
	
	/** The fixed value status. */
	public int fixedValueStatus;

	/** The declared type. */
	public String declaredType;
	
}

package com.sri.straylight.fmuWrapper.voNative;



import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableRealStruct.
 */
public class ScalarVariableRealStruct extends ScalarVariableStructBase {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends ScalarVariableRealStruct implements Structure.ByReference { }
	
	/** The type spec real. */
	public TypeSpecReal.ByReference typeSpecReal;

	public String[] toStringArray() {
		
		String[] ary  = {
				name,
				"{not set}",
				"Real",
				doubleToString(typeSpecReal.start),
				doubleToString(typeSpecReal.nominal),
				doubleToString(typeSpecReal.min),
				doubleToString(typeSpecReal.max),
				getCausalityEnum().toString(),
				getVariabilityEnum().toString(),
				description
		};
		
		return ary;
		
	}

	public static String[] getColumnNamesArray() {
		
		String[] ary  = {
				"name",
				"value",
				"type",
				"start",
				"nominal",
				"min",
				"max",
				"causality",
				"variability",
				"description"
		};
		
		return ary;
		
	}
	




}

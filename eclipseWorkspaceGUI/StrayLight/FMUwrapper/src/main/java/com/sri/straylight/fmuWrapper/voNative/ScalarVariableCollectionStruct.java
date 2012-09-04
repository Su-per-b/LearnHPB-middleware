package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableCollectionStruct.
 */
public class ScalarVariableCollectionStruct extends Structure {
	
	/**
	 * The Class ByReference.
	 */
	public static class ByReference extends ScalarVariableCollectionStruct implements Structure.ByReference { }
	
	/** The real value. */
	public ScalarVariableRealStruct.ByReference realValue;
	
	/** The real size. */
	public int realSize;
	
	/** The boolean value. */
	public ScalarVariableBooleanStruct.ByReference booleanValue;
	
	/** The boolean size. */
	public int booleanSize;
	
	/** The integer value. */
	public ScalarVariableIntegerStruct.ByReference integerValue;
	
	/** The integer size. */
	public int integerSize;
	
	/** The enumeration value. */
	public ScalarVariableEnumerationStruct.ByReference enumerationValue;
	
	/** The enumeration size. */
	public int enumerationSize;
	
	/** The string value. */
	public ScalarVariableStringStruct.ByReference stringValue;
	
	/** The string size. */
	public int stringSize;
	
	
	/**
	 * Gets the real as array.
	 *
	 * @param maxSize the max size
	 * @return the real as array
	 */
	public ScalarVariableRealStruct[] getRealAsArray(int maxSize) {
		
		int arraySize = realSize;
		
		if (maxSize > 0) { //check if maxSize should be enforced
			if (arraySize > maxSize) { //check if array size exceeds maximum
				arraySize = maxSize;  //array size exceeds maximum therefore limit to max
			}
		}
		
		if (arraySize > 0) {
			ScalarVariableRealStruct[] ary = (ScalarVariableRealStruct[]) realValue.toArray(arraySize);
			return ary;
		} else {
			return null;
		}
		
	}
	
	
	
	/**
	 * Gets the boolean as array.
	 *
	 * @param maxSize the max size
	 * @return the boolean as array
	 */
	public ScalarVariableBooleanStruct[] getBooleanAsArray(int maxSize) {

		int arraySize = booleanSize;
		
		if (maxSize > 0) { //check if maxSize should be enforced
			if (arraySize > maxSize) { //check if array size exceeds maximum
				arraySize = maxSize;  //array size exceeds maximum therefore limit to max
			}
		}
		
		if (arraySize > 0) {
			ScalarVariableBooleanStruct[] ary = (ScalarVariableBooleanStruct[]) booleanValue.toArray(arraySize);
			return ary;
		} else {
			return null;
		}
		
	}
	
	/**
	 * Gets the integer as array.
	 *
	 * @param maxSize the max size
	 * @return the integer as array
	 */
	public ScalarVariableIntegerStruct[] getIntegerAsArray(int maxSize) {
		

		int arraySize = integerSize;
		
		if (maxSize > 0) { //check if maxSize should be enforced
			if (arraySize > maxSize) { //check if array size exceeds maximum
				arraySize = maxSize;  //array size exceeds maximum therefore limit to max
			}
		}
		
		if (arraySize > 0) {
			ScalarVariableIntegerStruct[] ary = (ScalarVariableIntegerStruct[]) integerValue.toArray(arraySize);
			return ary;
		} else {
			return null;
		}
		
	}
	
	/**
	 * Gets the enumeration as array.
	 *
	 * @param maxSize the max size
	 * @return the enumeration as array
	 */
	public ScalarVariableEnumerationStruct[] getEnumerationAsArray(int maxSize) {
		

		int arraySize = enumerationSize;
		
		if (maxSize > 0) { //check if maxSize should be enforced
			if (arraySize > maxSize) { //check if array size exceeds maximum
				arraySize = maxSize;  //array size exceeds maximum therefore limit to max
			}
		}
		
		
		if (arraySize > 0) {
			ScalarVariableEnumerationStruct[] ary = (ScalarVariableEnumerationStruct[]) enumerationValue.toArray(arraySize);
			return ary;
		} else {
			return null;
		}
		
		
	}
	
	/**
	 * Gets the string as array.
	 *
	 * @param maxSize the max size
	 * @return the string as array
	 */
	public ScalarVariableStringStruct[] getStringAsArray(int maxSize) {
		

		int arraySize = stringSize;
		
		if (maxSize > 0) { //check if maxSize should be enforced
			if (arraySize > maxSize) { //check if array size exceeds maximum
				arraySize = maxSize;  //array size exceeds maximum therefore limit to max
			}
		}
		
		
		if (arraySize > 0) {
			ScalarVariableStringStruct[] ary = (ScalarVariableStringStruct[]) stringValue.toArray(arraySize);
			return ary;
		} else {
			return null;
		}
		
	}
	
	
}

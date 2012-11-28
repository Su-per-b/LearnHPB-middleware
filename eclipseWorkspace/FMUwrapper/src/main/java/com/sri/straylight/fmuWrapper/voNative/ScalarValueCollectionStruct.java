package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class ScalarValueCollectionStruct extends Structure {
	
	
	public static class ByReference extends ScalarValueCollectionStruct implements Structure.ByReference { }
	
	public ScalarValueRealStruct.ByReference realValue;
	public int realSize;
	
	public ScalarValueBooleanStruct.ByReference booleanValue;
	public int booleanSize;
	
	public double time;
	
	/**
	 * Gets the real as array.
	 *
	 * @param maxSize the max size
	 * @return the real as array
	 */
	public ScalarValueRealStruct[] getRealAsArray(int maxSize) {
		
		int arraySize = realSize;
		
		if (maxSize > 0) { //check if maxSize should be enforced
			if (arraySize > maxSize) { //check if array size exceeds maximum
				arraySize = maxSize;  //array size exceeds maximum therefore limit to max
			}
		}
		
		if (arraySize > 0) {
			ScalarValueRealStruct[] ary = (ScalarValueRealStruct[]) realValue.toArray(arraySize);
			return ary;
		} else {
			return null;
		}
		
	}


	public ScalarValueBooleanStruct[] getBooleanAsArray(int maxSize) {
		
		int arraySize = booleanSize;
		
		if (maxSize > 0) { //check if maxSize should be enforced
			if (arraySize > maxSize) { //check if array size exceeds maximum
				arraySize = maxSize;  //array size exceeds maximum therefore limit to max
			}
		}
		
		if (arraySize > 0) {
			ScalarValueBooleanStruct[] ary = (ScalarValueBooleanStruct[]) booleanValue.toArray(arraySize);
			return ary;
		} else {
			return null;
		}
		
	}
	
	
}

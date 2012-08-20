package com.sri.straylight.fmuWrapper.voNative;

import com.sun.jna.Structure;

public class ScalarVariableCollectionStruct extends Structure {
	
	public static class ByReference extends ScalarVariableCollectionStruct implements Structure.ByReference { }
	
	public ScalarVariableRealStruct.ByReference realValue;
	public int realSize;
	public ScalarVariableBooleanStruct.ByReference booleanValue;
	public int booleanSize;
	public ScalarVariableIntegerStruct.ByReference integerValue;
	public int integerSize;
	public ScalarVariableEnumerationStruct.ByReference enumerationValue;
	public int enumerationSize;
	public ScalarVariableStringStruct.ByReference stringValue;
	public int stringSize;
	
	
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

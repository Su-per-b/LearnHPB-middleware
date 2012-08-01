package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.ScalarVariableBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableCollectionStruct.ByReference;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableEnumerationStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableIntegerStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStringStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStructBase;

public class ScalarVariableCollection {
	

	public ScalarVariableRealStruct[] realValue;
	public ScalarVariableBooleanStruct[] booleanValue;
	public ScalarVariableIntegerStruct[] integerValue;
	public ScalarVariableEnumerationStruct[] enumerationValue;
	public ScalarVariableStringStruct[] stringValue;
	
	public int maxArraySize = 100;
	public int totalSize;
	
	public ScalarVariableStructBase[] allValues;
	private int allValuesLen;
	
	public ScalarVariableCollection(ScalarVariableCollectionStruct.ByReference struct) {
		// TODO Auto-generated constructor stub
		
		realValue = struct.getRealAsArray(maxArraySize);
		
		if (realValue != null) {
			totalSize += realValue.length;
		}

		
		booleanValue = struct.getBooleanAsArray(maxArraySize);
		if (booleanValue != null) {
			totalSize += booleanValue.length;
		}
		
		integerValue = struct.getIntegerAsArray(maxArraySize);
		if (integerValue != null) {
			totalSize += integerValue.length;
		}
		
		enumerationValue = struct.getEnumerationAsArray(maxArraySize);
		if (enumerationValue != null) {
			totalSize += enumerationValue.length;
		}
		
		stringValue = struct.getStringAsArray(maxArraySize);
		if (stringValue != null) {
			totalSize += stringValue.length;
		}
		
		allValuesLen = 0;
		allValues = new ScalarVariableStructBase[totalSize];
		add(realValue);
		add(booleanValue);
		add(integerValue);
		add(enumerationValue);
		add(stringValue);
		
		
	}
	
	
	private void add(ScalarVariableStructBase[] ary) {
			
		if (ary == null) return;
		
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			allValues[allValuesLen] = ary[i];
			allValuesLen++;
		}
		
	}
	
	
	
}

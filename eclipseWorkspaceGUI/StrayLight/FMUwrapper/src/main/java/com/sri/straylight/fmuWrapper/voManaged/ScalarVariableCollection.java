package com.sri.straylight.fmuWrapper.voManaged;

import com.sri.straylight.fmuWrapper.voNative.ScalarVariableBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableEnumerationStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableIntegerStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStringStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableStructBase;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableCollection.
 */
public class ScalarVariableCollection {
	

	/** The real value. */
	public ScalarVariableRealStruct[] realValue;
	
	/** The boolean value. */
	public ScalarVariableBooleanStruct[] booleanValue;
	
	/** The integer value. */
	public ScalarVariableIntegerStruct[] integerValue;
	
	/** The enumeration value. */
	public ScalarVariableEnumerationStruct[] enumerationValue;
	
	/** The string value. */
	public ScalarVariableStringStruct[] stringValue;
	
	/** The max array size. */
	public int maxArraySize = 100;
	
	/** The total size. */
	public int totalSize;
	
	/** The all values. */
	public ScalarVariableStructBase[] allValues;
	
	/** The all values len. */
	private int allValuesLen;
	
	/**
	 * Instantiates a new scalar variable collection.
	 *
	 * @param struct the struct
	 */
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
	
	
	/**
	 * Adds the.
	 *
	 * @param ary the ary
	 */
	private void add(ScalarVariableStructBase[] ary) {
			
		if (ary == null) return;
		
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			allValues[allValuesLen] = ary[i];
			allValuesLen++;
		}
		
	}
	
	
	
}

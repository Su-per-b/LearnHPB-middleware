package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;

public class ScalarValueCollection {
	
	
	public ScalarValueRealStruct realValueAry[];
	public ScalarValueBooleanStruct booleanValueAry[];
	
	/**
	 * Instantiates a new ScalarValueCollection from a struct.
	 *
	 * @param st1 the struct
	 */
	public ScalarValueCollection(ScalarValueCollectionStruct st1) {
		
		realValueAry = st1.getRealAsArray(0);
		booleanValueAry = st1.getBooleanAsArray(0);

	}
	
	
	
	public String toString() {
		
		Vector<String> list = getStringList();
		String str = list.toString();
				
		return str;
	}



	public Vector<String> getStringList() {
		
		
		String[] strArray = new String[realValueAry.length];
		
		
		int len = realValueAry.length;

		for (int i = 0; i < len; i++) {
			
			ScalarValueRealStruct scalarValueRealStruct = realValueAry[i];
			
			strArray[i] = scalarValueRealStruct.toString();
		}

		List<String> list = Arrays.asList(strArray);
		Vector<String> vector = new Vector<String>(list);
		
		return vector;
		
	}
	
	
	
	
}

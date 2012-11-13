package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;

public class ScalarValueCollection {
	
	
	//public ScalarValue<Double> realValueAry[];
	//public ScalarValue<Boolean> booleanValueAry[];

	private Vector<ScalarValueReal> realList_;
	private Vector<ScalarValueBoolean> booleanList_;
	
	private Vector<BaseScalarValue> valueList_;
	
	
	//public Real
	
	
	/**
	 * Instantiates a new ScalarValueCollection from a struct.
	 *
	 * @param st1 the struct
	 */
	public ScalarValueCollection(ScalarValueCollectionStruct struct) {
		
		init_(struct);
	}
	
	private void init_(ScalarValueCollectionStruct struct) {
		valueList_ = new Vector<BaseScalarValue>();
		
		initReal_(struct);
		initBoolean_(struct);
		initAll_();
	}
	
	private void initAll_() {

	}
	
	
	public int size() {
		
		int size = valueList_.size();
		return size;
	}

	private void initBoolean_(ScalarValueCollectionStruct struct) {
		
		booleanList_ = new Vector<ScalarValueBoolean>();
		
		
		if (struct.booleanSize > 0) {
			
			ScalarValueBooleanStruct[] aryBoolean = struct.getBooleanAsArray(0);	
			int len = aryBoolean.length;
			
			if (struct.booleanSize != len) {
				try {
					throw new Exception("Error converting Array");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			
			for (int i = 0; i < len; i++) {
				ScalarValueBooleanStruct boolStruct = aryBoolean[i];
				
				ScalarValueBoolean sv = new ScalarValueBoolean(boolStruct);
				
				
				booleanList_.add(sv);
				valueList_.add(sv);
				
			}
			
		}

	}
	
	
	
	
	private void initReal_(ScalarValueCollectionStruct struct) {
		
		ScalarValueRealStruct[] aryReal = struct.getRealAsArray(0);	
		int len = aryReal.length;
		
		realList_ = new Vector<ScalarValueReal>();
		
		
		for (int i = 0; i < len; i++) {
			ScalarValueRealStruct realStruct = aryReal[i];
			
			ScalarValueReal sv = new ScalarValueReal(realStruct);
			
			realList_.add(sv);
			valueList_.add(sv);
		}
	}


	public BaseScalarValue get(int idx) {
		
		return valueList_.get(idx);
	}
	
	public String toString() {
		
		Vector<String> list = getStringList();
		String str = list.toString();
				
		return str;
	}



	public Vector<String> getStringList() {
		
		int len = realList_.size();

		Vector<String> vector = new Vector<String>();
		
		for (int i = 0; i < len; i++) {
			
			ScalarValueReal scalarValueReal = realList_.get(i);
			String str = scalarValueReal.toString();
			vector.add(str);
		}

		
		return vector;
		
	}
	
	
	
	
}

package com.sri.straylight.fmuWrapper.voManaged;

import java.util.Vector;

import com.sri.straylight.fmuWrapper.voNative.ScalarVariableBooleanStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarVariableCollection.
 */
public class ScalarVariableCollection {
	

	private Vector<ScalarVariableReal> realVarList_;
	
	private Vector<ScalarVariableBoolean> booleanVarList_;
	

	/** The max array size. */
	public int maxArraySize = 0;
	
	/** The total size. */
	public int totalSize;
	


	/**
	 * @return the realValue
	 */
	public Vector<ScalarVariableReal> getRealVarList() {
		return realVarList_;
	}

	/**
	 * @param realValue the realValue to set
	*/
	public void setRealVarList(Vector<ScalarVariableReal> realVarList) {
		realVarList_ = realVarList;
	}
		 
	
	
	/**
	 * Instantiates a new scalar variable collection.
	 *
	 * @param struct the struct
	 * @param maxArraySize TODO
	 * @param maxArraySize TODO
	 */
	public ScalarVariableCollection(ScalarVariableCollectionStruct.ByReference struct, int maxArraySize) {
		init(struct,maxArraySize);
	}
	
	public ScalarVariableCollection(ScalarVariableCollectionStruct.ByReference struct) {
		init(struct,0);
	}
	

	public ScalarVariableCollection() {

	}
	

	private void init(ScalarVariableCollectionStruct.ByReference struct, int maxArraySize) {
		

		ScalarVariableRealStruct[] realAry = struct.getRealAsArray(maxArraySize);
		if (realAry != null) {
			totalSize += realAry.length;
		}

		realVarList_ = ScalarVariableReal.makeList(realAry);
		
		
		ScalarVariableBooleanStruct[] booleanAry = struct.getBooleanAsArray(maxArraySize);
		if (booleanVarList_ != null) {
			totalSize += booleanAry.length;
		}
		
		booleanVarList_ = ScalarVariableBoolean.makeList(booleanAry);



	}
	

	
	
}

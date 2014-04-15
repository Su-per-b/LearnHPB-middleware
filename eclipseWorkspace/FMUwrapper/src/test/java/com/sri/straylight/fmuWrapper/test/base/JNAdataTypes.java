package com.sri.straylight.fmuWrapper.test.base;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;
import com.sun.jna.Library;



public interface JNAdataTypes extends Library {
	
	
	public ScalarVariablesAllStruct test_GetScalarVariablesAllStruct();
	
	public ScalarVariablesAllStruct test_GetScalarVariablesAllStruct2();
	
	public ScalarVariableCollectionStruct test_GetScalarVariableCollectionStruct();
	
	public ScalarValueRealStruct test_GetScalarValueRealStruct();
	
	public ScalarVariableRealStruct test_GetScalarVariableRealStruct();
	
	
}


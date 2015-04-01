package com.sri.straylight.fmuWrapper.test.base;

import com.sri.straylight.fmuWrapper.voNative.BooleanValue;
import com.sri.straylight.fmuWrapper.voNative.DisplayUnitDefinitionStruct;
import com.sri.straylight.fmuWrapper.voNative.EnumerationItem;
import com.sri.straylight.fmuWrapper.voNative.IntegerValue;
import com.sri.straylight.fmuWrapper.voNative.RealValue;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableCollectionStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariableRealStruct;
import com.sri.straylight.fmuWrapper.voNative.ScalarVariablesAllStruct;
import com.sri.straylight.fmuWrapper.voNative.StringValue;
import com.sri.straylight.fmuWrapper.voNative.TypeDefinitionBoolean;
import com.sri.straylight.fmuWrapper.voNative.TypeDefinitionEnum;
import com.sri.straylight.fmuWrapper.voNative.TypeDefinitionEnumeration;
import com.sri.straylight.fmuWrapper.voNative.TypeDefinitionInteger;
import com.sri.straylight.fmuWrapper.voNative.TypeDefinitionReal;
import com.sri.straylight.fmuWrapper.voNative.TypeDefinitionString;
import com.sri.straylight.fmuWrapper.voNative.TypeSpecReal;
import com.sri.straylight.fmuWrapper.voNative.ValueStatus;
import com.sun.jna.Library;



public interface JNAdataTypes extends Library {
	
	
	public ScalarVariablesAllStruct test_GetScalarVariablesAllStruct();
	
	public ScalarVariableCollectionStruct.ByReference test_GetScalarVariableCollectionStruct();
	
	public ScalarValueRealStruct test_GetScalarValueRealStruct();
	
	public ScalarVariableRealStruct test_GetScalarVariableRealStruct();
	
	public TypeSpecReal test_GetTypeSpecReal();
	
	public DisplayUnitDefinitionStruct test_GetDisplayUnitDefinitionStruct();
	
	public TypeDefinitionReal test_GetTypeDefinitionReal();

	public TypeDefinitionString test_GetTypeDefinitionString();

	public TypeDefinitionInteger test_GetTypeDefinitionInteger();
	
	public TypeDefinitionBoolean test_GetTypeDefinitionBoolean();

	public TypeDefinitionEnum test_GetTypeDefinitionEnum();
	
	public TypeDefinitionEnumeration test_GetTypeDefinitionEnumeration();
	
	
	public ValueStatus test_GetValueStatus();
	
	//public ValueStatus.ByValue test_GetValueStatus2();

	
	public RealValue.ByValue test_GetRealValue();
	
	public IntegerValue.ByValue test_GetIntegerValue();

	public BooleanValue.ByValue test_GetBooleanValue();
	
	public StringValue.ByValue test_GetStringValue();
	
	public EnumerationItem.ByReference test_GetEnumerationItem();
	
	
	
	//public TypeDefinitionInteger2 test_GetTypeDefinitionInteger2();
	
	

}


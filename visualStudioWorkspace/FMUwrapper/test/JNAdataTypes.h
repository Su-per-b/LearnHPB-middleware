#pragma once

#include "stdafx.h"
#include "MainController.h"
#include "structs.h"
#include "enums.h"
#include "Logger.h"
#include "ResultOfStep.h"
#include "Config.h"
#include "MainDataModel.h"



using namespace Straylight;


extern "C" DllApi ScalarVariablesAllStruct * test_GetScalarVariablesAllStruct();

extern "C" DllApi ScalarVariableCollectionStruct * test_GetScalarVariableCollectionStruct();

extern "C" DllApi ScalarValueRealStruct * test_GetScalarValueRealStruct();

extern "C" DllApi ScalarVariableRealStruct * test_GetScalarVariableRealStruct();

extern "C" DllApi TypeSpecReal * test_GetTypeSpecReal();



extern "C" DllApi DisplayUnitDefinitionStruct * test_GetDisplayUnitDefinitionStruct();

extern "C" DllApi TypeDefinitionReal * test_GetTypeDefinitionReal();

extern "C" DllApi TypeDefinitionInteger * test_GetTypeDefinitionInteger();

extern "C" DllApi TypeDefinitionString * test_GetTypeDefinitionString();

extern "C" DllApi TypeDefinitionBoolean * test_GetTypeDefinitionBoolean();

extern "C" DllApi TypeDefinitionEnumeration * test_GetTypeDefinitionEnumeration();

extern "C" DllApi ValueStatus test_GetValueStatus();

extern "C" DllApi RealValue test_GetRealValue();

extern "C" DllApi IntegerValue test_GetIntegerValue();

extern "C" DllApi BooleanValue test_GetBooleanValue();

extern "C" DllApi StringValue test_GetStringValue();


extern "C" DllApi EnumerationItem * test_GetEnumerationItem();


extern "C" DllApi TypeDefinitionEnum * test_GetTypeDefinitionEnum();




//extern "C" DllApi TypeDefinitionInteger2 * test_GetTypeDefinitionInteger2();

//extern "C" DllApi TypeDefinitionCollectionStruct * test_TypeDefinitionCollectionStruct();
//
//
//
//
//
//
//

//
//
//
//
//
//
//
//
//extern "C" DllApi ScalarVariableStringStruct * test_GetScalarVariableStringStruct();
//
//extern "C" DllApi ScalarVariableEnumerationStruct * test_GetScalarVariableEnumerationStruct();
//
//extern "C" DllApi ScalarVariableIntegerStruct * test_GetScalarVariableIntegerStruct();
//
//extern "C" DllApi ScalarVariableBooleanStruct * test_GetScalarVariableBooleanStruct();


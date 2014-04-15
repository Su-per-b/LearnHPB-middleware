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

extern "C" DllApi ScalarVariablesAllStruct * test_GetScalarVariablesAllStruct2();

extern "C" DllApi ScalarVariableCollectionStruct * test_GetScalarVariableCollectionStruct();

extern "C" DllApi ScalarValueRealStruct * test_GetScalarValueRealStruct();

extern "C" DllApi ScalarVariableRealStruct * test_GetScalarVariableRealStruct();




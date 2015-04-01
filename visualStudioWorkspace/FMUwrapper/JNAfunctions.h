/*******************************************************//**
 * @file	JNAfunctions.h
 *
 * Declares the jn afunctions class.
 *******************************************************/
#pragma once

#include "stdafx.h"
#include "MainController.h"
#include "structs.h"
#include "enums.h"
#include "Logger.h"
#include "ResultOfStep.h"
#include "Config.h"
#include "MainDataModel.h"
#include "TypeDefinitions.h"

using namespace Straylight;



extern "C" void onMessageCallback(MessageStruct *);

extern "C" DllApi int forceCleanup();

extern "C" DllApi ScalarVariablesAllStruct * getAllScalarVariables();

extern "C" DllApi int isSimulationComplete();

extern "C" DllApi ConfigStruct * getConfig();

extern "C" DllApi ScalarValueResults * getScalarValueResults();

extern "C" DllApi BaseUnitStruct * getUnitDefinitions();

extern "C" DllApi TypeDefinitionsStruct * getTypeDefinitions();

extern "C" DllApi int setConfig(ConfigStruct * configStruct);

extern "C" DllApi void connect(
	void (*messageCallbackPtr)(MessageStruct *),
	void (*resultCallbackPtr)(ScalarValueResultsStruct *),
	void (*stateChangeCallbackPtr)(SimStateNative)
	);


extern "C" DllApi void xmlParse(char *);

extern "C" DllApi void requestStateChange (SimStateNative newState);

extern "C" DllApi fmiStatus setScalarValueReal (int idx, double value);

extern "C" DllApi  void setScalarValues (ScalarValueRealStruct * scalarValueAry , int length);

extern "C" DllApi SimStateNative getSimStateNative();

extern "C" DllApi FMImodelAttributesStruct * getFMImodelAttributesStruct();

extern "C" DllApi  void setOutputVariableNames(const char ** outputVariableNamesAry, int length);

extern "C" DllApi  void setInputVariableNames(const char ** inputVariableNamesAry, int length);


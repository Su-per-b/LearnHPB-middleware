#pragma once

#include "stdafx.h"
#include "MainController.h"
#include "structs.h"
#include "enums.h"
#include "Logger.h"
#include "ResultOfStep.h"
#include "Config.h"
#include "MainDataModel.h"


void (*messageCallbackPtr_)(MessageStruct *);
void (*resultCallbackPtr_)(ResultOfStepStruct *);
void (*stateChangeCallbackPtr_)(SimStateNative );

Straylight::MainController *  mainController;

extern "C" void onMessageCallback(MessageStruct *);

extern "C" DllExport int forceCleanup();

extern "C" DllExport ScalarVariableRealStruct * getScalarVariableInputStructs();
extern "C" DllExport ScalarVariableRealStruct * getScalarVariableOutputStructs();
extern "C" DllExport ScalarVariableStruct * getScalarVariableInternalStructs();

extern "C" DllExport int getInputVariableCount();
extern "C" DllExport int getOutputVariableCount();
extern "C" DllExport int getInternalVariableCount();

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport int run();

extern "C" DllExport ConfigStruct * getConfig();

extern "C" DllExport int setConfig(ConfigStruct * configStruct);

extern "C" DllExport void connect(
	void (*messageCallbackPtr)(MessageStruct *), 
	void (*resultCallbackPtr)(ResultOfStepStruct *),
	void (*stateChangeCallbackPtr)(SimStateNative)
	);

extern "C" DllExport void xmlParse(char *);

extern "C" DllExport int init();

extern "C" DllExport void requestStateChange (SimStateNative newState);

extern "C" DllExport fmiStatus setScalarValueReal (int idx, double value);


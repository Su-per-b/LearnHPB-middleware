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

extern "C" DllExport void doOneStep();

extern "C" void onMessageCallback(MessageStruct *);

//extern "C" DllExport void end();

extern "C" DllExport int forceCleanup();

extern "C" DllExport ScalarVariableRealStruct * getScalarVariableInputStructs();
extern "C" DllExport ScalarVariableRealStruct * getScalarVariableOutputStructs();
extern "C" DllExport ScalarVariableStruct * getScalarVariableInternalStructs();

//extern "C" DllExport ScalarVariableWrapper * getScalarVariables();




extern "C" DllExport int getInputVariableCount();
extern "C" DllExport int getOutputVariableCount();
extern "C" DllExport int getInternalVariableCount();

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport int run();

extern "C" DllExport ConfigStruct * getConfig();

extern "C" DllExport void setMetaData(ConfigStruct * configStruct);

extern "C" DllExport void connect(
	void (*messageCallbackPtr)(MessageStruct *), 
	void (*resultCallbackPtr)(ResultOfStepStruct *),
    void (*stateChangeCallbackPtr)(SimStateNative)
	);

extern "C" DllExport void xmlParse(char *);

extern "C" DllExport void init();

extern "C" DllExport void requestStateChange (SimStateNative newState);

extern "C" DllExport fmiStatus changeInput (int idx, double value);


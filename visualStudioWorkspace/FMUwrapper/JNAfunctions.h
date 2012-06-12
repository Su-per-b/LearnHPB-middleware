#pragma once

#include "stdafx.h"
#include "MainController.h"
#include "structs.h"
#include "enums.h"
#include "Logger.h"

void (*messageCallbackPtr_)(MessageStruct *);

void (*resultCallbackPtr_)(ResultStruct *);

void (*stateChangeCallbackPtr_)(State );


Straylight::MainController *  mainController;

extern "C" DllExport int doOneStep();

extern "C" DllExport void deleteMessageStruct(MessageStruct *);

extern "C" void onMessageCallback(MessageStruct *);

extern "C" DllExport void end();

extern "C" DllExport int forceCleanup();

extern "C" DllExport ResultStruct * getResultStruct();

extern "C" DllExport int getVariableCount();

extern "C" DllExport ScalarVariableStruct * getScalarVariableStructs();

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport int run();

extern "C" DllExport MetaDataStruct * getMetaData();

extern "C" DllExport void setMetaData(MetaDataStruct * metaDataStruct);


extern "C" DllExport void initCallbacks(
	void (*messageCallbackPtr)(MessageStruct *), 
	void (*resultCallbackPtr)(ResultStruct *),
    void (*stateChangeCallbackPtr)(State)
	);

extern "C" DllExport void initXML(char *);

extern "C" DllExport void initSimulation();
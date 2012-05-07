#pragma once


#include "stdafx.h"
#include "FMUwrapper.h"
#include "structs.h"
#include "enums.h"
#include "Logger.h"

void (*messageCallbackPtr_)(MessageStruct *);

void (*resultCallbackPtr_)(ResultStruct *);

void (*stateChangeCallbackPtr_)(State );




Straylight::FMUwrapper *  fmuWrapper;


extern "C" DllExport int doOneStep();

extern "C" DllExport void deleteMessageStruct(MessageStruct *);

extern "C" void onMessageCallback(MessageStruct *);


extern "C" DllExport void end();

extern "C" DllExport ResultStruct * getResultStruct();

extern "C" DllExport int getVariableCount();

extern "C" DllExport struct ScalarVariableStruct * getSVmetaData();

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport int run();

extern "C" DllExport int forceCleanup();


extern "C" DllExport void init_1(
	void (*messageCallbackPtr)(MessageStruct *), 
	void (*resultCallbackPtr)(ResultStruct *),
    void (*stateChangeCallbackPtr)(State)
	);

extern "C" DllExport void init_2(char *);

extern "C" DllExport void init_3();
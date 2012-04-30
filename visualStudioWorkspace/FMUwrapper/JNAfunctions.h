#pragma once


#include "stdafx.h"
#include "FMUwrapper.h"
#include "structs.h"
#include "enums.h"


void (*messageCallbackPtr_)(MessageStruct *);

void (*resultCallbackPtr_)(ResultItemStruct *);

void (*stateChangeCallbackPtr_)(State );




Straylight::FMUwrapper *  fmuWrapper;


extern "C" DllExport int doOneStep();

extern "C" DllExport void end();

extern "C" DllExport ResultItemStruct * getResultStruct();

extern "C" DllExport int getVariableCount();

extern "C" DllExport struct ScalarVariableMeta * getSVmetaData();

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport int run();

extern "C" DllExport void init_1(
	void (*messageCallbackPtr)(MessageStruct *), 
	void (*resultCallbackPtr)(ResultItemStruct *),
    void (*stateChangeCallbackPtr)(State)
	);

extern "C" DllExport void init_2(char *);

extern "C" DllExport void init_3();
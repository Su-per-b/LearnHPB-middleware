#pragma once


#include "stdafx.h"
#include "FMUwrapper.h"
#include "structs.h"
#include "enums.h"


void (*messageCallbackPtr_)(MessageStruct *);

void (*resultCallbackPtr_)(ResultItemStruct *);

Straylight::FMUwrapper *  fmuWrapper;


extern "C" DllExport int doOneStep();

extern "C" DllExport void end();

extern "C" DllExport ResultItemStruct * getResultStruct();

extern "C" DllExport int getVariableCount();

extern "C" DllExport struct ScalarVariableMeta * getSVmetaData();

extern "C" DllExport void init(char *);

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport int registerMessageCallback(void (*callbackPtr)(MessageStruct *));

extern "C" DllExport int registerResultCallback(void (*callbackPtr)(ResultItemStruct *));

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport int run();


//**Tests**//
extern "C" DllExport void testFMU(char * unzipFolder);




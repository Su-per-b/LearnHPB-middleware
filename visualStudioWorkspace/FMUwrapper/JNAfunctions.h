#pragma once

#include <tchar.h>
#include "stdafx.h"
#include <sstream>
#include <list>
#include "FMUwrapper.h"
#include "Structs.h"


void (*callbackPtr)(char *);

Straylight::FMUwrapper *  fmuWrapper;


extern "C" DllExport int doOneStep();

extern "C" DllExport void end();

extern "C" DllExport ResultItemStruct * getResultStruct();

extern "C" DllExport int getVariableCount();

extern "C" DllExport struct ScalarVariableMeta * getSVmetaData();

extern "C" DllExport void init(char *);

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport int registerCallback(void (*callbackPtr)(char *));

//**Tests**//
extern "C" DllExport void testFMU(char *);

extern "C" DllExport  ResultItemPrimitiveStruct * testPrimitive();

extern "C" DllExport  ResultItemPrimitiveStruct * testPrimitiveArray();

extern "C" DllExport  ResultItemStruct * testResultItemStruct();




#include <tchar.h>
#include "stdafx.h"
#include <sstream>
#include <list>
#include "FMUwrapper.h"




void (*callbackPtr)(char *);

Straylight::FMUwrapper *  fmuWrapper;


extern "C" DllExport void end();

extern "C" DllExport struct ScalarVariableMeta * getSVmetaData();

extern "C" DllExport char * getResultFromOneStep();

extern "C" DllExport int getVariableCount();

extern "C" DllExport Elm getVariableType(int idx);

extern "C" DllExport void initAll(char *);

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport void testFMU(char *);

extern "C" DllExport ResultItemStruct * getResultStruct();

extern "C" DllExport  ResultItemPrimitiveStruct * testPrimitive();

extern "C" DllExport  ResultItemPrimitiveStruct * testPrimitiveArray();

extern "C" DllExport  ResultItemStruct * testResultItemStruct();

extern "C" DllExport int registerCallback(void (*callbackPtr)(char *));
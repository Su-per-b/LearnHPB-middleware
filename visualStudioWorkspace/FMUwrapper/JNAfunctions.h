

#include <tchar.h>
#include "stdafx.h"
#include <sstream>
#include <list>



extern "C" DllExport void end();

extern "C" DllExport int * getaDataList();

extern "C" DllExport int * getaDataList2();

extern "C" DllExport int * getaDataList3();

extern "C" DllExport void getaDataList4(void * buf);

extern "C" DllExport char * getResultFromOneStep();

extern "C" DllExport Enu getVariableCausality(int idx);

extern "C" DllExport int getVariableCount();

extern "C" DllExport const char * getVariableDescription(int idx);

extern "C" DllExport const char * getVariableName(int idx);

extern "C" DllExport Elm getVariableType(int idx);

extern "C" DllExport void initAll(char *);

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport void testFMU(char *);








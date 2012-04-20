

#include <tchar.h>
#include "stdafx.h"
#include <sstream>
#include <list>



extern "C" DllExport void end();

extern "C" DllExport int * getDataList();

extern "C" DllExport int * getDataList2();

extern "C" DllExport int * getDataList3();

extern "C" DllExport void getDataList4(void * buf);

extern "C" DllExport void getDataList5(void * buf);

extern "C" DllExport struct ScalarVariableMeta * getDataList6();


extern "C" DllExport char * getResultFromOneStep();

extern "C" DllExport Enu getVariableCausality(int idx);

extern "C" DllExport int getVariableCount();

extern "C" DllExport const char * getVariableDescription(int idx);

extern "C" DllExport const char * getVariableName(int idx);

extern "C" DllExport Elm getVariableType(int idx);

extern "C" DllExport void initAll(char *);

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport void testFMU(char *);


extern "C" DllExport  struct ScalarVariableMeta *  getDataList6( );







#include <tchar.h>
#include "stdafx.h"


#include <sstream>

extern "C" DllExport void testFMU(char *);

extern "C" DllExport void initAll(char *);

extern "C" DllExport char * getStringXy();

extern "C" DllExport int isSimulationComplete();

extern "C" DllExport void end();

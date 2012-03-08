/* ------------------------------------------------------------------------- 
 * main.h
 * Function types for all FMU functions and a struct to wrap a FMU dll. 
 * Copyright 2010 QTronic GmbH. All rights reserved. 
 * -------------------------------------------------------------------------
 */

#pragma once

// Use windows.h only for Windows 
#ifdef _MSC_VER
	#define WINDOWS 1
#endif

#include "stdafx_c.h"






void fmuLogger(fmiComponent c, fmiString instanceName, fmiStatus status,
               fmiString category, fmiString message, ...);


void* getAdr(FMU *, const char*);


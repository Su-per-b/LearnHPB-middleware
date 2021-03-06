// stdafx.h : include file for standard system include files,
// or project specific include files that are used frequently, but
// are changed infrequently
//

#pragma once

#define DllApi   __declspec( dllexport )


// Including SDKDDKVer.h defines the highest available Windows platform.

// If you wish to build your application for a previous Windows platform, include WinSDKVer.h and
// set the _WIN32_WINNT macro to the platform you wish to support before including SDKDDKVer.h.

#include <SDKDDKVer.h>

#define WIN32_LEAN_AND_MEAN             // Exclude rarely-used stuff from Windows headers
#define MSG_BUFFER_SIZE 4096
#define MSG_BUFFER_SIZE_SMALL 1024

// Windows Header Files:
#include <windows.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sstream>
#include <tchar.h>
#include <vector>
#include <assert.h>
#include <map>

typedef std::map<std::string, bool> StringMap;

extern "C"
{
	#include <expat.h>
	#include <xml_parser.h>
	#include <fmi_cs.h>
}

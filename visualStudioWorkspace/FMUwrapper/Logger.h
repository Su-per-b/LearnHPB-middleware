#ifdef _MSC_VER
#include <windows.h>
#define WINDOWS 1
#else
#define WINDOWS 0
#define HANDLE void *
/* See http://www.yolinux.com/TUTORIALS/LibraryArchives-StaticAndDynamic.html */
#include <sys/stat.h> // for creating dirs on Linux
#endif

#include <tchar.h>
#include "stdafx.h"
#include "ResultSet.h"
#include "ResultItem.h"
#include <sstream>
#include <list>

namespace Straylight
{

	class Logger
	{

	//private member variables
	private:
        void (*utilCallbackPtr)(char *);
		int debug;  // Control for debug information
		int debugvs;  // Control for debug information to Output window in Visual Studio


	// public functions
	public:
		Logger(void);
		~Logger(void);
		void registerCallback(void (*callbackPtrArg)(char *));
		void doubleToCommaString(char* buffer, double r);
		void printDebug(const char* msg);
		void printDebug2(const char* str1, const char* str2);
		void printDebugHelper(const char* str1, const char* str2 );
		void printError(const char* msg);
		void printfError(const char* str1, const char* str2);
		void setDebug();

	};




};




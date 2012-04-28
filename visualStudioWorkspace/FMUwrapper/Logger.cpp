// FMUwrapper.cpp : Defines the exported functions for the DLL application.
//
#include "stdafx.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include "Logger.h"





namespace Straylight
{


	/*********************************************//**
	* Default constructor. 
	*********************************************/
	Logger::Logger(void)
	{
		debug = 0;  // Control for debug information
		debugvs = 1;  // Control for debug information to Output window in Visual Studio
	}


	/*********************************************//**
	* Destructor. Frees memory and releases FMU DLL.
	*********************************************/
	Logger::~Logger(void)
	{


	}

	void Logger::registerCallback(void (*callbackPtrArg)(char *))
	{
		void (*utilCallbackPtr2)(char *);

		utilCallbackPtr = callbackPtrArg;
	}


	///////////////////////////////////////////////////////////////////////////////
	/// Translate the double variable to string variable.
	///
	///\param buffer String variable.
	///\param r Double varaible.
	///////////////////////////////////////////////////////////////////////////////
	void Logger::doubleToCommaString(char* buffer, double r){
		char* comma;
		sprintf(buffer, "%.16g", r);
		comma = strchr(buffer, '.');
		if (comma) *comma = ',';
	}




	//////////////////////////////////////////////////////////////////////////////
	/// Set the mode in debug so that the debug information will be printed
	///
	//////////////////////////////////////////////////////////////////////////////
	void Logger::setDebug( )
	{
	  debug = 1;
	}




	//////////////////////////////////////////////////////////////////////////////
	/// Print debug message
	///
	///\param msg Message to be printed for debugging 
	//////////////////////////////////////////////////////////////////////////////
	void Logger::printDebug(const char* msg){

	  if (debug > 0)
	  {
		  fprintf(stdout, "Debug: ");
		  fprintf(stdout, "%s\n", msg);
	  }

	  if (debugvs > 0)
	  {
		  OutputDebugString(msg);
	  }
	}

	//////////////////////////////////////////////////////////////////////////////
	/// Print formatted debug message
	///
	///\param str1 Message to be printed for debugging 
	///\param str2 String variable to be printed for debugging
	//////////////////////////////////////////////////////////////////////////////
	void Logger::printDebug2(const char* str1, const char* str2){
		if (debug == 1)
		{
			fprintf(stdout, "Debug: ");
			fprintf(stdout, str1, str2);
		}

		if (debugvs > 0)
		{
			printDebugHelper(str1, str2);
		}

	}

	void Logger::printDebug5(const char* str1, const char* str2, const char* str3,
		const char* str4, const char* str5) {


		if (debug == 1)
		{
			fprintf(stdout, "Debug: ");
			fprintf(stdout, str1, str2, str3, str4, str5);
		}

		if (debugvs > 0)
		{
			char msg[256];

			sprintf (msg, str1, str2, str3, str4, str5);
			printDebugHelper("%s\n", msg);

		}

	}


	//////////////////////////////////////////////////////////////////////////////
	/// Print debug message to the console in Visual Studio
	///
	//////////////////////////////////////////////////////////////////////////////
	void Logger::printDebugHelper(const char* str1, const char* str2 ) {
	
		char msg[256];

		sprintf (msg, str1, str2);

		if (utilCallbackPtr != NULL) {
			utilCallbackPtr(msg);
		}

		OutputDebugString(msg);

	}



	//////////////////////////////////////////////////////////////////////////////
	/// Print error message
	///
	///\param msg Error message to be printed
	//////////////////////////////////////////////////////////////////////////////
	void Logger::printError(const char* msg){
	  fprintf(stderr, "*** Error: ");
	  fprintf(stderr, "%s\n", msg);

	  if (debugvs > 0)
	  {
		  OutputDebugString("*** Error: ");
		  printDebugHelper("%s\n", msg);
	  }
	}


	//////////////////////////////////////////////////////////////////////////////
	/// Print formatted error message
	///
	///\param str1 Error message to be printed
	///\param str2 String variable to be printed
	//////////////////////////////////////////////////////////////////////////////
	void Logger::printfError(const char* str1, const char* str2){
	  fprintf(stderr, "*** Error: ");
	  fprintf(stderr, str1, str2);

	  if (debugvs > 0)
	  {
		  OutputDebugString("*** Error: ");
		  printDebugHelper(str1, str2);
	  }
	}


}





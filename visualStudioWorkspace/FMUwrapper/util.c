///////////////////////////////////////////////////////
/// \file   util.c
///
/// \brief  utility functions
///
/// \author Wangda Zuo, Thierry S. Nouidui, Michael Wetter
///         Simulation Research Group,
///         LBNL,
///         WZuo@lbl.gov
///
/// \date   2011-11-02
///
///
/// This file provides utility functions for fmus
///
///////////////////////////////////////////////////////

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include "util.h"
#include <windows.h> 
#include <stdarg.h>



int debug = 0;  // Control for debug information
int debugvs = 1;  // Control for debug information to Output window in Visual Studio

///////////////////////////////////////////////////////////////////////////////
/// Translate the double variable to string variable.
///
///\param buffer String variable.
///\param r Double varaible.
///////////////////////////////////////////////////////////////////////////////
void doubleToCommaString(char* buffer, double r){
    char* comma;
    sprintf(buffer, "%.16g", r);
    comma = strchr(buffer, '.');
    if (comma) *comma = ',';
}




//////////////////////////////////////////////////////////////////////////////
/// Set the mode in debug so that the debug information will be printed
///
//////////////////////////////////////////////////////////////////////////////
void setDebug( )
{
  debug = 1;
}



//////////////////////////////////////////////////////////////////////////////
/// Print debug message
///
///\param msg Message to be printed for debugging 
//////////////////////////////////////////////////////////////////////////////
void printDebug(const char* msg){

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
/// Print debug message to the console in Visual Studio
///
//////////////////////////////////////////////////////////////////////////////
void printfDebugHelper(const char* str1, const char* str2 ) {
	
	char msg[256];

	sprintf (msg, str1, str2);
	OutputDebugString(msg);
}





//////////////////////////////////////////////////////////////////////////////
/// Print formatted debug message
///
///\param str1 Message to be printed for debugging 
///\param str2 String variable to be printed for debugging
//////////////////////////////////////////////////////////////////////////////
void printfDebug(const char* str1, const char* str2){
	if (debug == 1)
	{
		fprintf(stdout, "Debug: ");
		fprintf(stdout, str1, str2);
	}

  if (debugvs > 0)
  {


	  printfDebugHelper(str1, str2);
  }

}

//////////////////////////////////////////////////////////////////////////////
/// Print error message
///
///\param msg Error message to be printed
//////////////////////////////////////////////////////////////////////////////
void printError(const char* msg){
  fprintf(stderr, "*** Error: ");
  fprintf(stderr, "%s\n", msg);

  if (debugvs > 0)
  {
	  OutputDebugString("*** Error: ");
	  printfDebugHelper("%s\n", msg);
  }
}

//////////////////////////////////////////////////////////////////////////////
/// Print formatted error message
///
///\param str1 Error message to be printed
///\param str2 String variable to be printed
//////////////////////////////////////////////////////////////////////////////
void printfError(const char* str1, const char* str2){
  fprintf(stderr, "*** Error: ");
  fprintf(stderr, str1, str2);

  if (debugvs > 0)
  {
	  OutputDebugString("*** Error: ");
	  printfDebugHelper(str1, str2);
  }
}



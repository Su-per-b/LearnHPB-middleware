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
/// Get temporary path
///
///\param nam Name to be used for temporary path
///\param length Number of characters to be copied from \c nam.
///\return Point of tmpPat if there is no error occurred.
/////////////////////////////////////////////////////////////////////////////
char *getTmpPath(const char *nam, int length)
{
  char *tmpPat;

  tmpPat = (char * ) calloc(sizeof(char), length+2);
  
  // Define the temporary folder
  if(strncpy(tmpPat, nam, length) == NULL){
    printError("Fail to allocate memory for temp dir\n");
    return NULL;    
  }
  if(WINDOWS) strcat(tmpPat, "\\");
  else strcat(tmpPat, "/");

  return tmpPat;
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
	  printDebugHelper(msg);
  }
}


//////////////////////////////////////////////////////////////////////////////
/// Print debug message to the console in Visual Studio
///
///\param msg Message to be printed for debugging 
//////////////////////////////////////////////////////////////////////////////
void printfDebugHelper(const char* str1, const char* str2 ) {
	
	char msg[256];
	//wchar_t * wMsg;

	sprintf (msg, str1, str2);
	//wMsg = convertConstChar_LPWSTR(msg);

	OutputDebugString(msg);
	//free(msg);
}


wchar_t * convertConstChar_LPWSTR(const char* strIn) {
	wchar_t * strOut;
	int len;

	len = strlen(strIn) + 1; // I had to add an additional character I believe is
							//an end-of-line character

	strOut = (wchar_t *) calloc(sizeof(wchar_t), len);

	if (strOut == NULL){
		printfError("Failed to allocate memory for wText\n", strIn);
		return NULL;
	}

	MultiByteToWideChar(  0, 0, strIn, -1, strOut,len);

	return strOut;

}



//////////////////////////////////////////////////////////////////////////////
/// Print debug message to the console in Visual Studio
///
///\param msg Message to be printed for debugging 
//////////////////////////////////////////////////////////////////////////////
void printDebugHelper(const char* msg) {
	
	OutputDebugString(msg);

	/*
	wchar_t * wText;
	int len;
	int wlen;


	len = strlen(msg) + 1; // I had to add an additional character I believe is is
							//an end-of-line character
	wText = (wchar_t *) calloc(sizeof(wchar_t), len);


	if (wText == NULL){
		printfError("Failed to allocate memory for wText\n", msg);
		return;
	}

	wlen = MultiByteToWideChar(  0, 0, msg, -1, wText,len);

	free(wText);

	*/

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
	  printDebugHelper("*** Error: ");
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
	  printDebugHelper("*** Error: ");
	  printfDebugHelper(str1, str2);
  }
}



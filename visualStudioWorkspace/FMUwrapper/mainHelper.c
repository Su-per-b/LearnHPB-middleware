///////////////////////////////////////////////////////
/// \file   main.c
///
/// \brief  demo program of fmu for co-simulation.
///
/// \author Wangda Zuo,
///         Simulation Research Group, 
///         LBNL,
///         WZuo@lbl.gov
///
/// \date   2011-10-10
///
/// \version $Id: main.c 55724 2011-10-16 17:51:58Z wzuo $
///
/// This file is based on main.c that is copyrighted by
/// QTronic GmbH and that is distributed under the BSD license. 
/// The original file, its copyright notice and its license can 
/// be found in ../3rdParty
///
/// The file has been modified for use with the FMU standard for 
/// co-simulation. The original file was developed for model exchange.
/// The original file used 0 as indicator for failure and 
/// 1 as indicator for success.  
/// The new file uses 0 as indicator for success according to STL.
///
///////////////////////////////////////////////////////
/* ------------------------------------------------------------------------- 
* main.c
* Implements simulation of a single FMU instance using the forward Euler
* method for numerical integration.
* Command syntax: see printHelp()
* Simulates the given FMU from t = 0 .. tEnd with fixed step size h and 
* writes the computed solution to file 'result.csv'.
* The CSV file (comma-separated values) may e.g. be plotted using 
* OpenOffice Calc or Microsoft Excel. 
* This progamm demonstrates basic use of an FMU.
* Real applications may use advanced numerical solvers instead, means to 
* exactly locate state events in time, graphical plotting utilities, support 
* for coexecution of many FMUs, stepping and debug support, user control
* of parameter and start values etc. 
* All this is missing here.
*
* Revision history
*  07.02.2010 initial version released in FMU SDK 1.0
*  05.03.2010 bug fix: removed strerror(GetLastError()) from error messages
*     
* Free libraries and tools used to implement this simulator:
*  - eXpat 2.0.1 XML parser, see http://expat.sourceforge.net
*  - 7z.exe 4.57 zip and unzip tool, see http://www.7-zip.org
* Copyright 2010 QTronic GmbH. All rights reserved. 
* -------------------------------------------------------------------------
*/

#include "mainHelper.h"
#include "fmi_cs.h"

#define BUFSIZE 4096
#define XML_FILE  "modelDescription.xml"
#define DLL_DIR   ""
#define RESULT_FILE "result.csv"

static FMU fmu; // the fmu to simulate

///////////////////////////////////////////////////////////////////////////////
/// Get address of specific function from specific dll
///
///\param fmu Name of dll file.
///\param funnam Function name .
///\return Address of the specific function.
//////////////////////////////////////////////////////////////////////////////
void* getAdr(FMU *fmu, const char* funNam){
	char name[BUFSIZE];
	void* fp;
	sprintf(name, "%s_%s", getModelIdentifier(fmu->modelDescription), funNam); // Zuo: adding the model name in front of function name


	fp = GetProcAddress(fmu->dllHandle, name);

	if (!fp) {
		printfError("Function %s not found in dll\n", name);        
	}
	return fp;
}



///////////////////////////////////////////////////////////////////////////////
/// Output time and all non-alias variables in CSV format.
/// If separator is ',', columns are separated by ',' and '.' is used for floating-point numbers.
/// Otherwise, the given separator (e.g. ';' or '\t') is to separate columns, and ',' is used for 
/// floating-point numbers.
///
///\param fmu FMU.
///\param c FMI component.
///\param time Time when data is outputed.
///\param separator Separator in file.
///\param header Indicator of row head.
///////////////////////////////////////////////////////////////////////////////
void outputRow(FMU *fmu, fmiComponent c, double time, FILE* file, char separator) {
	int k;
	fmiReal r;
	fmiInteger i;
	fmiBoolean b;
	fmiString s;
	fmiValueReference vr;				
	ScalarVariable** vars = fmu->modelDescription->modelVariables;
	char buffer[32];

	// Print first column

	if (separator==',') 
		fprintf(file, "%.4f", time);				
	else {
		// Separator is e.g. ';' or '\t'
		doubleToCommaString(buffer, time);
		fprintf(file, "%s", buffer);       
	}


	// Print all other columns
	for (k=0; vars[k]; k++) {
		ScalarVariable* sv = vars[k];
		if (getAlias(sv)!=enu_noAlias) continue;


		// Output values
		vr = getValueReference(sv);
		switch (sv->typeSpec->type){
		case elm_Real:
			fmu->getReal(c, &vr, 1, &r);
			if (separator==',') 
				fprintf(file, ",%.4f", r);				
			else {
				// Separator is e.g. ';' or '\t'
				doubleToCommaString(buffer, r);
				fprintf(file, "%c%s", separator, buffer);       
			}
			break;
		case elm_Integer:
			fmu->getInteger(c, &vr, 1, &i);
			fprintf(file, "%c%d", separator, i);
			break;
		case elm_Boolean:
			fmu->getBoolean(c, &vr, 1, &b);
			fprintf(file, "%c%d", separator, b);
			break;
		case elm_String:
			fmu->getString(c, &vr, 1, &s);
			fprintf(file, "%c%s", separator, s);
			break;
		}

	} // for

	// Terminate this row
	fprintf(file, "\n"); 
}







///////////////////////////////////////////////////////////////////////////////
/// Translate FMI status to string variable
///
///\param status FMI status
///\return Corresponding string variable if it is found.
///////////////////////////////////////////////////////////////////////////////
static const char* fmiStatusToString(fmiStatus status){
	switch (status){
	case fmiOK:      return "ok";
	case fmiWarning: return "warning";
	case fmiDiscard: return "discard";
	case fmiError:   return "error";
		// case fmiPending: return "pending";	
	default:         return "?";
	}
}

///////////////////////////////////////////////////////////////////////////////
/// Search a fmu for the given variable.
///
///\param fmu FMU.
///\param type Type of FMU variable.
///\param vr FMI value reference.
///\return NULL if not found or vr = fmiUndefinedValueReference
///////////////////////////////////////////////////////////////////////////////
static ScalarVariable* getSV(FMU* fmu, char type, fmiValueReference vr) {
	int i;
	Elm tp;
	ScalarVariable** vars = fmu->modelDescription->modelVariables;
	if (vr==fmiUndefinedValueReference) return NULL;
	switch (type) {
	case 'r': tp = elm_Real;    break;
	case 'i': tp = elm_Integer; break;
	case 'b': tp = elm_Boolean; break;
	case 's': tp = elm_String;  break;                
	}
	for (i=0; vars[i]; i++) {
		ScalarVariable* sv = vars[i];
		if (vr==getValueReference(sv) && tp==sv->typeSpec->type) 
			return sv;
	}
	return NULL;
}

///////////////////////////////////////////////////////////////////////////////
/// Replace reference information in message.
/// E.g. #r1365# by variable name and ## by # in message
/// copies the result to buffer
///
///\param msg 
///\param buffer
///\param nBuffer
///\param fmu FMU
///////////////////////////////////////////////////////////////////////////////
static void replaceRefsInMessage(const char* msg, char* buffer, int nBuffer, FMU* fmu){
	int i=0; // position in msg
	int k=0; // position in buffer
	int n;
	char c = msg[i];
	while (c!='\0' && k < nBuffer) {
		if (c!='#') {
			buffer[k++]=c;
			i++;
			c = msg[i];
		}
		else {
			char* end = (char*) strchr(msg+i+1, '#');
			if (!end) {
				printf("unmatched '#' in '%s'\n", msg);
				buffer[k++]='#';
				break;
			}
			n = end - (msg+i);
			if (n==1) {
				// ## detected, output #
				buffer[k++]='#';
				i += 2;
				c = msg[i];
			}
			else {
				char type = msg[i+1]; // one of ribs
				fmiValueReference vr;
				int nvr = sscanf(msg+i+2, "%u", &vr);
				if (nvr==1) {
					// vr of type detected, e.g. #r12#
					ScalarVariable* sv = getSV(fmu, type, vr);
					const char* name = sv ? getName(sv) : "?";
					sprintf(buffer+k, "%s", name);
					k += strlen(name);
					i += (n+1);
					c = msg[i]; 
				}
				else {
					// could not parse the number
					printf("illegal value reference at position %d in '%s'\n", i+2, msg);
					buffer[k++]='#';
					break;
				}
			}
		}
	} // while
	buffer[k] = '\0';
}

#define MAX_MSG_SIZE 1000
///////////////////////////////////////////////////////////////////////////////
/// FMU logger
///
///\param c FMI component.
///\param instanceName FMI string.
///\param status FMI status.
///\param category FMI string. 
///\param message Message to be recorded.
///////////////////////////////////////////////////////////////////////////////
void fmuLogger(fmiComponent c, fmiString instanceName, fmiStatus status,
	fmiString category, fmiString message, ...) {
		char msg[MAX_MSG_SIZE];
		char* copy;
		va_list argp;

		// Replace C format strings
		va_start(argp, message);
		vsprintf(msg, message, argp);

		// Replace e.g. ## and #r12#  
		copy = strdup(msg);
		replaceRefsInMessage(copy, msg, MAX_MSG_SIZE, &fmu);
		free(copy);

		// Print the final message
		if (!instanceName) instanceName = "?";
		if (!category) category = "?";
		printf("%s %s (%s): %s\n", fmiStatusToString(status), instanceName, category, msg);
}






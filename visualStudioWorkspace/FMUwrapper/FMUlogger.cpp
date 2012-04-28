// FMUwrapper.cpp : Defines the exported functions for the DLL application.
//
#include "stdafx.h"
#include "FMUlogger.h"


namespace Straylight
{

	FMU FMUlogger::fmu;

	Logger* FMUlogger::logger; 


	/*********************************************//**
	* Default constructor. 
	*********************************************/
	FMUlogger::FMUlogger(void)
	{

	}


	/*********************************************//**
	* Destructor. Frees memory and releases FMU DLL.
	*********************************************/
	FMUlogger::~FMUlogger(void)
	{

	}


	///////////////////////////////////////////////////////////////////////////////
	/// Translate FMI status to string variable
	///
	///\param status FMI status
	///\return Corresponding string variable if it is found.
	///////////////////////////////////////////////////////////////////////////////
	const char* FMUlogger::fmiStatusToString(fmiStatus status) {
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
	ScalarVariable* FMUlogger::getSV(FMU* fmu, char type, fmiValueReference vr) {
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
	void FMUlogger::replaceRefsInMessage(const char* msg, char* buffer, int nBuffer, FMU* fmu) {
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
	void FMUlogger::log(fmiComponent c, fmiString instanceName, fmiStatus status,
		fmiString category, fmiString message, ...) {

		//printf("fmuLogger\n", 1);

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

		logger->printDebug5("fmuLogger - status:%s - instanceName:%s - category:%s - msg:%s\n", fmiStatusToString(status), instanceName, category, msg);

		//printf();
	}



}





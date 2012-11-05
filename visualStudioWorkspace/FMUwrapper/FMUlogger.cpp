// MainController.cpp : Defines the exported functions for the DLL application.
//
#include "FMUlogger.h"

namespace Straylight
{
	#define MAX_MSG_SIZE 4096

	/*******************************************************//**
	 * <summary> The fmulogger fmu.</summary>
	 *******************************************************/
	FMU * FMUlogger::fmu;

	/*******************************************************//**
	 * <summary> Default constructor.</summary>
	 *
	 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
	 *******************************************************/
	FMUlogger::FMUlogger(void)
	{
	}

	/*******************************************************//**
	 * <summary> Destructor.</summary>
	 *
	 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
	 *******************************************************/
	FMUlogger::~FMUlogger(void)
	{
	}

	/*******************************************************//**
	 * <summary> Logs.</summary>
	 *
	 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
	 *
	 * <param name="c">			   The fmiComponent to process.</param>
	 * <param name="instanceName"> Name of the instance.</param>
	 * <param name="status">	   The status.</param>
	 * <param name="category">	   The category.</param>
	 * <param name="message">	   The message.</param>
	 *******************************************************/
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
			replaceRefsInMessage(copy, msg, MAX_MSG_SIZE, fmu);
			free(copy);

			// Print the final message
			if (!instanceName) instanceName = "?";
			if (!category) category = "?";

			Logger::getInstance()->printDebug5("fmuLogger - status:%s - instanceName:%s - category:%s - msg:%s\n", fmiStatusToString(status), instanceName, category, msg);

			//printf();
	}
	/*******************************************************//**
	 * <summary> Sets a fmu.</summary>
	 *
	 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
	 *
	 * <param name="fmuArg"> If non-null, the fmu argument.</param>
	 *******************************************************/
	void FMUlogger::setFMU(FMU* fmuArg) {
		FMUlogger::fmu = fmuArg;
	}

	/*******************************************************//**
	 * <summary> Fmi status to string.</summary>
	 *
	 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
	 *
	 * <param name="status"> The status.</param>
	 *
	 * <returns> null if it fails, else.</returns>
	 *******************************************************/
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

	/*******************************************************//**
	 * <summary> Gets a sv.</summary>
	 *
	 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
	 *
	 * <param name="fmu">  If non-null, the fmu.</param>
	 * <param name="type"> The type.</param>
	 * <param name="vr">   The vr.</param>
	 *
	 * <returns> null if it fails, else the sv.</returns>
	 *******************************************************/
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

	/*******************************************************//**
	 * <summary> Replace references in message.</summary>
	 *
	 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
	 *
	 * <param name="msg">	  The message.</param>
	 * <param name="buffer">  If non-null, the buffer.</param>
	 * <param name="nBuffer"> The buffer.</param>
	 * <param name="fmu">	  If non-null, the fmu.</param>
	 *******************************************************/
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

}
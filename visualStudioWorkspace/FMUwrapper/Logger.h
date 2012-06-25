#pragma once


#include "stdafx.h"
#include "ResultOfStep.h"
#include "structs.h"
#include "enums.h"


namespace Straylight
{

	class Logger
	{

		//private member variables
	private:

		void (*messageCallbackPtr_)(MessageStruct *);

		int debug;  // Control for debug information
		int debugvs;  // Control for debug information to Output window in Visual Studio


		// public functions
	public:
		Logger(void);
		~Logger(void);



		void registerMessageCallback(void (*callbackPtrArg)(MessageStruct *));
		void registerCallback(void (*callbackPtrArg)(char *));
		void doubleToCommaString(char* buffer, double r);
		void printDebug(const char* msg);
		void printDebug2(const char* str1, const char* str2);

		void printDebug5(const char* str1, const char* str2,
			const char* str3, const char* str4, const char* str5);

		void printDebugHelper(const char* str1, const char* str2 );
		void printError(const char* msg);
		void printfError(const char* str1, const char* str2);
		void setDebug();
		void printDebugDouble(const char* key, double valueDouble);
	};




};




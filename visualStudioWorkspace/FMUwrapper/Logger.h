/*******************************************************//**
 * @file	Logger.h
 *
 * Declares the logger class.
 *******************************************************/
#pragma once

#include "stdafx.h"
#include "ResultOfStep.h"
#include "structs.h"
#include "enums.h"
#include "Utils.h"



namespace Straylight
{



	/*******************************************************//**
	 * Logger.
	 *******************************************************/
	class DllExport Logger
	{
		//private member variables
	private:

		static bool instanceFlag;

		/*******************************************************//**
		 * Message callback pointer.
		 *
		 * @param	parameter1	If non-null, the first parameter.
		 *******************************************************/
		void (*messageCallbackPtr_)(MessageStruct *);
		int debug_;  // Control for debug information
		int debugvs_;  // Control for debug information to Output window in Visual Studio

		static Logger* instance_;

		/*******************************************************//**
		 * Default constructor.
		 *******************************************************/
		Logger(void);


		// public functions
	public:

		/*******************************************************//**
		 * Destructor.
		 *******************************************************/
		~Logger(void);

		int getDebugvs() const { return debugvs_; }
		void setDebugvs(int val) { debugvs_ = val; }




		/*******************************************************//**
		 * The instance.
		 *******************************************************/

		static Logger* getInstance();


		void registerMessageCallback(void (*callbackPtrArg)(MessageStruct *));

		/*******************************************************//**
		 * Registers the callback described by callbackPtrArg.
		 *
		 * @param	callbackPtrArg	If non-null, the callback pointer argument.
		 *******************************************************/
		void registerCallback(void (*callbackPtrArg)(char *));
		void doubleToCommaString(char* buffer, double r);
		void printDebug(const char* msg);

		/*******************************************************//**
		 * Print debug 2.
		 *
		 * @param	str1	The first string.
		 * @param	str2	The second string.
		 *******************************************************/
		void printDebug2(const char* str1, const char* str2);

		/*******************************************************//**
		 * Print debug 5.
		 *
		 * @param	str1	The first string.
		 * @param	str2	The second string.
		 * @param	str3	The third string.
		 * @param	str4	The fourth string.
		 * @param	str5	The fifth string.
		 *******************************************************/
		void printDebug5(const char* str1, const char* str2,
			const char* str3, const char* str4, const char* str5);

		/*******************************************************//**
		 * Helper method that print debug.
		 *
		 * @param	str1	The first string.
		 * @param	str2	The second string.
		 *******************************************************/
		void printDebugHelper(const char* str1, const char* str2 );

		/*******************************************************//**
		 * Print error.
		 *
		 * @param	msg	The message.
		 *******************************************************/
		void printError(const char* msg);

		/*******************************************************//**
		 * Print error int.
		 *
		 * @param	msg	The message.
		 * @param	i  	Zero-based index of the.
		 *******************************************************/
		void printErrorInt(const char* msg, int i);

		/*******************************************************//**
		 * Printf error.
		 *
		 * @param	str1	The first string.
		 * @param	str2	The second string.
		 *******************************************************/
		void printfError(const char* str1, const char* str2);
		void setDebug();
		void printDebugDouble(const char* key, double valueDouble);
	};
};

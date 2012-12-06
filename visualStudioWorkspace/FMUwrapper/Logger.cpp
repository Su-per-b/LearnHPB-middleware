// MainController.cpp : Defines the exported functions for the DLL application.
//

#include "Logger.h"

namespace Straylight
{
	#define MAX_MSG_SIZE 4096

	/*******************************************************//**
	 * The logger instance.
	 *******************************************************/
	bool Logger::instanceFlag = false;
	Logger* Logger::instance_ = NULL;

	/*******************************************************//**
	 * Default constructor.
	 *******************************************************/
	Logger::Logger(void)
	{
		messageCallbackPtr_ = NULL;
		debug_ = 1;  // Control for debug information
		debugvs_ = 0;  // Control for debug information to Output window in Visual Studio
	}

	/*******************************************************//**
	 * Destructor. Frees memory and releases FMU DLL.
	 *******************************************************/
	Logger::~Logger(void)
	{
		instanceFlag = false;

	}

	Logger* Logger::getInstance()  { 

		if(! instanceFlag)
		{
			instance_ = new Logger();
			instanceFlag = true;
			return instance_;
		}
		else
		{
			return instance_;
		}

	}

	/*******************************************************//**
	 * Registers the message callback described by callbackPtrArg.
	 *
	 * @param [in,out]	callbackPtrArg	If non-null, the callback pointer argument.
	 *******************************************************/
	void Logger::registerMessageCallback(void (*callbackPtrArg)(MessageStruct *))
	{
		messageCallbackPtr_ = callbackPtrArg;
		printDebug(_T("Logger::registerMessageCallback"));
	}

	/*******************************************************//**
	 * Double to comma string.
	 *
	 * @param [in,out]	buffer	If non-null, the buffer.
	 * @param	r			  	The double to process.
	 *******************************************************/
	void Logger::doubleToCommaString(char* buffer, double r){
		char* comma;
		sprintf(buffer, _T("%.16g"), r);
		comma = strchr(buffer, '.');
		if (comma) *comma = ',';
	}

	/*******************************************************//**
	 * Sets the debug.
	 *******************************************************/
	void Logger::setDebug( )
	{
		debug_ = 1;
	}

	/*******************************************************//**
	 * Print debug.
	 *
	 * @param	msg	The message.
	 *******************************************************/
	void Logger::printDebug(const char* msg){
		printDebugHelper( _T("%s\n"), msg);
	}

	/*******************************************************//**
	 * Print debug double.
	 *
	 * @param	key		   	The key.
	 * @param	valueDouble	The value double.
	 *******************************************************/
	void Logger::printDebugDouble(const char* key, double valueDouble) {
		string valueStr;
		{
			ostringstream theStream;
			theStream << valueDouble;
			valueStr = theStream.str();
		}

		printDebugHelper(key, valueStr.c_str());
	}

	/*******************************************************//**
	 * Print debug 2.
	 *
	 * @param	str1	The first string.
	 * @param	str2	The second string.
	 *******************************************************/
	void Logger::printDebug2(const char* str1, const char* str2){
		printDebugHelper(str1, str2);
	}

	/*******************************************************//**
	 * Print debug 5.
	 *
	 * @param	str1	The first string.
	 * @param	str2	The second string.
	 * @param	str3	The third string.
	 * @param	str4	The fourth string.
	 * @param	str5	The fifth string.
	 *******************************************************/
	void Logger::printDebug5(const char* str1, const char* str2, const char* str3,
		const char* str4, const char* str5) {
			char msg[MAX_MSG_SIZE];

			sprintf (msg, str1, str2, str3, str4, str5);
			printDebugHelper(_T("%s\n"), msg);
	}

	/*******************************************************//**
	 * Helper method that print debug.
	 *
	 * @param	str1	The first string.
	 * @param	str2	The second string.
	 *******************************************************/
	void Logger::printDebugHelper(const char* str1, const char* str2 ) {
		char msg[MAX_MSG_SIZE];
		sprintf (msg, str1, str2);

		if (messageCallbackPtr_ != NULL) {
			MessageStruct * messageStruct = new MessageStruct;
			messageStruct->msgText = msg;
			messageStruct->messageType = messageType_info;

			messageCallbackPtr_(messageStruct);
		}

		if (debug_ > 0)
		{
			fprintf(stdout,  msg);
		}

		if (debugvs_ > 0)
		{
			OutputDebugString(msg);

		}
			fflush(stdout);
	}

	/*******************************************************//**
	 * Print error.
	 *
	 * @param	msg	The message.
	 *******************************************************/
	void Logger::printError(const char* msg){
		printfError( _T("%s\n"), msg);
	}

	/*******************************************************//**
	 * Print error int.
	 *
	 * @param	msg	The message.
	 * @param	i  	Zero-based index of the.
	 *******************************************************/
	void Logger::printErrorInt(const char* msg, int i) {
		char str[MAX_MSG_SIZE];
		Utils::intToString(str, i);

		printfError(msg, str);
	}

	/*******************************************************//**
	 * Printf error.
	 *
	 * @param	str1	The first string.
	 * @param	str2	The second string.
	 *******************************************************/
	void Logger::printfError(const char* str1, const char* str2){
		char msg[MAX_MSG_SIZE];
		sprintf (msg, str1, str2);

		fprintf(stderr, _T("*** Error:%s\n"), msg);
	//	fprintf(stdout, _T("*** Error:%s\n"), msg);

		fflush(stderr);
	//	fflush(stdout);

		if (messageCallbackPtr_ != NULL) {
			MessageStruct * messageStruct = new MessageStruct;
			messageStruct->msgText = msg;
			messageStruct->messageType = messageType_error;

			messageCallbackPtr_(messageStruct);
		}

		if (debugvs_ > 0)
		{
			OutputDebugString(msg);
		}
	}
}
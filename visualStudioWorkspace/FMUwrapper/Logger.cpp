// MainController.cpp : Defines the exported functions for the DLL application.
//

#include "Logger.h"





namespace Straylight
{

	/*********************************************//**
												   * Default constructor. 
												   *********************************************/
	Logger::Logger(void)
	{
		messageCallbackPtr_ = NULL;
		debug = 1;  // Control for debug information
		debugvs = 0;  // Control for debug information to Output window in Visual Studio
	}


	/*********************************************//**
												   * Destructor. Frees memory and releases FMU DLL.
												   *********************************************/
	Logger::~Logger(void)
	{


	}


	void Logger::registerMessageCallback(void (*callbackPtrArg)(MessageStruct *))
	{
		messageCallbackPtr_ = callbackPtrArg;
		printDebug(_T("Logger::registerMessageCallback"));
	}





	///////////////////////////////////////////////////////////////////////////////
	/// Translate the double variable to string variable.
	///
	///\param buffer String variable.
	///\param r Double varaible.
	///////////////////////////////////////////////////////////////////////////////
	void Logger::doubleToCommaString(char* buffer, double r){
		char* comma;
		sprintf(buffer, _T("%.16g"), r);
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
		printDebugHelper( _T("%s\n"), msg);
	}


	void Logger::printDebugDouble(const char* key, double valueDouble) {

		string valueStr;
		{ 
			ostringstream theStream;
			theStream << valueDouble;
			valueStr = theStream.str();
		}

		printDebugHelper(key, valueStr.c_str());

	}


	//////////////////////////////////////////////////////////////////////////////
	/// Print formatted debug message
	///
	///\param str1 Message to be printed for debugging 
	///\param str2 String variable to be printed for debugging
	//////////////////////////////////////////////////////////////////////////////
	void Logger::printDebug2(const char* str1, const char* str2){
		printDebugHelper(str1, str2);
	}

	void Logger::printDebug5(const char* str1, const char* str2, const char* str3,
		const char* str4, const char* str5) {

			char msg[256];

			sprintf (msg, str1, str2, str3, str4, str5);
			printDebugHelper(_T("%s\n"), msg);

	}


	//////////////////////////////////////////////////////////////////////////////
	/// Print debug message to the console in Visual Studio
	///
	//////////////////////////////////////////////////////////////////////////////
	void Logger::printDebugHelper(const char* str1, const char* str2 ) {

		char msg[256];
		sprintf (msg, str1, str2);

		if (messageCallbackPtr_ != NULL) {

			MessageStruct * messageStruct = new MessageStruct;
			messageStruct->msgText = msg;
			messageStruct->messageType = messageType_info;

			messageCallbackPtr_(messageStruct);
		}

		if (debug > 0)
		{
			fprintf(stdout, _T("%s\n"), msg);
		}

		if (debugvs > 0)
		{
			OutputDebugString(msg);
		}

		fflush(stdout);	
	}



	//////////////////////////////////////////////////////////////////////////////
	/// Print error message
	///
	///\param msg Error message to be printed
	//////////////////////////////////////////////////////////////////////////////
	void Logger::printError(const char* msg){

		printfError( _T("%s\n"), msg);

	}


	//////////////////////////////////////////////////////////////////////////////
	/// Print formatted error message
	///
	///\param str1 Error message to be printed
	///\param str2 String variable to be printed
	//////////////////////////////////////////////////////////////////////////////
	void Logger::printfError(const char* str1, const char* str2){

		char msg[256];
		sprintf (msg, str1, str2);

		fprintf(stderr, _T("*** Error:%s\n"), msg);
		fflush(stderr);	

		if (messageCallbackPtr_ != NULL) {

			MessageStruct * messageStruct = new MessageStruct;
			messageStruct->msgText = msg;
			messageStruct->messageType = messageType_error;

			messageCallbackPtr_(messageStruct);
		}


		if (debugvs > 0)
		{

			OutputDebugString(msg);
		}
	}


}





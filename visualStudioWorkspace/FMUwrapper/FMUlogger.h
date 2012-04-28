#pragma once

#include <tchar.h>
#include "stdafx.h"

#include "Logger.h"


namespace Straylight
{

	class FMUlogger
	{

	//private member variables
	private:


	// public functions
	public:
		FMUlogger(void);
		~FMUlogger(void);


		static void log(fmiComponent c, fmiString instanceName, fmiStatus status,
						fmiString category, fmiString message, ...);

		static FMU fmu; // the fmu to simulate

		static Logger* logger; 

	//private functions
	private:

		static const char* fmiStatusToString(fmiStatus status);
		static ScalarVariable* getSV(FMU* fmu, char type, fmiValueReference vr);
		static void replaceRefsInMessage(const char* msg, char* buffer, int nBuffer, FMU* fmu);

	};




};




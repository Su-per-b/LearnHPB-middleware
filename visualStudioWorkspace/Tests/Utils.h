#pragma once
#include "stdafx.h"

#define FMU_FOLDER "..\\..\\..\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_test"

namespace Microsoft
{
	namespace VisualStudio
	{
		namespace CppUnitTestFramework
		{

			template<> 
			static std::wstring ToString<ValueStatus>(const ValueStatus& t)           { RETURN_WIDE_STRING(t); }

			template<>
			static std::wstring ToString<SimStateNative>(const SimStateNative& val)
			{
				int valInt = (int)val;


				wstringstream ss;
				ss << valInt;
				wstring str = ss.str();

				return str;
			}

			template<>
			static std::wstring ToString<fmiStatus>(const fmiStatus& val)
			{
				int valInt = (int)val;


				wstringstream ss;
				ss << valInt;
				wstring str = ss.str();

				return str;
			}

			template<>
			static std::wstring ToString<Enu>(const Enu& val)
			{
				int valInt = (int)val;


				wstringstream ss;
				ss << valInt;
				wstring str = ss.str();

				return str;
			}

		}
	}
}



namespace StraylightTests
{

	class Utils
	{

	public:


		static void resultCallbackFunc(ScalarValueResultsStruct * scalarValueResultsStruct);

		static void messageCallbackFunc(MessageStruct * messageStruct);

		static void stateChangeCallbackFunc(SimStateNative simStateNative);

		static SimStateNative simStateNative;

		static ScalarValueResultsStruct * scalarValueResultsStruct;

		static MessageStruct * messageStruct;

	};

}
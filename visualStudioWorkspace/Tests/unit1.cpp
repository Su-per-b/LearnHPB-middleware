#include "stdafx.h"
#include "unit1.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;


void resultCallback2(ScalarValueResultsStruct * scalarValueResultsStruct) {
	std::string str;
}


void messageCallback2(MessageStruct * messageStruct) {
	//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
}

void fmuStateCallback2(SimStateNative simStateNative) {
	//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
	printf("Main.exe simStateNative: %s \n", _T("simStateNative"));
}


namespace StraylightTests
{










	TEST_CLASS(UT1)
	{
	public:
		
		TEST_METHOD(Assert)
		{
			Assert::AreEqual(1, 1, L"message", LINE_INFO());
		}

		TEST_METHOD(ConnectToFMU)
		{

			connect(&messageCallback2, &resultCallback2, &fmuStateCallback2);

			Assert::IsTrue(true, L"isSimulationComplete", LINE_INFO());

		}


	};
}
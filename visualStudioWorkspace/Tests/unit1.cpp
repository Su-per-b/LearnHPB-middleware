#include "stdafx.h"
#include "unit1.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;



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

			//connect(&messageCallback, &resultCallback, &fmuStateCallback);

			//bool isComplete = isSimulationComplete();
			//Assert::IsFalse(isComplete, L"isSimulationComplete", LINE_INFO());

		}


	};
}
#include "stdafx.h"
#include "CppUnitTest.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;
using namespace Straylight;

#define FMU_FOLDER _T("..\\..\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit1")


namespace Microsoft
{
	namespace VisualStudio
	{
		namespace CppUnitTestFramework
		{
			template<>
			static std::wstring ToString<SimStateNative>(const SimStateNative& val)
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




namespace Test2
{



	static MainController * mainController;
	static SimStateNative static_simStateNative;
	static TypeDefDataModel *  static_typeDefDataModel;


	void resultCallback2(ScalarValueResultsStruct * scalarValueResultsStruct) {
		std::string str;
	}


	void messageCallback2(MessageStruct * messageStruct) {
		//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
	}

	void fmuStateCallback2(SimStateNative simStateNative) {
		//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
		//printf("Main.exe simStateNative: %s \n", _T("simStateNative"));

		static_simStateNative = simStateNative;


		Microsoft::VisualStudio::CppUnitTestFramework::Logger::WriteMessage("simStateNative: ");

	}






	TEST_MODULE_INITIALIZE(ModuleInitialize)
	{
		Microsoft::VisualStudio::CppUnitTestFramework::Logger::WriteMessage("In Module Initialize");

	}

	TEST_MODULE_CLEANUP(ModuleCleanup)
	{
		Microsoft::VisualStudio::CppUnitTestFramework::Logger::WriteMessage("In Module Cleanup");
	}




	TEST_CLASS(UnitTest1)
	{

		BEGIN_TEST_CLASS_ATTRIBUTE()
			TEST_CLASS_ATTRIBUTE(L"Owner", L"Raj Dye")
			TEST_CLASS_ATTRIBUTE(L"Description", L"MainController Testing")
			END_TEST_CLASS_ATTRIBUTE()



	public:


		TEST_METHOD_INITIALIZE(Init) {

			mainController = new MainController();

		}

		TEST_METHOD_CLEANUP(Exit)
		{
			delete mainController;

		}


		TEST_METHOD(TestConnect)
		{

			Config::getInstance()->setAutoCorrect(true);

			SimStateNative state = mainController->getState();
			Assert::AreEqual(state, SimStateNative::simStateNative_0_uninitialized);

			int state2 = (int)state;
			Assert::AreEqual(state2, 0);

			mainController->connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);


			state = mainController->getState();

			Assert::AreEqual(state, SimStateNative::simStateNative_1_connect_completed);
			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_1_connect_completed);

		}


		TEST_METHOD(TestParse)
		{

			TestConnect();

			int result = mainController->xmlParse(FMU_FOLDER);

			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_2_xmlParse_completed);
			Assert::AreEqual(mainController->getState(), SimStateNative::simStateNative_2_xmlParse_completed);

			MainDataModel * mainDataModel = mainController->getMainDataModel();

			TypeDefDataModel *  typeDefDataModel = mainDataModel->getTypeDefDataModel();



			return;
		}






		TEST_METHOD(TestMainDataModel)
		{
			mainController->connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);

			MainDataModel *  mainDataModel = mainController->getMainDataModel();
			Assert::IsNotNull(mainDataModel);


		}







	};



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


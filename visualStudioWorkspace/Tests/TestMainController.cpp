#include "stdafx.h"
#include "TestMainController.h"



using namespace Microsoft::VisualStudio::CppUnitTestFramework;
using namespace Straylight;

#define FMU_FOLDER "..\\..\\..\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_test"

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





namespace StraylightTests
{



	static MainController * mainController;
	static SimStateNative static_simStateNative;



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

		Microsoft::VisualStudio::CppUnitTestFramework::Logger::WriteMessage("simStateNative: " );

	}






	TEST_MODULE_INITIALIZE(ModuleInitialize)
	{
		Microsoft::VisualStudio::CppUnitTestFramework::Logger::WriteMessage("In Module Initialize");

	}

	TEST_MODULE_CLEANUP(ModuleCleanup)
	{
		Microsoft::VisualStudio::CppUnitTestFramework::Logger::WriteMessage("In Module Cleanup");
	}




	TEST_CLASS(TestMainController)
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


		TEST_METHOD(Init01_Connect)
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

			Assert::AreEqual(state, SimStateNative::simStateNative_1_connect_completed );
			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_1_connect_completed);

			MainDataModel *  mainDataModel = mainController->getMainDataModel();
			Assert::IsNotNull(mainDataModel);

		}


		TEST_METHOD(Init02_XMLParse)
		{

			Init01_Connect();

			int result = mainController->xmlParse(FMU_FOLDER);

			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_2_xmlParse_completed);
			Assert::AreEqual(mainController->getState(), SimStateNative::simStateNative_2_xmlParse_completed);

			MainDataModel * mainDataModel = mainController->getMainDataModel();
			
			TypeDefinitions * typeDefinitions = mainDataModel->getTypeDefinitions();
			Assert::IsNotNull(typeDefinitions);

		}


		TEST_METHOD(Init03_GetConfig)
		{

			Init02_XMLParse();

			ConfigStruct * config = mainController->getConfig();
			Assert::IsNotNull(config);

			Assert::AreEqual(config->stepDelta, 1.0 );


			DefaultExperimentStruct * defaultExperimentStruct = config->defaultExperimentStruct;
			Assert::IsNotNull(defaultExperimentStruct);

			Assert::AreEqual(defaultExperimentStruct->startTime, 28000.0);
			Assert::AreEqual(defaultExperimentStruct->stopTime, 86400.0);


		}


		TEST_METHOD(Test04_FMImodelAttributes)
		{

			Init03_GetConfig();

			MainDataModel * mainDataModel = mainController->getMainDataModel();
			AttributeStruct * ary = mainDataModel->getFmiModelAttributesStruct();
			Assert::IsNotNull(ary);

			AttributeStruct  att = ary[0];

			Assert::AreEqual(ary[0].name, "fmiVersion");
			Assert::AreEqual(ary[0].value, "1.0");

			Assert::AreEqual(ary[1].name, "modelName");
			Assert::AreEqual(ary[1].value, "LearnGB_v4_2.VAVReheat.ClosedLoop");

			Assert::AreEqual(ary[2].name, "modelIdentifier");
			Assert::AreEqual(ary[2].value, "LearnGB_0v4_02_VAVReheat_ClosedLoop");

			Assert::AreEqual(ary[3].name, "guid");
			Assert::AreEqual(ary[3].value, "{a682377c-3cd5-4bb9-9844-b0e426b1eb15}");

			Assert::AreEqual(ary[4].name, "description");
			Assert::AreEqual(ary[4].value, "Variable air volume flow system with terminal reheat and five thermal zones");

			Assert::AreEqual(ary[5].name, "version");
			Assert::AreEqual(ary[5].value, "5");

			Assert::AreEqual(ary[6].name, "generationTool");
			Assert::AreEqual(ary[6].value, "Dymola Version 2013 (32-bit), 2012-03-28");

			Assert::AreEqual(ary[7].name, "generationDateAndTime");
			Assert::AreEqual(ary[7].value, "2012-09-07T23:42:12Z");

			Assert::AreEqual(ary[8].name, "variableNamingConvention");
			Assert::AreEqual(ary[8].value, "structured");

			Assert::AreEqual(ary[9].name, "numberOfContinuousStates");
			Assert::AreEqual(ary[9].value, "358");

			Assert::AreEqual(ary[10].name, "numberOfEventIndicators");
			Assert::AreEqual(ary[10].value, "1352");

		}


		TEST_METHOD(Test05_BaseUnitStruct)
		{

			Init03_GetConfig();

			MainDataModel * mainDataModel = mainController->getMainDataModel();
			Assert::IsNotNull(mainDataModel);

			BaseUnitStruct * baseUnitStructAry = mainDataModel->getBaseUnitStructAry();
			BaseUnitStruct  buStruct = baseUnitStructAry[0];
			Assert::AreEqual(buStruct.unit, "K");

			DisplayUnitDefinitionStruct * duStructAry = buStruct.displayUnitDefinitions;

			DisplayUnitDefinitionStruct  duStruct1 = duStructAry[0];
			Assert::AreEqual(duStruct1.displayUnit, "K");
			
			DisplayUnitDefinitionStruct  duStruct2 = duStructAry[1];
			Assert::AreEqual(duStruct2.displayUnit, "degC");
			Assert::AreEqual(duStruct2.offset, "-273.15");

			buStruct = baseUnitStructAry[1];
			Assert::AreEqual(buStruct.unit, "Pa");

			duStructAry = buStruct.displayUnitDefinitions;

			duStruct1 = duStructAry[0];
			Assert::AreEqual(duStruct1.displayUnit, "Pa");

			duStruct2 = duStructAry[1];
			Assert::AreEqual(duStruct2.displayUnit, "bar");
			Assert::AreEqual(duStruct2.offset, "{not set}");
			Assert::AreEqual(duStruct2.gain, "1E-005");

		}


	};






}
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

	void resultCallback2(ScalarValueResultsStruct * scalarValueResultsStruct) {
		static_scalarValueResultsStruct = scalarValueResultsStruct;
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



	TEST_CLASS(TestJNAfunctions)
	{

		TEST_METHOD(TestConnect)
		{

			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);


			forceCleanup();
			Assert::IsTrue(true);
		}

		TEST_METHOD(TestXmlParse)
		{

			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);

			xmlParse(FMU_FOLDER);

			forceCleanup();
			Assert::IsTrue(true);
		}



		TEST_METHOD(TestConfig)
		{

			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);

			xmlParse(FMU_FOLDER);

			ConfigStruct * configStruct = getConfig();

			DefaultExperimentStruct * defaultExperimentStruct = configStruct->defaultExperimentStruct;
			Assert::AreEqual(defaultExperimentStruct->startTime, 28000.0);
			Assert::AreEqual(defaultExperimentStruct->stopTime, 86400.0);
			Assert::AreEqual(defaultExperimentStruct->tolerance, 9.9999999999999995e-007);

			double stepDelta = configStruct->stepDelta;
			Assert::AreEqual(stepDelta, 1.0);


			forceCleanup();
			Assert::IsTrue(true);
		}



		TEST_METHOD(TestAttributes)
		{

			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);

			xmlParse(FMU_FOLDER);


			AttributeStruct * ary = getFmiModelAttributes();

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


			forceCleanup();
			Assert::IsTrue(true);
		}


		TEST_METHOD(TestUnitDefinitions)
		{


			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);

			xmlParse(FMU_FOLDER);


			BaseUnitStruct *  baseUnitStructAry = getUnitDefinitions();
			BaseUnitStruct baseUnitStruct;
			DisplayUnitDefinitionStruct duStruct;

			baseUnitStruct = baseUnitStructAry[0];
			Assert::AreEqual(baseUnitStruct.unit, "K");

			duStruct = baseUnitStruct.displayUnitDefinitions[0];
			Assert::AreEqual(duStruct.displayUnit, "K");

			duStruct = baseUnitStruct.displayUnitDefinitions[1];
			Assert::AreEqual(duStruct.displayUnit, "degC");
			Assert::AreEqual(duStruct.offset, "-273.15");
		

			baseUnitStruct = baseUnitStructAry[1];
			Assert::AreEqual(baseUnitStruct.unit, "Pa");

			duStruct = baseUnitStruct.displayUnitDefinitions[0];
			Assert::AreEqual(duStruct.displayUnit, "Pa");
			duStruct = baseUnitStruct.displayUnitDefinitions[1];
			Assert::AreEqual(duStruct.displayUnit, "bar");
			Assert::AreEqual(duStruct.gain, "1E-005");


			baseUnitStruct = baseUnitStructAry[2];
			Assert::AreEqual(baseUnitStruct.unit, "W");

			duStruct = baseUnitStruct.displayUnitDefinitions[0];
			Assert::AreEqual(duStruct.displayUnit, "kW");
			Assert::AreEqual(duStruct.gain, "0.001");


			forceCleanup();
			Assert::IsTrue(true);

		}

		

		TEST_METHOD(TestScalarVariables)
		{

			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);

			xmlParse(FMU_FOLDER);

			ScalarVariablesAllStruct * ary = getAllScalarVariables();

			ScalarVariablesAllStruct svStruct = ary[0];

			ScalarVariableCollectionStruct * input = svStruct.input;
			ScalarVariableCollectionStruct * output = svStruct.output;
			ScalarVariableCollectionStruct * internal = svStruct.internal;
			
			ScalarVariableRealStruct realValue = input->realValue[0];
			Assert::AreEqual(input->realSize, 107);


			Assert::AreEqual(realValue.name, "occSch.occupancy[1]");
			Assert::AreEqual(realValue.idx, 2711);
			Assert::AreEqual((int)realValue.causality, 6); //enu_input
			Assert::AreEqual((int)realValue.variability, 3); //enu_parameter
			Assert::AreEqual(realValue.description, "Occupancy table, each entry switching occupancy on or off");
			Assert::AreEqual(realValue.valueReference, (unsigned int)16777475);

			TypeSpecReal  * typeSpecReal = realValue.typeSpecReal;

			Assert::AreEqual(typeSpecReal->start, 21600.0);
			Assert::AreEqual(typeSpecReal->nominal, 0.0);

			Assert::AreEqual(typeSpecReal->min, 0.0);
			Assert::AreEqual(typeSpecReal->max, 86400.0);

			Assert::AreEqual(typeSpecReal->startValueStatus, 1);
			Assert::AreEqual(typeSpecReal->nominalValueStatus, 0);

			Assert::AreEqual(typeSpecReal->minValueStatus, 0);
			Assert::AreEqual(typeSpecReal->maxValueStatus, 1);

			Assert::AreEqual(typeSpecReal->unit, "{no unit}");


			realValue = input->realValue[1];
			Assert::AreEqual(realValue.name, "occSch.occupancy[2]");
			Assert::AreEqual(realValue.idx, 2712);
			Assert::AreEqual((int)realValue.causality, 6); //enu_input
			Assert::AreEqual((int)realValue.variability, 3); //enu_parameter
			Assert::AreEqual(realValue.description, "Occupancy table, each entry switching occupancy on or off");
			Assert::AreEqual(realValue.valueReference, (unsigned int)16777476);

			typeSpecReal = realValue.typeSpecReal;

			Assert::AreEqual(typeSpecReal->start, 68400.0);
			Assert::AreEqual(typeSpecReal->nominal, 0.0);

			Assert::AreEqual(typeSpecReal->min, 0.0);
			Assert::AreEqual(typeSpecReal->max, 86400.0);

			Assert::AreEqual(typeSpecReal->startValueStatus, 1);
			Assert::AreEqual(typeSpecReal->nominalValueStatus, 0);

			Assert::AreEqual(typeSpecReal->minValueStatus, 0);
			Assert::AreEqual(typeSpecReal->maxValueStatus, 1);

			Assert::AreEqual(typeSpecReal->unit, "{no unit}");


			realValue = input->realValue[2];
			Assert::AreEqual(realValue.name, "u_ZN[1]");
			Assert::AreEqual(realValue.idx, 56960);
			Assert::AreEqual((int)realValue.causality, 6); //enu_input
			Assert::AreEqual((int)realValue.variability, 5); //enu_parameter
			Assert::AreEqual(realValue.description, "Zone 1 (North) heating set point");
			Assert::AreEqual(realValue.valueReference, (unsigned int)352321536);

			typeSpecReal = realValue.typeSpecReal;

			Assert::AreEqual(typeSpecReal->start, 291.14999999999998);
			Assert::AreEqual(typeSpecReal->nominal, 291.14999999999998);

			Assert::AreEqual(typeSpecReal->min, 283.14999999999998);
			Assert::AreEqual(typeSpecReal->max, 313.14999999999998);

			Assert::AreEqual(typeSpecReal->startValueStatus, 1);
			Assert::AreEqual(typeSpecReal->nominalValueStatus, 1);

			Assert::AreEqual(typeSpecReal->minValueStatus, 1);
			Assert::AreEqual(typeSpecReal->maxValueStatus, 1);

			Assert::AreEqual(typeSpecReal->unit, "{no unit}");

			forceCleanup();
			Assert::IsTrue(true);
		}



		TEST_METHOD(TestScalarValues)
		{

			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_0_uninitialized);

			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);

			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_1_connect_completed);

			xmlParse(FMU_FOLDER);
			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_2_xmlParse_completed);

			requestStateChange(simStateNative_3_init_requested);
			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_3_ready);

			requestStateChange(simStateNative_5_step_requested);
			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_3_ready);


			ScalarValueCollectionStruct * inputAry = static_scalarValueResultsStruct->input;
			ScalarValueCollectionStruct * outputAry = static_scalarValueResultsStruct->output;

			ScalarValueCollectionStruct intputReal = inputAry[0];
			int realSize = intputReal.realSize;
			Assert::AreEqual(realSize, 107);

			ScalarValueRealStruct * realValueStruct = intputReal.realValue;

			int idx = realValueStruct->idx;
			double value = realValueStruct->value;

			Assert::AreEqual(idx, 2711);
			Assert::AreEqual(value, 21600.0);


			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_3_ready);


			forceCleanup();

		}

		TEST_METHOD(TestTerminate)
		{

			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_0_uninitialized);

			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);

			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_1_connect_completed);

			xmlParse(FMU_FOLDER);
			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_2_xmlParse_completed);

			requestStateChange(simStateNative_3_init_requested);
			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_3_ready);

			requestStateChange(simStateNative_5_step_requested);
			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_3_ready);

			requestStateChange(simStateNative_5_step_requested);
			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_3_ready);

			requestStateChange(simStateNative_7_terminate_requested);
			Assert::AreEqual(static_simStateNative, SimStateNative::simStateNative_7_terminate_completed);

			forceCleanup();

		}



		






	};


}
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


		TEST_METHOD(C01_Connect)
		{
			Config::getInstance()->setAutoCorrect(true);

			SimStateNative state = mainController->getState();
			Assert::AreEqual(SimStateNative::simStateNative_0_uninitialized, state);

			int state2 = (int)state;
			Assert::AreEqual(0, state2);

			mainController->connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);


			state = mainController->getState();

			Assert::AreEqual(SimStateNative::simStateNative_1_connect_completed, state);
			Assert::AreEqual(SimStateNative::simStateNative_1_connect_completed, static_simStateNative);

			MainDataModel *  mainDataModel = mainController->getMainDataModel();
			Assert::IsNotNull(mainDataModel);

		}


		TEST_METHOD(C02_XMLParse)
		{

			C01_Connect();

			int result = mainController->xmlParse(FMU_FOLDER);

			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, static_simStateNative);
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, mainController->getState());

			MainDataModel * mainDataModel = mainController->getMainDataModel();
			
			TypeDefinitions * typeDefinitions = mainDataModel->getTypeDefinitions();
			Assert::IsNotNull(typeDefinitions);

		}


		TEST_METHOD(C03_GetConfig)
		{

			C02_XMLParse();

			ConfigStruct * config = mainController->getConfig();
			Assert::IsNotNull(config);

			Assert::AreEqual(config->stepDelta, 1.0);

			DefaultExperimentStruct * defaultExperimentStruct = config->defaultExperimentStruct;
			Assert::IsNotNull(defaultExperimentStruct);

			Assert::AreEqual(defaultExperimentStruct->startTime, 28000.0);
			Assert::AreEqual(defaultExperimentStruct->stopTime, 86400.0);


		}


		TEST_METHOD(C04_FMImodelAttributes)
		{

			C03_GetConfig();

			MainDataModel * mainDataModel = mainController->getMainDataModel();
			AttributeStruct * ary = mainDataModel->getFmiModelAttributesStruct();
			Assert::IsNotNull(ary);

			AttributeStruct  att = ary[0];

			Assert::AreEqual("fmiVersion", ary[0].name);
			Assert::AreEqual("1.0", ary[0].value);

			Assert::AreEqual("modelName", ary[1].name);
			Assert::AreEqual("LearnGB_v4_2.VAVReheat.ClosedLoop", ary[1].value);

			Assert::AreEqual("modelIdentifier", ary[2].name);
			Assert::AreEqual("LearnGB_0v4_02_VAVReheat_ClosedLoop", ary[2].value);

			Assert::AreEqual("guid", ary[3].name);
			Assert::AreEqual("{a682377c-3cd5-4bb9-9844-b0e426b1eb15}", ary[3].value);

			Assert::AreEqual("description", ary[4].name);
			Assert::AreEqual("Variable air volume flow system with terminal reheat and five thermal zones", ary[4].value);

			Assert::AreEqual("version", ary[5].name);
			Assert::AreEqual("5", ary[5].value);

			Assert::AreEqual("generationTool", ary[6].name);
			Assert::AreEqual("Dymola Version 2013 (32-bit), 2012-03-28", ary[6].value);

			Assert::AreEqual("generationDateAndTime", ary[7].name);
			Assert::AreEqual("2012-09-07T23:42:12Z", ary[7].value);

			Assert::AreEqual("variableNamingConvention", ary[8].name);
			Assert::AreEqual("structured", ary[8].value);

			Assert::AreEqual("numberOfContinuousStates", ary[9].name);
			Assert::AreEqual("358", ary[9].value);

			Assert::AreEqual("numberOfEventIndicators", ary[10].name);
			Assert::AreEqual("1352", ary[10].value);

		}






		TEST_METHOD(C05_BaseUnitStruct)
		{

			C03_GetConfig();

			MainDataModel * mainDataModel = mainController->getMainDataModel();
			Assert::IsNotNull(mainDataModel);

			BaseUnitStruct * baseUnitStructAry = mainDataModel->getBaseUnitStructAry();
			BaseUnitStruct  buStruct = baseUnitStructAry[0];
			Assert::AreEqual("K", buStruct.unit);

			DisplayUnitDefinitionStruct * duStructAry = buStruct.displayUnitDefinitions;

			DisplayUnitDefinitionStruct  duStruct1 = duStructAry[0];
			Assert::AreEqual("K", duStruct1.displayUnit);

			DisplayUnitDefinitionStruct  duStruct2 = duStructAry[1];
			Assert::AreEqual("degC", duStruct2.displayUnit);
			Assert::AreEqual("-273.15", duStruct2.offset);

			buStruct = baseUnitStructAry[1];
			Assert::AreEqual("Pa", buStruct.unit);

			duStructAry = buStruct.displayUnitDefinitions;

			duStruct1 = duStructAry[0];
			Assert::AreEqual("Pa", duStruct1.displayUnit);

			duStruct2 = duStructAry[1];
			Assert::AreEqual("bar", duStruct2.displayUnit);
			Assert::AreEqual("{not set}", duStruct2.offset);
			Assert::AreEqual("1E-005", duStruct2.gain);

		}


		TEST_METHOD(C06_Step)
		{

			C03_GetConfig();
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, static_simStateNative);

			mainController->requestStateChange(SimStateNative::simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);

			mainController->requestStateChange(SimStateNative::simStateNative_5_step_requested);
			ScalarValueResults * scalarValueResult = mainController->getScalarValueResults();


			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);
		}




		TEST_METHOD(C07_GetOneScalarValue)
		{

			C03_GetConfig();
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, static_simStateNative);

			mainController->requestStateChange(SimStateNative::simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);


			//make request
			ScalarValueRealStruct * scalarValueRealStruct = mainController->getOneScalarValueStruct(56960);

			Assert::AreEqual(56960, scalarValueRealStruct->idx);
			Assert::AreEqual(291.14999999999998, scalarValueRealStruct->value);

			return;
		}


		TEST_METHOD(C08_GetOneScalarVariable)
		{

			C03_GetConfig();
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, static_simStateNative);

			mainController->requestStateChange(SimStateNative::simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);


			//make request
			ScalarVariableRealStruct * scalarVariableRealStruct = mainController->getOneScalarVariableStruct(56960);
			Assert::AreEqual(56960, scalarVariableRealStruct->idx);

			Assert::AreEqual("u_ZN[1]", scalarVariableRealStruct->name);

			Assert::AreEqual("Zone 1 (North) heating set point", scalarVariableRealStruct->description);
			Assert::AreEqual((unsigned int) 352321536, scalarVariableRealStruct->valueReference);

			Assert::AreEqual(Enu::enu_input, scalarVariableRealStruct->causality);
			Assert::AreEqual(Enu::enu_continuous, scalarVariableRealStruct->variability);

			Assert::AreEqual(291.14999999999998, scalarVariableRealStruct->typeSpecReal->start);
			Assert::AreEqual(291.14999999999998, scalarVariableRealStruct->typeSpecReal->nominal);

			Assert::AreEqual(283.14999999999998, scalarVariableRealStruct->typeSpecReal->min);
			Assert::AreEqual(313.14999999999998, scalarVariableRealStruct->typeSpecReal->max);

			Assert::AreEqual("{no unit}", scalarVariableRealStruct->typeSpecReal->unit);

			Assert::AreEqual(1, scalarVariableRealStruct->typeSpecReal->startValueStatus);
			Assert::AreEqual(1, scalarVariableRealStruct->typeSpecReal->nominalValueStatus);
			Assert::AreEqual(1, scalarVariableRealStruct->typeSpecReal->minValueStatus);
			Assert::AreEqual(1, scalarVariableRealStruct->typeSpecReal->maxValueStatus);

			Assert::AreEqual(0, scalarVariableRealStruct->typeSpecReal->unitValueStatus);

			return;
		}



		TEST_METHOD(C09_SetOneScalarValueContinous)
		{

			C03_GetConfig();
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, static_simStateNative);

			mainController->requestStateChange(SimStateNative::simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);



			//formulate request
			ScalarValueRealStruct * scalarValueRealStruct1 = new ScalarValueRealStruct();
			scalarValueRealStruct1->idx = 56960;
			scalarValueRealStruct1->value = 301.1;

			//make request
			fmiStatus status = mainController->setOneScalarValue(scalarValueRealStruct1);

			Assert::AreEqual(fmiStatus::fmiOK, status);

		}


		TEST_METHOD(C10_SetOneScalarValue)
		{

			C03_GetConfig();
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, static_simStateNative);

			mainController->requestStateChange(SimStateNative::simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);


			//change a scalarValue
			ScalarValueRealStruct * scalarValueRealStruct1 = new ScalarValueRealStruct();
			scalarValueRealStruct1->idx = 56960;
			scalarValueRealStruct1->value = 301.1;

			fmiStatus status = mainController->setOneScalarValue(scalarValueRealStruct1);

			Assert::AreEqual(fmiStatus::fmiOK, status);

		}




		//TEST_METHOD(C07_SetOneScalarValue)
		//{

		//	C03_GetConfig();
		//	Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, static_simStateNative);

		//	mainController->requestStateChange(SimStateNative::simStateNative_3_init_requested);
		//	Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);

		//	mainController->requestStateChange(SimStateNative::simStateNative_5_step_requested);
		//	ScalarValueResults * scalarValueResult = mainController->getScalarValueResults();

		//	Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);


		//	ScalarValueRealStruct * ary = new ScalarValueRealStruct[1];

		//	ScalarValueRealStruct * scalarValueRealStruct = new ScalarValueRealStruct();
		//	scalarValueRealStruct->idx = 56960;
		//	scalarValueRealStruct->value = 301.1;

		//	ary[0] = *scalarValueRealStruct;


		//	mainController->setScalarValues(ary, 1);


		//}





	};



	TEST_CLASS(TestJNAfunctions)
	{

		TEST_METHOD(T01_Connect)
		{

			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);


			forceCleanup();
			Assert::IsTrue(true);
		}

		TEST_METHOD(T01B_XmlParse)
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



		TEST_METHOD(T02_Config)
		{

			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);

			xmlParse(FMU_FOLDER);

			ConfigStruct * configStruct = getConfig();

			DefaultExperimentStruct * defaultExperimentStruct = configStruct->defaultExperimentStruct;
			Assert::AreEqual(28000.0, defaultExperimentStruct->startTime);
			Assert::AreEqual(86400.0, defaultExperimentStruct->stopTime);
			Assert::AreEqual(9.9999999999999995e-007, defaultExperimentStruct->tolerance);

			double stepDelta = configStruct->stepDelta;
			Assert::AreEqual(1.0, stepDelta);


			forceCleanup();
			Assert::IsTrue(true);
		}



		TEST_METHOD(T03_Attributes)
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

			Assert::AreEqual("fmiVersion", ary[0].name);
			Assert::AreEqual("1.0", ary[0].value);

			Assert::AreEqual("modelName", ary[1].name);
			Assert::AreEqual("LearnGB_v4_2.VAVReheat.ClosedLoop", ary[1].value);

			Assert::AreEqual("modelIdentifier", ary[2].name);
			Assert::AreEqual("LearnGB_0v4_02_VAVReheat_ClosedLoop", ary[2].value);

			Assert::AreEqual("guid", ary[3].name);
			Assert::AreEqual("{a682377c-3cd5-4bb9-9844-b0e426b1eb15}", ary[3].value);

			Assert::AreEqual("description", ary[4].name);
			Assert::AreEqual("Variable air volume flow system with terminal reheat and five thermal zones", ary[4].value);

			Assert::AreEqual("version", ary[5].name);
			Assert::AreEqual("5", ary[5].value);

			Assert::AreEqual("generationTool", ary[6].name);
			Assert::AreEqual("Dymola Version 2013 (32-bit), 2012-03-28", ary[6].value);

			Assert::AreEqual("generationDateAndTime", ary[7].name);
			Assert::AreEqual("2012-09-07T23:42:12Z", ary[7].value);

			Assert::AreEqual("variableNamingConvention", ary[8].name);
			Assert::AreEqual("structured", ary[8].value);

			Assert::AreEqual("numberOfContinuousStates", ary[9].name);
			Assert::AreEqual("358", ary[9].value);


			forceCleanup();
			Assert::IsTrue(true);
		}


		TEST_METHOD(T04_UnitDefinitions)
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
			Assert::AreEqual("K", baseUnitStruct.unit);

			duStruct = baseUnitStruct.displayUnitDefinitions[0];
			Assert::AreEqual("K", duStruct.displayUnit);

			duStruct = baseUnitStruct.displayUnitDefinitions[1];
			Assert::AreEqual("degC", duStruct.displayUnit);
			Assert::AreEqual("-273.15", duStruct.offset);


			baseUnitStruct = baseUnitStructAry[1];
			Assert::AreEqual("Pa", baseUnitStruct.unit);

			duStruct = baseUnitStruct.displayUnitDefinitions[0];
			Assert::AreEqual("Pa", duStruct.displayUnit);
			duStruct = baseUnitStruct.displayUnitDefinitions[1];
			Assert::AreEqual("bar", duStruct.displayUnit );
			Assert::AreEqual("1E-005", duStruct.gain);


			baseUnitStruct = baseUnitStructAry[2];
			Assert::AreEqual("W", baseUnitStruct.unit);

			duStruct = baseUnitStruct.displayUnitDefinitions[0];
			Assert::AreEqual("kW", duStruct.displayUnit);
			Assert::AreEqual("0.001", duStruct.gain);


			forceCleanup();
			Assert::IsTrue(true);

		}

		

		TEST_METHOD(T05_ScalarVariables)
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
			Assert::AreEqual(107, input->realSize);


			Assert::AreEqual("occSch.occupancy[1]", realValue.name);
			Assert::AreEqual(2711, realValue.idx);
			Assert::AreEqual(6, (int)realValue.causality); //enu_input
			Assert::AreEqual(3, (int)realValue.variability); //enu_parameter
			Assert::AreEqual("Occupancy table, each entry switching occupancy on or off", realValue.description);
			Assert::AreEqual((unsigned int)16777475, realValue.valueReference);

			TypeSpecReal  * typeSpecReal = realValue.typeSpecReal;

			Assert::AreEqual(21600.0, typeSpecReal->start);
			Assert::AreEqual(0.0, typeSpecReal->nominal);

			Assert::AreEqual(0.0, typeSpecReal->min);
			Assert::AreEqual(86400.0, typeSpecReal->max);

			Assert::AreEqual(1, typeSpecReal->startValueStatus);
			Assert::AreEqual(0, typeSpecReal->nominalValueStatus);

			Assert::AreEqual(0, typeSpecReal->minValueStatus);
			Assert::AreEqual(1, typeSpecReal->maxValueStatus);

			Assert::AreEqual("{no unit}", typeSpecReal->unit);


			realValue = input->realValue[1];
			Assert::AreEqual("occSch.occupancy[2]", realValue.name);
			Assert::AreEqual(2712, realValue.idx);
			Assert::AreEqual(6, (int)realValue.causality); //enu_input
			Assert::AreEqual(3, (int)realValue.variability); //enu_parameter
			Assert::AreEqual("Occupancy table, each entry switching occupancy on or off", realValue.description);
			Assert::AreEqual((unsigned int)16777476, realValue.valueReference);

			typeSpecReal = realValue.typeSpecReal;

			Assert::AreEqual(68400.0, typeSpecReal->start);
			Assert::AreEqual(0.0, typeSpecReal->nominal);

			Assert::AreEqual(0.0, typeSpecReal->min);
			Assert::AreEqual(86400.0, typeSpecReal->max);

			Assert::AreEqual(1, typeSpecReal->startValueStatus);
			Assert::AreEqual(0, typeSpecReal->nominalValueStatus);

			Assert::AreEqual(0, typeSpecReal->minValueStatus);
			Assert::AreEqual(1, typeSpecReal->maxValueStatus);

			Assert::AreEqual("{no unit}", typeSpecReal->unit);


			realValue = input->realValue[2];
			Assert::AreEqual("u_ZN[1]", realValue.name);
			Assert::AreEqual(56960, realValue.idx);
			Assert::AreEqual(6, (int)realValue.causality); //enu_input
			Assert::AreEqual(5, (int)realValue.variability); //enu_parameter
			Assert::AreEqual("Zone 1 (North) heating set point", realValue.description);
			Assert::AreEqual((unsigned int)352321536, realValue.valueReference);

			typeSpecReal = realValue.typeSpecReal;

			Assert::AreEqual(291.14999999999998, typeSpecReal->start);
			Assert::AreEqual(291.14999999999998, typeSpecReal->nominal);

			Assert::AreEqual(283.14999999999998, typeSpecReal->min);
			Assert::AreEqual(313.14999999999998, typeSpecReal->max);

			Assert::AreEqual(1, typeSpecReal->startValueStatus);
			Assert::AreEqual(1, typeSpecReal->nominalValueStatus);

			Assert::AreEqual(1, typeSpecReal->minValueStatus);
			Assert::AreEqual(1, typeSpecReal->maxValueStatus);

			Assert::AreEqual("{no unit}", typeSpecReal->unit);

			forceCleanup();
			Assert::IsTrue(true);
		}


		TEST_METHOD(T06_OutputScalarValuesAfterOneStep)
		{

			bool isCorrectState = (
				static_simStateNative == simStateNative_0_uninitialized ||
				static_simStateNative == simStateNative_8_tearDown_completed
				);

			Assert::IsTrue(isCorrectState);

			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);

			Assert::AreEqual(SimStateNative::simStateNative_1_connect_completed, static_simStateNative);

			xmlParse(FMU_FOLDER);
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, static_simStateNative);

			requestStateChange(simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);

			//run the simulation for one step
			requestStateChange(simStateNative_5_step_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);

			//these are the input and output values after one step
			ScalarValueCollectionStruct * outputAry = static_scalarValueResultsStruct->output;

			//Check to see that there a certain number of input values
			ScalarValueCollectionStruct outputScalarValueCollection = outputAry[0];
			int len = outputScalarValueCollection.realSize;
			Assert::AreEqual(138, len, L"unexpected number of output values");

			ScalarValueRealStruct * realValueStruct = outputScalarValueCollection.realValue;

			ScalarValueRealStruct realValueStruct_0 = realValueStruct[0];

			int idx_0 = realValueStruct_0.idx;
			double value_0 = realValueStruct_0.value;

			Assert::AreEqual(61807, idx_0, L"unexpected idx for ScalarValueRealStruct");
			Assert::AreEqual(293.14999999999867, value_0, L"unexpected value for ScalarValueRealStruct");


			ScalarValueRealStruct realValueStruct_1 = realValueStruct[1];

			int idx_1 = realValueStruct_1.idx;
			double value_1 = realValueStruct_1.value;

			Assert::AreEqual(61808, idx_1 , L"unexpected idx for ScalarValueRealStruct");
			Assert::AreEqual(293.14999995506417, value_1, L"unexpected value for ScalarValueRealStruct");

			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);


			forceCleanup();

		}


		TEST_METHOD(T07_InputScalarValuesAfterOneStep)
		{

			bool isCorrectState = (
				static_simStateNative == simStateNative_0_uninitialized ||
				static_simStateNative == simStateNative_8_tearDown_completed
				);

			Assert::IsTrue(isCorrectState);

			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);

			Assert::AreEqual(SimStateNative::simStateNative_1_connect_completed, static_simStateNative);

			xmlParse(FMU_FOLDER);
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, static_simStateNative);

			requestStateChange(simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);

			//run the simulation for one step
			requestStateChange(simStateNative_5_step_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);

			//these are the input and output values after one step
			ScalarValueCollectionStruct * inputAry = static_scalarValueResultsStruct->input;
			ScalarValueCollectionStruct * outputAry = static_scalarValueResultsStruct->output;

			//Check to see that there a certain number of input values
			ScalarValueCollectionStruct intputScalarValueCollection = inputAry[0];
			int len = intputScalarValueCollection.realSize;
			Assert::AreEqual(107, len, L"unexpected number of input values");

			ScalarValueRealStruct * realValueStruct = intputScalarValueCollection.realValue;

			ScalarValueRealStruct realValueStruct_0 = realValueStruct[0];

			int idx_0 = realValueStruct_0.idx;
			double value_0 = realValueStruct_0.value;

			Assert::AreEqual(2711, idx_0, L"unexpected idx for ScalarValueRealStruct");
			Assert::AreEqual(21600.0, value_0, L"unexpected value for ScalarValueRealStruct");


			ScalarValueRealStruct realValueStruct_1 = realValueStruct[1];

			int idx_1 = realValueStruct_1.idx;
			double value_1 = realValueStruct_1.value;

			Assert::AreEqual(2712, idx_1, L"unexpected idx for ScalarValueRealStruct");
			Assert::AreEqual(68400.0, value_1, L"unexpected value for ScalarValueRealStruct");

			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);


			forceCleanup();

		}

		TEST_METHOD(T08_Terminate)
		{

			bool isCorrectState = (
				static_simStateNative == simStateNative_0_uninitialized ||
				static_simStateNative == simStateNative_8_tearDown_completed
				);

			Assert::IsTrue(isCorrectState);

			connect(
				messageCallback2,
				resultCallback2,
				fmuStateCallback2
				);

			Assert::AreEqual(SimStateNative::simStateNative_1_connect_completed, static_simStateNative);

			xmlParse(FMU_FOLDER);
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, static_simStateNative);

			requestStateChange(simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);

			requestStateChange(simStateNative_5_step_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);

			requestStateChange(simStateNative_5_step_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, static_simStateNative);

			requestStateChange(simStateNative_7_terminate_requested);
			Assert::AreEqual(SimStateNative::simStateNative_7_terminate_completed, static_simStateNative);
			forceCleanup();

		}



		






	};


}
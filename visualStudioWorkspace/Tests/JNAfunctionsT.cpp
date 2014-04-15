#include "JNAfunctionsT.h"


using namespace Microsoft::VisualStudio::CppUnitTestFramework;
using namespace Straylight;



namespace StraylightTests
{


	TEST_CLASS(JNAfunctionsTest)
	{


		void JNAfunctionsTest::assertSimState_(SimStateNative simStateNativeExpected) {

			SimStateNative simStateNativeActual = getSimStateNative();
			Assert::AreEqual(simStateNativeExpected, simStateNativeActual);

		}


		//runs before each test
		TEST_METHOD_INITIALIZE(beforeEachTest) {
			assertSimState_(simStateNative_0_uninitialized);
		}

		//runs after each test
		TEST_METHOD_CLEANUP(afterEachTest)
		{
			int result_0 = forceCleanup();
			Assert::AreEqual(0, result_0);

			assertSimState_(simStateNative_0_uninitialized);
		}

		TEST_METHOD(T00_ForceCleanup)
		{
			assertSimState_(simStateNative_0_uninitialized);
		}



		TEST_METHOD(T01_Connect)
		{

			connect(
				Utils::messageCallbackFunc,
				Utils::resultCallbackFunc,
				Utils::stateChangeCallbackFunc
				);

			assertSimState_(simStateNative_1_connect_completed);

		}

		TEST_METHOD(T02_XmlParse)
		{
			T01_Connect();
			xmlParse(FMU_FOLDER);
			assertSimState_(simStateNative_2_xmlParse_completed);
		}



		TEST_METHOD(T03_Config)
		{
			T02_XmlParse();

			ConfigStruct * configStruct = getConfig();

			DefaultExperimentStruct * defaultExperimentStruct = configStruct->defaultExperimentStruct;
			Assert::AreEqual(28000.0, defaultExperimentStruct->startTime);
			Assert::AreEqual(86400.0, defaultExperimentStruct->stopTime);
			Assert::AreEqual(9.9999999999999995e-007, defaultExperimentStruct->tolerance);

			double stepDelta = configStruct->stepDelta;
			Assert::AreEqual(1.0, stepDelta);

			forceCleanup();

			assertSimState_(simStateNative_0_uninitialized);

		}



		TEST_METHOD(T04_Attributes)
		{
			T02_XmlParse();

			FMImodelAttributesStruct * fmiModelAttributesStruct = getFMImodelAttributesStruct();

			AttributeStruct * ary = fmiModelAttributesStruct->attributeStructAry;
			int size = fmiModelAttributesStruct->attributeStructSize;

			Assert::IsNotNull(ary);
			AttributeStruct  att = ary[0];

			Assert::AreEqual("1.0", ary[0].value);
			Assert::AreEqual("fmiVersion", ary[0].name);

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




		}


		TEST_METHOD(T05_UnitDefinitions)
		{

			T02_XmlParse();

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
			Assert::AreEqual("bar", duStruct.displayUnit);
			Assert::AreEqual("1E-005", duStruct.gain);


			baseUnitStruct = baseUnitStructAry[2];
			Assert::AreEqual("W", baseUnitStruct.unit);

			duStruct = baseUnitStruct.displayUnitDefinitions[0];
			Assert::AreEqual("kW", duStruct.displayUnit);
			Assert::AreEqual("0.001", duStruct.gain);



		}



		TEST_METHOD(T06_ScalarVariables)
		{

			T02_XmlParse();

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


		}


		TEST_METHOD(T07_OutputScalarValuesAfterOneStep)
		{

			T02_XmlParse();

			requestStateChange(simStateNative_3_init_requested);
			assertSimState_(simStateNative_3_ready);

			//run the simulation for one step
			requestStateChange(simStateNative_5_step_requested);

			assertSimState_(simStateNative_3_ready);

			//these are the input and output values after one step
			ScalarValueCollectionStruct * outputAry = Utils::scalarValueResultsStruct->output;

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

			Assert::AreEqual(61808, idx_1, L"unexpected idx for ScalarValueRealStruct");
			Assert::AreEqual(293.14999995506417, value_1, L"unexpected value for ScalarValueRealStruct");

			assertSimState_(simStateNative_3_ready);


		}


		TEST_METHOD(T08_InputScalarValuesAfterOneStep)
		{

			T02_XmlParse();

			requestStateChange(simStateNative_3_init_requested);
			assertSimState_(simStateNative_3_ready);

			//run the simulation for one step
			requestStateChange(simStateNative_5_step_requested);
			assertSimState_(simStateNative_3_ready);

			//these are the input and output values after one step
			ScalarValueCollectionStruct * inputAry = Utils::scalarValueResultsStruct->input;
			ScalarValueCollectionStruct * outputAry = Utils::scalarValueResultsStruct->output;

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

			assertSimState_(simStateNative_3_ready);


		}

		TEST_METHOD(T09_Terminate)
		{

			T02_XmlParse();

			requestStateChange(simStateNative_3_init_requested);
			assertSimState_(simStateNative_3_ready);

			requestStateChange(simStateNative_5_step_requested);
			assertSimState_(simStateNative_3_ready);

			requestStateChange(simStateNative_5_step_requested);
			assertSimState_(simStateNative_3_ready);

			requestStateChange(simStateNative_7_terminate_requested);
			assertSimState_(simStateNative_7_terminate_completed);


		}

	};


}
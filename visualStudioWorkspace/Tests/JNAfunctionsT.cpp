#include "JNAfunctionsT.h"


using namespace Microsoft::VisualStudio::CppUnitTestFramework;
using namespace Straylight;



namespace StraylightTests
{


	TEST_CLASS(JNAfunctionsT)
	{


		void JNAfunctionsT::assertSimState_(SimStateNative simStateNativeExpected) {

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

		TEST_METHOD(T01_ForceCleanup)
		{
			assertSimState_(simStateNative_0_uninitialized);
		}



		TEST_METHOD(T02_Connect)
		{

			connect(
				Utils::messageCallbackFunc,
				Utils::resultCallbackFunc,
				Utils::stateChangeCallbackFunc
				);

			assertSimState_(simStateNative_1_connect_completed);

		}

		TEST_METHOD(T03_XmlParse)
		{
			T02_Connect();
			xmlParse(FMU_FOLDER);
			assertSimState_(simStateNative_2_xmlParse_completed);
		}



		TEST_METHOD(T04_Config)
		{
			T03_XmlParse();

			ConfigStruct * configStruct = getConfig();

			DefaultExperimentStruct * defaultExperimentStruct = configStruct->defaultExperimentStruct;
			Assert::AreEqual(28000.0, defaultExperimentStruct->startTime);
			Assert::AreEqual(86400.0, defaultExperimentStruct->stopTime);
			Assert::AreEqual(9.9999999999999995e-007, defaultExperimentStruct->tolerance);

			double stepDelta = configStruct->stepDelta;
			Assert::AreEqual(300.0, stepDelta);

			forceCleanup();

			assertSimState_(simStateNative_0_uninitialized);

		}



		TEST_METHOD(T05_Attributes)
		{
			T03_XmlParse();

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


		TEST_METHOD(T06_UnitDefinitions)
		{

			T03_XmlParse();

			BaseUnitStruct *  baseUnitStructAry = getUnitDefinitions();
			BaseUnitStruct baseUnitStruct;
			DisplayUnitDefinitionStruct duStruct;

			baseUnitStruct = baseUnitStructAry[0];
			Assert::AreEqual("K", baseUnitStruct.unit);

			duStruct = baseUnitStruct.displayUnitDefinitions[0];
			Assert::AreEqual("K", duStruct.displayUnit);
			Assert::AreEqual(1, duStruct.displayUnitValueStatus);

			Assert::AreEqual(0.0, duStruct.offset);
			Assert::AreEqual(0, duStruct.offsetValueStatus);

			Assert::AreEqual(0.0, duStruct.gain);
			Assert::AreEqual(0, duStruct.gainValueStatus);

			duStruct = baseUnitStruct.displayUnitDefinitions[1];
			Assert::AreEqual("degC", duStruct.displayUnit);
			Assert::AreEqual(1, duStruct.displayUnitValueStatus);

			Assert::AreEqual(-273.15, duStruct.offset);
			Assert::AreEqual(1, duStruct.offsetValueStatus);

			Assert::AreEqual(0.0, duStruct.gain);
			Assert::AreEqual(0, duStruct.gainValueStatus);

			baseUnitStruct = baseUnitStructAry[1];
			Assert::AreEqual("Pa", baseUnitStruct.unit);

			duStruct = baseUnitStruct.displayUnitDefinitions[0];
			Assert::AreEqual("Pa", duStruct.displayUnit);
			Assert::AreEqual(1, duStruct.displayUnitValueStatus);

			Assert::AreEqual(0.0, duStruct.offset);
			Assert::AreEqual(0, duStruct.offsetValueStatus);

			Assert::AreEqual(0.0, duStruct.gain);
			Assert::AreEqual(0, duStruct.gainValueStatus);

			duStruct = baseUnitStruct.displayUnitDefinitions[1];
			Assert::AreEqual("bar", duStruct.displayUnit);
			Assert::AreEqual(1, duStruct.displayUnitValueStatus);

			Assert::AreEqual(0.0, duStruct.offset);
			Assert::AreEqual(0, duStruct.offsetValueStatus);

			Assert::AreEqual(1E-005, duStruct.gain);
			Assert::AreEqual(1, duStruct.gainValueStatus);

			baseUnitStruct = baseUnitStructAry[2];
			Assert::AreEqual("W", baseUnitStruct.unit);

			duStruct = baseUnitStruct.displayUnitDefinitions[0];
			Assert::AreEqual("kW", duStruct.displayUnit);
			Assert::AreEqual(1, duStruct.displayUnitValueStatus);

			Assert::AreEqual(0.0, duStruct.offset);
			Assert::AreEqual(0, duStruct.offsetValueStatus);

			Assert::AreEqual(0.001, duStruct.gain);
			Assert::AreEqual(1, duStruct.gainValueStatus);

			return;
		}



		TEST_METHOD(T07_ScalarVariables)
		{

			T03_XmlParse();

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


		TEST_METHOD(T08_OutputScalarValuesAfterOneStep)
		{

			T03_XmlParse();

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
			Assert::AreEqual(292.44444329455683, value_0, L"unexpected value for ScalarValueRealStruct");

			ScalarValueRealStruct realValueStruct_1 = realValueStruct[1];

			int idx_1 = realValueStruct_1.idx;
			double value_1 = realValueStruct_1.value;

			Assert::AreEqual(61808, idx_1, L"unexpected idx for ScalarValueRealStruct");
			Assert::AreEqual(291.01500644163440, value_1, L"unexpected value for ScalarValueRealStruct");


			assertSimState_(simStateNative_3_ready);

		}


		TEST_METHOD(T09_InputScalarValuesAfterOneStep)
		{

			T03_XmlParse();

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

		TEST_METHOD(T10_Terminate)
		{

			T03_XmlParse();

			requestStateChange(simStateNative_3_init_requested);
			assertSimState_(simStateNative_3_ready);

			requestStateChange(simStateNative_5_step_requested);
			assertSimState_(simStateNative_3_ready);

			requestStateChange(simStateNative_5_step_requested);
			assertSimState_(simStateNative_3_ready);

			requestStateChange(simStateNative_7_terminate_requested);
			assertSimState_(simStateNative_7_terminate_completed);


		}


		TEST_METHOD(T11_SetOutputVariableNames)
		{

			T02_Connect();

			const char** outputVariableNamesAry = new const char*[3];

			//const char* outputVariableNamesAry = new char[32]; // points to a char array
			outputVariableNamesAry[0] = "y_BOI[1]";
			outputVariableNamesAry[1] = "y_ZN[1]";
			outputVariableNamesAry[2] = "y_ZN[2]";

			setOutputVariableNames(outputVariableNamesAry, 3);

			xmlParse(FMU_FOLDER);
			assertSimState_(simStateNative_2_xmlParse_completed);

			requestStateChange(simStateNative_3_init_requested);
			assertSimState_(simStateNative_3_ready);

			requestStateChange(simStateNative_5_step_requested);
			assertSimState_(simStateNative_3_ready);

			//these are the input and output values after one step
			ScalarValueCollectionStruct * inputAry = Utils::scalarValueResultsStruct->input;
			ScalarValueCollectionStruct * outputAry = Utils::scalarValueResultsStruct->output;

			//Check to see that there a certain number of input values
			ScalarValueCollectionStruct intputScalarValueCollection = inputAry[0];
			int len1 = intputScalarValueCollection.realSize;
			Assert::AreEqual(107, len1, L"unexpected number of input values");

			//Check to see that there a certain number of output values
			ScalarValueCollectionStruct outputScalarValueCollection = outputAry[0];
			int len2 = outputScalarValueCollection.realSize;
			Assert::AreEqual(3, len2, L"unexpected number of output values");

		}

		TEST_METHOD(T12_SetInputVariableNames)
		{

			T02_Connect();

			const char**  inputVariableNamesAry = new const char*[3];

			inputVariableNamesAry[0] = "u_ZN[1]";
			inputVariableNamesAry[1] = "u_ZN[2]";
			inputVariableNamesAry[2] = "u_ZN[5]";

			setInputVariableNames(inputVariableNamesAry, 3);

			xmlParse(FMU_FOLDER);
			assertSimState_(simStateNative_2_xmlParse_completed);

			requestStateChange(simStateNative_3_init_requested);
			assertSimState_(simStateNative_3_ready);

			requestStateChange(simStateNative_5_step_requested);
			assertSimState_(simStateNative_3_ready);

			//these are the input and output values after one step
			ScalarValueCollectionStruct * inputAry = Utils::scalarValueResultsStruct->input;
			ScalarValueCollectionStruct * outputAry = Utils::scalarValueResultsStruct->output;

			//Check to see that there a certain number of input values
			ScalarValueCollectionStruct intputScalarValueCollection = inputAry[0];
			int len1 = intputScalarValueCollection.realSize;

			ScalarValueCollectionStruct outputScalarValueCollection = outputAry[0];
			int len2 = outputScalarValueCollection.realSize;

			Assert::AreEqual(3, len1, L"unexpected number of input values");
			Assert::AreEqual(138, len2, L"unexpected number of output values");

		}




		TEST_METHOD(T13_TypeDefinitionReal)
		{

			T03_XmlParse();

			TypeDefinitionsStruct * typeDefinitionsStruct = getTypeDefinitions();
			Assert::IsNotNull(typeDefinitionsStruct);

			TypeDefinitionReal * typeDefinitionRealArray = typeDefinitionsStruct->typeDefinitionRealArray;

			//TypeDefinition 0
			TypeDefinitionReal * typeDefReal_0 = &typeDefinitionRealArray[0];
			Assert::IsNotNull(typeDefReal_0);

			Assert::AreEqual(0, typeDefReal_0->idx);
			Assert::AreEqual("Type_name_real_0", typeDefReal_0->name.value);
			Assert::AreEqual("RealType_unit_0", typeDefReal_0->unit.value);
			Assert::AreEqual("RealType_quantity_0", typeDefReal_0->quantity.value);
			Assert::AreEqual("RealType_displayUnit_0", typeDefReal_0->displayUnit.value);

			Assert::AreEqual(5.75, typeDefReal_0->start.value);
			Assert::AreEqual(101.51, typeDefReal_0->nominal.value);
			Assert::AreEqual(1.1, typeDefReal_0->min.value);
			Assert::AreEqual(999.1, typeDefReal_0->max.value);

			Assert::AreEqual(valueDefined, typeDefReal_0->start.status);
			Assert::AreEqual(valueDefined, typeDefReal_0->nominal.status);
			Assert::AreEqual(valueDefined, typeDefReal_0->min.status);
			Assert::AreEqual(valueDefined, typeDefReal_0->max.status);

			TypeDefinitionReal * typeDefReal_1 = &typeDefinitionRealArray[1];
			Assert::IsNotNull(typeDefReal_1);

			//TypeDefinition 1
			Assert::AreEqual(1, typeDefReal_1->idx);
			Assert::AreEqual("Type_name_real_1", typeDefReal_1->name.value);
			Assert::AreEqual("RealType_quantity_1", typeDefReal_1->quantity.value);
			Assert::AreEqual("RealType_unit_1", typeDefReal_1->unit.value);
			Assert::AreEqual("RealType_displayUnit_1", typeDefReal_1->displayUnit.value);

			Assert::AreEqual(5.76, typeDefReal_1->start.value);
			Assert::AreEqual(101.52, typeDefReal_1->nominal.value);
			Assert::AreEqual(1.2, typeDefReal_1->min.value);
			Assert::AreEqual(999.2, typeDefReal_1->max.value);

			Assert::AreEqual(valueDefined, typeDefReal_1->start.status);
			Assert::AreEqual(valueDefined, typeDefReal_1->nominal.status);
			Assert::AreEqual(valueDefined, typeDefReal_1->min.status);
			Assert::AreEqual(valueDefined, typeDefReal_1->max.status);

			//TypeDefinition 2
			TypeDefinitionReal * typeDefReal_2 = &typeDefinitionRealArray[2];
			Assert::IsNotNull(typeDefReal_2);

			Assert::AreEqual(2, typeDefReal_2->idx);
			Assert::AreEqual("Type_name_real_2", typeDefReal_2->name.value);
			Assert::AreEqual(NULL, typeDefReal_2->quantity.value);
			Assert::AreEqual(NULL, typeDefReal_2->unit.value);
			Assert::AreEqual(NULL, typeDefReal_2->displayUnit.value);

			Assert::AreEqual(0.0, typeDefReal_2->start.value);
			Assert::AreEqual(0.0, typeDefReal_2->nominal.value);
			Assert::AreEqual(0.0, typeDefReal_2->min.value);
			Assert::AreEqual(0.0, typeDefReal_2->max.value);

			Assert::AreEqual(valueMissing, typeDefReal_2->start.status);
			Assert::AreEqual(valueMissing, typeDefReal_2->nominal.status);
			Assert::AreEqual(valueMissing, typeDefReal_2->min.status);
			Assert::AreEqual(valueMissing, typeDefReal_2->max.status);


			return;
		}


		TEST_METHOD(T14_TypeDefinitionInteger)
		{

			T03_XmlParse();

			TypeDefinitionsStruct * typeDefinitionsStruct = getTypeDefinitions();
			Assert::IsNotNull(typeDefinitionsStruct);

			TypeDefinitionInteger * typeDefinitionIntegerArray = typeDefinitionsStruct->typeDefinitionIntegerArray;
			TypeDefinitionInteger * typeDefInteger_0 = &typeDefinitionIntegerArray[0];

			Assert::IsNotNull(typeDefInteger_0);

			Assert::AreEqual(3, typeDefInteger_0->idx);
			Assert::AreEqual("Type_name_integer_0", typeDefInteger_0->name.value);
			Assert::AreEqual("IntegerType_unit_0", typeDefInteger_0->unit.value);
			Assert::AreEqual("IntegerType_quantity_0", typeDefInteger_0->quantity.value);
			Assert::AreEqual("IntegerType_displayUnit_0", typeDefInteger_0->displayUnit.value);

			Assert::AreEqual(5, typeDefInteger_0->start.value);
			Assert::AreEqual(101, typeDefInteger_0->nominal.value);
			Assert::AreEqual(1, typeDefInteger_0->min.value);
			Assert::AreEqual(999, typeDefInteger_0->max.value);

			Assert::AreEqual(valueDefined, typeDefInteger_0->start.status);
			Assert::AreEqual(valueDefined, typeDefInteger_0->nominal.status);
			Assert::AreEqual(valueDefined, typeDefInteger_0->min.status);
			Assert::AreEqual(valueDefined, typeDefInteger_0->max.status);

			TypeDefinitionInteger * typeDefInteger_1 = &typeDefinitionIntegerArray[1];
			Assert::IsNotNull(typeDefInteger_1);

			Assert::AreEqual(4, typeDefInteger_1->idx);
			Assert::AreEqual("Type_name_integer_1", typeDefInteger_1->name.value);
			Assert::AreEqual("IntegerType_unit_1", typeDefInteger_1->unit.value);
			Assert::AreEqual("IntegerType_quantity_1", typeDefInteger_1->quantity.value);
			Assert::AreEqual("IntegerType_displayUnit_1", typeDefInteger_1->displayUnit.value);

			Assert::AreEqual(6, typeDefInteger_1->start.value);
			Assert::AreEqual(102, typeDefInteger_1->nominal.value);
			Assert::AreEqual(2, typeDefInteger_1->min.value);
			Assert::AreEqual(1000, typeDefInteger_1->max.value);

			Assert::AreEqual(valueDefined, typeDefInteger_1->start.status);
			Assert::AreEqual(valueDefined, typeDefInteger_1->nominal.status);
			Assert::AreEqual(valueDefined, typeDefInteger_1->min.status);
			Assert::AreEqual(valueDefined, typeDefInteger_1->max.status);

			TypeDefinitionInteger * typeDefInteger_2 = &typeDefinitionIntegerArray[2];
			Assert::IsNotNull(typeDefInteger_2);

			Assert::AreEqual(5, typeDefInteger_2->idx);
			Assert::AreEqual("Type_name_integer_2", typeDefInteger_2->name.value);
			Assert::AreEqual(NULL, typeDefInteger_2->quantity.value);
			Assert::AreEqual(NULL, typeDefInteger_2->unit.value);
			Assert::AreEqual(NULL, typeDefInteger_2->displayUnit.value);

			Assert::AreEqual(0, typeDefInteger_2->start.value);
			Assert::AreEqual(0, typeDefInteger_2->nominal.value);
			Assert::AreEqual(0, typeDefInteger_2->min.value);
			Assert::AreEqual(0, typeDefInteger_2->max.value);

			Assert::AreEqual(valueMissing, typeDefInteger_2->start.status);
			Assert::AreEqual(valueMissing, typeDefInteger_2->nominal.status);
			Assert::AreEqual(valueMissing, typeDefInteger_2->min.status);
			Assert::AreEqual(valueMissing, typeDefInteger_2->max.status);

			return;
		}


		TEST_METHOD(T15_TypeDefinitionBoolean)
		{

			T03_XmlParse();

			TypeDefinitionsStruct * typeDefinitionsStruct = getTypeDefinitions();
			Assert::IsNotNull(typeDefinitionsStruct);

			TypeDefinitionBoolean * typeDefinitionBooleanArray = typeDefinitionsStruct->typeDefinitionBooleanArray;
			TypeDefinitionBoolean * typeDefBoolean_0 = &typeDefinitionBooleanArray[0];

			Assert::IsNotNull(typeDefBoolean_0);

			Assert::AreEqual(6, typeDefBoolean_0->idx);
			Assert::AreEqual("Type_name_boolean_0", typeDefBoolean_0->name.value);
			Assert::AreEqual("BooleanType_unit_0", typeDefBoolean_0->unit.value);
			Assert::AreEqual("BooleanType_quantity_0", typeDefBoolean_0->quantity.value);
			Assert::AreEqual("BooleanType_displayUnit_0", typeDefBoolean_0->displayUnit.value);

			Assert::AreEqual(true, typeDefBoolean_0->start.value);
			Assert::AreEqual(false, typeDefBoolean_0->fixed.value);


			Assert::AreEqual(valueDefined, typeDefBoolean_0->name.status);
			Assert::AreEqual(valueDefined, typeDefBoolean_0->unit.status);
			Assert::AreEqual(valueDefined, typeDefBoolean_0->quantity.status);
			Assert::AreEqual(valueDefined, typeDefBoolean_0->displayUnit.status);

			Assert::AreEqual(valueDefined, typeDefBoolean_0->start.status);
			Assert::AreEqual(valueDefined, typeDefBoolean_0->fixed.status);


			TypeDefinitionBoolean * typeDefBoolean_1 = &typeDefinitionBooleanArray[1];
			Assert::IsNotNull(typeDefBoolean_1);

			Assert::AreEqual(7, typeDefBoolean_1->idx);
			Assert::AreEqual("Type_name_boolean_1", typeDefBoolean_1->name.value);

			Assert::AreEqual(valueDefined, typeDefBoolean_1->name.status);
			Assert::AreEqual(valueMissing, typeDefBoolean_1->unit.status);
			Assert::AreEqual(valueMissing, typeDefBoolean_1->quantity.status);
			Assert::AreEqual(valueMissing, typeDefBoolean_1->displayUnit.status);
			Assert::AreEqual(valueMissing, typeDefBoolean_1->start.status);
			Assert::AreEqual(valueMissing, typeDefBoolean_1->fixed.status);

			return;
		}


		TEST_METHOD(T16_TypeDefinitionString)
		{

			T03_XmlParse();

			TypeDefinitionsStruct * typeDefinitionsStruct = getTypeDefinitions();
			Assert::IsNotNull(typeDefinitionsStruct);

			TypeDefinitionString * typeDefinitionStringArray = typeDefinitionsStruct->typeDefinitionStringArray;
			TypeDefinitionString * typeDefString_0 = &typeDefinitionStringArray[0];

			Assert::AreEqual("Type_name_string_0", typeDefString_0->name.value);
			Assert::AreEqual("StringType_unit_0", typeDefString_0->unit.value);
			Assert::AreEqual("StringType_quantity_0", typeDefString_0->quantity.value);
			Assert::AreEqual("StringType_displayUnit_0", typeDefString_0->displayUnit.value);

			Assert::AreEqual(valueDefined, typeDefString_0->name.status);
			Assert::AreEqual(valueDefined, typeDefString_0->unit.status);
			Assert::AreEqual(valueDefined, typeDefString_0->quantity.status);
			Assert::AreEqual(valueDefined, typeDefString_0->displayUnit.status);


			return;
		}

		TEST_METHOD(T17_TypeDefinitionEnumeration)
		{

			T03_XmlParse();

			TypeDefinitionsStruct * typeDefinitionsStruct = getTypeDefinitions();
			Assert::IsNotNull(typeDefinitionsStruct);

			TypeDefinitionEnumeration * typeDefinitionEnumerationArray = typeDefinitionsStruct->typeDefinitionEnumerationArray;
			TypeDefinitionEnumeration * typeDefEnumeration_0 = &typeDefinitionEnumerationArray[0];

			Assert::AreEqual("Type_name_enumeration_0", typeDefEnumeration_0->name.value);
			Assert::AreEqual("EnumerationType_unit_0", typeDefEnumeration_0->unit.value);
			Assert::AreEqual("EnumerationType_quantity_0", typeDefEnumeration_0->quantity.value);
			Assert::AreEqual("EnumerationType_displayUnit_0", typeDefEnumeration_0->displayUnit.value);

			Assert::AreEqual(1, typeDefEnumeration_0->min.value);
			Assert::AreEqual(2, typeDefEnumeration_0->max.value);

			Assert::AreEqual(valueDefined, typeDefEnumeration_0->name.status);
			Assert::AreEqual(valueDefined, typeDefEnumeration_0->unit.status);
			Assert::AreEqual(valueDefined, typeDefEnumeration_0->quantity.status);
			Assert::AreEqual(valueDefined, typeDefEnumeration_0->displayUnit.status);


			//EnumerationItem * itemArray = typeDefEnumeration_0->itemArray;
			//int length = typeDefEnumeration_0->itemArrayLength;

			//Assert::AreEqual(2, length);

			//EnumerationItem  item_0 = itemArray[0];
			//Assert::AreEqual("Item_1_Name", item_0.name);
			//Assert::AreEqual("Item_1_Description", item_0.description);

			//EnumerationItem  item_1 = itemArray[1];
			//Assert::AreEqual("Item_2_Name", item_1.name);
			//Assert::AreEqual("Item_2_Description", item_1.description);


			return;
		}


	};


}
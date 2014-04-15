#include "MainControllerT.h"


using namespace Microsoft::VisualStudio::CppUnitTestFramework;
using namespace Straylight;



namespace StraylightTests
{



	TEST_CLASS(MainControllerT)
	{

	private:
		MainController * mainController_;



		BEGIN_TEST_CLASS_ATTRIBUTE()
			TEST_CLASS_ATTRIBUTE(L"Owner", L"Raj Dye")
			TEST_CLASS_ATTRIBUTE(L"Description", L"MainController Testing")
		END_TEST_CLASS_ATTRIBUTE()




		//runs before each test
		TEST_METHOD_INITIALIZE(beforeEachTest) {
			mainController_ = new MainController();
		}

		//runs after each test
		TEST_METHOD_CLEANUP(afterEachTest)
		{
			mainController_->forceCleanup();
			delete mainController_;
		}

	public:

		TEST_METHOD(C01_Connect)
		{
			Config::getInstance()->setAutoCorrect(true);

			SimStateNative state = mainController_->getState();
			Assert::AreEqual(SimStateNative::simStateNative_0_uninitialized, state);

			int state2 = (int)state;
			Assert::AreEqual(0, state2);

			mainController_->connect(
				Utils::messageCallbackFunc,
				Utils::resultCallbackFunc,
				Utils::stateChangeCallbackFunc
				);


			state = mainController_->getState();

			Assert::AreEqual(SimStateNative::simStateNative_1_connect_completed, state);
			Assert::AreEqual(SimStateNative::simStateNative_1_connect_completed, Utils::simStateNative);

			MainDataModel *  mainDataModel = mainController_->getMainDataModel();
			Assert::IsNotNull(mainDataModel);

		}


		TEST_METHOD(C02_XMLParse)
		{

			C01_Connect();

			int result = mainController_->xmlParse(FMU_FOLDER);

			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, Utils::simStateNative);
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, mainController_->getState());

			MainDataModel * mainDataModel = mainController_->getMainDataModel();
			
			TypeDefinitions * typeDefinitions = mainDataModel->getTypeDefinitions();
			Assert::IsNotNull(typeDefinitions);

		}


		TEST_METHOD(C03_GetConfig)
		{

			C02_XMLParse();

			ConfigStruct * config = mainController_->getConfig();
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

			MainDataModel * mainDataModel = mainController_->getMainDataModel();
			FMImodelAttributesStruct * fmiModelAttributesStruct = mainDataModel->getFMImodelAttributesStruct();

			AttributeStruct * ary = fmiModelAttributesStruct->attributeStructAry;
			int size = fmiModelAttributesStruct->attributeStructSize;

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

			MainDataModel * mainDataModel = mainController_->getMainDataModel();
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
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, Utils::simStateNative);

			mainController_->requestStateChange(SimStateNative::simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, Utils::simStateNative);

			mainController_->requestStateChange(SimStateNative::simStateNative_5_step_requested);
			ScalarValueResults * scalarValueResult = mainController_->getScalarValueResults();


			Assert::AreEqual(SimStateNative::simStateNative_3_ready, Utils::simStateNative);
		}




		TEST_METHOD(C07_GetOneScalarValue)
		{

			C03_GetConfig();
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, Utils::simStateNative);

			mainController_->requestStateChange(SimStateNative::simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, Utils::simStateNative);


			//make request
			ScalarValueRealStruct * scalarValueRealStruct = mainController_->getOneScalarValueStruct(56960);

			Assert::AreEqual(56960, scalarValueRealStruct->idx);
			Assert::AreEqual(291.14999999999998, scalarValueRealStruct->value);

			return;
		}


		TEST_METHOD(C08_GetOneScalarVariable)
		{

			C03_GetConfig();
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, Utils::simStateNative);

			mainController_->requestStateChange(SimStateNative::simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, Utils::simStateNative);


			//make request
			ScalarVariableRealStruct * scalarVariableRealStruct = mainController_->getOneScalarVariableStruct(56960);
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
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, Utils::simStateNative);

			mainController_->requestStateChange(SimStateNative::simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, Utils::simStateNative);



			//formulate request
			ScalarValueRealStruct * scalarValueRealStruct1 = new ScalarValueRealStruct();
			scalarValueRealStruct1->idx = 56960;
			scalarValueRealStruct1->value = 301.1;

			//make request
			fmiStatus status = mainController_->setOneScalarValue(scalarValueRealStruct1);

			Assert::AreEqual(fmiStatus::fmiOK, status);

		}


		TEST_METHOD(C10_SetOneScalarValue)
		{

			C03_GetConfig();
			Assert::AreEqual(SimStateNative::simStateNative_2_xmlParse_completed, Utils::simStateNative);

			mainController_->requestStateChange(SimStateNative::simStateNative_3_init_requested);
			Assert::AreEqual(SimStateNative::simStateNative_3_ready, Utils::simStateNative);


			//change a scalarValue
			ScalarValueRealStruct * scalarValueRealStruct1 = new ScalarValueRealStruct();
			scalarValueRealStruct1->idx = 56960;
			scalarValueRealStruct1->value = 301.1;

			fmiStatus status = mainController_->setOneScalarValue(scalarValueRealStruct1);

			Assert::AreEqual(fmiStatus::fmiOK, status);

		}



	};




}
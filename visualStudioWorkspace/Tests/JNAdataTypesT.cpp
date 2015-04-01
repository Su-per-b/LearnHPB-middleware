#include "stdafx.h"
#include "JNAdataTypesT.h"


using namespace Microsoft::VisualStudio::CppUnitTestFramework;
using namespace Straylight;



namespace StraylightTests
{


	TEST_CLASS(JNAdataTypesT)
	{

	
		TEST_METHOD(T00_DataTypes)
		{

			ScalarVariablesAllStruct * scalarVariablesAllStruct_0 = test_GetScalarVariablesAllStruct();
			Assert::IsNotNull(scalarVariablesAllStruct_0);

			ScalarVariableCollectionStruct * scalarVariableCollectionStruct = test_GetScalarVariableCollectionStruct();
			Assert::IsNotNull(scalarVariableCollectionStruct);

			ScalarValueRealStruct * scalarValueRealStruct = test_GetScalarValueRealStruct();
			Assert::IsNotNull(scalarValueRealStruct);

			ScalarVariableRealStruct * scalarVariableRealStruct = test_GetScalarVariableRealStruct();
			Assert::IsNotNull(scalarVariableRealStruct);

			TypeSpecReal * typeSpecReal = test_GetTypeSpecReal();
			Assert::IsNotNull(typeSpecReal);

		}


		TEST_METHOD(T01_TypeSpecReal)
		{

			TypeSpecReal * typeSpecReal_0 = test_GetTypeSpecReal();
			Assert::IsNotNull(typeSpecReal_0);

			Assert::AreEqual(20.25, typeSpecReal_0->start);
			Assert::AreEqual(21.25, typeSpecReal_0->nominal);
			Assert::AreEqual(22.25, typeSpecReal_0->min);
			Assert::AreEqual(23.25, typeSpecReal_0->max);
			Assert::AreEqual("C1", typeSpecReal_0->unit);

			Assert::AreEqual(1, typeSpecReal_0->startValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->nominalValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->minValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->maxValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->unitValueStatus);

		}

		TEST_METHOD(T02_ScalarVariableRealStruct)
		{

			ScalarVariableRealStruct * scalarVariableReal_0 = test_GetScalarVariableRealStruct();
			Assert::IsNotNull(scalarVariableReal_0);

			Assert::AreEqual(enu_input, scalarVariableReal_0->causality);
			Assert::AreEqual("The Description", scalarVariableReal_0->description);
			Assert::AreEqual(1, scalarVariableReal_0->idx);
			Assert::AreEqual("scalarVar name", scalarVariableReal_0->name);
			Assert::AreEqual(enu_continuous, scalarVariableReal_0->variability);
			Assert::AreEqual(125420, (int)scalarVariableReal_0->valueReference);
			

			TypeSpecReal * typeSpecReal_0 = scalarVariableReal_0->typeSpecReal;

			Assert::AreEqual(20.25, typeSpecReal_0->start);
			Assert::AreEqual(21.25, typeSpecReal_0->nominal);
			Assert::AreEqual(22.25, typeSpecReal_0->min);
			Assert::AreEqual(23.25, typeSpecReal_0->max);
			Assert::AreEqual("C1", typeSpecReal_0->unit);

			Assert::AreEqual(1, typeSpecReal_0->startValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->nominalValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->minValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->maxValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->unitValueStatus);

		}


		TEST_METHOD(T03_ScalarVariableCollectionStruct)
		{

			ScalarVariableCollectionStruct * svCollectionStruct_0 = test_GetScalarVariableCollectionStruct();

			ScalarVariableRealStruct * realVariableAry = svCollectionStruct_0->realValue;
			int realSize = svCollectionStruct_0->realSize;

			Assert::AreEqual(2, realSize);

			ScalarVariableRealStruct  * scalarVariableReal_0 = &realVariableAry[0];
			ScalarVariableRealStruct  * scalarVariableReal_1 = &realVariableAry[1];

			ScalarVariableRealStruct  scalarVariableReal_2 = realVariableAry[0];
			ScalarVariableRealStruct  scalarVariableReal_3 = realVariableAry[1];


			Assert::IsNotNull(scalarVariableReal_0);
			Assert::IsNotNull(scalarVariableReal_1);


			Assert::AreEqual(enu_input, scalarVariableReal_0->causality);
			Assert::AreEqual("The Description", scalarVariableReal_0->description);
			Assert::AreEqual(1, scalarVariableReal_0->idx);
			Assert::AreEqual("scalarVar name", scalarVariableReal_0->name);
			Assert::AreEqual(enu_continuous, scalarVariableReal_0->variability);
			Assert::AreEqual(125420, (int)scalarVariableReal_0->valueReference);

			TypeSpecReal * typeSpecReal_0 = scalarVariableReal_0->typeSpecReal;

			Assert::AreEqual(20.25, typeSpecReal_0->start);
			Assert::AreEqual(21.25, typeSpecReal_0->nominal);
			Assert::AreEqual(22.25, typeSpecReal_0->min);
			Assert::AreEqual(23.25, typeSpecReal_0->max);
			Assert::AreEqual("C1", typeSpecReal_0->unit);

			Assert::AreEqual(1, typeSpecReal_0->startValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->nominalValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->minValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->maxValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->unitValueStatus);


			Assert::AreEqual(enu_input, scalarVariableReal_1->causality);
			Assert::AreEqual("The Description", scalarVariableReal_1->description);
			Assert::AreEqual(2, scalarVariableReal_1->idx);
			Assert::AreEqual("scalarVar name", scalarVariableReal_1->name);
			Assert::AreEqual(enu_continuous, scalarVariableReal_1->variability);
			Assert::AreEqual(125420, (int)scalarVariableReal_1->valueReference);

			TypeSpecReal * typeSpecReal_1 = scalarVariableReal_1->typeSpecReal;

			Assert::AreEqual(20.25, typeSpecReal_1->start);
			Assert::AreEqual(21.25, typeSpecReal_1->nominal);
			Assert::AreEqual(22.25, typeSpecReal_1->min);
			Assert::AreEqual(23.25, typeSpecReal_1->max);
			Assert::AreEqual("C1", typeSpecReal_1->unit);

			Assert::AreEqual(1, typeSpecReal_1->startValueStatus);
			Assert::AreEqual(1, typeSpecReal_1->nominalValueStatus);
			Assert::AreEqual(1, typeSpecReal_1->minValueStatus);
			Assert::AreEqual(1, typeSpecReal_1->maxValueStatus);
			Assert::AreEqual(1, typeSpecReal_1->unitValueStatus);


			return;

		}
		
		TEST_METHOD(T04_ScalarVariablesAllStruct)
		{


			ScalarVariablesAllStruct * svAll_0 = test_GetScalarVariablesAllStruct();
			Assert::IsNotNull(svAll_0);

			ScalarVariableCollectionStruct * svCollectionStruct_0 = svAll_0->input;
			Assert::IsNotNull(svCollectionStruct_0);

			ScalarVariableCollectionStruct * svCollectionStruct_1 = svAll_0->output;
			Assert::IsNotNull(svCollectionStruct_1);

			ScalarVariableRealStruct * realVariableAry_0 = svCollectionStruct_0->realValue;
			int realSize_0 = svCollectionStruct_0->realSize;

			Assert::AreEqual(2, realSize_0);

			ScalarVariableRealStruct  * scalarVariableReal_0 = &realVariableAry_0[0];
			ScalarVariableRealStruct  * scalarVariableReal_1 = &realVariableAry_0[1];

			Assert::IsNotNull(scalarVariableReal_0);
			Assert::IsNotNull(scalarVariableReal_1);


			Assert::AreEqual(enu_input, scalarVariableReal_0->causality);
			Assert::AreEqual("The Description", scalarVariableReal_0->description);
			Assert::AreEqual(1, scalarVariableReal_0->idx);
			Assert::AreEqual("scalarVar name", scalarVariableReal_0->name);
			Assert::AreEqual(enu_continuous, scalarVariableReal_0->variability);
			Assert::AreEqual(125420, (int)scalarVariableReal_0->valueReference);

			TypeSpecReal * typeSpecReal_0 = scalarVariableReal_0->typeSpecReal;

			Assert::AreEqual(20.25, typeSpecReal_0->start);
			Assert::AreEqual(21.25, typeSpecReal_0->nominal);
			Assert::AreEqual(22.25, typeSpecReal_0->min);
			Assert::AreEqual(23.25, typeSpecReal_0->max);
			Assert::AreEqual("C1", typeSpecReal_0->unit);

			Assert::AreEqual(1, typeSpecReal_0->startValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->nominalValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->minValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->maxValueStatus);
			Assert::AreEqual(1, typeSpecReal_0->unitValueStatus);

			Assert::AreEqual(enu_input, scalarVariableReal_1->causality);
			Assert::AreEqual("The Description", scalarVariableReal_1->description);
			Assert::AreEqual(2, scalarVariableReal_1->idx);
			Assert::AreEqual("scalarVar name", scalarVariableReal_1->name);
			Assert::AreEqual(enu_continuous, scalarVariableReal_1->variability);
			Assert::AreEqual(125420, (int)scalarVariableReal_1->valueReference);

			TypeSpecReal * typeSpecReal_1 = scalarVariableReal_1->typeSpecReal;

			Assert::AreEqual(20.25, typeSpecReal_1->start);
			Assert::AreEqual(21.25, typeSpecReal_1->nominal);
			Assert::AreEqual(22.25, typeSpecReal_1->min);
			Assert::AreEqual(23.25, typeSpecReal_1->max);
			Assert::AreEqual("C1", typeSpecReal_1->unit);

			Assert::AreEqual(1, typeSpecReal_1->startValueStatus);
			Assert::AreEqual(1, typeSpecReal_1->nominalValueStatus);
			Assert::AreEqual(1, typeSpecReal_1->minValueStatus);
			Assert::AreEqual(1, typeSpecReal_1->maxValueStatus);
			Assert::AreEqual(1, typeSpecReal_1->unitValueStatus);


			return;
		}



		TEST_METHOD(T05_DisplayUnitDefinitionStruct)
		{

			DisplayUnitDefinitionStruct * duStruct = test_GetDisplayUnitDefinitionStruct();

			Assert::AreEqual("K", duStruct->displayUnit);
			Assert::AreEqual(1, duStruct->displayUnitValueStatus);

			Assert::AreEqual(-273.15, duStruct->offset);
			Assert::AreEqual(1, duStruct->offsetValueStatus);

			Assert::AreEqual(0.001, duStruct->gain);
			Assert::AreEqual(1, duStruct->gainValueStatus);


			return;
		}


		TEST_METHOD(T06_TypeDefinitionReal)
		{

			TypeDefinitionReal * typeDefinitionReal = test_GetTypeDefinitionReal();

			Assert::AreEqual(1, typeDefinitionReal->idx);

			Assert::AreEqual("Modelica.Media.Interfaces.PartialMedium.AbsolutePressure", typeDefinitionReal->name.value);
			Assert::AreEqual("Pa", typeDefinitionReal->unit.value);

			Assert::AreEqual("Pressure", typeDefinitionReal->quantity.value);
			Assert::AreEqual("bar", typeDefinitionReal->displayUnit.value);

			Assert::AreEqual(1.0, typeDefinitionReal->start.value);
			Assert::AreEqual(100.0, typeDefinitionReal->nominal.value);
			Assert::AreEqual(0.0, typeDefinitionReal->min.value);
			Assert::AreEqual(100000000.0, typeDefinitionReal->max.value);
		
			Assert::AreEqual(valueDefined, typeDefinitionReal->start.status);
			Assert::AreEqual(valueDefined, typeDefinitionReal->nominal.status);
			Assert::AreEqual(valueDefined, typeDefinitionReal->min.status);
			Assert::AreEqual(valueDefined, typeDefinitionReal->max.status);

			return;
		}

		TEST_METHOD(T07_TypeDefinitionString)
		{

			TypeDefinitionString * typeDefinitionString = test_GetTypeDefinitionString();

			Assert::AreEqual(3, typeDefinitionString->idx);
			Assert::AreEqual("AbsolutePressure2", typeDefinitionString->name.value);
			Assert::AreEqual("Pa2", typeDefinitionString->unit.value);

			Assert::AreEqual("Pressure2", typeDefinitionString->quantity.value);
			Assert::AreEqual("bar2", typeDefinitionString->displayUnit.value);

			return;
		}


		TEST_METHOD(T08_TypeDefinitionInteger)
		{

			TypeDefinitionInteger * typeDefinitionInteger = test_GetTypeDefinitionInteger();

			Assert::AreEqual(2, typeDefinitionInteger->idx);

			Assert::AreEqual("AbsolutePressure3", typeDefinitionInteger->name.value);
			Assert::AreEqual("Pa3", typeDefinitionInteger->unit.value);

			Assert::AreEqual("Pressure3", typeDefinitionInteger->quantity.value);
			Assert::AreEqual("bar3", typeDefinitionInteger->displayUnit.value);

			Assert::AreEqual(1, typeDefinitionInteger->start.value);
			Assert::AreEqual(100, typeDefinitionInteger->nominal.value);
			Assert::AreEqual(0, typeDefinitionInteger->min.value);
			Assert::AreEqual(100000000, typeDefinitionInteger->max.value);

			Assert::AreEqual(valueDefined, typeDefinitionInteger->start.status);
			Assert::AreEqual(valueDefined, typeDefinitionInteger->nominal.status);
			Assert::AreEqual(valueDefined, typeDefinitionInteger->min.status);
			Assert::AreEqual(valueDefined, typeDefinitionInteger->max.status);

			return;
		}

		TEST_METHOD(T09_TypeDefinitionBoolean)
		{

			TypeDefinitionBoolean * typeDefinitionBoolean = test_GetTypeDefinitionBoolean();

			Assert::AreEqual(4, typeDefinitionBoolean->idx);

			Assert::AreEqual("AbsolutePressure4", typeDefinitionBoolean->name.value);
			Assert::AreEqual("Pa4", typeDefinitionBoolean->unit.value);

			Assert::AreEqual("Pressure4", typeDefinitionBoolean->quantity.value);
			Assert::AreEqual("bar4", typeDefinitionBoolean->displayUnit.value);

			Assert::AreEqual(true, typeDefinitionBoolean->start.value);
			Assert::AreEqual(false, typeDefinitionBoolean->fixed.value);

			Assert::AreEqual(valueDefined, typeDefinitionBoolean->start.status);
			Assert::AreEqual(valueDefined, typeDefinitionBoolean->fixed.status);

			return;
		}



		TEST_METHOD(T10_TypeDefinitionEnumeration)
		{

			TypeDefinitionEnumeration * typeDefinitionEnumeration = test_GetTypeDefinitionEnumeration();

			Assert::AreEqual(5, typeDefinitionEnumeration->idx);

			Assert::AreEqual("name5", typeDefinitionEnumeration->name.value);
			Assert::AreEqual("unit5", typeDefinitionEnumeration->unit.value);

			Assert::AreEqual("quantity5", typeDefinitionEnumeration->quantity.value);
			Assert::AreEqual("displayUnit5", typeDefinitionEnumeration->displayUnit.value);

			Assert::AreEqual(1, typeDefinitionEnumeration->start.value);
			Assert::AreEqual(1, typeDefinitionEnumeration->min.value);
			Assert::AreEqual(6, typeDefinitionEnumeration->max.value);

			Assert::AreEqual(valueDefined, typeDefinitionEnumeration->start.status);
			Assert::AreEqual(valueDefined, typeDefinitionEnumeration->min.status);
			Assert::AreEqual(valueDefined, typeDefinitionEnumeration->max.status);

			//EnumerationItem * itemArray = typeDefinitionEnumeration->itemArray;
			//int len = typeDefinitionEnumeration->itemArrayLength;
			//Assert::AreEqual(2, len);

			//EnumerationItem item_0 = itemArray[0];
			//Assert::AreEqual("name 1", item_0.name);
			//Assert::AreEqual("description 1", item_0.description);

			//EnumerationItem item_1 = itemArray[1];
			//Assert::AreEqual("name 2", item_1.name);
			//Assert::AreEqual("description 2", item_1.description);

			return;
		}


		TEST_METHOD(T11_ValueStatus)
		{

			ValueStatus valueStatus = test_GetValueStatus();
			Assert::AreEqual(valueDefined, valueStatus);

			return;
		}



		TEST_METHOD(T12_RealValue)
		{

			RealValue realValue = test_GetRealValue();
			Assert::AreEqual(13.01, realValue.value);
			Assert::AreEqual(valueDefined, realValue.status);

			return;
		}

		TEST_METHOD(T13_IntegerValue)
		{

			IntegerValue integerValue = test_GetIntegerValue();
			Assert::AreEqual(13, integerValue.value);
			Assert::AreEqual(valueDefined, integerValue.status);

			return;
		}

		TEST_METHOD(T14_BooleanValue)
		{

			BooleanValue booleanValue = test_GetBooleanValue();
			Assert::AreEqual(true, booleanValue.value);
			Assert::AreEqual(valueDefined, booleanValue.status);

			return;
		}

		TEST_METHOD(T15_StringValue)
		{

			StringValue stringValue = test_GetStringValue();

			Assert::AreEqual("stringValue_0", stringValue.value);
			Assert::AreEqual(valueDefined, stringValue.status);

			return;
		}

		TEST_METHOD(T16_EnumerationItem)
		{

			EnumerationItem * enumerationItem = test_GetEnumerationItem();

			Assert::AreEqual("name_0", enumerationItem->name);
			Assert::AreEqual("description_0", enumerationItem->description);

			return;
		}




	};

}

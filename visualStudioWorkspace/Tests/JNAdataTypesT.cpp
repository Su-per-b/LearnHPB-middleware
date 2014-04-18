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

			ScalarVariablesAllStruct * scalarVariablesAllStruct_1 = test_GetScalarVariablesAllStruct2();
			Assert::IsNotNull(scalarVariablesAllStruct_1);

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
		


	};

}

#include "JNAdataTypes.h"




		ScalarVariablesAllStruct * test_GetScalarVariablesAllStruct() {

			ScalarVariablesAllStruct * theStruct = new ScalarVariablesAllStruct();

			theStruct->input = test_GetScalarVariableCollectionStruct();
			theStruct->output = test_GetScalarVariableCollectionStruct();
			theStruct->internal = test_GetScalarVariableCollectionStruct();

			return theStruct;
		}





		ScalarVariableCollectionStruct * test_GetScalarVariableCollectionStruct() {


			ScalarVariableRealStruct * scalarVariableReal_0 = test_GetScalarVariableRealStruct();
			ScalarVariableRealStruct * scalarVariableReal_1 = test_GetScalarVariableRealStruct();
			scalarVariableReal_1->idx = 2;


			ScalarVariableRealStruct * ary = new ScalarVariableRealStruct[2];
			ary[0] = *scalarVariableReal_0;
			ary[1] = *scalarVariableReal_1;


			ScalarVariableRealStruct scalarVariableReal_3 = ary[0];
			ScalarVariableRealStruct scalarVariableReal_4 = ary[1];


			ScalarVariableCollectionStruct * collectionStruct = new ScalarVariableCollectionStruct();

			collectionStruct->realValue = ary;
			collectionStruct->realSize = 2;


			return collectionStruct;
		}



		ScalarValueRealStruct * test_GetScalarValueRealStruct() {

			ScalarValueRealStruct * theStruct = new ScalarValueRealStruct();
			theStruct->idx = 0;
			theStruct->value = 2.2;

			return theStruct;

		}


		ScalarVariableRealStruct * test_GetScalarVariableRealStruct() {

			TypeSpecReal * typeSpecReal = test_GetTypeSpecReal();

			ScalarVariableRealStruct * scalarVariableReal_0 = new ScalarVariableRealStruct();
			scalarVariableReal_0->name = "scalarVar name";
			scalarVariableReal_0->idx = 1;
			scalarVariableReal_0->causality = enu_input;
			scalarVariableReal_0->variability = enu_continuous;
			scalarVariableReal_0->description = "The Description";
			scalarVariableReal_0->valueReference = 125420;

			scalarVariableReal_0->typeSpecReal = typeSpecReal;

			return scalarVariableReal_0;

		}



		TypeSpecReal * test_GetTypeSpecReal() {

			TypeSpecReal * typeSpecReal = new TypeSpecReal();
			typeSpecReal->start = 20.25;
			typeSpecReal->nominal = 21.25;
			typeSpecReal->min = 22.25;
			typeSpecReal->max = 23.25;
			typeSpecReal->unit = "C1";

			typeSpecReal->startValueStatus = 1;
			typeSpecReal->nominalValueStatus = 1;
			typeSpecReal->minValueStatus = 1;
			typeSpecReal->maxValueStatus = 1;
			typeSpecReal->unitValueStatus = 1;

			return typeSpecReal;
		}

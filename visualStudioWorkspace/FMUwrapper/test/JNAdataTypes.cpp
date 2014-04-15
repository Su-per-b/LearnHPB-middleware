#include "JNAdataTypes.h"


ScalarVariablesAllStruct * test_GetScalarVariablesAllStruct() {
	ScalarVariablesAllStruct * theStruct = new ScalarVariablesAllStruct();
	return theStruct;
}


ScalarVariablesAllStruct * test_GetScalarVariablesAllStruct2() {
	ScalarVariablesAllStruct * theStruct = new ScalarVariablesAllStruct();
	return theStruct;
}




ScalarVariableCollectionStruct * test_GetScalarVariableCollectionStruct() {

	ScalarVariableCollectionStruct * theStruct = new ScalarVariableCollectionStruct();

	return theStruct;

}



ScalarValueRealStruct * test_GetScalarValueRealStruct() {

	ScalarValueRealStruct * theStruct = new ScalarValueRealStruct();
	theStruct->idx = 0;
	theStruct->value = 2.2;

	return theStruct;

} 


ScalarVariableRealStruct * test_GetScalarVariableRealStruct() {

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





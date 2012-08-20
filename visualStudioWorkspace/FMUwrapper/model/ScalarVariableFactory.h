#pragma once
#include "structs.h"
#include "Logger.h"


namespace Straylight
{

	class ScalarVariableFactory
	{

	public:
		ScalarVariableFactory();
		~ScalarVariableFactory(void);


		static ScalarVariableRealStruct* makeReal(ScalarVariable* scalarVariable, int i);
		static ScalarVariableBooleanStruct* makeBoolean(ScalarVariable* scalarVariable, int i);
		static ScalarVariableIntegerStruct* makeInteger(ScalarVariable* scalarVariable, int i);
		static ScalarVariableEnumerationStruct* makeEnumeration(ScalarVariable* scalarVariable, int i);
		static ScalarVariableStringStruct* makeString(ScalarVariable* scalarVariable, int i);


	};

}
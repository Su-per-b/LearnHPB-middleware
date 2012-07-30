#pragma once
#include "structs.h"

namespace Straylight
{

	class ScalarVariableFactory
	{
	public:
		ScalarVariableFactory(void);
		~ScalarVariableFactory(void);


		static ScalarVariableRealStruct* makeReal(ScalarVariable* scalarVariable);
		static ScalarVariableBooleanStruct* makeBoolean(ScalarVariable* scalarVariable);
		static ScalarVariableIntegerStruct* makeInteger(ScalarVariable* scalarVariable);
		static ScalarVariableEnumerationStruct* makeEnumeration(ScalarVariable* scalarVariable);
		static ScalarVariableStringStruct* makeString(ScalarVariable* scalarVariable);


	};

}
#pragma once
#include "stdafx.h"
#include <Logger.h>
#include "structs.h"

namespace Straylight
{
	class ScalarVariableCollection
	{
	public:
		ScalarVariableCollection(void);
		~ScalarVariableCollection(void);

		vector<ScalarVariableRealStruct*> real;
		vector<ScalarVariableBooleanStruct*> boolean;
		vector<ScalarVariableIntegerStruct*> integer;
		vector<ScalarVariableEnumerationStruct*> enumeration;
		vector<ScalarVariableStringStruct*> string;


		ScalarVariableRealStruct * getRealAsArray();
		ScalarVariableBooleanStruct * getBooleanAsArray();
		ScalarVariableIntegerStruct * getIntegerAsArray();
		ScalarVariableEnumerationStruct * getEnumerationAsArray();
		ScalarVariableStringStruct * getStringAsArray();

		ScalarVariableCollectionStruct * convertToStruct();

	};





}
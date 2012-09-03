#pragma once
#include "stdafx.h"
#include <Logger.h>
#include "structs.h"
#include "ScalarVariableFactory.h"
#include "ScalarVariableCollection.h"

namespace Straylight
{
	/*******************************************************//**
	 * Scalar variable data model.
	 *******************************************************/
	class ScalarVariableDataModel
	{
	public:
		ScalarVariableDataModel(void);
		~ScalarVariableDataModel(void);

		void extract(ScalarVariable** scalarVariableArray);

		ScalarVariableCollection * svAll_;
		ScalarVariableCollection * svInput_;
		ScalarVariableCollection * svOutput_;
		ScalarVariableCollection * svInternal_;

	private:

		//vector<ScalarVariableRealStruct*> svVectorReal_;
		//vector<ScalarVariableBooleanStruct*> svVectorBoolean_;
		//vector<ScalarVariableIntegerStruct*> svVectorInteger_;
		////vector<ScalarVariableEnumerationStruct*> svVectorEnumeration_;
	//	vector<ScalarVariableStringStruct*> svVectorString_;
	};

/*******************************************************//**
 * Gets or sets the.
 *
 * @value	.
 *******************************************************/
}
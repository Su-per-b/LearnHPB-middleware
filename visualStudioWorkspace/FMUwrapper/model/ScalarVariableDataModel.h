#pragma once
#include "stdafx.h"
#include <Logger.h>
#include "structs.h"
#include "ScalarVariableFactory.h"
#include "ScalarVariableCollection.h"
#include <hash_map>
#include <map>


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
		 
		ScalarVariableRealStruct * getOneScalarVariableStruct(int idx);

	private:
		vector<int> idxMap_;

		hash_map<int, ScalarVariableRealStruct* > idx2Real_;
		map<int, ScalarVariableRealStruct* > idx2Real2_;

	};

/*******************************************************//**
 * Gets or sets the.
 *
 * @value	.
 *******************************************************/
}
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



	};

/*******************************************************//**
 * Gets or sets the.
 *
 * @value	.
 *******************************************************/
}
/*******************************************************//**
 * @file	model\ScalarValueDataModel.h
 *
 * Declares the scalar value data model class.
 *******************************************************/

#pragma once

#include "stdafx.h"
#include <Logger.h>
#include "structs.h"
#include "ScalarValue.h"
#include "ScalarValueCollection.h"
#include "ScalarVariableDataModel.h"

namespace Straylight
{
	/*******************************************************//**
	 * Scalar value data model.
	 * After each simulations step the API is queried to find the
	 * input, output and internal Scalar Values.  This Class is then
	 * instantiated.
	 * TODO: refactor this class to use HASH, MAP or Polymorphism
	 *******************************************************/
	class ScalarValueDataModel
	{
	public:

		/*******************************************************//**
		 * Constructor.
		 *
		 * @param	time	The time.
		 *******************************************************/
		ScalarValueDataModel(double time);

		/*******************************************************//**
		 * Destructor.
		 *******************************************************/
		~ScalarValueDataModel(void);



		/*******************************************************//**
		 * Extracts this object.
		 *
		 * @param	scalarVariableCollection	If non-null, collection of scalar variables.
		 * @param	causality					The causality.
		 *******************************************************/
		void extract(ScalarVariableCollection * scalarVariableCollection, Enu causality);




	private:

		/*******************************************************//**
		 * Collection of scalar values.
		 *******************************************************/
		ScalarValueCollection * scalarValueCollection_;


		/*******************************************************//**
		 * Extracts the real.
		 *
		 * @param [in,out]	scalarVariableList	If non-null, list of scalar variables.
		 * @param	causality				  	The causality.
		 *******************************************************/
		void extractReal(vector<ScalarVariableRealStruct*> scalarVariableList, Enu causality);

		/*******************************************************//**
		 * Extracts the boolean.
		 *
		 * @param	scalarVariableList	If non-null, list of scalar variables.
		 * @param	causality		  	The causality.
		 *******************************************************/
		void extractBoolean(vector<ScalarVariableBooleanStruct*> scalarVariableList, Enu causality);

		/*******************************************************//**
		 * The time.
		 *******************************************************/

		double time_;

	};
}
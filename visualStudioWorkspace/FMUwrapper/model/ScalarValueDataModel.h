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
	 *******************************************************/
	class ScalarValueDataModel
	{
	public:
		double time_;

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
		 * Collection of scalar values.
		 *******************************************************/
		ScalarValueCollection * scalarValueCollection_;

		/*******************************************************//**
		 * Extracts this object.
		 *
		 * @param	scalarVariableCollection	If non-null, collection of scalar variables.
		 * @param	causality					The causality.
		 *******************************************************/
		void extract(ScalarVariableCollection * scalarVariableCollection, Enu causality);

		ScalarValueCollection * sValueCollectionAll_;

		/*******************************************************//**
		 * The value collection input.
		 *******************************************************/
		ScalarValueCollection * sValueCollectionInput_;

		/*******************************************************//**
		 * The value collection output.
		 *******************************************************/
		ScalarValueCollection * sValueCollectionOutput_;
		ScalarValueCollection * sValueCollectionInternal_;

	private:

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

		//void extractInteger(vector<ScalarVariableIntegerStruct*> scalarVariableList, Enu causality);
		//void extractEnumeration(vector<ScalarVariableEnumerationStruct*> scalarVariableList, Enu causality);
		//void extractString(vector<ScalarVariableStringStruct*> scalarVariableList, Enu causality);
	};
}
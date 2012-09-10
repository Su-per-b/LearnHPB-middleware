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

typedef struct {

	ScalarValueCollectionStruct * input;
	ScalarValueCollectionStruct * output;
	double time;

} ScalarValueResultsStruct;



	/*******************************************************//**
	 * Scalar value data model.
	 * After each simulations step the API is queried to find the
	 * input, output and internal Scalar Values.  This Class is then
	 * instantiated.
	 * TODO: refactor this class to use HASH, MAP or Polymorphism
	 *******************************************************/
	class DllApi ScalarValueResults
	{
	public:

		/*******************************************************//**
		 * Constructor.
		 *
		 * @param	time	The time.
		 *******************************************************/
		ScalarValueResults(double time, ScalarVariableDataModel * scalarVariableDataModel);

		/*******************************************************//**
		 * Destructor.
		 *******************************************************/
		~ScalarValueResults(void);

		void extractAll(ScalarVariableDataModel* pScalarVariableDataModel_);
		ScalarValueCollection * getScalarValueCollectionInput() const { return scalarValueCollectionInput_; }
		ScalarValueCollection * getScalarValueCollectionOutput() const { return scalarValueCollectionOutput_; }
		ScalarValueResultsStruct *  toStruct();

		void toString(char* buffer, int len);

	private:

		/*******************************************************//**
		 * <summary>Creates the scalarValueCollection objects.</summary>
		 *******************************************************/
		void extract();

		/*******************************************************//**
		 * Extracts the real.
		 *
		 * @param [in,out]	scalarVariableList	If non-null, list of scalar variables.
		 * @param	causality				  	The causality.
		 *******************************************************/
		vector<ScalarValueRealStruct*> extractReal(vector<ScalarVariableRealStruct*> scalarVariableRealList);


		/*******************************************************//**
		 * Extracts the boolean.
		 *
		 * @param	scalarVariableList	If non-null, list of scalar variables.
		 * @param	causality		  	The causality.
		 *******************************************************/
		vector<ScalarValueBooleanStruct*> extractBoolean(vector<ScalarVariableBooleanStruct*> scalarVariableBooleanList);


		/*******************************************************//**
		 * The time step value
		 *******************************************************/
		double time_;
		ScalarValueCollection * scalarValueCollectionInput_;
		ScalarValueCollection * scalarValueCollectionOutput_;
		ScalarVariableDataModel * scalarVariableDataModel_;

	};



}
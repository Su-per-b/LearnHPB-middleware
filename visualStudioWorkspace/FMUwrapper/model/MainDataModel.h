/*******************************************************//**
 * @file	model\MainDataModel.h
 *
 * Declares the main data model class.
 *******************************************************/
#pragma once



#include "stdafx.h"
#include "structs.h"
#include "ResultOfStep.h"
#include "Logger.h"
#include "TypeDefFactory.h"
#include "ScalarVariableFactory.h"
#include "TypeDefDataModel.h"
#include "ScalarVariableCollection.h"
#include "ScalarVariableDataModel.h"
#include "ScalarValueDataModel.h"

using namespace std;

namespace Straylight
{
	/*******************************************************//**
	 * Main data model.
	 *******************************************************/
	class MainDataModel
	{

	public:
		MainDataModel();
		~MainDataModel(void);

		void extract();

		/*******************************************************//**
		 * Gets result of step.
		 *
		 * @param	time	The time.
		 *
		 * @return	null if it fails, else the result of step.
		 *******************************************************/
		ResultOfStep* getResultOfStep(double time);

		/*******************************************************//**
		 * Sets scalar value real.
		 *
		 * @param	idx  	The index.
		 * @param	value	The value.
		 *
		 * @return	.
		 *******************************************************/
		fmiStatus setScalarValueReal(int idx, double value);

		void setFMU(FMU* fmu);
		void setFmiComponent(fmiComponent fmiComponent_arg);
		void setStartValues();

		/*******************************************************//**
		 * The type def data model.
		 *******************************************************/
		TypeDefDataModel * typeDefDataModel_;
		ScalarVariableDataModel * scalarVariableDataModel_;
		void setScalarValues (ScalarValueRealStruct * scalarValueAry, int length);

	private:

		/*******************************************************//**
		 * Information describing the model.
		 *******************************************************/
		ModelDescription* modelDescription_;
		int maxInternalScalarVariables;

		/*******************************************************//**
		 * The fmu.
		 *******************************************************/
		FMU* fmu_;
		fmiComponent fmiComponent_;

	};




};




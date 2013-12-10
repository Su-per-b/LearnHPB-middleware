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
#include "ScalarValueResults.h"

using namespace std;

namespace Straylight
{
	/*******************************************************//**
	 * <summary> Main data model.</summary>
	 *******************************************************/
	class MainDataModel
	{


	private:

		/*******************************************************//**
		* The struct which contains most of the meta-data about the model
		* this is part of the Qtronic library
		*******************************************************/
		ModelDescription* modelDescription_;

		/*******************************************************//**
		* <summary>This value ISerializableused for debugging </summary>
		*******************************************************/
		int maxInternalScalarVariables;

		/*******************************************************//**
		* The fmu.
		*******************************************************/
		FMU* fmu_;

		fmiComponent fmiComponent_;

		fmiStatus setScalarValueRealMin(int idx, double value);

	public:
		MainDataModel();
		~MainDataModel(void);

		void extract();


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

		TypeDefDataModel *  getTypeDefDataModel();


		/*******************************************************//**
		* The type def data model.
		*******************************************************/
		TypeDefDataModel * typeDefDataModel_;
		ScalarVariableDataModel * scalarVariableDataModel_;
		void setScalarValues (ScalarValueRealStruct * scalarValueAry, int length);

		/*******************************************************//**
		* Gets ScalarValueDataModel
		*
		* @param	time	The time value for this step of the simulation.
		*
		* @return	null if it fails, else the ScalarValueDataModel
		*******************************************************/
		ScalarValueResults * getScalarValueResults(double time);


	};
};

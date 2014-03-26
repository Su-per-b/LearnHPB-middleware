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
#include "TypeDefinitions.h"
#include "ScalarVariableCollection.h"
#include "ScalarVariableDataModel.h"
#include "ScalarValueResults.h"
#include "FmiModelAttributes.h"
#include "UnitDefinitions.h"

using namespace std;

namespace Straylight
{
	/*******************************************************//**
	 * <summary> Main data model.</summary>
	 *******************************************************/
	class DllApi MainDataModel
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

		TypeDefinitions * typeDefinitions_;
		FmiModelAttributes * fmiModelAttributes_;

		UnitDefinitions * unitDefinitions_;

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

		TypeDefinitions *  getTypeDefinitions();

		UnitDefinitions *  getUnitDefinitions();

		ScalarVariableDataModel * scalarVariableDataModel_;


		/*******************************************************//**
		* Gets ScalarValueDataModel
		*
		* @param	time	The time value for this step of the simulation.
		*
		* @return	null if it fails, else the ScalarValueDataModel
		*******************************************************/
		ScalarValueResults * getScalarValueResults(double time);

		AttributeStruct * getFmiModelAttributesStruct();

		BaseUnitStruct *  getBaseUnitStructAry();

		void setScalarValues(ScalarValueRealStruct * scalarValueAry, int length);

		fmiStatus setOneScalarValue(ScalarValueRealStruct * scalarValue);

		ScalarValue * getOneScalarValue(int idx);

		ScalarVariableRealStruct * getOneScalarVariableStruct(int idx);

		



	};
};

/*******************************************************//**
 * @file	model\ResultOfStep.h
 *
 * Declares the result of step class.
 *******************************************************/
#pragma once

#include "stdafx.h"
#include "ScalarValue.h"
#include "structs.h"
#include "enums.h"
#include "ScalarVariableCollection.h"

extern "C"
{
#include "xml_parser.h"
#include "fmi_cs.h"
}

/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct  {
	/*******************************************************//**
	 * The time.
	 *******************************************************/
	double time;

	double  * input;

	/*******************************************************//**
	 * Length of the input.
	 *******************************************************/
	int inputLength;

	double  * output;

	/*******************************************************//**
	 * Length of the output.
	 *******************************************************/
	int outputLength;
} ResultOfStepStruct;

/*******************************************************//**
 * .
 *******************************************************/
using namespace std;

namespace Straylight
{
	class  ResultOfStep
	{
	private:

	public:

		/*******************************************************//**
		 * The sv list output.
		 *******************************************************/
		vector<ScalarValue *> svListOutput;
		vector<ScalarValue *> svListInput;

		/*******************************************************//**
		 * The time.
		 *******************************************************/
		double time_;

	public:

		ResultOfStep(double time);
		~ResultOfStep(void);

		char * getString();

		ResultOfStepStruct* toStruct();

		void extractValues(vector<ScalarVariableRealStruct*> scalarVariableList, Enu causality);
	};
}

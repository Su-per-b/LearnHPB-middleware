/*******************************************************//**
 * @file	model\ResultOfStep.cpp
 *
 * Implements the result of step class.
 *******************************************************/
#include "ResultOfStep.h"


namespace Straylight
{
	/*******************************************************//**
	 * Constructor.
	 *
	 * @param	time	The time.
	 *******************************************************/
	ResultOfStep::ResultOfStep(double time)
	{

		time_ = time;
	}

	/*******************************************************//**
	 * Destructor.
	 *******************************************************/
	ResultOfStep::~ResultOfStep(void)
	{
	}

	/*******************************************************//**
	 * Gets the string.
	 *
	 * @return	null if it fails, else the string.
	 *******************************************************/
	char * ResultOfStep::getString()
	{
		char * cstr;
		vector<ScalarValue*>::iterator it = svListOutput.begin();

		string str;
		string output = "";

		ScalarValue * sv;
		sv = (*it);
		str = sv->getString();
		output.append(str);

		++it;

		for(it; it != svListOutput.end(); ++it)
		{

			output.append(",");
			sv = (*it);
			str = sv->getString();
			output.append(str);

		}


		cstr = new char [output.size()+1];
		strcpy (cstr, output.c_str());


		return cstr;
	}

	/*******************************************************//**
	 * Extracts the values.
	 *
	 * @param [in,out]	scalarVariableList	If non-null, list of scalar variables.
	 * @param	causality				  	The causality.
	 *******************************************************/
	void ResultOfStep::extractValues(vector<ScalarVariableRealStruct*> scalarVariableList, Enu causality) {

		vector<ScalarVariableRealStruct*>::iterator list_iter = scalarVariableList.begin();

		for(list_iter; 
			list_iter != scalarVariableList.end(); list_iter++)
		{
			ScalarVariableRealStruct * svm =  *list_iter;
			ScalarValue * scalarValue = new ScalarValue(svm->idx);

			if (causality == enu_input) {
				svListInput.push_back(scalarValue);
			} else if (causality == enu_output){
				svListOutput.push_back(scalarValue);
			}

		}

	}

	/*******************************************************//**
	 * Converts this object to a structure.
	 *
	 * @return	This object as a ResultOfStepStruct*.
	 *******************************************************/
	ResultOfStepStruct * ResultOfStep::toStruct ()
	{
		ResultOfStepStruct * result = new ResultOfStepStruct();
		result->time = time_;

		int len3 = svListInput.size();
		result->input = new double[len3];
		result->inputLength = len3;

		for (int k = 0; k < len3; k++)
		{
			Straylight::ScalarValue * sv  = svListInput[k];
			double realNumber = sv->getRealNumber();
			result->input [k] = realNumber;
		}

		int len = svListOutput.size();
		result->output = new double[len];
		result->outputLength = len;

		for (int i = 0; i < len; i++)
		{
			Straylight::ScalarValue * sv  = svListOutput[i];
			double realNumber = sv->getRealNumber();
			result->output [i] = realNumber;
		}

		return result;

	}

}
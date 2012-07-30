#pragma once

#include "stdafx.h"
#include "ScalarValue.h"
#include "structs.h"
#include "enums.h"

extern "C"
{
#include "xml_parser.h"
#include "fmi_cs.h"
}


typedef struct  {
	double time;

	double  * input;
	int inputLength;

	double  * output;
	int outputLength;

} ResultOfStepStruct;


using namespace std;

namespace Straylight
{
	class  ResultOfStep
	{

	private:


	public:
		vector<ScalarValue *> svListOutput;
		vector<ScalarValue *> svListInput;

		double time_;


	public:


		ResultOfStep(double time);
		~ResultOfStep(void);
		//	void setTime(double time);

		void addValue(ScalarValue * scalarValue);
		void addValueIn(ScalarValue * scalarValue);
		char * getString();

		ResultOfStepStruct* toStruct();

		void extractValues(vector<ScalarVariableRealStruct*> scalarVariableList, Enu causality);


	};



}

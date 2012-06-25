#pragma once



#include "stdafx.h"

#include "structs.h"
#include "ResultOfStep.h"
#include "Logger.h"


using namespace std;

namespace Straylight
{


	class MainDataModel
	{

	public:
		MainDataModel(Logger* logger);
		~MainDataModel(void);

		void extractScalarVariables();

		ScalarVariableRealStruct *  getSVinputArray();
		ScalarVariableRealStruct *  getSVoutputArray();
		ScalarVariableStruct *  getSVinternalArray();

		vector<ScalarVariableRealStruct*>  getSVoutputVector();
		vector<ScalarVariableRealStruct*>  getSVinputVector();


		int getInputVariableCount();
		int getOutputVariableCount();
		int getInternalVariableCount();

		ResultOfStep* getResultOfStep(double time);

		fmiStatus setScalarValueReal(int idx, double value);

		void setFMU(FMU* fmu);
		void setFmiComponent(fmiComponent fmiComponent_arg);


		void setStartValues();

		//private member variables
	private:
		vector<ScalarVariableRealStruct*> svOutput_;
		vector<ScalarVariableRealStruct*> svInput_;
		vector<ScalarVariableStruct*> svInternal_;

		ScalarVariableStruct* extractOneScalarVariable_(ScalarVariable* scalarVariable);
		ScalarVariableRealStruct* extractOneScalarVariableReal_(ScalarVariable* scalarVariable);

		ModelDescription* modelDescription_;

		int maxInternalScalarVariables;

		Logger* logger_;

		FMU* fmu_;
		fmiComponent fmiComponent_;

	};




};




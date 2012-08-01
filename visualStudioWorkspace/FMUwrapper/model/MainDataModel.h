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

using namespace std;

namespace Straylight
{


	class MainDataModel
	{

	public:
		MainDataModel(Logger* logger);
		~MainDataModel(void);

		//void extractScalarVariables();
		//void extractTypeDefinitions();

		void extract();
		void extractScalarVariables();

		ScalarVariableRealStruct *  getSVinputArray();
		ScalarVariableRealStruct *  getSVoutputArray();
		ScalarVariableRealStruct *  getSVinternalArray();

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

		TypeDefDataModel * typeDefDataModel_;
		ScalarVariableDataModel * scalarVariableDataModel_;

		//private member variables
	private:






		ScalarVariableStruct* extractOneScalarVariable_(ScalarVariable* scalarVariable);



		ModelDescription* modelDescription_;

		int maxInternalScalarVariables;

		Logger* logger_;

		FMU* fmu_;
		fmiComponent fmiComponent_;



	};




};




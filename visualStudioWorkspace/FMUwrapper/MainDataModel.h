#pragma once



#include "stdafx.h"

#include "structs.h"



using namespace std;

namespace Straylight
{


	class MainDataModel
	{

	public:
		MainDataModel(void);
		~MainDataModel(void);

		void extractScalarVariables(ModelDescription* modelDescription);

		ScalarVariableRealStruct *  getSVinputArray();
        ScalarVariableRealStruct *  getSVoutputArray();
        ScalarVariableStruct *  getSVinternalArray();

        vector<ScalarVariableRealStruct*>  getSVoutputVector();
        vector<ScalarVariableRealStruct*>  getSVinputVector();
		

		int getInputVariableCount();
        int getOutputVariableCount();
        int getInternalVariableCount();

		
	//private member variables
	private:
		vector<ScalarVariableRealStruct*> svOutput_;
		vector<ScalarVariableRealStruct*> svInput_;
		vector<ScalarVariableStruct*> svInternal_;

		ScalarVariableStruct* extractOneScalarVariable_(ScalarVariable* scalarVariable);
		ScalarVariableRealStruct* extractOneScalarVariableReal_(ScalarVariable* scalarVariable);
		
		ModelDescription* modelDescription_;

		int maxInternalScalarVariables;

	};




};




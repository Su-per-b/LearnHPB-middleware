#pragma once



#include "stdafx.h"
#include "structs.h"
#include "ResultOfStep.h"
#include "Logger.h"

#include "TypeDefFactory.h"
#include "ScalarVariableFactory.h"
#include "TypeDefDataModel.h"


using namespace std;

namespace Straylight
{


	class MainDataModel
	{

	public:
		MainDataModel(Logger* logger);
		~MainDataModel(void);

		void extractScalarVariables();
		void extractTypeDefinitions();


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
		vector<ScalarVariableRealStruct*> svOutput_; //depricated
		vector<ScalarVariableRealStruct*> svInput_;//depricated
		vector<ScalarVariableStruct*> svInternal_;//depricated

		vector<ScalarVariableRealStruct*> svVectorReal_;
		vector<ScalarVariableBooleanStruct*> svVectorBoolean_;
		vector<ScalarVariableIntegerStruct*> svVectorInteger_;
		vector<ScalarVariableEnumerationStruct*> svVectorEnumeration_;
		vector<ScalarVariableStringStruct*> svVectorString_;

		vector<TypeDefinitionReal*> typeDefVectorReal_;
		vector<TypeDefinitionBoolean*> typeDefVectorBoolean_;
		vector<TypeDefinitionInteger*> typeDefVectorInteger_;
		vector<TypeDefinitionEnumeration*> typeDefVectorEnumeration_;
		vector<TypeDefinitionString*> typeDefVectorString_;

		ScalarVariableStruct* extractOneScalarVariable_(ScalarVariable* scalarVariable);



		ModelDescription* modelDescription_;

		int maxInternalScalarVariables;

		Logger* logger_;

		FMU* fmu_;
		fmiComponent fmiComponent_;



	};




};




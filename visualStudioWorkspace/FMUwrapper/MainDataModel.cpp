#include "MainDataModel.h"




namespace Straylight
{


	/*********************************************//**
	* Default constructor. 
	*********************************************/
	MainDataModel::MainDataModel()
	{
		maxInternalScalarVariables = 1000;
	}



	/*********************************************//**
	* Destructor. Frees memory and releases FMU DLL.
	*********************************************/
	MainDataModel::~MainDataModel(void)
	{}



	void MainDataModel::extractScalarVariables(ModelDescription* modelDescription) {

		modelDescription_ = modelDescription;

		int internalSVcount = 0;

		int i;
		ScalarVariable* scalarVariable;
		for (i=0; scalarVariable = modelDescription->modelVariables[i]; i++) {
			Enu causality = getCausality(scalarVariable);

			if (causality ==  enu_input) {
				ScalarVariableRealStruct * svs = extractOneScalarVariableReal_(scalarVariable);
				svs->idx = i;
				svInput_.push_back(svs);
			} else if(causality ==  enu_output) {
				ScalarVariableRealStruct * svs = extractOneScalarVariableReal_(scalarVariable);
				svs->idx = i;
				svOutput_.push_back(svs);
			} else if(causality == enu_internal) {
				internalSVcount++;

				if (internalSVcount < maxInternalScalarVariables) {
					ScalarVariableStruct * svs = extractOneScalarVariable_(scalarVariable);
					svs->idx = i;
					svInternal_.push_back(svs);
				}

			}

		}

	}

	ScalarVariableRealStruct* MainDataModel::extractOneScalarVariableReal_(ScalarVariable* scalarVariable) {

			ScalarVariableRealStruct * svStruct = new ScalarVariableRealStruct(); 

			svStruct->name = getName( scalarVariable );
			svStruct->causality =  getCausality(scalarVariable);
			svStruct->valueReference = getValueReference(scalarVariable); 

			svStruct->description = getDescription(modelDescription_,  scalarVariable );
			if (svStruct->description == NULL) {
				svStruct->description = _T("{no description}");
			}
			
			svStruct->typeSpecReal = new TypeSpecReal();

			ValueStatus startValueStatus;
			svStruct->typeSpecReal->start = getRealStart(scalarVariable,&startValueStatus);
			svStruct->typeSpecReal->startValueStatus = startValueStatus;

			ValueStatus nominalValueStatus;
			svStruct->typeSpecReal->nominal = getRealNominal(scalarVariable,&nominalValueStatus);
			svStruct->typeSpecReal->nominalValueStatus = nominalValueStatus;

			ValueStatus minValueStatus;
			svStruct->typeSpecReal->min = getRealMin(scalarVariable,&minValueStatus);
			svStruct->typeSpecReal->minValueStatus = minValueStatus;

			ValueStatus maxValueStatus;
			svStruct->typeSpecReal->max = getRealMax(scalarVariable,&maxValueStatus);
			svStruct->typeSpecReal->maxValueStatus = maxValueStatus;



			return svStruct;

	}


	  ScalarVariableStruct *  MainDataModel::extractOneScalarVariable_(ScalarVariable* scalarVariable) {
			ScalarVariableStruct * svStruct = new ScalarVariableStruct(); // = new ScalarVariableStruct();

			svStruct->name = getName( scalarVariable );
			svStruct->causality =  getCausality(scalarVariable);
			svStruct->valueReference = getValueReference(scalarVariable); 
			svStruct->description = getDescription(modelDescription_,  scalarVariable );
			
			svStruct->typeSpec = new TypeSpec();
			svStruct->typeSpec->type = scalarVariable->typeSpec->type;
			svStruct->typeSpec->n = scalarVariable->typeSpec->n;


			if (svStruct->description == NULL) {
				svStruct->description = _T("{no description}");
			}

			return svStruct;
	}


		ScalarVariableRealStruct *  MainDataModel::getSVinputArray() {

			int count = svInput_.size();
			ScalarVariableRealStruct *svInputArray = new ScalarVariableRealStruct[count];

 
			for(int i = 0; i < count; i++)
				svInputArray[i] = *svInput_[i];


			return svInputArray;
		}


		ScalarVariableRealStruct *  MainDataModel::getSVoutputArray() {

			int count = svOutput_.size();
			ScalarVariableRealStruct * ary = new ScalarVariableRealStruct[count];

 
			for(int i = 0; i < count; i++)
				ary[i] = *svOutput_[i];


			return ary;
		}



		
		vector<ScalarVariableRealStruct*> MainDataModel::getSVoutputVector() {
			return svOutput_;
		}
		vector<ScalarVariableRealStruct*> MainDataModel::getSVinputVector() {
			return svInput_;
		}



		ScalarVariableStruct *  MainDataModel::getSVinternalArray() {

			int count = svInternal_.size();
			ScalarVariableStruct *svInternalArray = new ScalarVariableStruct[count];

 
			for(int i = 0; i < count; i++)
				svInternalArray[i] = *svInternal_[i];


			return svInternalArray;
		}




		int MainDataModel::getInputVariableCount() {
			return svInput_.size();
		}

		int MainDataModel::getOutputVariableCount() {
			return svOutput_.size();
		}

		int MainDataModel::getInternalVariableCount() {
			return svInternal_.size();
		}
}


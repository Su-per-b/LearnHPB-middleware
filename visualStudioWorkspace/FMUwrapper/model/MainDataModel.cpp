#include "MainDataModel.h"




namespace Straylight
{


	/*********************************************//**
												   * Default constructor. 
												   *********************************************/
	MainDataModel::MainDataModel(Logger* logger)
	{
		logger_ = logger;
		maxInternalScalarVariables = 1000;
	}



	/*********************************************//**
												   * Destructor. Frees memory and releases FMU DLL.
												   *********************************************/
	MainDataModel::~MainDataModel(void)
	{}


	void MainDataModel::extractTypeDefinitions() {



		TypeDefDataModel * typeDefDataModel = new TypeDefDataModel();
		typeDefDataModel->extract(fmu_->modelDescription->typeDefinitions);



	}






	void MainDataModel::extractScalarVariables() {

		int internalSVcount = 0;

		int i;
		ScalarVariable* scalarVariable;
		for (i=0; scalarVariable = fmu_->modelDescription->modelVariables[i]; i++) {

			Enu causality = getCausality(scalarVariable);
			Enu variability = getVariability(scalarVariable);

			Elm theType = scalarVariable->typeSpec->type;

			switch(theType) {

			case elm_RealType :  
				{
					//ScalarVariableRealStruct * svs = ScalarVariableFactory::makeReal(scalarVariable);
					ScalarVariableRealStruct * svs = ScalarVariableFactory::makeReal(scalarVariable); 
					svs->idx = i;
					svVectorReal_.push_back(svs);
					break;
				}
			case elm_Boolean: 
				{
					ScalarVariableBooleanStruct * svs = ScalarVariableFactory::makeBoolean(scalarVariable);
					svs->idx = i;
					svVectorBoolean_.push_back(svs);
					break;
				}

			case elm_Integer: 
				{

					ScalarVariableIntegerStruct * svs = ScalarVariableFactory::makeInteger(scalarVariable);

					svs->idx = i;
					svVectorInteger_.push_back(svs);
					break;
				}	
			case elm_EnumerationType: 
				{

					ScalarVariableEnumerationStruct * svs = ScalarVariableFactory::makeEnumeration(scalarVariable);
					svs->idx = i;


					/*
					if (svs->typeSpecEnumeration->declaredType != NULL) {

						int count = typeDefVectorEnumeration_.size();

						for(int i = 0; i < count; i++) {
							TypeDefinitionEnumeration * td = typeDefVectorEnumeration_[i];

							if (!strcmp(td->name, svs->name)) {

								svs->typeSpecEnumeration = new TypeSpecEnumeration();

								svs->typeSpecEnumeration->enumerationItemAry = td->itemArray;
								svs->typeSpecEnumeration->max = td->max;
								svs->typeSpecEnumeration->maxValueStatus = td->maxValueStatus;
								svs->typeSpecEnumeration->min = td->minValueStatus;
								svs->typeSpecEnumeration->minValueStatus = td->minValueStatus;

							}
						}
					}
					*/


					svVectorEnumeration_.push_back(svs);
					break;
				}

			case elm_String: 
				{
					ScalarVariableStringStruct * svs = ScalarVariableFactory::makeString(scalarVariable);

					svs->idx = i;
					svVectorString_.push_back(svs);
					break;
				}	

			}





			if (causality ==  enu_input) {
				//ScalarVariableRealStruct * svs = extractOneScalarVariableReal_(scalarVariable);
				ScalarVariableRealStruct * svs = ScalarVariableFactory::makeReal(scalarVariable);
				svs->idx = i;
				svInput_.push_back(svs);
			} else if(causality ==  enu_output) {
				//ScalarVariableRealStruct * svs = extractOneScalarVariableReal_(scalarVariable);
				ScalarVariableRealStruct * svs = ScalarVariableFactory::makeReal(scalarVariable);
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


			if (variability == enu_parameter) {
				//ScalarVariableRealStruct * svs = extractOneScalarVariableReal_(scalarVariable);
				//	svs->idx = i;
				//	svParameterReal_.push_back(svs);

			}


		}

	}






	ScalarVariableStruct *  MainDataModel::extractOneScalarVariable_(ScalarVariable* scalarVariable) {
		ScalarVariableStruct * svStruct = new ScalarVariableStruct(); // = new ScalarVariableStruct();

		svStruct->name = getName( scalarVariable );
		svStruct->causality =  getCausality(scalarVariable);
		svStruct->valueReference = getValueReference(scalarVariable); 
		svStruct->description = getDescription(fmu_->modelDescription,  scalarVariable );

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

	ResultOfStep * MainDataModel::getResultOfStep(double time)
	{
		ResultOfStep * resultOfStep = new ResultOfStep(time);

		resultOfStep->extractValues(svOutput_, enu_output);
		resultOfStep->extractValues(svInput_, enu_input);

		return resultOfStep;
	}

	fmiStatus MainDataModel::setScalarValueReal(int idx, double value)
	{
		ScalarValue * scalarValue = new ScalarValue(idx);

		fmiReal realNumber1;
		fmiReal realNumber2;
		fmiReal realNumber3;

		fmiStatus status1;
		fmiStatus status2;
		fmiStatus status3;

		realNumber1 = scalarValue->getRealNumber();
		status1 = scalarValue->getStatus();

		if (status1 == fmiFatal || status1 == fmiError ) {
			logger_->printError(_T("MainController::changeInput - error reading initial real value: " ));
			return status1;
		} else {
			scalarValue->setRealNumber(value);
			status2 = scalarValue->getStatus();

			if (status1 == fmiFatal || status1 == fmiError ) {
				logger_->printError(_T("MainController::changeInput - error writing real value:" ));
				return status2;
			} else {

				realNumber3 = scalarValue->getRealNumber();
				status3 = scalarValue->getStatus();

				if (status1 == fmiFatal || status1 == fmiError ) {
					logger_->printError(_T("MainController::changeInput - error reading real value after written: " ));
				} 

				return status3;
			}
		}


		//fmiComponent

	}


	void MainDataModel::setFMU(FMU* fmu) {
		fmu_ = fmu;
		ScalarValue::setFMU( fmu);
	}

	void MainDataModel::setFmiComponent(fmiComponent fmiComponent_arg) {
		fmiComponent_ = fmiComponent_arg;
		ScalarValue::setFmiComponent( fmiComponent_arg);
	}

	void MainDataModel::setStartValues() {


		int len = svInput_.size();

		for (int i = 0; i < len; i++)
		{

			ScalarVariableRealStruct * svStruct = svInput_[i];
			ValueStatus status = (ValueStatus) svStruct->typeSpecReal->startValueStatus;

			if(status == valueDefined) {
				setScalarValueReal(svStruct->idx, svStruct->typeSpecReal->start);
			} else {
				logger_->printError("No start value defined for input varable");
			}

		}

	}


}


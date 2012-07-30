#include "MainDataModel.h"
#include "ScalarVariableDataModel.h"



namespace Straylight
{


	/*********************************************//**
												   * Default constructor. 
												   *********************************************/
	MainDataModel::MainDataModel(Logger* logger)
	{
		logger_ = logger;
		maxInternalScalarVariables = 1000;

		typeDefDataModel_ = new TypeDefDataModel();
		scalarVariableDataModel_ = new ScalarVariableDataModel();
	}



	/*********************************************//**
												   * Destructor. Frees memory and releases FMU DLL.
												   *********************************************/
	MainDataModel::~MainDataModel(void)
	{
		delete typeDefDataModel_;
		delete scalarVariableDataModel_;

	}





	void MainDataModel::extract() {

		
		typeDefDataModel_->extract(fmu_->modelDescription->typeDefinitions);
		scalarVariableDataModel_->extract(fmu_->modelDescription->modelVariables);

		ostringstream convert;   // stream used for the conversion
		convert << getInputVariableCount();      // insert the textual representation of 'Number' in the characters in the stream
		
		string str;
		str = convert.str(); 

		const char  * theCstr = str.c_str();

		//logger_->printDebug2("getInputVariableCount() - %s\n", theCstr);

	}






	ScalarVariableRealStruct *  MainDataModel::getSVinputArray() {
		return scalarVariableDataModel_->svInput_->getRealAsArray();
	}


	ScalarVariableRealStruct *  MainDataModel::getSVoutputArray() {
		return scalarVariableDataModel_->svOutput_->getRealAsArray();
	}

	ScalarVariableRealStruct *  MainDataModel::getSVinternalArray() {
		return scalarVariableDataModel_->svInternal_->getRealAsArray();
	}


	vector<ScalarVariableRealStruct*> MainDataModel::getSVinputVector() {
		return scalarVariableDataModel_->svInput_->real;
	}

	vector<ScalarVariableRealStruct*> MainDataModel::getSVoutputVector() {
		return scalarVariableDataModel_->svOutput_->real;
	}



	int MainDataModel::getInputVariableCount() {
		return scalarVariableDataModel_->svInput_->real.size();
	}

	int MainDataModel::getOutputVariableCount() {
		return scalarVariableDataModel_->svOutput_->real.size();
	}

	int MainDataModel::getInternalVariableCount() {
		return scalarVariableDataModel_->svInternal_->real.size();
	}


	ResultOfStep * MainDataModel::getResultOfStep(double time)
	{
		ResultOfStep * resultOfStep = new ResultOfStep(time);

		resultOfStep->extractValues(scalarVariableDataModel_->svOutput_->real, enu_output);
		resultOfStep->extractValues(scalarVariableDataModel_->svInput_->real, enu_input);

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


		int len =  scalarVariableDataModel_->svInput_->real.size();

		for (int i = 0; i < len; i++)
		{

			ScalarVariableRealStruct * svStruct =  scalarVariableDataModel_->svInput_->real[i];
			ValueStatus status = (ValueStatus) svStruct->typeSpecReal->startValueStatus;

			if(status == valueDefined) {
				setScalarValueReal(svStruct->idx, svStruct->typeSpecReal->start);
			} else {
				logger_->printError("No start value defined for input varable");
			}

		}

	}


}


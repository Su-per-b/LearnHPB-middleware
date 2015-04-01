#include "MainDataModel.h"

namespace Straylight
{
	/*******************************************************//**
	 * Default constructor.
	 *******************************************************/
	MainDataModel::MainDataModel()
	{
		maxInternalScalarVariables = 1000;
		Logger::getInstance()->printDebug("MainController::staticLogger");

		fmiModelAttributes_ = new FmiModelAttributes();
		unitDefinitions_ = new UnitDefinitions();
		typeDefinitions_ = new TypeDefinitions();
		scalarVariableDataModel_ = new ScalarVariableDataModel();

	}

	/*******************************************************//**
	 * Destructor. Frees memory and releases FMU DLL.
	 *******************************************************/
	MainDataModel::~MainDataModel(void)
	{
		delete fmiModelAttributes_;
		fmiModelAttributes_ = NULL;

		delete unitDefinitions_;
		unitDefinitions_ = NULL;

		delete typeDefinitions_;
		typeDefinitions_ = NULL;

		delete scalarVariableDataModel_;
		scalarVariableDataModel_ = NULL;

	}

	/*******************************************************//**
	 * Extracts this object.
	 *******************************************************/
	void MainDataModel::extract() {

		fmiModelAttributes_->extract(fmu_->modelDescription->attributes, fmu_->modelDescription->n);
		unitDefinitions_->extract(fmu_->modelDescription->unitDefinitions);
		typeDefinitions_->extract(fmu_->modelDescription->typeDefinitions);
		scalarVariableDataModel_->extract(fmu_->modelDescription->modelVariables);
	}

	TypeDefinitions *  MainDataModel::getTypeDefinitions() {
		return typeDefinitions_;
	}

	UnitDefinitions *  MainDataModel::getUnitDefinitions() {
		return unitDefinitions_;
	}

	

	/*******************************************************//**
	 * Sets scalar value real.
	 *
	 * @param	idx  	The index.
	 * @param	value	The value.
	 *
	 * @return	.
	 *******************************************************/
	fmiStatus MainDataModel::setScalarValueReal(int idx, double value)
	{
		ScalarValue * scalarValue = new ScalarValue(idx);

		fmiReal realNumber1;
		//fmiReal realNumber2;
		fmiReal realNumber3;

		fmiStatus status1;
		fmiStatus status2;
		fmiStatus status3;

		realNumber1 = scalarValue->getRealNumber();
		status1 = scalarValue->getStatus();

		if (status1 == fmiFatal || status1 == fmiError ) {
			Logger::getInstance()->printError(_T("MainController::changeInput - error reading initial real value: " ));
			return status1;
		} else {
			scalarValue->setRealNumber(value);
			status2 = scalarValue->getStatus();

			if (status1 == fmiFatal || status1 == fmiError ) {
				Logger::getInstance()->printError(_T("MainController::changeInput - error writing real value:" ));
				return status2;
			} else {
				realNumber3 = scalarValue->getRealNumber();
				status3 = scalarValue->getStatus();

				if (status1 == fmiFatal || status1 == fmiError ) {
					Logger::getInstance()->printError(_T("MainController::changeInput - error reading real value after written: " ));
				}

				return status3;
			}
		}
	}

	/*******************************************************//**
	 * Sets a fmu.
	 *
	 * @param [in,out]	fmu	If non-null, the fmu.
	 *******************************************************/
	void MainDataModel::setFMU(FMU* fmu) {
		fmu_ = fmu;
		ScalarValue::setFMU( fmu);
	}

	/*******************************************************//**
	 * Sets fmi component.
	 *
	 * @param	fmiComponent_arg	The fmi component argument.
	 *******************************************************/
	void MainDataModel::setFmiComponent(fmiComponent fmiComponent_arg) {
		fmiComponent_ = fmiComponent_arg;
		ScalarValue::setFmiComponent( fmiComponent_arg);
	}

	/*******************************************************//**
	 * Sets start values.
	 *******************************************************/
	void MainDataModel::setStartValues() {

		int len =  scalarVariableDataModel_->svInput_->real.size();

		for (int i = 0; i < len; i++)
		{
			ScalarVariableRealStruct * svStruct =  scalarVariableDataModel_->svInput_->real[i];
			ValueStatus status = (ValueStatus) svStruct->typeSpecReal->startValueStatus;

			if(status == valueDefined) {
				setScalarValueRealByIdx(svStruct->idx, svStruct->typeSpecReal->start);
			} else {
				Logger::getInstance()->printError("No start value defined for input varable");
			}
		}


		int len2 = scalarVariableDataModel_->svInternal_->real.size();

		for (int j = 0; j < len2; j++)
		{
			ScalarVariableRealStruct * svStruct = scalarVariableDataModel_->svInternal_->real[j];
			ValueStatus status = (ValueStatus)svStruct->typeSpecReal->startValueStatus;

			if (status == valueDefined) {
				setScalarValueRealByIdx(svStruct->idx, svStruct->typeSpecReal->start);
			}
			else {
				//Logger::getInstance()->printError("No start value defined for internal varable");
			}
		}




	}



	/*******************************************************//**
	 * Sets scalar values.
	 *
	 * @param [in,out]	scalarValueAry	If non-null, the scalar value ary.
	 * @param	length				  	The length.
	 *******************************************************/
	void MainDataModel::setScalarValues (ScalarValueRealStruct * scalarValueAry, int length) {
		for (int i = 0; i < length; i++)
		{
			ScalarValueRealStruct  sv = scalarValueAry[i];
			fmiStatus status = setScalarValueReal(sv.idx, sv.value);

			if (status == fmiOK) {
				//Logger::instance->printDebug("setScalarValueReal fmiOK \n");
			} else {
				Logger::getInstance()->printError("setScalarValueReal ERROR \n");
			}
		}
	}


	fmiStatus  MainDataModel::setOneScalarValue(ScalarValueRealStruct * scalarValue) {

		fmiStatus status = setScalarValueReal(scalarValue->idx, scalarValue->value);

		if (status == fmiOK) {
			//Logger::instance->printDebug("setScalarValueReal fmiOK \n");
		}
		else {
			Logger::getInstance()->printError("setScalarValueReal ERROR \n");
		}

		return status;
		
	}





	/*******************************************************//**
	 * <summary> Gets scalar value results.</summary>
	 *
	 * <param name="time"> The time.</param>
	 *
	 * <returns> null if it fails, else the scalar value results.</returns>
	 *******************************************************/
	ScalarValueResults * MainDataModel::getScalarValueResults(double time)
	{
		ScalarValueResults * scalarValueResults = new ScalarValueResults(time, scalarVariableDataModel_);
		return scalarValueResults;
	}

	fmiStatus MainDataModel::setScalarValueRealByIdx(int idx, double value)
	{
		ScalarValue * scalarValue = new ScalarValue(idx);
		//fmiReal realNumber;

		fmiStatus status;
		scalarValue->setRealNumber(value);
		status = scalarValue->getStatus();

			if (status == fmiOK) {
				//Logger::getInstance()->printDebug("setScalarValueReal fmiOK");
			} else {
				Logger::getInstance()->printError("setScalarValueRealByIdx ERROR");
			}

		return status;
	}


	void MainDataModel::setOutputVariableNames(StringMap * outputNamesStringMap) {

		scalarVariableDataModel_->setOutputVariableNames(outputNamesStringMap);

	}


	void MainDataModel::setInputVariableNames(StringMap * inputNamesStringMap) {

		scalarVariableDataModel_->setInputVariableNames(inputNamesStringMap);

	}

	

	FMImodelAttributesStruct * MainDataModel::getFMImodelAttributesStruct() {

		FMImodelAttributesStruct * fmiModelAttributesStruct = fmiModelAttributes_->toStruct();

		return fmiModelAttributesStruct;
	}


	BaseUnitStruct *  MainDataModel::getBaseUnitStructAry() {
		BaseUnitStruct *  baseUnitStructAry = unitDefinitions_->getBaseUnitStructAry();
		return baseUnitStructAry;
	}


	ScalarValue * MainDataModel::getOneScalarValue(int idx) {

		ScalarValue * scalarValue = new ScalarValue(idx);
		

		return scalarValue;

	}

	ScalarVariableRealStruct * MainDataModel::getOneScalarVariableStruct(int idx) {


		ScalarVariableRealStruct * scalarVariableRealStruct = scalarVariableDataModel_->getOneScalarVariableStruct(idx);

		return scalarVariableRealStruct;

	}




}
/*******************************************************//**
 * @file	model\ScalarValue.cpp
 *
 * Implements the scalar value class.
 *******************************************************/
#include "ScalarValue.h"

namespace Straylight
{
	/*******************************************************//**
	 * The scalar value fmu.
	 *******************************************************/
	FMU * ScalarValue::fmu_;

	/*******************************************************//**
	 * The scalar value fmi component.
	 *******************************************************/
	fmiComponent ScalarValue::fmiComponent_;

	/*******************************************************//**
	 * Gets the index.
	 *
	 * @return	The index.
	 *******************************************************/
	int ScalarValue::getIdx() {
		return idx_;
	}

	/*******************************************************//**
	 * Sets a fmu.
	 *
	 * @param [in,out]	fmu	If non-null, the fmu.
	 *******************************************************/
	void ScalarValue::setFMU(FMU* fmu) {
		fmu_ = fmu;
	}

	/*******************************************************//**
	 * Sets fmi component.
	 *
	 * @param	fmiComponent_arg	The fmi component argument.
	 *******************************************************/
	void ScalarValue::setFmiComponent(fmiComponent fmiComponent_arg) {
		fmiComponent_ = fmiComponent_arg;
	}

	/*******************************************************//**
	 * Constructor.
	 *
	 * @param	idx_local	The index local.
	 *******************************************************/
	ScalarValue::ScalarValue(int idx_local)
	{
		idx_ = idx_local;
		scalarVariable_ = (ScalarVariable*)fmu_->modelDescription->modelVariables[idx_];

		valueReference_ = ::getValueReference(scalarVariable_);  //unsigned int
	}

	/*******************************************************//**
	 * Destructor.
	 *******************************************************/
	ScalarValue::~ScalarValue(void)
	{
	}

	/*******************************************************//**
	 * Gets real number.
	 *
	 * @return	The real number.
	 *******************************************************/
	fmiReal ScalarValue::getRealNumber()
	{
		fmiReal realNumber;
		status_ =  fmu_->getReal(fmiComponent_, &valueReference_, 1, &realNumber);

		return realNumber;
	}

	fmiBoolean ScalarValue::getBoolean()
	{
		fmiBoolean booleanValue;
		status_ =  fmu_->getBoolean(fmiComponent_, &valueReference_, 1, &booleanValue);

		return booleanValue;
	}



	/*******************************************************//**
	 * Sets real number.
	 *
	 * @param	realNumber	The real number.
	 *******************************************************/
	void ScalarValue::setRealNumber(double realNumber)
	{
		const fmiValueReference vr[] = {valueReference_};
		const fmiReal value[]= {realNumber};

		status_ =  fmu_->setReal(fmiComponent_, vr, 1, value);
	}

	/*******************************************************//**
	 * Gets the status.
	 *
	 * @return	The status.
	 *******************************************************/
	fmiStatus ScalarValue::getStatus() {
		return status_;
	}

	/*******************************************************//**
	 * Gets the string.
	 *
	 * @return	The string.
	 *******************************************************/
	string ScalarValue::getString(void)	{
		stringstream ss;
		string str;

		switch (this->type) {
		case 0:
			ss << this->scalarReal;
			str = ss.str();
			return str;
			break;
		case 1:
			return "unKnownType";
			break;
		case 2:
			return "unKnownType";
			break;
		case 3:
			return "unKnownType";
			break;
		default:
			return "unKnownType";
			break;
		}
	}


	fmiValueReference ScalarValue::getTheValueReference()	{

		return valueReference_;
	}

	
	ScalarValueRealStruct * ScalarValue::toStruct() {

		ScalarValueRealStruct * scalarValueRealStruct = new ScalarValueRealStruct();
		scalarValueRealStruct->idx = getIdx();
		scalarValueRealStruct->value = getRealNumber();

		return scalarValueRealStruct;

	}



}
#include "ScalarValue.h"


namespace Straylight
{


	FMU * ScalarValue::fmu_;

	fmiComponent ScalarValue::fmiComponent_;



	int ScalarValue::getIdx() {
		return idx_;
	}

	 void ScalarValue::setFMU(FMU* fmu) {
		fmu_ = fmu;
	}

	void ScalarValue::setFmiComponent(fmiComponent fmiComponent_arg) {
		fmiComponent_ = fmiComponent_arg;
	}


	ScalarValue::ScalarValue(int idx_local)
	{
		idx_ = idx_local;
		scalarVariable_ = (ScalarVariable*)fmu_->modelDescription->modelVariables[idx_];

		valueReference_ = getValueReference(scalarVariable_); //unsigned int 

	}


	ScalarValue::~ScalarValue(void)
	{

	}

	fmiReal ScalarValue::getRealNumber(void)
	{

		fmiReal realNumber;
		status_ =  fmu_->getReal(fmiComponent_, &valueReference_, 1, &realNumber);

		return realNumber;
	}

	void ScalarValue::setRealNumber(double realNumber)
	{
		status_ =  fmu_->setReal(fmiComponent_, &valueReference_, 1, &realNumber);

	}





	fmiStatus ScalarValue::getStatus() {
		return status_;
	}



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


}

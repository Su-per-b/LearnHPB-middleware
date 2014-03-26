/*******************************************************//**
 * @file	model\ScalarVariableDataModel.cpp
 *
 * Implements the scalar variable data model class.
 *******************************************************/
#include "ScalarVariableDataModel.h"

namespace Straylight
{
	/*******************************************************//**
	 * Default constructor.
	 *******************************************************/
	ScalarVariableDataModel::ScalarVariableDataModel(void)
	{
		svAll_ = new ScalarVariableCollection();
		svInput_= new ScalarVariableCollection();
		svOutput_= new ScalarVariableCollection();
		svInternal_= new ScalarVariableCollection();



	}

	/*******************************************************//**
	 * Destructor.
	 *******************************************************/
	ScalarVariableDataModel::~ScalarVariableDataModel(void)
	{

		delete svAll_;
		delete svInput_;
		delete svOutput_;
		delete svInternal_;

	}

	ScalarVariableRealStruct * ScalarVariableDataModel::getOneScalarVariableStruct(int idx) {


		
		ScalarVariableRealStruct* scalarVariableRealStruct = idx2Real2_[idx];

		return scalarVariableRealStruct;

	}



	/*******************************************************//**
	 * Extracts the given scalarVariableArray.
	 *
	 * @param [in,out]	scalarVariableArray	If non-null, array of scalar variables.
	 *******************************************************/
	void ScalarVariableDataModel::extract(ScalarVariable** scalarVariableArray) {
		int internalSVcount = 0;

		int i;
		ScalarVariable* scalarVariable;
		for (i=0; scalarVariable = scalarVariableArray[i]; i++) {
			//Enu causality = getCausality(scalarVariable);
			Elm theType = scalarVariable->typeSpec->type;




			switch(theType) {
			case elm_Real :
				{
					ScalarVariableRealStruct * svs = ScalarVariableFactory::makeReal(scalarVariable, i);

					switch (svs->causality) {
						case enu_input :
							svInput_->real.push_back(svs);
							break;
						case enu_output :
							svOutput_->real.push_back(svs);
							break;
						case enu_internal :
							svInternal_->real.push_back(svs);
							break;
					}




					idx2Real2_[svs->idx] = svs;


					//idx2Real_.insert(svs->idx, svs);

					break;
				}
			case elm_Boolean:
				{
					ScalarVariableBooleanStruct * svs = ScalarVariableFactory::makeBoolean(scalarVariable, i);

					switch (svs->causality) {
						case enu_input :
							svInput_->boolean.push_back(svs);
							break;
						case enu_output :
							svOutput_->boolean.push_back(svs);
							break;
						case enu_internal :
							svInternal_->boolean.push_back(svs);
							break;
					}

					break;
				}

			case elm_Integer:
				{
					ScalarVariableIntegerStruct * svs = ScalarVariableFactory::makeInteger(scalarVariable, i);

					svs->idx = i;

					switch (svs->causality) {
						case enu_input :
							svInput_->integer.push_back(svs);
							break;
						case enu_output :
							svOutput_->integer.push_back(svs);
							break;
						case enu_internal :
							svInternal_->integer.push_back(svs);
							break;
					}

					break;
				}
			case elm_Enumeration:
				{
					ScalarVariableEnumerationStruct * svs = ScalarVariableFactory::makeEnumeration(scalarVariable, i);



					switch (svs->causality) {
						case enu_input :
							svInput_->enumeration.push_back(svs);
							break;
						case enu_output :
							svOutput_->enumeration.push_back(svs);
							break;
						case enu_internal :
							svInternal_->enumeration.push_back(svs);
							break;
					}

					break;
				}

			case elm_String:
				{
					ScalarVariableStringStruct * svs = ScalarVariableFactory::makeString(scalarVariable, i);

					switch (svs->causality) {
						case enu_input :
							svInput_->string.push_back(svs);
							break;
						case enu_output :
							svOutput_->string.push_back(svs);
							break;
						case enu_internal :
							svInternal_->string.push_back(svs);
							break;
					}

					break;
				}
			}
		}
	}
}
/*******************************************************//**
 * \file	model\ScalarValueDataModel.cpp
 *
 * Implements the scalar value data model class.
 *******************************************************/
#include "ScalarValueResults.h"

namespace Straylight
{


	ScalarValueResults::ScalarValueResults(double time, ScalarVariableDataModel * scalarVariableDataModel)
	{
		time_ = time;
		scalarValueCollectionInput_ = new ScalarValueCollection();
		scalarValueCollectionOutput_ = new ScalarValueCollection();

		scalarVariableDataModel_ = scalarVariableDataModel;

		extract();
	}


	ScalarValueResults::~ScalarValueResults(void)
	{
	}


	void ScalarValueResults::extract()
	{
		scalarValueCollectionInput_->setReal(extractReal(scalarVariableDataModel_->svInput_->real)) ;
		scalarValueCollectionInput_->setBoolean(extractBoolean(scalarVariableDataModel_->svInput_->boolean));

		scalarValueCollectionOutput_->setReal(extractReal(scalarVariableDataModel_->svOutput_->real));
		scalarValueCollectionOutput_->setBoolean(extractBoolean(scalarVariableDataModel_->svOutput_->boolean));
	}



	vector<ScalarValueRealStruct*> ScalarValueResults::extractReal(vector<ScalarVariableRealStruct*> scalarVariableRealList) {

		vector<ScalarValueRealStruct*> realList;
		vector<ScalarVariableRealStruct*>::iterator list_iter = scalarVariableRealList.begin();

		for(list_iter;
			list_iter != scalarVariableRealList.end(); list_iter++)
		{
			ScalarVariableRealStruct * scalarVariableRealStruct =  *list_iter;
			ScalarValue * scalarValue = new ScalarValue(scalarVariableRealStruct->idx);

			ScalarValueRealStruct * scalarValueRealStruct = new ScalarValueRealStruct();
			scalarValueRealStruct->idx = scalarValue->getIdx();
			scalarValueRealStruct->value = scalarValue->getRealNumber();

			realList.push_back(scalarValueRealStruct);
		}

		return realList;

	}


	vector<ScalarValueBooleanStruct*> ScalarValueResults::extractBoolean( vector<ScalarVariableBooleanStruct*> scalarVariableBooleanList )
	{

		vector<ScalarValueBooleanStruct*> booleanList;

		vector<ScalarVariableBooleanStruct*>::iterator list_iter = scalarVariableBooleanList.begin();

		for(list_iter;
			list_iter != scalarVariableBooleanList.end(); list_iter++)
		{
			ScalarVariableBooleanStruct * scalarVariableBooleanStruct =  *list_iter;
			ScalarValue * scalarValue = new ScalarValue(scalarVariableBooleanStruct->idx);

			ScalarValueBooleanStruct * scalarValueBooleanStruct = new ScalarValueBooleanStruct();

			scalarValueBooleanStruct->idx = scalarValue->getIdx();
			scalarValueBooleanStruct->value = scalarValue->getRealNumber();

			booleanList.push_back(scalarValueBooleanStruct);

		}


		return booleanList;
	}


	void ScalarValueResults::extractAll( ScalarVariableDataModel* pScalarVariableDataModel_ )
	{
		//extractReal(scalarVariableCollection->real, causality);
		//extractBoolean(scalarVariableCollection->boolean, causality);
	}

	ScalarValueResultsStruct * ScalarValueResults::toStruct()
	{
		ScalarValueResultsStruct * struct1 = new ScalarValueResultsStruct();

		struct1->input = scalarValueCollectionInput_->toStruct();
		struct1->output = scalarValueCollectionOutput_->toStruct();
		struct1->time = time_;

		return struct1;
	}

	void ScalarValueResults::toString( std::string & stringRef )
	{
		std::stringstream ss;
		ss << _T("Time: ") << time_ << _T("  - input:");

		std::string strInput;
		scalarValueCollectionInput_->toString(strInput);
		ss << strInput << _T(" ");

		std::string strOutput;
		scalarValueCollectionOutput_->toString(strOutput);
		ss << strOutput << _T(" ");

		stringRef = ss.str();

	}


}
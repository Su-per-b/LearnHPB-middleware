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

		extractOne(scalarVariableDataModel->svInput_, scalarValueCollectionInput_);
		extractOne(scalarVariableDataModel->svOutput_, scalarValueCollectionOutput_);

	}

	void ScalarValueResults::extractOne(
		ScalarVariableCollection *  scalarVariableCollection,
		ScalarValueCollection * scalarValueCollection
		)
	{

		vector<ScalarValueRealStruct*> realList = extractReal(scalarVariableCollection->real);
		vector<ScalarValueBooleanStruct*> booleanList = extractBoolean(scalarVariableCollection->boolean);

		scalarValueCollection->setReal(realList);
		scalarValueCollection->setBoolean(booleanList);
	}

	ScalarValueResults::~ScalarValueResults(void)
	{

		if (NULL != scalarValueCollectionInput_) {
			delete scalarValueCollectionInput_;
		}

		if (NULL != scalarValueCollectionOutput_) {
			delete scalarValueCollectionOutput_;
		}


	}

	//void ScalarValueResults::extractOneTo(
	//	ScalarVariableCollection *  scalarVariableCollection,
	//	ScalarValueCollection * scalarValueCollection
	//	)
	//{
	//	scalarValueCollectionInput_->setReal(extractReal(scalarVariableDataModel_->svInput_->real));
	//	scalarValueCollectionInput_->setBoolean(extractBoolean(scalarVariableDataModel_->svInput_->boolean));

	//	scalarValueCollectionOutput_->setReal(extractReal(scalarVariableDataModel_->svOutput_->real));
	//	scalarValueCollectionOutput_->setBoolean(extractBoolean(scalarVariableDataModel_->svOutput_->boolean));
	//}


	//void ScalarValueResults::extract()
	//{
	//	scalarValueCollectionInput_->setReal(extractReal(scalarVariableDataModel_->svInput_->real)) ;
	//	scalarValueCollectionInput_->setBoolean(extractBoolean(scalarVariableDataModel_->svInput_->boolean));

	//	scalarValueCollectionOutput_->setReal(extractReal(scalarVariableDataModel_->svOutput_->real));
	//	scalarValueCollectionOutput_->setBoolean(extractBoolean(scalarVariableDataModel_->svOutput_->boolean));
	//}



	vector<ScalarValueRealStruct*> ScalarValueResults::extractReal(vector<ScalarVariableRealStruct*> scalarVariableRealList) {

		vector<ScalarValueRealStruct*> realList;
		vector<ScalarVariableRealStruct*>::iterator list_iter = scalarVariableRealList.begin();

		for(list_iter;
			list_iter != scalarVariableRealList.end(); list_iter++)
		{
			ScalarVariableRealStruct * scalarVariableRealStruct =  *list_iter;
			ScalarValue * scalarValue = new ScalarValue(scalarVariableRealStruct->idx);

			ScalarValueRealStruct * scalarValueRealStruct = scalarValue->toStruct();

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
			scalarValueBooleanStruct->value = scalarValue->getBoolean();

			booleanList.push_back(scalarValueBooleanStruct);

		}


		return booleanList;
	}


	//void ScalarValueResults::extractAll( ScalarVariableDataModel* pScalarVariableDataModel_ )
	//{
	//	//extractReal(scalarVariableCollection->real, causality);
	//	//extractBoolean(scalarVariableCollection->boolean, causality);
	//}

	ScalarValueResultsStruct * ScalarValueResults::toStruct()
	{
		ScalarValueResultsStruct * struct1 = new ScalarValueResultsStruct();

		struct1->input = scalarValueCollectionInput_->toStruct();
		struct1->output = scalarValueCollectionOutput_->toStruct();
		struct1->time = time_;

		return struct1;
	}

	void ScalarValueResults::toString( char* buffer, int len )
	{

		std::stringstream ss;
		ss << _T("Time: ") << time_ << _T("\n    --=input:");

		std::string strInput;
		scalarValueCollectionInput_->toString(strInput);
		ss << strInput << _T("\n    --=output:");

		std::string strOutput;
		scalarValueCollectionOutput_->toString(strOutput);
		ss << strOutput << _T("\n");

		string str4 =  ss.str();

		strncpy_s(buffer, sizeof(buffer), str4.c_str(), len - 1); // copy things, but don't overrun buffer
		buffer[len - 1] = '\0'; // 0 terminates the c-string


	}


}
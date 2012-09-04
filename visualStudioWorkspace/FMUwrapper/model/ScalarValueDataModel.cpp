#include "ScalarValueDataModel.h"

namespace Straylight
{
	/*******************************************************//**
	 * Constructor.
	 *
	 * @param	time	The time.
	 *******************************************************/
	ScalarValueDataModel::ScalarValueDataModel(double time)
	{
		time_ = time;
		scalarValueCollection_ = new ScalarValueCollection();

	}

	/*******************************************************//**
	 * Destructor.
	 *******************************************************/
	ScalarValueDataModel::~ScalarValueDataModel(void)
	{
	}

	/*******************************************************//**
	 * Extracts this object.
	 *
	 * @param [in,out]	scalarVariableCollection	If non-null, collection of scalar variables.
	 * @param	causality							The causality.
	 *******************************************************/
	void ScalarValueDataModel::extract(ScalarVariableCollection * scalarVariableCollection, Enu causality) {


		extractReal(scalarVariableCollection->real, causality);
		


	}

	/*******************************************************//**
	 * Extracts the real.
	 *
	 * @param [in,out]	scalarVariableRealList	If non-null, list of scalar variable reals.
	 * @param	causality					  	The causality.
	 *******************************************************/
	void ScalarValueDataModel::extractReal(vector<ScalarVariableRealStruct*> scalarVariableRealList, Enu causality) {
		vector<ScalarVariableRealStruct*>::iterator list_iter = scalarVariableRealList.begin();

		for(list_iter;
			list_iter != scalarVariableRealList.end(); list_iter++)
		{
			ScalarVariableRealStruct * scalarVariableRealStruct =  *list_iter;
			ScalarValue * scalarValue = new ScalarValue(scalarVariableRealStruct->idx);

			ScalarValueRealStruct * scalarValueRealStruct = new ScalarValueRealStruct();
			scalarValueRealStruct->idx = scalarValue->getIdx();
			scalarValueRealStruct->value = scalarValue->getRealNumber();


			scalarValueCollection_->real.push_back(scalarValueRealStruct);


		}







	}
}
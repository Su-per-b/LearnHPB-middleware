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


		//int c =1;
		
		extractReal(scalarVariableCollection->real, causality);

	}

	/*******************************************************//**
	 * Extracts the real.
	 *
	 * @param [in,out]	scalarVariableRealList	If non-null, list of scalar variable reals.
	 * @param	causality					  	The causality.
	 *******************************************************/
	void extractReal(vector<ScalarVariableRealStruct*> scalarVariableRealList, Enu causality) {


		vector<ScalarVariableRealStruct*>::iterator list_iter = scalarVariableRealList.begin();

		for(list_iter; 
			list_iter != scalarVariableRealList.end(); list_iter++)
		{
			ScalarVariableRealStruct * scalarVariableRealStruct =  *list_iter;
			ScalarValue * scalarValue = new ScalarValue(scalarVariableRealStruct->idx);

			if (causality == enu_input) {
				svListInput.push_back(scalarValue);
			} else if (causality == enu_output){
				svListOutput.push_back(scalarValue);
			}

		}

	}


	


}
#include "ScalarVariableDataModel.h"



namespace Straylight
{
	ScalarVariableDataModel::ScalarVariableDataModel(void)
	{
		svAll_ = new ScalarVariableCollection();
		svInput_= new ScalarVariableCollection();
		svOutput_= new ScalarVariableCollection();
		svInternal_= new ScalarVariableCollection();
	}


	ScalarVariableDataModel::~ScalarVariableDataModel(void)
	{
	}

	void ScalarVariableDataModel::extract(ScalarVariable** scalarVariableArray) {
		


		int internalSVcount = 0;

		int i;
		ScalarVariable* scalarVariable;
		for (i=0; scalarVariable = scalarVariableArray[i]; i++) {

			Enu causality = getCausality(scalarVariable);
			Enu variability = getVariability(scalarVariable);

			Elm theType = scalarVariable->typeSpec->type;

			switch(theType) {

			case elm_Real :  
				{
					//ScalarVariableRealStruct * svs = ScalarVariableFactory::makeReal(scalarVariable);
					ScalarVariableRealStruct * svs = ScalarVariableFactory::makeReal(scalarVariable); 

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


					svs->idx = i;
					svAll_->real.push_back(svs);
					break;
				}
			case elm_Boolean: 
				{
					ScalarVariableBooleanStruct * svs = ScalarVariableFactory::makeBoolean(scalarVariable);
					svs->idx = i;
					svAll_->boolean.push_back(svs);
					break;
				}

			case elm_Integer: 
				{

					ScalarVariableIntegerStruct * svs = ScalarVariableFactory::makeInteger(scalarVariable);

					svs->idx = i;
					svAll_->integer.push_back(svs);
					break;
				}	
			case elm_Enumeration: 
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


					svAll_->enumeration.push_back(svs);
					break;
				}

			case elm_String: 
				{
					ScalarVariableStringStruct * svs = ScalarVariableFactory::makeString(scalarVariable);

					svs->idx = i;
					svAll_->string.push_back(svs);
					break;
				}	

			}







		}

	}
}
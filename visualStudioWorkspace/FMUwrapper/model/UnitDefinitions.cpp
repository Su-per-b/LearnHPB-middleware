#include "UnitDefinitions.h"



namespace Straylight
{


	UnitDefinitions::UnitDefinitions()
	{
	}


	UnitDefinitions::~UnitDefinitions()
	{

		int len = baseUnitVector_.size();

		for (int i = 0; i < len; ++i)
		{
			delete baseUnitVector_[i];
			baseUnitVector_[i] = NULL;
		}

		baseUnitVector_.clear();

	}

	BaseUnitStruct * UnitDefinitions::getBaseUnitStructAry()
	{

		int count = baseUnitVector_.size();

		if (count > 0) {

			BaseUnitStruct *ary = new BaseUnitStruct[count];

			for (int i = 0; i < count; i++) {

				BaseUnit * bu = baseUnitVector_[i];

				BaseUnitStruct * baseUnitStruct = bu->toStruct();


				ary[i] = * baseUnitStruct;
			}

			return ary;
		}
		else {
			return new BaseUnitStruct();
		}

	}



	void UnitDefinitions::extract(ListElement** unitDefinitionAry)
	{

		int count = 0;

		ListElement* listElement;

		for (int i = 0; listElement = unitDefinitionAry[i]; i++) {


			if (listElement->type == elm_BaseUnit) {

				BaseUnit * baseUnit = new BaseUnit(listElement);

				baseUnitVector_.push_back(baseUnit);
			}


			count++;
		}
		

		return;
	}



}
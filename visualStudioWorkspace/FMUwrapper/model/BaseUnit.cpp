#include "BaseUnit.h"



namespace Straylight
{


	BaseUnit::BaseUnit(ListElement* listElement)
	{

		attributeStructVector_ = Straylight::Utils::extractAttributesFromListElement(listElement);

		//extract Elements
		//int count = listElement->list->n;

		for (int i = 0; Element* element = listElement->list[i]; i++) {

			//if (element != nullptr) {

				if (element->type == elm_DisplayUnitDefinition) {

					DisplayUnitDefinition * displayUnitDefinition = new DisplayUnitDefinition(element);
					displayUnitDefinitionVector_.push_back(displayUnitDefinition);

				}

			//}

		};


	}


	BaseUnit::~BaseUnit()
	{
	}


	BaseUnitStruct * BaseUnit::toStruct() {

		BaseUnitStruct * baseUnitStruct = new BaseUnitStruct();

		int size = attributeStructVector_.size();
		
		for (int i = 0; i < size; i++) {

			AttributeStruct* att = attributeStructVector_[i];


			if (strcmp(att->name, "unit"))
			{
				// strings are not equal
			}
			else
			{
				baseUnitStruct->unit = att->value;
			}
			



		}



		int size2 = displayUnitDefinitionVector_.size();

		DisplayUnitDefinitionStruct *structAry = new DisplayUnitDefinitionStruct[size2];

		for (int i = 0; i < size2; i++) {

			DisplayUnitDefinition* duObj = displayUnitDefinitionVector_[i];
			DisplayUnitDefinitionStruct * duStruct = duObj->toStruct();
			structAry[i] = * duStruct;

		}


		baseUnitStruct->displayUnitDefinitions = structAry;


		return baseUnitStruct;
	}



}
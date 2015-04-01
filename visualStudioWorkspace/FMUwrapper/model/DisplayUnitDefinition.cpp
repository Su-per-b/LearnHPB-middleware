#include "DisplayUnitDefinition.h"

namespace Straylight
{

	DisplayUnitDefinition::DisplayUnitDefinition(Element* element)
	{

		//attributeStructVector_ = Straylight::Utils::extractAttributesFromElement(element);
		//int len = attributeStructVector_.size();

		ValueStatus offsetValueStatus;
		offset_ = getElementAttributeReal(element, &offsetValueStatus, att_offset);
		offsetValueStatus_ = offsetValueStatus;

		ValueStatus gainValueStatus;
		gain_ = getElementAttributeReal(element, &gainValueStatus, att_gain);
		gainValueStatus_ = gainValueStatus;

		ValueStatus displayUnitValueStatus;
		displayUnit_ = getObjectAttributeString(element, &displayUnitValueStatus, att_displayUnit);
		displayUnitValueStatus_ = displayUnitValueStatus;

		return;

	}


	DisplayUnitDefinition::~DisplayUnitDefinition()
	{

		//int len = attributeStructVector_.size();
		//for (int i = 0; i < len; ++i)
		//{
		//	delete attributeStructVector_[i];
		//	attributeStructVector_[i] = NULL;
		//}
		//attributeStructVector_.clear();


	}

	//vector<AttributeStruct*> DisplayUnitDefinition::getAttributeStructVector()
	//{
	//	return attributeStructVector_;

	//}


	DisplayUnitDefinitionStruct * DisplayUnitDefinition::toStruct() {


		DisplayUnitDefinitionStruct * duStruct = new DisplayUnitDefinitionStruct();

		duStruct->displayUnit = displayUnit_;
		duStruct->displayUnitValueStatus = displayUnitValueStatus_;

		duStruct->offset = offset_;
		duStruct->offsetValueStatus = offsetValueStatus_;

		duStruct->gain = gain_;
		duStruct->gainValueStatus = gainValueStatus_;



		return duStruct;

	}




}
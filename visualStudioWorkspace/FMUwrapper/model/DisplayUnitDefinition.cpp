#include "DisplayUnitDefinition.h"

namespace Straylight
{

	DisplayUnitDefinition::DisplayUnitDefinition(Element* element)
	{
		attributeStructVector_ = Straylight::Utils::extractAttributesFromElement(element);
		Element* ee = element;
	}


	DisplayUnitDefinition::~DisplayUnitDefinition()
	{

		int len = attributeStructVector_.size();
		for (int i = 0; i < len; ++i)
		{
			delete attributeStructVector_[i];
		}
		attributeStructVector_.clear();


	}

	vector<AttributeStruct*> DisplayUnitDefinition::getAttributeStructVector()
	{
		return attributeStructVector_;

	}


	DisplayUnitDefinitionStruct * DisplayUnitDefinition::toStruct() {


		int len = attributeStructVector_.size();
		DisplayUnitDefinitionStruct * duStruct = new DisplayUnitDefinitionStruct();

		duStruct->displayUnit = _T("{not set}");
		duStruct->offset = _T("{not set}");
		duStruct->gain = _T("{not set}");

		for (int i = 0; i < len; i++) {

			AttributeStruct * att = attributeStructVector_[i];

			int result = strcmp(att->name, "displayUnit");

			if (result == 0) {
				duStruct->displayUnit = att->value;
			} else if (strcmp(att->name, "offset") == 0) {
				duStruct->offset = att->value;
			}
			else if (strcmp(att->name, "gain") == 0) {
				duStruct->gain = att->value;
			}

		}

		return duStruct;

	}




}
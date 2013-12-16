#pragma once

#include "stdafx.h"
#include <Logger.h>
#include "structs.h"
#include "DisplayUnitDefinition.h"
#include <Utils.h>


namespace Straylight
{

	class BaseUnit
	{
	public:
		BaseUnit(ListElement* listElement);
		~BaseUnit();

		BaseUnitStruct * toStruct();
	private:

		vector<AttributeStruct*> attributeStructVector_;
		vector<DisplayUnitDefinition*> displayUnitDefinitionVector_;

	};

}

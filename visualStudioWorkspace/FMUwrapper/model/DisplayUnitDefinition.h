#pragma once

#include "stdafx.h"
#include <Logger.h>
#include "structs.h"


namespace Straylight
{

	class DisplayUnitDefinition
	{
	public:
		DisplayUnitDefinition(Element* element);

		~DisplayUnitDefinition();
	
		vector<AttributeStruct*> getAttributeStructVector();

		DisplayUnitDefinitionStruct * toStruct();



	private:
		vector<AttributeStruct*>  attributeStructVector_;

	};


}
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
	
		DisplayUnitDefinitionStruct * toStruct();


	private:

		const char * displayUnit_;
		ValueStatus displayUnitValueStatus_;

		double offset_;
		ValueStatus offsetValueStatus_;
		
		double gain_;
		ValueStatus gainValueStatus_;

	};


}
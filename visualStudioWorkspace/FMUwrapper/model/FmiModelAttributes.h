#pragma once

#include "stdafx.h"
#include <Logger.h>
#include "structs.h"



namespace Straylight
{

	class FmiModelAttributes
	{

	public:
		FmiModelAttributes();
		~FmiModelAttributes();

		void extract(const char** attributes, int attributeCount);

		FMImodelAttributesStruct * toStruct();

	private:

		vector<AttributeStruct*> attributeStructVector_;


	};


}
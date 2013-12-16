#pragma once
#include "stdafx.h"
#include <Logger.h>
#include "structs.h"
#include "BaseUnit.h"

namespace Straylight
{


	class UnitDefinitions
	{
	public:
		UnitDefinitions();
		~UnitDefinitions();

		void extract(ListElement** unitDefinitions);

		BaseUnitStruct * getBaseUnitStructAry();

	private:

		vector<BaseUnit*> baseUnitVector_;


	};

}


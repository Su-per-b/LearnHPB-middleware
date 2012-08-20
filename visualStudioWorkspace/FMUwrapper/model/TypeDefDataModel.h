#pragma once
#include "stdafx.h"
#include "structs.h"
#include "Utils.h"
#include <Logger.h>
#include "TypeDefFactory.h"


namespace Straylight
{
	class TypeDefDataModel
	{
	public:
		TypeDefDataModel();
		~TypeDefDataModel(void);

		void extract(Type** typeDefinitions);


	private:
		vector<TypeDefinitionReal*> typeDefVectorReal_;
		vector<TypeDefinitionBoolean*> typeDefVectorBoolean_;
		vector<TypeDefinitionInteger*> typeDefVectorInteger_;
		vector<TypeDefinitionEnumeration*> typeDefVectorEnumeration_;
		vector<TypeDefinitionString*> typeDefVectorString_;


	};
}

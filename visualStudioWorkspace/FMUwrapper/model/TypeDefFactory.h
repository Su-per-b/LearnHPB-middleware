#pragma once
#include "structs.h"

namespace Straylight
{
	class TypeDefFactory
	{
	public:
		TypeDefFactory(void);
		~TypeDefFactory(void);

		static TypeDefinitionReal* makeReal(Type* type);
		static TypeDefinitionBoolean* makeBoolean(Type* type);
		static TypeDefinitionInteger* makeInteger(Type* type);
		static TypeDefinitionEnumeration* makeEnumeration(Type* type);
		static TypeDefinitionString* makeString(Type* type);

	private:
		static EnumerationItem * extractItemArray(ListElement* listElement, int * length);


	};

}
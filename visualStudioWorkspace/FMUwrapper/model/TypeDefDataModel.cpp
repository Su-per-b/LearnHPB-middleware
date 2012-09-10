/*******************************************************//**
 * @file	model\TypeDefDataModel.cpp
 *
 * Implements the type def data model class.
 *******************************************************/
#include "TypeDefDataModel.h"

namespace Straylight
{
	/*******************************************************//**
	 * Default constructor.
	 *******************************************************/
	TypeDefDataModel::TypeDefDataModel()
	{
	}

	/*******************************************************//**
	 * Destructor.
	 *******************************************************/
	TypeDefDataModel::~TypeDefDataModel(void)
	{
	}

	/*******************************************************//**
	 * Extracts the given typeDefinitions.
	 *
	 * @param [in,out]	typeDefinitions	If non-null, the type definitions.
	 *******************************************************/
	void TypeDefDataModel::extract(Type** typeDefinitions) {
		Type* type;
		for (int i=0; type = typeDefinitions[i]; i++) {
			Elm theType = type->typeSpec->type;

			switch(theType) {
			case elm_RealType :
				{
					TypeDefinitionReal * typeDefinitionReal = TypeDefFactory::makeReal(type);
					typeDefinitionReal->idx = i;

					if (typeDefinitionReal->maxValueStatus == valueDefined &&
						typeDefinitionReal->start == valueDefined)
					{
						if (typeDefinitionReal->start > typeDefinitionReal->max) {
							Logger::getInstance()->printErrorInt
								( "TypeDefDataModel::extract() start value above maximum idx:%s\n", i);
						}
					}

					typeDefVectorReal_.push_back(typeDefinitionReal);
					break;
				}
			case elm_Boolean:
				{
					TypeDefinitionBoolean * typeDefinitionBoolean = TypeDefFactory::makeBoolean(type);
					typeDefinitionBoolean->idx = i;
					typeDefVectorBoolean_.push_back(typeDefinitionBoolean);
					break;
				}

			case elm_Integer:
				{
					TypeDefinitionInteger * typeDefinitionInteger = TypeDefFactory::makeInteger(type);
					typeDefinitionInteger->idx = i;
					typeDefVectorInteger_.push_back(typeDefinitionInteger);
					break;
				}
			case elm_EnumerationType:
				{
					TypeDefinitionEnumeration * typeDefinitionEnumeration = TypeDefFactory::makeEnumeration(type);
					typeDefinitionEnumeration->idx = i;
					typeDefVectorEnumeration_.push_back(typeDefinitionEnumeration);
					break;
				}

			case elm_String:
				{
					TypeDefinitionString * typeDefinitionString = TypeDefFactory::makeString(type);
					typeDefinitionString->idx = i;
					typeDefVectorString_.push_back(typeDefinitionString);
					break;
				}
			}
		}
	}
}
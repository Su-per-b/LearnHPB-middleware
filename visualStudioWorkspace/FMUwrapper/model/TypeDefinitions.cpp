/*******************************************************//**
 * @file	model\TypeDefinitions.cpp
 *
 * Implements the type def data model class.
 *******************************************************/
#include "TypeDefinitions.h"

namespace Straylight
{
	/*******************************************************//**
	 * Default constructor.
	 *******************************************************/
	TypeDefinitions::TypeDefinitions()
	{
	}

	/*******************************************************//**
	 * Destructor.
	 *******************************************************/
	TypeDefinitions::~TypeDefinitions(void)
	{
		

		int len = typeDefVectorReal_.size();
		for (int i = 0; i < len; ++i)
		{
			delete typeDefVectorReal_[i];
		}
		typeDefVectorReal_.clear();


		len = typeDefVectorBoolean_.size();
		for (int i = 0; i < len; ++i)
		{
			delete typeDefVectorBoolean_[i];
		}
		typeDefVectorBoolean_.clear();


		len = typeDefVectorInteger_.size();
		for (int i = 0; i < len; ++i)
		{
			delete typeDefVectorInteger_[i];
		}
		typeDefVectorInteger_.clear();


		len = typeDefVectorEnumeration_.size();
		for (int i = 0; i < len; ++i)
		{
			delete typeDefVectorEnumeration_[i];
		}
		typeDefVectorEnumeration_.clear();


		len = typeDefVectorString_.size();
		for (int i = 0; i < len; ++i)
		{
			delete typeDefVectorString_[i];
		}
		typeDefVectorString_.clear();


	}

	/*******************************************************//**
	 * Extracts the given typeDefinitions.
	 *
	 * @param [in,out]	typeDefinitions	If non-null, the type definitions.
	 *******************************************************/
	void TypeDefinitions::extract(Type** typeDefinitions) {
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
								( "TypeDefinitions::extract() start value above maximum idx:%s\n", i);
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
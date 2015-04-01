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



	TypeDefinitions::~TypeDefinitions(void)
	{
		

		int len = typeDefVectorReal_.size();
		for (int i = 0; i < len; ++i)
		{
			delete typeDefVectorReal_[i];
			typeDefVectorReal_[i] = NULL;
		}
		typeDefVectorReal_.clear();


		len = typeDefVectorBoolean_.size();
		for (int i = 0; i < len; ++i)
		{
			delete typeDefVectorBoolean_[i];
			typeDefVectorBoolean_[i] = NULL;
		}
		typeDefVectorBoolean_.clear();


		len = typeDefVectorInteger_.size();
		for (int i = 0; i < len; ++i)
		{
			delete typeDefVectorInteger_[i];
			typeDefVectorInteger_[i] = NULL;
		}
		typeDefVectorInteger_.clear();


		len = typeDefVectorEnumeration_.size();
		for (int i = 0; i < len; ++i)
		{
			delete typeDefVectorEnumeration_[i];
			typeDefVectorEnumeration_[i] = NULL;
		}
		typeDefVectorEnumeration_.clear();


		len = typeDefVectorString_.size();
		for (int i = 0; i < len; ++i)
		{
			delete typeDefVectorString_[i];
			typeDefVectorString_[i] = NULL;
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
					TypeDefinitionReal * typeDefinitionReal = TypeDefFactory::makeReal(type, i);


					if (typeDefinitionReal->max.status == valueDefined &&
						typeDefinitionReal->start.status == valueDefined)
					{
						if (typeDefinitionReal->start.value > typeDefinitionReal->max.value) {
							Logger::getInstance()->printErrorInt
								( "TypeDefinitions::extract() start value above maximum idx:%s\n", i);
						}
					}

					typeDefVectorReal_.push_back(typeDefinitionReal);
					break;
				}
			case elm_BooleanType:
				{
					TypeDefinitionBoolean * typeDefinitionBoolean = TypeDefFactory::makeBoolean(type, i);
					typeDefVectorBoolean_.push_back(typeDefinitionBoolean);
					break;
				}

			case elm_IntegerType:
				{
					TypeDefinitionInteger * typeDefinitionInteger = TypeDefFactory::makeInteger(type, i);
					typeDefVectorInteger_.push_back(typeDefinitionInteger);
					break;
				}
			case elm_EnumerationType:
				{
					TypeDefinitionEnumeration * typeDefinitionEnumeration = TypeDefFactory::makeEnumeration(type, i);
					typeDefVectorEnumeration_.push_back(typeDefinitionEnumeration);
					break;
				}

			case elm_StringType:
				{
					TypeDefinitionString * typeDefinitionString = TypeDefFactory::makeString(type, i);
					typeDefVectorString_.push_back(typeDefinitionString);
					break;
				}

			default: {

				Logger::getInstance()->printErrorInt
					("TypeDefinitions::extract() unknown Type idx:%s\n", i);
				 break;
			}
			}
		}
	}


	TypeDefinitionsStruct * TypeDefinitions::toStruct()
	{

		TypeDefinitionsStruct * typeDefinitionsStruct = new TypeDefinitionsStruct();


		int len_0 = typeDefVectorReal_.size();

		TypeDefinitionReal * typeDefinitionRealArray = new TypeDefinitionReal[len_0];

		for (int i = 0; i < len_0; i++)
		{
			TypeDefinitionReal * typeDefinitionReal = typeDefVectorReal_.at(i);
			typeDefinitionRealArray[i] = *typeDefinitionReal;
		}
		typeDefinitionsStruct->typeDefinitionRealArray = typeDefinitionRealArray;


		int len_1 = typeDefVectorInteger_.size();
		TypeDefinitionInteger * typeDefinitionIntegerArray = new TypeDefinitionInteger[len_1];

		for (int j = 0; j < len_1; j++)
		{
			TypeDefinitionInteger * typeDefinitionInteger = typeDefVectorInteger_.at(j);
			typeDefinitionIntegerArray[j] = *typeDefinitionInteger;
		}
		typeDefinitionsStruct->typeDefinitionIntegerArray = typeDefinitionIntegerArray;


		int len_2 = typeDefVectorBoolean_.size();
		TypeDefinitionBoolean * typeDefinitionBooleanArray = new TypeDefinitionBoolean[len_2];

		for (int k = 0; k < len_2; k++)
		{
			TypeDefinitionBoolean * typeDefinitionBoolean = typeDefVectorBoolean_.at(k);
			typeDefinitionBooleanArray[k] = *typeDefinitionBoolean;
		}
		typeDefinitionsStruct->typeDefinitionBooleanArray = typeDefinitionBooleanArray;

		int len_3 = typeDefVectorString_.size();
		TypeDefinitionString * typeDefinitionStringArray = new TypeDefinitionString[len_3];

		for (int m = 0; m < len_3; m++)
		{
			TypeDefinitionString * typeDefinitionString = typeDefVectorString_.at(m);
			typeDefinitionStringArray[m] = *typeDefinitionString;
		}
		typeDefinitionsStruct->typeDefinitionStringArray = typeDefinitionStringArray;


		int len_4 = typeDefVectorEnumeration_.size();
		TypeDefinitionEnumeration * typeDefinitionEnumerationArray = new TypeDefinitionEnumeration[len_4];

		for (int n = 0; n < len_4; n++)
		{
			TypeDefinitionEnumeration * typeDefinitionEnumeration = typeDefVectorEnumeration_.at(n);
			typeDefinitionEnumerationArray[n] = *typeDefinitionEnumeration;
		}
		typeDefinitionsStruct->typeDefinitionEnumerationArray = typeDefinitionEnumerationArray;

		return typeDefinitionsStruct;
	}



}
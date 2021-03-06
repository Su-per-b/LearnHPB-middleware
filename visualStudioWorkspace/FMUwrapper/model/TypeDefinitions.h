/*******************************************************//**
 * @file	model\TypeDefDataModel.h
 *
 * Declares the type def data model class.
 *******************************************************/
#pragma once
#include "stdafx.h"
#include "structs.h"
#include "Utils.h"
#include <Logger.h>
#include "TypeDefFactory.h"

namespace Straylight
{
	/*******************************************************//**
	 * Type def data model.
	 *******************************************************/
	class TypeDefinitions
	{
	public:
		TypeDefinitions();
		~TypeDefinitions(void);

		/*******************************************************//**
		 * Extracts the given typeDefinitions.
		 *
		 * @param [in,out]	typeDefinitions	If non-null, the type definitions.
		 *******************************************************/
		void extract(Type** typeDefinitions);

		TypeDefinitionsStruct * toStruct();

		vector<TypeDefinitionReal*> typeDefVectorReal_;

		vector<TypeDefinitionInteger*> typeDefVectorInteger_;

		vector<TypeDefinitionString*> typeDefVectorString_;

		vector<TypeDefinitionBoolean*> typeDefVectorBoolean_;

		vector<TypeDefinitionEnumeration*> typeDefVectorEnumeration_;

	private:





	};
}

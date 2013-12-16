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
	private:



		/*******************************************************//**
		 * The type def vector real.
		 *******************************************************/
		vector<TypeDefinitionReal*> typeDefVectorReal_;

		/*******************************************************//**
		 * The type def vector boolean.
		 *******************************************************/
		vector<TypeDefinitionBoolean*> typeDefVectorBoolean_;

		/*******************************************************//**
		 * The type def vector integer.
		 *******************************************************/
		vector<TypeDefinitionInteger*> typeDefVectorInteger_;

		/*******************************************************//**
		 * The type def vector enumeration.
		 *******************************************************/
		vector<TypeDefinitionEnumeration*> typeDefVectorEnumeration_;

		/*******************************************************//**
		 * The type def vector string.
		 *******************************************************/
		vector<TypeDefinitionString*> typeDefVectorString_;
	};
}

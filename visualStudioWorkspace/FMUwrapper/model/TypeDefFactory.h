/*******************************************************//**
 * @file	model\TypeDefFactory.h
 *
 * Declares the type def factory class.
 *******************************************************/
#pragma once
#include "structs.h"
#include "Utils.h"

namespace Straylight
{
	/*******************************************************//**
	 * Type def factory.
	 *******************************************************/
	class TypeDefFactory
	{
	public:

		/*******************************************************//**
		 * Default constructor.
		 *******************************************************/
		TypeDefFactory(void);

		/*******************************************************//**
		 * Destructor.
		 *******************************************************/
		~TypeDefFactory(void);

		static TypeDefinitionReal* makeReal(Type* type, int idx);

		/*******************************************************//**
		 * Makes a boolean.
		 *
		 * @param [in,out]	type	If non-null, the type.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static TypeDefinitionBoolean* makeBoolean(Type* type, int idx);

		/*******************************************************//**
		 * Makes an integer.
		 *
		 * @param [in,out]	type	If non-null, the type.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static TypeDefinitionInteger* makeInteger(Type* type, int idx);

		/*******************************************************//**
		 * Makes an enumeration.
		 *
		 * @param [in,out]	type	If non-null, the type.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static TypeDefinitionEnumeration* makeEnumeration(Type* type, int idx);

		/*******************************************************//**
		 * Makes a string.
		 *
		 * @param [in,out]	type	If non-null, the type.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static TypeDefinitionString* makeString(Type* type, int idx);

	private:

		/*******************************************************//**
		 * Extracts the item array.
		 *
		 * @param [in,out]	listElement	If non-null, the list element.
		 * @param [in,out]	length	   	If non-null, the length.
		 *
		 * @return	null if it fails, else the extracted item array.
		 *******************************************************/
		static EnumerationItem * extractItemArray(ListElement* listElement, int * length);
	};


}
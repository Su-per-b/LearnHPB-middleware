/*******************************************************//**
 * @file	model\TypeDefFactory.h
 *
 * Declares the type def factory class.
 *******************************************************/
#pragma once
#include "structs.h"

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

		static TypeDefinitionReal* makeReal(Type* type);

		/*******************************************************//**
		 * Makes a boolean.
		 *
		 * @param [in,out]	type	If non-null, the type.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static TypeDefinitionBoolean* makeBoolean(Type* type);

		/*******************************************************//**
		 * Makes an integer.
		 *
		 * @param [in,out]	type	If non-null, the type.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static TypeDefinitionInteger* makeInteger(Type* type);

		/*******************************************************//**
		 * Makes an enumeration.
		 *
		 * @param [in,out]	type	If non-null, the type.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static TypeDefinitionEnumeration* makeEnumeration(Type* type);

		/*******************************************************//**
		 * Makes a string.
		 *
		 * @param [in,out]	type	If non-null, the type.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static TypeDefinitionString* makeString(Type* type);

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

/*******************************************************//**
 * Gets or sets the.
 *
 * @value	.
 *******************************************************/
}
#pragma once
#include "stdafx.h"
#include <Logger.h>
#include "structs.h"

namespace Straylight
{
	class ScalarVariableCollection
	{
	public:

		/*******************************************************//**
		 * Default constructor.
		 *******************************************************/
		ScalarVariableCollection(void);

		/*******************************************************//**
		 * Destructor.
		 *******************************************************/
		~ScalarVariableCollection(void);

		/*******************************************************//**
		 * The real.
		 *******************************************************/
		vector<ScalarVariableRealStruct*> real;

		/*******************************************************//**
		 * The boolean.
		 *******************************************************/
		vector<ScalarVariableBooleanStruct*> boolean;

		/*******************************************************//**
		 * The integer.
		 *******************************************************/
		vector<ScalarVariableIntegerStruct*> integer;

		/*******************************************************//**
		 * The enumeration.
		 *******************************************************/
		vector<ScalarVariableEnumerationStruct*> enumeration;

		/*******************************************************//**
		 * The string.
		 *******************************************************/
		vector<ScalarVariableStringStruct*> string;

		/*******************************************************//**
		 * Gets real as array.
		 *
		 * @return	null if it fails, else the real as array.
		 *******************************************************/
		ScalarVariableRealStruct * getRealAsArray();

		/*******************************************************//**
		 * Gets boolean as array.
		 *
		 * @return	null if it fails, else the boolean as array.
		 *******************************************************/
		ScalarVariableBooleanStruct * getBooleanAsArray();

		/*******************************************************//**
		 * Gets integer as array.
		 *
		 * @return	null if it fails, else the integer as array.
		 *******************************************************/
		ScalarVariableIntegerStruct * getIntegerAsArray();

		/*******************************************************//**
		 * Gets enumeration as array.
		 *
		 * @return	null if it fails, else the enumeration as array.
		 *******************************************************/
		ScalarVariableEnumerationStruct * getEnumerationAsArray();

		/*******************************************************//**
		 * Gets string as array.
		 *
		 * @return	null if it fails, else the string as array.
		 *******************************************************/
		ScalarVariableStringStruct * getStringAsArray();

		/*******************************************************//**
		 * Converts this object to a structure.
		 *
		 * @return	null if it fails, else object converted to a structure.
		 *******************************************************/
		ScalarVariableCollectionStruct * convertToStruct();

	};





}
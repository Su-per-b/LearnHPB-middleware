#pragma once
#include "stdafx.h"
#include <Logger.h>
#include "structs.h"

namespace Straylight
{


/*******************************************************//**
* This struct is used to 
 *******************************************************/
typedef struct {

	ScalarValueRealStruct * realValue;
	int realSize;

	ScalarValueBooleanStruct * booleanValue;
	int booleanSize;

} ScalarValueCollectionStruct;


	/*******************************************************//**
	 * <summary> Collection of scalar variables. This is essentially for storing Meta-Data 
	 * 			 describing the input and output values</summary>
	 *
	 * <remarks> Raj Dye raj@pcdigi.com, 9/3/2012.</remarks>
	 *******************************************************/
	class ScalarVariableCollection
	{
	public:

		/*******************************************************//**
		 * Default constructor t
		 *******************************************************/
		ScalarVariableCollection(void);

		/*******************************************************//**
		 * Destructor 
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
		ScalarVariableCollectionStruct * toStruct();



	};
}
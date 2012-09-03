/*******************************************************//**
 * @file	model\ScalarValueCollection.h
 *
 * Declares the scalar value collection class.
 *******************************************************/
#pragma once

#include "stdafx.h"
#include <Logger.h>
#include "structs.h"
#include "ScalarValue.h"


namespace Straylight
{
	/*******************************************************//**
	 * Collection of scalar values.
	 *******************************************************/
	class ScalarValueCollection
	{
	public:

		/*******************************************************//**
		 * Default constructor.
		 *******************************************************/
		ScalarValueCollection(void);
		~ScalarValueCollection(void);

		/*******************************************************//**
		 * Converts this object to a structure.
		 *
		 * @return	null if it fails, else object converted to a structure.
		 *******************************************************/
		ScalarValueCollectionStruct * convertToStruct();

		vector<ScalarValueRealStruct*> real;
		vector<ScalarValueBooleanStruct*> boolean;

		//vector<ScalarValueIntegerStruct*> integer;
		//vector<ScalarValueEnumerationStruct*> enumeration;
		//vector<ScalarValueStringStruct*> string;

		ScalarValueRealStruct * getRealAsArray();
		ScalarValueBooleanStruct * getBooleanAsArray();

		//ScalarValueIntegerStruct * getIntegerAsArray();
		//ScalarValueEnumerationStruct * getEnumerationAsArray();
		//ScalarValueStringStruct * getStringAsArray();

	};

}
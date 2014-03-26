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
	private:

		vector<ScalarValueRealStruct*> real_;

		vector<ScalarValueBooleanStruct*> boolean_;

	public:

		vector<ScalarValueRealStruct*> getReal() const { return real_; }
		void setReal(vector<ScalarValueRealStruct*> val) { real_ = val; }

		vector<ScalarValueBooleanStruct*> getBoolean() const { return boolean_; }
		void setBoolean(vector<ScalarValueBooleanStruct*> val) { boolean_ = val; }



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
		ScalarValueCollectionStruct * toStruct();



		ScalarValueRealStruct * getRealAsArray();
		ScalarValueBooleanStruct * getBooleanAsArray();
		void toString( std::string & stringRef);
	};
}
/*******************************************************//**
 * @file	model\ScalarVariableFactory.h
 *
 * Declares the scalar variable factory class.
 *******************************************************/
#pragma once
#include "structs.h"
#include "Logger.h"
#include "Config.h"

namespace Straylight
{
	/*******************************************************//**
	 * Scalar variable factory.
	 *******************************************************/
	class ScalarVariableFactory
	{
	public:

		/*******************************************************//**
		 * Default constructor.
		 *******************************************************/
		ScalarVariableFactory();

		/*******************************************************//**
		 * Destructor.
		 *******************************************************/
		~ScalarVariableFactory(void);

		/*******************************************************//**
		 * Makes a real.
		 *
		 * @param	scalarVariable	If non-null, the scalar variable.
		 * @param	i			  	Zero-based index of the.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static ScalarVariableRealStruct* makeReal(ScalarVariable* scalarVariable, int i);

		/*******************************************************//**
		 * Makes a boolean.
		 *
		 * @param [in,out]	scalarVariable	If non-null, the scalar variable.
		 * @param	i					  	Zero-based index of the.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static ScalarVariableBooleanStruct* makeBoolean(ScalarVariable* scalarVariable, int i);

		/*******************************************************//**
		 * Makes an integer.
		 *
		 * @param [in,out]	scalarVariable	If non-null, the scalar variable.
		 * @param	i					  	Zero-based index of the.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static ScalarVariableIntegerStruct* makeInteger(ScalarVariable* scalarVariable, int i);

		/*******************************************************//**
		 * Makes an enumeration.
		 *
		 * @param [in,out]	scalarVariable	If non-null, the scalar variable.
		 * @param	i					  	Zero-based index of the.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static ScalarVariableEnumerationStruct* makeEnumeration(ScalarVariable* scalarVariable, int i);

		/*******************************************************//**
		 * Makes a string.
		 *
		 * @param [in,out]	scalarVariable	If non-null, the scalar variable.
		 * @param	i					  	Zero-based index of the.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static ScalarVariableStringStruct* makeString(ScalarVariable* scalarVariable, int i);
	};

/*******************************************************//**
 * Gets or sets the.
 *
 * @value	.
 *******************************************************/
}
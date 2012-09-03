/*******************************************************//**
 * @file	Utils.h
 *
 * Declares the utilities class.
 *******************************************************/
#pragma once

#include "stdafx.h"

using namespace std;



namespace Straylight
{
	/*******************************************************//**
	 * Utilities.
	 *******************************************************/
	class Utils
	{


	public:

		/*******************************************************//**
		 * Character to double.
		 *
		 * @param	valueChar		   	The value character.
		 * @param [in,out]	valueStatus	If non-null, the value status.
		 *
		 * @return	.
		 *******************************************************/
		static double charToDouble(const char * valueChar, ValueStatus * valueStatus);

		/*******************************************************//**
		 * Character to double.
		 *
		 * @param	valueChar	The value character.
		 *
		 * @return	.
		 *******************************************************/
		static double charToDouble(const char * valueChar);

		/*******************************************************//**
		 * Converts an x to a string.
		 *
		 * @param	x	The double to process.
		 *
		 * @return	x as a std::string.
		 *******************************************************/
		static std::string to_string(double x);

		/*******************************************************//**
		 * Double to comma string.
		 *
		 * @param [in,out]	buffer	If non-null, the buffer.
		 * @param	r			  	The double to process.
		 *******************************************************/
		static void doubleToCommaString(char* buffer, double r);

		/*******************************************************//**
		 * Int to string.
		 *
		 * @param [in,out]	buffer	If non-null, the buffer.
		 * @param	i			  	Zero-based index of the.
		 *******************************************************/
		static void intToString(char* buffer, int i);


	};
};

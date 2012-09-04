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
	 * <summary> Utilities.</summary>
	 *
	 * <remarks> Raj Dye raj@pcdigi.com, 9/3/2012.</remarks>
	 *******************************************************/
	class Utils
	{
	public:

		/*******************************************************//**
		 * <summary> Character to double.</summary>
		 *
		 * <remarks> Raj Dye raj@pcdigi.com, 9/3/2012.</remarks>
		 *
		 * <param name="valueChar">   The value character.</param>
		 * <param name="valueStatus"> If non-null, the value status.</param>
		 *
		 * <returns> .</returns>
		 *******************************************************/
		static double charToDouble(const char * valueChar, ValueStatus * valueStatus);

		/*******************************************************//**
		 * <summary> Character to double.</summary>
		 *
		 * <remarks> Raj Dye raj@pcdigi.com, 9/3/2012.</remarks>
		 *
		 * <param name="valueChar"> The value character.</param>
		 *
		 * <returns> .</returns>
		 *******************************************************/
		static double charToDouble(const char * valueChar);

		/*******************************************************//**
		 * <summary> Converts an x to a string.</summary>
		 *
		 * <remarks> Raj Dye raj@pcdigi.com, 9/3/2012.</remarks>
		 *
		 * <param name="x"> The double to process.</param>
		 *
		 * <returns> x as a std::string.</returns>
		 *******************************************************/
		static std::string to_string(double x);

		/*******************************************************//**
		 * <summary> Double to comma string.</summary>
		 *
		 * <remarks> Raj Dye raj@pcdigi.com, 9/3/2012.</remarks>
		 *
		 * <param name="buffer"> [in,out] If non-null, the buffer.</param>
		 * <param name="r">		 The double to process.</param>
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

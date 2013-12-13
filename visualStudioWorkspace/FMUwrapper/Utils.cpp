#pragma once

#include "Utils.h"




namespace Straylight
{
	/*******************************************************//**
	 * Character to double.
	 *
	 * @param	valueChar		   	The value character.
	 * @param [in,out]	valueStatus	If non-null, the value status.
	 *
	 * @return
	 *******************************************************/
	double Utils::charToDouble(const char * valueChar, ValueStatus * valueStatus) {
		double valueDouble = 0.0;

		if (!valueChar) {
			*valueStatus = valueMissing;
		} else {
			*valueStatus = (1==sscanf_s(valueChar, "%lf", &valueDouble)) ? valueDefined : valueIllegal;
		}

		return valueDouble;
	}

	/*******************************************************//**
	 * Character to double.
	 *
	 * @param	valueChar	The value character.
	 *
	 * @return	.
	 *******************************************************/
	double Utils::charToDouble(const char * valueChar) {
		ValueStatus valueStatus;
		return charToDouble(valueChar, &valueStatus);
	}

	/*******************************************************//**
	 * Converts an x to a string.
	 *
	 * @param	x	The double to process.
	 *
	 * @return	x as a std::string.
	 *******************************************************/
	std::string Utils::to_string(double x)
	{
		std::ostringstream ss;
		ss << x;
		return ss.str();
	}

	/*******************************************************//**
	 * Int to string.
	 *
	 * @param [in,out]	buffer	If non-null, the buffer.
	 * @param	i			  	Zero-based index of the.
	 *******************************************************/
	void Utils::intToString(char* buffer, int i) {
		sprintf_s(buffer, MSG_BUFFER_SIZE, "%d", i);
	}

	/*******************************************************//**
	 * Double to comma string.
	 *
	 * @param [in,out]	buffer	If non-null, the buffer.
	 * @param	r			  	The double to process.
	 *******************************************************/
	void Utils::doubleToCommaString(char* buffer, double r){
		char* comma;

		sprintf_s(buffer, MSG_BUFFER_SIZE, _T("%.16g"), r);

		comma = strchr(buffer, '.');
		if (comma) *comma = ',';
	}
}
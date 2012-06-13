#pragma once


#include "Utils.h";



namespace Straylight
{


	double Utils::charToDouble(const char * valueChar, ValueStatus * valueStatus) {

		double valueDouble = 0.0;

		if (!valueChar) { 
			*valueStatus = valueMissing;  
		} else {
			*valueStatus = (1==sscanf(valueChar, "%lf", &valueDouble)) ? valueDefined : valueIllegal;
		}

		return valueDouble;
	}



	double Utils::charToDouble(const char * valueChar) {


		ValueStatus valueStatus;
		return charToDouble(valueChar, &valueStatus);

	}




}

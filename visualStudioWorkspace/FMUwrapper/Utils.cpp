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



	std::string Utils::to_string(double x)
	{
		std::ostringstream ss;
		ss << x;
		return ss.str();
	}

	void Utils::intToString(char* buffer, int i) {

		sprintf( buffer, "%d", i );
	}

	void Utils::doubleToCommaString(char* buffer, double r){
		char* comma;
		sprintf(buffer, _T("%.16g"), r);
		comma = strchr(buffer, '.');
		if (comma) *comma = ',';
	}

}

#pragma once

#include "stdafx.h"

using namespace std;



namespace Straylight
{
	class Utils
	{


	public:
		static double charToDouble(const char * valueChar, ValueStatus * valueStatus);
		static double charToDouble(const char * valueChar);

		static std::string to_string(double x);
		static void doubleToCommaString(char* buffer, double r);

		static void intToString(char* buffer, int i);


	};
};

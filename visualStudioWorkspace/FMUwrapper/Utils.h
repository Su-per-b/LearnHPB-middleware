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

		static std::string Utils::to_string(double x);

	 };
};

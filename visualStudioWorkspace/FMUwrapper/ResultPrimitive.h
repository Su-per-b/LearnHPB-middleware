#pragma once

#include "stdafx.h"
#include <sstream>
#include <iostream>

namespace Straylight
{  
class DllExport ResultPrimitive
{
public:
	ResultPrimitive(void);
	~ResultPrimitive(void);
	std::string  getString();

public:

	double scalarReal;
	double scalarInt;
	double scalarBool;
	double scalarString;
	int type;



};


}

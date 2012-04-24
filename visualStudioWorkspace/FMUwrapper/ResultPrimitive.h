#pragma once

#include "stdafx.h"
#include <sstream>
#include <iostream>

namespace Straylight
{  
class  ResultPrimitive
{
public:
	ResultPrimitive(int idx_local);
	~ResultPrimitive(void);
	std::string  getString();

public:

	double scalarReal;
	int scalarInt;
	bool scalarBool;
	std::string scalarString;
	int type;

	int idx;

};


}

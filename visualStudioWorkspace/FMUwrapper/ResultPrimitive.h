#pragma once

#include "stdafx.h"

using namespace std;

namespace Straylight
{  
class  ResultPrimitive
{
public:
	ResultPrimitive(int idx_local);
	~ResultPrimitive(void);
	string  getString();

public:

	double scalarReal;
	int scalarInt;
	bool scalarBool;
	string scalarString;
	int type;
	int idx;

};


}

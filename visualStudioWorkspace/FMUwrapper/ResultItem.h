#pragma once

#include "stdafx.h"
#include "ResultPrimitive.h"
#include "structs.h"
#include "enums.h"

extern "C"
{
#include "xml_parser.h"
#include "fmi_cs.h"
}

using namespace std;

namespace Straylight
{
	class  ResultItem
	{

	private:
		FMU* fmuPointer_;
		fmiComponent fmiComp_;


	public:
		vector<ResultPrimitive *> resultPrimitiveList;
		double time_;


	public:
		ResultItem(FMU* fmuPointer, fmiComponent fmiComp);
		~ResultItem(void);
		void setTime(double time);
		void addValue(ScalarVariable*, int idx);
		void addModelVariables( FMU * fmuPointer, fmiComponent fmiComp);
		void init( FMU * fmuPointer, fmiComponent fmiComp);
	    char *  getString();
		ResultStruct* toStruct();

	};



}

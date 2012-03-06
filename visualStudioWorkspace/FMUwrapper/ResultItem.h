#pragma once

#include "stdafx.h"
#include <list>
#include "ResultPrimitive.h"

extern "C"
{
#include "xml_parser.h"
#include "fmi_cs.h"
}

namespace Straylight
{
	class DllExport ResultItem
	{

	private:
		FMU* fmuPointer_;

		fmiComponent fmiComp_;


	public:
		double time_;

		std::list<ResultPrimitive *> resultPrimitiveList;


	public:
		ResultItem(void);

		~ResultItem(void);

		void setTime(double time);

		void addValue(ScalarVariable*);

		void addModelVariables( FMU * fmuPointer, fmiComponent fmiComp);

	    char *  getString();

	};



}

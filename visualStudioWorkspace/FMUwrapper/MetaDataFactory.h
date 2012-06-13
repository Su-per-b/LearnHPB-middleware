#pragma once

#include "stdafx.h"

typedef struct DefaultExperimentStruct_ {
	double startTime;
	double stopTime;
	double tolerance;

} DefaultExperimentStruct;


typedef struct MetaDataStruct_ {
	DefaultExperimentStruct * defaultExperimentStruct;
	double stepDelta;
} MetaDataStruct;



using namespace std;

namespace Straylight
{
	  class MetaDataFactory
	{


	public:
		 static MetaDataStruct *  make(FMU* fmuPointer);

	 };
};

#pragma once

#include "stdafx.h"

typedef struct DefaultExperimentStruct_ {
	double startTime;
	double stopTime;
	double tolerance;

} DefaultExperimentStruct;


typedef struct {
	DefaultExperimentStruct * defaultExperimentStruct;
	double stepDelta;
} ConfigStruct;



using namespace std;

namespace Straylight
{
	  class Config
	{

	public:
		 static ConfigStruct *  make(FMU* fmuPointer);

	 };
};

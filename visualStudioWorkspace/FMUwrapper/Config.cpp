#pragma once

#include "Config.h";
#include "Utils.h";

namespace Straylight
{
	/*******************************************************//**
	 * Makes the given fmu pointer.
	 *
	 * @param	fmuPointer	If non-null, the fmu pointer.
	 *
	 * @return	null if it fails, else.
	 *******************************************************/
	ConfigStruct *  Config::make(FMU* fmuPointer) {
		ConfigStruct * configStruct = new ConfigStruct(); // = new ScalarVariableStruct();
		configStruct->defaultExperimentStruct = new DefaultExperimentStruct();
		Element* de =  fmuPointer->modelDescription->defaultExperiment;

		int len = de->n;
		for (int i=0; i<len; i+=2)  {
			const char * key = de->attributes[i];
			const char * value = de->attributes[i+1];

			double doubleValue = Utils::charToDouble(value);

			if(strcmp(key, "startTime") == 0) {
				configStruct->defaultExperimentStruct->startTime = doubleValue;
			} else if(strcmp(key, "stopTime") == 0) {
				configStruct->defaultExperimentStruct->stopTime = doubleValue;
			} else if(strcmp(key, "tolerance") == 0) {
				configStruct->defaultExperimentStruct->tolerance = doubleValue;
			}

			printf(" %s=%s", key, value);
		}

		configStruct->stepDelta = 1.0;
		return configStruct;
	}
}
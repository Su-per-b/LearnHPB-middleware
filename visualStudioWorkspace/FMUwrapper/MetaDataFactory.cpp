
#pragma once

#include "MetaDataFactory.h";
#include "Utils.h";

namespace Straylight
{

	MetaDataStruct *  MetaDataFactory::make(FMU* fmuPointer) {

		MetaDataStruct * metaDataStruct = new MetaDataStruct(); // = new ScalarVariableStruct();
		metaDataStruct->defaultExperimentStruct = new DefaultExperimentStruct();
		Element* de =  fmuPointer->modelDescription->defaultExperiment;

		int len = de->n;
		for (int i=0; i<len; i+=2)  {

			const char * key = de->attributes[i];
			const char * value = de->attributes[i+1];


			double doubleValue = Utils::charToDouble(value);


			if(strcmp(key, "startTime") == 0) {
				metaDataStruct->defaultExperimentStruct->startTime = doubleValue;
			} else if(strcmp(key, "stopTime") == 0) {
				metaDataStruct->defaultExperimentStruct->stopTime = doubleValue;
			} else if(strcmp(key, "tolerance") == 0) {
				metaDataStruct->defaultExperimentStruct->tolerance = doubleValue;
			}

			printf(" %s=%s", key, value);

		}


		metaDataStruct->stepDelta = 1.0;
		return metaDataStruct;
	}



}


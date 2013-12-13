#pragma once
#include "Config.h"


namespace Straylight
{


	bool Config::instanceFlag = false;
	Config* Config::instance_ = NULL;


	Config::Config( void )
	{
		autoCorrect_ =false;
		stepDelta_ = 1.0;
	}

	Config::~Config( void )
	{
		instanceFlag = false;
	}




	Config* Config::getInstance()
	{
		if(! instanceFlag)
		{
			instance_ = new Config();
			instanceFlag = true;
			return instance_;
		}
		else
		{
			return instance_;
		}
	}

	ConfigStruct * Config::toStruct()
	{
		ConfigStruct * configStruct = new ConfigStruct(); 
		configStruct->defaultExperimentStruct = defaultExperimentStruct_;
		configStruct->stepDelta = stepDelta_;

		return configStruct;
	}


	void Config::parseFmu( FMU* fmuPointer )
	{
		defaultExperimentStruct_ = new DefaultExperimentStruct();
		Element* defaultExperimentQtronic_ =  fmuPointer->modelDescription->defaultExperiment;

		int len = defaultExperimentQtronic_->n;
		for (int i=0; i<len; i+=2)  {
			const char * key = defaultExperimentQtronic_->attributes[i];
			const char * value = defaultExperimentQtronic_->attributes[i+1];

			double doubleValue = Utils::charToDouble(value);

			if(strcmp(key, "startTime") == 0) {
				defaultExperimentStruct_->startTime = doubleValue;
			} else if(strcmp(key, "stopTime") == 0) {
				defaultExperimentStruct_->stopTime = doubleValue;
			} else if(strcmp(key, "tolerance") == 0) {
				defaultExperimentStruct_->tolerance = doubleValue;
			}

			printf(" %s=%s\n", key, value);
		}


	}




}
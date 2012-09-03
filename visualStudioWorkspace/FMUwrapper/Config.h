/*******************************************************//**
 * @file	Config.h
 *
 * Declares the configuration class.
 *******************************************************/
#pragma once

#include "stdafx.h"

/*******************************************************//**
 * Defines an alias representing the default experiment structure.
 *******************************************************/
typedef struct DefaultExperimentStruct_ {

	/*******************************************************//**
	 * The start time.
	 *******************************************************/
	double startTime;
	double stopTime;

	/*******************************************************//**
	 * The tolerance.
	 *******************************************************/
	double tolerance;

/*******************************************************//**
 * The default experiment structure.
 *******************************************************/
} DefaultExperimentStruct;


typedef struct {

	/*******************************************************//**
	 * The default experiment structure.
	 *******************************************************/
	DefaultExperimentStruct * defaultExperimentStruct;
	double stepDelta;
} ConfigStruct;



using namespace std;

namespace Straylight
{
	/*******************************************************//**
	 * Configuration.
	 *******************************************************/
	class Config
	{

	public:
		static ConfigStruct *  make(FMU* fmuPointer);

	};
};

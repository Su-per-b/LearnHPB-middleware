/*******************************************************//**
 * @file	Config.h
 *
 * Declares the configuration class.
 *******************************************************/
#pragma once

#include "stdafx.h"

/*******************************************************//**
 * <summary> Defines an alias representing the default experiment structure.</summary>
 *
 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
 *******************************************************/
typedef struct DefaultExperimentStruct_ {

	/*******************************************************//**
	 * <summary> The start time.</summary>
	 *******************************************************/
	double startTime;

	/*******************************************************//**
	 * <summary> Time of the stop.</summary>
	 *******************************************************/
	double stopTime;

	/*******************************************************//**
	 * <summary> The tolerance.</summary>
	 *******************************************************/
	double tolerance;


} DefaultExperimentStruct;

/*******************************************************//**
 * <summary> Defines an alias representing the structure.</summary>
 *
 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
 *******************************************************/
typedef struct {

	/*******************************************************//**
	 * <summary> The default experiment structure.</summary>
	 *******************************************************/
	DefaultExperimentStruct * defaultExperimentStruct;

	/*******************************************************//**
	 * <summary> The step delta.</summary>
	 *******************************************************/
	double stepDelta;
} ConfigStruct;

using namespace std;

namespace Straylight
{
	/*******************************************************//**
	 * <summary> Configuration.</summary>
	 *
	 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
	 *******************************************************/
	class Config
	{
	public:
		static ConfigStruct *  make(FMU* fmuPointer);
	};
};

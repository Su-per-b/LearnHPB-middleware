#pragma once

#include "stdafx.h"
#include "Utils.h"

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
	class DllApi Config
	{

	private:

		Config(void);

		static bool instanceFlag;
		static Config* instance_;

		DefaultExperimentStruct * defaultExperimentStruct_;
		double stepDelta_;

		bool autoCorrect_;

		int scalarVariablePrecision_;

		int msPerTimeStep_;

	public:

		~Config(void);

		bool getAutoCorrect() const { return autoCorrect_; }
		void setAutoCorrect(bool val) { autoCorrect_ = val; }

		DefaultExperimentStruct * getDefaultExperimentStruct() const { return defaultExperimentStruct_; }
		void setDefaultExperimentStruct(DefaultExperimentStruct * val) { defaultExperimentStruct_ = val; }

		double getStepDelta() const { return stepDelta_; }
		void setStepDelta(int val) { stepDelta_ = val; }

		static Config* getInstance();

		ConfigStruct * toStruct();
        void parseFmu(FMU* fmuPointer);

	};





};

/*******************************************************//**
 * @file	JNAfunctions.h
 *
 * Declares the jn afunctions class.
 *******************************************************/
#pragma once

#include "stdafx.h"
#include "MainController.h"
#include "structs.h"
#include "enums.h"
#include "Logger.h"
#include "ResultOfStep.h"
#include "Config.h"
#include "MainDataModel.h"

using namespace Straylight;

/*******************************************************//**
 * Message callback pointer.
 *
 * @param [in,out]	parameter1	If non-null, the first parameter.
 *******************************************************/
void (*messageCallbackPtr_)(MessageStruct *);

/*******************************************************//**
 * Result callback pointer.
 *
 * @param [in,out]	parameter1	If non-null, the first parameter.
 *******************************************************/
void (*resultCallbackPtr_)(ResultOfStepStruct *);

/*******************************************************//**
 * State change callback pointer.
 *
 * @param	parameter1	The first parameter.
 *******************************************************/
void (*stateChangeCallbackPtr_)(SimStateNative );

/*******************************************************//**
 * The main controller.
 *******************************************************/
Straylight::MainController *  mainController;

/*******************************************************//**
 * Executes the message callback action.
 *
 * @param [in,out]	parameter1	If non-null, the first parameter.
 *******************************************************/
extern "C" void onMessageCallback(MessageStruct *);

/*******************************************************//**
 * Gets the force cleanup.
 *
 * @return	.
 *******************************************************/
extern "C" DllExport int forceCleanup();



/*******************************************************//**
 * Gets all scalar variables.
 *
 * @return	null if it fails, else all scalar variables.
 *******************************************************/
extern "C" DllExport ScalarVariablesAllStruct * getAllScalarVariables();


extern "C" DllExport TypeDefDataModel * getTypeDefDataModel();



/*******************************************************//**
 * Gets the is simulation complete.
 *
 * @return	.
 *******************************************************/
extern "C" DllExport int isSimulationComplete();

/*******************************************************//**
 * Gets the run.
 *
 * @return	.
 *******************************************************/
//extern "C" DllExport int run();

/*******************************************************//**
 * Gets the configuration.
 *
 * @return	null if it fails, else the configuration.
 *******************************************************/
extern "C" DllExport ConfigStruct * getConfig();

/*******************************************************//**
 * <summary> Gets scalar value results from one time step</summary>
 *
 * <returns> null if it fails, else the scalar value results.</returns>
 *******************************************************/
extern "C" DllExport ScalarValueResults * getScalarValueResults();

/*******************************************************//**
 * Sets a configuration.
 *
 * @param [in,out]	configStruct	If non-null, the configuration structure.
 *
 * @return	.
 *******************************************************/
extern "C" DllExport int setConfig(ConfigStruct * configStruct);

/*******************************************************//**
 * Connects the given message callback pointer.
 *
 * @param [in,out]	messageCallbackPtr	If non-null, the message callback pointer to connect.
 *******************************************************/
extern "C" DllExport void connect(
	void (*messageCallbackPtr)(MessageStruct *),
	void (*resultCallbackPtr)(ScalarValueResultsStruct *),
	void (*stateChangeCallbackPtr)(SimStateNative)
	);

/*******************************************************//**
 * XML parse.
 *
 * @param [in,out]	parameter1	If non-null, the first parameter.
 *******************************************************/
extern "C" DllExport void xmlParse(char *);



/*******************************************************//**
 * Request state change.
 *
 * @param	newState	State of the new.
 *******************************************************/
extern "C" DllExport void requestStateChange (SimStateNative newState);

/*******************************************************//**
 * Sets scalar value real.
 *
 * @param	idx  	The index.
 * @param	value	The value.
 *
 * @return	.
 *******************************************************/
extern "C" DllExport fmiStatus setScalarValueReal (int idx, double value);


/*******************************************************//**
 * Sets scalar values.
 *
 * @param [in,out]	scalarValueAry	If non-null, the scalar value ary.
 * @param	length				  	The length.
 *******************************************************/
extern "C" DllExport  void setScalarValues (ScalarValueRealStruct * scalarValueAry , int length);


extern "C" DllExport ScalarValueResultsStruct* getTest();





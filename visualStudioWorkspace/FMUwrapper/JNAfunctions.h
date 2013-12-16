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
extern "C" DllApi int forceCleanup();



/*******************************************************//**
 * Gets all scalar variables.
 *
 * @return	null if it fails, else all scalar variables.
 *******************************************************/
extern "C" DllApi ScalarVariablesAllStruct * getAllScalarVariables();


//extern "C" DllApi TypeDefDataModel * getTypeDefDataModel();



/*******************************************************//**
 * Gets the is simulation complete.
 *
 * @return	.
 *******************************************************/
extern "C" DllApi int isSimulationComplete();

/*******************************************************//**
 * Gets the run.
 *
 * @return	.
 *******************************************************/
//extern "C" DllApi int run();

/*******************************************************//**
 * Gets the configuration.
 *
 * @return	null if it fails, else the configuration.
 *******************************************************/
extern "C" DllApi ConfigStruct * getConfig();

/*******************************************************//**
 * <summary> Gets scalar value results from one time step</summary>
 *
 * <returns> null if it fails, else the scalar value results.</returns>
 *******************************************************/
extern "C" DllApi ScalarValueResults * getScalarValueResults();

/*******************************************************//**
 * Sets a configuration.
 *
 * @param [in,out]	configStruct	If non-null, the configuration structure.
 *
 * @return	.
 *******************************************************/
extern "C" DllApi int setConfig(ConfigStruct * configStruct);

/*******************************************************//**
 * Connects the given message callback pointer.
 *
 * @param [in,out]	messageCallbackPtr	If non-null, the message callback pointer to connect.
 *******************************************************/
extern "C" DllApi void connect(
	void (*messageCallbackPtr)(MessageStruct *),
	void (*resultCallbackPtr)(ScalarValueResultsStruct *),
	void (*stateChangeCallbackPtr)(SimStateNative)
	);

/*******************************************************//**
 * XML parse.
 *
 * @param [in,out]	parameter1	If non-null, the first parameter.
 *******************************************************/
extern "C" DllApi void xmlParse(char *);



/*******************************************************//**
 * Request state change.
 *
 * @param	newState	State of the new.
 *******************************************************/
extern "C" DllApi void requestStateChange (SimStateNative newState);

/*******************************************************//**
 * Sets scalar value real.
 *
 * @param	idx  	The index.
 * @param	value	The value.
 *
 * @return	.
 *******************************************************/
extern "C" DllApi fmiStatus setScalarValueReal (int idx, double value);


/*******************************************************//**
 * Sets scalar values.
 *
 * @param [in,out]	scalarValueAry	If non-null, the scalar value ary.
 * @param	length				  	The length.
 *******************************************************/
extern "C" DllApi  void setScalarValues (ScalarValueRealStruct * scalarValueAry , int length);


extern "C" DllApi ScalarValueResultsStruct* getTest();





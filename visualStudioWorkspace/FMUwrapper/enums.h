/*******************************************************//**
 * @file	enums.h
 *
 * Declares the enums class.
 *******************************************************/
#pragma once

#include "stdafx.h"

// Enumeration values

/*******************************************************//**
 * Defines an alias representing the enum.
 *******************************************************/
typedef enum {
	///< .
	messageType_debug,
	messageType_info,
	///< .
	messageType_error
} MessageType;

/*******************************************************//**
 * Defines an alias representing the enum.
 *******************************************************/
typedef enum {
	///< .
	fmuState_level_0_uninitialized,
	fmuState_level_1_xmlParsed,
	///< .
	fmuState_level_2_dllLoaded,
	fmuState_level_3_instantiatedSlaves,
	///< .
	fmuState_level_4_initializedSlaves,
	fmuState_level_5_initializedFMU,
	///< .
	fmuState_runningSimulation,
	fmuState_completedSimulation,
	///< .
	fmuState_cleanedup,
	fmuState_error
} SimulationStateEnum;

/*******************************************************//**
 * Defines an alias representing the enum.
 *******************************************************/
typedef enum {
	/** uninitialized. */
	simStateNative_0_uninitialized,
	
	/** connect */
	simStateNative_1_connect_requested,
	simStateNative_1_connect_completed,
	
	/** XML parse */
	simStateNative_2_xmlParse_requested,
	simStateNative_2_xmlParse_completed,
	
	/** init */
	simStateNative_3_init_requested,
	simStateNative_3_init_dllLoaded,
	simStateNative_3_init_instantiatedSlaves,
	simStateNative_3_init_initializedSlaves,
	simStateNative_3_init_completed,

	/** ready */
	simStateNative_3_ready,
	
	/** run */
	simStateNative_4_run_requested,
	simStateNative_4_run_started,
	simStateNative_4_run_completed,
	simStateNative_4_run_cleanedup,
	
	/** stop. */
	simStateNative_5_stop_requested,
	simStateNative_5_stop_completed,
	
	/** step */
	simStateNative_5_step_requested,
	simStateNative_5_step_started,
	simStateNative_5_step_completed,
	
	/** pause */
	simStateNative_6_pause_requested,
	simStateNative_6_pause_completed,
	
	/** terminate */
	simStateNative_7_terminate_requested,
	simStateNative_7_terminate_completed,
	
	/** reset */
	simStateNative_7_reset_requested,
	simStateNative_7_reset_completed,
	
	/** resume */
	simStateNative_7_resume_requested,
	simStateNative_7_resume_completed,
	
	/** error */
	simStateNative_e_error

} SimStateNative;

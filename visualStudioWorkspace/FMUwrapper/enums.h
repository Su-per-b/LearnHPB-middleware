#pragma once

#include "stdafx.h"


// Enumeration values
typedef enum {
	messageType_debug, 
	messageType_info, 
	messageType_error   
} MessageType;


typedef enum {
	fmuState_level_0_uninitialized, 
	fmuState_level_1_xmlParsed, 
	fmuState_level_2_dllLoaded,
	fmuState_level_3_instantiatedSlaves,
	fmuState_level_4_initializedSlaves,
	fmuState_level_5_initializedFMU,
	fmuState_runningSimulation,
	fmuState_completedSimulation,
	fmuState_cleanedup,
	fmuState_error

} SimulationStateEnum;

typedef enum {
	simStateNative_0_uninitialized,
	simStateNative_1_connect_requested,
	simStateNative_1_connect_completed,
	simStateNative_2_xmlParse_requested,
	simStateNative_2_xmlParse_completed,
	simStateNative_3_init_requested,
	simStateNative_3_init_dllLoaded,
	simStateNative_3_init_instantiatedSlaves,
	simStateNative_3_init_initializedSlaves,
	simStateNative_3_ready,
	simStateNative_4_run_requested,
	simStateNative_4_run_started,
	simStateNative_4_run_completed,
	simStateNative_4_run_cleanedup,
	simStateNative_5_stop_requested,
	simStateNative_5_step_requested,
	simStateNative_6_pause_requested,
	simStateNative_6_pause_completed,
	simStateNative_7_reset_requested,
	simStateNative_7_reset_completed,
	simStateNative_e_error

} SimStateNative;



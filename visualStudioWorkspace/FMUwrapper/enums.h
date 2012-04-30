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

} State;
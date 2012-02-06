/* ------------------------------------------------------------------------- 
 * main.h
 * Function types for all FMU functions and a struct to wrap a FMU dll. 
 * Copyright 2010 QTronic GmbH. All rights reserved. 
 * -------------------------------------------------------------------------
 */

#ifndef main_h
#define main_h

// Use windows.h only for Windows 
#ifdef _MSC_VER
#include <windows.h>
#define WINDOWS 1
#else
#define WINDOWS 0
#define HANDLE void *
/* See http://www.yolinux.com/TUTORIALS/LibraryArchives-StaticAndDynamic.html */
#include <sys/stat.h> // for creating dirs on Linux
#endif

#include "../include/fmiModelFunctions.h"
#include "../common/xml_parser_cosim.h"	// Zuo: parser for co-simulation


//typedef const char* (*fGetModelTypesPlatform)();	// Zuo: definition in model-ex V1.0
typedef const char* (*fGetTypesPlatform)();	// Zuo: definition in co-sim V1.0
typedef const char* (*fGetVersion)();

//typedef fmiComponent (*fInstantiateModel)(fmiString instanceName, fmiString GUID,
//                                        fmiCallbackFunctions functions, fmiBoolean loggingOn);	// Zuo: definition in model-ex V1.0
typedef fmiComponent (*fInstantiateSlave)(fmiString instanceName, fmiString GUID,					// Zuo: definition in co-sim V1.0
											fmiString fmuLocation, fmiString mimeType, fmiReal timeout,		
											fmiBoolean visible, fmiBoolean interactive,					
											fmiCallbackFunctions functions, fmiBoolean loggingOn);		

//typedef void      (*fFreeModelInstance)  (fmiComponent c);			// Zuo: definition in model-ex V1.0
typedef void      (*fFreeSlaveInstance)  (fmiComponent c);				// Zuo: definition in co-sim V1.0

typedef fmiStatus (*fSetDebugLogging)    (fmiComponent c, fmiBoolean loggingOn);
//typedef fmiStatus (*fSetTime)            (fmiComponent c, fmiReal time);						// Zuo: not in co-sim V1.0
//typedef fmiStatus (*fSetContinuousStates)(fmiComponent c, const fmiReal x[], size_t nx);		// Zuo: not in co-sim V1.0
//typedef fmiStatus (*fCompletedIntegratorStep)(fmiComponent c, fmiBoolean* callEventUpdate);	// Zuo: not in co-sim V1.0
typedef fmiStatus (*fSetReal)   (fmiComponent c, const fmiValueReference vr[], size_t nvr, const fmiReal    value[]);
typedef fmiStatus (*fSetInteger)(fmiComponent c, const fmiValueReference vr[], size_t nvr, const fmiInteger value[]);
typedef fmiStatus (*fSetBoolean)(fmiComponent c, const fmiValueReference vr[], size_t nvr, const fmiBoolean value[]);
typedef fmiStatus (*fSetString) (fmiComponent c, const fmiValueReference vr[], size_t nvr, const fmiString  value[]);

//typedef fmiStatus (*fInitialize)(fmiComponent c, fmiBoolean toleranceControlled, 
//                               fmiReal relativeTolerance, fmiEventInfo* eventInfo);	// Zuo: definition in model-ex V1.0
typedef fmiStatus (*fInitializeSlave)(fmiComponent c, fmiReal tStart,
										fmiBoolean StopTimeDefined, fmiReal tStop);		// Zuo: definition in co-sim V1.0

//typedef fmiStatus (*fGetDerivatives)    (fmiComponent c, fmiReal derivatives[]    , size_t nx);	// Zuo: not in co-sim V1.0
//typedef fmiStatus (*fGetEventIndicators)(fmiComponent c, fmiReal eventIndicators[], size_t ni);	// Zuo: not in co-sim V1.0
typedef fmiStatus (*fGetReal)   (fmiComponent c, const fmiValueReference vr[], size_t nvr, fmiReal    value[]);
typedef fmiStatus (*fGetInteger)(fmiComponent c, const fmiValueReference vr[], size_t nvr, fmiInteger value[]);
typedef fmiStatus (*fGetBoolean)(fmiComponent c, const fmiValueReference vr[], size_t nvr, fmiBoolean value[]);
typedef fmiStatus (*fGetString) (fmiComponent c, const fmiValueReference vr[], size_t nvr, fmiString  value[]);
//typedef fmiStatus (*fEventUpdate)               (fmiComponent c, fmiBoolean intermediateResults, fmiEventInfo* eventInfo);	// Zuo: not in co-sim V1.0
//typedef fmiStatus (*fGetContinuousStates)       (fmiComponent c, fmiReal states[], size_t nx);								// Zuo: not in co-sim V1.0
//typedef fmiStatus (*fGetNominalContinuousStates)(fmiComponent c, fmiReal x_nominal[], size_t nx);								// Zuo: not in co-sim V1.0
//typedef fmiStatus (*fGetStateValueReferences)   (fmiComponent c, fmiValueReference vrx[], size_t nx);							// Zuo: not in co-sim V1.0
//typedef fmiStatus (*fTerminate)                 (fmiComponent c);																// Zuo: not in co-sim V1.0
typedef fmiStatus (*fDoStep) (fmiComponent c, fmiReal currentCommunicationPoint, fmiReal communicationStepSize, fmiBoolean newStep); // Zuo: add for co-sim


typedef struct {
    ModelDescription* modelDescription;
    HINSTANCE dllHandle;
    //fGetModelTypesPlatform getModelTypesPlatform;	// Zuo: definition in model-ex V1.0
	fGetTypesPlatform getTypesPlatform;				// Zuo: definition in V1.0

    fGetVersion getVersion;

    //fInstantiateModel instantiateModel;			// Zuo: definition in model-ex V1.0
	fInstantiateSlave instantiateSlave;				// Zuo: definition in co-sim V1.0

    //fFreeModelInstance freeModelInstance;			// Zuo: definition in model-ex V1.0
	fFreeSlaveInstance freeSlaveInstance;			// Zuo: definition in cos-im V1.0

    fSetDebugLogging setDebugLogging;
    //fSetTime setTime;										// Zuo: not in co-sim V1.0
    //fSetContinuousStates setContinuousStates;				// Zuo: not in co-sim V1.0
    //fCompletedIntegratorStep completedIntegratorStep;		// Zuo: not in co-sim V1.0 
    fSetReal setReal;
    fSetInteger setInteger;
    fSetBoolean setBoolean;
    fSetString setString;
    
	//fInitialize initialize;						// Zuo: definition in model-ex V1.0
	fInitializeSlave initializeSlave;				// Zuo: definition in co-sim V1.0
    //fGetDerivatives getDerivatives;				// Zuo: not in co-sim V1.0
    //fGetEventIndicators getEventIndicators;		// Zuo: not in co-sim V1.0
    fGetReal getReal;
    fGetInteger getInteger;
    fGetBoolean getBoolean;
    fGetString getString;
    //fEventUpdate eventUpdate;									// Zuo: not in co-sim V1.0
    //fGetContinuousStates getContinuousStates;					// Zuo: not in co-sim V1.0
    //fGetNominalContinuousStates getNominalContinuousStates;	// Zuo: not in co-sim V1.0
    //fGetStateValueReferences getStateValueReferences;			// Zuo: not in co-sim V1.0
    //fTerminate terminate;										// Zuo: not in co-sim V1.0
	fDoStep doStep;									// Zuo: add for co-sim
} FMU;

#endif // main_h


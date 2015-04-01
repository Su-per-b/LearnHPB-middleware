/*******************************************************//**
 * @file	MainController.h
 *
 * Declares the main controller class.
 *******************************************************/
#pragma once

#include "stdafx.h"

#include "ResultOfStep.h"
#include "Logger.h"
#include "FMUlogger.h"
#include "structs.h"
#include "enums.h"
#include "Config.h"
#include "MainDataModel.h"
#include "TypeDefinitions.h"

using namespace std;

namespace Straylight
{
	/*******************************************************//**
	 * Main controller.
	 *******************************************************/
	class DllApi MainController
	{

	private:

				
		/*******************************************************//**
		 * Full pathname of the unzip folder file.
		 *******************************************************/
		char* unzipFolderPath_;

		/*******************************************************//**
		 * Full pathname of the XML file.
		 *******************************************************/
		const char* xmlFilePath_;

		/*******************************************************//**
		 * Full pathname of the DLL file.
		 *******************************************************/
		char* dllFilePath_;

		/*******************************************************//**
		 * <summary> the Functional MOckup Unit to simulate </summary>
		 *******************************************************/
		FMU* fmu_;  

		/*******************************************************//**
		 * The time.
		 *******************************************************/
		double time_;

		/*******************************************************//**
		 * The fmi status.
		 *******************************************************/
		fmiStatus fmiStatus_;				

		/*******************************************************//**
		 * The steps.
		 *******************************************************/
		int nSteps_;

		/*******************************************************//**
		 * The fmi component.
		 *******************************************************/
		fmiComponent fmiComponent_;               

		/*******************************************************//**
		 * The is logging enabled.
		 *******************************************************/
		fmiBoolean isLoggingEnabled_;

		/*******************************************************//**
		 * The fmu logger.
		 *******************************************************/
		FMUlogger fmuLogger_;

		/*******************************************************//**
		 * State change callback pointer.
		 *
		 * @param	parameter1	The first parameter.
		 *******************************************************/
		void (*stateChangeCallbackPtr_)(SimStateNative );

		/*******************************************************//**
		 * Result callback pointer.
		 *
		 * @param	parameter1	If non-null, the first parameter.
		 *******************************************************/
		void (*resultCallbackPtr_)(ScalarValueResultsStruct *);

        void (*resultClassCallbackPtr_)(ScalarValueResults *);


		/*******************************************************//**
		 * The state.
		 *******************************************************/
		SimStateNative state_;

		/*******************************************************//**
		 * The result of step.
		 *******************************************************/
		ResultOfStep* resultOfStep_;

		/*******************************************************//**
		 * Number of variables.
		 *******************************************************/
		int variableCount_;

		/*******************************************************//**
		 * The configuration structure.
		 *******************************************************/
		ConfigStruct * configStruct_;

		/*******************************************************//**
		 * The main data model.
		 *******************************************************/
		MainDataModel * mainDataModel_;



		/*******************************************************//**
		 * Loads dl lhelper.
		 *
		 * @param	parameter1	The first parameter.
		 * @param	parameter2	If non-null, the second parameter.
		 *
		 * @return	The dl lhelper.
		 *******************************************************/
		int loadDLLhelper(const char* , FMU *);

		/*******************************************************//**
		 * Helper method that parse.
		 *
		 * @param	parameter1	The first parameter.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		ModelDescription* parseHelper(const char*);

		/*******************************************************//**
		 * Gets XML file path.
		 *
		 * @return	null if it fails, else the XML file path.
		 *******************************************************/
		const char* getXmlFilePath();

		/*******************************************************//**
		 * Gets the address.
		 *
		 * @param	parameter1	The first parameter.
		 *
		 * @return	null if it fails, else the address.
		 *******************************************************/
		void* getAdr(const char*);

		/*******************************************************//**
		 * Extracts the variables.
		 *******************************************************/
		void extractVariables();

		/*******************************************************//**
		 * Sets a state.
		 *
		 * @param	newState	State of the new.
		 *******************************************************/
		void setState_(SimStateNative newState);

        /*******************************************************//**
         * Executes the one step operation.
         *
         * @return	0 success, 1 failure
         *******************************************************/
        int doOneStep();

		/*******************************************************//**
		 * Executes the helper do step operation.
		 *
		 * @return	0 success, 1 failure
		 *******************************************************/
		int runHelperDoStep_();

		/*******************************************************//**
		 * Gets the instantiate slave.
		 *
		 * @return	0 success, 1 failure
		 *******************************************************/
		int instantiateSlave_();

		/*******************************************************//**
		 * Initializes the slave.
		 *
		 *@return	0 success, 1 failure
		 *******************************************************/
		int initializeSlave_();


		/*******************************************************//**
		 * Terminates the slave.
		 *
		 * @return	0 success, 1 failure
		 *******************************************************/
		int  terminateSlave_();


		/*******************************************************//**
		 * Sets state error.
		 *
		 * @param	message	The message.
		 *******************************************************/
		void setStateError_(const char * message);


		ScalarValueResults * scalarValueResults_;



		/*******************************************************//**
		 * Runs this object.
		 *******************************************************/
		void run();


	public:
				

		/*******************************************************//**
		 * Default constructor.
		 *******************************************************/
		MainController(void);

		/*******************************************************//**
		 * Destructor.
		 *******************************************************/
		~MainController(void);

		/*******************************************************//**
		 * <summary> Public accessor for scalarValueResults_ member variable</summary>
		 *
		 * <returns>Pointer to ScalarValueResults object</returns>
		 *******************************************************/


		/*******************************************************//**
		 * <summary> This is the first function that must be called to start the simulamtion.</summary>
		 *
		 * <param name="messageCallbackPtr"> [in,out] If non-null, a pointer to a callback function
		 * 									 which will be called when new messages are generated.</param>
		 * <param name="resultCallbackPtr"> [in,out] If non-null, a pointer to a callback function
		 * 									 which will be called when new results are generated.</param>
		 * <param name="stateChangeCallbackPtr"> [in,out] If non-null, a pointer to a callback function
		 * 									 which will be called when the systems state changes</param>
		 *******************************************************/
		void connect( void (*messageCallbackPtr)(MessageStruct *), void (*resultCallbackPtr)(ScalarValueResultsStruct *), void (*stateChangeCallbackPtr)(SimStateNative ) );



		void setResultClassCallback( void (*resultClassCallbackPtr)(ScalarValueResults *) );


		/*******************************************************//**
		 * Request state change.
		 *
		 * @param	simStateNative	The simulation state native.
		 *******************************************************/
		void MainController::requestStateChange (SimStateNative simStateNative);

		/*******************************************************//**
		 * Gets variable description.
		 *
		 * @param	idx	The index.
		 *
		 * @return	null if it fails, else the variable description.
		 *******************************************************/
		const char * getVariableDescription(int idx);

		/*******************************************************//**
		 * Gets variable name.
		 *
		 * @param	idx	The index.
		 *
		 * @return	null if it fails, else the variable name.
		 *******************************************************/
		const char * getVariableName(int idx);

		/*******************************************************//**
		 * Gets the is simulation complete.
		 *
		 * @return	.
		 *******************************************************/
		int isSimulationComplete();


		int forceCleanup();


		/*******************************************************//**
		 * Loads the DLL.
		 *
		 * @return	The DLL.
		 *******************************************************/
		int loadDll();



		/*******************************************************//**
		 * Print summary.
		 *******************************************************/
		void printSummary();

		/*******************************************************//**
		 * Initializes this object.
		 *
		 * @return	.
		 *******************************************************/
		int init();

		/*******************************************************//**
		 * Gets module folder path.
		 *
		 * @param	szDir	If non-null, the dir.
		 *******************************************************/
		void getModuleFolderPath(_TCHAR * szDir);


		/*******************************************************//**
		 * Gets the state.
		 *
		 * @return	The state.
		 *******************************************************/
		SimStateNative getState();

		/*******************************************************//**
		 * Sets time step delta.
		 *
		 * @param	timeStepDelta	The time step delta.
		 *******************************************************/
		void setTimeStepDelta(double timeStepDelta);

		/*******************************************************//**
		 * Sets time end.
		 *
		 * @param	timeEnd	The time end.
		 *******************************************************/
		void setTimeEnd(double timeEnd);

		/*******************************************************//**
		 * Gets the configuration.
		 *
		 * @return	null if it fails, else the configuration.
		 *******************************************************/
		ConfigStruct * getConfig();

		/*******************************************************//**
		 * Sets a configuration.
		 *
		 * @param	configStruct	If non-null, the configuration structure.
		 *******************************************************/
		void setConfig(ConfigStruct * configStruct);

		/*******************************************************//**
		 * Gets stop time.
		 *
		 * @return	The stop time.
		 *******************************************************/
		double getStopTime();

		/*******************************************************//**
		 * Gets start time.
		 *
		 * @return	The start time.
		 *******************************************************/
		double getStartTime();

		/*******************************************************//**
		 * Gets step delta.
		 *
		 * @return	The step delta.
		 *******************************************************/
		double getStepDelta();

		/*******************************************************//**
		 * Gets main data model.
		 *
		 * @return	null if it fails, else the main data model.
		 *******************************************************/
		MainDataModel * getMainDataModel();



		/*******************************************************//**
		 * Stops this object.
		 *******************************************************/
		void stop();

		/*******************************************************//**
		 * Cleanups this object.
		 *******************************************************/
		void cleanup();

		/*******************************************************//**
		 * Sets scalar value real.
		 *
		 * @param	idx  	The index.
		 * @param	value	The value.
		 *
		 * @return	.
		 *******************************************************/
		fmiStatus setScalarValueReal(int idx, double value);

		/*******************************************************//**
		 * Sets scalar values.
		 *
		 * @param	scalarValueAry	If non-null, the scalar value ary.
		 * @param	length		  	The length.
		 *******************************************************/
		void setScalarValues (ScalarValueRealStruct * scalarValueAry, int length);

		fmiStatus setOneScalarValue(ScalarValueRealStruct * scalarValue);

		void setOutputVariableNames(StringMap * outputNamesStringMap);
		
		void setInputVariableNames(StringMap * inputNamesStringMap);

		ScalarValue * getOneScalarValue(int idx);

		ScalarValueRealStruct *  getOneScalarValueStruct(int idx);

		ScalarValueResults * getScalarValueResults();

		ScalarVariableRealStruct * getOneScalarVariableStruct(int idx);

		TypeDefinitions * getTypeDefinitions();


		/*******************************************************//**
		 * XML parse.
		 *
		 * @param	unzipfolder	If non-null, the unzipfolder.
		 *
		 * @return	.
		 *******************************************************/
		int xmlParse(char* unzipfolder);


	};
};

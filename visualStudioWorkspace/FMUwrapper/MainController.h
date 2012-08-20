#pragma once



#include "stdafx.h"

#include "ResultOfStep.h"
#include "Logger.h"
#include "FMUlogger.h"
#include "structs.h"
#include "enums.h"
#include "Config.h"
#include "MainDataModel.h"

using namespace std;

namespace Straylight
{


	class MainController
	{

		//private member variables
	private:
		char* unzipFolderPath_;
		char* xmlFilePath_;
		char* dllFilePath_;
		FMU* fmu_;  // the fmu to simulate
		double time_;
		fmiStatus fmiStatus_;				// Zuo: add stauus for fmi
		int nSteps_;
		fmiComponent fmiComponent_;                  // instance of the fmu 
		fmiBoolean isLoggingEnabled_;

		FMUlogger fmuLogger_;
		void (*stateChangeCallbackPtr_)(SimStateNative );
		void (*resultCallbackPtr_)(ResultOfStepStruct *);

		SimStateNative state_;

		ResultOfStep* resultOfStep_;
		int variableCount_;

		ConfigStruct * configStruct_;

		MainDataModel * mainDataModel_;
		Logger* logger_;

		// public functions
	public:

		MainController(void);
		~MainController(void);


		void connect(
			void (*messageCallbackPtr)(MessageStruct *), 
			void (*resultCallbackPtr)(ResultOfStepStruct *),
			void (*stateChangeCallbackPtr)(SimStateNative )
			);


		void MainController::requestStateChange (SimStateNative simStateNative);

		const char * getVariableDescription(int idx);
		const char * getVariableName(int idx);

		int isSimulationComplete();
		int loadDll();
		int xmlParse(char* unzipfolder);
		void printSummary();
		int init();

		void getModuleFolderPath(_TCHAR * szDir);

		ResultOfStepStruct * getResultStruct();

		SimStateNative getState();

		void setTimeStepDelta(double timeStepDelta);
		void setTimeEnd(double timeEnd);

		ConfigStruct * getConfig();



		void setConfig(ConfigStruct * configStruct);

		double getStopTime();
		double getStartTime();
		double getStepDelta();

		MainDataModel * getMainDataModel();

		void run();
		void stop();
		void cleanup();

		fmiStatus setScalarValueReal(int idx, double value);
		
		void setScalarValues (ScalarValueRealStruct * scalarValueAry, int length);


		//private functions
	private:
		int loadDLLhelper(const char* , FMU *);
		ModelDescription* parseHelper(const char*);
		char* getXmlFilePath();
		void* getAdr(const char*);
		void extractVariables();
		void setState_(SimStateNative newState);
        int doOneStep();
		int runHelperDoStep_();
		int instantiateSlave_();
		int initializeSlave_();
		int setStartValues_();
		void setStateError_(const char * message);
	};




};




#pragma once


#include "stdafx.h"

#include "ResultItem.h"
#include "Logger.h"
#include "FMUlogger.h"
#include "structs.h"
#include "enums.h"



namespace Straylight
{


	class FMUwrapper
	{

	//private member variables
	private:
		char* unzipFolderPath_;
		char* xmlFilePath_;
		char* dllFilePath_;
		double timeEnd_;
		FMU fmu_; // the fmu to simulate
		FMU* fmuPointer_;
		double time_;
		fmiStatus fmiStatus_;				// Zuo: add stauus for fmi
		int nSteps_;
		fmiReal t0;                  // start time
		fmiComponent fmiComponent_;                  // instance of the fmu 
		fmiBoolean loggingOn_;
		double timeDelta_;

		FMUlogger fmuLogger_;
		void (*stateChangeCallbackPtr_)(State );
		State state_;
		std::list<ScalarVariableStruct> metaDataList_;
		std::list<ScalarVariableStruct> metaDataListOuput_;
		ResultItem* resultItem_;
		int variableCount_;

	// public functions
	public:
		FMUwrapper(
			void (*messageCallbackPtr)(MessageStruct *), 
			void (*stateChangeCallbackPtr)(State )
			);

		~FMUwrapper(void);

		void doOneStep();

		int getVariableCount();
		const char * getVariableDescription(int idx);
		const char * getVariableName(int idx);

		int isSimulationComplete();
		int loadDll();
		int parseXML(char* unzipfolder);
		void printSummary();
		int simulateHelperInit();

		void getModuleFolderPath(_TCHAR * szDir);

		ResultStruct * getResultStruct();
		void setState(State newState);
		State getState();

		std::list<ScalarVariableStruct> getMetaDataList();
		std::list<ScalarVariableStruct> getMetaDataListOuput();
		Logger* logger_;


	//private functions
	private:
		int loadDLLhelper(const char* , FMU *);
		ModelDescription* parseHelper(const char*);
		char* getXmlFilePath();
		void* getAdr(const char*);
		int extractVariables();

	};




};




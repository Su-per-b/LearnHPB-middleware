#pragma once



#include "stdafx.h"

#include "ResultItem.h"
#include "Logger.h"
#include "FMUlogger.h"
#include "structs.h"
#include "enums.h"
#include "MetaDataFactory.h"
#include "ScalarVariableFactory.h";


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
		FMU* fmuPointer_;  // the fmu to simulate
		double time_;
		fmiStatus fmiStatus_;				// Zuo: add stauus for fmi
		int nSteps_;
		fmiComponent fmiComponent_;                  // instance of the fmu 
		fmiBoolean loggingOn_;

		FMUlogger fmuLogger_;
		void (*stateChangeCallbackPtr_)(State );
		State state_;

		vector<ScalarVariableStruct*> scalarVariables_;
		vector<ScalarVariableStruct*> scalarVariableOutput_;

		ResultItem* resultItem_;
		int variableCount_;
		MetaDataStruct * metaDataStruct_;

	// public functions
	public:
		MainController(
			void (*messageCallbackPtr)(MessageStruct *), 
			void (*stateChangeCallbackPtr)(State )
			);

		~MainController(void);

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

		void setTimeStepDelta(double timeStepDelta);
		void setTimeEnd(double timeEnd);

		MetaDataStruct * getMetaData();

		vector<ScalarVariableStruct*> getScalarVariableStructs();
		Logger* logger_;

		void setMetaData(MetaDataStruct * metaDataStruct);

		double getStopTime();
		double getStartTime();
		double getStepDelta();

		

	//private functions
	private:
		int loadDLLhelper(const char* , FMU *);
		ModelDescription* parseHelper(const char*);
		char* getXmlFilePath();
		void* getAdr(const char*);
		void extractVariables();


	};




};




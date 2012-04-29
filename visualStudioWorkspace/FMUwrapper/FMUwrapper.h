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
		fmiValueReference vru_[1], vry_[1]; // value references for two input and two output variables 
		char csv_separator_; 
		fmiReal ru1, ry, ry1;	// add real variables for input and output
		fmiInteger ix, iy;				// add integer variables for input and output
		fmiBoolean bx, by;				// add boolean variables for input and output
		fmiString sx, sy;				// Zuo: add string variables for input and output
		fmiStatus status;				// Zuo: add stauus for fmi
		int nSteps;
		fmiValueReference vr;			// add it to get value reference for variables
		fmiReal t0;                  // start time
		fmiComponent fmiComponent_;                  // instance of the fmu 
		ScalarVariable** vars;
		fmiBoolean loggingOn;
		double timeDelta_;
		ModelDescription* modelDescription_;
		Logger* logger_;
		FMUlogger fmuLogger_;

		void (*stateChangeCallbackPtr_)(State );

		State state_;


	// public functions
	public:
		FMUwrapper(
			void (*messageCallbackPtr)(MessageStruct *), 
			void (*stateChangeCallbackPtr)(State )
			);

		~FMUwrapper(void);
		void doAll(const char * fileName);
		void doOneStep();
		std::list<int> getDataList();

		ResultItem * getResultItem();
		fmiReal getResultSnapshot();
		Enu getVariableCausality(int idx);
		int getVariableCount();
		const char * getVariableDescription(int idx);
		const char * getVariableName(int idx);
		Elm getVariableType(int idx);

		int isSimulationComplete();
		int loadDll();
		int parseXML(char* unzipfolder);
		void printSummary();
		int simulateHelperInit();
		void unzip();

		std::list<ScalarVariableMeta> metaDataList;
		std::list<ScalarVariableMeta> metaDataListOuput;

		void getModuleFolderPath(_TCHAR * szDir);
		ResultItem* resultItem_;

	//	int registerMessageCallback(void (*callbackPtrArg)(MessageStruct *));
	//	int registerStateChangeCallback(void (*callbackPtrArg)(FMUstate ));


		ResultItemStruct * getResultStruct();

	//private functions
	private:
		int loadDLLhelper(const char* , FMU *);
		ModelDescription* parseHelper(const char*);
		int variableCount_;
		char* getXmlFilePath();
		void* getAdr(const char*);
		int extractVariables();
		void setState(State newState);
		State getState();
	};




};




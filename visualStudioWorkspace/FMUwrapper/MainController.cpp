// MainController.cpp : Defines the exported functions for the DLL application.
//

#include "MainController.h"

#define BUFSIZE 4096
#define XML_FILE_STR  "\\modelDescription.xml"
#define DLL_DIR_STR   "\\binaries\\win32\\"
//#define ZLIB_WINAPI


namespace Straylight
{


	/*********************************************//**
												   * Default constructor. 
												   *********************************************/
	MainController::MainController(
		void (*messageCallbackPtr)(MessageStruct *), 
		void (*stateChangeCallbackPtr)(State )
		)
	{

		fmuPointer_ = new FMU();

		logger_ = new Logger();
		FMUlogger::logger = logger_;

		logger_->registerMessageCallback(messageCallbackPtr);
		stateChangeCallbackPtr_ =stateChangeCallbackPtr;
		setState( fmuState_level_0_uninitialized );
	}



	/*********************************************//**
												   * Destructor. Frees memory and releases FMU DLL.
												   *********************************************/
	MainController::~MainController(void)
	{

		printf(_T("executing deconstructor"));


		// Cleanup
		free((void *) unzipFolderPath_);
		free((void *) xmlFilePath_);
		free((void *) dllFilePath_);

		// Release FMU
		FreeLibrary(fmuPointer_->dllHandle);
		freeElement(fmuPointer_->modelDescription);
		delete fmuPointer_;

		delete logger_;

	}

	double MainController::getStopTime() {
		return metaDataStruct_->defaultExperimentStruct->stopTime;
	}
	double MainController::getStartTime() {
		return metaDataStruct_->defaultExperimentStruct->startTime;
	}
	double MainController::getStepDelta() {
		return metaDataStruct_->stepDelta;
	}




	void MainController::setMetaData(MetaDataStruct * metaDataStruct) {
		metaDataStruct_ = metaDataStruct;
		//metaDataStruct_->stepDelta = 1.0;
	}


	MetaDataStruct * MainController::getMetaData() {
		return metaDataStruct_;
	}



	void MainController::setState(State newState) {
		if (state_ != newState) {
			state_ = newState;
			stateChangeCallbackPtr_( state_ );
		}
	}


	State MainController::getState() {
		return state_;
	}

	vector<ScalarVariableStruct*> MainController::getScalarVariableStructs() {
		return  scalarVariables_;
	}





	/*******************************************************//**
															 * Parses the xml file located in the FMU.  
															 *
															 * @param [in,out]	unzipfolder	where the FMU was unzipped to.
															 *******************************************************/
	int MainController::parseXML(char* unzipfolder) {

		//	logger_->printDebug2("MainController::parseXML unzipfolder %s\n", unzipfolder);	
		int len1 = strlen(unzipfolder) + 1;

		unzipFolderPath_ = (char *) calloc(sizeof(char), len1 + 1);
		lstrcpy(unzipFolderPath_, unzipfolder);
		logger_->printDebug2(_T("MainController::parseXML unzipFolderPath_ %s\n"), unzipFolderPath_);	

		//construct the path to the XML file and 
		// store as member variable
		int len2 = len1 + 1 + strlen(XML_FILE_STR);
		xmlFilePath_ = (char *) calloc(sizeof(char), len2 + 1);

		sprintf(xmlFilePath_, _T("%s%s"), unzipfolder, XML_FILE_STR);
		fmuPointer_->modelDescription = parse(xmlFilePath_); 
		logger_->printDebug2(_T("MainController::parseXML xmlFilePath_ %s\n"), xmlFilePath_);	


		metaDataStruct_ =  MetaDataFactory::make(fmuPointer_);
		extractVariables();

		setState( fmuState_level_1_xmlParsed );


		// Parse only parses the model description and store in structure fmu.modelDescription
		if (fmuPointer_->modelDescription) {
			return 0;
		} else {
			return 1;
		}


	}



	void MainController::extractVariables() {


		int i;
		for (i=0; fmuPointer_->modelDescription->modelVariables[i]; i++) {

			ScalarVariableStruct * scalarVariableStruct = 
				ScalarVariableFactory::make(fmuPointer_, i);

			scalarVariables_.push_back(scalarVariableStruct);

			if (scalarVariableStruct->causality == enu_output) {
				scalarVariableOutput_.push_back(scalarVariableStruct);
			}

		}

		variableCount_ = i;

	}


	/*********************************************//**
												   /* 
												   * Constructs the path to the DLL file and stores it
												   * in the dllFilePath_ member variable. Loads
												   * the DLL with the "LoadLibrary" command and populates the
												   * FMU struct.
												   *
												   * @return	0 for success
												   *********************************************/
	int MainController::loadDll( ) {

		logger_->printDebug2 (_T("MainController::loadDll unzipFolderPath_: %s\n"), unzipFolderPath_);
		const char* modelId = getModelIdentifier(fmuPointer_->modelDescription);

		dllFilePath_ = (char *) calloc(sizeof(char), strlen(unzipFolderPath_) + strlen(DLL_DIR_STR) 
			+ strlen( modelId ) +  strlen(_T(".dll")) + 1); 

		sprintf(dllFilePath_,
			_T("%s%s%s.dll"),
			unzipFolderPath_,
			DLL_DIR_STR,
			modelId
			);


		if (dllFilePath_ == NULL){
			logger_->printfError(_T("Failed to allocate memory for wText\n"), dllFilePath_);
			return 1;
		}


		logger_->printDebug2 (_T("dllFilePath_: %s \n"), dllFilePath_);


		HINSTANCE h;
		h = LoadLibrary(dllFilePath_);

		if(!h) {
			logger_->printfError(_T("Can not load %s\n"), dllFilePath_);
			exit(EXIT_FAILURE);
		}

		fmuPointer_->dllHandle = h;
		fmuPointer_->getVersion = (fGetVersion) getAdr("fmiGetVersion");
		fmuPointer_->instantiateSlave = (fInstantiateSlave) getAdr("fmiInstantiateSlave");
		fmuPointer_->freeSlaveInstance = (fFreeSlaveInstance) getAdr( "fmiFreeSlaveInstance");
		fmuPointer_->setDebugLogging = (fSetDebugLogging) getAdr( "fmiSetDebugLogging");
		fmuPointer_->setReal = (fSetReal) getAdr( "fmiSetReal");
		fmuPointer_->setInteger = (fSetInteger) getAdr( "fmiSetInteger");
		fmuPointer_->setBoolean = (fSetBoolean) getAdr( "fmiSetBoolean");
		fmuPointer_->setString = (fSetString) getAdr( "fmiSetString");
		fmuPointer_->initializeSlave = (fInitializeSlave) getAdr("fmiInitializeSlave");
		fmuPointer_->getReal = (fGetReal) getAdr( "fmiGetReal");
		fmuPointer_->getInteger = (fGetInteger) getAdr( "fmiGetInteger");
		fmuPointer_->getBoolean = (fGetBoolean) getAdr("fmiGetBoolean");
		fmuPointer_->getString = (fGetString) getAdr( "fmiGetString");
		fmuPointer_->doStep = (fDoStep) getAdr("fmiDoStep");

		setState( fmuState_level_2_dllLoaded );

		return 0;
	} 

	///////////////////////////////////////////////////////////////////////////////
	/// Get address of specific function from specific dll
	///
	///\param fmu Name of dll file.
	///\param funnam Function name .
	///\return Address of the specific function.
	//////////////////////////////////////////////////////////////////////////////
	void* MainController::getAdr(const char* funNam){
		char name[BUFSIZE];
		void* fp;
		sprintf(name, _T("%s_%s"), getModelIdentifier(fmuPointer_->modelDescription), funNam); // Zuo: adding the model name in front of function name


		fp = GetProcAddress(fmuPointer_->dllHandle, name);

		if (!fp) {
			logger_->printfError(_T("Function %s not found in dll\n"), name);        
		}
		return fp;
	}



	int MainController::simulateHelperInit() {

		logger_->printDebug(_T("MainController::simulateHelperInit\n"));	

		_TCHAR currentDirectory[MAX_PATH] =  _T("");
		_TCHAR exeDirectory[MAX_PATH] =  _T("");

		GetCurrentDirectory(MAX_PATH, currentDirectory);
		this->getModuleFolderPath(exeDirectory);

		logger_->printDebug2(_T("exeDirectory: %s\n"), exeDirectory);	

		GetCurrentDirectory(MAX_PATH, currentDirectory);
		logger_->printDebug2(_T("currentDirectory: %s\n"), currentDirectory);	

		loggingOn_ = 1;     
		const char* guid;                // global unique id of the fmu
		const char* modelId;                //
		fmiCallbackFunctions callbacks;  // called by the model during simulation
		fmiStatus fmiFlag;               // return code of the fmu functions
		fmiBoolean toleranceControlled = fmiFalse;
		nSteps_ = 0;

		// Run the simulation


		// convert double b to string s
		string s;
		{ ostringstream ss;
		ss << metaDataStruct_->defaultExperimentStruct->stopTime;
		s = ss.str();
		}

		printf("FMU Simulator: run '%s' from t=0..%g with step size h=%g, loggingOn=%d'\n", 
			unzipFolderPath_, s.c_str(), getStepDelta(), loggingOn_);

		logger_->printDebug2(_T("Instantiate the fmu%s\n"), "");

		// instantiate the fmu

		guid = getString(fmuPointer_->modelDescription, att_guid);

		logger_->printDebug2(_T("Set GUID = %s!\n"), guid);

		callbacks.logger = FMUlogger::log;
		callbacks.allocateMemory = calloc;
		callbacks.freeMemory = free;

		//logger_->printDebug("Set callbacks\n");

		modelId =  getModelIdentifier(fmuPointer_->modelDescription);
		logger_->printDebug2(_T("Model Identifer is %s\n"), modelId);


		fmiComponent_ = fmuPointer_->instantiateSlave (
			modelId, 
			guid, 
			"Model1", 
			"", 
			100, 
			fmiFalse, 
			fmiFalse, 
			callbacks, 
			loggingOn_
			);


		if (!fmiComponent_) {
			logger_->printError(_T("could not instantiate slaves\n"));
			return 1;
		}

		setState( fmuState_level_3_instantiatedSlaves );

		// Set the start time and initialize
		time_ = getStartTime();

		logger_->printDebug(_T("start to initializeSlave() \n"));
		fmiFlag =  fmuPointer_->initializeSlave(fmiComponent_, time_, fmiTrue, getStopTime());


		if (fmiFlag > fmiWarning) {
			logger_->printError(_T("could not initializeSlave()"));
			setState( fmuState_error );
			return 1;																
		} else {
			logger_->printDebug(_T("initializeSlave() successful\n"));

			setState( fmuState_level_5_initializedFMU );
			return 0;
		}
	}



	int MainController::isSimulationComplete() {
		return !(time_ < getStopTime()) ;
	}



	const char* MainController::getVariableDescription(int idx) {
		ScalarVariable* sv = (ScalarVariable*)fmuPointer_->modelDescription->modelVariables[idx];

		return getDescription(fmuPointer_->modelDescription,  sv );
	}


	int MainController::getVariableCount() {
		return variableCount_;
	}

	ResultStruct * MainController::getResultStruct() {

		return resultItem_->toStruct();
	}

	void MainController::printSummary() {

		// Print simulation summary
		if (loggingOn_) printf("Step %d to t=%.4f\n", nSteps_, time_);

		char msg1[1024];
		char msg2[1024];
		char msg3[1024];

		sprintf (msg1, "Simulation from %g to %g terminated successful\n", getStartTime(), getStopTime());
		logger_->printDebug (msg1);

		sprintf (msg2, "  steps ............ %d\n",  nSteps_);
		logger_->printDebug (msg2);

		sprintf (msg3, "  fixed step size .. %g\n",  getStepDelta());
		logger_->printDebug (msg3);

	}




	void MainController::doOneStep() {

		// Advance to next time step
		fmiStatus_ = fmuPointer_->doStep(fmiComponent_, time_, getStepDelta(), fmiTrue);  

		time_ = min(time_+getStepDelta(), getStopTime());

		resultItem_ = new ResultItem(fmuPointer_,  fmiComponent_);
		resultItem_->setTime(time_);

		vector<ScalarVariableStruct*>::iterator list_iter = scalarVariableOutput_.begin();

		for(list_iter; 
			list_iter != scalarVariableOutput_.end(); list_iter++)
		{
			ScalarVariableStruct * svm =  *list_iter;
			ScalarVariable* sv = (ScalarVariable*)fmuPointer_->modelDescription->modelVariables[svm->idx];
			if (getAlias(sv)!=enu_noAlias) continue;
			resultItem_->addValue(sv, svm->idx);
		}

		nSteps_++;

	}



	void MainController::getModuleFolderPath(_TCHAR * szDir) {

		//allocate string buffers using character independant data type.
		_TCHAR exePath[MAX_PATH] =  _T("");
		GetModuleFileName(NULL, exePath, MAX_PATH);

		char    ch = '\\';   
		_TCHAR * slashAndFileName = _tcsrchr(exePath, ch);
		size_t theSize = slashAndFileName - exePath;

		// Extract directory
		_tcsnccpy(szDir, exePath, theSize);
		szDir[theSize] = '\0';

	}

	char* MainController::getXmlFilePath()
	{
		return xmlFilePath_;
	}


}


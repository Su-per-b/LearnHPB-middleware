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
	MainController::MainController()
	{


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
		FreeLibrary(fmu_->dllHandle);
		freeElement(fmu_->modelDescription);

		delete fmu_;
		delete logger_;
	}

	
	void MainController:: connect(
		void (*messageCallbackPtr)(MessageStruct *),
		void (*resultCallbackPtr)(ResultOfStepStruct *),
		void (*stateChangeCallbackPtr)(SimStateNative )
		) {

		fmu_ = new FMU();
		logger_ = new Logger();
		logger_->registerMessageCallback(messageCallbackPtr);

		resultCallbackPtr_ = resultCallbackPtr;
		stateChangeCallbackPtr_ = stateChangeCallbackPtr;


		FMUlogger::setLogger(logger_);


		
		notifyStateChange_( simStateNative_0_uninitialized );

	}
		


	void MainController::requestStateChange (SimStateNative newState) {

		if (state_ != newState) {
			if (state_ == simStateNative_4_run_started &&
				newState == simStateNative_5_stop_requested) {
				state_ = newState;
			}
			if (state_ == simStateNative_5_stop_completed &&
				newState == simStateNative_7_resume_requested) {
				state_ = newState;
				notifyStateChange_(simStateNative_7_resume_completed);
				run();
			}
		}
	}



	void MainController::cleanup() {


		//Straylight::Logger * logger = fmuWrapper->logger_;

		//TODO: Fix this - the event should be fired *after* the object is deleted
		notifyStateChange_(simStateNative_4_run_cleanedup);

	}


	double MainController::getStopTime() {
		return configStruct_->defaultExperimentStruct->stopTime;
	}
	double MainController::getStartTime() {
		return configStruct_->defaultExperimentStruct->startTime;
	}
	double MainController::getStepDelta() {
		return configStruct_->stepDelta;
	}

	fmiStatus MainController::changeInput(int idx, double value) {

		ScalarValue * scalarValue = new ScalarValue(idx);

		fmiReal realNumber1;
		fmiReal realNumber2;
		fmiReal realNumber3;

		fmiStatus status1;
        fmiStatus status2;
        fmiStatus status3;

		realNumber1 = scalarValue->getRealNumber();
		status1 = scalarValue->getStatus();

		if (status1 != fmiOK) {
			logger_->printError(_T("MainController::changeInput - error reading initial real value: " ));
			return status1;
		} else {
			scalarValue->setRealNumber(value);
			status2 = scalarValue->getStatus();

			if (status2 != fmiOK) {
				logger_->printError(_T("MainController::changeInput - error writing real value:" ));
				return status2;
			} else {

				realNumber3 = scalarValue->getRealNumber();
				status3 = scalarValue->getStatus();
				
				if (status3 != fmiOK) {
					logger_->printError(_T("MainController::changeInput - error reading real value after written: " ));
				} 

				return status3;
			}
		}

	}



	void MainController::setConfig(ConfigStruct * configStruct) {
		configStruct_ = configStruct;
		//metaDataStruct_->stepDelta = 1.0;
	}


	ConfigStruct * MainController::getConfig() {
		return configStruct_;
	}



	void MainController::notifyStateChange_(SimStateNative newState) {
		if (state_ != newState) {
			state_ = newState;
			stateChangeCallbackPtr_( state_ );
		}
	}


	SimStateNative MainController::getState() {
		return state_;
	}





	MainDataModel *  MainController::getMainDataModel() {
		return mainDataModel_;
	}


	/*******************************************************//**
	* Parses the xml file located in the FMU.  
	*
	* @param [in,out]	unzipfolder	where the FMU was unzipped to.
	*******************************************************/
	int MainController::xmlParse(char* unzipfolder) {

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
		fmu_->modelDescription = parse(xmlFilePath_); 

		if (fmu_->modelDescription == NULL) {
			logger_->printfError(_T("Error - MainController::parseXML xmlFilePath_ %s\n"), xmlFilePath_);
			return 1;
		} else {

			logger_->printDebug2(_T("MainController::parseXML xmlFilePath_ %s\n"), xmlFilePath_);	
			configStruct_ =  Config::make(fmu_);
			mainDataModel_ = new MainDataModel();
			mainDataModel_->extractScalarVariables(fmu_->modelDescription);

			notifyStateChange_( simStateNative_2_xmlParse_completed );
			return 0;
		}







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
		const char* modelId = getModelIdentifier(fmu_->modelDescription);

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

		fmu_->dllHandle = h;
		fmu_->getVersion = (fGetVersion) getAdr("fmiGetVersion");
		fmu_->instantiateSlave = (fInstantiateSlave) getAdr("fmiInstantiateSlave");
		fmu_->freeSlaveInstance = (fFreeSlaveInstance) getAdr( "fmiFreeSlaveInstance");
		fmu_->setDebugLogging = (fSetDebugLogging) getAdr( "fmiSetDebugLogging");
		fmu_->setReal = (fSetReal) getAdr( "fmiSetReal");
		fmu_->setInteger = (fSetInteger) getAdr( "fmiSetInteger");
		fmu_->setBoolean = (fSetBoolean) getAdr( "fmiSetBoolean");
		fmu_->setString = (fSetString) getAdr( "fmiSetString");
		fmu_->initializeSlave = (fInitializeSlave) getAdr("fmiInitializeSlave");
		fmu_->getReal = (fGetReal) getAdr( "fmiGetReal");
		fmu_->getInteger = (fGetInteger) getAdr( "fmiGetInteger");
		fmu_->getBoolean = (fGetBoolean) getAdr("fmiGetBoolean");
		fmu_->getString = (fGetString) getAdr( "fmiGetString");
		fmu_->doStep = (fDoStep) getAdr("fmiDoStep");

		notifyStateChange_( simStateNative_3_init_dllLoaded );

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
		sprintf(name, _T("%s_%s"), getModelIdentifier(fmu_->modelDescription), funNam); // Zuo: adding the model name in front of function name


		fp = GetProcAddress(fmu_->dllHandle, name);

		if (!fp) {
			logger_->printfError(_T("Function %s not found in dll\n"), name);        
		}
		return fp;
	}



	int MainController::init() {

		logger_->printDebug(_T("MainController::simulateHelperInit\n"));	

		_TCHAR currentDirectory[MAX_PATH] =  _T("");
		_TCHAR currentDirectory2[MAX_PATH] =  _T("");
		_TCHAR exeDirectory[MAX_PATH] =  _T("");

		isLoggingEnabled_ = 1;     
		const char* guid;                // global unique id of the fmu
		const char* modelIdentifier;                //
		fmiCallbackFunctions callbackFunctions;  // called by the model during simulation
		fmiStatus fmiFlag;               // return code of the fmu functions
		fmiBoolean toleranceControlled = fmiFalse;
		nSteps_ = 0;
		fmiReal timeout = 100.0;
		fmiBoolean visible = false;
		fmiBoolean interactive = true;

		// Set the start time and initialize
		time_ = getStartTime();

		GetCurrentDirectory(MAX_PATH, currentDirectory);
		logger_->printDebug2(_T("currentDirectory: %s\n"), currentDirectory);

		this->getModuleFolderPath(exeDirectory);

		callbackFunctions.logger = FMUlogger::log;
		callbackFunctions.allocateMemory = calloc;
		callbackFunctions.freeMemory = free;

		guid = getString(fmu_->modelDescription, att_guid);
		modelIdentifier =  getModelIdentifier(fmu_->modelDescription);


		logger_->printDebug2(_T("exeDirectory: %s\n"), exeDirectory);	
		logger_->printDebug2(_T("unzipFolderPath_%s\n"), unzipFolderPath_);
		logger_->printDebugDouble(_T("stepDelta: %s\n"), getStepDelta());
		logger_->printDebugDouble(_T("time_: %s\n"), time_);
		logger_->printDebugDouble(_T("stopTime: %s\n"), getStopTime());


		logger_->printDebug2(_T("instanceName is %s\n"), modelIdentifier);
		logger_->printDebug2(_T("GUID = %s!\n"), guid);


		BOOL isSet = SetCurrentDirectory(unzipFolderPath_);
		int bufferLength = GetCurrentDirectory(MAX_PATH, currentDirectory2);
		logger_->printDebug2(_T("currentDirectory2: %s\n"), currentDirectory2);


		logger_->printDebug(_T("=================Instantiate Slave====================\n"));
		fmiComponent_ = fmu_->instantiateSlave (
			modelIdentifier,		//fmiString  instanceName
			guid,					//fmiString  fmuGUID
			"Model1",		//fmuLocation 
			"",						//fmiString  mimeType 
			timeout,				//fmiReal timeout
			visible,				//fmiBoolean visible 
			interactive,			//fmiBoolean interactive 
			callbackFunctions,
			isLoggingEnabled_		//
			);


		if (!fmiComponent_) {
			logger_->printError(_T("Could not instantiate slaves\n"));
			notifyStateChange_( simStateNative_e_error );
			return 1;
		} else {
			logger_->printDebug(_T("Successfully instantiated one slave\n"));
		}

		ScalarValue::setFMU(fmu_);
		ScalarValue::setFmiComponent(fmiComponent_);
		notifyStateChange_( simStateNative_3_init_instantiatedSlaves );

		logger_->printDebug(_T("=================Initialize Slave====================\n"));
		fmiFlag =  fmu_->initializeSlave(fmiComponent_, time_, fmiTrue, getStopTime());


		if (fmiFlag > fmiWarning) {
			logger_->printError(_T("could not initializeSlave()"));
			notifyStateChange_( simStateNative_e_error );
			return 1;																
		} else {
			logger_->printDebug(_T("initializeSlave() successful\n"));


			resultOfStep_ = new ResultOfStep(time_);
			vector<ScalarVariableRealStruct*> svOutput = mainDataModel_->getSVoutputVector();
			resultOfStep_->extractValues(svOutput, enu_output);

			vector<ScalarVariableRealStruct*> svInput = mainDataModel_->getSVinputVector();
			resultOfStep_->extractValues(svInput, enu_input);

			ResultOfStepStruct *  resultOfStepStruct = resultOfStep_->toStruct();
			resultCallbackPtr_(resultOfStepStruct);


			notifyStateChange_( simStateNative_3_init_completed );
			return 0;
		}



	}



	int MainController::isSimulationComplete() {
		return !(time_ < getStopTime()) ;
	}


	void MainController::run() {


		notifyStateChange_(simStateNative_4_run_started);

		// enter the simulation loop
		while (!isSimulationComplete()) {

			if (state_ == simStateNative_5_stop_requested) {
				break;
			}

			doOneStep_();
			ResultOfStepStruct *  resultOfStepStruct = getResultStruct();
			resultCallbackPtr_(resultOfStepStruct);
		}

		if (state_ == simStateNative_5_stop_requested) {
			notifyStateChange_(simStateNative_5_stop_completed);
		} else {
			printSummary();
			notifyStateChange_(simStateNative_4_run_completed);
		}


	}


	const char* MainController::getVariableDescription(int idx) {
		ScalarVariable* sv = (ScalarVariable*)fmu_->modelDescription->modelVariables[idx];

		return getDescription(fmu_->modelDescription,  sv );
	}




	ResultOfStepStruct * MainController::getResultStruct() {

		return resultOfStep_->toStruct();
	}

	void MainController::printSummary() {

		// Print simulation summary
		if (isLoggingEnabled_) printf("Step %d to t=%.4f\n", nSteps_, time_);

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



		if (!isSimulationComplete() &&
			state_ == simStateNative_3_init_completed) 
		{

			doOneStep_();
			ResultOfStepStruct *  resultOfStepStruct = getResultStruct();
			resultCallbackPtr_(resultOfStepStruct);
		}



	}


	void MainController::doOneStep_() {

		// Advance to next time step
		fmiStatus_ = fmu_->doStep(fmiComponent_, time_, getStepDelta(), fmiTrue); 

		time_ = min(time_+getStepDelta(), getStopTime());

		resultOfStep_ = new ResultOfStep(time_);


		vector<ScalarVariableRealStruct*> svOutput = mainDataModel_->getSVoutputVector();
		resultOfStep_->extractValues(svOutput, enu_output);

		vector<ScalarVariableRealStruct*> svInput = mainDataModel_->getSVinputVector();
		resultOfStep_->extractValues(svInput, enu_input);

		nSteps_++;

	}



	void MainController::getModuleFolderPath(_TCHAR * szDir) {

		//allocate string buffers using character independent data type.
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


// MainController.cpp : Defines the exported functions for the DLL application.
//

#include "MainController.h"

/*******************************************************//**
 * A macro that defines bufsize.
 *******************************************************/
#define BUFSIZE 4096

/*******************************************************//**
 * A macro that defines XML file string.
 *******************************************************/
#define XML_FILE_STR  "\\modelDescription.xml"

/*******************************************************//**
 * A macro that defines DLL dir string.
 *******************************************************/
#define DLL_DIR_STR   "\\binaries\\win32\\"

namespace Straylight
{

	MainController::MainController()
	{
		logger_ = new Logger();

	}


	MainController::~MainController(void)
	{
		printf(_T("executing deconstructor"));

		//cleanup();

		// Cleanup
		free((void *) unzipFolderPath_);
		free((void *) xmlFilePath_);
		free((void *) dllFilePath_);

		// Release FMU
		FreeLibrary(fmu_->dllHandle);
		freeElement(fmu_->modelDescription);

		delete fmu_;
		delete Logger::instance;
	}


	void MainController::connect( void (*messageCallbackPtr)(MessageStruct *), void (*resultCallbackPtr)(ScalarValueResultsStruct *), void (*stateChangeCallbackPtr)(SimStateNative ) )
	{
			fmu_ = new FMU();
			Logger::instance->registerMessageCallback(messageCallbackPtr);

			resultCallbackPtr_ = resultCallbackPtr;
			stateChangeCallbackPtr_ = stateChangeCallbackPtr;

			FMUlogger::setFMU(fmu_);

			mainDataModel_ = new MainDataModel();
			mainDataModel_->setFMU(fmu_);

			setState_( simStateNative_0_uninitialized );
	}

	/*******************************************************//**
	 * Request state change.
	 *
	 * @param	newState	State of the new.
	 *******************************************************/
	void MainController::requestStateChange (SimStateNative newState) {
		switch (newState) {
		case simStateNative_5_stop_requested :
			if (state_ == simStateNative_4_run_started) {
				state_ = newState;
			}
			break;
		case simStateNative_5_step_requested :
			if (state_ == simStateNative_3_ready) {
				state_ = simStateNative_5_step_requested;
				doOneStep();
				setState_(simStateNative_3_ready);
			}

			break;
		case simStateNative_7_reset_requested :
			if (state_ == simStateNative_4_run_completed ||
				state_ == simStateNative_3_ready
				) {
				time_ = 0;
				nSteps_ = 0;

				int result3 = initializeSlave_();

				mainDataModel_->setStartValues();
				setState_(simStateNative_7_reset_completed);

				ScalarValueResults * scalarValueResults = mainDataModel_->getScalarValueResults(time_);
				resultCallbackPtr_(scalarValueResults->toStruct());

				setState_(simStateNative_3_ready);
			}

			break;
		}
	}

	/*******************************************************//**
	 * Cleanups this object.
	 *******************************************************/
	void MainController::cleanup() {
		fmu_->freeSlaveInstance(fmiComponent_);
		time_ = 0;
		setState_(simStateNative_4_run_cleanedup);
	}

	/*******************************************************//**
	 * Gets stop time.
	 *
	 * @return	The stop time.
	 *******************************************************/
	double MainController::getStopTime() {
		return configStruct_->defaultExperimentStruct->stopTime;
	}

	/*******************************************************//**
	 * Gets start time.
	 *
	 * @return	The start time.
	 *******************************************************/
	double MainController::getStartTime() {
		return configStruct_->defaultExperimentStruct->startTime;
	}

	/*******************************************************//**
	 * Gets step delta.
	 *
	 * @return	The step delta.
	 *******************************************************/
	double MainController::getStepDelta() {
		return configStruct_->stepDelta;
	}

	/*******************************************************//**
	 * Sets scalar value real.
	 *
	 * @param	idx  	The index.
	 * @param	value	The value.
	 *
	 * @return	.
	 *******************************************************/
	fmiStatus MainController::setScalarValueReal(int idx, double value) {
		fmiStatus status = mainDataModel_->setScalarValueReal(idx, value);
		return status;
	}

	/*******************************************************//**
	 * Sets a configuration.
	 *
	 * @param	configStruct	If non-null, the configuration structure.
	 *******************************************************/
	void MainController::setConfig(ConfigStruct * configStruct) {
		configStruct_ = configStruct;
		//metaDataStruct_->stepDelta = 1.0;
	}

	/*******************************************************//**
	 * Gets the configuration.
	 *
	 * @return	null if it fails, else the configuration.
	 *******************************************************/
	ConfigStruct * MainController::getConfig() {
		return configStruct_;
	}

	/*******************************************************//**
	 * Sets a state.
	 *
	 * @param	newState	State of the new.
	 *******************************************************/
	void MainController::setState_(SimStateNative newState) {
		if (state_ != newState) {
			state_ = newState;
			stateChangeCallbackPtr_( state_ );
		}
	}

	/*******************************************************//**
	 * Sets state error.
	 *
	 * @param	message	The message.
	 *******************************************************/
	void MainController::setStateError_(const char * message) {
		Logger::instance->printError(message);
		state_ = simStateNative_e_error;

		stateChangeCallbackPtr_( state_ );
	}

	/*******************************************************//**
	 * Gets the state.
	 *
	 * @return	The state.
	 *******************************************************/
	SimStateNative MainController::getState() {
		return state_;
	}

	/*******************************************************//**
	 * Gets main data model.
	 *
	 * @return	null if it fails, else the main data model.
	 *******************************************************/
	MainDataModel *  MainController::getMainDataModel() {
		return mainDataModel_;
	}

	/*******************************************************//**
	 * Parses the xml file located in the FMU.
	 *
	 * @param	unzipfolder	where the FMU was unzipped to.
	 *
	 * @return	.
	 *******************************************************/
	int MainController::xmlParse(char* unzipfolder) {
		//	Logger::instance->printDebug2("MainController::parseXML unzipfolder %s\n", unzipfolder);
		int len1 = strlen(unzipfolder) + 1;

		unzipFolderPath_ = (char *) calloc(sizeof(char), len1 + 1);
		lstrcpy(unzipFolderPath_, unzipfolder);
		Logger::instance->printDebug2(_T("MainController::parseXML unzipFolderPath_ %s\n"), unzipFolderPath_);

		//construct the path to the XML file and
		// store as member variable
		int len2 = len1 + 1 + strlen(XML_FILE_STR);
		xmlFilePath_ = (char *) calloc(sizeof(char), len2 + 1);

		sprintf(xmlFilePath_, _T("%s%s"), unzipfolder, XML_FILE_STR);
		fmu_->modelDescription = parse(xmlFilePath_);

		if (fmu_->modelDescription == NULL) {
			Logger::instance->printfError(_T("Error - MainController::parseXML xmlFilePath_ %s\n"), xmlFilePath_);
			return 1;
		} else {
			Logger::instance->printDebug2(_T("MainController::parseXML xmlFilePath_ %s\n"), xmlFilePath_);
			configStruct_ =  Config::make(fmu_);

			mainDataModel_->extract();

			setState_( simStateNative_2_xmlParse_completed );
			return 0;
		}
	}

	/*******************************************************//**
	 * Gets the address.
	 *
	 * @param	funNam	The fun nam.
	 *
	 * @return	null if it fails, else the address.
	 *******************************************************/
	void* MainController::getAdr(const char* funNam){
		char name[BUFSIZE];
		void* fp;
		sprintf(name, _T("%s_%s"), getModelIdentifier(fmu_->modelDescription), funNam); // Zuo: adding the model name in front of function name

		fp = GetProcAddress(fmu_->dllHandle, name);

		if (!fp) {
			Logger::instance->printfError(_T("Function %s not found in dll\n"), name);
		}
		return fp;
	}

	/*******************************************************//**
	 * Initialises this object.
	 *
	 * @return	.
	 *******************************************************/
	int MainController::init() {
		Logger::instance->printDebug(_T("-=MainController::init=-\n"));

		int result1 = loadDll();
		if (result1) return result1;

		int result2 = instantiateSlave_();
		if (result2) return result2;

		int result3 = initializeSlave_();
		if (result3) return result3;

		int result4 = setStartValues_();
		return result4;
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

	/*******************************************************//**
	 * Loads the DLL.
	 *
	 * @return	The DLL.
	 *******************************************************/
	int MainController::loadDll( ) {
		Logger::instance->printDebug2 (_T("MainController::loadDll unzipFolderPath_: %s\n"), unzipFolderPath_);
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
			Logger::instance->printfError(_T("Failed to allocate memory for wText\n"), dllFilePath_);
			return 1;
		}

		Logger::instance->printDebug2 (_T("dllFilePath_: %s \n"), dllFilePath_);

		HINSTANCE h;
		h = LoadLibrary(dllFilePath_);

		if(!h) {
			Logger::instance->printfError(_T("Can not load %s\n"), dllFilePath_);
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
		fmu_->terminateSlave = (fTerminateSlave) getAdr("fmiTerminateSlave");
		fmu_->resetSlave = (fResetSlave) getAdr("fmiResetSlave");

		setState_( simStateNative_3_init_dllLoaded );

		return 0;
	}

	/*******************************************************//**
	 * Gets the instantiate slave.
	 *
	 * @return	.
	 *******************************************************/
	int MainController::instantiateSlave_() {
		Logger::instance->printDebug(_T("-=MainController::instantiateSlave_=-\n"));

		Logger::instance->printDebug(_T("MainController::simulateHelperInit\n"));

		_TCHAR currentDirectory[MAX_PATH] =  _T("");
		_TCHAR currentDirectory2[MAX_PATH] =  _T("");
		_TCHAR exeDirectory[MAX_PATH] =  _T("");

		isLoggingEnabled_ = 1;
		const char* guid;                // global unique id of the fmu
		const char* modelIdentifier;                //
		fmiCallbackFunctions callbackFunctions;  // called by the model during simulation
		fmiBoolean toleranceControlled = fmiFalse;
		nSteps_ = 0;
		fmiReal timeout = 100.0;
		fmiBoolean visible = false;
		fmiBoolean interactive = true;

		// Set the start time and initialize
		time_ = getStartTime();

		GetCurrentDirectory(MAX_PATH, currentDirectory);
		Logger::instance->printDebug2(_T("currentDirectory: %s\n"), currentDirectory);

		this->getModuleFolderPath(exeDirectory);

		callbackFunctions.logger = FMUlogger::log;
		callbackFunctions.allocateMemory = calloc;
		callbackFunctions.freeMemory = free;

		guid = getString(fmu_->modelDescription, att_guid);
		modelIdentifier =  getModelIdentifier(fmu_->modelDescription);

		Logger::instance->printDebug2(_T("exeDirectory: %s\n"), exeDirectory);
		Logger::instance->printDebug2(_T("unzipFolderPath_%s\n"), unzipFolderPath_);

		Logger::instance->printDebugDouble(_T("stepDelta: %s\n"), getStepDelta());
		Logger::instance->printDebugDouble(_T("time_: %s\n"), time_);
		Logger::instance->printDebugDouble(_T("stopTime: %s\n"), getStopTime());

		Logger::instance->printDebug2(_T("instanceName is %s\n"), modelIdentifier);
		Logger::instance->printDebug2(_T("GUID = %s!\n"), guid);

		BOOL isSet = SetCurrentDirectory(unzipFolderPath_);
		int bufferLength = GetCurrentDirectory(MAX_PATH, currentDirectory2);
		Logger::instance->printDebug2(_T("currentDirectory2: %s\n"), currentDirectory2);

		fmiComponent_ = fmu_->instantiateSlave (
			modelIdentifier,		//fmiString  instanceName
			guid,					//fmiString  fmuGUID
			"Model1",				//fmuLocation
			"",						//fmiString  mimeType
			timeout,				//fmiReal timeout
			visible,				//fmiBoolean visible
			interactive,			//fmiBoolean interactive
			callbackFunctions,
			isLoggingEnabled_		//
			);

		if (!fmiComponent_) {
			setStateError_(_T("Could not instantiate slaves\n"));
			return 1;
		} else {
			Logger::instance->printDebug(_T("Successfully instantiated one slave\n"));
			mainDataModel_->setFmiComponent(fmiComponent_);
			setState_( simStateNative_3_init_instantiatedSlaves );

			return 0;
		}
	}

	/*******************************************************//**
	 * Initializes the slave.
	 *
	 * @return	.
	 *******************************************************/
	int MainController::initializeSlave_() {
		Logger::instance->printDebug(_T("-=MainController::initializeSlave_=-\n"));

		fmiStatus fmiFlag;

		setState_( simStateNative_3_init_instantiatedSlaves );
		double stopTime = getStopTime();
		fmiFlag =  fmu_->initializeSlave(fmiComponent_, time_, fmiTrue, stopTime);

		if (fmiFlag > fmiWarning) {
			setStateError_(_T("Could not initialize slaves\n"));
			return 1;
		} else {
			Logger::instance->printDebug(_T("initializeSlave() successful\n"));
			return 0;
		}
	}

	/*******************************************************//**
	 * Sets start values.
	 *
	 * @return	.
	 *******************************************************/
	
	int MainController::setStartValues_() {
		mainDataModel_->setStartValues();

	//	resultOfStep_ = mainDataModel_->getResultOfStep(time_);

		scalarValueResults_ = mainDataModel_->getScalarValueResults(time_);

		ScalarValueResultsStruct *  scalarValueResultsStruct = scalarValueResults_->toStruct();
		resultCallbackPtr_(scalarValueResultsStruct);

		setState_( simStateNative_3_ready );

		return 0;
	}






	/*******************************************************//**
	 * Gets the is simulation complete.
	 *
	 * @return	.
	 *******************************************************/
	int MainController::isSimulationComplete() {
		return !(time_ < getStopTime()) ;
	}

	/*******************************************************//**
	 * Runs this object.
	 *******************************************************/
	void MainController::run() {
		setState_(simStateNative_4_run_started);

		// enter the simulation loop
		while (!isSimulationComplete()) {
			if (state_ == simStateNative_5_stop_requested) {
				break;
			}

			runHelperDoStep_();

			//ScalarValueResultsStruct *  scalarValueResultsStruct = getResultStruct();
			resultCallbackPtr_(scalarValueResults_->toStruct());
		}

		if (state_ == simStateNative_5_stop_requested) {
			setState_(simStateNative_3_ready);
		} else {
			printSummary();
			setState_(simStateNative_4_run_completed);
			//cleanup();
		}
	}

	/*******************************************************//**
	 * Gets variable description.
	 *
	 * @param	idx	The index.
	 *
	 * @return	null if it fails, else the variable description.
	 *******************************************************/
	const char* MainController::getVariableDescription(int idx) {
		ScalarVariable* sv = (ScalarVariable*)fmu_->modelDescription->modelVariables[idx];

		return getDescription(fmu_->modelDescription,  sv );
	}


	/*******************************************************//**
	 * Print summary.
	 *******************************************************/
	void MainController::printSummary() {
		// Print simulation summary
		if (isLoggingEnabled_) printf("Step %d to t=%.4f\n", nSteps_, time_);

		char msg1[1024];
		char msg2[1024];
		char msg3[1024];

		sprintf (msg1, "Simulation from %g to %g terminated successful\n", getStartTime(), getStopTime());
		Logger::instance->printDebug (msg1);

		sprintf (msg2, "  steps ............ %d\n",  nSteps_);
		Logger::instance->printDebug (msg2);

		sprintf (msg3, "  fixed step size .. %g\n",  getStepDelta());
		Logger::instance->printDebug (msg3);
	}

	/*******************************************************//**
	 * Executes the helper do step operation.
	 *
	 * @return	.
	 *******************************************************/
	int MainController::runHelperDoStep_() {
		// Advance to next time step
		fmiStatus_ = fmu_->doStep(fmiComponent_, time_, getStepDelta(), fmiTrue);

		if(fmiStatus_ != fmiOK) {
			Logger::instance->printError("MainController::doOneStep_ resulted in error");
			return 1;
		}

		time_ = min(time_+getStepDelta(), getStopTime());

	//	resultOfStep_ = mainDataModel_->getResultOfStep(time_);
		scalarValueResults_ = mainDataModel_->getScalarValueResults(time_);

		

		nSteps_++;
		return 0;
	}



	/*******************************************************//**
	 * Executes the one step operation.
	 *
	 * @return	.
	 *******************************************************/
	int MainController::doOneStep() {
		if(isSimulationComplete()) {
			setStateError_("MainController::doOneStep cannot execute simulation is complete");
			return 1;
		}

		if(!(state_ == simStateNative_3_ready|| state_ ==  simStateNative_5_step_requested)) {
			setStateError_("MainController::doOneStep cannot execute simulation is in the wrong state");
			return 1;
		}

		int result = runHelperDoStep_();

		if(result) {
			return 1;
		} else {
			resultCallbackPtr_(scalarValueResults_->toStruct());
			return 0;
		}
	}

	/*******************************************************//**
	 * Gets module folder path.
	 *
	 * @param [in,out]	szDir	If non-null, the dir.
	 *******************************************************/
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

	/*******************************************************//**
	 * Gets XML file path.
	 *
	 * @return	null if it fails, else the XML file path.
	 *******************************************************/
	char* MainController::getXmlFilePath()
	{
		return xmlFilePath_;
	}

	/*******************************************************//**
	 * Sets scalar values.
	 *
	 * @param [in,out]	scalarValueAry	If non-null, the scalar value ary.
	 * @param	length				  	The length.
	 *******************************************************/
	void MainController::setScalarValues (ScalarValueRealStruct * scalarValueAry, int length) {
		if (state_ != simStateNative_4_run_cleanedup ) {
			mainDataModel_->setScalarValues(scalarValueAry, length);
		}
	}

	ScalarValueResultsStruct * MainController::getTest()
	{

		ScalarValueResults * scalarValueResults = mainDataModel_->getScalarValueResults(time_);
		ScalarValueResultsStruct * res = scalarValueResults->toStruct();

		return res;
	}

}
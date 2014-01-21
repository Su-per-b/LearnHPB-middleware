// MainController.cpp : Defines the exported functions for the DLL application.
//

#include "MainController.h"

/*******************************************************//**
 * A macro that defines bufsize.
 *******************************************************/


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
		//logger_ = new Logger();
		resultCallbackPtr_ = NULL;
		unzipFolderPath_ = NULL;
		xmlFilePath_ = NULL;
		dllFilePath_ = NULL;
		configStruct_ = NULL;
		fmu_= NULL;
		fmiComponent_ = NULL;
		mainDataModel_ = NULL;
		state_ = simStateNative_0_uninitialized;
		stateChangeCallbackPtr_ = NULL;
		scalarValueResults_ = NULL;
		resultOfStep_ = NULL;

	}


	MainController::~MainController(void)
	{
		printf(_T("executing deconstructor"));


		// Cleanup
		if(NULL != unzipFolderPath_) {
			free((void *) unzipFolderPath_);
		}

		if(NULL != dllFilePath_) {
			free((void *) dllFilePath_);
		}

		if(NULL != configStruct_ ) {
			delete configStruct_;
		}

		if (NULL != scalarValueResults_) {
			delete scalarValueResults_;
		}

		if (NULL != resultOfStep_) {
			delete resultOfStep_;
		}

		

		// Release FMU
		if(NULL != fmu_) {

			if (NULL != fmiComponent_) {
				fmu_->terminateSlave(fmiComponent_);
				fmu_->freeSlaveInstance(fmiComponent_);
			}


			if (NULL !=  fmu_->modelDescription) {
				free((void *) fmu_->modelDescription);
			}


			if (NULL !=  fmu_->dllHandle) {
				FreeLibrary (fmu_->dllHandle);
			}


			delete fmu_;
		}


		if(NULL != mainDataModel_) {
			delete mainDataModel_;
		}
		

		delete Logger::getInstance();

		printf(_T("deconstructor done"));
	}


	void MainController::connect( void (*messageCallbackPtr)(MessageStruct *), void (*resultCallbackPtr)(ScalarValueResultsStruct *), void (*stateChangeCallbackPtr)(SimStateNative ) )
	{
			fmu_ = new FMU();
			fmu_->dllHandle =NULL;
			fmu_->modelDescription =NULL;

			Logger::getInstance()->registerMessageCallback(messageCallbackPtr);
			Logger::getInstance()->setDebugvs(1);

			resultCallbackPtr_ = resultCallbackPtr;
			stateChangeCallbackPtr_ = stateChangeCallbackPtr;

			FMUlogger::setFMU(fmu_);

			mainDataModel_ = new MainDataModel();
			mainDataModel_->setFMU(fmu_);


			setState_( simStateNative_1_connect_completed );
	}



	/*******************************************************//**
	 * Request state change.
	 *
	 * @param	newState	State of the new.
	 *******************************************************/
	void MainController::requestStateChange (SimStateNative requestedState) {
		
		switch (requestedState) {


		case simStateNative_3_init_requested :
			if (state_ == simStateNative_2_xmlParse_completed) {
				int result = init();

				if (0 == result) { 
					setState_(simStateNative_3_ready);
				} else {
					setStateError_(_T("Could not init after XML parse\n"));
				}

			} else if (state_ == simStateNative_7_terminate_completed) {

				mainDataModel_->setStartValues();
				int result = initializeSlave_();

				if (0 == result) { 
					setState_(simStateNative_3_ready);
				} else {
					setStateError_(_T("Could not init after terminate\n"));
				}
			}


			break;
		case simStateNative_4_run_requested :

			if (state_ == simStateNative_3_ready) {
				this->run();
			}
			break;

		case simStateNative_5_stop_requested :
			if (state_ == simStateNative_4_run_started) {
				setState_(simStateNative_5_stop_requested);
			}
			break;

		case simStateNative_5_step_requested :
			if (state_ == simStateNative_3_ready) {

				setState_(simStateNative_5_step_started);
				
				int result = doOneStep();

				if (0 == result) { 
					//setState_(simStateNative_5_step_completed);
					setState_(simStateNative_3_ready);
				} else {
					setStateError_(_T("Could not step\n"));
				}
			} else  {

				Logger::getInstance()->printError(_T("Could not step\n"));
			}



			break;

		case simStateNative_7_terminate_requested :

			if (state_ == simStateNative_4_run_completed ||
				state_ == simStateNative_3_ready
				) {

				setState_(simStateNative_7_terminate_requested);
				int result = terminateSlave_();

				if (0 == result) { 
					setState_(simStateNative_7_terminate_completed);
				} else {
					setStateError_(_T("Could not terminate\n"));
				}
			}
			break;


	}
	}



	int MainController::forceCleanup() {

		if (state_ == simStateNative_1_connect_requested ||
			simStateNative_1_connect_completed ||
			simStateNative_2_xmlParse_requested ||
			simStateNative_2_xmlParse_completed
			) 
		{

			setState_(simStateNative_8_tearDown_completed);
			return 0;
		} else if (state_ == simStateNative_3_ready) {


			setState_(simStateNative_8_tearDown_completed);
			return 0;
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
			stateChangeCallbackPtr_( newState );
		}
	}

	/*******************************************************//**
	 * Sets state error.
	 *
	 * @param	message	The message.
	 *******************************************************/
	void MainController::setStateError_(const char * message) {
		Logger::getInstance()->printError(message);
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

		unzipFolderPath_ = (char *) calloc(sizeof(char), len1 );

		lstrcpy(unzipFolderPath_, unzipfolder);
		Logger::getInstance()->printDebug2(_T("MainController::xmlParse unzipFolderPath_ %s\n"), unzipFolderPath_);

		//construct the path to the XML file and
		// store as member variable
		int len2 = len1 + strlen(XML_FILE_STR);
		xmlFilePath_ = (char *) calloc(sizeof(char), len2);


		std::stringstream xmlFilePathStringStream;
		xmlFilePathStringStream << unzipfolder << XML_FILE_STR;
		string theStr = xmlFilePathStringStream.str();

		xmlFilePath_ = theStr.c_str();

		Logger::getInstance()->printDebug2(_T("xmlFilePath_ = %s\n"), xmlFilePath_);


		fmu_->modelDescription = parse(xmlFilePath_);

		if (fmu_->modelDescription == NULL) {
			Logger::getInstance()->printfError(_T("Error - MainController::xmlParse xmlFilePath_ %s\n"), xmlFilePath_);
			return 1;
		} else {

			Config * config = Config::getInstance();
			config->parseFmu(fmu_);
			configStruct_ =  config->toStruct();

			mainDataModel_->extract();

			Logger::getInstance()->printDebug(_T("MainController::xmlParse xmlFilePath_ complete \n"));

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
		char name[MSG_BUFFER_SIZE];
		void* fp;
		sprintf_s(name, MSG_BUFFER_SIZE, _T("%s_%s"), getModelIdentifier(fmu_->modelDescription), funNam); // Zuo: adding the model name in front of function name

		fp = GetProcAddress(fmu_->dllHandle, name);

		if (!fp) {
			Logger::getInstance()->printfError(_T("Function %s not found in dll\n"), name);
		}
		return fp;
	}

	/*******************************************************//**
	 * Initializes this object.
	 *
	 * @return	.
	 *******************************************************/
	int MainController::init() {
		Logger::getInstance()->printDebug(_T("-=MainController::init=-\n"));

		int result1 = loadDll();
		if (result1) return result1;

		int result2 = instantiateSlave_();
		if (result2) return result2;

		mainDataModel_->setStartValues();
		int result3 = initializeSlave_();

		return result3;
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
		Logger::getInstance()->printDebug2 (_T("MainController::loadDll unzipFolderPath_: %s\n"), unzipFolderPath_);
		const char* modelId = getModelIdentifier(fmu_->modelDescription);
		
		int len = strlen(unzipFolderPath_) + strlen(DLL_DIR_STR)
			+ strlen(modelId) + strlen(_T(".dll")) + 1;

		dllFilePath_ = (char *)calloc(sizeof(char), len);


		sprintf_s(dllFilePath_,
			len,
			_T("%s%s%s.dll"),
			unzipFolderPath_,
			DLL_DIR_STR,
			modelId
			);

		if (dllFilePath_ == NULL){
			Logger::getInstance()->printfError(_T("Failed to allocate memory for wText\n"), dllFilePath_);
			return 1;
		}

		Logger::getInstance()->printDebug2 (_T("dllFilePath_: %s \n"), dllFilePath_);

		HINSTANCE h;
		h = LoadLibrary(dllFilePath_);

		if(!h) {
			Logger::getInstance()->printfError(_T("Can not load %s\n"), dllFilePath_);
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
		
		//resetSlave does not seem to be implented 
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

		Logger::getInstance()->printDebug(_T("-=MainController::instantiateSlave_=-\n"));
		Logger::getInstance()->printDebug(_T("MainController::simulateHelperInit\n"));

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
		Logger::getInstance()->printDebug2(_T("currentDirectory: %s\n"), currentDirectory);

		this->getModuleFolderPath(exeDirectory);

		callbackFunctions.logger = FMUlogger::log;
		callbackFunctions.allocateMemory = calloc;
		callbackFunctions.freeMemory = free;

		guid = getString(fmu_->modelDescription, att_guid);
		modelIdentifier =  getModelIdentifier(fmu_->modelDescription);

		Logger::getInstance()->printDebug2(_T("exeDirectory: %s\n"), exeDirectory);
		Logger::getInstance()->printDebug2(_T("unzipFolderPath_%s\n"), unzipFolderPath_);

		Logger::getInstance()->printDebugDouble(_T("stepDelta: %s\n"), getStepDelta());
		Logger::getInstance()->printDebugDouble(_T("time_: %s\n"), time_);
		Logger::getInstance()->printDebugDouble(_T("stopTime: %s\n"), getStopTime());

		Logger::getInstance()->printDebug2(_T("instanceName is %s\n"), modelIdentifier);
		Logger::getInstance()->printDebug2(_T("GUID = %s!\n"), guid);

		BOOL isSet = SetCurrentDirectory(unzipFolderPath_);
		int bufferLength = GetCurrentDirectory(MAX_PATH, currentDirectory2);
		Logger::getInstance()->printDebug2(_T("currentDirectory2: %s\n"), currentDirectory2);

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
			Logger::getInstance()->printDebug(_T("Successfully instantiated one slave\n"));
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
		
		Logger::getInstance()->printDebug(_T("-=MainController::initializeSlave_=-\n"));

		time_ = getStartTime();
		double stopTime = getStopTime();

		Logger::getInstance()->printDebug(_T("-=MainController::initializeSlave_=-B\n"));

		fmiStatus status =  fmu_->initializeSlave(fmiComponent_, time_, fmiTrue, stopTime);

		Logger::getInstance()->printDebug(_T("-=MainController::initializeSlave_=-C\n"));

		if (status > fmiWarning) {
			setStateError_(_T("Could not initialize slaves\n"));
			return 1;
		} else {
			Logger::getInstance()->printDebug(_T("initializeSlave() successful\n"));
			setState_( simStateNative_3_init_initializedSlaves );


			return 0;
		}
	}



	int MainController::terminateSlave_() {
		Logger::getInstance()->printDebug(_T("-=MainController::terminateSlave_()=-\n"));

		fmiStatus fmiFlag;

		fmiFlag =  fmu_->terminateSlave(fmiComponent_);

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
		}


		if (state_ == simStateNative_5_stop_requested) {
			setState_(simStateNative_3_ready);
		} else {
			printSummary();
			setState_(simStateNative_4_run_completed);
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

		char msg1[MSG_BUFFER_SIZE_SMALL];
		char msg2[MSG_BUFFER_SIZE_SMALL];
		char msg3[MSG_BUFFER_SIZE_SMALL];

		sprintf_s(msg1, MSG_BUFFER_SIZE_SMALL,"Simulation from %g to %g terminated successful\n", getStartTime(), getStopTime());
		Logger::getInstance()->printDebug (msg1);

		sprintf_s(msg2, MSG_BUFFER_SIZE_SMALL, "  steps ............ %d\n", nSteps_);
		Logger::getInstance()->printDebug (msg2);

		sprintf_s(msg3, MSG_BUFFER_SIZE_SMALL, "  fixed step size .. %g\n", getStepDelta());
		Logger::getInstance()->printDebug (msg3);
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
			Logger::getInstance()->printError("MainController::doOneStep_ resulted in error");
			return 1;
		}
		scalarValueResults_ = mainDataModel_->getScalarValueResults(time_);


		if (scalarValueResults_ == NULL) {
			return 1;
		}

		if (resultCallbackPtr_ != NULL) {
			resultCallbackPtr_(scalarValueResults_->toStruct());
		}

		nSteps_++;
		time_ = min(time_+getStepDelta(), getStopTime());

		return 0;
	}

	ScalarValueResults * MainController::getScalarValueResults() {

		return scalarValueResults_;

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

		if(!(state_ == simStateNative_3_ready|| state_ ==  simStateNative_5_step_started)) {
			setStateError_("MainController::doOneStep cannot execute simulation is in the wrong state");
			return 1;
		}

		int result = runHelperDoStep_();
		return result;


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
		_tcsnccpy_s(szDir, MAX_PATH, exePath, theSize);
		szDir[theSize] = '\0';
	}

	/*******************************************************//**
	 * Gets XML file path.
	 *
	 * @return	null if it fails, else the XML file path.
	 *******************************************************/
	const char* MainController::getXmlFilePath()
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



}
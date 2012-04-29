// FMUwrapper.cpp : Defines the exported functions for the DLL application.
//
#include "stdafx.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include "FMUwrapper.h"


#define BUFSIZE 4096
#define XML_FILE_STR  "\\modelDescription.xml"
#define DLL_DIR_STR   "\\binaries\\win32\\"
#define ZLIB_WINAPI



namespace Straylight
{


	/*********************************************//**
	* Default constructor. 
	*********************************************/
	FMUwrapper::FMUwrapper(void)
	{
		timeEnd_ = 1.0;
		csv_separator_ = ',';
		t0 = 0;
		timeDelta_=0.1;
		fmuPointer_ = &fmu_;

		logger_ = new Logger();
		FMUlogger::fmu = fmu_;
		FMUlogger::logger = logger_;
	}


	/*********************************************//**
	* Destructor. Frees memory and releases FMU DLL.
	*********************************************/
	FMUwrapper::~FMUwrapper(void)
	{

		// Cleanup
		free((void *) unzipFolderPath_);
		free((void *) xmlFilePath_);
		free((void *) dllFilePath_);

		// Release FMU
		FreeLibrary(fmu_.dllHandle);
		freeElement(fmu_.modelDescription);

	}



	int FMUwrapper::registerMessageCallback(void (*callbackPtrArg)(MessageStruct *))
	{

		if (callbackPtrArg != NULL) {
			//callbackPtrArg(_T("FMUwrapper::registerCallback"));

			logger_->registerMessageCallback(callbackPtrArg);

		}

	   return 0;
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
	int FMUwrapper::loadDll( ) {

		logger_->printDebug2 ("FMUwrapper::loadDll unzipFolderPath_: %s\n", unzipFolderPath_);
		const char* modelId = getModelIdentifier(fmu_.modelDescription);

		dllFilePath_ = (char *) calloc(sizeof(char), strlen(unzipFolderPath_) + strlen(DLL_DIR_STR) 
			+ strlen( modelId ) +  strlen(".dll") + 1); 

		sprintf(dllFilePath_,
			"%s%s%s.dll",
			unzipFolderPath_,
			DLL_DIR_STR,
			modelId
			);


		if (dllFilePath_ == NULL){
			logger_->printfError("Failed to allocate memory for wText\n", dllFilePath_);
			return 1;
		}


		logger_->printDebug2 ("dllFilePath_: %s \n", dllFilePath_);


		HINSTANCE h;
		h = LoadLibrary(dllFilePath_);

		if(!h) {
			logger_->printfError("Can not load %s\n", dllFilePath_);
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


		return 0;
	} 

	///////////////////////////////////////////////////////////////////////////////
	/// Get address of specific function from specific dll
	///
	///\param fmu Name of dll file.
	///\param funnam Function name .
	///\return Address of the specific function.
	//////////////////////////////////////////////////////////////////////////////
	void* FMUwrapper::getAdr(const char* funNam){
		char name[BUFSIZE];
		void* fp;
		sprintf(name, "%s_%s", getModelIdentifier(fmuPointer_->modelDescription), funNam); // Zuo: adding the model name in front of function name


		fp = GetProcAddress(fmuPointer_->dllHandle, name);

		if (!fp) {
			logger_->printfError("Function %s not found in dll\n", name);        
		}
		return fp;
	}



	/*******************************************************//**
	* Parses the xml file located in the FMU.  
	*
	* @param [in,out]	unzipfolder	where the FMU was unzipped to.
	*******************************************************/
	int FMUwrapper::parseXML(char* unzipfolder) {

		logger_->printDebug2("FMUwrapper::parseXML unzipfolder %s\n", unzipfolder);	
		int len1 = strlen(unzipfolder) + 1;

		unzipFolderPath_ = (char *) calloc(sizeof(char), len1 + 1);
		lstrcpy(unzipFolderPath_, unzipfolder);
		logger_->printDebug2("FMUwrapper::parseXML unzipFolderPath_ %s\n", unzipFolderPath_);	

		//construct the path to the XML file and 
		// store as member variable
		int len2 = len1 + 1 + strlen(XML_FILE_STR);
		xmlFilePath_ = (char *) calloc(sizeof(char), len2 + 1);

		sprintf(xmlFilePath_, "%s%s", unzipfolder, XML_FILE_STR);
		fmu_.modelDescription = parse(xmlFilePath_); 
		logger_->printDebug2("FMUwrapper::parseXML xmlFilePath_ %s\n", xmlFilePath_);	

		// Parse only parses the model description and store in structure fmu.modelDescription
		if (fmu_.modelDescription) {
			return 0;
		} else {
			return 1;
		}

	}






	int FMUwrapper::simulateHelperInit() {

		logger_->printDebug("FMUwrapper::simulateHelperInit\n");	


		_TCHAR currentDirectory[MAX_PATH] =  _T("");
		_TCHAR exeDirectory[MAX_PATH] =  _T("");

		GetCurrentDirectory(MAX_PATH, currentDirectory);
		this->getModuleFolderPath(exeDirectory);

		logger_->printDebug2("exeDirectory: %s\n", exeDirectory);	


		//bool success = SetCurrentDirectory(unzipFolderPath_);
		GetCurrentDirectory(MAX_PATH, currentDirectory);
		logger_->printDebug2("currentDirectory: %s\n", currentDirectory);	

		loggingOn = 1;
		ModelDescription* md;            // handle to the parsed XML file        
		const char* guid;                // global unique id of the fmu
		const char* modelId;                //


		fmiCallbackFunctions callbacks;  // called by the model during simulation

		fmiStatus fmiFlag;               // return code of the fmu functions

		fmiBoolean toleranceControlled = fmiFalse;
		nSteps = 0;

		// Run the simulation

		std::string s;

		// convert double b to string s
		{ std::ostringstream ss;
			ss << timeEnd_;
			s = ss.str();
		}

		printf("FMU Simulator: run '%s' from t=0..%g with step size h=%g, loggingOn=%d'\n", 
			unzipFolderPath_, s.c_str(), timeDelta_, loggingOn);


		vars = fmuPointer_->modelDescription->modelVariables;		// add it to get variables
		logger_->printDebug2("Instantiate the fmu%s\n", "");

		fflush(stdout);		
		// instantiate the fmu
		md = fmuPointer_->modelDescription;
		guid = getString(md, att_guid);

		logger_->printDebug2("Got GUID = %s!\n", guid);
		fflush(stdout);		

		callbacks.logger = FMUlogger::log;
		callbacks.allocateMemory = calloc;
		callbacks.freeMemory = free;

		logger_->printDebug("Got callbacks!\n");
		modelId =  getModelIdentifier(md);
		logger_->printDebug2("Model Identifer is %s\n", modelId);

		fmiComponent_ = fmuPointer_->instantiateSlave(modelId, guid, "Model1", "", 100, fmiFalse, fmiFalse, callbacks, loggingOn);

		if (!fmiComponent_) {
			printf("could not instantiate slaves\n");
			return 1;
		}

		logger_->printDebug("Instantiated slaves!\n");	
		// Set the start time and initialize
		time_ = t0;

		logger_->printDebug("start to initialize fmu!\n");
		fflush(stdout);			
		
		fmiFlag =  fmuPointer_->initializeSlave(fmiComponent_, t0, fmiTrue, timeEnd_);

		
		logger_->printDebug("Initialized fmu!\n");
		fflush(stdout);		

		if (fmiFlag > fmiWarning) {
			printf("could not initialize model");
			return 1;																
		}

		logger_->printDebug("OUTPUT: \n");
		int i;

		if (md->modelVariables)

		for (i=0; md->modelVariables[i]; i++){
			ScalarVariable* sv = (ScalarVariable*)md->modelVariables[i];
			
			Enu causality;  // input, output, internal or none
			causality = getCausality(sv);
			
			ScalarVariableMeta meta;
			
			meta.idx = i;
			meta.name = getName( sv );
			meta.causality =  getCausality(sv);
			//meta.description = getDescription(fmuPointer_->modelDescription,  sv );

			metaDataList.push_back(meta);

			if (meta.causality == enu_output) {
				metaDataListOuput.push_back(meta);
			}


		}
		variableCount_ = i;

		return 0;
	}



	int FMUwrapper::isSimulationComplete() {
		return !(time_ < timeEnd_) ;
	}
	
	

	const char* FMUwrapper::getVariableDescription(int idx) {
		ScalarVariable* sv = (ScalarVariable*)fmuPointer_->modelDescription->modelVariables[idx];

		return getDescription(fmuPointer_->modelDescription,  sv );
	}




	int FMUwrapper::getVariableCount() {
		return variableCount_;
	}
	


	void FMUwrapper::printSummary() {

		// Print simulation summary
		if (loggingOn) printf("Step %d to t=%.4f\n", nSteps, time_);		
		printf("Simulation from %g to %g terminated successful\n", t0, timeEnd_);
		printf("  steps ............ %d\n", nSteps);
		printf("  fixed step size .. %g\n", timeDelta_);
	}


	fmiReal FMUwrapper::getResultSnapshot() {

		fmiReal r;

		fmiValueReference vr;				
		ScalarVariable** vars = fmuPointer_->modelDescription->modelVariables;


		// Print all other columns
		ScalarVariable* sv = vars[0];

		// Output values
		vr = getValueReference(sv);
		switch (sv->typeSpec->type){
		case elm_Real:
			fmuPointer_->getReal(fmiComponent_, &vr, 1, &r);
			return r;
			break;
		default:
			return 0;
			break;
		}

	}





	ResultItem * FMUwrapper::getResultItem() {
		return resultItem_;
	}


	ResultItemStruct * FMUwrapper::getResultStruct() {
		return resultItem_->toStruct();
	}



	void FMUwrapper::doOneStep() {

		// Advance to next time step
		status = fmuPointer_->doStep(fmiComponent_, time_, timeDelta_, fmiTrue);  

		time_ = min(time_+timeDelta_, timeEnd_);

		resultItem_ = new ResultItem(fmuPointer_,  fmiComponent_);
		resultItem_->setTime(time_);

		for(std::list<ScalarVariableMeta>::iterator list_iter = metaDataListOuput.begin(); 
			list_iter != metaDataListOuput.end(); list_iter++)
		{
				ScalarVariableMeta svm =  * list_iter;
				ScalarVariable* sv = (ScalarVariable*)fmuPointer_->modelDescription->modelVariables[svm.idx];
				if (getAlias(sv)!=enu_noAlias) continue;
				
				Enu  causality =  getCausality(sv);

				resultItem_->addValue(sv, svm.idx);
		}

		nSteps++;

	}



	void FMUwrapper::getModuleFolderPath(_TCHAR * szDir) {

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

	char* FMUwrapper::getXmlFilePath()
	{
		return xmlFilePath_;
	}


}





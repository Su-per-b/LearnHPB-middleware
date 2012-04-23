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

		printf ("FMUwrapper::loadDll unzipFolderPath_: %s\n", unzipFolderPath_);

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
			printfError("Failed to allocate memory for wText\n", dllFilePath_);
			return 1;
		}


		printf ("dllFilePath_: %s \n", dllFilePath_);


		HINSTANCE h;
		h = LoadLibrary(dllFilePath_);

		if(!h) {
			printfError("Can not load %s\n", dllFilePath_);
			exit(EXIT_FAILURE);
		}

		fmuPointer_->dllHandle = h;
		fmuPointer_->getVersion = (fGetVersion) getAdr(fmuPointer_, "fmiGetVersion");
		fmuPointer_->instantiateSlave = (fInstantiateSlave) getAdr(fmuPointer_, "fmiInstantiateSlave");
		fmuPointer_->freeSlaveInstance = (fFreeSlaveInstance) getAdr(fmuPointer_, "fmiFreeSlaveInstance");
		fmuPointer_->setDebugLogging = (fSetDebugLogging) getAdr(fmuPointer_, "fmiSetDebugLogging");
		fmuPointer_->setReal = (fSetReal) getAdr(fmuPointer_, "fmiSetReal");
		fmuPointer_->setInteger = (fSetInteger) getAdr(fmuPointer_, "fmiSetInteger");
		fmuPointer_->setBoolean = (fSetBoolean) getAdr(fmuPointer_, "fmiSetBoolean");
		fmuPointer_->setString = (fSetString) getAdr(fmuPointer_, "fmiSetString");
		fmuPointer_->initializeSlave = (fInitializeSlave) getAdr(fmuPointer_, "fmiInitializeSlave");
		fmuPointer_->getReal = (fGetReal) getAdr(fmuPointer_, "fmiGetReal");
		fmuPointer_->getInteger = (fGetInteger) getAdr(fmuPointer_, "fmiGetInteger");
		fmuPointer_->getBoolean = (fGetBoolean) getAdr(fmuPointer_, "fmiGetBoolean");
		fmuPointer_->getString = (fGetString) getAdr(fmuPointer_, "fmiGetString");
		fmuPointer_->doStep = (fDoStep) getAdr(fmuPointer_, "fmiDoStep");


		return 0;
	} 


	/*******************************************************//**
	* Parses the xml file located in the FMU.  
	*
	* @param [in,out]	unzipfolder	where the FMU was unzipped to.
	*******************************************************/
	int FMUwrapper::parseXML(char* unzipfolder) {

		printf("FMUwrapper::parseXML unzipfolder %s\n", unzipfolder);	
		int len1 = strlen(unzipfolder) + 1;

		unzipFolderPath_ = (char *) calloc(sizeof(char), len1 + 1);
		lstrcpy(unzipFolderPath_, unzipfolder);
		printf("FMUwrapper::parseXML unzipFolderPath_ %s\n", unzipFolderPath_);	

		//construct the path to the XML file and 
		// store as member variable
		int len2 = len1 + 1 + strlen(XML_FILE_STR);
		xmlFilePath_ = (char *) calloc(sizeof(char), len2 + 1);

		sprintf(xmlFilePath_, "%s%s", unzipfolder, XML_FILE_STR);
		fmu_.modelDescription = parse(xmlFilePath_); 
		printf("FMUwrapper::parseXML xmlFilePath_ %s\n", xmlFilePath_);	

		// Parse only parses the model description and store in structure fmu.modelDescription
		if (fmu_.modelDescription) {
			return 0;
		} else {
			return 1;
		}

	}






	int FMUwrapper::simulateHelperInit() {

		printf("FMUwrapper::simulateHelperInit\n");	


		_TCHAR currentDirectory[MAX_PATH] =  _T("");
		_TCHAR exeDirectory[MAX_PATH] =  _T("");

		GetCurrentDirectory(MAX_PATH, currentDirectory);
		this->getModuleFolderPath(exeDirectory);

		printf("exeDirectory: %s\n", exeDirectory);	


		//bool success = SetCurrentDirectory(unzipFolderPath_);
		GetCurrentDirectory(MAX_PATH, currentDirectory);
		printf("currentDirectory: %s\n", currentDirectory);	

		loggingOn = 1;
		ModelDescription* md;            // handle to the parsed XML file        
		const char* guid;                // global unique id of the fmu
		const char* modelId;                //


		fmiCallbackFunctions callbacks;  // called by the model during simulation

		fmiStatus fmiFlag;               // return code of the fmu functions

		fmiBoolean toleranceControlled = fmiFalse;
		nSteps = 0;

		// Run the simulation
		printf("FMU Simulator: run '%s' from t=0..%g with step size h=%g, loggingOn=%d'\n", 
			unzipFolderPath_, timeEnd_, timeDelta_, loggingOn);

		vars = fmuPointer_->modelDescription->modelVariables;		// add it to get variables
		printf("Instantiate the fmu\n");
		fflush(stdout);		
		// instantiate the fmu
		md = fmuPointer_->modelDescription;
		guid = getString(md, att_guid);
		printf("Got GUID = %s!\n", guid);
		fflush(stdout);		
		callbacks.logger = fmuLogger;
		callbacks.allocateMemory = calloc;
		callbacks.freeMemory = free;

		printf("Got callbacks!\n");
		modelId =  getModelIdentifier(md);
		printf("Model Identifer is %s\n", modelId);

		fmiComponent_ = fmuPointer_->instantiateSlave(modelId, guid, "Model1", "", 100, fmiFalse, fmiFalse, callbacks, loggingOn);

		if (!fmiComponent_) {
			printf("could not instantiate slaves\n");
			return 1;
		}

		printf("Instantiated slaves!\n");	
		// Set the start time and initialize
		time_ = t0;

		printf("start to initialize fmu!\n");
		fflush(stdout);			
		
		fmiFlag =  fmuPointer_->initializeSlave(fmiComponent_, t0, fmiTrue, timeEnd_);

		
		printf("!!Initialized fmu!\n");
		fflush(stdout);		

		if (fmiFlag > fmiWarning) {
			printf("could not initialize model");
			return 1;																
		}

		//vry_[0] = getValueReference(getVariableByName(md, "TrmSou"));


		printf("OUTPUT: \n");

		//std::list<ScalarVariableMeta> myList;

		int i;

		if (md->modelVariables)

		for (i=0; md->modelVariables[i]; i++){
			ScalarVariable* sv = (ScalarVariable*)md->modelVariables[i];
			

			Enu causality;  // input, output, internal or none
			causality = getCausality(sv);
			

			ScalarVariableMeta meta;
			
			meta.idx = i;
			meta.name = getName( sv );
			meta.causality = getVariableCausality(i);

			metaDataList.push_back(meta);
			metaDataListTest.push_back(i);



			//if (enu_output == causality) {
			//	printf("OUTPUT variable name: %s\n", getName( sv ));
			//	printf("                desc: %s\n", getDescription(md, sv));


				//list.push_front (2);

			//	printf("                ausality: %s\n", getCausality( sv));

				
			//}
		}


		return 0;
	}



	int FMUwrapper::isSimulationComplete() {
		return !(time_ < timeEnd_) ;
	}
	
	
	std::list<int> FMUwrapper::getDataList() {
		return metaDataListTest;
	}
	


	const char* FMUwrapper::getVariableName(int idx) {
		ScalarVariable* sv = (ScalarVariable*)fmuPointer_->modelDescription->modelVariables[idx];

		return getName( sv );
	}

	Elm FMUwrapper::getVariableType(int idx) {


		ScalarVariable* sv = (ScalarVariable*)fmuPointer_->modelDescription->modelVariables[idx];
		Elm elmType = sv->typeSpec->type;

		return elmType;


	}
	




	const char* FMUwrapper::getVariableDescription(int idx) {
		ScalarVariable* sv = (ScalarVariable*)fmuPointer_->modelDescription->modelVariables[idx];

		return getDescription(fmuPointer_->modelDescription,  sv );
	}

	Enu FMUwrapper::getVariableCausality(int idx) {
		ScalarVariable* sv = (ScalarVariable*)fmuPointer_->modelDescription->modelVariables[idx];


		Enu causality;  // input, output, internal or none
		causality = getCausality(sv);

		return causality;
	}


	int FMUwrapper::getVariableCount() {

		//int len = sizeof (fmuPointer_->modelDescription->modelVariables);


		int i;
		int len;
		len = 0;

		ModelDescription* md = fmuPointer_->modelDescription;  

		for (i=0; md->modelVariables[i]; i++){
			len++;
		}

		return len;

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




	void FMUwrapper::doOneStep() {



		// Advance to next time step
		status = fmuPointer_->doStep(fmiComponent_, time_, timeDelta_, fmiTrue);  


		time_ = min(time_+timeDelta_, timeEnd_);

		resultItem_ = new ResultItem();
		resultItem_->setTime(time_);
		resultItem_->addModelVariables(fmuPointer_ , fmiComponent_); //fmuPointer_->modelDescription->modelVariables);

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





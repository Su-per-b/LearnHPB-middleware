// MainFMUwrapper.cpp : Defines the exported functions for the DLL application.
//
#include "stdafx.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include "MainFMUwrapper.h"

#define BUFSIZE 4096
#define XML_FILE_STR  "\\modelDescription.xml"
#define DLL_DIR_STR   "\\binaries\\win32\\"
#define RESULT_FILE "result.csv"
#define ZLIB_WINAPI





extern "C" __declspec(dllexport) 
int add (int x, int y)
{
    return x + y;
}

extern "C" __declspec(dllexport) 
void testFMU ()
{
	init(_T("C:\\Temp\\LearnGB_VAVReheat_ClosedLoop"));

	//MainFMUwrapper::test1
}


void init(_TCHAR * fmuSubPath) {
//
	Straylight::MainFMUwrapper *  fmuWrapper = new Straylight::MainFMUwrapper();

	//fmuWrapper->test1();


	//_TCHAR *fmuUnzippedFolder=new TCHAR[MAX_PATH+1];

	//fmuWrapper->getModuleFolderPath(fmuUnzippedFolder);
	//_tcscat(fmuUnzippedFolder, fmuSubPath);

	//fmuUnzippedFolder[lstrlen(fmuUnzippedFolder)] = '\0';

	char * wfmuUnzippedFolder = wstrdup (fmuSubPath);

	int result = fmuWrapper->parseXML(wfmuUnzippedFolder);
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();



}


	char * wstrdup(_TCHAR * wSrc)
	{
		int l_idx=0;
		int l_len = wstrlen(wSrc);
		char * l_nstr = (char*)malloc(l_len);
		if (l_nstr) {
			do {
			   l_nstr[l_idx] = (char)wSrc[l_idx];
			   l_idx++;
			} while ((char)wSrc[l_idx]!=0);
		}
		l_nstr[l_idx] = 0;
		return l_nstr;
	}


// returns number of TCHARs in string
int wstrlen(_TCHAR * wstr)
{
    int l_idx = 0;
    while (((char*)wstr)[l_idx]!=0) l_idx+=2;
    return l_idx;
}


namespace Straylight
{
	/*********************************************//**
												   * Default constructor. 
												   *********************************************/
	MainFMUwrapper::MainFMUwrapper(void)
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
	MainFMUwrapper::~MainFMUwrapper(void)
	{

		// Cleanup
		//fclose(file);

		free((void *) unzipFolderPath_);
		free((void *) xmlFilePath_);
		free((void *) dllFilePath_);

		// Release FMU 
		FreeLibrary(fmu_.dllHandle);
		freeElement(fmu_.modelDescription);

	}



	/*********************************************//**
												   * Constructs the path to the DLL file and stores it
												   * in the dllFilePath_ member variable. Loads
												   * the DLL with the "LoadLibrary" command and populates the
												   * FMU struct.
												   *
												   * @return	0 for success
																							   *	   *********************************************/
	int MainFMUwrapper::loadDll( ) {

		printf ("MainFMUwrapper::loadDll unzipFolderPath_: %s\n", unzipFolderPath_);

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


		//int result = loadDLLhelper(dllFilePath_, &fmu_);

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
	int MainFMUwrapper::parseXML(char* unzipfolder) {

		//unzipFolderPath_ = unzipfolder;

		//add the trailing slash to the path and store
		// in member variable
		//char* unzipFolderPathLocal;
		printf("MainFMUwrapper::parseXML unzipfolder %s\n", unzipfolder);	


		int len1 = strlen(unzipfolder) + 1;

		//unzipFolderPathLocal = (char *) calloc(sizeof(char), xmlFilePath1_len + 1);


		unzipFolderPath_ = (char *) calloc(sizeof(char), len1 + 1);
		//sprintf(xmlFilePath_, "%s%s", unzipfolder, XML_FILE_STR);


		lstrcpy(unzipFolderPath_, unzipfolder);

		printf("MainFMUwrapper::parseXML unzipFolderPath_ %s\n", unzipFolderPath_);	

		//construct the path to the XML file and 
		// store as member variable
		int len2 = len1 + 1 + strlen(XML_FILE_STR);
		xmlFilePath_ = (char *) calloc(sizeof(char), len2 + 1);

		//sprintf(xmlFilePath_, "%s%s", unzipfolder, "\\");
		sprintf(xmlFilePath_, "%s%s", unzipfolder, XML_FILE_STR);

		fmu_.modelDescription = parse(xmlFilePath_); 

		printf("MainFMUwrapper::parseXML xmlFilePath_ %s\n", xmlFilePath_);	

		// Parse only parses the model description and store in structure fmu.modelDescription
		if (fmu_.modelDescription) {
			return 0;
		} else {
			return 1;
		}


	}


	void MainFMUwrapper::test1() {
		printf("MainFMUwrapper::test1\n");	
	}



	int MainFMUwrapper::simulateHelperInit() {

		printf("MainFMUwrapper::simulateHelperInit\n");	


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



		// Output solution for time t0 
	//	outputRow(fmuPointer_, fmiComponent_, t0, file, csv_separator_); // output initla value of fmu 


		///////////////////////////////////////////////////////////////////////////// 
		// Get value references for input and output varibles
		// Note: User needs to specify the name of variables for their own fmus
		//Begin------------------------------------------------------------------

		 // vru_[0] = getValueReference(getVariableByName(md, "Toa"));
		  vry_[0] = getValueReference(getVariableByName(md, "TrmSou"));


		//End--------------------------------------------------------------------

		//printf("Enter in simulation loop\n");	
			
	
		return 0;
	}




	int MainFMUwrapper::isSimulationComplete() {

		return !(time_ < timeEnd_) ;
	}



	void MainFMUwrapper::printSummary() {

		// Print simulation summary
		if (loggingOn) printf("Step %d to t=%.4f\n", nSteps, time_);		
		printf("Simulation from %g to %g terminated successful\n", t0, timeEnd_);
		printf("  steps ............ %d\n", nSteps);
		printf("  fixed step size .. %g\n", timeDelta_);
	}


	fmiReal MainFMUwrapper::getResultSnapshot() {


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





	ResultItem * MainFMUwrapper::getResultItem() {
		return resultItem_;
	}


	void MainFMUwrapper::doOneStep() {


		//if (loggingOn) printf(, nSteps, time_);	
		//printfDebug
		/*
		char msg[256];
		sprintf (msg, "Step %d to t=%.4f\n", nSteps, time_);
		OutputDebugString(msg);



		int kCounter_;							// add a counter for variables

		///////////////////////////////////////////////////////////////////////////
		// Step 1: get values of output variables	from slaves
		for (kCounter_=0; vars[kCounter_]; kCounter_++) {
			ScalarVariable* sv = vars[kCounter_];
			if (getAlias(sv)!=enu_noAlias) continue;
			if (getCausality(sv) != enu_output) continue; // only get output variable
			vr = getValueReference(sv);

			switch (sv->typeSpec->type){
			case elm_Real:
				fmuPointer_->getReal(fmiComponent_, &vr, 1, &ry);
				break;
			case elm_Integer:
				fmuPointer_->getInteger(fmiComponent_, &vr, 1, &iy);  
				break;
			case elm_Boolean:
				fmuPointer_->getBoolean(fmiComponent_, &vr, 1, &by);
				break;
			case elm_String:
				fmuPointer_->getString(fmiComponent_, &vr, 1, &sy);
				break;
			}


			// Allocate values to cooresponding varibles on master program
			// Note: User needs to specify the output variables for their own fmu 
			//Begin------------------------------------------------------------------
			if (vr == vry_[0]) 
				ry1 = ry;

			//End--------------------------------------------------------------------
		} 
		*/

		///////////////////////////////////////////////////////////////////////////
		// Step 2: compute on master side 
		// Note: User can adjust the computing schemes of mater program
		//Begin------------------------------------------------------------------
		//ru1 = 293;
		//ru2 = ry1 + 0.5;
		//End--------------------------------------------------------------------


		/*
		for (kCounter_=0; vars[kCounter_]; kCounter_++) {
			ScalarVariable* sv = vars[kCounter_];
			if (getAlias(sv)!=enu_noAlias) continue;
			if (getCausality(sv) != enu_input) continue; // only set input variable
			vr = getValueReference(sv);

			switch (sv->typeSpec->type){
			case elm_Real:

				if(vr == vru_[0]) {
					fmuPointer_->setReal(fmiComponent_, &vr, 1, &ru1); 				
					printDebug("Set u1\n");
				}
				else
					printf("Warning: no data given for input variable\n");
				break;
			case elm_Integer:
				fmuPointer_->setInteger(fmiComponent_, &vr, 1, &ix);  
				break;
			case elm_Boolean:
				fmuPointer_->setBoolean(fmiComponent_, &vr,1, &bx);
				break;
			case elm_String:
				fmuPointer_->setString(fmiComponent_, &vr, 1, &sx);
				break;        
			}
			//End--------------------------------------------------------------------        
		} 
		*/



		// Advance to next time step
		status = fmuPointer_->doStep(fmiComponent_, time_, timeDelta_, fmiTrue);  


		time_ = min(time_+timeDelta_, timeEnd_);

		resultItem_ = new ResultItem();
		resultItem_->setTime(t0);

		resultItem_->addModelVariables(fmuPointer_ , fmiComponent_); //fmuPointer_->modelDescription->modelVariables);

		//resultSet_->resultItemList.push_back(resultItem_);

		nSteps++;


	}



	void MainFMUwrapper::getModuleFolderPath(_TCHAR * szDir) {

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


}





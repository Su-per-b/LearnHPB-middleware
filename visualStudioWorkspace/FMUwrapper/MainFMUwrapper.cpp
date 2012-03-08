// MainFMUwrapper.cpp : Defines the exported functions for the DLL application.
//
#include "stdafx.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include "MainFMUwrapper.h"

#define BUFSIZE 4096
#define XML_FILE  "modelDescription.xml"
#define DLL_DIR   "binaries\\win32\\"
#define RESULT_FILE "result.csv"
#define ZLIB_WINAPI




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
												   *********************************************/
	int MainFMUwrapper::loadDll( ) {


		const char* modelId = getModelIdentifier(fmu_.modelDescription);

		dllFilePath_ = (char *) calloc(sizeof(char), strlen(unzipFolderPath_) + strlen(DLL_DIR) 
			+ strlen( modelId ) +  strlen(".dll") + 1); 

		sprintf(dllFilePath_,
			"%s%s%s.dll",
			unzipFolderPath_,
			DLL_DIR,
			modelId
			);


		if (dllFilePath_ == NULL){
			printfError("Failed to allocate memory for wText\n", dllFilePath_);
			return 1;
		}


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


		//add the trailing slash to the path and store
		// in member variable
		int xmlFilePath1_len = strlen(unzipfolder) + 2;
		unzipFolderPath_ = (char *) calloc(sizeof(char), xmlFilePath1_len);
		sprintf(unzipFolderPath_, "%s%s", unzipfolder, "\\");


		//construct the path to the XML file and 
		// store as member variable
		int xmlFilePath2_len = xmlFilePath1_len + strlen(XML_FILE);
		xmlFilePath_ = (char *) calloc(sizeof(char), xmlFilePath2_len);

		sprintf(xmlFilePath_, "%s%s", unzipFolderPath_, XML_FILE);

		fmu_.modelDescription = parse(xmlFilePath_); 

		// Parse only parses the model description and store in structure fmu.modelDescription
		if (fmu_.modelDescription) {
			return 0;
		} else {
			return 1;
		}


	}



	int MainFMUwrapper::simulateHelperInit() {

		loggingOn = 0;
		ModelDescription* md;            // handle to the parsed XML file        
		const char* guid;                // global unique id of the fmu
		fmiCallbackFunctions callbacks;  // called by the model during simulation

		fmiStatus fmiFlag;               // return code of the fmu functions

		fmiBoolean toleranceControlled = fmiFalse;
		nSteps = 0;


		//resultSet_ = new ResultSet();

		// Run the simulation
		printf("FMU Simulator: run '%s' from t=0..%g with step size h=%g, loggingOn=%d, csv separator='%c'\n", 
			unzipFolderPath_, timeEnd_, timeDelta_, loggingOn, csv_separator_);


		vars = fmuPointer_->modelDescription->modelVariables;		// add it to get variables
		printDebug("Instantiate the fmu\n");

		// instantiate the fmu
		md = fmuPointer_->modelDescription;
		guid = getString(md, att_guid);
		printfDebug("Got GUID = %s!\n", guid);	
		callbacks.logger = fmuLogger;

		callbacks.allocateMemory = calloc;
		callbacks.freeMemory = free;
		printDebug("Got callbacks!\n");
		printfDebug("Model Identifer is %s\n", getModelIdentifier(md));

		fmiComponent_ = fmuPointer_->instantiateSlave(getModelIdentifier(md), guid, "Model1", "", 10, fmiFalse, fmiFalse, callbacks, loggingOn);

		if (!fmiComponent_) {
			printError("could not instantiate slaves\n");
			return 1;
		}


		printDebug("Instantiated slaves!\n");	



		// Set the start time and initialize
		time_ = t0;

		printDebug("start to initialize fmu!\n");	   
		fmiFlag =  fmuPointer_->initializeSlave(fmiComponent_, t0, fmiTrue, timeEnd_);	
		printDebug("Initialized fmu!\n");
		if (fmiFlag > fmiWarning) {
			printError("could not initialize model");
			return 1;																
		}

		// Output solution for time t0 
	//	outputRow(fmuPointer_, fmiComponent_, t0, file, csv_separator_); // output initla value of fmu 


		///////////////////////////////////////////////////////////////////////////// 
		// Get value references for input and output varibles
		// Note: User needs to specify the name of variables for their own fmus
		//Begin------------------------------------------------------------------
		vru_[0] = getValueReference(getVariableByName(md, "u1"));
		vru_[1] = getValueReference(getVariableByName(md, "u2"));
		vry_[0] = getValueReference(getVariableByName(md, "y1"));
		vry_[1] = getValueReference(getVariableByName(md, "y2"));
		//End--------------------------------------------------------------------

		printDebug("Enter in simulation loop\n");	

		return 1;
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
			if (vr == vry_[0]) ry1 = ry;
			else if(vr == vry_[1]) ry2 = ry;
			//End--------------------------------------------------------------------
		} 

		///////////////////////////////////////////////////////////////////////////
		// Step 2: compute on master side 
		// Note: User can adjust the computing schemes of mater program
		//Begin------------------------------------------------------------------
		ru1 = ry2 + 3.0; 
		ru2 = ry1 + 0.5;
		//End--------------------------------------------------------------------

		//////////////////////////////////////////////////////////////////////////
		// Step 3: set input variables back to slaves
		for (kCounter_=0; vars[kCounter_]; kCounter_++) {
			ScalarVariable* sv = vars[kCounter_];
			if (getAlias(sv)!=enu_noAlias) continue;
			if (getCausality(sv) != enu_input) continue; // only set input variable
			vr = getValueReference(sv);
			// Note: User can adjust the settings for input variables
			//Begin------------------------------------------------------------------
			switch (sv->typeSpec->type){
			case elm_Real:

				if(vr == vru_[0]) {
					fmuPointer_->setReal(fmiComponent_, &vr, 1, &ru1); 				
					printDebug("Set u1\n");
				}
				else if (vr == vru_[1]) {
					fmuPointer_->setReal(fmiComponent_, &vr, 1, &ru2);
					printDebug("Set u2\n");
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

		// Advance to next time step
		status = fmuPointer_->doStep(fmiComponent_, time_, timeDelta_, fmiTrue);  


		time_ = min(time_+timeDelta_, timeEnd_);

		resultItem_ = new ResultItem();
		resultItem_->setTime(t0);

		resultItem_->addModelVariables(fmuPointer_ , fmiComponent_); //fmuPointer_->modelDescription->modelVariables);

		//resultSet_->resultItemList.push_back(resultItem_);

		nSteps++;


	}





}





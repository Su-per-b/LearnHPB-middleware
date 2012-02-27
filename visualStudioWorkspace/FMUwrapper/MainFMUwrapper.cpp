// MainFMUwrapper.cpp : Defines the exported functions for the DLL application.
//
#include "stdafx.h"
#include <jni.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include "MainFMUwrapper.h"

#define BUFSIZE 4096
#define XML_FILE  "modelDescription.xml"
#define DLL_DIR   "binaries\\win32\\"
#define RESULT_FILE "result.csv"
#define ZLIB_WINAPI

//static FMU fmu; // the fmu to simulate

  /*
   * Class:     ReadFile
   * Method:    loadFile
   * Signature: (Ljava/lang/String;)[B
   */
JNIEXPORT void JNICALL Java_ReadFile_loadFile
  (JNIEnv * env, jobject jobj, jstring name) {


    return;

}


namespace Straylight
{
		

		MainFMUwrapper::MainFMUwrapper(void)
		{
			timeEnd_ = 1.0;
			csv_separator_ = ',';
			t0 = 0;
			timeDelta_=0.1;
			fmuPointer_ = &fmu_;
		}


		MainFMUwrapper::~MainFMUwrapper(void)
		{
			free(dllFilePath_);
			
		}


		



		int MainFMUwrapper::loadDll( ) {


			const char* modelId = getModelIdentifier(fmu_.modelDescription);

			dllFilePath_ = (char *) calloc(sizeof(char), strlen(unzipfolder_) + strlen(DLL_DIR) 
					+ strlen( modelId ) +  strlen(".dll") + 1); 

			sprintf(dllFilePath_,
				"%s%s%s.dll",
				unzipfolder_,
				DLL_DIR,
				modelId
			);



			if (dllFilePath_ == NULL){
				printfError("Failed to allocate memory for wText\n", dllFilePath_);
				return 1;
			}


			int result = loadDLLhelper(dllFilePath_, &fmu_);

			if (result) exit(EXIT_FAILURE);

			return 0;
		}

		///////////////////////////////////////////////////////////////////////////////
		/// Load the given dll and set function pointers in fmu.
		/// It changes the names of the standard FMI functions by adding the model identifer
		/// and links the new functions with QTronic's FMU structure.
		///
		///\param dllPat Path of the dll file.
		///\param fmu Name of FMU.
		///\return 0 if there is no error occurred.
		///////////////////////////////////////////////////////////////////////////////
		int MainFMUwrapper::loadDLLhelper(const char* dllPat, FMU *fmu) {
  
		//wchar_t * dllPatW;


			HINSTANCE h;

		  h = LoadLibrary(dllPat);
		  free((void *) dllPat);

		  if(!h) {
			printfError("Can not load %s\n", dllPat);
			return 1;
		  }

		  fmu->dllHandle = h;
		  fmu->getVersion = (fGetVersion) getAdr(fmu, "fmiGetVersion");
		  fmu->instantiateSlave = (fInstantiateSlave) getAdr(fmu, "fmiInstantiateSlave");
		  fmu->freeSlaveInstance = (fFreeSlaveInstance) getAdr(fmu, "fmiFreeSlaveInstance");
		  fmu->setDebugLogging = (fSetDebugLogging) getAdr(fmu, "fmiSetDebugLogging");
		  fmu->setReal = (fSetReal) getAdr(fmu, "fmiSetReal");
		  fmu->setInteger = (fSetInteger) getAdr(fmu, "fmiSetInteger");
		  fmu->setBoolean = (fSetBoolean) getAdr(fmu, "fmiSetBoolean");
		  fmu->setString = (fSetString) getAdr(fmu, "fmiSetString");
		  fmu->initializeSlave = (fInitializeSlave) getAdr(fmu, "fmiInitializeSlave");
		  fmu->getReal = (fGetReal) getAdr(fmu, "fmiGetReal");
		  fmu->getInteger = (fGetInteger) getAdr(fmu, "fmiGetInteger");
		  fmu->getBoolean = (fGetBoolean) getAdr(fmu, "fmiGetBoolean");
		  fmu->getString = (fGetString) getAdr(fmu, "fmiGetString");
		  fmu->doStep = (fDoStep) getAdr(fmu, "fmiDoStep");

		  return 0;
		}


		

		void MainFMUwrapper::parseXML(char* unzipfolder) {
			

			char* xmlFilePath1;

			int xmlFilePath1_len = strlen(unzipfolder) + 2;

			xmlFilePath1 = (char *) calloc(sizeof(char), xmlFilePath1_len);
			sprintf(xmlFilePath1, "%s%s", unzipfolder, "\\");

			unzipfolder_ = xmlFilePath1;

			int xmlFilePath2_len = strlen(xmlFilePath1) + 1 + strlen(XML_FILE);
			xmlFilePath_ = (char *) calloc(sizeof(char), xmlFilePath2_len);
			
			sprintf(xmlFilePath_, "%s%s", xmlFilePath1, XML_FILE);



			fmu_.modelDescription = parse(xmlFilePath_); 

			// Parse only parses the model description and store in structure fmu.modelDescription
			
			if (!fmu_.modelDescription) exit(EXIT_FAILURE);

		}



		int MainFMUwrapper::simulateHelperInit() {

			loggingOn = 0;
			ModelDescription* md;            // handle to the parsed XML file        
			const char* guid;                // global unique id of the fmu
			fmiCallbackFunctions callbacks;  // called by the model during simulation

			fmiStatus fmiFlag;               // return code of the fmu functions

			fmiBoolean toleranceControlled = fmiFalse;
			nSteps = 0;


			// Run the simulation
			printf("FMU Simulator: run '%s' from t=0..%g with step size h=%g, loggingOn=%d, csv separator='%c'\n", 
					unzipfolder_, timeEnd_, timeDelta_, loggingOn, csv_separator_);


			//Note: User defined references
			//Begin------------------------------------------------------------------

			//End--------------------------------------------------------------------

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

			// Open result file
			if (!(file=fopen(RESULT_FILE, "w"))) {
			printfError("could not write %s because:\n", RESULT_FILE);
			printfError("    %s\n", strerror(errno));
			return 1;
			}
			printDebug("Open results file!\n");    

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
			outputRow(fmuPointer_, fmiComponent_, t0, file, csv_separator_); // output initla value of fmu 

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

		int MainFMUwrapper::simulateLoop( ) {

			simulateHelperInit();


			// enter the simulation loop
			while (!isSimulationComplete()) {
				doOneStep();
			} 

			simulateHelperCleanup();

			return 0; // success
		}

		int MainFMUwrapper::isSimulationComplete() {

			return !(time_ < timeEnd_) ;
		}


		void MainFMUwrapper::simulateHelperCleanup( ) {

			// Cleanup
			fclose(file);
			free(unzipfolder_);
			free(xmlFilePath_);

			
			// Release FMU 
			FreeLibrary(fmu_.dllHandle);
			freeElement(fmu_.modelDescription);

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

		
		void MainFMUwrapper::doOneStep() {


			if (loggingOn) printf("Step %d to t=%.4f\n", nSteps, time_);		  
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
					fmuPointer_->setBoolean(fmiComponent_, &vr, 1, &bx);
					break;
					case elm_String:
					fmuPointer_->setString(fmiComponent_, &vr, 1, &sx);
					break;        
				}
				//End--------------------------------------------------------------------        
			} 
    
			// Advance to next time step
			status = fmuPointer_->doStep(fmiComponent_, time_, timeDelta_, fmiTrue);  
			// Terminate this row
			fprintf(file, "\n");      
   
			time_ = min(time_+timeDelta_, timeEnd_);
			outputRow(fmuPointer_, fmiComponent_, time_, file, csv_separator_); // output values for this step
			nSteps++;


		}





    }





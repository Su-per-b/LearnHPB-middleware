// FMUloader.cpp : Defines the exported functions for the DLL application.
//
#include <jni.h>
#include <sys/types.h>


#include <sys/stat.h>
#include <fcntl.h>


#include "FMUloader.h"

//#include "FMU.h"







#define BUFSIZE 4096
#define XML_FILE  "modelDescription.xml"
#define DLL_DIR   ""
#define RESULT_FILE "result.csv"

//static FMU fmu; // the fmu to simulate

  /*
   * Class:     ReadFile
   * Method:    loadFile
   * Signature: (Ljava/lang/String;)[B
   */
JNIEXPORT void JNICALL Java_ReadFile_loadFile
  (JNIEnv * env, jobject jobj, jstring name) {



    jbyteArray jb;
    jboolean iscopy;
    struct stat finfo;



    return;



}


namespace Straylight
{
		

		FMUloader::FMUloader(void)
		{
			timeEnd_ = 1.0;
			csv_separator_ = ',';
			t0 = 0;
			timeDelta_=0.1;
			fmuPointer_ = &fmu_;
		}


		FMUloader::~FMUloader(void)
		{
			free(unzippedPath_);
			
		}


		
		void FMUloader::doAll(const char * f) {
			fmuFileName_ = f;
			doall(f);
		}


		void FMUloader::setFMU(const char * f) {
			fmuFileName_ = f;
		}

		void FMUloader::unzip() {

			double tEnd = 1.0;
			double h=0.1;
			int loggingOn = 0;
			char csv_separator = ',';  

			// Get absolute path to FMU, NULL if not found  
			unzippedPath_ = getTmpPath(fmuFileName_, strlen(this->fmuFileName_)-4);

			if(unzippedPath_==NULL){
			  printError("Cannot allocate temporary path\n");
			  exit(EXIT_FAILURE);
			}

			// Unzip the FMU to the tmpPat directory
			if (unpack(fmuFileName_, unzippedPath_)) {  
				printfError("Fail to unpack fmu \"%s\"\n", fmuFileName_);
				exit(EXIT_FAILURE);
			}

		}

		void FMUloader::parseXML() {
			char* xmlFilePath;

		    printDebug("parse tmpPat\\modelDescription.xml\n");
			xmlFilePath = (char *) calloc(sizeof(char), strlen(unzippedPath_) + strlen(XML_FILE) + 1);
			sprintf(xmlFilePath, "%s%s", unzippedPath_, XML_FILE);

			// Parse only parses the model description and store in structure fmu.modelDescription
			this->fmu_.modelDescription = parse(xmlFilePath); 
			
			free(xmlFilePath);
			if (!fmu_.modelDescription) exit(EXIT_FAILURE);

		}


		void FMUloader::loadDLL() {

			dllPath_ = (char *) calloc(sizeof(char), strlen(unzippedPath_) + strlen(DLL_DIR) 
					+ strlen( getModelIdentifier(fmu_.modelDescription)) +  strlen(".dll") + 1); 
			sprintf(dllPath_,"%s%s%s.dll", unzippedPath_, DLL_DIR, getModelIdentifier(fmu_.modelDescription)); 

		    // Load the FMU dll
			if (loadDLLhelper(dllPath_, &fmu_)) exit(EXIT_FAILURE); 
			printfDebug("Loaded \"%s\"\n", dllPath_); 

		}




		int FMUloader::runSimulation() {


			if (simulateHelper()){
			  printError("Simulation failed\n");
			  exit(EXIT_FAILURE);
			}

			printf("CSV file '%s' written", RESULT_FILE);

			// Release FMU 
			FreeLibrary(fmu_.dllHandle);
			freeElement(fmu_.modelDescription);
			return 0;

		}

		int FMUloader::simulateHelperInit() {

			loggingOn = 0;
			ModelDescription* md;            // handle to the parsed XML file        
			const char* guid;                // global unique id of the fmu
			fmiCallbackFunctions callbacks;  // called by the model during simulation

			fmiStatus fmiFlag;               // return code of the fmu functions

			fmiBoolean toleranceControlled = fmiFalse;
			nSteps = 0;


			// Run the simulation
			printf("FMU Simulator: run '%s' from t=0..%g with step size h=%g, loggingOn=%d, csv separator='%c'\n", 
					fmuFileName_, timeEnd_, timeDelta_, loggingOn, csv_separator_);


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
		}

		int FMUloader::simulateHelper( ) {

			simulateHelperInit();


			// enter the simulation loop
			while (isSimulationComplete()) {
				doOneStep();
			} 

			simulateHelperCleanup();

			return 0; // success
		}

		int FMUloader::isSimulationComplete() {

			return (time_ < timeEnd_) ;
		}


		void FMUloader::simulateHelperCleanup( ) {

			// Cleanup
			fclose(file);

			// Print simulation summary 
			if (loggingOn) printf("Step %d to t=%.4f\n", nSteps, time_);		
			printf("Simulation from %g to %g terminated successful\n", t0, timeEnd_);
			printf("  steps ............ %d\n", nSteps);
			printf("  fixed step size .. %g\n", timeDelta_);
		}


		fmiReal FMUloader::getResultSnapshot() {

			  int k;
			  fmiReal r;
			  fmiInteger i;
			  fmiBoolean b;
			  fmiString s;
			  fmiValueReference vr;				
			  ScalarVariable** vars = fmuPointer_->modelDescription->modelVariables;
			  char buffer[32];

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

		
		void FMUloader::doOneStep() {


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





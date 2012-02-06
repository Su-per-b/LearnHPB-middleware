// FMUloader.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"
#include "FMUloader.h"

//#include "FMU.h"

#include <stdlib.h>
#include <stdio.h>
#include <string.h>




#define BUFSIZE 4096
#define XML_FILE  "modelDescription.xml"
#define DLL_DIR   ""
#define RESULT_FILE "result.csv"

//static FMU fmu; // the fmu to simulate


namespace Straylight
{
		

		FMUloader::FMUloader(void)
		{
			timeEnd_ = 1.0;

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





		void FMUloader::runSimulation() {

			double h=0.1;
			int loggingOn = 0;
			char csv_separator = ','; 

			// Run the simulation
			printf("FMU Simulator: run '%s' from t=0..%g with step size h=%g, loggingOn=%d, csv separator='%c'\n", 
					fmuFileName_, timeEnd_, h, loggingOn, csv_separator);

			if (this->simulateHelper(&fmu_, h, loggingOn, csv_separator)){
			  printError("Simulation failed\n");
			  exit(EXIT_FAILURE);
			}
		}


		int FMUloader::simulateHelper(FMU* fmu, double h, fmiBoolean loggingOn, char separator) {

			double time;  
			ModelDescription* md;            // handle to the parsed XML file        
			const char* guid;                // global unique id of the fmu
			fmiCallbackFunctions callbacks;  // called by the model during simulation
			fmiComponent c;                  // instance of the fmu 
			fmiStatus fmiFlag;               // return code of the fmu functions
			fmiReal t0 = 0;                  // start time
			fmiBoolean toleranceControlled = fmiFalse;
			int nSteps = 0;
			FILE* file;

			fmiValueReference vr;			// add it to get value reference for variables

			//Note: User defined references
			//Begin------------------------------------------------------------------
			fmiValueReference vru[2], vry[2]; // value references for two input and two output variables 
			//End--------------------------------------------------------------------

			ScalarVariable** vars = fmu->modelDescription->modelVariables;		// add it to get variables
			int k;							// add a counter for variables
			fmiReal ru1, ru2, ry, ry1, ry2;	// add real variables for input and output
			fmiInteger ix, iy;				// add integer variables for input and output
			fmiBoolean bx, by;				// add boolean variables for input and output
			fmiString sx, sy;				// Zuo: add string variables for input and output
			fmiStatus status;				// Zuo: add stauus for fmi

			printDebug("Instantiate the fmu\n");

			// instantiate the fmu
			md = fmu->modelDescription;
			guid = getString(md, att_guid);
			printfDebug("Got GUID = %s!\n", guid);	
			callbacks.logger = fmuLogger;

			callbacks.allocateMemory = calloc;
			callbacks.freeMemory = free;
			printDebug("Got callbacks!\n");
			printfDebug("Model Identifer is %s\n", getModelIdentifier(md));
 			c = fmu->instantiateSlave(getModelIdentifier(md), guid, "Model1", "", 10, fmiFalse, fmiFalse, callbacks, loggingOn);
			if (!c) {
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
			time = t0;

			printDebug("start to initialize fmu!\n");	   
			fmiFlag =  fmu->initializeSlave(c, t0, fmiTrue, timeEnd_);	
			printDebug("Initialized fmu!\n");
			if (fmiFlag > fmiWarning) {
			printError("could not initialize model");
			return 1;
			}
 
			// Output solution for time t0 
			outputRow(fmu, c, t0, file, separator); // output initla value of fmu 

			///////////////////////////////////////////////////////////////////////////// 
			// Get value references for input and output varibles
			// Note: User needs to specify the name of variables for their own fmus
			//Begin------------------------------------------------------------------
			vru[0] = getValueReference(getVariableByName(md, "u1"));
			vru[1] = getValueReference(getVariableByName(md, "u2"));
			vry[0] = getValueReference(getVariableByName(md, "y1"));
			vry[1] = getValueReference(getVariableByName(md, "y2"));
			  //End--------------------------------------------------------------------
  
			  printDebug("Enter in simulation loop\n");	

			// enter the simulation loop
			while (time < timeEnd_) {


			if (loggingOn) printf("Step %d to t=%.4f\n", nSteps, time);		  

			///////////////////////////////////////////////////////////////////////////
			// Step 1: get values of output variables	from slaves
			for (k=0; vars[k]; k++) {
				ScalarVariable* sv = vars[k];
				if (getAlias(sv)!=enu_noAlias) continue;
				if (getCausality(sv) != enu_output) continue; // only get output variable
				vr = getValueReference(sv);

				switch (sv->typeSpec->type){
				case elm_Real:
					fmu->getReal(c, &vr, 1, &ry); 
					break;
				case elm_Integer:
					fmu->getInteger(c, &vr, 1, &iy);  
					break;
				case elm_Boolean:
					fmu->getBoolean(c, &vr, 1, &by);
					break;
				case elm_String:
					fmu->getString(c, &vr, 1, &sy);
					break;
				}


				// Allocate values to cooresponding varibles on master program
				// Note: User needs to specify the output variables for their own fmu 
				//Begin------------------------------------------------------------------
				if (vr == vry[0]) ry1 = ry;
				else if(vr == vry[1]) ry2 = ry;
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
			for (k=0; vars[k]; k++) {
				ScalarVariable* sv = vars[k];
				if (getAlias(sv)!=enu_noAlias) continue;
				if (getCausality(sv) != enu_input) continue; // only set input variable
				vr = getValueReference(sv);
				// Note: User can adjust the settings for input variables
				//Begin------------------------------------------------------------------
				switch (sv->typeSpec->type){
					case elm_Real:

					if(vr == vru[0]) {
						fmu->setReal(c, &vr, 1, &ru1); 				
						printDebug("Set u1\n");
					}
					else if (vr == vru[1]) {
						fmu->setReal(c, &vr, 1, &ru2);
						printDebug("Set u2\n");
					}
					else
						printf("Warning: no data given for input variable\n");
					break;
					case elm_Integer:
					fmu->setInteger(c, &vr, 1, &ix);  
					break;
					case elm_Boolean:
					fmu->setBoolean(c, &vr, 1, &bx);
					break;
					case elm_String:
					fmu->setString(c, &vr, 1, &sx);
					break;        
				}
				//End--------------------------------------------------------------------        
			} 
    
			// Advance to next time step
			status = fmu->doStep(c, time, h, fmiTrue);  
			// Terminate this row
			fprintf(file, "\n");      
   
			time = min(time+h, timeEnd_);
			outputRow(fmu, c, time, file, separator); // output values for this step
			nSteps++;
   
			} // end of while  

			  // Cleanup
			  fclose(file);

			  // Print simulation summary 
			  if (loggingOn) printf("Step %d to t=%.4f\n", nSteps, time);		
			  printf("Simulation from %g to %g terminated successful\n", t0, timeEnd_);
			  printf("  steps ............ %d\n", nSteps);
			  printf("  fixed step size .. %g\n", h);

			  return 0; // success
		}






    }





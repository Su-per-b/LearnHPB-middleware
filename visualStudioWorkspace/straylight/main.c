///////////////////////////////////////////////////////
/// \file   main.c
///
/// \brief  demo program of fmu for co-simulation.
///
/// \author Wangda Zuo,
///         Simulation Research Group, 
///         LBNL,
///         WZuo@lbl.gov
///
/// \date   2011-10-10
///
/// \version $Id: main.c 55724 2011-10-16 17:51:58Z wzuo $
///
/// This file is based on main.c that is copyrighted by
/// QTronic GmbH and that is distributed under the BSD license. 
/// The original file, its copyright notice and its license can 
/// be found in ../3rdParty
///
/// The file has been modified for use with the FMU standard for 
/// co-simulation. The original file was developed for model exchange.
/// The original file used 0 as indicator for failure and 
/// 1 as indicator for success.  
/// The new file uses 0 as indicator for success according to STL.
///
///////////////////////////////////////////////////////
/* ------------------------------------------------------------------------- 
 * main.c
 * Implements simulation of a single FMU instance using the forward Euler
 * method for numerical integration.
 * Command syntax: see printHelp()
 * Simulates the given FMU from t = 0 .. tEnd with fixed step size h and 
 * writes the computed solution to file 'result.csv'.
 * The CSV file (comma-separated values) may e.g. be plotted using 
 * OpenOffice Calc or Microsoft Excel. 
 * This progamm demonstrates basic use of an FMU.
 * Real applications may use advanced numerical solvers instead, means to 
 * exactly locate state events in time, graphical plotting utilities, support 
 * for coexecution of many FMUs, stepping and debug support, user control
 * of parameter and start values etc. 
 * All this is missing here.
 *
 * Revision history
 *  07.02.2010 initial version released in FMU SDK 1.0
 *  05.03.2010 bug fix: removed strerror(GetLastError()) from error messages
 *     
 * Free libraries and tools used to implement this simulator:
 *  - eXpat 2.0.1 XML parser, see http://expat.sourceforge.net
 *  - 7z.exe 4.57 zip and unzip tool, see http://www.7-zip.org
 * Copyright 2010 QTronic GmbH. All rights reserved. 
 * -------------------------------------------------------------------------
 */


#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "main.h"
#include "../common/expat.h"
#include "../common/util.h"

#define BUFSIZE 4096
#define XML_FILE  "modelDescription.xml"
#define DLL_DIR   ""
#define RESULT_FILE "result.csv"

static FMU fmu; // the fmu to simulate

///////////////////////////////////////////////////////////////////////////////
/// Get address of specific function from specific dll
///
///\param fmu Name of dll file.
///\param funnam Function name .
///\return Address of the specific function.
//////////////////////////////////////////////////////////////////////////////
static void* getAdr(FMU *fmu, const char* funNam){
    char name[BUFSIZE];
    void* fp;
    sprintf(name, "%s_%s", getModelIdentifier(fmu->modelDescription), funNam); // Zuo: adding the model name in front of funciton name

    fp = GetProcAddress(fmu->dllHandle, name);
    if (!fp) {
        printfError("Function %s not found in dll\n", name);        
    }
    return fp;
}

///////////////////////////////////////////////////////////////////////////////
/// Load the given dll and set function pointers in fmu.
/// It changes the names of the standard FMI functions by adding the modlei identifer
/// and links the new functions with QTronic's FMU structure.
///
///\param dllPat Path of the dll file.
///\param fmu Name of FMU.
///\return 0 if there is no error occurred.
///////////////////////////////////////////////////////////////////////////////
static int loadDll(const char* dllPat, FMU *fmu) {
  
	wchar_t * dllPatW;
	int len;
	int wlen;
	HINSTANCE h;

	len = strlen(dllPat) + 1; // I had to add an additional character I believe is is
							//an end-of-line character
	dllPatW = (wchar_t *) calloc(sizeof(wchar_t), len);


	if (dllPatW == NULL){
		printfError("Failed to allocate memory for wText\n", dllPat);
		return 1;
	}

	wlen = MultiByteToWideChar(  0, 0, dllPat, -1, dllPatW,len);


  h = LoadLibrary(dllPatW);
  free(dllPatW);

  if(!h) {
    printfError("Can not load %s\n", dllPat);
    return 1;
  }

  fmu->dllHandle = h;
  fmu->getVersion              = (fGetVersion)         getAdr(fmu, "fmiGetVersion");
  fmu->instantiateSlave        = (fInstantiateSlave)   getAdr(fmu, "fmiInstantiateSlave");
  fmu->freeSlaveInstance       = (fFreeSlaveInstance)  getAdr(fmu, "fmiFreeSlaveInstance");
  fmu->setDebugLogging         = (fSetDebugLogging)    getAdr(fmu, "fmiSetDebugLogging");
  fmu->setReal                 = (fSetReal)            getAdr(fmu, "fmiSetReal");
  fmu->setInteger              = (fSetInteger)         getAdr(fmu, "fmiSetInteger");
  fmu->setBoolean              = (fSetBoolean)         getAdr(fmu, "fmiSetBoolean");
  fmu->setString               = (fSetString)          getAdr(fmu, "fmiSetString");
  fmu->initializeSlave         = (fInitializeSlave)    getAdr(fmu, "fmiInitializeSlave");
  fmu->getReal                 = (fGetReal)            getAdr(fmu, "fmiGetReal");
  fmu->getInteger              = (fGetInteger)         getAdr(fmu, "fmiGetInteger");
  fmu->getBoolean              = (fGetBoolean)         getAdr(fmu, "fmiGetBoolean");
  fmu->getString               = (fGetString)          getAdr(fmu, "fmiGetString");
  fmu->doStep				   = (fDoStep)			   getAdr(fmu, "fmiDoStep");

  return 0;
}

///////////////////////////////////////////////////////////////////////////////
/// Output time and all non-alias variables in CSV format.
/// If separator is ',', columns are separated by ',' and '.' is used for floating-point numbers.
/// Otherwise, the given separator (e.g. ';' or '\t') is to separate columns, and ',' is used for 
/// floating-point numbers.
///
///\param fmu FMU.
///\param c FMI component.
///\param time Time when data is outputed.
///\param separator Separator in file.
///\param header Indicator of row head.
///////////////////////////////////////////////////////////////////////////////
static void outputRow(FMU *fmu, fmiComponent c, double time, FILE* file, char separator, boolean header) {
  int k;
  fmiReal r;
  fmiInteger i;
  fmiBoolean b;
  fmiString s;
  fmiValueReference vr;				
  ScalarVariable** vars = fmu->modelDescription->modelVariables;
  char buffer[32];
  
  // Print first column
  if (header) 
    fprintf(file, "time"); 
  else {
    if (separator==',') 
      fprintf(file, "%.4f", time);				
    else {
      // Separator is e.g. ';' or '\t'
      doubleToCommaString(buffer, time);
      fprintf(file, "%s", buffer);       
    }
  }
    
  // Print all other columns
  for (k=0; vars[k]; k++) {
    ScalarVariable* sv = vars[k];
    if (getAlias(sv)!=enu_noAlias) continue;
    if (header) {
      // Output names only
      fprintf(file, "%c%s", separator, getName(sv));
    }
    else {
      // Output values
      vr = getValueReference(sv);
      switch (sv->typeSpec->type){
        case elm_Real:
          fmu->getReal(c, &vr, 1, &r);
          if (separator==',') 
            fprintf(file, ",%.4f", r);				
        else {
          // Separator is e.g. ';' or '\t'
          doubleToCommaString(buffer, r);
          fprintf(file, "%c%s", separator, buffer);       
          }
          break;
        case elm_Integer:
          fmu->getInteger(c, &vr, 1, &i);
          fprintf(file, "%c%d", separator, i);
          break;
        case elm_Boolean:
          fmu->getBoolean(c, &vr, 1, &b);
          fprintf(file, "%c%d", separator, b);
          break;
        case elm_String:
          fmu->getString(c, &vr, 1, &s);
          fprintf(file, "%c%s", separator, s);
          break;
      }
    }
  } // for
    
  // Terminate this row
  fprintf(file, "\n"); 
}

///////////////////////////////////////////////////////////////////////////////
/// Translate FMI status to string variable
///
///\param status FMI status
///\return Corresponding string variable if it is found.
///////////////////////////////////////////////////////////////////////////////
static const char* fmiStatusToString(fmiStatus status){
  switch (status){
    case fmiOK:      return "ok";
    case fmiWarning: return "warning";
    case fmiDiscard: return "discard";
    case fmiError:   return "error";
	  case fmiPending: return "pending";	
    default:         return "?";
  }
}

///////////////////////////////////////////////////////////////////////////////
/// Search a fmu for the given variable.
///
///\param fmu FMU.
///\param type Type of FMU variable.
///\param vr FMI value reference.
///\return NULL if not found or vr = fmiUndefinedValueReference
///////////////////////////////////////////////////////////////////////////////
static ScalarVariable* getSV(FMU* fmu, char type, fmiValueReference vr) {
  int i;
  Elm tp;
  ScalarVariable** vars = fmu->modelDescription->modelVariables;
  if (vr==fmiUndefinedValueReference) return NULL;
  switch (type) {
    case 'r': tp = elm_Real;    break;
    case 'i': tp = elm_Integer; break;
    case 'b': tp = elm_Boolean; break;
    case 's': tp = elm_String;  break;                
  }
  for (i=0; vars[i]; i++) {
    ScalarVariable* sv = vars[i];
    if (vr==getValueReference(sv) && tp==sv->typeSpec->type) 
      return sv;
  }
  return NULL;
}

///////////////////////////////////////////////////////////////////////////////
/// Replace reference information in message.
/// E.g. #r1365# by variable name and ## by # in message
/// copies the result to buffer
///
///\param msg 
///\param buffer
///\param nBuffer
///\param fmu FMU
///////////////////////////////////////////////////////////////////////////////
static void replaceRefsInMessage(const char* msg, char* buffer, int nBuffer, FMU* fmu){
  int i=0; // position in msg
  int k=0; // position in buffer
  int n;
  char c = msg[i];
  while (c!='\0' && k < nBuffer) {
    if (c!='#') {
      buffer[k++]=c;
      i++;
      c = msg[i];
    }
    else {
      char* end = (char*) strchr(msg+i+1, '#');
      if (!end) {
        printf("unmatched '#' in '%s'\n", msg);
        buffer[k++]='#';
        break;
      }
      n = end - (msg+i);
      if (n==1) {
        // ## detected, output #
        buffer[k++]='#';
        i += 2;
        c = msg[i];
      }
      else {
        char type = msg[i+1]; // one of ribs
        fmiValueReference vr;
        int nvr = sscanf(msg+i+2, "%u", &vr);
        if (nvr==1) {
          // vr of type detected, e.g. #r12#
          ScalarVariable* sv = getSV(fmu, type, vr);
          const char* name = sv ? getName(sv) : "?";
          sprintf(buffer+k, "%s", name);
          k += strlen(name);
          i += (n+1);
          c = msg[i]; 
        }
        else {
          // could not parse the number
          printf("illegal value reference at position %d in '%s'\n", i+2, msg);
          buffer[k++]='#';
          break;
        }
      }
    }
  } // while
  buffer[k] = '\0';
}

#define MAX_MSG_SIZE 1000
///////////////////////////////////////////////////////////////////////////////
/// FMU logger
///
///\param c FMI component.
///\param instanceName FMI string.
///\param status FMI status.
///\param category FMI string. 
///\param message Message to be recorded.
///////////////////////////////////////////////////////////////////////////////
static void fmuLogger(fmiComponent c, fmiString instanceName, fmiStatus status,
               fmiString category, fmiString message, ...) {
  char msg[MAX_MSG_SIZE];
  char* copy;
  va_list argp;

  // Replace C format strings
	va_start(argp, message);
  vsprintf(msg, message, argp);

  // Replace e.g. ## and #r12#  
  copy = strdup(msg);
  replaceRefsInMessage(copy, msg, MAX_MSG_SIZE, &fmu);
  free(copy);
  
  // Print the final message
  if (!instanceName) instanceName = "?";
  if (!category) category = "?";
  printf("%s %s (%s): %s\n", fmiStatusToString(status), instanceName, category, msg);
}

///////////////////////////////////////////////////////////////////////////////
/// Simulate the given FMU using the forward euler method.
/// Time events are processed by reducing step size to exactly hit tNext.
/// State events are checked and fired only at the end of an Euler step. 
/// The simulator may therefore miss state events and fires state events typically too late.
///
///\param fmu FMU.
///\param tEnd Ending time of simulation.
///\param h Tiem step size.
///\param loggingOn Controller for logging.
///\param separator Separator character.
///\return 0 if there is no error occurred.
///////////////////////////////////////////////////////////////////////////////
static int simulate(FMU* fmu, double tEnd, double h, fmiBoolean loggingOn, char separator) {
		


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
	fmiFlag =  fmu->initializeSlave(c, t0, fmiTrue, tEnd);	
	printDebug("Initialized fmu!\n");
  if (fmiFlag > fmiWarning) {
    printError("could not initialize model");
    return 1;
  }
 
  // Output solution for time t0 
  outputRow(fmu, c, t0, file, separator, TRUE);  // output column names
  outputRow(fmu, c, t0, file, separator, FALSE); // output initla value of fmu 

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
  while (time < tEnd) {
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
   
    time = min(time+h, tEnd);
    outputRow(fmu, c, time, file, separator, FALSE); // output values for this step
    nSteps++;
   
  } // end of while  

  // Cleanup
  fclose(file);

  // Print simulation summary 
  if (loggingOn) printf("Step %d to t=%.4f\n", nSteps, time);		
  printf("Simulation from %g to %g terminated successful\n", t0, tEnd);
  printf("  steps ............ %d\n", nSteps);
  printf("  fixed step size .. %g\n", h);

  return 0; // success
}

///////////////////////////////////////////////////////////////////////////////
/// Print help information
///
///\param fmusim Name of executable program
///////////////////////////////////////////////////////////////////////////////
static void printHelp(const char* fmusim) {
  printf("This is a demo program for using FMU for co-simulation. "); 
  printf("The settings of the master program is designed to do co-simulation with DoubleInputDoubleOutput.fmu only. ");
  printf("Users can change the function compute() for cosimulation with their own fmus\n"); 
  printf("command syntax: %s <model.fmu> <tEnd> <h> <loggingOn> <csv separator>\n", fmusim);
  printf("   <model.fmu> .... path to FMU, relative to current dir or absolute, required. It is ../fmus/DoubleInputDoubleOutput/DoubleInputDoubleOutput.fmu for the demo\n");
  printf("   <tEnd> ......... end  time of simulation, optional, defaults to 1.0 sec\n");
  printf("   <h> ............ step size of simulation, optional, defaults to 0.1 sec\n");
  printf("   <loggingOn> .... 1 to activate logging,   optional, defaults to 0\n");
  printf("   <csv separator>. column separator char in csv file, optional, defaults to ';'\n");
}

///////////////////////////////////////////////////////////////////////////////
/// main routine of the demo code
///
///\param argc Number of arguments
///\param argv Arguments
///\return 0 if no error occurred
///////////////////////////////////////////////////////////////////////////////
int main(int argc, char *argv[]) {
    const char* fmuFilNam;
    char* tmpPat;
    char* xmlPat;
    char* dllPat;
    
    // define default argument values
    double tEnd = 1.0;
    double h=0.1;
    int loggingOn = 0;
    char csv_separator = ',';  
   
    // parse command line arguments
    if (argc>1) {
        fmuFilNam = argv[1];
        
        if(!strcmp(fmuFilNam, "-h") | !strcmp(fmuFilNam, "--help")) {
          printHelp(argv[0]);
          return 0;
        }

        if(!strstr(fmuFilNam,"DoubleInputDoubleOutput.fmu")) {
          printf("Sorry, this demo only works with the FMU file DoubleInputDoubleOutput.fmu");
          return 0;
        }
    }
    else {
        printError("No fmu file\n");
        printHelp(argv[0]);
        exit(EXIT_FAILURE);
    }
    if (argc>2) {
        if (sscanf(argv[2],"%lf", &tEnd) != 1) {
            printfError("The given end time (%s) is not a number\n", argv[2]);
            exit(EXIT_FAILURE);
        }
    }
    if (argc>3) {
        if (sscanf(argv[3],"%lf", &h) != 1) {
            printfError("The given stepsize (%s) is not a number\n", argv[3]);
            exit(EXIT_FAILURE);
        }
    }
    if (argc>4) {
        if (sscanf(argv[4],"%d", &loggingOn) != 1 || loggingOn<0 || loggingOn>1) {
            printfError("The given logging flag (%s) is not boolean\n", argv[4]);
            exit(EXIT_FAILURE);
        }
        if(loggingOn) setDebug();
    }
    if (argc>5) {
        if (strlen(argv[5]) != 1) {
            printfError("The given CSV separator char (%s) is not valid\n", argv[5]);
            exit(EXIT_FAILURE);
        }
        csv_separator = argv[5][0];
    }
    if (argc>6) {
        printf("warning: Ignoring %d additional arguments: %s ..\n", argc-6, argv[6]);
        printHelp(argv[0]);
    }

    // Get absolute path to FMU, NULL if not found  
    tmpPat = getTmpPath(fmuFilNam, strlen(fmuFilNam)-4);
    if(tmpPat==NULL){
      printError("Cannot allocate temporary path\n");
      exit(EXIT_FAILURE);
    }

    // Unzip the FMU to the tmpPat directory
    if (unpack(fmuFilNam, tmpPat)) {  
        printfError("Fail to unpack fmu \"%s\"\n", fmuFilNam);
        exit(EXIT_FAILURE);
    }

    printDebug("parse tmpPat\\modelDescription.xml\n");
    xmlPat = (char *) calloc(sizeof(char), strlen(tmpPat) + strlen(XML_FILE) + 1);
    sprintf(xmlPat, "%s%s", tmpPat, XML_FILE);

    // Parse only parses the model description and store in structure fmu.modelDescription
    fmu.modelDescription = parse(xmlPat); 
    free(xmlPat);
    if (!fmu.modelDescription) exit(EXIT_FAILURE);

    // Allocate the memory for dllPat
    dllPat = (char *) calloc(sizeof(char), strlen(tmpPat) + strlen(DLL_DIR) 
            + strlen( getModelIdentifier(fmu.modelDescription)) +  strlen(".dll") + 1); 
    sprintf(dllPat,"%s%s%s.dll", tmpPat, DLL_DIR, getModelIdentifier(fmu.modelDescription)); 

    // Load the FMU dll
    if (loadDll(dllPat, &fmu)) exit(EXIT_FAILURE); 
	  printfDebug("Loaded \"%s\"\n", dllPat); 

    free(dllPat);
    free(tmpPat);

    // Run the simulation
    printf("FMU Simulator: run '%s' from t=0..%g with step size h=%g, loggingOn=%d, csv separator='%c'\n", 
            fmuFilNam, tEnd, h, loggingOn, csv_separator);
    if (simulate(&fmu, tEnd, h, loggingOn, csv_separator)){
      printError("Simulation failed\n");
      exit(EXIT_FAILURE);
    }

    printf("CSV file '%s' written", RESULT_FILE);

    // Release FMU 
    FreeLibrary(fmu.dllHandle);
    freeElement(fmu.modelDescription);
    return EXIT_SUCCESS;
}

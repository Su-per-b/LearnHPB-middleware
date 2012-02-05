///////////////////////////////////////////////////////
/// \file   parser.c
///
/// \brief  Methods for master program that interacts
///         with an FMU for co-simulation
///
/// \author Wangda Zuo, Thierry S. Nouidui, Michael Wetter
///         Simulation Research Group,
///         LBNL,
///         WZuo@lbl.gov
///
/// \date   2011-11-02
///
///
/// This file parse the FMU for co-simulation and translate related information into Energyplus input file (.idf)
///
///
///
///////////////////////////////////////////////////////
#include "parser.h"
#include "string.h"
#include "../common/util.h"


#define XML_FILE  "modelDescription.xml"

FMU fmu;        // FMU to parse

///////////////////////////////////////////////////////////////////////////
/// Call the parser and save the information in fmu.modelDescription
///
///\param fmuFilNam The name of fmu file
///\param tmpPat Temporary path of the fmu file
///\return 0 if no error occurred
///////////////////////////////////////////////////////////////////////////
int callparser(const char* fmuFilNam, const char* tmpPat){

  char* xmlPat;

  // Get the model description
  xmlPat = (char*) calloc(sizeof(char), strlen(fmuFilNam) + strlen(XML_FILE) + 1 + 10);

    sprintf(xmlPat, "%s%s%s", tmpPat, PATH_SEP, XML_FILE);
  fmu.modelDescription = parse(xmlPat);

  if (!fmu.modelDescription){
    printfError("Fail to parse xml file \"%s\".\n", xmlPat);
    free(xmlPat);
    return -1;
  }
  free(xmlPat);
  printfDebug("Model description is \"%s\".", getModelIdentifier(fmu.modelDescription));

  // Clean up memory
  return 0;
}



//////////////////////////////////////////////////////////////////////
/// Print help information
/////////////////////////////////////////////////////////////////////
void help(){

  printf("NAME\n");
  printf("\tparser - parse xml file\n\n");
  printf("SYNOPSIS\n");
  printf("\tparser OPTION...FMUFILE...[-name objNam]\n\n");
  printf("DESCRIPION\n");
  printf("\tThe parser parses informaiton in modelDescription.xml file in functional mockup unit for co-simulation.\n\n");
  printf("OPTION\n");
  printf("\t-d, --delete\n");
  printf("\t\tdelete the unpacked fmu folder\n");
  printf("\t-h, --help\n");
  printf("\t\tprint help information\n");
  printf("\t-p, --printidf\n");
  printf("\t\tprint temporary IDF file for Energyplus\n");
  printf("\t-u, --unpack\n");
  printf("\t\tunpack the FMU\n");
  printf("\t-v, --verbose\n");
  printf("\t\tprint debug information. It is the only option that can combine with other options.\n");
  printf("OTHERS\n");
  printf("\t-n, --name\n");
  printf("\t\textract the fmu in folder objNam. The default folder name is fmu's name without the \".fmu\".\n\n");
  printf("EXAMPLE\n");
  printf("\tTo generate a temporary IDF file from a fmu named havc.fmu in Linux\n"); 
  printf("\t\tparser -p hvac.fmu\n\n");
  printf("\tTo unpack the fmu havc.fmu in a folder called MyFMU in linux\n");
  printf("\t\tparser -u havc.fmu -n MyFMU\n");  
  printf("AUTHOR\n");
  printf("\t Wangda Zuo, Thierry S. Nouidui, Michael Wetter @ Lawrence Berkeley National Laboratory\n");
}


//////////////////////////////////////////////////////////////////////////////////
/// Main routine of parser
///
///\param argc Number of arguments
///\param argv Arguments
///\return 0 if no error occurred
//////////////////////////////////////////////////////////////////////////////////
int main(int argc, char* argv[]){
  int i;
  const char *fmuFilNam;
  char *tmpPat;
  char *objNam;
  option opt;
  int length, nam = 0;
  int optNum = 0;

  fmuFilNam = NULL;

  // Read command line arguments
  for(i=1; i < argc; i++) {
    if (strcmp(argv[i], "--delete") == 0 || strcmp(argv[i], "-d") == 0) {         
      opt = opt_delete;
      optNum++;
      printDebug("Get option: delete");
    }
    else if (strcmp(argv[i], "--printidf") == 0 || strcmp(argv[i], "-p") == 0) { 
      opt = opt_printidf;
      optNum++;
      printDebug("Get option: printidf");
    }
    else if (strcmp(argv[i], "--unpack") == 0 || strcmp(argv[i], "-u") == 0) {   
      opt = opt_unpack;
      optNum++;
      printDebug("Get option: unpack");
    }
    else if (strcmp(argv[i], "--help") == 0 || strcmp(argv[i], "-h") == 0) {     
      opt = opt_help;
      optNum++;
      printDebug("Get option: help");
    }
    else if (strcmp(argv[i], "--verbose") == 0 || strcmp(argv[i], "-v") == 0) {  
      setDebug();
      printDebug("Get option: verbose");
    }
    else if (strcmp(argv[i], "--name") == 0 || strcmp(argv[i], "-n") == 0) {
      i++; nam = 1; 
      if(argv[i] != NULL)
        objNam = argv[i];
      else {
        printError("with option -n, objNam is needed.");
        return -1;
      }
      printDebug("Get option: name");
    }
    else if (strstr(argv[i], ".fmu") != NULL)
      fmuFilNam = argv[i];
    else
      printf("Warning: Ingore unrecognized input \"%s\"\n", argv[i]);   
  }

  if (optNum == 0) {
    if (WINDOWS) {
	    printError("Missing option: parser.exe option [argument]");
      printf("For help, use: parser.exe -h \n");
    }
    else {
	    printError("Missing option: parser option [argument]");
      printf("For help, use: parser -h \n");
    }
	    return -1;
  }  
  
  if (optNum > 1) {
    printError("Can not use more than 1 option at the same time except \"-u\".");
    if (WINDOWS) {	   
      printf("For help, use: parser.exe -h \n");
    }
    else {
      printf("For help, use: parser -h \n");
    }
	    return -1;
  }

  if(fmuFilNam == NULL && opt != opt_help) {
    switch (opt) {
      case opt_printidf: 
      case opt_unpack: 
        printError("No FMU file name is given.\n"); 
        break;
      case opt_delete:
        printError("No FMU folder is given.\n"); break;
     }

    if (WINDOWS) {
	    printf("Correct usage: parser.exe option [argument] \n");
      printf("For help information, use: parser.exe -h \n");
    }
    else {
	    printf("Correct usage: parser option [argument] \n");
      printf("For help information, use: parser -h \n");
    }

    return -1;
  }
  else    
    printfDebug("FMU file/folder name is \"%s\".\n", fmuFilNam);



  // Check command line arguments
  switch (opt){
	  case opt_printidf:
    case opt_unpack:		
      if(nam == 0){
  		  length = strlen(fmuFilNam)-4;	
        objNam = malloc(length*sizeof(char));	
        if(strncpy(objNam, fmuFilNam, length)==NULL) {
          printError("Can not get name for new folder.\n");
          return -1;
        }
      }
      else 
        length = strlen(objNam);

		  break;
	  case opt_delete:	      
      length = strlen(fmuFilNam);	break;      
    case opt_help:
      help();
      return 0;
	  default:
		  printError("Missing options: --delete | -d | --printidf | -p | --unpack | -u| --verbose | -v \n");
		  return -1;    
  } 

  printDebug("Get length of file name");
 
  // Define the temporary folder
  tmpPat = getTmpPath(objNam, length);    
  if(tmpPat == NULL){
     printError("Fail to allocate memory for temp dir.\n");
	    return -1;
    }
 
  printfDebug("Received tmpPat \"%s\".\n", tmpPat);

  // Act according to the option
  switch (opt)
  {
	  case opt_delete:      
	    if(delete(tmpPat)!= 0){
		    printfError("Fail to delete \"%s\".\n", tmpPat);
        return -1;
	    }
	    else
		    printfDebug("Successfully deleted the temporary folder \"%s\".\n", tmpPat);      
	    break;
	  case opt_unpack:
	  case opt_printidf:
      // Unpack: Common part for unpack and printidf
	    if(unpack(fmuFilNam, tmpPat)!= 0){
        printfError("Fail to unpack \"%s\".\n", fmuFilNam);
        return -1;
	    }
	    else
		    printDebug("Successfully unpacked the fmu.\n");

      // Parser: Additional part for printidf
	    if (opt == opt_printidf) {    
        if ( callparser(fmuFilNam, tmpPat) != 0 ){
	        printfError("Fail to print the idf file from \"%s\".", fmuFilNam);
          return -1;
        }
        else printDebug("Successfully printed the idf file.\n");
      }  
    break;    
  }

  return 0;
}

#pragma once

#include "stdafx.h"


typedef struct ScalarValueStruct_ {
	  int idx;
	  const char * string;
} ScalarValueStruct;



typedef struct Element2_ {
  //  Elm type;          // element type 
   // const char** attributes; // null or n attribute value strings
    int n;             // size of attributes, even number
} Element2;

	//  Element2* element;
typedef struct ScalarVariableStruct_ {
	  const char * name;
	  int idx;
	  Enu causality;
	  const char* description;
	  int type;

	  Element2  * element;
} ScalarVariableStruct;





typedef struct ResultStruct_ {
	  double time;
	  const char * string;
	  int scalarValueCount;
	  ScalarValueStruct * scalarValueStruct;
} ResultStruct;


typedef struct MessageStruct_ {
	  const char * msgText;
	  int messageType;
} MessageStruct;



typedef struct DefaultExperimentStruct_ {
	double startTime;
	double stopTime;
	double tolerance;

} DefaultExperimentStruct;


typedef struct MetaDataStruct_ {
	DefaultExperimentStruct * defaultExperimentStruct;
	double stepDelta;
} MetaDataStruct;






#pragma once

#include "stdafx.h"


struct ScalarVariableStruct {
	  const char * name;
	  int idx;
	  Enu causality;
	  Elm type;
	  const char* description;
	  //const char* attributes;
};


typedef struct ScalarValueStruct_ {
	  int idx;
	  const char * string;
} ScalarValueStruct;



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
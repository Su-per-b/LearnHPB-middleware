#pragma once

#include "stdafx.h"


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








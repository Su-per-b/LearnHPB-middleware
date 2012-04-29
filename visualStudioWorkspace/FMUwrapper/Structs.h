#pragma once

#include "stdafx.h"


struct ScalarVariableMeta {
	  const char * name;
	  int idx;
	  Enu causality;
};


typedef struct ResultItemPrimitiveStruct_ {
	  int idx;
	  const char * string;
} ResultItemPrimitiveStruct;



typedef struct ResultItemStruct_ {
	  double time;
	  const char * string;
	  int primitiveCount;
	  ResultItemPrimitiveStruct * primitive;
} ResultItemStruct;



typedef struct MessageStruct_ {
	  const char * msgText;
	  int messageType;
} MessageStruct;
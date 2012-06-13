#pragma once

#include "stdafx.h"

typedef struct Element2_ {
  //  Elm type;          // element type 
   // const char** attributes; // null or n attribute value strings
    int n;             // size of attributes, even number
} Element2;


typedef struct ScalarVariableStruct_ {
	  const char * name;
	  int idx;
	  Enu causality;
	  const char* description;
	  int type;

	  Element2  * element;
} ScalarVariableStruct;


using namespace std;


namespace Straylight
{
	  class ScalarVariableFactory
	{


	public:
		 static ScalarVariableStruct *  make(FMU* fmuPointer, int i);

	 };
};


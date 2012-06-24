#pragma once

#include "stdafx.h"




typedef struct {
	  const char * msgText;
	  int messageType;
} MessageStruct;



typedef struct {
	double start;
	double nominal;
	double min;
	double max;

	int startValueStatus;
	int nominalValueStatus;
	int minValueStatus;
	int maxValueStatus;

} TypeSpecReal;



typedef struct {
    Elm type;          // element type 
//   const char** attributes; // null or n attribute value strings
    int n;             // size of attributes, even number
} TypeSpec;


typedef struct {
	  const char * name;
	  int idx;
	  Enu causality;
	  const char* description;
	  TypeSpec  * typeSpec;
	 unsigned int valueReference;
} ScalarVariableStruct;


typedef struct {
	  const char * name;
	  int idx;
	  Enu causality;
	  const char* description;
	 TypeSpecReal  * typeSpecReal;
	 unsigned int valueReference;
} ScalarVariableRealStruct;





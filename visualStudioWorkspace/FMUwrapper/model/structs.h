#pragma once

#include "..\stdafx.h"


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

	const char * declaredType;
} TypeSpecReal;

typedef struct {
	int start;
	int nominal;
	int min;
	int max;
	int fixed;
	
	int startValueStatus;
	int nominalValueStatus;
	int minValueStatus;
	int maxValueStatus;
	int fixedValueStatus;

	const char * declaredType;
} TypeSpecInteger;


typedef struct {
	char start;
	char fixed;

	int startValueStatus;
	int fixedValueStatus;

	const char * declaredType;
} TypeSpecBoolean;





typedef struct {
	const char * name;
	const char * description;
} EnumerationItem;


typedef struct {
	int min;
	int max;
	
	int minValueStatus;
	int maxValueStatus;


	EnumerationItem  * enumerationItemAry;
	const char * declaredType;

} TypeSpecEnumeration;





typedef struct {
	const char * start;
	const char * declaredType;

	int startValueStatus;
} TypeSpecString;



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
	unsigned int valueReference;

	TypeSpecReal  * typeSpecReal;

} ScalarVariableRealStruct;


typedef struct {
	const char * name;
	int idx;
	Enu causality;
	const char* description;
	unsigned int valueReference;

	TypeSpecBoolean  * typeSpecBoolean;
} ScalarVariableBooleanStruct;

typedef struct {
	const char * name;
	int idx;
	Enu causality;
	const char* description;
	unsigned int valueReference;

	TypeSpecInteger  * typeSpecInteger;
} ScalarVariableIntegerStruct;


typedef struct {
	const char * name;
	int idx;
	Enu causality;
	const char* description;
	unsigned int valueReference;

	TypeSpecEnumeration  * typeSpecEnumeration;
} ScalarVariableEnumerationStruct;


typedef struct {
	const char * name;
	int idx;
	Enu causality;
	const char* description;
	unsigned int valueReference;

	TypeSpecString  * typeSpecString;
} ScalarVariableStringStruct;


typedef struct {
	const char * name;
	const char * unit;
	int idx;
	double start;
	double nominal;
	double min;
	double max;
	int startValueStatus;
	int nominalValueStatus;
	int minValueStatus;
	int maxValueStatus;
} TypeDefinitionReal;


typedef struct {
	const char * name;
	const char * unit;
	int idx;

	char start;
	char fixed;

	int startValueStatus;
	int fixedValueStatus;
} TypeDefinitionBoolean;

typedef struct {
	const char * name;
	const char * unit;
	int idx;

	int start;
	int nominal;
	int min;
	int max;
	int startValueStatus;
	int nominalValueStatus;
	int minValueStatus;
	int maxValueStatus;

} TypeDefinitionInteger;


typedef struct {
	const char * name;
	int idx;

	int min;
	int max;
	int minValueStatus;
	int maxValueStatus;

	EnumerationItem * itemArray;
	int itemArrayLength;
} TypeDefinitionEnumeration;



typedef struct {
	const char * name;
	const char * unit;
	int idx;

} TypeDefinitionString;


typedef struct {

	ScalarVariableRealStruct * realValue;
	int realSize;
	ScalarVariableBooleanStruct * booleanValue;
	int booleanSize;
	ScalarVariableIntegerStruct * integerValue;
	int integerSize;
	ScalarVariableEnumerationStruct * enumerationValue;
	int enumerationSize;
	ScalarVariableStringStruct * stringValue;
	int stringSize;

} ScalarVariableCollectionStruct;


typedef struct {

	ScalarVariableCollectionStruct * input;
	ScalarVariableCollectionStruct * output;
	ScalarVariableCollectionStruct * internal;

} ScalarVariablesAllStruct;

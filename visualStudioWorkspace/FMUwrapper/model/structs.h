/*******************************************************//**
 * @file	model\structs.h
 *
 * Declares the structs class.
 *******************************************************/
#pragma once

#include "..\stdafx.h"



typedef struct {

	const char * name;
	const char * value;

} AttributeStruct;


typedef struct {

	AttributeStruct * attributeStructAry;
	int attributeStructSize;

} FMImodelAttributesStruct;




/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	/*******************************************************//**
	 * The message text.
	 *******************************************************/
	const char * msgText;
	int messageType;
} MessageStruct;

/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	/*******************************************************//**
	 * The start.
	 *******************************************************/
	double start;
	double nominal;

	/*******************************************************//**
	 * The minimum.
	 *******************************************************/
	double min;
	double max;

	const char * unit;

	/*******************************************************//**
	 * The start value status.
	 *******************************************************/
	int startValueStatus;
	int nominalValueStatus;

	/*******************************************************//**
	 * The minimum value status.
	 *******************************************************/
	int minValueStatus;
	int maxValueStatus;

	int unitValueStatus;


} TypeSpecReal;

/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	int start;

	/*******************************************************//**
	 * The nominal.
	 *******************************************************/
	int nominal;
	int min;

	/*******************************************************//**
	 * The maximum.
	 *******************************************************/
	int max;
	int fixed;

	/*******************************************************//**
	 * The start value status.
	 *******************************************************/
	int startValueStatus;
	int nominalValueStatus;

	/*******************************************************//**
	 * The minimum value status.
	 *******************************************************/
	int minValueStatus;
	int maxValueStatus;

	/*******************************************************//**
	 * The fixed value status.
	 *******************************************************/
	int fixedValueStatus;

	const char * declaredType;
} TypeSpecInteger;

/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	/*******************************************************//**
	 * The start.
	 *******************************************************/
	char start;
	char fixed;

	/*******************************************************//**
	 * The start value status.
	 *******************************************************/
	int startValueStatus;
	int fixedValueStatus;

	/*******************************************************//**
	 * Type of the declared.
	 *******************************************************/
	const char * declaredType;
} TypeSpecBoolean;

/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	/*******************************************************//**
	 * The name.
	 *******************************************************/
	const char * name;

	/*******************************************************//**
	 * The description.
	 *******************************************************/
	const char * description;
} EnumerationItem;

/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	int min;

	/*******************************************************//**
	 * The maximum.
	 *******************************************************/
	int max;

	int minValueStatus;

	/*******************************************************//**
	 * The maximum value status.
	 *******************************************************/
	int maxValueStatus;

	EnumerationItem  * enumerationItemAry;

	/*******************************************************//**
	 * Type of the declared.
	 *******************************************************/
	const char * declaredType;
} TypeSpecEnumeration;


/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	/*******************************************************//**
	 * The start.
	 *******************************************************/
	const char * start;

	/*******************************************************//**
	 * Type of the declared.
	 *******************************************************/
	const char * declaredType;

	int startValueStatus;
} TypeSpecString;


/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	/*******************************************************//**
	 * The name.
	 *******************************************************/
	const char * name;

	/*******************************************************//**
	 * The index.
	 *******************************************************/
	int idx;
	Enu causality;

	/*******************************************************//**
	 * The variability.
	 *******************************************************/
	Enu variability;
	const char* description;

	/*******************************************************//**
	 * Gets the value reference.
	 *
	 * @return	The value reference.
	 *******************************************************/
	unsigned int valueReference;


	TypeSpecReal  * typeSpecReal;
} ScalarVariableRealStruct;


/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	/*******************************************************//**
	 * The name.
	 *******************************************************/
	const char * name;
	int idx;

	/*******************************************************//**
	 * The causality.
	 *******************************************************/
	Enu causality;
	Enu variability;

	/*******************************************************//**
	 * The description.
	 *******************************************************/
	const char* description;
	unsigned int valueReference;

	/*******************************************************//**
	 * The type specifier boolean.
	 *******************************************************/
	TypeSpecBoolean  * typeSpecBoolean;
} ScalarVariableBooleanStruct;

/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	const char * name;

	/*******************************************************//**
	 * The index.
	 *******************************************************/
	int idx;
	Enu causality;

	/*******************************************************//**
	 * The variability.
	 *******************************************************/
	Enu variability;
	const char* description;

	/*******************************************************//**
	 * Gets the value reference.
	 *
	 * @return	The value reference.
	 *******************************************************/
	unsigned int valueReference;

	TypeSpecInteger  * typeSpecInteger;
} ScalarVariableIntegerStruct;

/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	/*******************************************************//**
	 * The name.
	 *******************************************************/
	const char * name;
	int idx;

	/*******************************************************//**
	 * The causality.
	 *******************************************************/
	Enu causality;
	Enu variability;

	/*******************************************************//**
	 * The description.
	 *******************************************************/
	const char* description;
	unsigned int valueReference;

	/*******************************************************//**
	 * The type specifier enumeration.
	 *******************************************************/
	TypeSpecEnumeration  * typeSpecEnumeration;

/*******************************************************//**
 * The scalar variable enumeration structure.
 *******************************************************/
} ScalarVariableEnumerationStruct;

typedef struct {
	/*******************************************************//**
	 * The name.
	 *******************************************************/
	const char * name;
	int idx;

	/*******************************************************//**
	 * The causality.
	 *******************************************************/
	Enu causality;
	Enu variability;

	/*******************************************************//**
	 * The description.
	 *******************************************************/
	const char* description;
	unsigned int valueReference;

	/*******************************************************//**
	 * The type specifier string.
	 *******************************************************/
	TypeSpecString  * typeSpecString;

/*******************************************************//**
 * The scalar variable string structure.
 *******************************************************/
} ScalarVariableStringStruct;

typedef struct {
	/*******************************************************//**
	 * The name.
	 *******************************************************/
	const char * name;
	const char * unit;
	const char * quantity; //Pressure, Density ... etc
	
	/*******************************************************//**
	 * The index.
	 *******************************************************/
	int idx;

	//values
	double start;
	double nominal;
	double min;
	double max;

	//statuses
	int startValueStatus;
	int nominalValueStatus;
	int minValueStatus;
	int maxValueStatus;
	int unitValueStatus;
	int quantityValueStatus;

} TypeDefinitionReal;

/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	const char * name;

	/*******************************************************//**
	 * The unit.
	 *******************************************************/
	const char * unit;
	int idx;

	/*******************************************************//**
	 * The start.
	 *******************************************************/
	char start;
	char fixed;

	/*******************************************************//**
	 * The start value status.
	 *******************************************************/
	int startValueStatus;
	int fixedValueStatus;
} TypeDefinitionBoolean;

/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	/*******************************************************//**
	 * The name.
	 *******************************************************/
	const char * name;
	const char * unit;

	/*******************************************************//**
	 * The index.
	 *******************************************************/
	int idx;

	int start;

	/*******************************************************//**
	 * The nominal.
	 *******************************************************/
	int nominal;
	int min;

	/*******************************************************//**
	 * The maximum.
	 *******************************************************/
	int max;
	int startValueStatus;

	/*******************************************************//**
	 * The nominal value status.
	 *******************************************************/
	int nominalValueStatus;
	int minValueStatus;

	/*******************************************************//**
	 * The maximum value status.
	 *******************************************************/
	int maxValueStatus;

/*******************************************************//**
 * The type definition integer.
 *******************************************************/
} TypeDefinitionInteger;

typedef struct {
	/*******************************************************//**
	 * The name.
	 *******************************************************/
	const char * name;
	int idx;

	/*******************************************************//**
	 * The minimum.
	 *******************************************************/
	int min;
	int max;

	/*******************************************************//**
	 * The minimum value status.
	 *******************************************************/
	int minValueStatus;
	int maxValueStatus;

	/*******************************************************//**
	 * Array of items.
	 *******************************************************/
	EnumerationItem * itemArray;
	int itemArrayLength;
} TypeDefinitionEnumeration;

/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	/*******************************************************//**
	 * The name.
	 *******************************************************/
	const char * name;
	const char * unit;

	/*******************************************************//**
	 * The index.
	 *******************************************************/
	int idx;

/*******************************************************//**
 * The type definition string.
 *******************************************************/
} TypeDefinitionString;

typedef struct {
	/*******************************************************//**
	 * The real value.
	 *******************************************************/
	ScalarVariableRealStruct * realValue;
	int realSize;

	/*******************************************************//**
	 * The boolean value.
	 *******************************************************/
	ScalarVariableBooleanStruct * booleanValue;
	int booleanSize;

	/*******************************************************//**
	 * The integer value.
	 *******************************************************/
	ScalarVariableIntegerStruct * integerValue;
	int integerSize;

	/*******************************************************//**
	 * The enumeration value.
	 *******************************************************/
	ScalarVariableEnumerationStruct * enumerationValue;
	int enumerationSize;

	/*******************************************************//**
	 * The string value.
	 *******************************************************/
	ScalarVariableStringStruct * stringValue;
	int stringSize;
} ScalarVariableCollectionStruct;

/*******************************************************//**
 * Defines an alias representing the structure.
 *******************************************************/
typedef struct {
	/*******************************************************//**
	 * The input.
	 *******************************************************/
	ScalarVariableCollectionStruct * input;
	ScalarVariableCollectionStruct * output;

	/*******************************************************//**
	 * Gets the internal.
	 *
	 * @return	The internal.
	 *******************************************************/
	ScalarVariableCollectionStruct * internal;
} ScalarVariablesAllStruct;


typedef struct {


	TypeDefinitionReal * input;
	ScalarVariableCollectionStruct * output;


} TypeDefinitionCollectionStruct;



typedef struct {

	const char * displayUnit;
	const char * offset;
	const char * gain;

} DisplayUnitDefinitionStruct;


typedef struct {

	const char * unit;
	DisplayUnitDefinitionStruct * displayUnitDefinitions;

} BaseUnitStruct;



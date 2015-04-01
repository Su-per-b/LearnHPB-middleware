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



typedef struct {
	const char * name;
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

	const char * displayUnit;
	int displayUnitValueStatus;

	double offset;
	int offsetValueStatus;

	double gain;
	int gainValueStatus;

} DisplayUnitDefinitionStruct;


typedef struct {

	const char * unit;
	DisplayUnitDefinitionStruct * displayUnitDefinitions;

} BaseUnitStruct;


typedef struct {

	int			value;
	ValueStatus status;

} IntegerValue;


typedef struct {

	double		value;
	ValueStatus status;

} RealValue;


typedef struct {

	bool		value;
	ValueStatus status;

} BooleanValue;


typedef struct {

	const char * value;
	ValueStatus status;

} StringValue;


typedef struct {

	int idx;

	//strings
	StringValue name;  //name is required
	StringValue unit;
	StringValue quantity; //Pressure, Density ... etc
	StringValue displayUnit; //bar ... etc

	//values
	RealValue start;
	RealValue nominal;
	RealValue min;
	RealValue max;

}  TypeDefinitionReal;



typedef struct {

	int idx;

	//strings
	StringValue name;  //name is required
	StringValue unit;
	StringValue quantity; //Pressure, Density ... etc
	StringValue displayUnit; //bar ... etc

	//values
	IntegerValue start;
	IntegerValue nominal;
	IntegerValue min;
	IntegerValue max;

} TypeDefinitionInteger;


typedef struct {

	int idx;

	//strings
	StringValue name;  //name is required
	StringValue unit;
	StringValue quantity; //Pressure, Density ... etc
	StringValue displayUnit; //bar ... etc

} TypeDefinitionString;



typedef struct {

	int idx;

	//strings
	StringValue name;  //name is required
	StringValue unit;
	StringValue quantity; //Pressure, Density ... etc
	StringValue displayUnit; //bar ... etc

	BooleanValue start;
	BooleanValue fixed;

} TypeDefinitionBoolean;



typedef struct {

	int idx;

	//strings
	StringValue name;  //name is required
	StringValue unit;
	StringValue quantity; //Pressure, Density ... etc
	StringValue displayUnit; //bar ... etc

	IntegerValue start;
	IntegerValue min;
	IntegerValue max;

	//EnumerationItem * itemArray;
	int itemArrayLength;

} TypeDefinitionEnumeration;


typedef struct {

	int idx;

	//strings
	StringValue name;  //name is required
	StringValue unit;
	StringValue quantity; //Pressure, Density ... etc
	StringValue displayUnit; //bar ... etc

	IntegerValue start;
	IntegerValue min;
	IntegerValue max;

	EnumerationItem * itemArray;

	int itemArrayLength;

} TypeDefinitionEnum;



typedef struct {

	TypeDefinitionReal * typeDefinitionRealArray;

	TypeDefinitionInteger * typeDefinitionIntegerArray;

	TypeDefinitionString * typeDefinitionStringArray;

	TypeDefinitionBoolean * typeDefinitionBooleanArray;

	TypeDefinitionEnumeration * typeDefinitionEnumerationArray;


} TypeDefinitionsStruct;



typedef struct {

	TypeDefinitionReal * input;
	ScalarVariableCollectionStruct * output;

} TypeDefinitionCollectionStruct;

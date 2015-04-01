#include "JNAdataTypes.h"




		ScalarVariablesAllStruct * test_GetScalarVariablesAllStruct() {

			ScalarVariablesAllStruct * theStruct = new ScalarVariablesAllStruct();

			theStruct->input = test_GetScalarVariableCollectionStruct();
			theStruct->output = test_GetScalarVariableCollectionStruct();
			theStruct->internal = test_GetScalarVariableCollectionStruct();

			return theStruct;
		}





		ScalarVariableCollectionStruct * test_GetScalarVariableCollectionStruct() {


			ScalarVariableRealStruct * scalarVariableReal_0 = test_GetScalarVariableRealStruct();
			ScalarVariableRealStruct * scalarVariableReal_1 = test_GetScalarVariableRealStruct();
			scalarVariableReal_1->idx = 2;


			ScalarVariableRealStruct * ary = new ScalarVariableRealStruct[2];
			ary[0] = *scalarVariableReal_0;
			ary[1] = *scalarVariableReal_1;


			ScalarVariableRealStruct scalarVariableReal_3 = ary[0];
			ScalarVariableRealStruct scalarVariableReal_4 = ary[1];


			ScalarVariableCollectionStruct * collectionStruct = new ScalarVariableCollectionStruct();

			collectionStruct->realValue = ary;
			collectionStruct->realSize = 2;


			return collectionStruct;
		}



		ScalarValueRealStruct * test_GetScalarValueRealStruct() {

			ScalarValueRealStruct * theStruct = new ScalarValueRealStruct();
			theStruct->idx = 0;
			theStruct->value = 2.2;

			return theStruct;

		}


		ScalarVariableRealStruct * test_GetScalarVariableRealStruct() {

			TypeSpecReal * typeSpecReal = test_GetTypeSpecReal();

			ScalarVariableRealStruct * scalarVariableReal_0 = new ScalarVariableRealStruct();
			scalarVariableReal_0->name = "scalarVar name";
			scalarVariableReal_0->idx = 1;
			scalarVariableReal_0->causality = enu_input;
			scalarVariableReal_0->variability = enu_continuous;
			scalarVariableReal_0->description = "The Description";
			scalarVariableReal_0->valueReference = 125420;

			scalarVariableReal_0->typeSpecReal = typeSpecReal;

			return scalarVariableReal_0;

		}



		TypeSpecReal * test_GetTypeSpecReal() {

			TypeSpecReal * typeSpecReal = new TypeSpecReal();
			typeSpecReal->start = 20.25;
			typeSpecReal->nominal = 21.25;
			typeSpecReal->min = 22.25;
			typeSpecReal->max = 23.25;
			typeSpecReal->unit = "C1";

			typeSpecReal->startValueStatus = valueDefined;
			typeSpecReal->nominalValueStatus = valueDefined;
			typeSpecReal->minValueStatus = valueDefined;
			typeSpecReal->maxValueStatus = valueDefined;
			typeSpecReal->unitValueStatus = valueDefined;

			return typeSpecReal;
		}


		//TypeSpecReal * test_GetTypeSpecInteger() {

		//	TypeSpecInteger * typeSpecInteger = new TypeSpecInteger();
		//	typeSpecInteger->start = 20.25;
		//	typeSpecInteger->nominal = 21.25;
		//	typeSpecInteger->min = 22.25;
		//	typeSpecInteger->max = 23.25;
		//	typeSpecInteger->unit = "C1";

		//	typeSpecInteger->startValueStatus = valueDefined;
		//	typeSpecInteger->nominalValueStatus = valueDefined;
		//	typeSpecInteger->minValueStatus = valueDefined;
		//	typeSpecInteger->maxValueStatus = valueDefined;
		//	typeSpecInteger->unitValueStatus = valueDefined;

		//	return typeSpecInteger;
		//}



		DisplayUnitDefinitionStruct * test_GetDisplayUnitDefinitionStruct() {

			DisplayUnitDefinitionStruct * displayUnitDefinitionStruct = new DisplayUnitDefinitionStruct();

			displayUnitDefinitionStruct->displayUnit = "K";
			displayUnitDefinitionStruct->displayUnitValueStatus = valueDefined;

			displayUnitDefinitionStruct->offset = -273.15;
			displayUnitDefinitionStruct->offsetValueStatus = valueDefined;

			displayUnitDefinitionStruct->gain = 0.001;
			displayUnitDefinitionStruct->gainValueStatus = valueDefined;


			return displayUnitDefinitionStruct;


		}


		TypeDefinitionReal * test_GetTypeDefinitionReal() {

			TypeDefinitionReal * typeDefinitionReal = new TypeDefinitionReal();
			typeDefinitionReal->idx = 1;

			typeDefinitionReal->name.value = "Modelica.Media.Interfaces.PartialMedium.AbsolutePressure";
			typeDefinitionReal->unit.value = "Pa";
			typeDefinitionReal->quantity.value = "Pressure";
			typeDefinitionReal->displayUnit.value = "bar";

			typeDefinitionReal->name.status = valueDefined;
			typeDefinitionReal->unit.status = valueDefined;
			typeDefinitionReal->quantity.status = valueDefined;
			typeDefinitionReal->displayUnit.status = valueDefined;

			typeDefinitionReal->start.value = 1.0;
			typeDefinitionReal->nominal.value = 100.0;
			typeDefinitionReal->min.value = 0.0;
			typeDefinitionReal->max.value = 100000000.0;

			typeDefinitionReal->start.status = valueDefined;
			typeDefinitionReal->nominal.status = valueDefined;
			typeDefinitionReal->min.status = valueDefined;
			typeDefinitionReal->max.status = valueDefined;

			return typeDefinitionReal;
		}



		TypeDefinitionInteger * test_GetTypeDefinitionInteger() {

			TypeDefinitionInteger * typeDefinitionInteger = new TypeDefinitionInteger();
			typeDefinitionInteger->idx = 2;
			
			typeDefinitionInteger->name.value = "AbsolutePressure3";
			typeDefinitionInteger->unit.value = "Pa3";
			typeDefinitionInteger->quantity.value = "Pressure3";
			typeDefinitionInteger->displayUnit.value = "bar3";

			typeDefinitionInteger->name.status = valueDefined;
			typeDefinitionInteger->unit.status = valueDefined;
			typeDefinitionInteger->quantity.status = valueDefined;
			typeDefinitionInteger->displayUnit.status = valueDefined;


			typeDefinitionInteger->start.value = 1;
			typeDefinitionInteger->nominal.value = 100;
			typeDefinitionInteger->min.value = 0;
			typeDefinitionInteger->max.value = 100000000;

			typeDefinitionInteger->start.status = valueDefined;
			typeDefinitionInteger->nominal.status = valueDefined;
			typeDefinitionInteger->min.status = valueDefined;
			typeDefinitionInteger->max.status = valueDefined;


			return typeDefinitionInteger;
		}




		TypeDefinitionString * test_GetTypeDefinitionString() {

			TypeDefinitionString * typeDefinitionString = new TypeDefinitionString();
			typeDefinitionString->idx = 3;

			typeDefinitionString->name.value = "AbsolutePressure2";
			typeDefinitionString->unit.value = "Pa2";
			typeDefinitionString->quantity.value = "Pressure2";
			typeDefinitionString->displayUnit.value = "bar2";

			typeDefinitionString->name.status = valueDefined;
			typeDefinitionString->unit.status = valueDefined;
			typeDefinitionString->quantity.status = valueDefined;
			typeDefinitionString->displayUnit.status = valueDefined;

			return typeDefinitionString;
		}
		


		TypeDefinitionBoolean * test_GetTypeDefinitionBoolean() {

			TypeDefinitionBoolean * typeDefinitionBoolean = new TypeDefinitionBoolean();
			typeDefinitionBoolean->idx = 4;

			typeDefinitionBoolean->name.value = "AbsolutePressure4";
			typeDefinitionBoolean->unit.value = "Pa4";
			typeDefinitionBoolean->quantity.value = "Pressure4";
			typeDefinitionBoolean->displayUnit.value = "bar4";

			typeDefinitionBoolean->name.status = valueDefined;
			typeDefinitionBoolean->unit.status = valueDefined;
			typeDefinitionBoolean->quantity.status = valueDefined;
			typeDefinitionBoolean->displayUnit.status = valueDefined;

			typeDefinitionBoolean->start.value = true;
			typeDefinitionBoolean->fixed.value = false;

			typeDefinitionBoolean->start.status = valueDefined;
			typeDefinitionBoolean->fixed.status = valueDefined;

			return typeDefinitionBoolean;
		}



		TypeDefinitionEnumeration * test_GetTypeDefinitionEnumeration() {

			TypeDefinitionEnumeration * typeDefinitionEnumeration = new TypeDefinitionEnumeration();
			typeDefinitionEnumeration->idx = 5;

			typeDefinitionEnumeration->name.value = "name5";
			typeDefinitionEnumeration->unit.value = "unit5";
			typeDefinitionEnumeration->quantity.value = "quantity5";
			typeDefinitionEnumeration->displayUnit.value = "displayUnit5";

			typeDefinitionEnumeration->name.status = valueDefined;
			typeDefinitionEnumeration->unit.status = valueDefined;
			typeDefinitionEnumeration->quantity.status = valueDefined;
			typeDefinitionEnumeration->displayUnit.status = valueDefined;


			typeDefinitionEnumeration->start.value = 1;
			typeDefinitionEnumeration->min.value = 1;
			typeDefinitionEnumeration->max.value = 6;

			typeDefinitionEnumeration->start.status = valueDefined;
			typeDefinitionEnumeration->min.status = valueDefined;
			typeDefinitionEnumeration->max.status = valueDefined;


			EnumerationItem * item_0 = new EnumerationItem();
			item_0->name = "name 1";
			item_0->description = "description 1";

			EnumerationItem * item_1 = new EnumerationItem();
			item_1->name = "name 2";
			item_1->description = "description 2";

			EnumerationItem * enumerationItemArray = new EnumerationItem[2];
			enumerationItemArray[0] = *item_0;
			enumerationItemArray[1] = *item_1;

			//typeDefinitionEnumeration->itemArray = enumerationItemArray;
			//typeDefinitionEnumeration->itemArrayLength = 2;

			return typeDefinitionEnumeration;
		}


		ValueStatus test_GetValueStatus() {

			return valueDefined;

		}
		
		


		RealValue test_GetRealValue() {

			RealValue realValue; 

			realValue.status = valueDefined;
			realValue.value = 13.01;

			return realValue;

		}



		IntegerValue test_GetIntegerValue() {

			IntegerValue integerValue; 
		
			integerValue.status = valueDefined;
			integerValue.value = 13;


			return integerValue;

		}

		BooleanValue test_GetBooleanValue() {

			BooleanValue booleanValue; 

			booleanValue.status = valueDefined;
			booleanValue.value = true;

			return booleanValue;

		}



		StringValue test_GetStringValue() {

			StringValue stringValue; 

			stringValue.status = valueDefined;
			stringValue.value = "stringValue_0";

			return stringValue;

		}


		EnumerationItem * test_GetEnumerationItem() {

			EnumerationItem * enumerationItem = new EnumerationItem();
			
			enumerationItem->name = "name_0";
			enumerationItem->description = "description_0";

			return enumerationItem;
		}



		TypeDefinitionEnum * test_GetTypeDefinitionEnum() {

			TypeDefinitionEnum * typeDefinitionEnum = new TypeDefinitionEnum();

			typeDefinitionEnum->name.value = "typeDefinitionEnum_name_0";


			typeDefinitionEnum->idx = 6;

			typeDefinitionEnum->name.value = "typeDefinitionEnum_name_0";
			typeDefinitionEnum->unit.value = "unit6";
			typeDefinitionEnum->quantity.value = "quantity6";
			typeDefinitionEnum->displayUnit.value = "displayUnit6";

			typeDefinitionEnum->name.status = valueDefined;
			typeDefinitionEnum->unit.status = valueDefined;
			typeDefinitionEnum->quantity.status = valueDefined;
			typeDefinitionEnum->displayUnit.status = valueDefined;

			//EnumerationItem * enumerationItem = new EnumerationItem();

			////enumerationItem->name = "name_0";
			//enumerationItem->description = "description_0";

			typeDefinitionEnum->itemArray = new EnumerationItem();
			typeDefinitionEnum->itemArray->name = "EnumerationItem_name_0";

			typeDefinitionEnum->itemArrayLength = 1;

			return typeDefinitionEnum;
		}



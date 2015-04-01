/*******************************************************//**
 * @file	model\TypeDefFactory.cpp
 *
 * Implements the type def factory class.
 *******************************************************/
#include "TypeDefFactory.h"


namespace Straylight
{
	/*******************************************************//**
	 * Default constructor.
	 *******************************************************/
	TypeDefFactory::TypeDefFactory(void)
	{
	}

	/*******************************************************//**
	 * Destructor.
	 *******************************************************/
	TypeDefFactory::~TypeDefFactory(void)
	{
	}


	TypeDefinitionReal* TypeDefFactory::makeReal(Type* type, int idx) {

		TypeDefinitionReal* typeDefinitionReal = new TypeDefinitionReal();

		typeDefinitionReal->idx = idx;
		typeDefinitionReal->name = Utils::getNameAsStringValue(type);

		typeDefinitionReal->unit = Utils::makeStringValueFromAttribute(type->typeSpec, att_unit);
		typeDefinitionReal->quantity = Utils::makeStringValueFromAttribute(type->typeSpec, att_quantity);
		typeDefinitionReal->displayUnit = Utils::makeStringValueFromAttribute(type->typeSpec, att_displayUnit);

		typeDefinitionReal->start = Utils::makeRealValueFromAttribute(type->typeSpec, att_start);
		typeDefinitionReal->nominal = Utils::makeRealValueFromAttribute(type->typeSpec, att_nominal);
		typeDefinitionReal->min = Utils::makeRealValueFromAttribute(type->typeSpec, att_min);
		typeDefinitionReal->max = Utils::makeRealValueFromAttribute(type->typeSpec, att_max);

		return typeDefinitionReal;
	}



	TypeDefinitionInteger* TypeDefFactory::makeInteger(Type* type, int idx) {

		TypeDefinitionInteger* typeDefinitionInteger = new TypeDefinitionInteger();

		typeDefinitionInteger->idx = idx;
		typeDefinitionInteger->name = Utils::getNameAsStringValue(type);

		typeDefinitionInteger->unit = Utils::makeStringValueFromAttribute(type->typeSpec, att_unit);
		typeDefinitionInteger->quantity = Utils::makeStringValueFromAttribute(type->typeSpec, att_quantity);
		typeDefinitionInteger->displayUnit = Utils::makeStringValueFromAttribute(type->typeSpec, att_displayUnit);

		typeDefinitionInteger->start = Utils::makeIntegerValueFromAttribute(type->typeSpec, att_start);
		typeDefinitionInteger->nominal = Utils::makeIntegerValueFromAttribute(type->typeSpec, att_nominal);
		typeDefinitionInteger->min = Utils::makeIntegerValueFromAttribute(type->typeSpec, att_min);
		typeDefinitionInteger->max = Utils::makeIntegerValueFromAttribute(type->typeSpec, att_max);


		return typeDefinitionInteger;
	}

	TypeDefinitionString* TypeDefFactory::makeString(Type* type, int idx) {

		TypeDefinitionString* typeDefinitionString = new TypeDefinitionString();

		typeDefinitionString->idx = idx;
		typeDefinitionString->name = Utils::getNameAsStringValue(type);

		typeDefinitionString->unit = Utils::makeStringValueFromAttribute(type->typeSpec, att_unit);
		typeDefinitionString->quantity = Utils::makeStringValueFromAttribute(type->typeSpec, att_quantity);
		typeDefinitionString->displayUnit = Utils::makeStringValueFromAttribute(type->typeSpec, att_displayUnit);

		return typeDefinitionString;
	}



	TypeDefinitionBoolean* TypeDefFactory::makeBoolean(Type* type, int idx) {
		TypeDefinitionBoolean * typeDefinitionBoolean = new TypeDefinitionBoolean();

		typeDefinitionBoolean->idx = idx;
		typeDefinitionBoolean->name = Utils::getNameAsStringValue(type);

		typeDefinitionBoolean->unit = Utils::makeStringValueFromAttribute(type->typeSpec, att_unit);
		typeDefinitionBoolean->quantity = Utils::makeStringValueFromAttribute(type->typeSpec, att_quantity);
		typeDefinitionBoolean->displayUnit = Utils::makeStringValueFromAttribute(type->typeSpec, att_displayUnit);

		typeDefinitionBoolean->start = Utils::makeBooleanValueFromAttribute(type->typeSpec, att_start);
		typeDefinitionBoolean->fixed = Utils::makeBooleanValueFromAttribute(type->typeSpec, att_fixed);

		return typeDefinitionBoolean;
	}





	TypeDefinitionEnumeration* TypeDefFactory::makeEnumeration(Type* type, int idx) {

		TypeDefinitionEnumeration* typeDefinitionEnumeration = new TypeDefinitionEnumeration();

		typeDefinitionEnumeration->idx = idx;
		typeDefinitionEnumeration->name = Utils::getNameAsStringValue(type);

		typeDefinitionEnumeration->unit = Utils::makeStringValueFromAttribute(type->typeSpec, att_unit);
		typeDefinitionEnumeration->quantity = Utils::makeStringValueFromAttribute(type->typeSpec, att_quantity);
		typeDefinitionEnumeration->displayUnit = Utils::makeStringValueFromAttribute(type->typeSpec, att_displayUnit);

		typeDefinitionEnumeration->start = Utils::makeIntegerValueFromAttribute(type->typeSpec, att_start);
		typeDefinitionEnumeration->min = Utils::makeIntegerValueFromAttribute(type->typeSpec, att_min);
		typeDefinitionEnumeration->max = Utils::makeIntegerValueFromAttribute(type->typeSpec, att_max);

		ListElement* listElement = (ListElement*)type->typeSpec;
		assert (typeDefinitionEnumeration->min.value == 1);

		int length;

		EnumerationItem * enumerationItemArray = extractItemArray(listElement,  &length);

		//typeDefinitionEnumeration->itemArray = enumerationItemArray;
		typeDefinitionEnumeration->itemArrayLength = length;

		assert(typeDefinitionEnumeration->max.value == length);

		return typeDefinitionEnumeration;
	}


	EnumerationItem * TypeDefFactory::extractItemArray(ListElement* listElement, int * length) {
		Element** listArray = listElement->list;

		//
		int lengthCalculated = 0;
		for (int k=0; Element* listElement = listArray[k]; k++) {
			lengthCalculated++;
		}

		*length = lengthCalculated;

		EnumerationItem * enumerationItemArray = new EnumerationItem[*length];

		for (int i=0; Element* listElement = listArray[i]; i++) {
			EnumerationItem * item = new EnumerationItem();
			item->name = getName(listElement);

			ValueStatus descriptionValueStatus;
			item->description = getElementAttributeString(listElement, &descriptionValueStatus, att_description);

			enumerationItemArray[i] = *item;
		}

			#ifdef DEBUG

				for (int j=0; j<length; j++) {
					EnumerationItem item = enumerationItemArray[j];
					int xx=0;
				}
			#endif

		return enumerationItemArray;
	}





}
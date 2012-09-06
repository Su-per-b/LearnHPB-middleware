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

	/*******************************************************//**
	 * Makes a real.
	 *
	 * @param [in,out]	type	If non-null, the type.
	 *
	 * @return	null if it fails, else.
	 *******************************************************/
	TypeDefinitionReal* TypeDefFactory::makeReal(Type* type) {
		TypeDefinitionReal* typeDefinitionReal = new TypeDefinitionReal();

		ValueStatus unitValueStatus;
		typeDefinitionReal->unit = getElementAttributeString(type->typeSpec, &unitValueStatus, att_unit);
		typeDefinitionReal->name = getName( type );

		ValueStatus startValueStatus;
		typeDefinitionReal->start = getElementAttributeReal(type->typeSpec, &startValueStatus, att_start);
		typeDefinitionReal->startValueStatus = startValueStatus;

		ValueStatus nominalValueStatus;
		typeDefinitionReal->nominal = getElementAttributeReal(type->typeSpec,&nominalValueStatus, att_nominal);
		typeDefinitionReal->nominalValueStatus = nominalValueStatus;

		ValueStatus minValueStatus;
		typeDefinitionReal->min = getElementAttributeReal(type->typeSpec,&minValueStatus, att_min);
		typeDefinitionReal->minValueStatus = minValueStatus;

		ValueStatus maxValueStatus;
		typeDefinitionReal->max = getElementAttributeReal(type->typeSpec,&maxValueStatus, att_max);
		typeDefinitionReal->maxValueStatus = maxValueStatus;

		return typeDefinitionReal;
	}

	/*******************************************************//**
	 * Makes a boolean.
	 *
	 * @param [in,out]	type	If non-null, the type.
	 *
	 * @return	null if it fails, else.
	 *******************************************************/
	TypeDefinitionBoolean* TypeDefFactory::makeBoolean(Type* type) {
		TypeDefinitionBoolean * typeDefinitionBoolean = new TypeDefinitionBoolean();

		ValueStatus unitValueStatus;
		typeDefinitionBoolean->unit = getElementAttributeString(type->typeSpec, &unitValueStatus, att_unit);
		typeDefinitionBoolean->name = getName( type );

		ValueStatus startValueStatus;
		typeDefinitionBoolean->start = getElementAttributeBoolean(type->typeSpec, &startValueStatus,att_start);
		typeDefinitionBoolean->startValueStatus = startValueStatus;

		ValueStatus fixedValueStatus;
		typeDefinitionBoolean->fixed = getElementAttributeBoolean(type->typeSpec, &fixedValueStatus,att_fixed);
		typeDefinitionBoolean->fixedValueStatus = fixedValueStatus;

		return typeDefinitionBoolean;
	}

	/*******************************************************//**
	 * Makes an integer.
	 *
	 * @param [in,out]	type	If non-null, the type.
	 *
	 * @return	null if it fails, else.
	 *******************************************************/
	TypeDefinitionInteger* TypeDefFactory::makeInteger(Type* type) {
		TypeDefinitionInteger* typeDefinitionInteger = new TypeDefinitionInteger();

		ValueStatus unitValueStatus;
		typeDefinitionInteger->unit = getElementAttributeString(type->typeSpec, &unitValueStatus, att_unit);
		typeDefinitionInteger->name = getName( type );

		ValueStatus startValueStatus;
		typeDefinitionInteger->start = getElementAttributeInteger(type->typeSpec, &startValueStatus, att_start);
		typeDefinitionInteger->startValueStatus = startValueStatus;

		ValueStatus nominalValueStatus;
		typeDefinitionInteger->nominal = getElementAttributeInteger(type->typeSpec,&nominalValueStatus, att_nominal);
		typeDefinitionInteger->nominalValueStatus = nominalValueStatus;

		ValueStatus minValueStatus;
		typeDefinitionInteger->min = getElementAttributeInteger(type->typeSpec,&minValueStatus, att_min);
		typeDefinitionInteger->minValueStatus = minValueStatus;

		ValueStatus maxValueStatus;
		typeDefinitionInteger->max = getElementAttributeInteger(type->typeSpec,&maxValueStatus, att_max);
		typeDefinitionInteger->maxValueStatus = maxValueStatus;

		return typeDefinitionInteger;
	}

	/*******************************************************//**
	 * Makes an enumeration.
	 *
	 * @param [in,out]	type	If non-null, the type.
	 *
	 * @return	null if it fails, else.
	 *******************************************************/
	TypeDefinitionEnumeration* TypeDefFactory::makeEnumeration(Type* type) {
		TypeDefinitionEnumeration* typeDefinitionEnumeration = new TypeDefinitionEnumeration();

		typeDefinitionEnumeration->name = getName( type );

		ValueStatus minValueStatus;
		typeDefinitionEnumeration->min = getElementAttributeInteger(type->typeSpec,&minValueStatus, att_min);
		typeDefinitionEnumeration->minValueStatus = minValueStatus;

		ValueStatus maxValueStatus;
		typeDefinitionEnumeration->max = getElementAttributeInteger(type->typeSpec,&maxValueStatus, att_max);
		typeDefinitionEnumeration->maxValueStatus = maxValueStatus;

		ListElement* listElement = (ListElement*)type->typeSpec;
		assert (typeDefinitionEnumeration->min == 1);

		int length;

		EnumerationItem * enumerationItemArray = extractItemArray(listElement,  &length);

		typeDefinitionEnumeration->itemArray = enumerationItemArray;
		typeDefinitionEnumeration->itemArrayLength = length;

		assert (typeDefinitionEnumeration->max == length);

		return typeDefinitionEnumeration;
	}

	/*******************************************************//**
	 * Extracts the item array.
	 *
	 * @param [in,out]	listElement	If non-null, the list element.
	 * @param [in,out]	length	   	If non-null, the length.
	 *
	 * @return	null if it fails, else the extracted item array.
	 *******************************************************/
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

	/*******************************************************//**
	 * Makes a string.
	 *
	 * @param [in,out]	type	If non-null, the type.
	 *
	 * @return	null if it fails, else.
	 *******************************************************/
	TypeDefinitionString* TypeDefFactory::makeString(Type* type) {
		TypeDefinitionString* typeDefinitionString = new TypeDefinitionString();

		ValueStatus unitValueStatus;
		typeDefinitionString->unit = getElementAttributeString(type->typeSpec, &unitValueStatus, att_unit);
		typeDefinitionString->name = getName( type );

		return typeDefinitionString;
	}
}
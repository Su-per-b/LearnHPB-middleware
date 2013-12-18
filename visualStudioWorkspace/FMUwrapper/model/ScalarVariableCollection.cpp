#include "ScalarVariableCollection.h"

namespace Straylight
{
	/*******************************************************//**
	 * Default constructor.
	 *******************************************************/
	ScalarVariableCollection::ScalarVariableCollection(void)
	{
	}

	/*******************************************************//**
	 * Destructor.
	 *******************************************************/
	ScalarVariableCollection::~ScalarVariableCollection(void)
	{

		int len = real.size();
		for (int i = 0; i < len; ++i)
		{
			delete real[i];
		}
		real.clear();

		len = boolean.size();
		for (int i = 0; i < len; ++i)
		{
			delete boolean[i];
		}
		boolean.clear();

		len = integer.size();
		for (int i = 0; i < len; ++i)
		{
			delete integer[i];
		}
		integer.clear();

		len = enumeration.size();
		for (int i = 0; i < len; ++i)
		{
			delete enumeration[i];
		}
		enumeration.clear();


		len = string.size();
		for (int i = 0; i < len; ++i)
		{
			delete string[i];
		}
		string.clear();


	}

	/*******************************************************//**
	 * Converts this object to a structure.
	 *
	 * @return	null if it fails, else object converted to a structure.
	 *******************************************************/
	ScalarVariableCollectionStruct * ScalarVariableCollection::toStruct()
	{
		ScalarVariableCollectionStruct * svStruct = new ScalarVariableCollectionStruct();

		svStruct->realValue = getRealAsArray();
		svStruct->realSize = real.size();

		svStruct->booleanValue = getBooleanAsArray();
		svStruct->booleanSize = boolean.size();

		svStruct->integerValue = getIntegerAsArray();
		svStruct->integerSize = integer.size();

		svStruct->enumerationValue = getEnumerationAsArray();
		svStruct->enumerationSize = enumeration.size();

		svStruct->stringValue = getStringAsArray();
		svStruct->stringSize = string.size();

		return svStruct;
	}

	/*******************************************************//**
	 * Gets real as array.
	 *
	 * @return	null if it fails, else the real as array.
	 *******************************************************/
	ScalarVariableRealStruct * ScalarVariableCollection::getRealAsArray() {
		int count = real.size();

		if (count > 0) {
			ScalarVariableRealStruct *ary = new ScalarVariableRealStruct[count];
			for(int i = 0; i < count; i++) {
				ary[i] = *real[i];
			}

			return ary;
		} else {
			return new ScalarVariableRealStruct();
		}
	}

	/*******************************************************//**
	 * Gets boolean as array.
	 *
	 * @return	null if it fails, else the boolean as array.
	 *******************************************************/
	ScalarVariableBooleanStruct * ScalarVariableCollection::getBooleanAsArray() {
		int count = boolean.size();

		if (count > 0) {
			ScalarVariableBooleanStruct *ary = new ScalarVariableBooleanStruct[count];
			for(int i = 0; i < count; i++) {
				ary[i] = *boolean[i];
			}

			return ary;
		} else {
			return new ScalarVariableBooleanStruct();
		}
	}

	/*******************************************************//**
	 * Gets integer as array.
	 *
	 * @return	null if it fails, else the integer as array.
	 *******************************************************/
	ScalarVariableIntegerStruct * ScalarVariableCollection::getIntegerAsArray() {
		int count = integer.size();

		if (count > 0) {
			ScalarVariableIntegerStruct *ary = new ScalarVariableIntegerStruct[count];
			for(int i = 0; i < count; i++) {
				ary[i] = *integer[i];
			}

			return ary;
		} else {
			return new ScalarVariableIntegerStruct();
		}
	}

	/*******************************************************//**
	 * Gets enumeration as array.
	 *
	 * @return	null if it fails, else the enumeration as array.
	 *******************************************************/
	ScalarVariableEnumerationStruct * ScalarVariableCollection::getEnumerationAsArray() {
		int count = enumeration.size();

		if (count > 0) {
			ScalarVariableEnumerationStruct *ary = new ScalarVariableEnumerationStruct[count];
			for(int i = 0; i < count; i++) {
				ary[i] = *enumeration[i];
			}

			return ary;
		} else {
			return new ScalarVariableEnumerationStruct();
		}
	}

	/*******************************************************//**
	 * Gets string as array.
	 *
	 * @return	null if it fails, else the string as array.
	 *******************************************************/
	ScalarVariableStringStruct * ScalarVariableCollection::getStringAsArray() {
		int count = string.size();

		if (count > 0) {
			ScalarVariableStringStruct *ary = new ScalarVariableStringStruct[count];
			for(int i = 0; i < count; i++) {
				ary[i] = *string[i];
			}

			return ary;
		} else {
			return new ScalarVariableStringStruct();
		}
	}
}
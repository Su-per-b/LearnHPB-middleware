#include "ScalarVariableCollection.h"

namespace Straylight
{
	ScalarVariableCollection::ScalarVariableCollection(void)
	{
	}


	ScalarVariableCollection::~ScalarVariableCollection(void)
	{
	}


	ScalarVariableCollectionStruct * ScalarVariableCollection::convertToStruct() {

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
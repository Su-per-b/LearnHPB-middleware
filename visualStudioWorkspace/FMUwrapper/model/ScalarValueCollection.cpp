#include "ScalarValueCollection.h"


namespace Straylight
{

	ScalarValueCollection::ScalarValueCollection(void)
	{
	}



	ScalarValueCollection::~ScalarValueCollection(void)
	{
	}


	ScalarValueCollectionStruct * ScalarValueCollection::convertToStruct() {

		ScalarValueCollectionStruct * sValCol = new ScalarValueCollectionStruct();

		sValCol->realValue = getRealAsArray();
		sValCol->booleanValue = getBooleanAsArray();

		return sValCol;
	}



	ScalarValueRealStruct * ScalarValueCollection::getRealAsArray() {

		int count = real.size();

		if (count > 0) {
			ScalarValueRealStruct *ary = new ScalarValueRealStruct[count];
			for(int i = 0; i < count; i++) {
				ary[i] = *real[i];
			}

			return ary;
		} else {
			return new ScalarValueRealStruct();
		}
	}


	ScalarValueBooleanStruct * ScalarValueCollection::getBooleanAsArray() {

		int count = boolean.size();

		if (count > 0) {
			ScalarValueBooleanStruct *ary = new ScalarValueBooleanStruct[count];
			for(int i = 0; i < count; i++) {
				ary[i] = *boolean[i];
			}

			return ary;
		} else {
			return new ScalarValueBooleanStruct();
		}

	}
}
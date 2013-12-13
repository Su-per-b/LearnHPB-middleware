#include "ScalarValueCollection.h"

namespace Straylight
{
	ScalarValueCollection::ScalarValueCollection(void)
	{
	}

	ScalarValueCollection::~ScalarValueCollection(void)
	{
	}

	ScalarValueCollectionStruct * ScalarValueCollection::toStruct() {
		ScalarValueCollectionStruct * sValCol = new ScalarValueCollectionStruct();

		sValCol->realValue = getRealAsArray();
		sValCol->realSize = real_.size();

		sValCol->booleanValue = getBooleanAsArray();
		sValCol->booleanSize = boolean_.size();

		return sValCol;
	}

	ScalarValueRealStruct * ScalarValueCollection::getRealAsArray() {
		int count = real_.size();

		if (count > 0) {
			ScalarValueRealStruct *ary = new ScalarValueRealStruct[count];
			for(int i = 0; i < count; i++) {
				ary[i] = *real_[i];
			}

			return ary;
		} else {
			return new ScalarValueRealStruct();
		}
	}

	ScalarValueBooleanStruct * ScalarValueCollection::getBooleanAsArray() {
		int count = boolean_.size();

		if (count > 0) {
			ScalarValueBooleanStruct *ary = new ScalarValueBooleanStruct[count];
			for(int i = 0; i < count; i++) {
				ary[i] = *boolean_[i];
			}

			return ary;
		} else {
			return new ScalarValueBooleanStruct();
		}
	}

	void ScalarValueCollection::toString( std::string & stringRef)
	{

		std::ostringstream sstream;

		int size = real_.size();


		for (int i = 0; i<size; i++){
		   ScalarValueRealStruct * realStruct = real_[i];  //Copy the vector to the string
		   sstream << realStruct->value << _T(" ");
		}


		stringRef = sstream.str();

	}

}
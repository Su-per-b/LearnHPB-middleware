#include "FmiModelAttributes.h"


namespace Straylight
{
	FmiModelAttributes::FmiModelAttributes(){}


	FmiModelAttributes::~FmiModelAttributes(){
	
		int len = attributeStructVector_.size();

		for (int i = 0; i < len; ++i)
		{
			delete attributeStructVector_[i];
		}


		attributeStructVector_.clear();

	}


	void FmiModelAttributes::extract(const char** attributeAry, int attributeCount)
	{

		int idx = 0;
		for (int i = 0; i < attributeCount; i = i + 2) {

			AttributeStruct * attributeStruct = new AttributeStruct();
			attributeStruct->name = attributeAry[i];
			attributeStruct->value = attributeAry[i + 1];

			attributeStructVector_.push_back(attributeStruct);

		}
	}

	AttributeStruct * FmiModelAttributes::toStructArray() {

		int count = attributeStructVector_.size();

		if (count > 0) {

			AttributeStruct *ary = new AttributeStruct[count];

			for (int i = 0; i < count; i++) {
				ary[i] = *attributeStructVector_[i];
			}

			return ary;
		}
		else {
			return new AttributeStruct();
		}

	}
}
#include "FmiModelAttributes.h"


namespace Straylight
{
	FmiModelAttributes::FmiModelAttributes(){}


	FmiModelAttributes::~FmiModelAttributes(){
	
		int len = attributeStructVector_.size();

		for (int i = 0; i < len; ++i)
		{
			delete attributeStructVector_[i];
			attributeStructVector_[i] = NULL;
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

	FMImodelAttributesStruct * FmiModelAttributes::toStruct() {

		int size = attributeStructVector_.size();
		AttributeStruct *ary;

		if (size > 0) {

			ary = new AttributeStruct[size];

			for (int i = 0; i < size; i++) {
				ary[i] = *attributeStructVector_[i];
			}

		}

		else {
			ary = new AttributeStruct();
		}

		FMImodelAttributesStruct * fmiModelAttributesStruct = new FMImodelAttributesStruct();

		fmiModelAttributesStruct->attributeStructAry = ary;
		fmiModelAttributesStruct->attributeStructSize = size;

		return fmiModelAttributesStruct;

	}
}
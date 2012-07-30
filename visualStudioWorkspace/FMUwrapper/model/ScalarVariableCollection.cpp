#include "ScalarVariableCollection.h"

namespace Straylight
{
	ScalarVariableCollection::ScalarVariableCollection(void)
	{
	}


	ScalarVariableCollection::~ScalarVariableCollection(void)
	{
	}


	ScalarVariableRealStruct * ScalarVariableCollection::getRealAsArray() {

		int count = real.size();
		ScalarVariableRealStruct *ary = new ScalarVariableRealStruct[count];
		for(int i = 0; i < count; i++)
			ary[i] = *real[i];


		return ary;

	}
}
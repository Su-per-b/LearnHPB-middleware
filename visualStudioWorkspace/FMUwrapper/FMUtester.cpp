
#include "FMUtester.h"






namespace Straylight
{

	FMUtester::FMUtester(void)
	{
	}


	FMUtester::~FMUtester(void)
	{
	}

    void FMUtester::test1()
    {
       printDebug("test1\n");
    }

    void FMUtester::test2()
    {
       printDebug("test2\n");

	//   doall("DoubleInputDoubleOutput.fmu");
    }


    void FMUtester::test3(const char* fmuFilNam)
    {
       printDebug("FMUwrapper::test3\n");

	   //doall(fmuFilNam);
    }
};


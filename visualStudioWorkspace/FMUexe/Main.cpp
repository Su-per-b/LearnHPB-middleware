// FMUexe.cpp : Defines the entry point for the console application.
//



#include "Main.h"


int _tmain(int argc, _TCHAR* argv[])
{
	test5();
	return 0;
}

void callback(char * msg) {
	printf ("Main.exe callback: %s \n", msg);
}

void test5() {

	int size =sizeof(int);

    void *(*foo)(char *);
  //  foo = callback;



	//void * (*callbackFn)(char *) = &callback;

	//registerCallback(foo);


	initAll(_T("C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop"));
	struct ScalarVariableMeta * svmAry  = getSVmetaData();

	int len = getVariableCount();

    while(isSimulationComplete() == 0) {
        char * str = getResultFromOneStep();



		ResultItemStruct * ri = testResultItemStruct();


		ResultItemStruct * riStruct;
		riStruct = getResultStruct();

		//int pLen = 


		printf ("result: %s \n", str);
    }

	end();

}




void test2() {
	testFMU(_T("C:\\Temp\\LearnGB_VAVReheat_ClosedLoop"));
}



void test3() {

	initAll(_T("C:\\Temp\\LearnGB_VAVReheat_ClosedLoop"));

    while(isSimulationComplete() == 0) {
        char * str = getResultFromOneStep();
		printf ("result: %s \n", str);
    }

	end();

}



void test4() {

	initAll(_T("C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop"));
	
	//const char * variableName = getVariableName(1);


	int c = getVariableCount();

    while(isSimulationComplete() == 0) {
        char * str = getResultFromOneStep();
		printf ("result: %s \n", str);
    }

	end();

}




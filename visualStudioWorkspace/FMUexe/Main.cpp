// FMUexe.cpp : Defines the entry point for the console application.
//



#include "Main.h"


int _tmain(int argc, _TCHAR* argv[])
{
	test5();
	return 0;
}


void test5() {

	int size =sizeof(int);

	//const char * str = testcpp();

	initAll(_T("C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop"));

	int * ptr = getaDataList2();

	int val = ptr[4];



	int len = getVariableCount();
	int i;

	for (i=0; i<len; i++){

		const char * name = getVariableName(i);
		const char * description = getVariableDescription(i);
		int c = getVariableCausality(i);

		int x = 0;

	}


    while(isSimulationComplete() == 0) {
        char * str = getResultFromOneStep();
		printf ("result: %s \n", str);
    }

	end();

}



void test1() {

	Straylight::FMUwrapper *fmuWrapper;
	fmuWrapper = new Straylight::FMUwrapper();

	int result = fmuWrapper->parseXML(_T("C:\\Temp\\LearnGB_VAVReheat_ClosedLoop"));

	if (result != 0) {
		exit(EXIT_FAILURE);
	}

	fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();

	Straylight::ResultItem * ri;

	// enter the simulation loop
	while (!fmuWrapper->isSimulationComplete()) {
		fmuWrapper->doOneStep();
		ri = fmuWrapper->getResultItem();

		char *  str2 = ri->getString();
		OutputDebugString(str2);
	}

	fmuWrapper->printSummary();
	delete fmuWrapper;
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




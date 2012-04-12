// FMUexe.cpp : Defines the entry point for the console application.
//



#include "Main.h"


int _tmain(int argc, _TCHAR* argv[])
{
	test3();
	return 0;
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
        char * str = getStringXy();
		printf ("result: %s \n", str);
    }

	end();

}








// FMUexe.cpp : Defines the entry point for the console application.
//



#include "Main.h"


int _tmain(int argc, _TCHAR* argv[])
{
	test5();
	return 0;
}



void resultCallback(ResultItemStruct * resultItemStruct) {
	printf ("Main.exe resultCallback: %s \n", resultItemStruct->string);
}

void messageCallback(MessageStruct * messageStruct) {
	printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
}

void test5() {

	//void (*callbackPtr)(ResultItemStruct *) = &resultCallback;
	registerResultCallback(&resultCallback);
	registerMessageCallback(&messageCallback);

	init(_T("C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop"));

	run();
	end();

}






void test2() {
	testFMU(_T("C:\\Temp\\LearnGB_VAVReheat_ClosedLoop"));
}



void test3() {

	init(_T("C:\\Temp\\LearnGB_VAVReheat_ClosedLoop"));

    while(isSimulationComplete() == 0) {
       // char * str = getResultFromOneStep();
		//printf ("result: %s \n", str);
    }

	end();

}



void test4() {

	init(_T("C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop"));
	
	//const char * variableName = getVariableName(1);


	int c = getVariableCount();

    while(isSimulationComplete() == 0) {
      //  char * str = getResultFromOneStep();
		//printf ("result: %s \n", str);
    }

	end();

}




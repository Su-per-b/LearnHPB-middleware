// FMUexe.cpp : Defines the entry point for the console application.
//



#include "Main.h"


int _tmain(int argc, _TCHAR* argv[])
{
	test5();
	return 0;
}



void resultCallback(ResultStruct * ResultStruct) {
	printf ("Main.exe resultCallback: %s \n", ResultStruct->string);
}

void messageCallback(MessageStruct * messageStruct) {
	printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
}

void fmuStateCallback(State fmuState) {
	//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
	printf ("Main.exe fmuStateCallback: %s \n", _T("state"));

}

//void test5() {

	//void (*callbackPtr)(ResultStruct *) = &resultCallback;
	//registerResultCallback(&resultCallback);
	//registerMessageCallback(&messageCallback);


	//init(_T("C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop"));

	//run();
//	end();

//}


void test5() {

	//void (*callbackPtr)(ResultStruct *) = &resultCallback;
	//registerResultCallback(&resultCallback);
	//registerMessageCallback(&messageCallback);

	initCallbacks(&messageCallback, &resultCallback, &fmuStateCallback);
	initXML(_T("C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop"));
	initSimulation();
	//ScalarVariableStruct * s = getScalarVariableStructs();

	ScalarVariableStruct *ptr2= getScalarVariableStructs();

	run();
	end();

}








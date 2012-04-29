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

void fmuStateCallback(State fmuState) {
	//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
	printf ("Main.exe fmuStateCallback: %s \n", _T("state"));

}

//void test5() {

	//void (*callbackPtr)(ResultItemStruct *) = &resultCallback;
	//registerResultCallback(&resultCallback);
	//registerMessageCallback(&messageCallback);


	//init(_T("C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop"));

	//run();
//	end();

//}


void test5() {

	//void (*callbackPtr)(ResultItemStruct *) = &resultCallback;
	//registerResultCallback(&resultCallback);
	//registerMessageCallback(&messageCallback);

	init_1(&messageCallback, &resultCallback, &fmuStateCallback);
	init_2(_T("C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop"));
	init_3();

	run();
	end();

}








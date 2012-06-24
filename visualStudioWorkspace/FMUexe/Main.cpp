// FMUexe.cpp : Defines the entry point for the console application.
//



#include "Main.h"
#include "Utils.h"


int _tmain(int argc, _TCHAR* argv[])
{
	test1();
	return 0;
}



void resultCallback(ResultOfStepStruct * resultOfStepStruct) {

	printf ("Main.exe resultCallback: %s \n", "");
}

void messageCallback(MessageStruct * messageStruct) {
	printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
}

void fmuStateCallback(SimStateNative simStateNative) {
	//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
	printf ("Main.exe simStateNative: %s \n", _T("simStateNative"));

}




void test1() {

	connect(&messageCallback, &resultCallback, &fmuStateCallback);

	xmlParse(_T("E:\\SRI\\straylight_repo\\assets\\FMUs\\LearnGB_0v2_VAVReheat_ClosedLoopEdit"));
	//xmlParse(_T("C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop"));
	
	init();
	changeInput(56106,10);
	run();


}








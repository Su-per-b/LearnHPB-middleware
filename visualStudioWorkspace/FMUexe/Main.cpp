// FMUexe.cpp : Defines the entry point for the console application.
//



#include "Main.h"
#include "Utils.h"
#include <conio.h>


int _tmain(int argc, _TCHAR* argv[])
{
	test1();
	getch();

	return 0;
}



void resultCallback(ResultOfStepStruct * resultOfStepStruct) {
	
	 std::string str;
	 resultOfStepStructToString(str, resultOfStepStruct);

	 printf ("result: %s \n", str.c_str());
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

	xmlParse(_T("E:\\SRI\\straylight_repo\\assets\\FMUs\\LearnGB_0v2_VAVReheat_ClosedLoopEdit2"));


	
	int result = init();

	if(result) {
		return;
	}

	setScalarValueReal(56106,300.2);

	run();



	requestStateChange (simStateNative_7_reset_requested);
	run();

	//reset();

}

void test2 () {

	connect(&messageCallback, &resultCallback, &fmuStateCallback);

	xmlParse(_T("E:\\SRI\\straylight_repo\\assets\\FMUs\\LearnGB_0v2_VAVReheat_ClosedLoop"));


	
	int result = init();

	if(result) {
		return;
	}

	requestStateChange(simStateNative_5_step_requested);
	//doOneStep();


}

void doubleToCommaString(char* buffer, double r){
	char* comma;
	sprintf(buffer, _T("%.16g"), r);
	comma = strchr(buffer, '.');
	if (comma) *comma = ',';
}


void resultOfStepStructToString(std::string & s, ResultOfStepStruct * resultOfStepStruct)
{

	std::stringstream ss;
	ss << _T("Time: ") << resultOfStepStruct->time << _T("  - input:");


	int len = resultOfStepStruct->inputLength;

	for (int i = 0; i < len; i++)
	{
		double theDouble =  resultOfStepStruct->input[i];
		ss << theDouble << _T(" ");
	}

	ss << _T("\n                    output:");


	int len2 = resultOfStepStruct->outputLength;

	for (int i = 0; i < len2; i++)
	{
		double theDouble =  resultOfStepStruct->output[i];
		ss << theDouble << _T(" ");
	}



	s = ss.str();

}



void doubleToString(char* buffer, double x)
{
	sprintf(buffer, _T("%.16g"), x);
}


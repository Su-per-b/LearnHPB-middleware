// FMUexe.cpp : Defines the entry point for the console application.
//

#include "Main.h"
#include "Utils.h"
#include <conio.h>

int _tmain(int argc, _TCHAR* argv[])
{
	//terminate1();
	//terminate2();
	terminate4();

	_getch();

	return 0;
}

void resultCallback(ScalarValueResultsStruct * scalarValueResultsStruct) {
	std::string str;
}


void messageCallback(MessageStruct * messageStruct) {
	//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
}

void fmuStateCallback(SimStateNative simStateNative) {
	//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
	printf ("Main.exe simStateNative: %s \n", _T("simStateNative"));
}

void terminate1 () {

	connect(&messageCallback, &resultCallback, &fmuStateCallback);
	forceCleanup();

	return;
}

void terminate2 () {

	connect(&messageCallback, &resultCallback, &fmuStateCallback);
	xmlParse(_T("E:\\SRI\\straylight_repo\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit1"));

	forceCleanup();

	return;
}


void terminate3 () {

	connect(&messageCallback, &resultCallback, &fmuStateCallback);
	xmlParse(_T("E:\\SRI\\straylight_repo\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit1"));

	requestStateChange(simStateNative_3_init_requested);

	forceCleanup();

	return;
}

void terminate4 () {

	connect(&messageCallback, &resultCallback, &fmuStateCallback);
	xmlParse(_T("E:\\SRI\\straylight_repo\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit1"));

	requestStateChange(simStateNative_3_init_requested);
	requestStateChange(simStateNative_5_step_requested);

	forceCleanup();

	return;
}

/*


void test2 () {


connect(&messageCallback, &resultCallback, &fmuStateCallback);


xmlParse(_T("E:\\SRI\\straylight_repo\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit1"));
ScalarVariablesAllStruct * s4 = getAllScalarVariables();
int result = init();

if(result) {
return;
}

run();
}

void test3 () {
connect(&messageCallback, &resultCallback, &fmuStateCallback);

xmlParse(_T("E:\\SRI\\straylight_repo\\assets\\FMUs\\LearnGB_0v2_0forTestingDataType_VAVReheat_ClosedLoop2"));

ScalarVariablesAllStruct * s4 = getAllScalarVariables();

//int result = init();
requestStateChange()


//if(result) {
return;
}

ScalarValueRealStruct *ary = new ScalarValueRealStruct[100];

ScalarValueRealStruct * sVal_1 = new ScalarValueRealStruct();
sVal_1->idx =1;
sVal_1->value = 11.1;

ary[0] = *sVal_1;

ScalarValueRealStruct * sVal_2 = new ScalarValueRealStruct();
sVal_2->idx =1;
sVal_2->value = 12.2;

ary[1] = *sVal_2;

setScalarValues(ary,2);
}
*/

void doubleToCommaString(char* buffer, double r){
	char* comma;
	sprintf(buffer, _T("%.16g"), r);

	comma = strchr(buffer, '.');
	if (comma) *comma = ',';
}


void doubleToString(char* buffer, double x)
{
	sprintf(buffer, _T("%.16g"), x);
}
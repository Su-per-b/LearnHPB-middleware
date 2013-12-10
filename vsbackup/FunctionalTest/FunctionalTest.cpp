// FunctionalTest.cpp : Defines the entry point for the console application.
#include "stdafx.h"
#include "FunctionalTest.h"

#define XML_FILE_STR  "\\modelDescription.xml"
#define FMU_FOLDER_STR  "..\\..\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit1"



int _tmain(int argc, _TCHAR* argv[])
{


	test1();

	return 0;
}


//Callbacks

void resultCallback(ScalarValueResultsStruct * scalarValueResultsStruct) {
	std::string str;
}

void messageCallback(MessageStruct * messageStruct) {
	//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
}

void fmuStateCallback(SimStateNative simStateNative) {
	//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
	printf("Main.exe simStateNative: %s \n", _T("simStateNative"));
}



void test_fmu_files_exist() {



	xmlParse(FMU_FOLDER_STR);

	ScalarVariablesAllStruct * ss = getAllScalarVariables();

	requestStateChange(simStateNative_3_init_requested);
	requestStateChange(simStateNative_5_step_requested);

	forceCleanup();

	return;
}




void test1() {

	connect(&messageCallback, &resultCallback, &fmuStateCallback);

	xmlParse(FMU_FOLDER_STR);

	ScalarVariablesAllStruct * ss = getAllScalarVariables();

	requestStateChange(simStateNative_3_init_requested);
	requestStateChange(simStateNative_5_step_requested);

	forceCleanup();

	return;
}














void resultCallbackClass(ScalarValueResults * scalarValueResults) {

	char buf[1024] = { 0 };
	scalarValueResults->toString(buf, 1024);
	printf(buf);

}






/*


void test2()
{

Config::getInstance()->setAutoCorrect(true);
MainController * mainController = new MainController();

Logger * logger = Logger::getInstance();
logger->setDebugvs(1);

mainController->connect(&messageCallback, &resultCallback, &fmuStateCallback);
mainController->setResultClassCallback(&resultCallbackClass);

mainController->xmlParse(_T("E:\\SRI\\straylight_repo\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit1"));
mainController->init();
mainController->run();

}


void test3()
{
Config::getInstance()->setAutoCorrect(true);
MainController * mainController = new MainController();
Logger * logger = Logger::getInstance();
logger->setDebugvs(1);

mainController->connect(&messageCallback, &resultCallback, &fmuStateCallback);
mainController->setResultClassCallback(&resultCallbackClass);
mainController->xmlParse(_T("E:\\SRI\\straylight_repo\\assets\\FMUs\\LearnGB_0v4_02_VAVReheat_ClosedLoop_edit1"));

mainController->init();

mainController->requestStateChange(simStateNative_5_step_requested);
mainController->requestStateChange(simStateNative_5_step_requested);

}



*/




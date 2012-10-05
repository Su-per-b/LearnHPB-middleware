// FunctionalTest.cpp : Defines the entry point for the console application.
#include "stdafx.h"
#include "FunctionalTest.h"


int _tmain(int argc, _TCHAR* argv[])
{


	test3();

	return 0;
}

void resultCallback(ScalarValueResultsStruct * scalarValueResultsStruct) {





	//std::string str;
}


void messageCallback(MessageStruct * messageStruct) {
	//printf ("Main.exe messageCallback: %s \n", messageStruct->msgText);
}

void fmuStateCallback(SimStateNative simStateNative) {
	printf ("Main.exe simStateNative: %s \n", _T("simStateNative"));
}

void resultCallbackClass(ScalarValueResults * scalarValueResults) {

	char buf[1024] = { 0 };
	scalarValueResults->toString(buf, 1024);
	printf(buf);

}



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







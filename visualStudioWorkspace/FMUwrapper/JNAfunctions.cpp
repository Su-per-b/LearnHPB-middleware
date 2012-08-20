#include "JNAfunctions.h"



void requestStateChange (SimStateNative simStateNative) {
	mainController->requestStateChange(simStateNative);
}


ConfigStruct * getConfig() {
	return mainController->getConfig();
}

int setConfig(ConfigStruct * configStruct) {
	mainController->setConfig(configStruct);

	return 0;
}


void onMessageCallbackC(MessageStruct * messageStruct)
{
	messageCallbackPtr_(messageStruct);
	delete messageStruct;
}



void cleanup() {

	mainController->cleanup();
}


int forceCleanup()
{
	//fmuWrapper->setState(fmuState_cleanedup);
	delete mainController;

	return 0;
}

int run()
{
	mainController->run();
	return 0;
}



ScalarVariablesAllStruct * getAllScalarVariables() {

	Straylight::MainDataModel * model = mainController->getMainDataModel();

	ScalarVariablesAllStruct * allScalarVariables = new ScalarVariablesAllStruct();
	allScalarVariables->input = model->scalarVariableDataModel_->svInput_->convertToStruct();
	allScalarVariables->output = model->scalarVariableDataModel_->svOutput_->convertToStruct();
	allScalarVariables->internal = model->scalarVariableDataModel_->svInternal_->convertToStruct();

	return allScalarVariables;

}



int isSimulationComplete () {
	return mainController->isSimulationComplete();
}



void connect (
	void (*messageCallbackPtr)(MessageStruct *), 
	void (*resultCallbackPtr)(ResultOfStepStruct *),
	void (*stateChangeCallbackPtr)(SimStateNative )
	)
{	

	mainController = new MainController();

	mainController->connect ( 
		messageCallbackPtr, 
		resultCallbackPtr,
		stateChangeCallbackPtr
		);

}

void xmlParse (char * unzipFolder)
{
	int result = mainController->xmlParse(unzipFolder);
}


int init ()
{
	return mainController->init();
}


ResultOfStepStruct * getResultStruct ()
{
	return mainController->getResultStruct();
}



fmiStatus setScalarValueReal (int idx, double value) {
	return mainController->setScalarValueReal(idx,value);
}

void setScalarValues (ScalarValueRealStruct * scalarValueAry , int length) {

		vector<ScalarValueRealStruct> scalarValueList;

		for (int i = 0; i < length; i++)
		{
			ScalarValueRealStruct st = scalarValueAry[i];
			scalarValueList.push_back(st);

			Logger::instance->printDebug(_T("setScalarValues\n"));
		}

		mainController->setScalarValues(scalarValueAry,length);

}

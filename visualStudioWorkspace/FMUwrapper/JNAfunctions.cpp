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


/*

ScalarVariableRealStruct *  getScalarVariableInputStructs() {

	Straylight::MainDataModel * model = mainController->getMainDataModel();
	return model->getSVinputArray();
}

ScalarVariableRealStruct *  getScalarVariableOutputStructs() {

	Straylight::MainDataModel * model = mainController->getMainDataModel();
	return model->getSVoutputArray();
}

ScalarVariableRealStruct *  getScalarVariableInternalStructs() {

	Straylight::MainDataModel * model = mainController->getMainDataModel();
	return model->getSVinternalArray();
}




int getInputVariableCount() {
	Straylight::MainDataModel * model = mainController->getMainDataModel();
	return model->getInputVariableCount();
}

int getOutputVariableCount() {

	Straylight::MainDataModel * model = mainController->getMainDataModel();
	return model->getOutputVariableCount();

}

int getInternalVariableCount() {
	Straylight::MainDataModel * model = mainController->getMainDataModel();
	return model->getInternalVariableCount();
}
ScalarVariableRealStruct * testSVRealStruct() {
	Straylight::MainDataModel * model = mainController->getMainDataModel();
	return model->scalarVariableDataModel_->svInput_->getRealAsArray();
}

ScalarVariableBooleanStruct * testSVBooleanStruct() {
	
	
 	Straylight::MainDataModel * model = mainController->getMainDataModel();
	ScalarVariableBooleanStruct * st1  = model->scalarVariableDataModel_->svInput_->getBooleanAsArray();
	st1->causality = enu_input;

	ScalarVariableBooleanStruct * st2 = new ScalarVariableBooleanStruct();
	st2->causality = enu_input;

	return st1;
}

ScalarVariableCollectionStruct * getScalarVariableCollectionStruct() {

	Straylight::MainDataModel * model = mainController->getMainDataModel();
	return model->scalarVariableDataModel_->svInput_->convertToStruct();

}




*/


int isSimulationComplete () {
	return mainController->isSimulationComplete();
}



void connect (
	void (*messageCallbackPtr)(MessageStruct *), 
	void (*resultCallbackPtr)(ResultOfStepStruct *),
	void (*stateChangeCallbackPtr)(SimStateNative )
	)
{	

	mainController = new Straylight::MainController();

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


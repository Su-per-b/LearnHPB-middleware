#include "JNAfunctions.h"



void requestStateChange (SimStateNative simStateNative) {
	mainController->requestStateChange(simStateNative);
}


ConfigStruct * getConfig() {
	return mainController->getConfig();
}

void setConfig(ConfigStruct * configStruct) {
	mainController->setConfig(configStruct);
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






ScalarVariableRealStruct *  getScalarVariableInputStructs() {

	Straylight::MainDataModel * model = mainController->getMainDataModel();
	return model->getSVinputArray();
}

ScalarVariableRealStruct *  getScalarVariableOutputStructs() {

	Straylight::MainDataModel * model = mainController->getMainDataModel();
	return model->getSVoutputArray();
}

ScalarVariableStruct *  getScalarVariableInternalStructs() {

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


void init ()
{
	int result2 = mainController->loadDll();
	mainController->init();
}






 ResultOfStepStruct * getResultStruct ()
{
	return mainController->getResultStruct();
}



 fmiStatus changeInput (int idx, double value) {
	 return mainController->changeInput(idx,value);
 }

 void doOneStep () {
	 return mainController->doOneStep();
 }
 
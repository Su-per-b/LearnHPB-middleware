#include "JNAfunctions.h"



int doOneStep()
{
   mainController->doOneStep();
   return 0;
}

void deleteMessageStruct(MessageStruct * messageStruct)
{
   //fmuWrapper->doOneStep();
   //delete messageStruct;
}



MetaDataStruct * getMetaData() {
	return mainController->getMetaData();
}

void setMetaData(MetaDataStruct * metaDataStruct) {
	mainController->setMetaData(metaDataStruct);
}


void onMessageCallbackC(MessageStruct * messageStruct)
{
   messageCallbackPtr_(messageStruct);
   delete messageStruct;
}



void end() {

	mainController->printSummary();

	//Straylight::Logger * logger = fmuWrapper->logger_;

	//TODO: Fix this - the event should be fired *after* the object is deleted
	mainController->setState(fmuState_cleanedup);
	delete mainController;

}


int forceCleanup()
{
	//fmuWrapper->setState(fmuState_cleanedup);
	delete mainController;

	return 0;
}

int run()
{


	mainController->setState(fmuState_runningSimulation);

	// enter the simulation loop
	while (!mainController->isSimulationComplete()) {
		mainController->doOneStep();

		ResultStruct *  ResultStruct = mainController->getResultStruct();

		if(resultCallbackPtr_) {
			resultCallbackPtr_(ResultStruct);
		}
	}

	mainController->setState(fmuState_completedSimulation);

   return 0;
}





ScalarVariableStruct *  getScalarVariableStructs() {

	/*
	int count = fmuWrapper->getVariableCount();
	ScalarVariableStruct *ptr = new ScalarVariableStruct[count];

	int i = 0;
	
	vector<ScalarVariableStruct*> list = fmuWrapper->getScalarVariableStructs();

	vector<ScalarVariableStruct*>::iterator list_iter = list.begin();

	for(list_iter; 
		list_iter != list.end(); list_iter++)
	{
		ScalarVariableStruct * svm = *list_iter;
		ptr[i]  = *svm;
		i++;
	}




	return ptr;
	*/


		vector<ScalarVariableStruct*> vec = mainController->getScalarVariableStructs();
		int count = vec.size();

		ScalarVariableStruct *ptr5 = new ScalarVariableStruct[count];

 
		for(int i = 0; i < count; i++)
			ptr5[i] = *vec[i];


		return ptr5;
}


 int getVariableCount() {
	 return mainController->getVariableCount();
 }


int isSimulationComplete () {
	return mainController->isSimulationComplete();
}



void initCallbacks (
	void (*messageCallbackPtr)(MessageStruct *), 
	void (*resultCallbackPtr)(ResultStruct *),
	void (*stateChangeCallbackPtr)(State )
	
	)
{	
	 messageCallbackPtr_ = messageCallbackPtr;
	 resultCallbackPtr_ = resultCallbackPtr;
	 stateChangeCallbackPtr_ = stateChangeCallbackPtr;

	 mainController = new Straylight::MainController( &onMessageCallbackC, stateChangeCallbackPtr_);

}

void initXML (char * unzipFolder)
{
	int result = mainController->parseXML(unzipFolder);
}


void initSimulation ()
{
	int result2 = mainController->loadDll();
	mainController->simulateHelperInit();
}






 ResultStruct * getResultStruct ()
{
	return mainController->getResultStruct();
}




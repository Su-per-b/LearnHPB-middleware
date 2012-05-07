#include "JNAfunctions.h"



int doOneStep()
{
   fmuWrapper->doOneStep();
   return 0;
}

void deleteMessageStruct(MessageStruct * messageStruct)
{
   //fmuWrapper->doOneStep();

   //delete messageStruct;
   

}

void onMessageCallbackC(MessageStruct * messageStruct)
{
   messageCallbackPtr_(messageStruct);
   delete messageStruct;
}



void end() {

	fmuWrapper->printSummary();

	//Straylight::Logger * logger = fmuWrapper->logger_;

	//TODO: Fix this - the event should be fired *after* the object is deleted
	fmuWrapper->setState(fmuState_cleanedup);
	delete fmuWrapper;

}


int forceCleanup()
{
	//fmuWrapper->setState(fmuState_cleanedup);
	delete fmuWrapper;

	return 0;
}

int run()
{


	fmuWrapper->setState(fmuState_runningSimulation);

	// enter the simulation loop
	while (!fmuWrapper->isSimulationComplete()) {
		fmuWrapper->doOneStep();

		ResultStruct *  ResultStruct = fmuWrapper->getResultStruct();

		if(resultCallbackPtr_) {
			resultCallbackPtr_(ResultStruct);
		}
	}

	fmuWrapper->setState(fmuState_completedSimulation);

   return 0;
}



struct ScalarVariableStruct *  getSVmetaData() {
	int count = getVariableCount();

	struct ScalarVariableStruct *ptr = new ScalarVariableStruct[count];

	int i;
	i = 0;


	std::list<ScalarVariableStruct> list = fmuWrapper->getMetaDataList();


	for(std::list<ScalarVariableStruct>::iterator list_iter = list.begin(); 
		list_iter != list.end(); list_iter++)
	{
		ScalarVariableStruct svm = * list_iter;
		ptr[i]  = * list_iter;
		i++;
	}


	return ptr;
}


 int getVariableCount() {
	 return fmuWrapper->getVariableCount();
 }


int isSimulationComplete () {
	return fmuWrapper->isSimulationComplete();
}



void init_1 (
	void (*messageCallbackPtr)(MessageStruct *), 
	void (*resultCallbackPtr)(ResultStruct *),
	void (*stateChangeCallbackPtr)(State )
	
	)
{	
	 messageCallbackPtr_ = messageCallbackPtr;
	 resultCallbackPtr_ = resultCallbackPtr;
	 stateChangeCallbackPtr_ = stateChangeCallbackPtr;

	 fmuWrapper = new Straylight::FMUwrapper( &onMessageCallbackC, stateChangeCallbackPtr_);

}

void init_2 (char * unzipFolder)
{
	int result = fmuWrapper->parseXML(unzipFolder);
}


void init_3 ()
{
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();
}






 ResultStruct * getResultStruct ()
{
	return fmuWrapper->getResultStruct();
}




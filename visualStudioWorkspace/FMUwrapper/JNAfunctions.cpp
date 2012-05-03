#include "JNAfunctions.h"



int doOneStep()
{
   fmuWrapper->doOneStep();
   return 0;
}


void end() {

	fmuWrapper->printSummary();
	delete fmuWrapper;

	fmuWrapper->setState(fmuState_cleanedup);
}



int run()
{


	fmuWrapper->setState(fmuState_runningSimulation);

	// enter the simulation loop
	while (!fmuWrapper->isSimulationComplete()) {
		fmuWrapper->doOneStep();

		ResultItemStruct *  resultItemStruct = fmuWrapper->getResultStruct();

		if(resultCallbackPtr_) {
			resultCallbackPtr_(resultItemStruct);
		}
	}

	fmuWrapper->setState(fmuState_completedSimulation);

   return 0;
}



struct ScalarVariableMeta *  getSVmetaData() {
	int count = getVariableCount();

	struct ScalarVariableMeta *ptr = new ScalarVariableMeta[count];

	int i;
	i = 0;


	std::list<ScalarVariableMeta> list = fmuWrapper->getMetaDataList();


	for(std::list<ScalarVariableMeta>::iterator list_iter = list.begin(); 
		list_iter != list.end(); list_iter++)
	{
		ScalarVariableMeta svm = * list_iter;
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
	void (*resultCallbackPtr)(ResultItemStruct *),
	void (*stateChangeCallbackPtr)(State )
	
	)
{	
	 messageCallbackPtr_ = messageCallbackPtr;
	 resultCallbackPtr_ = resultCallbackPtr;
	 stateChangeCallbackPtr_ = stateChangeCallbackPtr;

	 fmuWrapper = new Straylight::FMUwrapper( messageCallbackPtr_, stateChangeCallbackPtr_);

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






 ResultItemStruct * getResultStruct ()
{
	return fmuWrapper->getResultStruct();
}




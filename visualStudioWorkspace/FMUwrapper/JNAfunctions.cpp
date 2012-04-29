#include "JNAfunctions.h"



int doOneStep()
{
   fmuWrapper->doOneStep();
   return 0;
}


void end() {

	fmuWrapper->printSummary();
	delete fmuWrapper;
}



int registerMessageCallback(void (*callbackPtrArg)(MessageStruct *))
{
	MessageStruct * messageStruct = new MessageStruct;
	messageStruct->msgText = _T("!!JNAfuntions: registerMessageCallback");
	messageStruct->messageType = messageType_info;
	messageCallbackPtr_ = callbackPtrArg;
    messageCallbackPtr_(messageStruct);

   return 0;
}


int registerResultCallback(void (*callbackPtrArg)(ResultItemStruct *))
{
   resultCallbackPtr_ = callbackPtrArg;
   return 0;
}


int run()
{

	// enter the simulation loop
	while (!fmuWrapper->isSimulationComplete()) {
		fmuWrapper->doOneStep();

		ResultItemStruct *  resultItemStruct = fmuWrapper->getResultStruct();

		if(resultCallbackPtr_) {
			resultCallbackPtr_(resultItemStruct);
		}

	}

   return 0;
}




void testFMU (char * unzipFolder)
{
	fmuWrapper = new Straylight::FMUwrapper();

	int result = fmuWrapper->parseXML(unzipFolder);
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();

	Straylight::ResultItem * ri;

	// enter the simulation loop
	while (!fmuWrapper->isSimulationComplete()) {
		fmuWrapper->doOneStep();
		ri = fmuWrapper->getResultItem();

		printf ("result: %s \n", ri->getString());
	}

	fmuWrapper->printSummary();
	delete fmuWrapper;

}




struct ScalarVariableMeta *  getSVmetaData() {
	int count = getVariableCount();

	struct ScalarVariableMeta *ptr = new ScalarVariableMeta[count];

	int i;
	i = 0;

	for(std::list<ScalarVariableMeta>::iterator list_iter = fmuWrapper->metaDataList.begin(); 
		list_iter != fmuWrapper->metaDataList.end(); list_iter++)
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


void init (char * unzipFolder)
{
	fmuWrapper = new Straylight::FMUwrapper();
	fmuWrapper->registerMessageCallback(messageCallbackPtr_);
	
	int result = fmuWrapper->parseXML(unzipFolder);
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();

}






 ResultItemStruct * getResultStruct ()
{

	return fmuWrapper->getResultStruct();

}




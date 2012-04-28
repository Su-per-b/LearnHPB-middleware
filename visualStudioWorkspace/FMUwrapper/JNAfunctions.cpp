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


int registerCallback(void (*callbackPtrArg)(char *))
{
	callbackPtr = callbackPtrArg;
    callbackPtr(_T("JNAfuntions: registerCallback"));

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
	fmuWrapper->registerCallback(callbackPtr);

	int result = fmuWrapper->parseXML(unzipFolder);
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();

}


ResultItemPrimitiveStruct * testPrimitive() {
	ResultItemPrimitiveStruct * ps = new ResultItemPrimitiveStruct;

	ps->idx = 1;
	ps->string = _T("test xxx");

	return ps;

}

ResultItemPrimitiveStruct * testPrimitiveArray() {
	ResultItemPrimitiveStruct * ps = new ResultItemPrimitiveStruct[2];

	ps[0].idx = 0;
	ps[0].string = _T("test xxx");

	ps[1].idx = 1;
	ps[1].string = _T("test yyy");

	return ps;

}



ResultItemStruct * testResultItemStruct() {
	ResultItemPrimitiveStruct * ps = new ResultItemPrimitiveStruct[2];

	ps[0].idx = 0;
	ps[0].string = _T("test xx1");

	ps[1].idx = 1;
	ps[1].string = _T("test yyy");


	ResultItemStruct * riStruct = new ResultItemStruct;


	riStruct->time = 1.000;
	riStruct->string = _T("test zzz");
	riStruct->primitive =   ps;
	riStruct->primitiveCount = 2;

	return riStruct;

}


 ResultItemStruct * getResultStruct ()
{
	Straylight::ResultItem * ri;
	ri = fmuWrapper->getResultItem();
	ResultItemStruct *riStruct = new ResultItemStruct;

	int len = ri->resultPrimitiveList.size();
	ResultItemPrimitiveStruct * primitiveArry = new ResultItemPrimitiveStruct[len];

	ResultItemPrimitiveStruct * primitiveStruct;

	for (int i = 0; i < len; i++)
	{
		Straylight::ResultPrimitive * rp  = ri->resultPrimitiveList[i];

		primitiveArry[i].idx = rp->idx;
		std::string str = rp->getString();

		char * cstr = new char [str.size()+1];
        strcpy (cstr, str.c_str());
		primitiveArry[i].string = cstr;
	}

	riStruct->time = ri->time_;
	riStruct->string = ri->getString();
	riStruct->primitive =  primitiveArry;
	riStruct->primitiveCount = len;

	return riStruct;

}




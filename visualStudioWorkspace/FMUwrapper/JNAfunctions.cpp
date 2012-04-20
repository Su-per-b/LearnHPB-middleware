

#include "JNAfunctions.h"
#include "FMUwrapper.h"



Straylight::FMUwrapper *  fmuWrapper;



void testFMU (char * unzipFolder)
{

	fmuWrapper = new Straylight::FMUwrapper();
	//char * wfmuUnzippedFolder = wstrdup (unzipFolder);

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

int * getaDataList2() {

	//void * ptr;

	std::list<int> lst =  fmuWrapper->getaDataList();

	int size = lst.size();

	int *arrPointer = new int[size];
	copy(lst.begin(),lst.end(),arrPointer);


	int test = arrPointer[2];

	return arrPointer;

}

int * getaDataList3( ) {

	int* arrPointer = (int*) malloc(3 * sizeof(int));

	//int *arrPointer = new int[3];

	arrPointer[0] = 0;
	arrPointer[1] = 10;
	arrPointer[2] = 22;

	//int billy [] = { 16, 2, 77, 40, 12071 }

	return arrPointer;
}

void getaDataList4(void * buf ) {


	int * buf2 = (int *) buf;

    std::list<int> lst =  fmuWrapper->getaDataList();
	int *arrPointer = new int[lst.size()];
	copy(lst.begin(),lst.end(),buf2);

}

  int * getDataList() {
	 std::list<int> lst =  fmuWrapper->getaDataList();

	int *arrPointer = new int[lst.size()];


	copy(lst.begin(),lst.end(),arrPointer);


	// new int[lst.size()];

	//int my_array[] = {1,23,17,4,-5,100};
	//void *ptr;

	//ptr = my_array;


	//int * x;

	return arrPointer;

 }


	

 const char * getVariableName(int idx) {
	 return fmuWrapper->getVariableName(idx);
 }

 const char * getVariableDescription(int idx) {
	 return fmuWrapper->getVariableDescription(idx);
 }

 Enu getVariableCausality(int idx) {
	 return fmuWrapper->getVariableCausality(idx);
 }

  Elm getVariableType(int idx) {



	 return fmuWrapper->getVariableType(idx);
 }


 int getVariableCount() {
	 return fmuWrapper->getVariableCount();
 }



int isSimulationComplete () {
	return fmuWrapper->isSimulationComplete();
}


void initAll (char * unzipFolder)
{

	fmuWrapper = new Straylight::FMUwrapper();
	//char * wfmuUnzippedFolder = wstrdup (_T("C:\\Temp\\LearnGB_VAVReheat_ClosedLoop"));

	int result = fmuWrapper->parseXML(unzipFolder);
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();

	//Straylight::ResultItem * ri;

}


void end() {

	fmuWrapper->printSummary();
	delete fmuWrapper;
}


char * getResultFromOneStep ()
{

	Straylight::ResultItem * ri;

	fmuWrapper->doOneStep();
	ri = fmuWrapper->getResultItem();
	char * str = ri->getString();

	return str;

}




void init(_TCHAR * fmuSubPath) {

	fmuWrapper = new Straylight::FMUwrapper();
	char * wfmuUnzippedFolder = wstrdup (fmuSubPath);

	int result = fmuWrapper->parseXML(wfmuUnzippedFolder);
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();

}


char * wstrdup(_TCHAR * wSrc)
{
	int l_idx=0;
	int l_len = wstrlen(wSrc);
	char * l_nstr = (char*)malloc(l_len);
	if (l_nstr) {
		do {
			l_nstr[l_idx] = (char)wSrc[l_idx];
			l_idx++;
		} while ((char)wSrc[l_idx]!=0);
	}
	l_nstr[l_idx] = 0;
	return l_nstr;
}


// returns number of TCHARs in string
int wstrlen(_TCHAR * wstr)
{
    int l_idx = 0;
    while (((char*)wstr)[l_idx]!=0) l_idx+=2;
    return l_idx;
}

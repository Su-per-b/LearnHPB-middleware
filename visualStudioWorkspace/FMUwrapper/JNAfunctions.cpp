

#include "JNAfunctions.h"
#include "FMUwrapper.h"



Straylight::FMUwrapper *  fmuWrapper;



void testFMU ()
{

	fmuWrapper = new Straylight::FMUwrapper();

	char * wfmuUnzippedFolder = wstrdup (_T("C:\\Temp\\LearnGB_VAVReheat_ClosedLoop"));

	int result = fmuWrapper->parseXML(wfmuUnzippedFolder);
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();

	Straylight::ResultItem * ri;

	// enter the simulation loop
	while (!fmuWrapper->isSimulationComplete()) {
		fmuWrapper->doOneStep();
		ri = fmuWrapper->getResultItem();
	}

	fmuWrapper->printSummary();
	delete fmuWrapper;

}

int isSimulationComplete () {

	return fmuWrapper->isSimulationComplete();

}


void initAll ()
{

	fmuWrapper = new Straylight::FMUwrapper();
	char * wfmuUnzippedFolder = wstrdup (_T("C:\\Temp\\LearnGB_VAVReheat_ClosedLoop"));

	int result = fmuWrapper->parseXML(wfmuUnzippedFolder);
	int result2 = fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();

	Straylight::ResultItem * ri;

}


void end() {

	fmuWrapper->printSummary();
	delete fmuWrapper;
}


char * getStringXy ()
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

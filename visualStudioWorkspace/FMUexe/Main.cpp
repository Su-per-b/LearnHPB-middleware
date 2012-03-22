// FMUexe.cpp : Defines the entry point for the console application.
//



#include "Main.h"


int _tmain(int argc, _TCHAR* argv[])
{

	test6();
	return 0;
}



void test6() {
	Straylight::MainFMUwrapper *fmuWrapper;

	fmuWrapper = new Straylight::MainFMUwrapper();

	int result = fmuWrapper->parseXML("\\FMUs\\LearnGB_VAVReheat_ClosedLoopXP");

	if (result != 0) {
		exit(EXIT_FAILURE);
	}


	fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();

	Straylight::ResultItem * ri;

	// enter the simulation loop
	while (!fmuWrapper->isSimulationComplete()) {
		fmuWrapper->doOneStep();
		ri = fmuWrapper->getResultItem();

		char *  str2 = ri->getString();
		OutputDebugString(str2);
	}

	fmuWrapper->printSummary();
	delete fmuWrapper;
}



std::wstring s2ws(const std::string& s)
{
	int len;
	int slength = (int)s.length() + 1;
	len = MultiByteToWideChar(CP_ACP, 0, s.c_str(), slength, 0, 0); 
	wchar_t* buf = new wchar_t[len];
	MultiByteToWideChar(CP_ACP, 0, s.c_str(), slength, buf, len);
	std::wstring r(buf);
	delete[] buf;
	return r;
}









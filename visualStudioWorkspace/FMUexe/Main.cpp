// FMUexe.cpp : Defines the entry point for the console application.
//


#include "stdafx.h"
#include "Main.h"

#include <sstream>
#include <string>
#include <ResultPrimitive.h>

int _tmain(int argc, _TCHAR* argv[])
{

	test6();
	return 0;
}



void test6() {
	Straylight::MainFMUwrapper *fmuWrapper;

	fmuWrapper = new Straylight::MainFMUwrapper();
	fmuWrapper->parseXML("C:\\Temp\\DoubleInputDoubleOutput.fmu_unzipped");
	fmuWrapper->loadDll();
	fmuWrapper->simulateHelperInit();

	Straylight::ResultItem * ri; // = new Straylight::ResultItem();

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









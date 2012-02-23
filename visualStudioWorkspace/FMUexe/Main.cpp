// FMUexe.cpp : Defines the entry point for the console application.
//


#include "stdafx.h"
#include "Main.h"

#include <sstream>
#include <string>


	int _tmain(int argc, _TCHAR* argv[])
	{

		test6();
		return 0;
	}



	void test1() {
		Straylight::FMUtester::test1();

		Straylight::FMUtester::test3(
			"DoubleInputDoubleOutput.fmu"
		);
	}

	void test2() {
		Straylight::MainFMUwrapper *loader;
		loader = new Straylight::MainFMUwrapper();
		loader->doAll("DoubleInputDoubleOutput.fmu");
	}

	void test3() {
		Straylight::MainFMUwrapper *loader;
		loader = new Straylight::MainFMUwrapper();
		loader->setFMU("DoubleInputDoubleOutput.fmu");
		loader->unzip();
		loader->parseXML();
		loader->loadDLL();
		
		loader->runSimulation();
	}


	void test4() {
		Straylight::MainFMUwrapper *loader;
		loader = new Straylight::MainFMUwrapper();
		loader->setFMU("DoubleInputDoubleOutput.fmu");
		loader->unzip();
		loader->parseXML();
		loader->loadDLL();
		
		loader->simulateHelperInit();

		// enter the simulation loop
		while (!loader->isSimulationComplete()) {
			loader->doOneStep();
			fmiReal d = loader->getResultSnapshot();

			std::ostringstream os;
			os << d;
			std::string str = os.str();


		}


		loader->simulateHelperCleanup();


	}

	void test5() {

		Straylight::MainFMUwrapper *loader;
		loader = new Straylight::MainFMUwrapper();
		loader->initAll();

		// enter the simulation loop
		while (!loader->isSimulationComplete()) {
			loader->doOneStep();
			fmiReal d = loader->getResultSnapshot();


			std::ostringstream os;
			os << d;
			std::string str = os.str();

			//LPWSTR sss = os.str();
			//LPWSTR sss = s2ws2(str);

			//OutputDebugString(str);

		//	CString temp;
		//	temp.Format("%f",pDoc->m_r1);


		}



		loader->simulateHelperCleanup();

	}


	void test6() {
		Straylight::MainFMUwrapper *fmuWrapper;

	    fmuWrapper = new Straylight::MainFMUwrapper();
		fmuWrapper->setFMU("DoubleInputDoubleOutput.fmu");
		fmuWrapper->unzip();
		fmuWrapper->parseXML();
		fmuWrapper->loadDLL();
		
		fmuWrapper->simulateHelperInit();

		// enter the simulation loop
		while (!fmuWrapper->isSimulationComplete()) {
			fmuWrapper->doOneStep();
			fmiReal d = fmuWrapper->getResultSnapshot();

			std::ostringstream os;
			os << d;
			std::string str = os.str();


		}


		fmuWrapper->simulateHelperCleanup();


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









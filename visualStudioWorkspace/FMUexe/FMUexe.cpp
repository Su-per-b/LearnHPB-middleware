// FMUexe.cpp : Defines the entry point for the console application.
//


#include "stdafx.h"
#include "FMUexe.h"


	int _tmain(int argc, _TCHAR* argv[])
	{



		test3();
		
		return 0;
	}



	void test1() {
		Straylight::FMUtester::test1();

		Straylight::FMUtester::test3(
			"DoubleInputDoubleOutput.fmu"
		);
	}

	void test2() {
		Straylight::FMUloader *loader;
		loader = new Straylight::FMUloader();
		loader->doAll("DoubleInputDoubleOutput.fmu");
	}

	void test3() {
		Straylight::FMUloader *loader;
		loader = new Straylight::FMUloader();
		loader->setFMU("DoubleInputDoubleOutput.fmu");
		loader->unzip();
		loader->parseXML();
		loader->loadDLL();
		
		loader->runSimulation();
	}




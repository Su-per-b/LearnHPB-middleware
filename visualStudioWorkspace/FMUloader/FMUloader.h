#pragma once



extern "C"
{

#include "include/expat.h"
#include "include/util.h"
#include "mainHelper.h"

}


namespace Straylight
{
    class FMUloader
    {
    
	private:
		const char * fmuFileName_;
		char* unzippedPath_;
		char* dllPath_;

		double timeEnd_;
		FMU fmu_; // the fmu to simulate

	public:
		__declspec(dllexport) FMUloader(void);
		__declspec(dllexport) ~FMUloader(void);

        __declspec(dllexport) void doAll(const char * fileName);

		__declspec(dllexport) void setFMU(const char * fileName);
	
		__declspec(dllexport) void unzip();

		__declspec(dllexport) void parseXML();

		__declspec(dllexport) void loadDLL();

		
		__declspec(dllexport) void runSimulation();
		
		__declspec(dllexport) int simulateHelper(FMU* fmu,  double h, fmiBoolean loggingOn, char separator);

    };




};




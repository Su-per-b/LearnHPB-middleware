

#include "stdafx.h"

#include <sstream>





namespace Straylight
{
    class MainFMUwrapper
    {
    
	private:
		const char * fmuFileName_;

		char* unzipfolder_;
		char* xmlFilePath_;
		char* dllFilePath_;

		double timeEnd_;
		FMU fmu_; // the fmu to simulate
		FMU* fmuPointer_;

		int simulateLoop( );


		double time_;
		fmiValueReference vru_[2], vry_[2]; // value references for two input and two output variables 
		char csv_separator_; 

		fmiReal ru1, ru2, ry, ry1, ry2;	// add real variables for input and output
		fmiInteger ix, iy;				// add integer variables for input and output
		fmiBoolean bx, by;				// add boolean variables for input and output
		fmiString sx, sy;				// Zuo: add string variables for input and output
		fmiStatus status;				// Zuo: add stauus for fmi
		int nSteps;
		fmiValueReference vr;			// add it to get value reference for variables
		fmiReal t0;                  // start time
		FILE* file;
		fmiComponent fmiComponent_;                  // instance of the fmu 
		ScalarVariable** vars;

		fmiBoolean loggingOn;
		double timeDelta_;
		ModelDescription* modelDescription_;


	public:
		__declspec(dllexport) MainFMUwrapper(void);
		__declspec(dllexport) ~MainFMUwrapper(void);
        __declspec(dllexport) void doAll(const char * fileName);
		__declspec(dllexport) void unzip();

		__declspec(dllexport) void parseXML(char* unzipfolder);
		__declspec(dllexport) void parseXML2();

		__declspec(dllexport) int isSimulationComplete();

		__declspec(dllexport) int simulateHelperInit();

		__declspec(dllexport) void simulateHelperCleanup();

		__declspec(dllexport) void doOneStep();

		__declspec(dllexport) fmiReal getResultSnapshot();
		
		__declspec(dllexport) int loadDll();


	private:
		int loadDLLhelper(const char* , FMU *);

    };




};




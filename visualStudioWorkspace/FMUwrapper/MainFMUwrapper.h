

#include <tchar.h>
#include "stdafx.h"
#include "ResultSet.h"
#include "ResultItem.h"


#include <sstream>


namespace Straylight
{


	class MainFMUwrapper
	{


	private:

		char* unzipFolderPath_;

		char* xmlFilePath_;

		char* dllFilePath_;

		double timeEnd_;
		FMU fmu_; // the fmu to simulate
		FMU* fmuPointer_;


		double time_;
		fmiValueReference vru_[1], vry_[1]; // value references for two input and two output variables 
		char csv_separator_; 

		fmiReal ru1, ry, ry1;	// add real variables for input and output
		fmiInteger ix, iy;				// add integer variables for input and output
		fmiBoolean bx, by;				// add boolean variables for input and output
		fmiString sx, sy;				// Zuo: add string variables for input and output
		fmiStatus status;				// Zuo: add stauus for fmi
		int nSteps;
		fmiValueReference vr;			// add it to get value reference for variables
		fmiReal t0;                  // start time
	//	FILE* file;
		fmiComponent fmiComponent_;                  // instance of the fmu 
		ScalarVariable** vars;

		fmiBoolean loggingOn;
		double timeDelta_;
		ModelDescription* modelDescription_;


	public:
		DllExport MainFMUwrapper(void);
		DllExport ~MainFMUwrapper(void);
		DllExport void doAll(const char * fileName);
		DllExport void unzip();

		DllExport int parseXML(char* unzipfolder);

		DllExport int isSimulationComplete();

		DllExport int simulateHelperInit();

		DllExport void printSummary();

		DllExport void doOneStep();

		DllExport fmiReal getResultSnapshot();

		DllExport int loadDll();

		DllExport ResultItem * getResultItem();

		void getModuleFolderPath(_TCHAR * szDir);



		//	ResultSet* resultSet_;

		ResultItem* resultItem_;

		char* getXmlFilePath()
		{
			return xmlFilePath_;
		}


	private:
		int loadDLLhelper(const char* , FMU *);
		ModelDescription* parseHelper(const char*);

		//	void outputRow2(FMU *, fmiComponent, double, FILE*, char);


	};




};




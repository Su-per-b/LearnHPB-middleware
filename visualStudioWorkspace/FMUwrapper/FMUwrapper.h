

#include <tchar.h>
#include "stdafx.h"
#include "ResultSet.h"
#include "ResultItem.h"
#include "JNAfunctions.h"

#include <sstream>
#include <list>


char * wstrdup(_TCHAR * );

int wstrlen(_TCHAR * );

void init(_TCHAR * );


struct ScalarVariableMeta {
  const char * name;
  int idx;
};



namespace Straylight
{


	class FMUwrapper
	{

	//private member variables
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
		fmiComponent fmiComponent_;                  // instance of the fmu 
		ScalarVariable** vars;
		fmiBoolean loggingOn;
		double timeDelta_;
		ModelDescription* modelDescription_;
        char* getXmlFilePath();




	//exported public functions
	public:
		DllExport FMUwrapper(void);
		DllExport ~FMUwrapper(void);
		DllExport void doAll(const char * fileName);
		DllExport void doOneStep();
		DllExport std::list<int> getDataList();

		DllExport ResultItem * getResultItem();
		DllExport fmiReal getResultSnapshot();
		DllExport Enu getVariableCausality(int idx);
		DllExport int getVariableCount();
		DllExport const char * getVariableDescription(int idx);
		DllExport const char * getVariableName(int idx);
		DllExport Elm getVariableType(int idx);

		DllExport int isSimulationComplete();
		DllExport int loadDll();
		DllExport int parseXML(char* unzipfolder);
		DllExport void printSummary();
		DllExport int simulateHelperInit();
		DllExport void test1();
		DllExport void unzip();

		std::list<ScalarVariableMeta> metaDataList;
		std::list<int> metaDataListTest;

	//public functions which are not exported
	public:	
		void getModuleFolderPath(_TCHAR * szDir);
		ResultItem* resultItem_;


	//private functions
	private:
		int loadDLLhelper(const char* , FMU *);
		ModelDescription* parseHelper(const char*);


	};




};




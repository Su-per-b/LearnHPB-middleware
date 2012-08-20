#pragma once



#include "stdafx.h"
#include "structs.h"
#include "ResultOfStep.h"
#include "Logger.h"
#include "TypeDefFactory.h"
#include "ScalarVariableFactory.h"
#include "TypeDefDataModel.h"
#include "ScalarVariableCollection.h"
#include "ScalarVariableDataModel.h"
#include "ScalarVariableDataModel.h"

using namespace std;

namespace Straylight
{


	class MainDataModel
	{

	public:
		MainDataModel();
		~MainDataModel(void);

		void extract();
		ResultOfStep* getResultOfStep(double time);
		fmiStatus setScalarValueReal(int idx, double value);

		void setFMU(FMU* fmu);
		void setFmiComponent(fmiComponent fmiComponent_arg);
		void setStartValues();

		TypeDefDataModel * typeDefDataModel_;
		ScalarVariableDataModel * scalarVariableDataModel_;
		void setScalarValues (ScalarValueRealStruct * scalarValueAry, int length);

	private:

		ModelDescription* modelDescription_;
		int maxInternalScalarVariables;

		FMU* fmu_;
		fmiComponent fmiComponent_;

	};




};




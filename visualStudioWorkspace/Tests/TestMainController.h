#pragma once
#include "stdafx.h"

#include <JNAfunctions.h>
#include <MainController.h>
#include <Logger.h>

namespace StraylightTests
{

	static MainController * mainController;
	static SimStateNative static_simStateNative;
	static ScalarValueResultsStruct * static_scalarValueResultsStruct;

	void resultCallback2(ScalarValueResultsStruct * scalarValueResultsStruct);

	void messageCallback2(MessageStruct * messageStruct);

	void fmuStateCallback2(SimStateNative simStateNative);


}

#pragma once

#include "stdafx.h"
#include "CppUnitTest.h"
#include "fmu.h"


void resultCallback2(ScalarValueResultsStruct * scalarValueResultsStruct);

void messageCallback2(MessageStruct * messageStruct);

void fmuStateCallback2(SimStateNative simStateNative);
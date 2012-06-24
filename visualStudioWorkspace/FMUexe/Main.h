#pragma once

#include "stdafx.h"
#include <JNAfunctions.h>
#include <ResultOfStep.h>
#include <ScalarValue.h>




void test1();

void resultCallback(ResultOfStepStruct * resultOfStepStruct);

void messageCallback(MessageStruct * messageStruct);

void fmuStateCallback(SimulationStateEnum fmuState);
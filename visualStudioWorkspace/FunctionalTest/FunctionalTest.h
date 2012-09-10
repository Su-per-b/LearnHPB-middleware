#pragma once

#include "stdafx.h"
#include <JNAfunctions.h>
#include <MainController.h>
#include <Logger.h>
#include <ScalarValueResults.h>




using namespace Straylight;
using namespace std;



void resultCallback(ResultOfStepStruct * resultOfStepStruct);

void messageCallback(MessageStruct * messageStruct);

void fmuStateCallback(SimulationStateEnum fmuState);

void resultCallbackClass(ScalarValueResults * scalarValueResults);


void test2();
void test3();


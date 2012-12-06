#pragma once

#include "stdafx.h"
#include <JNAfunctions.h>
#include <ResultOfStep.h>
#include <ScalarValue.h>



using namespace Straylight;

void terminate1();
void terminate2();
void terminate3();
void terminate4();

void test2();

void test3();

void resultCallback(ResultOfStepStruct * resultOfStepStruct);

void messageCallback(MessageStruct * messageStruct);

void fmuStateCallback(SimulationStateEnum fmuState);

void doubleToCommaString(char* buffer, double r);

void doubleToString(char* buffer, double x);

void resultOfStepStructToString(std::string & s, ResultOfStepStruct * resultOfStepStruct);
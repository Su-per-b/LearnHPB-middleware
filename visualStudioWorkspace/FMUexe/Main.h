#pragma once

#include "stdafx.h"
#include <JNAfunctions.h>
#include <ResultItem.h>
#include <ResultPrimitive.h>




void test5();

void resultCallback(ResultItemStruct * resultItemStruct);

void messageCallback(MessageStruct * messageStruct);

void fmuStateCallback(State fmuState);
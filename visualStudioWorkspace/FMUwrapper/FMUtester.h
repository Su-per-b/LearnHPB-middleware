#pragma once


#include "StdAfx.h"

extern "C"
{


#include "util.h"
#include "mainHelper.h"

}

namespace Straylight
{

	class FMUtester
	{
	public:
		FMUtester(void);
		~FMUtester(void);

        static __declspec(dllexport) void test1();

		static __declspec(dllexport) void test2();

		static __declspec(dllexport) void test3(const char* fmuFilNam);
	};

};
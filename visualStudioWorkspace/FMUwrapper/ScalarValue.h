#pragma once

#include "stdafx.h"

using namespace std;

typedef struct ScalarValueStruct_ {
	int idx;
	const char * string;
} ScalarValueStruct;

typedef struct ScalarValueRealStruct_ {
	int idx;
	double value;
} ScalarValueRealStruct;




namespace Straylight
{  
	class  ScalarValue
	{
	public:
		ScalarValue(int idx_local);
		~ScalarValue(void);
		string  getString();
		fmiReal getRealNumber();

		void setRealNumber(double value);

		fmiStatus getStatus();

		int getIdx();

		static void setFMU(FMU* fmu);
		static void setFmiComponent(fmiComponent fmiComponent_arg);

	private:
		fmiValueReference valueReference_;

		ScalarVariable* scalarVariable_;
		fmiStatus status_;

		static FMU* fmu_;
		static fmiComponent fmiComponent_;

		int idx_;

	public:

		double scalarReal;
		int scalarInt;
		bool scalarBool;
		string scalarString;
		int type;



	};


}

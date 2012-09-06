/*******************************************************//**
 * @file	model\ScalarValue.h
 *
 * Declares the scalar value class.
 *******************************************************/
#pragma once

#include "stdafx.h"

using namespace std;

/*******************************************************//**
 * Defines an alias representing the scalar value structure.
 *******************************************************/
typedef struct ScalarValueStruct_ {
	/*******************************************************//**
	 * The index to 
	 *******************************************************/
	int idx;
	const char * string;
} ScalarValueStruct;

/*******************************************************
 * Defines an alias representing the structure. to 
 * TODO: Can this converted to a class to 
 *******************************************************/
typedef struct  {
	/*******************************************************//**
	 * The index.
	 *******************************************************/
	int idx;
	double value;
} ScalarValueRealStruct;

/*******************************************************//**
 * Defines an alias representing the structure.     
 *******************************************************/
typedef struct  {
	/*******************************************************//**
	 * The index.  to
	 *******************************************************/
	int idx;
	int value;
} ScalarValueBooleanStruct;


namespace Straylight
{
	/*******************************************************//**
	 * Scalar value.
	 *******************************************************/
	class  ScalarValue
	{
	public:
		ScalarValue(int idx_local);
		~ScalarValue(void);

		string  getString();
		fmiReal getRealNumber();
		fmiBoolean getBoolean(void);

		void setRealNumber(double value);

		fmiStatus getStatus();

		int getIdx();

		static void setFMU(FMU* fmu);

		/*******************************************************//**
		 * Sets fmi component.
		 *
		 * @param	fmiComponent_arg	The fmi component argument.
		 *******************************************************/
		static void setFmiComponent(fmiComponent fmiComponent_arg);

	private:
		fmiValueReference valueReference_;

		ScalarVariable* scalarVariable_;

		/*******************************************************//**
		 * The status.
		 *******************************************************/
		fmiStatus status_;

		/*******************************************************//**
		 * The fmu.
		 *******************************************************/
		static FMU* fmu_;
		static fmiComponent fmiComponent_;

		int idx_;

	public:

		/*******************************************************//**
		 * The scalar real.
		 *******************************************************/
		double scalarReal;
		int scalarInt;

		/*******************************************************//**
		 * The scalar bool.
		 *******************************************************/
		bool scalarBool;
		string scalarString;

		/*******************************************************//**
		 * The type.
		 *******************************************************/
		int type;
	};
}

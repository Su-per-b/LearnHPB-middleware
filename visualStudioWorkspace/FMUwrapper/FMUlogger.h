/*******************************************************//**
 * @file	FMUlogger.h
 *
 * Declares the fm ulogger class.
 *******************************************************/
#pragma once

#include "stdafx.h"
#include "Logger.h"


namespace Straylight
{
	/*******************************************************//**
	 * Fm ulogger.
	 *******************************************************/
	class FMUlogger
	{

		//private member variables
	private:


		// public functions
	public:

		/*******************************************************//**
		 * Default constructor.
		 *******************************************************/
		FMUlogger(void);

		/*******************************************************//**
		 * Destructor.
		 *******************************************************/
		~FMUlogger(void);

		/*******************************************************//**
		 * Logs.
		 *
		 * @param	c				The fmiComponent to process.
		 * @param	instanceName	Name of the instance.
		 * @param	status			The status.
		 * @param	category		The category.
		 * @param	message			The message.
		 *******************************************************/
		static void log(fmiComponent c, fmiString instanceName, fmiStatus status,
			fmiString category, fmiString message, ...);

		/*******************************************************//**
		 * Sets a fmu.
		 *
		 * @param [in,out]	fmuArg	If non-null, the fmu argument.
		 *******************************************************/
		static void setFMU(FMU* fmuArg);

		//private functions
	private:

		/*******************************************************//**
		 * The fmu.
		 *******************************************************/
		static FMU * fmu; // the fmu to simulate

		/*******************************************************//**
		 * Fmi status to string.
		 *
		 * @param	status	The status.
		 *
		 * @return	null if it fails, else.
		 *******************************************************/
		static const char* fmiStatusToString(fmiStatus status);

		/*******************************************************//**
		 * Gets a sv.
		 *
		 * @param [in,out]	fmu	If non-null, the fmu.
		 * @param	type	   	The type.
		 * @param	vr		   	The vr.
		 *
		 * @return	null if it fails, else the sv.
		 *******************************************************/
		static ScalarVariable* getSV(FMU* fmu, char type, fmiValueReference vr);

		/*******************************************************//**
		 * Replace references in message.
		 *
		 * @param	msg			  	The message.
		 * @param [in,out]	buffer	If non-null, the buffer.
		 * @param	nBuffer		  	The buffer.
		 * @param [in,out]	fmu   	If non-null, the fmu.
		 *******************************************************/
		static void replaceRefsInMessage(const char* msg, char* buffer, int nBuffer, FMU* fmu);

	};


};




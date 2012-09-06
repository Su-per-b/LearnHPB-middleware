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
	 * <summary> Fm ulogger.</summary>
	 *
	 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
	 *******************************************************/
	class FMUlogger
	{

	public:

		/*******************************************************//**
		 * <summary> Default constructor.</summary>
		 *
		 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
		 *******************************************************/
		FMUlogger(void);

		/*******************************************************//**
		 * <summary> Destructor.</summary>
		 *
		 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
		 *******************************************************/
		~FMUlogger(void);

		/*******************************************************//**
		 * <summary> Logs.</summary>
		 *
		 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
		 *
		 * <param name="c">			   The fmiComponent to process.</param>
		 * <param name="instanceName"> Name of the instance.</param>
		 * <param name="status">	   The status.</param>
		 * <param name="category">	   The category.</param>
		 * <param name="message">	   The message.</param>
		 *******************************************************/
		static void log(fmiComponent c, fmiString instanceName, fmiStatus status,
			fmiString category, fmiString message, ...);

		/*******************************************************//**
		 * <summary> Sets a fmu.</summary>
		 *
		 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
		 *
		 * <param name="fmuArg"> [in,out] If non-null, the fmu argument.</param>
		 *******************************************************/
		static void setFMU(FMU* fmuArg);

		//private functions
	private:

		/*******************************************************//**
		 * <summary> The fmu.</summary>
		 *******************************************************/
		static FMU * fmu; // the fmu to simulate

		/*******************************************************//**
		 * <summary> Fmi status to string.</summary>
		 *
		 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
		 *
		 * <param name="status"> The status.</param>
		 *
		 * <returns> null if it fails, else.</returns>
		 *******************************************************/
		static const char* fmiStatusToString(fmiStatus status);

		/*******************************************************//**
		 * <summary> Gets a sv.</summary>
		 *
		 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
		 *
		 * <param name="fmu">  [in,out] If non-null, the fmu.</param>
		 * <param name="type"> The type.</param>
		 * <param name="vr">   The vr.</param>
		 *
		 * <returns> null if it fails, else the sv.</returns>
		 *******************************************************/
		static ScalarVariable* getSV(FMU* fmu, char type, fmiValueReference vr);

		/*******************************************************//**
		 * <summary> Replace references in message.</summary>
		 *
		 * <remarks> Raj Dye raj@pcdigi.com, 9/4/2012.</remarks>
		 *
		 * <param name="msg">	  The message.</param>
		 * <param name="buffer">  [in,out] If non-null, the buffer.</param>
		 * <param name="nBuffer"> The buffer.</param>
		 * <param name="fmu">	  [in,out] If non-null, the fmu.</param>
		 *******************************************************/
		static void replaceRefsInMessage(const char* msg, char* buffer, int nBuffer, FMU* fmu);
	};
};

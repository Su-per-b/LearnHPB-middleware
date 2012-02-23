// rajUnzip.h : main header file for the rajUnzip DLL
//

#pragma once

#include "stdafx.h"





#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"		// main symbols


// CrajUnzipApp
// See rajUnzip.cpp for the implementation of this class
//

class CrajUnzipApp : public CWinApp
{
public:
	CrajUnzipApp();

	BOOL test();

// Overrides
public:
	virtual BOOL InitInstance();

	DECLARE_MESSAGE_MAP()
};

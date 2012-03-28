// fmutest.h : main header file for the fmutest DLL
//

#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"		// main symbols


// CfmutestApp
// See fmutest.cpp for the implementation of this class
//

class CfmutestApp : public CWinApp
{
public:
	CfmutestApp();

// Overrides
public:
	virtual BOOL InitInstance();

	DECLARE_MESSAGE_MAP()
};

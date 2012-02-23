// MyCompressDemo.h : main header file for the MYCOMPRESSDEMO application
//

#if !defined(AFX_MYCOMPRESSDEMO_H__BA330576_8F63_4E33_A5A5_7ED3ADFED314__INCLUDED_)
#define AFX_MYCOMPRESSDEMO_H__BA330576_8F63_4E33_A5A5_7ED3ADFED314__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"       // main symbols

/////////////////////////////////////////////////////////////////////////////
// CMyCompressDemoApp:
// See MyCompressDemo.cpp for the implementation of this class
//

class CMyCompressDemoApp : public CWinApp
{
public:
	CMyCompressDemoApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CMyCompressDemoApp)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation
	//{{AFX_MSG(CMyCompressDemoApp)
	afx_msg void OnAppAbout();
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_MYCOMPRESSDEMO_H__BA330576_8F63_4E33_A5A5_7ED3ADFED314__INCLUDED_)

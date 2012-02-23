// MyCompressDemoView.h : interface of the CMyCompressDemoView class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_MYCOMPRESSDEMOVIEW_H__1E8D57D2_235C_424D_B106_708C8B474177__INCLUDED_)
#define AFX_MYCOMPRESSDEMOVIEW_H__1E8D57D2_235C_424D_B106_708C8B474177__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


class CMyCompressDemoView : public CFormView
{
protected: // create from serialization only
	CMyCompressDemoView();
	DECLARE_DYNCREATE(CMyCompressDemoView)

public:
	//{{AFX_DATA(CMyCompressDemoView)
	enum { IDD = IDD_MYCOMPRESSDEMO_FORM };
	CString	m_sPathIn;
	CString	m_sPathOut;
	//}}AFX_DATA

// Attributes
public:
	CMyCompressDemoDoc* GetDocument();

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CMyCompressDemoView)
	public:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	virtual void OnInitialUpdate(); // called first time after construct
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CMyCompressDemoView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	//{{AFX_MSG(CMyCompressDemoView)
	afx_msg void OnButtonCompress();
	afx_msg void OnButtonUncompress();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

#ifndef _DEBUG  // debug version in MyCompressDemoView.cpp
inline CMyCompressDemoDoc* CMyCompressDemoView::GetDocument()
   { return (CMyCompressDemoDoc*)m_pDocument; }
#endif

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_MYCOMPRESSDEMOVIEW_H__1E8D57D2_235C_424D_B106_708C8B474177__INCLUDED_)

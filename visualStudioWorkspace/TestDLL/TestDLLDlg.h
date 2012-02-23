
// TestDLLDlg.h : header file
//

#pragma once

#include "..\MyDLL\MyClass.h"

// CTestDLLDlg dialog
class CTestDLLDlg : public CDialogEx
{
// Construction
public:
	CTestDLLDlg(CWnd* pParent = NULL);	// standard constructor

// Dialog Data
	enum { IDD = IDD_TESTDLL_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support

	afx_msg void OnBnClickedOk();

	CMyClass objMyClass; 

// Implementation
protected:
	HICON m_hIcon;

	// Generated message map functions
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()


public:
	CString m_edit;
};

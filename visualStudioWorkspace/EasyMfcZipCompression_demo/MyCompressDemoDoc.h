// MyCompressDemoDoc.h : interface of the CMyCompressDemoDoc class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_MYCOMPRESSDEMODOC_H__8A0966DC_5B18_4E7C_A2C3_BC3D2B2D2089__INCLUDED_)
#define AFX_MYCOMPRESSDEMODOC_H__8A0966DC_5B18_4E7C_A2C3_BC3D2B2D2089__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


class CMyCompressDemoDoc : public CDocument
{
protected: // create from serialization only
	CMyCompressDemoDoc();
	DECLARE_DYNCREATE(CMyCompressDemoDoc)

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CMyCompressDemoDoc)
	public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CMyCompressDemoDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	//{{AFX_MSG(CMyCompressDemoDoc)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_MYCOMPRESSDEMODOC_H__8A0966DC_5B18_4E7C_A2C3_BC3D2B2D2089__INCLUDED_)

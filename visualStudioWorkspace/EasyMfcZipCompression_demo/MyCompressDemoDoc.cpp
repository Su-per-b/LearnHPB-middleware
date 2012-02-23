// MyCompressDemoDoc.cpp : implementation of the CMyCompressDemoDoc class
//

#include "stdafx.h"
#include "MyCompressDemo.h"

#include "MyCompressDemoDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CMyCompressDemoDoc

IMPLEMENT_DYNCREATE(CMyCompressDemoDoc, CDocument)

BEGIN_MESSAGE_MAP(CMyCompressDemoDoc, CDocument)
	//{{AFX_MSG_MAP(CMyCompressDemoDoc)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CMyCompressDemoDoc construction/destruction

CMyCompressDemoDoc::CMyCompressDemoDoc()
{
	// TODO: add one-time construction code here

}

CMyCompressDemoDoc::~CMyCompressDemoDoc()
{
}

BOOL CMyCompressDemoDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}



/////////////////////////////////////////////////////////////////////////////
// CMyCompressDemoDoc serialization

void CMyCompressDemoDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: add storing code here
	}
	else
	{
		// TODO: add loading code here
	}
}

/////////////////////////////////////////////////////////////////////////////
// CMyCompressDemoDoc diagnostics

#ifdef _DEBUG
void CMyCompressDemoDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CMyCompressDemoDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CMyCompressDemoDoc commands

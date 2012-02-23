// MyCompressDemoView.cpp : implementation of the CMyCompressDemoView class
//

#include "stdafx.h"
#include "MyCompressDemo.h"

#include "MyCompressDemoDoc.h"
#include "MyCompressDemoView.h"
#include "MyCompress.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CMyCompressDemoView

IMPLEMENT_DYNCREATE(CMyCompressDemoView, CFormView)

BEGIN_MESSAGE_MAP(CMyCompressDemoView, CFormView)
	//{{AFX_MSG_MAP(CMyCompressDemoView)
	ON_BN_CLICKED(IDC_BUTTON_COMPRESS, OnButtonCompress)
	ON_BN_CLICKED(IDC_BUTTON_UNCOMPRESS, OnButtonUncompress)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CMyCompressDemoView construction/destruction

CMyCompressDemoView::CMyCompressDemoView()
	: CFormView(CMyCompressDemoView::IDD)
{
	//{{AFX_DATA_INIT(CMyCompressDemoView)
	m_sPathIn = _T("");
	m_sPathOut = _T("");
	//}}AFX_DATA_INIT
	// TODO: add construction code here

}

CMyCompressDemoView::~CMyCompressDemoView()
{
}

void CMyCompressDemoView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CMyCompressDemoView)
	DDX_Text(pDX, IDC_EDIT_INPATH, m_sPathIn);
	DDX_Text(pDX, IDC_EDIT_OUTPATH, m_sPathOut);
	//}}AFX_DATA_MAP
}

BOOL CMyCompressDemoView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs

	return CFormView::PreCreateWindow(cs);
}

void CMyCompressDemoView::OnInitialUpdate()
{
	CFormView::OnInitialUpdate();
	GetParentFrame()->RecalcLayout();
	ResizeParentToFit();

}

/////////////////////////////////////////////////////////////////////////////
// CMyCompressDemoView diagnostics

#ifdef _DEBUG
void CMyCompressDemoView::AssertValid() const
{
	CFormView::AssertValid();
}

void CMyCompressDemoView::Dump(CDumpContext& dc) const
{
	CFormView::Dump(dc);
}

CMyCompressDemoDoc* CMyCompressDemoView::GetDocument() // non-debug version is inline
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CMyCompressDemoDoc)));
	return (CMyCompressDemoDoc*)m_pDocument;
}
#endif //_DEBUG


/////////////////////////////////////////////////////////////////////////////
// CMyCompressDemoView message handlers

//
void CMyCompressDemoView::OnButtonCompress()
{
	UpdateData(true);

	MyCompress mc;
	VERIFY(mc.Compress(m_sPathIn, m_sPathOut));
}

//
void CMyCompressDemoView::OnButtonUncompress()
{
	UpdateData(true);
	
	MyCompress mc;
	VERIFY(mc.Uncompress(m_sPathIn, m_sPathOut));
}

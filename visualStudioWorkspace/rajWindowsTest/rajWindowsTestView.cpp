
// rajWindowsTestView.cpp : implementation of the CrajWindowsTestView class
//

#include "stdafx.h"
// SHARED_HANDLERS can be defined in an ATL project implementing preview, thumbnail
// and search filter handlers and allows sharing of document code with that project.
#ifndef SHARED_HANDLERS
#include "rajWindowsTest.h"
#endif

#include "rajWindowsTestDoc.h"
#include "rajWindowsTestView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CrajWindowsTestView

IMPLEMENT_DYNCREATE(CrajWindowsTestView, CView)

BEGIN_MESSAGE_MAP(CrajWindowsTestView, CView)
	// Standard printing commands
	ON_COMMAND(ID_FILE_PRINT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, &CrajWindowsTestView::OnFilePrintPreview)
	ON_WM_CONTEXTMENU()
	ON_WM_RBUTTONUP()
END_MESSAGE_MAP()

// CrajWindowsTestView construction/destruction

CrajWindowsTestView::CrajWindowsTestView()
{
	// TODO: add construction code here

}

CrajWindowsTestView::~CrajWindowsTestView()
{
}

BOOL CrajWindowsTestView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs

	return CView::PreCreateWindow(cs);
}

// CrajWindowsTestView drawing

void CrajWindowsTestView::OnDraw(CDC* /*pDC*/)
{
	CrajWindowsTestDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	// TODO: add draw code for native data here
}


// CrajWindowsTestView printing


void CrajWindowsTestView::OnFilePrintPreview()
{
#ifndef SHARED_HANDLERS
	AFXPrintPreview(this);
#endif
}

BOOL CrajWindowsTestView::OnPreparePrinting(CPrintInfo* pInfo)
{
	// default preparation
	return DoPreparePrinting(pInfo);
}

void CrajWindowsTestView::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: add extra initialization before printing
}

void CrajWindowsTestView::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: add cleanup after printing
}

void CrajWindowsTestView::OnRButtonUp(UINT /* nFlags */, CPoint point)
{
	ClientToScreen(&point);
	OnContextMenu(this, point);
}

void CrajWindowsTestView::OnContextMenu(CWnd* /* pWnd */, CPoint point)
{
#ifndef SHARED_HANDLERS
	theApp.GetContextMenuManager()->ShowPopupMenu(IDR_POPUP_EDIT, point.x, point.y, this, TRUE);
#endif
}


// CrajWindowsTestView diagnostics

#ifdef _DEBUG
void CrajWindowsTestView::AssertValid() const
{
	CView::AssertValid();
}

void CrajWindowsTestView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

CrajWindowsTestDoc* CrajWindowsTestView::GetDocument() const // non-debug version is inline
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CrajWindowsTestDoc)));
	return (CrajWindowsTestDoc*)m_pDocument;
}
#endif //_DEBUG


// CrajWindowsTestView message handlers

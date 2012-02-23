// MyClass.cpp : implementation file
//

#include "stdafx.h"
#include "MyDll.h"
#include "MyClass.h"


// CMyClass

IMPLEMENT_DYNAMIC(CMyClass, CWnd)

CMyClass::CMyClass()
{

}

CMyClass::~CMyClass()
{
}


BEGIN_MESSAGE_MAP(CMyClass, CWnd)
END_MESSAGE_MAP()



// CMyClass message handlers




CString CMyClass::SayHello(CString strName)
{
	//Return the input string with a Hello prefixed 
	 return _T("Hello ") + strName; 
}

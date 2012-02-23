#pragma once


// CMyClass

class CMyClass : public CWnd
{
	DECLARE_DYNAMIC(CMyClass)

public:
	__declspec(dllexport)  CMyClass();
	__declspec(dllexport)  virtual ~CMyClass();
    __declspec(dllexport)  CString SayHello(CString strName);

protected:
	DECLARE_MESSAGE_MAP()



};



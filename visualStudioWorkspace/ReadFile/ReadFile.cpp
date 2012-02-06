// ReadFile.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"
#include "ReadFile.h"


JNIEXPORT jstring JNICALL Java_nativetest_sayHello
  (JNIEnv *env, jobject thisobject, jstring js)

{
	return js;
}
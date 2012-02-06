// nativetest.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"


#include "nativetest.h"	/*double quotes tells it to search current directory*/

JNIEXPORT jstring JNICALL Java_nativetest_sayHello
  (JNIEnv *env, jobject thisobject, jstring js)

{
	return js;
}
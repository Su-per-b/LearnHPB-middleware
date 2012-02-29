// nativetest.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"

#include "JNIinterface.h"	
#include "MainFMUwrapper.h"


#define JNI_FALSE  0 
#define JNI_TRUE   1 

Straylight::MainFMUwrapper * fmuWrapper;

wchar_t * JavaToWSZ(JNIEnv* env, jstring string)
{
    if (string == NULL)
        return NULL;
    int len = env->GetStringLength(string);
    const jchar* raw = env->GetStringChars(string, NULL);
    if (raw == NULL)
        return NULL;

    wchar_t* wsz = new wchar_t[len+1];
    memcpy(wsz, raw, len*2);
    wsz[len] = 0;

    env->ReleaseStringChars(string, raw);

    return wsz;
}

char * JavaToSZ(JNIEnv* env, jstring string)
{
    if (string == NULL)
        return NULL;
    int len = env->GetStringLength(string);
    const jchar* raw = env->GetStringChars(string, NULL);
    if (raw == NULL)
        return NULL;

    char* sz = new char[len+1];
    memcpy(sz, raw, len);
    sz[len] = 0;

    env->ReleaseStringChars(string, raw);

    return sz;
}



JNIEXPORT jstring JNICALL Java_com_sri_straylight_socketserver_JNIinterface_load
  (JNIEnv *env, jobject thisobject, jstring unzipfolder) {

    char * unzipfolder_wchar = JavaToSZ(env, unzipfolder);

	fmuWrapper = new Straylight::MainFMUwrapper();
	fmuWrapper->parseXML(unzipfolder_wchar);

	jstring jstrBuf = env->NewStringUTF("load");
	return jstrBuf;
}




JNIEXPORT jstring JNICALL Java_com_sri_straylight_socketserver_JNIinterface_sayHello
  (JNIEnv *env, jobject thisobject, jstring js)

{
	return js;
}




JNIEXPORT jstring JNICALL Java_com_sri_straylight_socketserver_JNIinterface_test1
  (JNIEnv * env, jobject) {



	jstring jstrBuf = env->NewStringUTF("Testing 1!");

	return jstrBuf;
}

/*
 * Class:     FMUwrapper
 * Method:    initAll
 * Signature: ()V
 */
JNIEXPORT jstring JNICALL Java_com_sri_straylight_socketserver_JNIinterface_initAll
  (JNIEnv * env, jobject) 
{
	fmuWrapper = new Straylight::MainFMUwrapper();
	//fmuWrapper->initAll();

	jstring jstrBuf = env->NewStringUTF("Java_com_sri_straylight_socketserver_JNIinterface_initAll");

	return jstrBuf;

}




/*
 * Class:     FMUwrapper
 * Method:    runStep
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_sri_straylight_socketserver_JNIinterface_runStep
  (JNIEnv * env, jobject) 
{



	fmuWrapper->doOneStep();
	fmiReal d = fmuWrapper->getResultSnapshot();

	std::ostringstream os;
	os << d;

	std::string str = os.str();


	jstring jstrBuf = env->NewStringUTF("Java_com_sri_straylight_socketserver_JNIinterface_runStep");

	return jstrBuf;
}


/*
 * Class:     FMUwrapper
 * Method:    cleanup
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_sri_straylight_socketserver_JNIinterface_cleanup
  (JNIEnv * env, jobject)

{

	jstring jstrBuf = env->NewStringUTF("Java_com_sri_straylight_socketserver_JNIinterface_cleanup");
	return jstrBuf;

}


/*
 * Class:     FMUwrapper
 * Method:    cleanup
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jboolean JNICALL Java_com_sri_straylight_socketserver_JNIinterface_isSimulationComplete
  (JNIEnv * env, jobject)

{
	
	int isComplete= fmuWrapper->isSimulationComplete();
	jboolean result;

	if (isComplete) {
		return JNI_TRUE;
	} else {
		return JNI_FALSE;
	}
	

	return result;

}


/*
 * Class:     JNIinterface
 * Method:    getResultSnapshot
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_sri_straylight_socketserver_JNIinterface_getResultSnapshot
  (JNIEnv *, jobject) 
{

	double result = fmuWrapper->getResultSnapshot();


	return result;

}


/*
 * Class:     JNIinterface
 * Method:    simulateHelperCleanup
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_sri_straylight_socketserver_JNIinterface_simulateHelperCleanup
  (JNIEnv *, jobject)
{


	fmuWrapper->printSummary();
	delete fmuWrapper;

	return JNI_TRUE;
}
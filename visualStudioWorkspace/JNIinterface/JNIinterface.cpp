// nativetest.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"

#include "JNIinterface.h"	
#include "mainFMUwrapper.h"
#include "FMUtester.h"

#define JNI_FALSE  0 
#define JNI_TRUE   1 

Straylight::MainFMUwrapper * fmuLoader;


JNIEXPORT jstring JNICALL Java_JNIinterface_sayHello
  (JNIEnv *env, jobject thisobject, jstring js)

{
	return js;
}


JNIEXPORT jstring JNICALL Java_JNIinterface_test1
  (JNIEnv * env, jobject) {

	Straylight::FMUtester::test1();

	Straylight::FMUtester::test3(
		"DoubleInputDoubleOutput.fmu"
	);

	jstring jstrBuf = env->NewStringUTF("Testing 1!");

	return jstrBuf;
}

/*
 * Class:     FMUwrapper
 * Method:    initAll
 * Signature: ()V
 */
JNIEXPORT jstring JNICALL Java_JNIinterface_initAll
  (JNIEnv * env, jobject) 
{
	fmuLoader = new Straylight::MainFMUwrapper();
	fmuLoader->initAll();

	jstring jstrBuf = env->NewStringUTF("Java_JNIinterface_initAll");

	return jstrBuf;

}




/*
 * Class:     FMUwrapper
 * Method:    runStep
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_JNIinterface_runStep
  (JNIEnv * env, jobject) 
{



	fmuLoader->doOneStep();
	fmiReal d = fmuLoader->getResultSnapshot();

	std::ostringstream os;
	os << d;

	std::string str = os.str();


	jstring jstrBuf = env->NewStringUTF("Java_JNIinterface_runStep");

	return jstrBuf;
}


/*
 * Class:     FMUwrapper
 * Method:    cleanup
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_JNIinterface_cleanup
  (JNIEnv * env, jobject)

{

	jstring jstrBuf = env->NewStringUTF("Java_JNIinterface_cleanup");
	return jstrBuf;

}


/*
 * Class:     FMUwrapper
 * Method:    cleanup
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jboolean JNICALL Java_JNIinterface_isSimulationComplete
  (JNIEnv * env, jobject)

{
	
	int isComplete= fmuLoader->isSimulationComplete();
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
JNIEXPORT jdouble JNICALL Java_JNIinterface_getResultSnapshot
  (JNIEnv *, jobject) 
{

	double result = fmuLoader->getResultSnapshot();


	return result;

}


/*
 * Class:     JNIinterface
 * Method:    simulateHelperCleanup
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_JNIinterface_simulateHelperCleanup
  (JNIEnv *, jobject)
{


	fmuLoader->simulateHelperCleanup();

	return JNI_TRUE;
}
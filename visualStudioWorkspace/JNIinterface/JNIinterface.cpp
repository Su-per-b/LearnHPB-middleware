// nativetest.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"

#include "JNIinterface.h"	



#define JNI_FALSE  0 
#define JNI_TRUE   1 

using namespace std;
using namespace Straylight;

MainFMUwrapper * fmuWrapper;

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

 char * JavaToSZ(JNIEnv* env, jstring str)
{


    if (str == NULL)
       return NULL;

    int len = env->GetStringLength(str);
	char chString[4];

	//convert to string
	itoa(len, chString, 10);
	//printf("JavaToSZ len  '%s'\n", chString);

	const char* ansiString = env->GetStringUTFChars(str, 0);
	//printf("ansiString  '%s'\n", ansiString);

	char *outStr = new char[len + 1];
	std::strcpy ( outStr, ansiString );

	printf("JavaToSZ outStr '%s'\n", outStr);


    return outStr;
}



JNIEXPORT jstring JNICALL Java_com_sri_straylight_socketserver_JNIinterface_load
  (JNIEnv *env, jobject thisobject, jstring unzipfolder) {


	printf("Java_com_sri_straylight_socketserver_JNIinterface_load:\n");

    char * unzipfolder_char = JavaToSZ(env, unzipfolder);

	char * wfmuUnzippedFolder = wstrdup (_T("C:\\Temp\\LearnGB_VAVReheat_ClosedLoop"));

	fmuWrapper = new MainFMUwrapper();
	int result = fmuWrapper->parseXML(wfmuUnzippedFolder);

	if (result != 0) {
		exit(EXIT_FAILURE);
	}



	jstring jstrBuf = env->NewStringUTF("load");
	return jstrBuf;


}

char * wstrdup(_TCHAR * wSrc)
{
    int l_idx=0;
    int l_len = wstrlen(wSrc);
    char * l_nstr = (char*)malloc(l_len);
    if (l_nstr) {
        do {
           l_nstr[l_idx] = (char)wSrc[l_idx];
           l_idx++;
        } while ((char)wSrc[l_idx]!=0);
    }
    l_nstr[l_idx] = 0;
    return l_nstr;
}

// returns number of TCHARs in string
int wstrlen(_TCHAR * wstr)
{
    int l_idx = 0;
    while (((char*)wstr)[l_idx]!=0) l_idx+=2;
    return l_idx;
} 

JNIEXPORT jstring JNICALL Java_com_sri_straylight_socketserver_JNIinterface_sayHello
  (JNIEnv *env, jobject thisobject, jstring js)

{
	return js;
}




JNIEXPORT jstring JNICALL Java_com_sri_straylight_socketserver_JNIinterface_test1
  (JNIEnv * env) {


	printf("JNIinterface_test1:\n");


  //  char * unzipfolder_char = JavaToSZ(env, unzipfolder);

	char * wfmuUnzippedFolder = wstrdup (_T("C:\\Temp\\LearnGB_VAVReheat_ClosedLoop"));

	fmuWrapper = new MainFMUwrapper();
	int result = fmuWrapper->parseXML(wfmuUnzippedFolder);

	if (result != 0) {
		exit(EXIT_FAILURE);
	}


	printf("fmuWrapper->loadDll:\n");
	fmuWrapper->loadDll();

	printf("fmuWrapper->simulateHelperInit:\n");
	fmuWrapper->simulateHelperInit();


	//jstring jstrBuf = env->NewStringUTF("load");


	//jstring jstrBuf = env->NewStringUTF("Testing 1!");

	return NULL;
}

/*
 * Class:     FMUwrapper
 * Method:    initAll
 * Signature: ()V
 */
JNIEXPORT jstring JNICALL Java_com_sri_straylight_socketserver_JNIinterface_initAll
  (JNIEnv * env, jobject) 
{
	jstring jstrBuf = env->NewStringUTF("JNIinterface_initAll");

	Java_com_sri_straylight_socketserver_JNIinterface_test1(env);



	return jstrBuf;


	printf("fmuWrapper->loadDll:\n");


	fmuWrapper->loadDll();

	printf("fmuWrapper->simulateHelperInit:\n");


	fmuWrapper->simulateHelperInit();




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
  (JNIEnv * env, jobject) 
{

	double result = fmuWrapper->getResultSnapshot();


	return result;

}



/*
 * Class:     JNIinterface
 * Method:    getResultSnapshot
 * Signature: ()D
 */
JNIEXPORT jstring JNICALL Java_com_sri_straylight_socketserver_JNIinterface_getResultItemAsString
  (JNIEnv * env, jobject) 
{

	Straylight::ResultItem * ri;

	fmuWrapper->doOneStep();
	ri = fmuWrapper->getResultItem();
	char* str = ri->getString();

	jstring jstrBuf = env->NewStringUTF(str);
	return jstrBuf;


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
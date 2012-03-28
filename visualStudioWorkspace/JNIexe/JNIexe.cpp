// JNIexe.cpp : Defines the entry point for the console application.
//


#include "JNIexe.h"


int _tmain(int argc, _TCHAR* argv[])
{


	/*
	//JNIinterface j;
    JavaVM *jvm;       // denotes a Java VM 
    JNIEnv *env;       // pointer to native method interface 
    JavaVMInitArgs vm_args; /// JDK/JRE 6 VM initialization arguments 
    JavaVMOption* options = new JavaVMOption[1];
    options[0].optionString = "-Djava.class.path=/usr/lib/java";
    vm_args.version = JNI_VERSION_1_6;
    vm_args.nOptions = 1;
    vm_args.options = options;
    vm_args.ignoreUnrecognized = false;
    // load and initialize a Java VM, return a JNI interface
     // pointer in env 
    JNI_CreateJavaVM(&jvm, (void **) &env, &vm_args);
	*/


	jstring hh;
	JNIEnv * env = new JNIEnv();

	Java_com_sri_straylight_socketserver_JNIinterface_test1(env);
	

	return 0;
}


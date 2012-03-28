package com.sri.straylight.socketserver;


import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;



public interface JNAwrapper extends Library {

	Add  INSTANCE = (Add ) Native.loadLibrary("fmutest", Add .class);
	
	void test1();

}
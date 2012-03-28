package com.sri.straylight.socketserver;

public class JNIinterface {
	
	static {
		System.loadLibrary("JNIinterface");
	}
	
	public native String sayHello(String s);
	
	public native String test1();
	
	public native String initAll();
	
	public native String runStep();
	
	public native String cleanup();
	
	public native String getResultItemAsString();
	
	public native String load(String fmuUnzippedFolder);
	
	public native boolean isSimulationComplete();
	
	public native double getResultSnapshot();
	
	public native boolean simulateHelperCleanup();
	
	
}

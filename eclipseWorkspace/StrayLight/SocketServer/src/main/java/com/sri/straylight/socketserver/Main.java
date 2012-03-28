package com.sri.straylight.socketserver;


import com.sun.jna.Library; 
import com.sun.jna.Native;
import com.sun.jna.Platform;

import java.net.URL;


public class Main 
{
	
	public static Config config;
	public static FMU fmu_;
	
	
    public static void main( String[] args )
    {
    	

    	
    	config = ConfigHelper.load();


    	
    	
    	testFMU();
    	
      //  SocketServer server = new SocketServer();
	    
       // server.showBanner();
     //   server.start();
        
       // Jmodelica jmodelica = new Jmodelica();
       // jmodelica.test4();
        
        
    }
    
    public static void test( ) {
    	
    	
    	fmu_ = new FMU(config.testFmuFile);
    	fmu_.unzip();
    	
    	fmu_.load();
    	
    	fmu_.init();
    	
    	
    }
    
    public static void test2( ) {
    	
    	
    	 JNIinterface jni = new JNIinterface();
    	// jni.test1();
    	
    	String res = jni.initAll();
    }
    
    public static void testAdd( ) {
    	
    	String result = System.setProperty("jna.library.path", "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug");
    	//System.setProperty("java.library.path", "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug");

    	Add lib = (Add) Native.loadLibrary("fmutest", Add.class);

        System.out.println(lib.add(10, 20));

   }
    
    public static void testFMU( ) {
    	
    	String result = System.setProperty("jna.library.path", "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug");
    	FMUwrapper lib = (FMUwrapper) Native.loadLibrary("FMUwrapper", FMUwrapper.class);
    	
    	
    	lib.testFMU();
    	
       // Add lib = Add.INSTANCE;
       // System.out.println(lib.add(10, 20));

   }
    
}

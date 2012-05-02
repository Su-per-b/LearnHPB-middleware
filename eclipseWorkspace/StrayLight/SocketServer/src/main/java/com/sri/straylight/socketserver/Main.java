package com.sri.straylight.socketserver;

import com.sri.straylight.fmuWrapper.*;



public class Main 
{
	
	public static Config config;
	public static FMU fmu_;
	//private JNAfmuWrapper  JNAfmuWrapper_;
	public static JNAfmuWrapper jnaFMUWrapper_;
	
	public static String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
	
    public static void main( String[] args )
    {
    	
		config = ConfigHelper.load();
		System.setProperty("jna.library.path", config.dllFolder);
		
		SocketServer ss = new SocketServer();
		
		ss.showBanner();
		ss.start();
		
		//test2();
			



    }
    
    public static void test( ) {
    	
    	
    	
    	
//    	fmu_ = new FMU(config.testFmuFile);
//    	fmu_.unzip();
//    	fmu_.init(unzipFolder);
    	
    }
    
    public static void test2( ) {
    	
//		fmu_ = new FMU(config.testFmuFile);
//		fmu_.init(unzipFolder);
//	    
		//ArrayList<ScalarVariableMeta> inList = fmu_.getInputs();
		//ArrayList<ScalarVariableMeta> outList = fmu_.getOutputs();
		
		
		//fmu_.test();
		fmu_.run();
    	
    }
    


    
}

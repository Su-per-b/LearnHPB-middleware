package com.sri.straylight.socketserver;


import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.Library; 
import com.sun.jna.Native;
import com.sun.jna.Platform;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sri.straylight.fmu.*;


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
		
		
    ///	String str = JNA2.INSTANCE.testcpp();
    	

			
		fmu_ = new FMU(config.testFmuFile);
		fmu_.init(unzipFolder);
	    
		ArrayList<ScalarVariableMeta> inList = fmu_.getInputs();
		ArrayList<ScalarVariableMeta> outList = fmu_.getOutputs();
		
		
		//fmu_.test();
		fmu_.run();
		
		//int x = 0;

    }
    
    public static void test( ) {
    	
    	fmu_ = new FMU(config.testFmuFile);
    	fmu_.unzip();
    	fmu_.init(unzipFolder);
    	
    }
    
    public static void testFMU3( )  {
        
    	fmu_ = new FMU(config.testFmuFile);
    	fmu_.unzip();
    	fmu_.init(unzipFolder);
    	fmu_.run();

   }
    
    public static void testFMU6( )  {
    	TestEnum tt = TestEnum.FILE;
    	//tt.
    	
    	fmu_ = new FMU(config.testFmuFile);
    	fmu_.init(unzipFolder);
    	
   }
    


    
}

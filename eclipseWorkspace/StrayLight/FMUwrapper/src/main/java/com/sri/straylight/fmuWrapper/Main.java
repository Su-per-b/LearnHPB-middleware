package com.sri.straylight.fmuWrapper;

import java.util.ArrayList;


public class Main 
{
	
	public static Config config;
	public static FMU fmu_;
	public static JNAfmuWrapper jnaFMUWrapper_;
	public static String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
	
    public static void main( String[] args )
    {
    	
		config = ConfigHelper.load();
		//System.setProperty("jna.library.path", config.dllFolder);
		
			
		fmu_ = new FMU(config.testFmuFile, config.dllFolder);
		fmu_.init(unzipFolder);
	    
		ArrayList<ScalarVariableMeta> inList = fmu_.getInputs();
		ArrayList<ScalarVariableMeta> outList = fmu_.getOutputs();

		fmu_.run();

    }
    
    public static void test( ) {
    	
    	fmu_ = new FMU(config.testFmuFile , config.dllFolder);
    	fmu_.unzip();
    	fmu_.init(unzipFolder);
    	
    }
    

    


    
}

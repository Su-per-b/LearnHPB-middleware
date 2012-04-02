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
    	//testFMU3();
    	
    	
    
    }
    
    public static void test( ) {
    	
    	fmu_ = new FMU(config.testFmuFile);
    	fmu_.unzip();
    //	fmu_.load();
    	fmu_.init();
    	
    }
    
    public static void testFMU3( )  {
        
    	fmu_ = new FMU(config.testFmuFile);
    	fmu_.unzip();
    	fmu_.init();
    	
    	//ResultEventListener listener = new ResultEventListener();
    	//ResultEventDispatacher disp = new ResultEventDispatacher();
    //	ResultEventListener list = new ResultEventListener();
    	//disp.addListener(list);

    	fmu_.run();
    	
    	
   }
    
    
    public static void testFMU( )  {
    
    	JNAfmuWrapper.INSTANCE.testFMU();
   }
    
    public static void testFMU2( )  {
        

    	JNAfmuWrapper.INSTANCE.initAll();
    	while(JNAfmuWrapper.INSTANCE.isSimulationComplete() == 0) {
    		
        	String str = JNAfmuWrapper.INSTANCE.getStringXy();
        	System.out.println("getStringXy " + str);
    	}
    	
    	
    	JNAfmuWrapper.INSTANCE.end();
    	
   }
    
}

package com.sri.straylight.socketserver;



import java.net.URL;



public class Main 
{
	
	public static Config config;
	public static FMU fmu_;
	
	
    public static void main( String[] args )
    {
    	

    	
    	config = ConfigHelper.load();


    	
    	
    	test();
    	
        SocketServer server = new SocketServer();
	    
        server.showBanner();
        server.start();
        
       // Jmodelica jmodelica = new Jmodelica();
       // jmodelica.test4();
        
        
    }
    
    public static void test( ) {
    	
    	
    	fmu_ = new FMU(config.testFmuFile);
    	fmu_.unzip();
    	
    	fmu_.load();
    	
    	
    }
    
}

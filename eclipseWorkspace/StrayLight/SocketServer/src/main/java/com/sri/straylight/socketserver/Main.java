package com.sri.straylight.socketserver;



import java.net.URL;



public class Main 
{
	
	public static Config config;
	
	
    public static void main( String[] args )
    {
    	
    	//JNIinterface jniInterface = new JNIinterface();
    	
    	
    	//String result = jniInterface.sayHello("sayHello");
    	
    	
    	config = ConfigHelper.load();

    	//ConfigHelper.make();
    	
    	
        SocketServer server = new SocketServer();
	    
        server.showBanner();
        server.start();
        
       // Jmodelica jmodelica = new Jmodelica();
       // jmodelica.test4();
        
        
    }
    
    
}

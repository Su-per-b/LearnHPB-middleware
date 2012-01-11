package com.sri.straylight.socketserver;


public class Main 
{
    public static void main( String[] args )
    {
    	

        
        SocketServer server = new SocketServer();
	    
        server.showBanner();
        server.start();
        
       // Jmodelica jmodelica = new Jmodelica();
       // jmodelica.test4();
        
        
    }
}

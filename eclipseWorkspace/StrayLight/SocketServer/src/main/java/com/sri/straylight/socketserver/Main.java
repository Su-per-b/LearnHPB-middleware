package com.sri.straylight.socketserver;


public class Main 
{
    public static void main( String[] args )
    {
    	
        Jmodelica jmodelica = new Jmodelica();
        jmodelica.test();
        
    	SocketServer server = new SocketServer();
        
        server.showBanner();
        server.start();
        
    }
}

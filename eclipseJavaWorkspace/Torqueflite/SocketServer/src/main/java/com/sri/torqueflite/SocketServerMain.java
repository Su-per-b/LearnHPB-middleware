package com.sri.torqueflite;


public class SocketServerMain 
{
    public static void main( String[] args )
    {
    	SocketServer server = new SocketServer();
        
        server.showBanner();
        server.start();
    }
}

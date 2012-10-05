package com.sri.straylight.socketserver;




public class Main 
{
	
	public static Config config;
	

	
    public static void main( String[] args )
    {

    	
		config = ConfigHelper.load();
		System.setProperty("jna.library.path", config.dllFolder);
		
		SocketServer ss = new SocketServer();
		
		ss.showBanner();
		ss.start();
		

    }



    
}

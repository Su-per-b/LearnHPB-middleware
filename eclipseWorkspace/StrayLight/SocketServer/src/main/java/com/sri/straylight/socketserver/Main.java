package com.sri.straylight.socketserver;

import com.sri.straylight.fmuWrapper.*;



public class Main 
{
	
	public static Config config;
	//public static String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
//	
    public static void main( String[] args )
    {
    	
		config = ConfigHelper.load();
		System.setProperty("jna.library.path", config.dllFolder);
		
		SocketServer ss = new SocketServer();
		
		ss.showBanner();
		ss.start();
		

    }



    
}

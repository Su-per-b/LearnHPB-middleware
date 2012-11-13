package com.sri.straylight.socketserver;

import com.sri.straylight.socketserver.controller.MainController;




public class Main 
{
	
	public static Config config;
	

	
    public static void main( String[] args )
    {

    	
		config = ConfigHelper.load();
		System.setProperty("jna.library.path", config.dllFolder);
		
		
		MainController mainController = new MainController();
		mainController.init();
		
		

		

    }



    
}

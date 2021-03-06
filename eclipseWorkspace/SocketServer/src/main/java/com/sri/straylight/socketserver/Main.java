package com.sri.straylight.socketserver;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.bushe.swing.event.EventService;

import com.sri.straylight.socketserver.controller.MainController;


public class Main 
{
	
	//public static SocketServerConfig config;
	public static MainController mainController_;

	
    public static void main( String[] args )
    {
    	
    	DOMConfigurator.configure(Main.class.getResource("/log4j.xml"));
    	Logger  logger = Logger.getLogger(EventService.class.getCanonicalName());
    	
    	logger.setLevel(Level.WARN);
    	
		//config = ConfigHelper.load();
		
		mainController_ = new MainController();
		mainController_.init();

    }

    
    public static void stop()
    {
    	mainController_.stop();
    }
    
    
}

package com.sri.straylight.client;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.EventServiceLocator;

import com.sri.straylight.client.controller.MainController;
import com.sri.straylight.fmuWrapper.event.ExceptionThrowingEventService;
import com.sri.straylight.fmuWrapper.framework.AbstractController;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main {
	
	/** The application controller. */
	public static AbstractController applicationController;
	
	
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	

            	
        		System.setProperty(EventServiceLocator.SERVICE_NAME_EVENT_BUS,
        				ExceptionThrowingEventService.class.getName());
        		
        		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        		
        		//InputStream is = classLoader.getResourceAsStream("log4j.xml");
        		
        		//	DOMConfigurator.configure("log4j.xml");
            	//
            	
            	DOMConfigurator.configure(Main.class.getResource("/log4j.xml"));
            	Logger  logger = Logger.getLogger(EventService.class.getCanonicalName());
            	
            	logger.setLevel(Level.WARN);
            	
    	 		try {
		 			EventServiceLocator.setEventService(EventServiceLocator.SERVICE_NAME_EVENT_BUS,
		 				new ExceptionThrowingEventService());
		 		} catch (EventServiceExistsException e) {
		 			logger.warn("Unable to register EventService.", e);
		 		}
            	
    	 		
            	
            	applicationController = new MainController();
            }
        });
    }
    


    
}

package com.sri.straylight.client;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.bushe.swing.event.EventService;


import com.sri.straylight.client.controller.MainController;
import com.sri.straylight.client.framework.AbstractController;

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
            	DOMConfigurator.configure("log4j.xml");
            	Logger  logger = Logger.getLogger(EventService.class.getCanonicalName());
            	logger.setLevel(Level.ERROR);
            	applicationController = new MainController();
            }
        });
    }
    


    
}

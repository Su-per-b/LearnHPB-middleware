package com.sri.straylight.client;

import org.apache.log4j.xml.DOMConfigurator;

import com.sri.straylight.client.controller.MainController;
import com.sri.straylight.client.controller.MainController2;
import com.sri.straylight.client.framework.AbstractController;

public class Main {
	
	public static AbstractController applicationController;
	
	
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	
            	
            	DOMConfigurator.configure("log4j.xml");
            	
            	//MainController.getInstance().init();
            	
            	applicationController = new MainController2();
            	
            	
            }
        });
    }
    


    
}

package com.sri.straylight.client.controller;

import javax.swing.SwingUtilities;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.view.BaseView;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;



public class BaseController extends AbstractController {
	
	public BaseController(AbstractController parentController) {
		super(parentController);
	}
	
	protected void init_(XMLparsed xmlParsed) {  
		

	    
    }
	
	
	@EventSubscriber(eventClass=XMLparsedEvent.class)
	public void onXMLparsedEvent(final XMLparsedEvent event) {  

		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
			    init_(event.xmlParsed);
		    }
		});
    }
	
	
    /**
     * Gets the view.
     *
     * @return the view
     */
    public BaseView getView() {
    	
    	if (view == null) {
    		return null;
    	} else {
    		return (BaseView) view;
    	}
    }
    
    
	
}

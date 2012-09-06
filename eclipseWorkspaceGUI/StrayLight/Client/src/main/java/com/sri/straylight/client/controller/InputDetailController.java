package com.sri.straylight.client.controller;

import java.util.Vector;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ScalarValueChangeRequest;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.client.view.InputDetailView;
import com.sri.straylight.client.view.InputFormView;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;

// TODO: Auto-generated Javadoc
/**
 * The Class InputDetailController.
 */
public class InputDetailController extends AbstractController {



    /** The xml parsed_. */
    private XMLparsed xmlParsed_;
    
	/**
	 * Instantiates a new input detail controller.
	 *
	 * @param parentController the parent controller
	 */
	public InputDetailController(AbstractController parentController) {
		super(parentController);
	}
	


	/**
	 * Inits the.
	 *
	 * @param xmlParsed the xml parsed
	 */
	public void init(XMLparsed xmlParsed) {  
		
		xmlParsed_ = xmlParsed;
		InputDetailView theView = new InputDetailView(this, xmlParsed_);
	    setView_(theView);

    }
	
	
	/**
	 * Reset.
	 *
	 * @param xmlParsed the xml parsed
	 */
	public void reset(XMLparsed xmlParsed) {  
		xmlParsed_ = xmlParsed;
		
		InputDetailView theView = (InputDetailView) this.getView();
	//	theView.reset(xmlParsed_);
	}
	
	

	/**
	 * On result event.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {

		
		InputDetailView theView = (InputDetailView) this.getView();
		theView.setResult( event.getScalarValueResults() );
		
		
		
		

		 
	}



	/**
	 * On data model update request.
	 *
	 * @param event the event
	 */
	public void onDataModelUpdateRequest(ScalarValueChangeRequest event) {
		EventBus.publish(event);
	}
	

	
	
	

    
}

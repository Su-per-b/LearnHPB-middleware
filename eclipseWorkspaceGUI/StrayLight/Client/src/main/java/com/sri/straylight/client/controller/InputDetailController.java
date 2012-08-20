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

public class InputDetailController extends AbstractController {



    private XMLparsed xmlParsed_;
    
	public InputDetailController(AbstractController parentController) {
		super(parentController);
	}
	


	public void init(XMLparsed xmlParsed) {  
		
		xmlParsed_ = xmlParsed;
		InputDetailView theView = new InputDetailView(this, xmlParsed_);
	    setView_(theView);

    }
	
	
	public void reset(XMLparsed xmlParsed) {  
		xmlParsed_ = xmlParsed;
		
		InputDetailView theView = (InputDetailView) this.getView();
	//	theView.reset(xmlParsed_);
	}
	
	

	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {

		
		InputDetailView theView = (InputDetailView) this.getView();
		theView.setResult( event.resultOfStep );
		
		
		
		

		 
	}



	public void onDataModelUpdateRequest(ScalarValueChangeRequest event) {
		EventBus.publish(event);
	}
	

	
	
	

    
}

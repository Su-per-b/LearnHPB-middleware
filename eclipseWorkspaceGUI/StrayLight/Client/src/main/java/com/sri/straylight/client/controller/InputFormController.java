package com.sri.straylight.client.controller;

import java.util.Vector;

import javax.swing.JOptionPane;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ScalarValueChangeRequest;
import com.sri.straylight.client.event.menu.About_Help;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.client.model.ClientConfigXML;
import com.sri.straylight.client.model.InputFormDataModel;
import com.sri.straylight.client.view.InputFormView;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;

// TODO: Auto-generated Javadoc
/**
 * The Class InputFormController.
 */
public class InputFormController extends AbstractController {



    /** The input form data model_. */
    private InputFormDataModel inputFormDataModel_;
    
	/**
	 * Instantiates a new input form controller.
	 *
	 * @param parentController the parent controller
	 */
	public InputFormController(AbstractController parentController) {
		super(parentController);
	}
	


	/**
	 * Inits the.
	 *
	 * @param xmlParsed the xml parsed
	 */
	public void init(XMLparsed xmlParsed) {  
		
		
		inputFormDataModel_ = new InputFormDataModel();
		inputFormDataModel_.xmlParsed = xmlParsed;
		
		

		InputFormView theView = new InputFormView(this, inputFormDataModel_);
	    setView_(theView);

    }
	
	
	/**
	 * Reset.
	 *
	 * @param xmlParsed the xml parsed
	 */
	public void reset(XMLparsed xmlParsed) {  
		//	xmlParsed_ = xmlParsed;
		
		//InputFormView theView = (InputFormView) this.getView();
		//theView.reset(xmlParsed_);
	}
	
	

	/**
	 * On result event.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {

		
		ScalarValueResults scalarValueResults = event.getScalarValueResults();
		
		Vector<String> stringList = scalarValueResults.input.getStringList();
		InputFormView theView = (InputFormView) this.getView();
		 
		 theView.newResult(stringList);
		 
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

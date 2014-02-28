package com.sri.straylight.client.controller;

import javax.swing.SwingUtilities;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ViewInitialized;
import com.sri.straylight.client.model.InputDataModel;
import com.sri.straylight.client.view.TableOfVariablesView;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class InputFormController.
 */
public class InputVariablesController extends BaseController {


    /** The input form data model_. */
    private InputDataModel inputDataModel_;
    
    
	/**
	 * Instantiates a new input form controller.
	 *
	 * @param parentController the parent controller
	 */
	public InputVariablesController(AbstractController parentController) {
		super(parentController);
	}
	

	public void initXML( XMLparsedInfo xmlParsed) {  

		inputDataModel_ = new InputDataModel();
		inputDataModel_.xmlParsed = xmlParsed;
		
		TableOfVariablesView theView = new TableOfVariablesView(this, inputDataModel_);
	    setView_(theView);
	
	    ViewInitialized e = new ViewInitialized(this, theView);
	    EventBus.publish(e);

    }
	
	@EventSubscriber(eventClass=XMLparsedEvent.class)
	public void onXMLparsedEventEX(final XMLparsedEvent event) {  

		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
			    initXML(event.getPayload());
		    }
		});
    }
	

	/**
	 * On result event.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {
		
		TableOfVariablesView theView = (TableOfVariablesView) this.getView();
		ScalarValueResults svr = event.getPayload();
		
		theView.addResult( svr );
		
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

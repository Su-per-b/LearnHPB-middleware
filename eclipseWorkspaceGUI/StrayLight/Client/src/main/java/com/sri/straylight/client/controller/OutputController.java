package com.sri.straylight.client.controller;

import javax.swing.SwingUtilities;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ViewInitialized;
import com.sri.straylight.client.model.OutputDataModel;
import com.sri.straylight.client.view.OutputView;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsed;


// TODO: Auto-generated Javadoc
/**
 * The Class ResultsFormController.
 */
public class OutputController extends BaseController {
	
	
    private OutputDataModel outputDataModel_;
    
    
	/**
	 * Instantiates a new results form controller.
	 *
	 * @param parentController the parent controller
	 */
	public OutputController(AbstractController parentController) {
		super(parentController);
	}
	
	/**
	 * Inits the.
	 *
	 * @param xmlParsed the xml parsed
	 */
	protected void init_(XMLparsed xmlParsed) {  
		
		outputDataModel_ = new OutputDataModel(xmlParsed);
		OutputView theView = new OutputView(this, outputDataModel_);
	    setView_(theView);
	   
	    ViewInitialized e = new ViewInitialized(this, theView);
	    EventBus.publish(e);
    }
	
	
	/**
	 * On result event.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {
		
		final ScalarValueResults scalarValueResults = event.getPayload();
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	
		    	OutputView outputView = (OutputView) getView();
		    	
		    	outputView.updateResults(scalarValueResults);
		    }
		});
		
	}
	


}
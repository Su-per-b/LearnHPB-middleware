package com.sri.straylight.client.controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ClearViewAction;
import com.sri.straylight.client.event.ViewInitialized;
import com.sri.straylight.client.model.ResultsLogModel;
import com.sri.straylight.client.view.ResultsLogView;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.Enu;


// TODO: Auto-generated Javadoc
/**
 * The Class ResultsTableController.
 */
public class ResultsLogController extends BaseController {
	

    private ResultsLogModel dataModel_;
    

	/**
	 * Instantiates a new results table controller.
	 *
	 * @param parentController the parent controller
	 */
	public ResultsLogController(AbstractController parentController) {
		super(parentController);
	}
	
	
	/**
	 * Inits the.
	 *
	 * @param xmlParsed the xml parsed
	 */
	protected void initXML(XMLparsedInfo xmlParsed) {  

		Vector<ScalarVariableReal> variables = xmlParsed.getVariables(Enu.enu_output);
		dataModel_ = new ResultsLogModel(variables);
		
		
		ResultsLogView theView = new ResultsLogView(this, dataModel_);
	    
	    theView.setPreferredSize(new Dimension(704, 500));
	    theView.setLayout(new GridLayout(1, 1, 0, 0));
	    
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

		final ScalarValueResults scalarValueResults = event.getPayload();
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	
		    	dataModel_.addNewResult(scalarValueResults);
		    	
		    	ResultsLogView theView = (ResultsLogView) getView();
		    	theView.update();
		    }
		});
		
		
	}
	

	
	/**
	 * On clear debug console action.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ClearViewAction.class)
	public void onClearDebugConsoleAction(ClearViewAction event) {
		
		dataModel_.clear();
		
		ResultsLogView theView = (ResultsLogView) getView();
		theView.clear();
    	
	}

	
	

	
	
}
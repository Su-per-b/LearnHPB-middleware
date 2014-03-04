package com.sri.straylight.client.controller;

import java.util.Vector;

import javax.swing.SwingUtilities;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ViewInitialized;
import com.sri.straylight.client.model.VariableDataModel;
import com.sri.straylight.client.view.TableOfVariablesView;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.Enu;


// TODO: Auto-generated Javadoc
/**
 * The Class ResultsFormController.
 */
public class OutputVariablesController extends BaseController {
	
	
    private VariableDataModel dataModel_;
    
    
	/**
	 * Instantiates a new results form controller.
	 *
	 * @param parentController the parent controller
	 */
	public OutputVariablesController(AbstractController parentController) {
		super(parentController);
	}
	

	public void initXML( XMLparsedInfo xmlParsed) {  

		
		Vector<ScalarVariableReal> variables = xmlParsed.getVariables(Enu.enu_output);
		
		dataModel_ = new VariableDataModel(variables);
		
		
//		dataModel_ = new OutputDataModel(xmlParsed);

		TableOfVariablesView theView = new TableOfVariablesView(this, dataModel_, "Output");
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
		
//		TableOfVariablesView theView = (TableOfVariablesView) this.getView();
		
		ScalarValueResults svr = event.getPayload();
		
		ScalarValueCollection svc = svr.getOutput();
		dataModel_.addResult( svc );
		
	}
	



}
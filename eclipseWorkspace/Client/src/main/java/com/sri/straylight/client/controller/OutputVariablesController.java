package com.sri.straylight.client.controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.TabViewInitialized;
import com.sri.straylight.client.model.ResultsLogModel;
import com.sri.straylight.client.model.VariableDataModel;
import com.sri.straylight.client.view.ResultsLogView;
import com.sri.straylight.client.view.TableOfVariablesView;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.Enu;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


// TODO: Auto-generated Javadoc
/**
 * The Class ResultsFormController.
 */
public class OutputVariablesController extends BaseController {
	
	
    private VariableDataModel dataModel_;
    
    private TableOfVariablesView view_;
    
	/**
	 * Instantiates a new results form controller.
	 *
	 * @param parentController the parent controller
	 */
	public OutputVariablesController(AbstractController parentController) {
		super(parentController);
	}
	

	public void initXML( XMLparsedInfo xmlParsed) {  

		if (view_ == null) {
			
			Vector<ScalarVariableReal> variables = xmlParsed.getVariables(Enu.enu_output);
			dataModel_ = new VariableDataModel("Output", variables);

			view_ = new TableOfVariablesView( dataModel_, this);
		    setView_(view_);
		
		    TabViewInitialized event = new TabViewInitialized(this, view_);
		    EventBus.publish(event);
		} else {
			dataModel_.clear();
		}
		
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
		
		ScalarValueResults svr = event.getPayload();
		
		ScalarValueCollection svc = svr.getOutput();
		dataModel_.addResult( svc );
		
	}
	
	
	@EventSubscriber(eventClass = SimStateClientNotify.class)
	public void onSimStateNativeNotify(final SimStateClientNotify event) {

		SimStateNative simStateNative = event.getPayload();
		
		if (simStateNative == SimStateNative.simStateNative_8_tearDown_completed) {
			
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
					dataModel_.clear();
			    }
			});
		}
		
		
	}


}
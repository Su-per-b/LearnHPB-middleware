package com.sri.straylight.client.controller;

import java.util.Vector;

import javax.swing.SwingUtilities;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.TabViewInitialized;
import com.sri.straylight.client.model.VariableDataModel;
import com.sri.straylight.client.view.TableOfVariablesView;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.StraylightEventListener;
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
 * The Class InputFormController.
 */
public class InputVariablesController extends BaseController {


    /** The input form data model_. */
    private VariableDataModel dataModel_;
    
    
	/**
	 * Instantiates a new input form controller.
	 *
	 * @param parentController the parent controller
	 */
	public InputVariablesController(AbstractController parentController) {
		super(parentController);
	}
	

	public void initXML( XMLparsedInfo xmlParsed) {  

		if (view_ == null) {
			
			Vector<ScalarVariableReal> variables = xmlParsed.getVariables(Enu.enu_input);
			dataModel_ = new VariableDataModel("Input", variables);
			TableOfVariablesView theView = new TableOfVariablesView(dataModel_, this);
			
			registerEventListener (
					ScalarValueChangeRequest.class,
					new StraylightEventListener<ScalarValueChangeRequest, ScalarValueCollection>() {
						@Override
						public void handleEvent(ScalarValueChangeRequest event) {
							
							 EventBus.publish(event);
						}
					});
			
			
		    setView_(theView);
		
		    TabViewInitialized event = new TabViewInitialized(this, theView);
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
		
		ScalarValueCollection svc = svr.getInput();
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

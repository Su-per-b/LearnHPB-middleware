package com.sri.straylight.client.controller;

import java.util.Vector;

import javax.swing.SwingUtilities;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.TabViewInitialized;
import com.sri.straylight.client.model.VariableDataModel;
import com.sri.straylight.client.view.TableOfVariablesView;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarVariableReal;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.Enu;

// TODO: Auto-generated Javadoc
/**
 * The Class InternalFormController.
 */
public class InternalVariablesController extends BaseController {


    /** The Internal form data model_. */
    private VariableDataModel dataModel_;
    
    
	/**
	 * Instantiates a new Internal form controller.
	 *
	 * @param parentController the parent controller
	 */
	public InternalVariablesController(AbstractController parentController) {
		super(parentController);
	}
	

	public void initXML( XMLparsedInfo xmlParsed) {  
		
		Vector<ScalarVariableReal> variables = xmlParsed.getVariables(Enu.enu_internal);
		dataModel_ = new VariableDataModel("Internal", variables);
		
		TableOfVariablesView theView = new TableOfVariablesView(dataModel_, this);
	    setView_(theView);
	
	    TabViewInitialized event = new TabViewInitialized(this, theView);
	    EventBus.publish(event);
	    
    }
	
	
	@EventSubscriber(eventClass=XMLparsedEvent.class)
	public void onXMLparsedEventEX(final XMLparsedEvent event) {  

		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
			    initXML(event.getPayload());
		    }
		});
    }
	




	
	

    
}

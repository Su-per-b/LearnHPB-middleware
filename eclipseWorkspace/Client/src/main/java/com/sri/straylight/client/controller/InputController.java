package com.sri.straylight.client.controller;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ViewInitialized;
import com.sri.straylight.client.model.InputDataModel;
import com.sri.straylight.client.view.InputView;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class InputFormController.
 */
public class InputController extends BaseController {


    /** The input form data model_. */
    private InputDataModel inputDataModel_;
    
	/**
	 * Instantiates a new input form controller.
	 *
	 * @param parentController the parent controller
	 */
	public InputController(AbstractController parentController) {
		super(parentController);
	}
	

	public void init_( XMLparsedInfo xmlParsed) {  

		inputDataModel_ = new InputDataModel();
		inputDataModel_.xmlParsed = xmlParsed;
		
		InputView theView = new InputView(this, inputDataModel_);
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
		
		InputView theView = (InputView) this.getView();
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

package com.sri.straylight.client.controller;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.SimStateClientRequest;
import com.sri.straylight.client.model.TopPanelDataModel;
import com.sri.straylight.client.view.TopPanelView;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


// TODO: Auto-generated Javadoc
/**
 * The Class TopPanelController.
 */
public class TopPanelController2 extends BaseController {


	/** The simulation state_. */

	
	private TopPanelDataModel topPanelDataModel_;
	
	///private TopPanelView topPanelView_;
	
	
	/**
	 * Instantiates a new top panel controller.
	 *
	 * @param parentController the parent controller
	 */
	public TopPanelController2 (AbstractController parentController) {

		super(parentController);

		
		topPanelDataModel_ = new TopPanelDataModel();
		TopPanelView theView = new TopPanelView(this, topPanelDataModel_);
		setView_(theView);
		//BaseView theView = new BaseView("Top Panel");
	}


	
	/**
	 * On sim state notify.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateClientNotify.class)
	public void onSimStateClientNotify(SimStateClientNotify event) {
		
		SimStateNative simStateNative = event.getPayload();
		TopPanelView theView = (TopPanelView) getView();
		
		theView.updateSimStateClient(simStateNative);
		
	}


	@EventSubscriber(eventClass=SimStateClientRequest.class)
	public void onSimStateClientRequest(SimStateClientRequest event) {
		
		SimStateNative simStateNative = event.getPayload();
		TopPanelView theView = (TopPanelView) getView();
		
		theView.updateSimStateClient(simStateNative);
		
	}



}

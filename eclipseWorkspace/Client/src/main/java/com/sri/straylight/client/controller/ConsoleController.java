package com.sri.straylight.client.controller;

import java.awt.Color;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ClearViewAction;
import com.sri.straylight.client.model.ConsoleModel;
import com.sri.straylight.client.view.ConsoleView;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;



// TODO: Auto-generated Javadoc
/**
 * The Class DebugConsoleController.
 */
public class ConsoleController extends BaseController {


	/**
	 * Instantiates a new debug console controller.
	 *
	 * @param parentController the parent controller
	 */
	public ConsoleController (AbstractController parentController) {
		super(parentController);

		ConsoleModel theModel = new ConsoleModel();
		
		ConsoleView theView = new ConsoleView(this, theModel);
		setView_(theView);
		
	}
	
	
	
	/**
	 * On sim state request.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateNativeRequest.class)
	public void onSimStateRequest(SimStateNativeRequest event) {
		
		ConsoleView view = (ConsoleView) getView();
		view.outputText ("SimStateRequest: " + event.getPayload().toString(), Color.black);
	}
	
	/**
	 * On sim state request.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateNativeNotify.class)
	public void onSimStateRequest(SimStateNativeNotify event) {
		ConsoleView view = (ConsoleView) getView();
		view.outputText ("SimStateNotify: " + event.getPayload().toString(), Color.black);
	}
	
	
	/**
	 * On result event.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ResultEvent.class)
	public void onResultEvent(ResultEvent event) {
		
		ScalarValueResults scalarValueResults = event.getPayload();
		ConsoleView view = (ConsoleView) getView();
		view.outputText (scalarValueResults.toString(), Color.black);
	}
	

	/**
	 * On message event.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=MessageEvent.class)
	public void onMessageEvent(MessageEvent event) {
		
		MessageStruct messageStruct = event.getPayload();
		MessageType type = messageStruct.getMessageTypeEnum();
		
		ConsoleView view = (ConsoleView) getView();
		
		
		switch (type) {
		
			case messageType_debug:
				view.outputText (event.getPayload().msgText,  Color.black);
				break;
			case messageType_info:
				view.outputText (event.getPayload().msgText, Color.black);
				break;
			case messageType_error:
				view.outputText (event.getPayload().msgText, Color.red);
				break;

		}

	}
	
	
	/**
	 * On clear debug console action.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ClearViewAction.class)
	public void onClearDebugConsoleAction(ClearViewAction event) {
    	ConsoleView view = (ConsoleView) getView();
    	view.clear();
	}

	



}

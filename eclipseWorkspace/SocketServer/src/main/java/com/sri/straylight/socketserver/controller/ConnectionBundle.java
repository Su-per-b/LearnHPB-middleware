package com.sri.straylight.socketserver.controller;

import org.bushe.swing.event.EventBus;

import com.sri.straylight.fmuWrapper.Controller.FMUcontroller;
import com.sri.straylight.fmuWrapper.Controller.ThreadedFMUcontroller;
import com.sri.straylight.fmuWrapper.event.ConfigChangeNotify;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.event.StraylightEventListener;
import com.sri.straylight.fmuWrapper.event.XMLparsedEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.XMLparsedInfo;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;
import com.sri.straylight.socketserver.event.MessageReceived;


public class ConnectionBundle extends AbstractController {


	private WebSocketConnectionController webSocketConnectionController_;
	private FMUcontroller fmuController_;
	private int idx_;

	private ThreadedFMUcontroller threadedFMUcontroller_;
	
	
	public ConnectionBundle(AbstractController parent,
			SocketController webSocketConnection) {
		super(null);
		
		setIdx_(webSocketConnection.getIdx());

		webSocketConnectionController_ = new WebSocketConnectionController(this, webSocketConnection);
	}

	public void init() {

		webSocketConnectionController_.init();
		
		
		fmuController_ = new FMUcontroller(this);
		threadedFMUcontroller_ = new ThreadedFMUcontroller(fmuController_);
		
		registerSocketListeners_();
		registerSimulationListeners_();
		
		webSocketConnectionController_.processQuedItem();

	}

	private void registerSimulationListeners_() {
		//SimStateNativeNotify
		fmuController_
		.registerEventListener(
				SimStateNativeNotify.class,
				new StraylightEventListener<SimStateNativeNotify, SimStateNative>() {
					@Override
					public void handleEvent(SimStateNativeNotify event) {
						webSocketConnectionController_.send(event);
					}
				});
		

		//MessageEvent
		fmuController_
		.registerEventListener(
				MessageEvent.class,
				new StraylightEventListener<MessageEvent, MessageStruct>() {
					@Override
					public void handleEvent(MessageEvent event) {
						webSocketConnectionController_.send(event);
					}
				});
		
		//ResultEvent
		fmuController_
		.registerEventListener(
				ResultEvent.class,
				new StraylightEventListener<ResultEvent, ScalarValueResults>() {
					@Override
					public void handleEvent(ResultEvent event) {
						webSocketConnectionController_.send(event);
					}
				});

		
		//XMLparsedEvent
		fmuController_
		.registerEventListener(
				XMLparsedEvent.class,
				new StraylightEventListener<XMLparsedEvent, XMLparsedInfo>() {
					@Override
					public void handleEvent(XMLparsedEvent event) {
						webSocketConnectionController_.send(event);
					}
				});
		
		
		//ConfigChangeNotify
		fmuController_
		.registerEventListener(
				ConfigChangeNotify.class,
				new StraylightEventListener<ConfigChangeNotify, ConfigStruct>() {
					@Override
					public void handleEvent(ConfigChangeNotify event) {
						webSocketConnectionController_.send(event);
					}
				});
	}

	private void registerSocketListeners_() {
		
		

		
		webSocketConnectionController_
		
		.registerEventListener(
				MessageReceived.class,
				
				new StraylightEventListener<MessageReceived, String>() {
					
					@Override
					public void handleEvent(MessageReceived event) {
						
						String messageText = event.getPayload();
				    	JsonSerializable deserializedEvent = JsonController.getInstance().fromJson(messageText);
				    	
				    	//if it is an event then just publish it
				    	if (deserializedEvent instanceof SimStateNativeRequest) {
				    		SimStateNativeRequest newEvent = (SimStateNativeRequest) deserializedEvent;
				    		threadedFMUcontroller_.requestStateChange(newEvent.getPayload());
				    	} else if (deserializedEvent instanceof ScalarValueChangeRequest) {
				    		
				    		ScalarValueChangeRequest newEvent = (ScalarValueChangeRequest) deserializedEvent;
				    		threadedFMUcontroller_.setScalarValueCollection(newEvent.getPayload());
				    	} {
				    		
				    		
				    		MessageEvent newEvent = new MessageEvent(this, "Could not deserialize object ", MessageType.messageType_error);
				    		fireEvent(newEvent);
				    		
				    	}
						
					}
					
				}
				
				);
		
		
		
	}

	public int getIdx_() {
		return idx_;
	}

	public void setIdx_(int idx_) {
		this.idx_ = idx_;
	}

	public void notifyClient() {

		SimStateNativeNotify event = 
				new  SimStateNativeNotify(this, fmuController_.getSimStateNative());
		
		
		webSocketConnectionController_.send(event);
	}

	public void forceCleanup() {
		
		if (null != fmuController_) {
			fmuController_.forceCleanup();
		}
		
	}
	


}

package com.sri.straylight.socketserver.controller;

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
import com.sri.straylight.fmuWrapper.util.WorkerThreadAbstract;
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
	//private int idx_;
	private String sessionID_;
	private StrayLightWebSocketHandler socketHandler_;

	private ThreadedFMUcontroller threadedFMUcontroller_;
	
	private WorkerHandleResultEvent workerHandleResultEvent_;
	
	private Object connectionBundleSync_ = new Object();
	
	private JsonController jsonController_;
	
	
	public ConnectionBundle(AbstractController parent, StrayLightWebSocketHandler socketHandler, String sessionID) {
		super(null);
		
		//setIdx_(socketHandler.getIdx());
		
		sessionID_ = sessionID;
		socketHandler_ = socketHandler;
		webSocketConnectionController_ = new WebSocketConnectionController(this, socketHandler);
		
		jsonController_ = new JsonController();
		jsonController_.init();
	}

	public void init() {
		

		
		webSocketConnectionController_.init();
		
		
		fmuController_ = new FMUcontroller(this);
		fmuController_.setSessionID(sessionID_);
		webSocketConnectionController_.setSessionID(sessionID_);
		
		threadedFMUcontroller_ = new ThreadedFMUcontroller(fmuController_);
		
		registerSimulationListeners_();
		registerSocketListeners_();

		
		webSocketConnectionController_.processQuedItem();
		
		//SimStateNativeNotify event = new SimStateNativeNotify(this, SimStateNative.simStateNative_1_connect_completed);
	//	webSocketConnectionController_.send(event);

	}

	private void registerSimulationListeners_() {
		
		
		//fmuController_ = threadedFMUcontroller_.getFMUcontroller();
		
		
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
						
						WorkerHandleResultEvent w = new WorkerHandleResultEvent(event);
						w.execute();
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
						
						//System.out.println("MessageReceived Event handled");
						
						String messageText = event.getPayload();
						
						//System.out.println("StraylightEventListener.handleEvent sessionID_: "+ sessionID_ + ' ' + messageText);
						
				    	JsonSerializable deserializedEvent = JsonController.getInstance().fromJson(messageText);
				    	
				    	//if it is an event then just publish it
				    	if (deserializedEvent instanceof SimStateNativeRequest) {
				    		SimStateNativeRequest newEvent = (SimStateNativeRequest) deserializedEvent;
				    		
				    		//System.out.println("MessageReceived -> SimStateNativeRequest Event");
				    		
				    		threadedFMUcontroller_.requestStateChange(newEvent.getPayload());
				    	} else if (deserializedEvent instanceof ScalarValueChangeRequest) {
				    		
				    		ScalarValueChangeRequest newEvent = (ScalarValueChangeRequest) deserializedEvent;
				    		
				    	//	System.out.println("MessageReceived -> ScalarValueChangeRequest Event");
				    		threadedFMUcontroller_.setScalarValueCollection(newEvent.getPayload());
				    	} else {
				    		
				    		System.out.println("MessageReceived -> Unknown Event");
				    		MessageEvent newEvent = new MessageEvent(this, "Could not deserialize object ", MessageType.messageType_error);
				    		fireEvent(newEvent);
				    		
				    	}
						
					}
					
				}
				
				);
		
		
		
	}

	/*
	public int getIdx_() {
		return idx_;
	}

	public void setIdx_(int idx_) {
		this.idx_ = idx_;
	}
*/
	
	public void notifyClient() {

		SimStateNativeNotify event = 
				new  SimStateNativeNotify(this, fmuController_.getSimStateNative());
		
		System.out.println("ConnectionBundle.notifyClient() = SessionID: " + sessionID_);
		
		webSocketConnectionController_.send(event);
	}

	public void forceCleanup() {
		
		
		System.out.println("ConnectionBundle.forceCleanup() = SessionID: " + sessionID_);
		
		
		if (null != fmuController_) {
			fmuController_.forceCleanup();
		}
		
	}
	
	
	

protected class WorkerHandleResultEvent extends WorkerThreadAbstract {
		
		private ResultEvent event_;
		
		WorkerHandleResultEvent(ResultEvent event) {
			event_ = event;
			
			setSyncObject(connectionBundleSync_);
			
		}
		
		@Override
		public void doIt_() {
			
			setName_("WorkerHandleResultEvent ");
			String json = jsonController_.toJson(event_);
			
			
			webSocketConnectionController_.send(json);
			
		}
		
		@Override
		public void doneIt_() {
			//workerHandleResultEvent_ = null;
		}
	}

}

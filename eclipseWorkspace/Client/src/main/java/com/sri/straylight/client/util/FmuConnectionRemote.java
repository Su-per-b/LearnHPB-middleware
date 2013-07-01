package com.sri.straylight.client.util;

import java.net.URI;
import java.util.Vector;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.SimStateClientRequest;
import com.sri.straylight.client.event.WebSocketEvent;
import com.sri.straylight.client.event.WebSocketEventType;
import com.sri.straylight.client.model.WebSocketState;
import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.util.WorkerThreadAbstract;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class FmuConnectRemote.
 */
public class FmuConnectionRemote extends FmuConnectionAbstract {

	private WorkerRequestStateChange workerRequestStateChange_;
//	private WorkerSetScalarValues workerSetScalarValues_;
//	private WorkerSetConfig workerSetConfig_;
//	
	private JsonController gsonController_ = JsonController.getInstance();
	private WebSocket webSocketConnection_;
	private WebSocketState webSocketState_;
	private final String urlString_;

	public FmuConnectionRemote(String hostName) {

		AnnotationProcessor.process(this);

		urlString_ = "ws://" + hostName + ":8081/";
		try {
			initSocketClient_();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@EventSubscriber(eventClass = SimStateNativeNotify.class)
	public void onSimStateNotify(SimStateNativeNotify event) {

		SimStateNative simStateNative = event.getPayload();
		SimStateClientNotify event2 = new SimStateClientNotify(this,
				simStateNative);
		EventBus.publish(event2);

	}


	public void setConfig(ConfigStruct configStruct) {
		// fmu_.setMetaData(metaDataStruct);
	}




	public void setScalarValues(Vector<ScalarValueRealStruct> scalrValueList) {

	}

	private void initSocketClient_() throws Exception {

		URI uri = new URI(urlString_);
		webSocketConnection_ = new WebSocketConnection(uri);
		webSocketState_ = WebSocketState.uninitialized;
		
		WebSocketEventHandler webSocketEventHandler = new WebSocketEventHandler() {
			
			
			public void onOpen() {
				
				webSocketState_ = WebSocketState.open;
				
				MessageEvent event = new MessageEvent(this,
						"WebSocket Connection open to " + urlString_,
						MessageType.messageType_info);

				EventBus.publish(event);

			}

			public void onMessage(WebSocketMessage msg) {

				String jsonString = msg.getText();
				
				
				JsonSerializable deserializedObject = gsonController_
						.fromJson(jsonString);

				if (deserializedObject instanceof BaseEvent) {
					EventBus.publish(deserializedObject);
				}

			}

			public void onClose() {

				webSocketState_ = WebSocketState.closed;
				
				MessageEvent event = new MessageEvent(this,
						"WebSocket Connection closed",
						MessageType.messageType_info);

				EventBus.publish(event);
			}

		};

		AnnotationProcessor.process(webSocketEventHandler);
		webSocketConnection_.setEventHandler(webSocketEventHandler);

	}



	public void requestStateChange(SimStateNative state) {
		workerRequestStateChange_ = new WorkerRequestStateChange(state);
		workerRequestStateChange_.execute();
	}


	
	protected class WorkerRequestStateChange extends WorkerThreadAbstract {
		private SimStateNative simStateNative_;
		
		WorkerRequestStateChange(SimStateNative requestedState) {
			simStateNative_ = requestedState;
			setSyncObject(webSocketConnection_);
		}
		
		@Override
		public void doIt_() {
			SimStateNativeRequest event = new SimStateNativeRequest(this, simStateNative_);
			

			
			if (webSocketState_ == WebSocketState.uninitialized) {
				try {
					webSocketConnection_.connect();
				} catch (WebSocketException e) {
					
					WebSocketEvent errorEvent = new WebSocketEvent(this,
							"Unable to connect to WebSocket",
							e.getMessage(),
							WebSocketEventType.webSocketEventType_error);

					EventBus.publish(errorEvent);
					
					
					EventBus.publish(
							new SimStateClientRequest(this, SimStateNative.simStateNative_0_uninitialized)
							);	
					
					return;
				}
				
			}
			
			
//			if (simStateNative_ == SimStateNative.simStateNative_0_uninitialized &&
//					webSocketState_ == WebSocketState.uninitialized 
//					
//					) {
//				return;
//			}
			
			
			try {
				
				String json = event.toJson();
				
				webSocketConnection_.send(json);
			} catch (WebSocketException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void doneIt_() {
			workerRequestStateChange_ = null;
		}
	}
	
	


}

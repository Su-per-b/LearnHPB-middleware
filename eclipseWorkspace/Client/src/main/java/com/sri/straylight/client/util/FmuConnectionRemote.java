package com.sri.straylight.client.util;

import java.net.URI;
import java.util.Vector;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

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

	private WorkerConnect workerConnect_;
	private WorkerRequestStateChange workerRequestStateChange_;
//	private WorkerRun workerRun_;
//	private WorkerRequestStateChange workerRequestStateChange_;
//	private WorkerSetScalarValues workerSetScalarValues_;
//	private WorkerSetConfig workerSetConfig_;
//	
	private JsonController gsonController_ = JsonController.getInstance();
	private WebSocket websocketConnection_;
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


	public void changeInput(int idx, double value) {

	}





	public void setScalarValues(Vector<ScalarValueRealStruct> scalrValueList) {

	}

	private void initSocketClient_() throws Exception {

		URI uri = new URI(urlString_);
		websocketConnection_ = new WebSocketConnection(uri);

		WebSocketEventHandler webSocketEventHandler = new WebSocketEventHandler() {

			public void onOpen() {

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

				MessageEvent event = new MessageEvent(this,
						"WebSocket Connection closed",
						MessageType.messageType_info);

				EventBus.publish(event);
			}

		};

		AnnotationProcessor.process(webSocketEventHandler);
		websocketConnection_.setEventHandler(webSocketEventHandler);

	}

	public void connect() {

		MessageEvent event = new MessageEvent(this,
				"Connecting to remote host at: " + urlString_,
				MessageType.messageType_info);

		EventBus.publish(event);

		workerConnect_ = new WorkerConnect();
		workerConnect_.execute();

	}



	public void xmlParse() {
		workerRequestStateChange_ = new WorkerRequestStateChange(SimStateNative.simStateNative_2_xmlParse_requested);
		workerRequestStateChange_.execute();
	}
	
	
	public void requestStateChange(SimStateNative state) {
		workerRequestStateChange_ = new WorkerRequestStateChange(state);
		workerRequestStateChange_.execute();
	}


	public void run() {

		try {
			websocketConnection_.send("run");
		} catch (WebSocketException wse) {
			wse.printStackTrace();
			wse.getMessage();
			String msg = wse.getClass().getSimpleName() + ": "
					+ wse.getMessage();
			System.out.println(msg);

		}
	}
	
	
	protected class WorkerConnect extends WorkerThreadAbstract {
		
		WorkerConnect() {
			setSyncObject(websocketConnection_);
		}
		
		//called by superclass
		@Override
		public void doIt_() {
			try {
				websocketConnection_.connect();
			} catch (WebSocketException e) {
				e.printStackTrace();
			}
		}
		
		//called by superclass
		@Override
		public void doneIt_() {
			workerConnect_ = null;
		}
	}
	

	protected class WorkerRequestStateChange extends WorkerThreadAbstract {
		private SimStateNative state_;
		
		WorkerRequestStateChange(SimStateNative state) {
			
			state_ = state;
			setSyncObject(websocketConnection_);
			
		}
		
		@Override
		public void doIt_() {
			SimStateNativeRequest event = new SimStateNativeRequest(this, state_);
			try {
				websocketConnection_.send(event.toJson());
			} catch (WebSocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void doneIt_() {
			workerRequestStateChange_ = null;
		}
	}
	
	
//	
//	
//	public void requestStateChange(SimStateNative newState) {
//
//		SimStateNativeRequest event = new SimStateNativeRequest(this, newState);
//		try {
//			websocketConnection_.send(event.toJson());
//		} catch (WebSocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

}

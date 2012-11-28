package com.sri.straylight.client.controller;


import java.net.URI;
import java.util.Vector;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeRequest;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
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
public class FmuConnectRemote implements IFmuConnect {
	

    /** The websocket connection_. */
    private WebSocket websocketConnection_;

	
    /** The url string_. */
   private final String urlString_;


    /**
     * Instantiates a new fmu connect remote.
     *
     * @param hostName the host name
     */
    public FmuConnectRemote(String hostName) {
    	
    	AnnotationProcessor.process(this);
    	
    	urlString_ = "ws://" + hostName + ":8081/";	
    	try {
    		initSocketClient_();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    }
    
    
	/**
	 * On sim state notify.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateNativeNotify.class)
	public void onSimStateNotify(SimStateNativeNotify event) {

		SimStateNative simStateNative = event.getPayload();
		SimStateClientNotify event2 = new SimStateClientNotify(this, simStateNative);
		EventBus.publish(event2);

	}
	
	
	/* (non-Javadoc)
	 * @see com.sri.straylight.client.controller.IFmuConnect#setConfig(com.sri.straylight.fmuWrapper.voNative.ConfigStruct)
	 */
	public void setConfig(ConfigStruct configStruct) {
		//fmu_.setMetaData(metaDataStruct);
	}
	
	/* (non-Javadoc)
	 * @see com.sri.straylight.client.controller.IFmuConnect#changeInput(int, double)
	 */
	public void changeInput(int idx, double value) {
		
	}

	/* (non-Javadoc)
	 * @see com.sri.straylight.client.controller.IFmuConnect#requestStateChange(com.sri.straylight.fmuWrapper.voNative.SimStateNative)
	 */
	public void requestStateChange(SimStateNative newState) {
		
		
		SimStateNativeRequest event = new SimStateNativeRequest(this, newState);
		
		try {
			websocketConnection_.send(event.toJson());
		} catch (WebSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	

	/* (non-Javadoc)
	 * @see com.sri.straylight.client.controller.IFmuConnect#changeScalarValues(java.util.Vector)
	 */
	public void changeScalarValues(Vector<ScalarValueRealStruct> scalrValueList) {
		
		
	}
	
	
	private void initSocketClient_() throws Exception {
		

        URI uri = new URI(urlString_);
        websocketConnection_ = new WebSocketConnection(uri);
        
        WebSocketEventHandler webSocketEventHandler = new WebSocketEventHandler() {
        	
        	
            public void onOpen()
            {

            	MessageEvent event = new MessageEvent(
            			this, 
            			"WebSocket Connection open", 
            			MessageType.messageType_info);
            	

            	EventBus.publish(event);	
            	
            }
                  
            
            
            public void onMessage(WebSocketMessage msg)
            {
            	

            	String jsonString = msg.getText();
            	JsonSerializable deserializedObject = JsonController.getInstance().fromJson(jsonString);

            	System.out.println(jsonString);
            	
            	if (deserializedObject instanceof BaseEvent) {
            		EventBus.publish(deserializedObject);
            	}
            	
            }
            
            
                            




			public void onClose()
            {

            	MessageEvent event = new MessageEvent(
            			this, 
            			"WebSocket Connection closed", 
            			MessageType.messageType_info);
            	
            	EventBus.publish(event);
            }
            
            
            
            
        };
        
        
        AnnotationProcessor.process(webSocketEventHandler);
        websocketConnection_.setEventHandler(webSocketEventHandler);	
        	
	}
	
	/* (non-Javadoc)
	 * @see com.sri.straylight.client.controller.IFmuConnect#connect()
	 */
	public void connect() throws Exception {
		
        
        
    	MessageEvent event = new MessageEvent(
    			this, 
    			"Connecting to remote host at: " + urlString_, 
    			MessageType.messageType_info);
    	
    	EventBus.publish(event);
    	
    	
        // Establish WebSocket Connection
        websocketConnection_.connect();
        
        
	}
	
	

	
	/**
	 * Resume.
	 */
	public void resume() {
		
	}
	
    /* (non-Javadoc)
     * @see com.sri.straylight.client.controller.IFmuConnect#xmlParse()
     */
    public void xmlParse() {

    	
    }
 
    
    /* (non-Javadoc)
     * @see com.sri.straylight.client.controller.IFmuConnect#run()
     */
    public void run() {
    	
    	try {
    		websocketConnection_.send("run");
    	} 
		catch (WebSocketException wse) {
	        wse.printStackTrace();
	        wse.getMessage();
			String msg = wse.getClass().getSimpleName() + ": " + wse.getMessage();
			System.out.println(msg);

		}
    }
    


    



    

    
}

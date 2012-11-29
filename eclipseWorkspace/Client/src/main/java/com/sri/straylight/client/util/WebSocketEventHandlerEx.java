package com.sri.straylight.client.util;

import java.net.URI;

import org.bushe.swing.event.EventBus;

import com.sri.straylight.fmuWrapper.event.BaseEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.serialization.JsonSerializable;
import com.sri.straylight.fmuWrapper.voNative.MessageType;

import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

public class WebSocketEventHandlerEx extends WebSocketConnection {

	public WebSocketEventHandlerEx(URI url) throws WebSocketException {
		super(url);
		// TODO Auto-generated constructor stub
	}
	
	
	
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
    

}

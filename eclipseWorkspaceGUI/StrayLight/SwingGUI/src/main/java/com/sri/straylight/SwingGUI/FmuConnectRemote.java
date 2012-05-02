package com.sri.straylight.SwingGUI;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.SwingWorker;
import javax.tools.JavaFileObject;


//import org.jwebsocket.client.java.ReliabilityOptions;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sri.straylight.fmuWrapper.FMU;
import com.sri.straylight.fmuWrapper.MessageStruct;
import com.sri.straylight.fmuWrapper.MessageType;
import com.sri.straylight.fmuWrapper.ScalarVariableMeta;
import com.sri.straylight.fmuWrapper.event.FMUeventDispatacher;
import com.sri.straylight.fmuWrapper.event.FMUeventListener;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.serialization.GsonController;
import com.sri.straylight.fmuWrapper.serialization.SerializeableObject;
import com.sri.straylight.fmuWrapper.test.*;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

public class FmuConnectRemote implements FmuConnect {
	
    private FMU fmu_;
    private WebSocket websocketConnection_;
    private final String websocketServerUrl_ = "ws://localhost:8081/";

	
	public FMUeventDispatacher fmuEventDispatacher;
	
    public FmuConnectRemote() {
    	fmuEventDispatacher = new FMUeventDispatacher();
    }
    
    public void init() {

		try {

			try {
		        URI url = new URI(websocketServerUrl_);
		        websocketConnection_ = new WebSocketConnection(url);
		        
		        // Register Event Handlers
		        websocketConnection_.setEventHandler(new WebSocketEventHandler() {
	                public void onOpen()
	                {
	                	MessageEvent event = new MessageEvent(this);
	                	event.messageStruct = new MessageStruct();
	                	event.messageStruct.msgText = "WebSocket Connection open";
	                	event.messageStruct.setMessageTypeEnum(MessageType.messageType_info);
	                	fmuEventDispatacher.fireMessageEvent(event);
	                	
	                    //System.out.println("--open");
	                }
	                                
	                public void onMessage(WebSocketMessage message)
	                {
	                	Gson gson = GsonController.getInstance().getGson();
	                	String jsonString = message.getText();

	              
	                	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);

	                	
	                	try {
	                		Class cl = Class.forName(obj.type);
	                		
		                	if (cl == MessageEvent.class) {
		                		
		                		MessageEvent event = gson.fromJson(jsonString, MessageEvent.class);
			                	fmuEventDispatacher.fireMessageEvent(event);
			                	
		                	} else if (cl == ResultEvent.class) {
		                		
		                		ResultEvent event = gson.fromJson(jsonString, ResultEvent.class);
			                	fmuEventDispatacher.fireResultEvent(event);
			                	
		                	} else if (cl == FMUstateEvent.class) {
		                		
		                		FMUstateEvent event = gson.fromJson(jsonString, FMUstateEvent.class);
			                	fmuEventDispatacher.fireStateEvent(event);
			                	
		                	} else if (cl == InitializedEvent.class) {
		                		
		                		InitializedEvent event = gson.fromJson(jsonString, InitializedEvent.class);
			                	fmuEventDispatacher.fireInitializedEvent(event);
			                	
		                	}
	                	}
	                	catch (ClassNotFoundException ex) {
	                		ex.printStackTrace();
	                		ex.getMessage();
	    			        String msg = ex.getClass().getSimpleName() + ": " + ex.getMessage();
	    			        System.out.println(msg);
	                	}

	                	
	                	int one = gson.fromJson("1", int.class);

	                	System.out.println(jsonString);

	                }
	                                
	                public void onClose()
	                {
	                	MessageEvent event = new MessageEvent(this);
	                	event.messageStruct = new MessageStruct();
	                	event.messageStruct.msgText = "WebSocket Connection closed";
	                	event.messageStruct.setMessageTypeEnum(MessageType.messageType_info);
	                	fmuEventDispatacher.fireMessageEvent(event);
	                }
	        });
		        
		        // Establish WebSocket Connection
		        websocketConnection_.connect();
		        
		        // Send UTF-8 Text
		        websocketConnection_.send("start");
		        
		        // Close WebSocket Connection
		        //websocket.close();
			}
			catch (WebSocketException wse) {
			        wse.printStackTrace();
			        wse.getMessage();
				String msg = wse.getClass().getSimpleName() + ": " + wse.getMessage();
				System.out.println(msg);
				

			        
			}
			catch (URISyntaxException use) {
			        use.printStackTrace();
				String msg = use.getClass().getSimpleName() + ": " + use.getMessage();
				System.out.println(msg);
				

			}
			
		} catch (Exception ex) {
			
			String msg = ex.getClass().getSimpleName() + ": " + ex.getMessage();
			System.out.println(msg);
			
        	MessageEvent event = new MessageEvent(this);
        	event.messageStruct = new MessageStruct();
        	event.messageStruct.msgText = msg;
        	event.messageStruct.setMessageTypeEnum(MessageType.messageType_error);
        	fmuEventDispatacher.fireMessageEvent(event);
        	
		}  
        
    	
    }
    
    
    public void run() {

    }
    


    



    

    
}

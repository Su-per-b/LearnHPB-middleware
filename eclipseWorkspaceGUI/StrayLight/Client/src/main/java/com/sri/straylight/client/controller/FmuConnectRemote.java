package com.sri.straylight.client.controller;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import org.bushe.swing.event.EventBus;

import com.google.gson.Gson;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateServerNotify;
import com.sri.straylight.fmuWrapper.serialization.GsonController;
import com.sri.straylight.fmuWrapper.serialization.SerializeableObject;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.MessageType;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

public class FmuConnectRemote implements IFmuConnect {
	

    private WebSocket websocketConnection_;
   // private final String websocketServerUrl_ = "ws://localhost:8081/";
   // private final String websocketServerUrlRemote_ = "ws://wintermute.straylightsim.com:8081/";
	
    private final String urlString_;

    public FmuConnectRemote(String hostName) {
    	
    	urlString_ = "ws://" + hostName + ":8081/";	
    }
    
	public void setConfig(ConfigStruct configStruct) {
		//fmu_.setMetaData(metaDataStruct);
	}
	
	public void changeInput(int idx, double value) {
		
	}

	public void requestStateChange(SimStateNative newState) {
		
	
	}
	

	public void changeScalarValues(Vector<ScalarValueRealStruct> scalrValueList) {
		
		
	}
	
	public void connect() {

	}
	
	public void init() {

	}

	public void resume() {
		
	}
	
    public void xmlParse() {

    	
		try {

			try {
		        URI uri = new URI(urlString_);
		        websocketConnection_ = new WebSocketConnection(uri);
		        
		        // Register Event Handlers
		        websocketConnection_.setEventHandler(new WebSocketEventHandler() {
	                public void onOpen()
	                {

	                	MessageEvent event = new MessageEvent(
	                			this, 
	                			"WebSocket Connection open", 
	                			MessageType.messageType_info);
	                	

	                	EventBus.publish(event);	
	                }
	                                
	                public void onMessage(WebSocketMessage message)
	                {
	                	Gson gson = GsonController.getInstance().getGson();
	                	String jsonString = message.getText();

	              
	                	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);

	                	
	                	try {
	                		Class<?> cl = Class.forName(obj.type);
	                		
		                	if (cl == MessageEvent.class) {
		                		
		                		MessageEvent event = gson.fromJson(jsonString, MessageEvent.class);
		                		EventBus.publish(event);
			                	
		                	} else if (cl == ResultEvent.class) {
		                		
		                		ResultEvent event = gson.fromJson(jsonString, ResultEvent.class);
		                		EventBus.publish(event);
			                	
		                	} else if (cl == SimStateServerNotify.class) {
		                		
		                		SimStateServerNotify event = gson.fromJson(jsonString, SimStateServerNotify.class);
		                		EventBus.publish(event);
			                	
		                	} else if (cl == InitializedEvent.class) {
		                		
		                		InitializedEvent event = gson.fromJson(jsonString, InitializedEvent.class);
		                		EventBus.publish(event);
			                	
		                	}
	                	}
	                	catch (ClassNotFoundException ex) {
	                		ex.printStackTrace();
	    			        String msg = ex.getClass().getSimpleName() + ": " + ex.getMessage();
	    			        System.out.println(msg);
	                	}

	                	

	                	System.out.println(jsonString);

	                }
	                                
	                public void onClose()
	                {

	                	MessageEvent event = new MessageEvent(
	                			this, 
	                			"WebSocket Connection closed", 
	                			MessageType.messageType_info);
	                	
	                	EventBus.publish(event);
	                }
	        });
		        
		        
            	MessageEvent event = new MessageEvent(
            			this, 
            			"Connecting to remote host at: " + urlString_, 
            			MessageType.messageType_info);
            	
            	EventBus.publish(event);
            	
            	
		        // Establish WebSocket Connection
		        websocketConnection_.connect();
		        
		        // Send UTF-8 Text
		        websocketConnection_.send("init");
		        
		        // Close WebSocket Connection
		        //websocket.close();
			}
			catch (WebSocketException wse) {
			    wse.printStackTrace();

				String msg = wse.getClass().getSimpleName() + ": " + wse.getMessage();
				
	        	MessageEvent event = new MessageEvent(this, msg, MessageType.messageType_error);
	        	EventBus.publish(event);
				

			        
			}
			catch (URISyntaxException use) {
			        use.printStackTrace();
				String msg = use.getClass().getSimpleName() + ": " + use.getMessage();
				System.out.println(msg);
				

			}
			
		} catch (Exception ex) {
			
			String msg = ex.getClass().getSimpleName() + ": " + ex.getMessage();
			System.out.println(msg);
			
        	MessageEvent event = new MessageEvent(this, msg, MessageType.messageType_error);
        	EventBus.publish(event);
        	
		}
    	
    }
    /*
    public void init( ) {
    	
    	fmuEventDispatacher_ = new FMUeventDispatacher();
    	fmuEventDispatacher_.addListener(l);
    	
		try {

			try {
		        URI uri = new URI(urlString_);
		        websocketConnection_ = new WebSocketConnection(uri);
		        
		        // Register Event Handlers
		        websocketConnection_.setEventHandler(new WebSocketEventHandler() {
	                public void onOpen()
	                {

	                	MessageEvent event = new MessageEvent(
	                			this, 
	                			"WebSocket Connection open", 
	                			MessageType.messageType_info);
	                	

	                	fmuEventDispatacher_.fireEvent(event);

	                }
	                                
	                public void onMessage(WebSocketMessage message)
	                {
	                	Gson gson = GsonController.getInstance().getGson();
	                	String jsonString = message.getText();

	              
	                	SerializeableObject obj = gson.fromJson(jsonString, SerializeableObject.class);

	                	
	                	try {
	                		Class<?> cl = Class.forName(obj.type);
	                		
		                	if (cl == MessageEvent.class) {
		                		
		                		MessageEvent event = gson.fromJson(jsonString, MessageEvent.class);
			                	fmuEventDispatacher_.fireEvent(event);
			                	
		                	} else if (cl == ResultEvent.class) {
		                		
		                		ResultEvent event = gson.fromJson(jsonString, ResultEvent.class);
			                	fmuEventDispatacher_.fireEvent(event);
			                	
		                	} else if (cl == SimulationStateEvent.class) {
		                		
		                		SimulationStateEvent event = gson.fromJson(jsonString, SimulationStateEvent.class);
			                	fmuEventDispatacher_.fireEvent(event);
			                	
		                	} else if (cl == InitializedEvent.class) {
		                		
		                		InitializedEvent event = gson.fromJson(jsonString, InitializedEvent.class);
			                	fmuEventDispatacher_.fireEvent(event);
			                	
		                	}
	                	}
	                	catch (ClassNotFoundException ex) {
	                		ex.printStackTrace();
	    			        String msg = ex.getClass().getSimpleName() + ": " + ex.getMessage();
	    			        System.out.println(msg);
	                	}

	                	

	                	System.out.println(jsonString);

	                }
	                                
	                public void onClose()
	                {

	                	MessageEvent event = new MessageEvent(
	                			this, 
	                			"WebSocket Connection closed", 
	                			MessageType.messageType_info);
	                	
	                	fmuEventDispatacher_.fireEvent(event);
	                }
	        });
		        
		        
            	MessageEvent event = new MessageEvent(
            			this, 
            			"Connecting to remote host at: " + urlString_, 
            			MessageType.messageType_info);
            	
            	fmuEventDispatacher_.fireEvent(event);
            	
            	
		        // Establish WebSocket Connection
		        websocketConnection_.connect();
		        
		        // Send UTF-8 Text
		        websocketConnection_.send("init");
		        
		        // Close WebSocket Connection
		        //websocket.close();
			}
			catch (WebSocketException wse) {
			    wse.printStackTrace();

				String msg = wse.getClass().getSimpleName() + ": " + wse.getMessage();
				
	        	MessageEvent event = new MessageEvent(this, msg, MessageType.messageType_error);
	        	fmuEventDispatacher_.fireEvent(event);
				

			        
			}
			catch (URISyntaxException use) {
			        use.printStackTrace();
				String msg = use.getClass().getSimpleName() + ": " + use.getMessage();
				System.out.println(msg);
				

			}
			
		} catch (Exception ex) {
			
			String msg = ex.getClass().getSimpleName() + ": " + ex.getMessage();
			System.out.println(msg);
			
        	MessageEvent event = new MessageEvent(this, msg, MessageType.messageType_error);
        	fmuEventDispatacher_.fireEvent(event);
        	
		} 
        
    	
    }
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

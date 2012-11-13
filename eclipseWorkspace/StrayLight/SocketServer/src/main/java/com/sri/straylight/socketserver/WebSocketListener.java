package com.sri.straylight.socketserver;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.eclipse.jetty.websocket.WebSocket;

import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.serialization.JsonController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.socketserver.event.MessageIn;
import com.sri.straylight.socketserver.event.MessageOut;




public class WebSocketListener implements WebSocket.OnTextMessage, WebSocket.OnControl  {

	private Connection connection;

	
    public static String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
    public static String testFmuFile = "E:\\SRI\\modelica-projects\\fmus\\no_licence_needed\\LearnGB_VAVReheat_ClosedLoop.fmu";
    public static String nativeLibFolder = "..\\..\\..\\..\\..\\..\\visualStudioWorkspace\\bin\\Debug";
    
	private final Set<WebSocketListener> webSockets = new CopyOnWriteArraySet<WebSocketListener>();
	


	
    public WebSocketListener() {
    	
    	AnnotationProcessor.process(this);

    }
    
    


	public void onOpen(Connection connection) {
		// Client (Browser) WebSockets has opened a connection.
		// 1) Store the opened connection
		this.connection = connection;
		// 2) Add ChatWebSocket in the global list of ChatWebSocket
		// instances
		// instance.
		webSockets.add(this);
		
		
		
		EventBus.publish(
				new MessageIn(this, "state_query")
			);	
		
	}
	
	
	public boolean onControl(byte controlCode,byte[] data, int offset, int length) {
		
		
		return true;
	}
	
	
	@EventSubscriber(eventClass=com.sri.straylight.socketserver.event.MessageOut.class)
	public void onMessageOut(MessageOut event) {
		
		String messageText = event.getPayload();
		sendMessage(messageText);
	}
	
	
	private void sendMessage(String msg) {
		
		// System.out.println("SocketHandlerStream " + msg);
		 
		try {
			for (WebSocketListener webSocket : webSockets) {
				// send a message to the current client WebSocket.
				webSocket.connection.sendMessage(msg);
			}
		} catch (IOException x) {
			// Error was detected, close the ChatWebSocket client side
			this.connection.disconnect();
			//fmu_.forceCleanup();
		}
		
	}


	
	public void onMessage(String messageText) {
		 
		System.out.println("SocketHandlerStream.onMessage " + messageText);

		EventBus.publish(
				new MessageIn(this, messageText)
				);	

	}


	
	
    public void onResultEvent(ResultEvent event) {
    	
    	
    	ScalarValueResults scalarValueResults = event.getScalarValueResults();
    	
    	//event.resultItem.string = "";

    }
   

    

	
	public void onClose(int closeCode, String message) {
//		// Remove ChatWebSocket in the global list of ChatWebSocket
//		// instance.
//
		webSockets.remove(this);
//		fmu_.forceCleanup();
	}
	
}
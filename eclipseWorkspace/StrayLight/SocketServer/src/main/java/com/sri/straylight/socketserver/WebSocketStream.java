package com.sri.straylight.socketserver;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocket.Connection;



public class WebSocketStream implements WebSocket.OnTextMessage {

	private Connection connection;
	private JNIinterface jniInterface;
	
	
	private final Set<WebSocketStream> webSockets = new CopyOnWriteArraySet<WebSocketStream>();
	
	
	
	public void onOpen(Connection connection) {
		// Client (Browser) WebSockets has opened a connection.
		// 1) Store the opened connection
		this.connection = connection;
		// 2) Add ChatWebSocket in the global list of ChatWebSocket
		// instances
		// instance.
		webSockets.add(this);
	}
	
	
	
	private void sendMessage(String msg) {
		
		 System.out.println("SocketHandlerStream " + msg);
		 
		try {
			for (WebSocketStream webSocket : webSockets) {
				// send a message to the current client WebSocket.
				webSocket.connection.sendMessage(msg);
			}
		} catch (IOException x) {
			// Error was detected, close the ChatWebSocket client side
			this.connection.disconnect();
		}
		
	}

	
	public void onMessage(String data) {
		 
		System.out.println("SocketHandlerStream.onMessage " + data);


		if (data.equals("start")) {
			jniInterface = new JNIinterface();
			String result = jniInterface.initAll();
			
			
	    	while(!jniInterface.isSimulationComplete()) {
	    		
	    		result = jniInterface.runStep();
	    		double r = jniInterface.getResultSnapshot();
	    		
	    		String str = Double.toString(r);
	    		System.out.println(str);
	    		this.sendMessage(str);
	    		
	    		try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	
	    	jniInterface.simulateHelperCleanup();
	    	jniInterface = null;
		}
		

		
			

	}

	public void onClose(int closeCode, String message) {
		// Remove ChatWebSocket in the global list of ChatWebSocket
		// instance.
		webSockets.remove(this);
	}
}
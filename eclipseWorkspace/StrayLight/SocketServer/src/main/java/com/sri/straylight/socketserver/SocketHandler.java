package com.sri.straylight.socketserver;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;
import java.text.DecimalFormat;

public class SocketHandler extends WebSocketHandler {

	private final Set<MyWebSocket> webSockets = new CopyOnWriteArraySet<MyWebSocket>();

	public WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		return new MyWebSocket();
	}

	private class MyWebSocket implements WebSocket.OnTextMessage {

		private Connection connection;
		private JNIinterface jniInterface;
		
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
			
			try {
				for (MyWebSocket webSocket : webSockets) {
					// send a message to the current client WebSocket.
					webSocket.connection.sendMessage(msg);
				}
			} catch (IOException x) {
				// Error was detected, close the ChatWebSocket client side
				this.connection.disconnect();
			}
			
		}

		
		public void onMessage(String data) {



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
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
				
			}
			
			
			
				

		}

		public void onClose(int closeCode, String message) {
			// Remove ChatWebSocket in the global list of ChatWebSocket
			// instance.
			webSockets.remove(this);
		}
	}
}
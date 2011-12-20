package com.sri.torqueflite;

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

		public void onOpen(Connection connection) {
			// Client (Browser) WebSockets has opened a connection.
			// 1) Store the opened connection
			this.connection = connection;
			// 2) Add ChatWebSocket in the global list of ChatWebSocket
			// instances
			// instance.
			webSockets.add(this);
		}

		public void onMessage(String data) {
			// Loop for each instance of ChatWebSocket to send message server to
			// each client WebSockets.
			
			SocketServer.logger.info( "onMessage: " + data ); 
					
			Double gallons = Double.parseDouble(data);
			Double footPounds = gallons * 97181188.7888;
			
			//String footPoundsStr = String.valueOf(footPounds);
			
			
			DecimalFormat df = new DecimalFormat("###,###.##");
			String shortString = (df.format(footPounds));

			
			//String footPoundsStr = Double.toString(((int)(footPounds * 1000))/1000.0);
			
			try {
				for (MyWebSocket webSocket : webSockets) {
					// send a message to the current client WebSocket.
					webSocket.connection.sendMessage(shortString);
				}
			} catch (IOException x) {
				// Error was detected, close the ChatWebSocket client side
				this.connection.disconnect();
			}

		}

		public void onClose(int closeCode, String message) {
			// Remove ChatWebSocket in the global list of ChatWebSocket
			// instance.
			webSockets.remove(this);
		}
	}
}
package org.samples.websockets.embeddingjetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;

public class ChatWebSocketServer {

	public static void main(String[] args) {
		try {
			// 1) Create a Jetty server with the 8091 port.
			Server server = new Server(8081);
			// 2) Register ChatWebSocketHandler in the Jetty server instance.
			ChatWebSocketHandler chatWebSocketHandler = new ChatWebSocketHandler();
			chatWebSocketHandler.setHandler(new DefaultHandler());
			server.setHandler(chatWebSocketHandler);
			// 2) Start the Jetty server.
			server.start();
			// Jetty server is stopped when the Thread is interruped.
			server.join();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
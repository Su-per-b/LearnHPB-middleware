package com.sri.straylight.socketserver;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocket.Connection;

import com.sri.straylight.fmu.ResultItemPrimitiveStruct;
import com.sri.straylight.fmu.ScalarVariableMeta;



public class WebSocketStream implements WebSocket.OnTextMessage,  ResultEventListener {

	private Connection connection;
	//private JNIinterface jniInterface;
	public static FMU fmu_;
	
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
			
	    	
			fmu_ = new FMU(Main.config.testFmuFile);
			fmu_.disp.addListener(this);
			
			fmu_.init(Main.unzipFolder);
			fmu_.run();
	    	
		}

	}

	
	public void eventUpdate(ResultEvent re) {
		
    	sendMessage (re.resultString);
    	
	}
	
	
	public void onClose(int closeCode, String message) {
		// Remove ChatWebSocket in the global list of ChatWebSocket
		// instance.
		webSockets.remove(this);
	}
}
package com.sri.straylight.socketserver;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bushe.swing.event.annotation.EventSubscriber;
import org.eclipse.jetty.websocket.WebSocket;

import com.google.gson.Gson;
import com.sri.straylight.fmuWrapper.FMUcontroller;

import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.event.SimStateServerNotify;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.sri.straylight.fmuWrapper.serialization.GsonController;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueResults;
import com.sri.straylight.fmuWrapper.voManaged.SimStateServer;
import com.sri.straylight.socketserver.controller.SimulationController;




public class WebSocketListener implements WebSocket.OnTextMessage, WebSocket.OnControl  {

	private Connection connection;
	public static FMUcontroller fmu_;
	
    public static String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
    public static String testFmuFile = "E:\\SRI\\modelica-projects\\fmus\\no_licence_needed\\LearnGB_VAVReheat_ClosedLoop.fmu";
    public static String nativeLibFolder = "..\\..\\..\\..\\..\\..\\visualStudioWorkspace\\bin\\Debug";
    
	private final Set<WebSocketListener> webSockets = new CopyOnWriteArraySet<WebSocketListener>();
	


	private SimulationController simulationController_;
	
    public WebSocketListener() {
    	
    	simulationController_ = new SimulationController();
    	simulationController_.start();
    }
    
    


	public void onOpen(Connection connection) {
		// Client (Browser) WebSockets has opened a connection.
		// 1) Store the opened connection
		this.connection = connection;
		// 2) Add ChatWebSocket in the global list of ChatWebSocket
		// instances
		// instance.
		webSockets.add(this);
	}
	
	
	public boolean onControl(byte controlCode,byte[] data, int offset, int length) {
		
		
		return true;
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

	
	public void onMessage(String data) {
		 
		System.out.println("SocketHandlerStream.onMessage " + data);


		if (data.equals("init"))  {
			
			
			//on my machine it will be E:\\SRI\\straylight_repo\\eclipseWorkspace\\StrayLight\\SocketServer\\target\\classes
			//File file = new File(WebSocketListener.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			
			//String path = file.getPath() + nativeLibFolder;
			
		//	FMUwrapperConfig fmuWrapperConfig = new FMUwrapperConfig();
			
			
		//	fmu_ = new FMUcontroller();
			
			
			//fmu_.fmuEventDispatacher.addListener(this);
			
    		//fmu_.init_1();
    	//	fmu_.init_2(unzipFolder);
    	//fmu_.init_3();
    		
		} else if (data.equals("run")) {
			
			
			
			fmu_.run();
		}

	}

    
    public void onResultEvent(ResultEvent event) {
    	
    	
    	ScalarValueResults scalarValueResults = event.getScalarValueResults();
    	
    	//event.resultItem.string = "";
    	//Gson gson = GsonController.getInstance().getGson();
    	
    	//String jsonString = gson.toJson(event);
    	//sendMessage(jsonString);
    }
   
    public void onMessageEvent(MessageEvent event) {
    	String jsonString = GsonController.getInstance().getGson().toJson(event);
    	sendMessage(jsonString);
    }
    
//    public void onFMUstateEvent(FMUstateEvent event) {
//    	String jsonString = GsonController.getInstance().getGson().toJson(event);
//    	sendMessage(jsonString);
//    }
    
    public void onInitializedEvent(InitializedEvent event) {
    	
    	String jsonString = GsonController.getInstance().getGson().toJson(event);
    	sendMessage(jsonString);
    }
    

	
	public void onClose(int closeCode, String message) {
//		// Remove ChatWebSocket in the global list of ChatWebSocket
//		// instance.
//
		webSockets.remove(this);
//		fmu_.forceCleanup();
	}
	
}
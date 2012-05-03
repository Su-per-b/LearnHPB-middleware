package com.sri.straylight.socketserver;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.jetty.websocket.WebSocket;

import com.google.gson.Gson;
import com.sri.straylight.fmuWrapper.FMU;
import com.sri.straylight.fmuWrapper.event.FMUeventDispatacher;
import com.sri.straylight.fmuWrapper.event.FMUeventListener;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.serialization.GsonController;




public class WebSocketStream implements WebSocket.OnTextMessage, WebSocket.OnControl, FMUeventListener {

	private Connection connection;
	//private JNIinterface jniInterface;
	public static FMU fmu_;
	
    public static String unzipFolder = "C:\\Temp\\LearnGB_0v2_VAVReheat_ClosedLoop";
    public static String testFmuFile = "E:\\SRI\\modelica-projects\\fmus\\no_licence_needed\\LearnGB_VAVReheat_ClosedLoop.fmu";
//    public static String nativeLibFolder = "E:\\SRI\\straylight_repo\\visualStudioWorkspace\\bin\\Debug";
    public static String nativeLibFolder = "C:\\SRI\\straylight_repo\\eclipseWorkspaceGUI\\StrayLight\\FMUwrapper";
    
    
	private final Set<WebSocketStream> webSockets = new CopyOnWriteArraySet<WebSocketStream>();
	
	public FMUeventDispatacher fmuEventDispatacher;
	
	
    public WebSocketStream() {
    	fmuEventDispatacher = new FMUeventDispatacher();
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


		if (data.equals("init"))  {
			fmu_ = new FMU(testFmuFile, nativeLibFolder);
			fmu_.fmuEventDispatacher.addListener(this);
			
    		fmu_.init_1();
    		fmu_.init_2(unzipFolder);
    		fmu_.init_3();
    		
		} else if (data.equals("run")) {
			
			
			
			fmu_.run();
		}

	}

    
    public void onResultEvent(ResultEvent event) {
    	
    	event.resultItem.string = "";
    	Gson gson = GsonController.getInstance().getGson();
    	
    	String jsonString = gson.toJson(event);
    	sendMessage(jsonString);
    }
   
    public void onMessageEvent(MessageEvent event) {
    	String jsonString = GsonController.getInstance().getGson().toJson(event);
    	sendMessage(jsonString);
    }
    
    public void onFMUstateEvent(FMUstateEvent event) {
    	String jsonString = GsonController.getInstance().getGson().toJson(event);
    	sendMessage(jsonString);
    }
    
    public void onInitializedEvent(InitializedEvent event) {
    	
    	String jsonString = GsonController.getInstance().getGson().toJson(event);
    	sendMessage(jsonString);
    }
    

	
	public void onClose(int closeCode, String message) {
		// Remove ChatWebSocket in the global list of ChatWebSocket
		// instance.
		webSockets.remove(this);
	}
	
}
package com.sri.straylight.socketserver.controller;

import java.util.Properties;

import org.bushe.swing.event.annotation.EventSubscriber;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.socketserver.WebSocketConnectionEx;
import com.sri.straylight.socketserver.event.WebSocketConnectionNotify;


public class MainController extends AbstractController  {
	
	public static Logger logger = Log.getLogger("SocketServer");
	public static Properties properties;
	
	private SimulationController simulationController_;
	public static MainController instance;
	
	private WebSocketConnectionController webSocketConnectionController_;

	
	public MainController() {
		super(null);
	}
	 
	public void init() {
		
		simulationController_ = new SimulationController(this);
		simulationController_.init();
	
		webSocketConnectionController_ = new WebSocketConnectionController(this);
		webSocketConnectionController_.init();
		
		JettyServerController jettyServerController_ = new JettyServerController(this);
		jettyServerController_.init();
		jettyServerController_.start();
		
	}
	
	
	@EventSubscriber(eventClass=WebSocketConnectionNotify.class)
    public void onWebSocketConnectionNotify(WebSocketConnectionNotify event) {
		
		WebSocketConnectionEx webSocketConnection = event.getPayload();
		webSocketConnectionController_.setWebSocketConnection(webSocketConnection);

		}
    }


	//public void spawnNewInstance
	
	


	
//	//ResultEvent from FMU
//	@EventSubscriber(eventClass=ResultEvent.class)
//	private void onResultEvent_(ResultEvent event) {
//		serializeAndSend(event);
//	}
//	
//	private void serializeAndSend(JsonSerializable obj) {
//		
//		String jsonString = obj.toJson();
//		
//		EventBus.publish(
//				new MessageOut(this, jsonString)
//		);	
//		
//	}


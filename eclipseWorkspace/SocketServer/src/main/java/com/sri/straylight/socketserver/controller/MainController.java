package com.sri.straylight.socketserver.controller;

import java.util.ArrayList;
import java.util.Properties;

import org.bushe.swing.event.annotation.EventSubscriber;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import antlr.collections.impl.Vector;

import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.util.WorkerThreadAbstract;
import com.sri.straylight.socketserver.StraylightWebSocket;
import com.sri.straylight.socketserver.event.WebSocketConnectionNotify;


public class MainController extends AbstractController  {
	
	public static Logger logger = Log.getLogger("SocketServer");
//	public static Properties properties;
	

	public static MainController instance;
	
	private ArrayList<ConnectionBundle> connectionBundleList_;
	
	private WorkerMakeBundle workerMakeBundle_;
	
	public MainController() {
		super(null);
	}
	 
	public void init() {
		
		connectionBundleList_ = new ArrayList<ConnectionBundle>();
		
		JettyServerController jettyServerController_ = new JettyServerController(this);
		jettyServerController_.init();
	}
	
	
	@EventSubscriber(eventClass=WebSocketConnectionNotify.class)
    public void onWebSocketConnectionNotify(WebSocketConnectionNotify event) {
		
		StraylightWebSocket webSocketConnection = event.getPayload();
		
		int idx = connectionBundleList_.size();
		webSocketConnection.setIdx(idx);
		
		workerMakeBundle_ = new WorkerMakeBundle(this, webSocketConnection, idx);
		workerMakeBundle_.execute();
	}
	

	
	protected class WorkerMakeBundle extends WorkerThreadAbstract {
		
		private MainController parent_; 
		private StraylightWebSocket  webSocketConnection_;
		private int  idx_;
		
		 
		WorkerMakeBundle(MainController parent, StraylightWebSocket  webSocketConnection, int idx) {
			//setSyncObject(FMUcontrollerSync_);
			parent_ = parent;
			webSocketConnection_ = webSocketConnection;
			idx_ = idx;
			
		}
		
		@Override
		public void doIt_() {
			
			setName_("WorkerMakeBundle " + idx_);

			ConnectionBundle connectionBundle = new ConnectionBundle(parent_, webSocketConnection_, idx_);
			connectionBundleList_.add(connectionBundle);
			
			connectionBundle.init();
		}


		@Override
		public void doneIt_() {
			workerMakeBundle_ = null;
		}
	}
	
	
	
}
	
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


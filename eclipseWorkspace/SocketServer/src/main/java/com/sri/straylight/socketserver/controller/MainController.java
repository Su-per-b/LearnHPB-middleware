package com.sri.straylight.socketserver.controller;

import java.util.ArrayList;

import org.bushe.swing.event.annotation.EventSubscriber;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.util.WorkerThreadAbstract;
import com.sri.straylight.socketserver.event.WebSocketConnectionStateEvent;
import com.sri.straylight.socketserver.model.WebSocketConnectionState;


public class MainController extends AbstractController  {
	
	public static Logger logger = Log.getLogger("SocketServer");
	public static MainController instance;
	private ArrayList<ConnectionBundle> connectionBundleList_;
	private WorkerMakeBundle workerMakeBundle_;
	private WorkerTearDownBundle workerTearDownBundle_;
	
	public MainController() {
		super(null);
	}
	 
	public void init() {
		
		connectionBundleList_ = new ArrayList<ConnectionBundle>();
		
		JettyServerController jettyServerController_ = new JettyServerController(this);
		jettyServerController_.init();
	}
	
	

	@EventSubscriber(eventClass=WebSocketConnectionStateEvent.class)
    public void onWebSocketConnectionNotify(WebSocketConnectionStateEvent event) {
		
		
		WebSocketConnectionState state = event.getPayload();
		SocketController straylightWebSocket = event.getStronglyTypedSource();
		
		//int idx = straylightWebSocket.getIdx();
		
		if (state == WebSocketConnectionState.opened_new) {
			
			int idxNew = connectionBundleList_.size();
			straylightWebSocket.setIdx(idxNew);
			
			workerMakeBundle_ = new WorkerMakeBundle(this, straylightWebSocket);
			workerMakeBundle_.execute();
		}  else if (state == WebSocketConnectionState.closed) {
			
			workerTearDownBundle_ = new WorkerTearDownBundle(this, straylightWebSocket);
			workerTearDownBundle_.execute();
			
		}
	}
	
	
	protected class WorkerTearDownBundle extends WorkerThreadAbstract {
		
	//	private MainController parent_; 
		private SocketController straylightWebSocket_;
		private ConnectionBundle connectionBundle_;
		
		 
		WorkerTearDownBundle(MainController parent, SocketController  straylightWebSocket) {
			//setSyncObject(FMUcontrollerSync_);
			
		//	parent_ = parent;
			straylightWebSocket_ = straylightWebSocket;
			
			setSyncObject(connectionBundleList_);
			
		}
		
		@Override
		public void doIt_() {
			
			setName_("WorkerTearDownBundle " + straylightWebSocket_.getIdx());
			int idx = straylightWebSocket_.getIdx();
			
			connectionBundle_ = connectionBundleList_.get(idx);
			connectionBundle_.forceCleanup();

			connectionBundleList_.remove(connectionBundle_);
			

		}


		@Override
		public void doneIt_() {
			

			workerTearDownBundle_ = null;
		}
	}
	
	
	protected class WorkerMakeBundle extends WorkerThreadAbstract {
		
		private MainController parent_; 
		private SocketController  straylightWebSocket_;
		private ConnectionBundle connectionBundle_;
		
		 
		WorkerMakeBundle(MainController parent, SocketController  straylightWebSocket) {
			//setSyncObject(FMUcontrollerSync_);
			parent_ = parent;
			straylightWebSocket_ = straylightWebSocket;
			
			setSyncObject(connectionBundleList_);
			
		}
		
		@Override
		public void doIt_() {
			
			setName_("WorkerMakeBundle " + straylightWebSocket_.getIdx());

			connectionBundle_ = new ConnectionBundle(parent_, straylightWebSocket_);
			connectionBundleList_.add(connectionBundle_);
			
			connectionBundle_.init();
		}


		@Override
		public void doneIt_() {
			connectionBundle_.notifyClient();
			
			workerMakeBundle_ = null;
		}
	}
	
	
	
}



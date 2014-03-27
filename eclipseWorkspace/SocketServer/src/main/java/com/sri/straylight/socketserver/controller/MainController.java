package com.sri.straylight.socketserver.controller;

import java.util.HashMap;
import java.util.Vector;

import org.bushe.swing.event.annotation.EventSubscriber;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.sri.straylight.fmuWrapper.Controller.ThreadedFMUcontroller;
import com.sri.straylight.fmuWrapper.event.SessionControlEvent;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.util.WorkerThreadAbstract;
import com.sri.straylight.fmuWrapper.voManaged.SessionControl;
import com.sri.straylight.socketserver.event.WebSocketConnectionStateEvent;
import com.sri.straylight.socketserver.model.WebSocketConnectionState;


public class MainController extends AbstractController  {
	
	public static Logger logger = Log.getLogger("SocketServer");
	public static MainController instance;
//	private ArrayList<ConnectionBundle> connectionBundleList_;
	private WorkerMakeBundle workerMakeBundle_;
	private WorkerTearDownBundle workerTearDownBundle_;
	
	private HashMap<String, ConnectionBundle> sessionID2ConnectionMap_ = 
			new HashMap<String, ConnectionBundle>();
	
	
	private JettyServerController jettyServerController_;
	
	private Vector<String> sessionIdList_ = new Vector<String>();
	

	
	
	public MainController() {
		super(null);
	}
	 
	public void init() {
		
		//connectionBundleList_ = new ArrayList<ConnectionBundle>();
		
		jettyServerController_ = new JettyServerController(this);
		jettyServerController_.init();
	}
	
	
	public void registerConnectionBundle(String sessionID,
			ConnectionBundle connectionBundle_) {

		sessionID2ConnectionMap_.put(sessionID, connectionBundle_);
		
	}
	
	
	public ConnectionBundle unregisterConnectionBundle(String sessionID) {
		
		ConnectionBundle connectionBundle  = sessionID2ConnectionMap_.get(sessionID);
		sessionID2ConnectionMap_.remove(sessionID);
		
		
		return connectionBundle;
	}
	
	
	
	@EventSubscriber(eventClass=SessionControlEvent.class)
    public void onSessionControlEvent(SessionControlEvent event) {
		
		SessionControl sessionControl = event.getPayload();
		
		int idx = sessionControl.getIdx();
		if (0 == idx) {
			
    		Object source = event.getSource();
    		WebSocketConnectionController ws = (WebSocketConnectionController) source;
    		
    		String attachSessionID = ws.getSessionID();
			String hostSessionID = sessionControl.getValue();
			

    		if (hostSessionID.equals ("last")) {
    			
//    			int size = sessionIdList_.size();
    			hostSessionID = this.getLastSessionID(attachSessionID);

    		}
    		
    		System.out.println("hostSessionID: " + hostSessionID );
    		System.out.println("attachSessionID: " + attachSessionID );
    		
    		if (hostSessionID.equals("no_host_sessionID")) {
    			
    			return;
    		}
    		
			ConnectionBundle hostConenctionBundle = sessionID2ConnectionMap_.get(hostSessionID);
			ConnectionBundle attachConenctionBundle = sessionID2ConnectionMap_.get(attachSessionID);
			
			ThreadedFMUcontroller threadedFMUcontroller = hostConenctionBundle.getThreadedFMUcontroller();	
			attachConenctionBundle.setThreadedFMUcontroller_(threadedFMUcontroller);
    		
			
    		
		} else if (1 == idx) {
			
    		Object source = event.getSource();
    		WebSocketConnectionController ws = (WebSocketConnectionController) source;
    		
    		String sessionID = ws.getSessionID();
    		
			ConnectionBundle hostConenctionBundle = sessionID2ConnectionMap_.get(sessionID);
    		
			
		}
		
	}
	
	
	
    private String getLastSessionID(String attachSessionID ) {
    	
		int size = sessionIdList_.size();
		String sessionID = "";
		
		for (int i = size - 1; i >= 0; i--) {
			
		  sessionID = sessionIdList_.get(i);
		   
		   if (!sessionID.equals(attachSessionID)) {
			   
			   break;
		   }
		   
		 }

		
	   if (sessionID.equals(attachSessionID)) {
		   
		   sessionID = "no_host_sessionID";
	   }
		   
		return sessionID;
    }
    
    
	
	@EventSubscriber(eventClass=WebSocketConnectionStateEvent.class)
    public void onWebSocketConnectionNotify(WebSocketConnectionStateEvent event) {
		
		
		WebSocketConnectionState state = event.getPayload();
		StrayLightWebSocketHandler socketHandler = event.getStronglyTypedSource();
		
		String sessionID = socketHandler.getSessionID();
		//lastSessionID_ = sessionID;
		
		sessionIdList_.add(sessionID);
		
		//check for existing connection bundle
		
		if (sessionID2ConnectionMap_.containsKey(sessionID)) {
			
			
			if (state == WebSocketConnectionState.opened_new) {
				
				//reconnecting
				logger.info("reconnecting");
				
				
			}  else if (state == WebSocketConnectionState.closed) {
				
				

				
				workerTearDownBundle_ = new WorkerTearDownBundle(this, socketHandler);
				workerTearDownBundle_.execute();
				 
			} else if (state == WebSocketConnectionState.closeRequested) {
				
				//workerTearDownBundle_ = new WorkerTearDownBundle(this, socketHandler);
				//workerTearDownBundle_.execute();

			}
			
			
		} else {
			
			
			if (state == WebSocketConnectionState.opened_new) {
				
				//int idxNew = connectionBundleList_.size();
				//socketHandler.setIdx(idxNew);
				
				workerMakeBundle_ = new WorkerMakeBundle(this, socketHandler);
				workerMakeBundle_.execute();
				
				
			}  else if (state == WebSocketConnectionState.closed) {
				

				throw new RuntimeException("WebSocketConnectionState out of sync - sessionID not in sessionID2ConnectionMap_ ");
				
				
			}
			
		}
		
		

	}
	
	
	protected class WorkerTearDownBundle extends WorkerThreadAbstract {
		
		private MainController parent_; 
		private StrayLightWebSocketHandler socketHandler_;
		
		 
		WorkerTearDownBundle(MainController parent, StrayLightWebSocketHandler socketHandler) {
			setSyncObject(socketHandler);
			
			parent_ = parent;
			socketHandler_ = socketHandler;
			
		}
		
		@Override
		public void doIt_() {
			
			String sessionID  = socketHandler_.getSessionID();
			setName_("WorkerTearDownBundle sessionID: " + sessionID);

		
			ConnectionBundle connectionBundle = parent_.unregisterConnectionBundle(sessionID);
			connectionBundle.forceCleanup();

			sessionID2ConnectionMap_.remove(sessionID);
		}


		@Override
		public void doneIt_() {
			workerTearDownBundle_ = null;
		}
	}
	
	
	protected class WorkerMakeBundle extends WorkerThreadAbstract {
		
		private MainController parent_; 
		private StrayLightWebSocketHandler  socketHandler_;
		private ConnectionBundle connectionBundle_;
		
		 
		WorkerMakeBundle(MainController parent, StrayLightWebSocketHandler socketHandler) {
			setSyncObject(socketHandler);
			
			parent_ = parent;
			socketHandler_ = socketHandler;
			
			
		}
		
		@Override
		public void doIt_() {
			
			String sessionID  = socketHandler_.getSessionID();
			setName_("WorkerMakeBundle sessionID: " + sessionID);
			
			
			connectionBundle_ = new ConnectionBundle(parent_, socketHandler_, sessionID);
			
			parent_.registerConnectionBundle(sessionID, connectionBundle_);
			connectionBundle_.init();
			
		}


		@Override
		public void doneIt_() {
			connectionBundle_.notifyClient();
			
			workerMakeBundle_ = null;
		}
	}


	public void stop() {
		jettyServerController_.stop();
		
	}






	
	
	
}



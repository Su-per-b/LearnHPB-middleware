package com.sri.straylight.socketserver.controller;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.fmuWrapper.event.SimStateServerNotify;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voManaged.SimStateServer;
import com.sri.straylight.socketserver.FmuConnect;

public class SimulationController extends AbstractController  {

	private FmuConnect fmuConnect_;

	public void start() {
    	fmuConnect_ = new FmuConnect();
    	fmuConnect_.connect();	
	}
	
	
	/**
	 * On sim state notify.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateServerNotify.class)
    public void onSimStateNotify(SimStateServerNotify event) {
		
		
		SimStateServer simStateServer = event.getPayload();
		
		
	//	switch (simStateServer) {
		//	case simStateServer_1_connect_completed:
		        //	requestStateChange_(SimStateNative.level_2_xmlParse_requested);
		        
			//	break;

		//}
		
	}
	
	

	
	
	
}

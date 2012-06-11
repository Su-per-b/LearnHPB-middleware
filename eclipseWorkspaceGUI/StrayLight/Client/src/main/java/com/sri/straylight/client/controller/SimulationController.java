package com.sri.straylight.client.controller;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.action.InitAction;
import com.sri.straylight.client.event.action.MenuEvent_About_Help;
import com.sri.straylight.client.event.action.RunSimulationAction;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.client.model.Config;
import com.sri.straylight.client.model.FmuConnectLocal;
import com.sri.straylight.client.model.FmuConnectRemote;
import com.sri.straylight.client.model.IFmuConnect;
import com.sri.straylight.fmuWrapper.event.FMUeventListener;
import com.sri.straylight.fmuWrapper.event.FMUstateEvent;
import com.sri.straylight.fmuWrapper.event.InitializedEvent;
import com.sri.straylight.fmuWrapper.event.MessageEvent;
import com.sri.straylight.fmuWrapper.event.ResultEvent;
import com.sri.straylight.fmuWrapper.voNative.State;




public class SimulationController extends AbstractController implements FMUeventListener {

	private IFmuConnect fmuConnect_;
	
    private Config configModel = new Config();
    
    
	public SimulationController (AbstractController parentController) {
        super(parentController);
	}
	

    public void init() {
    	
    	


    }
    
	
	@EventSubscriber(eventClass=InitAction.class)
    public void onInitAction(InitAction event) {
    	
    	switch (configModel.connectTo) {
    	
		case connecTo_localhost :
			fmuConnect_ = new FmuConnectRemote("localhost");
			break;
		case connecTo_straylightsim_com :
			fmuConnect_ = new FmuConnectRemote("wintermute.straylightsim.com");
			break;
		case connecTo_file :
			fmuConnect_ = new FmuConnectLocal();
			break;
    	}

	
    	fmuConnect_.init(this);
    }
    
	@EventSubscriber(eventClass=RunSimulationAction.class)
    public void onRunSimulationAction(RunSimulationAction event) {
		fmuConnect_.run();
	}
	
	
    
	public void onResultEvent(ResultEvent event) {
		EventBus.publish(event);
	}
	
    public void onMessageEvent(MessageEvent event) {
    	EventBus.publish(event);
    }
    
    public void onFMUstateEvent(FMUstateEvent event) {
    	EventBus.publish(event);
    }

    
    
    public void onInitializedEvent(InitializedEvent event) {
    	EventBus.publish(event);
    }
    
}
    
    

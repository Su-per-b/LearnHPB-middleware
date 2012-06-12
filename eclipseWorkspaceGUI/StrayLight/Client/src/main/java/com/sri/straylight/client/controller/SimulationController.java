package com.sri.straylight.client.controller;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.action.InitAction;
import com.sri.straylight.client.event.action.LoadAction;
import com.sri.straylight.client.event.action.RunSimulationAction;
import com.sri.straylight.client.event.action.MetaDataChangeAction;
import com.sri.straylight.client.event.menu.About_Help;
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




public class SimulationController extends AbstractController  {

	private IFmuConnect fmuConnect_;
	
    private Config configModel_;
    
    
	public SimulationController (AbstractController parentController, Config configModel) {
        super(parentController);
        configModel_ = configModel;
	}
	



	@EventSubscriber(eventClass=LoadAction.class)
    public void onInitAction(LoadAction event) {
    	
    	switch (configModel_.connectTo) {
    	
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

	
    	fmuConnect_.load();
    }
	
    
	@EventSubscriber(eventClass=InitAction.class)
    public void onInitAction(InitAction event) {
    	fmuConnect_.init();
    }
	
    
	@EventSubscriber(eventClass=MetaDataChangeAction.class)
    public void onRunSimulationAction(MetaDataChangeAction event) {
		fmuConnect_.setMetaData(event.payload);
	}
	
	
	@EventSubscriber(eventClass=RunSimulationAction.class)
    public void onRunSimulationAction(RunSimulationAction event) {
		fmuConnect_.run();
	}
	

    
}
    
    

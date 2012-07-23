package com.sri.straylight.client.controller;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.InputChangeRequest;
import com.sri.straylight.client.event.SimStateNotify;
import com.sri.straylight.client.event.SimStateRequest;
import com.sri.straylight.client.framework.AbstractController;
import com.sri.straylight.client.model.ConfigClient;
import com.sri.straylight.client.model.SimStateClient;
import com.sri.straylight.fmuWrapper.voManaged.SimStateServer;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;




public class SimulationController extends AbstractController  {

	private IFmuConnect fmuConnect_;
	
    private ConfigClient configModel_;
    
    private SimStateClient simulationStateClient_ = 
    		SimStateClient.level_0_uninitialized;
    
    
	public SimulationController (AbstractController parentController, ConfigClient configModel) {
        super(parentController);
        configModel_ = configModel;
        
	}
	


	private void connect_() {
		
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
    	
    	fmuConnect_.connect();
    }
	
	
	
	
	@EventSubscriber(eventClass=InputChangeRequest.class)
    public void onInputChangeRequest(InputChangeRequest event) {
		double v = event.value;
		fmuConnect_.changeInput(event.idx, event.value);
	}
	
	
	@EventSubscriber(eventClass=SimStateNotify.class)
    public void onSimStateNotify(SimStateNotify event) {
		simulationStateClient_ = event.getPayload();
	}
	
	
	@EventSubscriber(eventClass=SimStateRequest.class)
    public void onSimStateRequest(SimStateRequest event) {
		SimStateClient state = event.getPayload();
		
		if (simulationStateClient_ == state)
			return;
			
		switch (state) {
			case level_1_connect_requested :
				connect_();
				break;
			case level_2_xmlParse_requested :
				fmuConnect_.xmlParse();
				break;
			case level_3_init_requested :
				fmuConnect_.init();
				break;
			case level_4_run_requested :
				fmuConnect_.run();
				break;
			case level_5_stop_requested:
				//fmuConnect_.stop();
				fmuConnect_.requestStateChange(SimStateNative.simStateNative_5_stop_requested);
				break;
			case level_5_step_requested:
				fmuConnect_.requestStateChange(SimStateNative.simStateNative_5_step_requested);
				break;
			case level_6_reset_requested:
				fmuConnect_.requestStateChange(SimStateNative.simStateNative_7_reset_requested);
				break;
		}
    }
	
	

	
	
	


    
}
    
    

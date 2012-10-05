package com.sri.straylight.client.controller;

import java.util.Vector;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.InputChangeRequest;
import com.sri.straylight.client.event.ScalarValueChangeRequest;
import com.sri.straylight.client.event.SimStateNotify;
import com.sri.straylight.client.event.SimStateRequest;
import com.sri.straylight.client.model.ClientConfig;
import com.sri.straylight.client.model.ClientConfigXML;
import com.sri.straylight.client.model.SimStateClient;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;




// TODO: Auto-generated Javadoc
/**
 * The Class SimulationController.
 */
public class SimulationController extends AbstractController  {

	/** The fmu connect_. */
	private IFmuConnect fmuConnect_;
	
    /** The config model_. */
    private ClientConfig configModel_;
    
    /** The simulation state client_. */
    private SimStateClient simulationStateClient_ = 
    		SimStateClient.level_0_uninitialized;
    
    
	/**
	 * Instantiates a new simulation controller.
	 *
	 * @param parentController the parent controller
	 * @param configModel the config model
	 */
	public SimulationController (AbstractController parentController, ClientConfig configModel) {
        super(parentController);
        configModel_ = configModel;
        
        
        if (configModel_.autoConnectFlag) {
        	requestStateChange_(SimStateClient.level_1_connect_requested);
        }
	}
	


	/**
	 * Connect_.
	 */
	private void connect_() {
		
		
		if (configModel_.connectTo == null) {
		    throw new IllegalArgumentException("configModel_.connectTo");
		}

		
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
	
	
	
	
	/**
	 * On input change request.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ScalarValueChangeRequest.class)
    public void onInputChangeRequest(ScalarValueChangeRequest event) {
		//double v = event.value;
		
		
		Vector<ScalarValueRealStruct> list = event.getPayload();
		
		
		fmuConnect_.changeScalarValues(list);
		
	}
	
	
	/**
	 * On sim state notify.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=SimStateNotify.class)
    public void onSimStateNotify(SimStateNotify event) {
		simulationStateClient_ = event.getPayload();
		
		

        
		switch (simulationStateClient_) {
			case level_1_connect_completed:
		        if (configModel_.autoParseXMLFlag) {
		        	requestStateChange_(SimStateClient.level_2_xmlParse_requested);
		        }
				break;
			case level_2_xmlParse_completed :
		        if (configModel_.autoInitFlag) {
		        	requestStateChange_(SimStateClient.level_3_init_requested);
		        }
				break;
		}
		
	}
	
	
	/**
	 * Request state change_.
	 *
	 * @param state_arg the state_arg
	 */
	private void requestStateChange_(SimStateClient state_arg)
	{
		EventBus.publish(
				new SimStateRequest(this, state_arg)
				);	
		
	}
	
	/**
	 * On sim state request.
	 *
	 * @param event the event
	 */
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
    
    

package com.sri.straylight.client.controller;

import java.util.Vector;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.ScalarValueChangeRequest;
import com.sri.straylight.client.event.SimStateClientNotify;
import com.sri.straylight.client.event.SimStateClientRequest;
import com.sri.straylight.client.model.ClientConfig;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;




// TODO: Auto-generated Javadoc
/**
 * The Class SimulationController.
 */
public class SimulationController extends BaseController  {

	private IFmuConnect fmuConnect_;
    private ClientConfig configModel_;
    private SimStateNative simStateNative_ = SimStateNative.simStateNative_0_uninitialized;
    
    
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
        	requestStateChange_(SimStateNative.simStateNative_1_connect_requested);
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
    	
    	
    	
    	try 
    	{
			fmuConnect_.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	
	
	
	/**
	 * On input change request.
	 *
	 * @param event the event
	 */
	@EventSubscriber(eventClass=ScalarValueChangeRequest.class)
    public void onInputChangeRequest(ScalarValueChangeRequest event) {
		Vector<ScalarValueRealStruct> list = event.getPayload();
		fmuConnect_.changeScalarValues(list);
	}
	
	
	@EventSubscriber(eventClass=SimStateClientRequest.class)
    public void onSimStateRequest(SimStateClientRequest event) {
		
		SimStateNative requestedState = event.getPayload();
		
		switch (requestedState) {
			case simStateNative_1_connect_requested :
				connect_();
				break;
			case simStateNative_2_xmlParse_requested :
				fmuConnect_.xmlParse();
				break;
			case simStateNative_4_run_requested :
				fmuConnect_.run();
				break;
			default:
				fmuConnect_.requestStateChange(requestedState);
		}
    }
	
	@EventSubscriber(eventClass=SimStateClientNotify.class)
    public void onSimStateNotify(SimStateClientNotify event) {
		
		simStateNative_ = event.getPayload();
		
		switch (simStateNative_) {
			case simStateNative_1_connect_completed:
		        if (configModel_.autoParseXMLFlag) {
		        	requestStateChange_(SimStateNative.simStateNative_2_xmlParse_requested);
		        }
				break;
			case simStateNative_2_xmlParse_completed :
		        if (configModel_.autoInitFlag) {
		        	requestStateChange_(SimStateNative.simStateNative_3_init_requested);
		        }
				break;
			default:
				break;
		}
	}
	
	

	private void requestStateChange_(SimStateNative simStateNative)
	{
		EventBus.publish(
				new SimStateClientRequest(this, simStateNative)
				);	
		
	}
	


	
	
}
    
    

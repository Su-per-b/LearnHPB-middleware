package com.sri.straylight.client.controller;

import java.io.File;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.sri.straylight.client.event.SimStateClientRequest;
import com.sri.straylight.client.model.ClientConfig;
import com.sri.straylight.client.util.FmuConnectionAbstract;
import com.sri.straylight.client.util.FmuConnectionLocal;
import com.sri.straylight.client.util.FmuConnectionRemote;
import com.sri.straylight.fmuWrapper.event.ConfigChangeRequest;
import com.sri.straylight.fmuWrapper.event.ScalarValueChangeRequest;
import com.sri.straylight.fmuWrapper.event.SimStateClientNotify;
import com.sri.straylight.fmuWrapper.event.SimStateNativeNotify;
import com.sri.straylight.fmuWrapper.framework.AbstractController;
import com.sri.straylight.fmuWrapper.model.FMUwrapperConfig;
import com.sri.straylight.fmuWrapper.voManaged.ScalarValueCollection;
import com.sri.straylight.fmuWrapper.voNative.ConfigStruct;
import com.sri.straylight.fmuWrapper.voNative.SimStateNative;


/**
 * The Class SimulationController.
 */
public class SimulationController extends BaseController  {

	private FmuConnectionAbstract fmuConnect_;
    private ClientConfig configModel_;
    private SimStateNative simStateNative_ = SimStateNative.simStateNative_0_uninitialized;
    private File fmuFile_;
    
    
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
        	requestStateChange(SimStateNative.simStateNative_1_connect_requested);
        }
	}
	
	
	public SimulationController(ClientConfig configModel) {
		super(null);
        configModel_ = configModel;
	}

	
	

	/**
	 * Connect_.
	 */
	private void connect_() {
		
		if (configModel_.connectTo == null) {
		    throw new IllegalArgumentException("configModel_.connectTo");
		}

    	switch (configModel_.connectTo) {
			case connectTo_localhost :
				fmuConnect_ = new FmuConnectionRemote("localhost");
				break;
			case connectTo_pfalco_global :
				fmuConnect_ = new FmuConnectionRemote("liquid.straylightsim.com");
				break;
			case connectTo_pfalco_local :
				fmuConnect_ = new FmuConnectionRemote("192.168.0.12");
				break;
			case connectTo_file :
				fmuConnect_ = new FmuConnectionLocal(fmuFile_);
				fmuConnect_.requestStateChange(SimStateNative.simStateNative_1_connect_requested  );
				break;
    	}
    }
	
	
	@EventSubscriber(eventClass = ConfigChangeRequest.class)
	public void onConfigChangeRequest(ConfigChangeRequest event) {

		ConfigStruct configStruct = event.getPayload();
		fmuConnect_.setConfig(configStruct);
	}
	
	
	//this event comes from the FMU engine
	@EventSubscriber(eventClass=SimStateClientNotify.class)
    public void onSimStateNotify(SimStateClientNotify event) {
		
		simStateNative_ = event.getPayload();
		
		switch (simStateNative_) {
			case simStateNative_1_connect_completed:
		        if (configModel_.autoParseXMLFlag) {
		        	requestStateChange(SimStateNative.simStateNative_2_xmlParse_requested);
		        }
				break;
			case simStateNative_2_xmlParse_completed :
		        if (configModel_.autoInitFlag) {
		        	requestStateChange(SimStateNative.simStateNative_3_init_requested);
		        }
				break;
			default:
				break;
		}
	}
	

	@EventSubscriber(eventClass = SimStateNativeNotify.class)
	public void onSimStateNativeNotify(SimStateNativeNotify event) {

		SimStateNative state = event.getPayload();
		
		//if (state == SimStateNative.simStateNative_0_uninitialized ) {
			
			//fmuConnect_.requestStateChange(SimStateNative.simStateNative_1_connect_requested);
		//} else {
			
			SimStateClientNotify newEvent = 
					new SimStateClientNotify(this,state);
			
			EventBus.publish(newEvent);
		//}
		
	}

	
	@EventSubscriber(eventClass=ScalarValueChangeRequest.class)
    public void onInputChangeRequest(ScalarValueChangeRequest event) {
		
		ScalarValueCollection collection = event.getPayload();
		fmuConnect_.setScalarValueCollection(collection);
	}
	
	
	private void requestStateChange(SimStateNative simStateNative) {
		
		if (simStateNative == SimStateNative.simStateNative_1_connect_requested ) {
			connect_();
		} else {
			fmuConnect_.requestStateChange(simStateNative);
		}
		
	}
	
	
	
	//this event comes from the GUI
	@EventSubscriber(eventClass=SimStateClientRequest.class)
    public void onSimStateClientRequest(SimStateClientRequest event) {
		SimStateNative requestedState = event.getPayload();
		requestStateChange(requestedState);
    }


	public void setFMUfile(File fmuFile) {
		fmuFile_ = fmuFile;
		
		FMUwrapperConfig.fmuFolderAbsolutePathOverride = fmuFile;
	}
	


	
	
}
    
    

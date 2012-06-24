package com.sri.straylight.client.event;

import java.util.EventObject;

import com.sri.straylight.client.model.SimStateClient;

public class SimStateRequest extends EventObject {

	
	private static final long serialVersionUID = 1L;

	private SimStateClient payload_;
	
    //here's the constructor
    public SimStateRequest(Object source, SimStateClient newState) {
        super(source);
        payload_ = newState;
    }
    
    public SimStateClient getPayload() {
    	return payload_;
    }
    
}

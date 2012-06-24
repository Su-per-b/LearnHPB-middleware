package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;


import com.sri.straylight.fmuWrapper.voManaged.SimStateServer;

public class SimStateServerNotify extends EventObject {

	
	private static final long serialVersionUID = 1L;

	private SimStateServer payload_;
	
    //here's the constructor
    public SimStateServerNotify(Object source, SimStateServer newState) {
        super(source);
        payload_ = newState;
    }
    
    public SimStateServer getPayload() {
    	return payload_;
    }
    
}

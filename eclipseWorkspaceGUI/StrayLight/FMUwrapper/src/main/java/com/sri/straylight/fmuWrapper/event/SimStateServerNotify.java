package com.sri.straylight.fmuWrapper.event;

import java.util.EventObject;


import com.sri.straylight.fmuWrapper.voManaged.SimStateServer;

// TODO: Auto-generated Javadoc
/**
 * The Class SimStateServerNotify.
 */
public class SimStateServerNotify extends EventObject {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The payload_. */
	private SimStateServer payload_;
	
    //here's the constructor
    /**
     * Instantiates a new sim state server notify.
     *
     * @param source the source
     * @param newState the new state
     */
    public SimStateServerNotify(Object source, SimStateServer newState) {
        super(source);
        payload_ = newState;
    }
    
    /**
     * Gets the payload.
     *
     * @return the payload
     */
    public SimStateServer getPayload() {
    	return payload_;
    }
    
}

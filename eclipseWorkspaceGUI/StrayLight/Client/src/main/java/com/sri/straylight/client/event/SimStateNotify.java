package com.sri.straylight.client.event;

import java.util.EventObject;

import com.sri.straylight.client.model.SimStateClient;

// TODO: Auto-generated Javadoc
/**
 * The Class SimStateNotify.
 */
public class SimStateNotify extends EventObject {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The payload_. */
	private SimStateClient payload_;
	
    //here's the constructor
    /**
     * Instantiates a new sim state notify.
     *
     * @param source the source
     * @param newState the new state
     */
    public SimStateNotify(Object source, SimStateClient newState) {
        super(source);
        payload_ = newState;
    }
    
    /**
     * Gets the payload.
     *
     * @return the payload
     */
    public SimStateClient getPayload() {
    	return payload_;
    }
    
}

package com.sri.straylight.client.event;

import java.util.EventObject;
import java.util.Vector;

import com.sri.straylight.fmuWrapper.voNative.ScalarValueRealStruct;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalarValueChangeRequest.
 */
public class ScalarValueChangeRequest extends EventObject {
	
	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The payload_. */
	private Vector<ScalarValueRealStruct> payload_;
    //here's the constructor
    /**
     * Instantiates a new scalar value change request.
     *
     * @param source the source
     * @param scalarValueList the scalar value list
     */
    public ScalarValueChangeRequest(Object source, Vector<ScalarValueRealStruct> scalarValueList) {
        super(source);
        
        payload_ = scalarValueList;
        
    }
    
    /**
     * Gets the payload.
     *
     * @return the payload
     */
    public Vector<ScalarValueRealStruct> getPayload() {
    	
    	return payload_;
    }
    
    
    
}

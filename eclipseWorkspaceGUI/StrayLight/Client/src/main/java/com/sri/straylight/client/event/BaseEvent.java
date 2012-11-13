package com.sri.straylight.client.event;

import java.util.EventObject;

// TODO: Auto-generated Javadoc
/**
 * An event with an Object payload.
 * <p>
 * Instantiate an event, put an optional payload into it fire it with the API of a controller.
 *
 * @param <PAYLOAD> the generic type
 * @author Christian Bauer
 */
public class BaseEvent<T> extends EventObject {

    /** The payload. */
    private T payload_;
    

    private static final long serialVersionUID = 1L;
    

    
    public BaseEvent( Object source ) {
        super( source );
    }
    

    public BaseEvent(Object source, T payload) {
        super( source );
        this.payload_ = payload;
    }


	/**
     * Gets the payload.
     *
     * @return the payload
     */
    public T getPayload() {
        return payload_;
    }
    

    /**
     * Sets the payload.
     *
     * @param payload the new payload
     */
    public void setPayload(T payload) {
    	payload_ = payload;
    }

    public String toJson() {
    	
    	return "toJson";
    }

    
    
    
}

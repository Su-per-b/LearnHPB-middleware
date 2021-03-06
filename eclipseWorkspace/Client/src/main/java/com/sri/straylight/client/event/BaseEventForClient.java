package com.sri.straylight.client.event;

import java.util.EventObject;

import org.bushe.swing.event.EventBus;

// TODO: Auto-generated Javadoc
/**
 * An event with an Object payload.
 * <p>
 * Instantiate an event, put an optional payload into it fire it with the API of a controller.
 *
 * @param <PAYLOAD> the generic type
 * @author Christian Bauer
 */
public class BaseEventForClient<T> extends EventObject {

    /** The payload. */
    protected T payload_;
    

    private static final long serialVersionUID = 1L;
    

    
    public BaseEventForClient( Object source ) {
        super( source );
    }
    

    public BaseEventForClient(Object source, T payload) {
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


	public void fire() {
		EventBus.publish(this);
	}
    
}
